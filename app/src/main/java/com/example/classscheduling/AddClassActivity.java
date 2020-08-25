package com.example.classscheduling;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AddClassActivity extends AppCompatActivity {

    ProgressDialog progressDialog;


    private EditText name;
    private EditText unit;
    private EditText day1;
    private EditText day1_start;
    private EditText day1_end;
    private EditText day2;
    private EditText day2_start;
    private EditText day2_end;
    private Button add;

    String name_;
    String unit_;
    String day1_;
    String day1_start_;
    String day1_end_;
    String day2_;
    String day2_start_;
    String day2_end_;
    DataGenerator dataGenerator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        dataGenerator=new DataGenerator();
        setupViews();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name_=name.getText().toString().trim();
                unit_=unit.getText().toString().trim();
                day1_=day1.getText().toString().trim();
                day1_start_=day1_start.getText().toString().trim();
                day1_end_=day1_end.getText().toString().trim();
                day2_=day2.getText().toString().trim();
                day2_start_=day2_start.getText().toString().trim();
                day2_end_=day2_end.getText().toString().trim();

                if(!unit_.isEmpty()) {
                    if (Integer.parseInt(unit_) == 3) {
                        if (!name_.isEmpty() && !day1_.isEmpty() && !day1_start_.isEmpty() && !day1_end_.isEmpty() && !day2_.isEmpty() && !day2_start_.isEmpty() && !day2_end_.isEmpty()) {

                            UploadClass(name_,Integer.parseInt(unit_), day1_, Integer.parseInt(day1_start_),Integer.parseInt( day1_end_), day2_, Integer.parseInt(day2_start_),Integer.parseInt(day2_end_));
                        } else {
                            Toast.makeText(AddClassActivity.this, "همه فیلد ها را تکمیل کنید", Toast.LENGTH_SHORT).show();
                        }
                    } else if (Integer.parseInt(unit_) == 2) {
                        if (!name_.isEmpty() && !day1_.isEmpty() && !day1_start_.isEmpty() && !day1_end_.isEmpty()) {
                            UploadClass(name_,Integer.parseInt(unit_), day1_, Integer.parseInt(day1_start_),Integer.parseInt( day1_end_), "", 0,0);

                        } else {
                            Toast.makeText(AddClassActivity.this, "تعداد واحد ها صحیح نیست", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(AddClassActivity.this, "تعداد واحد ها تکمیل نشده", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void setupViews() {
        name=findViewById(R.id.name_et);
        unit=findViewById(R.id.unit_et);
        day1=findViewById(R.id.day1_et);
        day1_start=findViewById(R.id.day1_start_et);
        day1_end=findViewById(R.id.day1_end_et);
        day2=findViewById(R.id.day2_et);
        day2_start=findViewById(R.id.day2_start_et);
        day2_end=findViewById(R.id.day2_end_et);
        add=findViewById(R.id.add_class_btn);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading..");
        progressDialog.setMessage("please wait . . . ");
    }

    private void UploadClass(String name,int unit,String day1,int day1_start,int day1_end,String day2,int day2_start,int day2_end){
        progressDialog.show();


        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UploadInterface.url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)
                .build();

        UploadInterface uploadInterface= retrofit.create(UploadInterface.class);


        Call<String> call = uploadInterface.addClass(name,unit,day1,day1_start,day1_end,day2,day2_start,day2_end);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Response", response.body().toString());
                progressDialog.dismiss();
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        Toast.makeText(AddClassActivity.this, "Class Uploaded Successfully!!", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AddClassActivity.this, "ERROR: " + t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(t.getMessage());
            }
        });
    }
}
