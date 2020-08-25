package com.example.classscheduling;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApiService {

    private static final String TAG = "ApiService";
    private Context context;

    List<Classes> foods_list = new ArrayList<>();


    public ApiService(Context context) {
        this.context = context;
    }

    public void getFoods(final onFoodsReceived onFoodsReceived) {
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, "http://192.168.56.1/classScheduling/getClasses.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Classes> foods = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    Classes food = new Classes();
                    try {

                        JSONObject jsonObject = response.getJSONObject(i);
                        food.setName(jsonObject.getString("name"));
                        food.setUnit(jsonObject.getInt("unit"));

                        food.setDay1(jsonObject.getString("day1"));
                        food.setTime1_start(jsonObject.getInt("day1_start"));
                        food.setTime1_end(jsonObject.getInt("day1_end"));

                        food.setDay2(jsonObject.getString("day2"));
                        food.setTime2_start(jsonObject.getInt("day2_start"));
                        food.setTime2_end(jsonObject.getInt("day2_end"));

                        foods.add(food);
                        foods_list.addAll(foods);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                onFoodsReceived.OnReceived(foods);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error in Response: " + error);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(request);
    }

    public interface onFoodsReceived {
        void OnReceived(List<Classes> foods);
    }

    public List<Classes> getList(){
        return foods_list;
    }

}
