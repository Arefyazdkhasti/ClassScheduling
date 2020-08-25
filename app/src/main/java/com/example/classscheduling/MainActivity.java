package com.example.classscheduling;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_POSSIBLE_UNITS = 20;
    private static int index = 0;
    private static int counter=0;

    DataGenerator dataGenerator = new DataGenerator();
    ApiService apiService = new ApiService(this);
    List<Classes> arr = new ArrayList<>();

    LinearLayout linearLayout;
    RecyclerView recyclerView;
    Button show_result_button;
    Button show_all_button;
    FloatingActionButton floatingActionButton;
    TextView maxUnit;
    TextView maxPossibleUnit;
    ProgressBar progressBar;

    List<Classes> class_list = new ArrayList<>();
    ClassAdapter classAdapter;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();

       /* ApiService apiService = new ApiService(this);
        apiService.getFoods(new ApiService.onFoodsReceived() {
            @Override
            public void OnReceived(List<Classes> foods) {
                MainActivity.this.class_list = foods;
                recyclerView = findViewById(R.id.recycler_view);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
                classAdapter = new ClassAdapter(MainActivity.this);
                classAdapter.setClasses(foods);
                arr = setUpClasses(foods);
            }
        });


        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
        classAdapter = new ClassAdapter(MainActivity.this);
        recyclerView.setAdapter(classAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        classAdapter.setClasses(arr);*/

        show_result_button.setOnClickListener(v -> {
            linearLayout.setVisibility(View.VISIBLE);
            setupRecyclerView();
        });

        show_all_button.setOnClickListener(v -> {
            if (linearLayout.getVisibility() == View.VISIBLE) {
                linearLayout.setVisibility(View.GONE);
            }
            setupRecyclerViewAll();
        });

        floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.add_class)));

        floatingActionButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddClassActivity.class)));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        setupRecyclerViewAll();
        setupRecyclerView();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onStart() {
        super.onStart();
        setupRecyclerViewAll();
        setupRecyclerView();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        setupRecyclerViewAll();
        setupRecyclerView();
    }

    public void setupViews() {
        show_result_button = findViewById(R.id.btn_show_result);
        show_all_button = findViewById(R.id.btn_show_all);
        floatingActionButton = findViewById(R.id.float_action_btn);
        linearLayout = findViewById(R.id.most_unit_layer);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        maxUnit = findViewById(R.id.possible_unit);
        maxPossibleUnit = findViewById(R.id.all_possible_unit);
        maxPossibleUnit.setText(Integer.toString(MAX_POSSIBLE_UNITS));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setupRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
        classAdapter = new ClassAdapter(MainActivity.this);
        recyclerView.setAdapter(classAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        classAdapter.setClasses(setUpClasses(class_list));
    }

    public void setupRecyclerViewAll() {
        ApiService apiService = new ApiService(this);
        apiService.getFoods(new ApiService.onFoodsReceived() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void OnReceived(List<Classes> foods) {
                MainActivity.this.class_list = foods;
                recyclerView = findViewById(R.id.recycler_view);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
                classAdapter = new ClassAdapter(MainActivity.this);
                recyclerView.setAdapter(classAdapter);
                recyclerView.setLayoutManager(linearLayoutManager);
                classAdapter.setClasses(foods);
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
        /*recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
        classAdapter = new ClassAdapter(MainActivity.this);
        recyclerView.setAdapter(classAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        classAdapter.setClasses(getClasses());*/
    }


    private static int findLastNonConflictingJob_forDay1(List<Classes> jobs, int n) {
        // search space
        int low = 0;
        int high = n;

        // iterate till search space is not exhausted
        while (low <= high) {
            int mid = (low + high) / 2;
            if (jobs.get(mid).getTime1_end() <= jobs.get(n).getTime1_start()) {
                if (jobs.get(mid + 1).getTime1_end() <= jobs.get(n).getTime1_start()) {
                    low = mid + 1;
                } else {
                    return mid;
                }
            } else {
                high = mid - 1;
            }
        }

        // return negative index if no non-conflicting job is found
        return -1;
    }

    // Function to print the non-overlapping jobs involved in maximum profit
    // using Dynamic Programming
    @RequiresApi(api = Build.VERSION_CODES.N)
    private static List<Classes> findMaxProfitJobs_forDay1(List<Classes> jobs) {
        // sort jobs in increasing order of their finish times
        //   jobs.sort((x, y) -> x.getTime1_end() - y.getTime1_end());
        jobs.sort(Comparator.comparingInt(Classes::getTime1_end));

        // get number of jobs
        int n = jobs.size();

        // maxProfit[i] stores the maximum profit possible for the first i jobs and
        // tasks[i] stores the index of jobs involved in the maximum profit
        int[] maxProfit = new int[n];
        List<List<Integer>> tasks = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            tasks.add(i, new ArrayList<>());
        }

        // initialize maxProfit[0] and tasks[0] with the first job
        maxProfit[0] = jobs.get(0).getUnit();
        tasks.get(0).add(0);

        // fill tasks[] and maxProfit[] in bottom-up manner
        for (int i = 1; i < n; i++) {
            // find the index of last non-conflicting job with current job
            int index = findLastNonConflictingJob_forDay1(jobs, i);

            // include the current job with its non-conflicting jobs
            int currentProfit = jobs.get(i).getUnit();
            if (index != -1) {
                currentProfit += maxProfit[index];
            }

            // if including the current job leads to the maximum profit so far
            if (maxProfit[i - 1] < currentProfit) {
                maxProfit[i] = currentProfit;

                if (index != -1) {
                    tasks.set(i, tasks.get(index));
                }
                tasks.get(i).add(i);
            }

            // excluding the current job leads to the maximum profit so far
            else {
                tasks.set(i, tasks.get(i - 1));
                maxProfit[i] = maxProfit[i - 1];
            }
        }

        // maxProfit[n-1] stores the maximum profit
        System.out.println("The maximum unit is " + maxProfit[n - 1]);

        //list for save lessons that can be chosen
        List<Classes> exact_day_possible_classes = new ArrayList<>();

        // tasks[n-1] stores the index of jobs involved in the maximum profit
        System.out.println("The jobs involved in the maximum profit are: ");
        for (int i : tasks.get(n - 1)) {
            System.out.println(jobs.get(i).getName() + " " + jobs.get(i).getDay1());
            exact_day_possible_classes.add(jobs.get(i));
        }
        //return final list
        return exact_day_possible_classes;


    }

    private static int findLastNonConflictingJob_forDay2(List<Classes> jobs, int n) {
        // search space
        int low = 0;
        int high = n;

        // iterate till search space is not exhausted
        while (low <= high) {
            int mid = (low + high) / 2;
            if (jobs.get(mid).getTime2_end() <= jobs.get(n).getTime2_start()) {
                if (jobs.get(mid + 1).getTime2_end() <= jobs.get(n).getTime2_start()) {
                    low = mid + 1;
                } else {
                    return mid;
                }
            } else {
                high = mid - 1;
            }
        }

        // return negative index if no non-conflicting job is found
        return -1;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static List<Classes> findMaxProfitJobs_forDay2(List<Classes> jobs) {
        // sort jobs in increasing order of their finish times
        //   jobs.sort((x, y) -> x.getTime1_end() - y.getTime1_end());
        jobs.sort(Comparator.comparingInt(Classes::getTime1_end));

        // get number of jobs
        int n = jobs.size();

        // maxProfit[i] stores the maximum profit possible for the first i jobs and
        // tasks[i] stores the index of jobs involved in the maximum profit
        int[] maxProfit = new int[n];
        List<List<Integer>> tasks = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            tasks.add(i, new ArrayList<>());
        }

        // initialize maxProfit[0] and tasks[0] with the first job
        maxProfit[0] = jobs.get(0).getUnit();
        tasks.get(0).add(0);

        // fill tasks[] and maxProfit[] in bottom-up manner
        for (int i = 1; i < n; i++) {
            // find the index of last non-conflicting job with current job
            int index = findLastNonConflictingJob_forDay2(jobs, i);

            // include the current job with its non-conflicting jobs
            int currentProfit = jobs.get(i).getUnit();
            if (index != -1) {
                currentProfit += maxProfit[index];
            }

            // if including the current job leads to the maximum profit so far
            if (maxProfit[i - 1] < currentProfit) {
                maxProfit[i] = currentProfit;

                if (index != -1) {
                    tasks.set(i, tasks.get(index));
                }
                tasks.get(i).add(i);
            }

            // excluding the current job leads to the maximum profit so far
            else {
                tasks.set(i, tasks.get(i - 1));
                maxProfit[i] = maxProfit[i - 1];
            }
        }

        // maxProfit[n-1] stores the maximum profit
        System.out.println("The maximum unit is " + maxProfit[n - 1]);

        //list for save lessons that can be chosen
        List<Classes> exact_day_possible_classes = new ArrayList<>();

        // tasks[n-1] stores the index of jobs involved in the maximum profit
        System.out.println("The jobs involved in the maximum profit are: ");
        for (int i : tasks.get(n - 1)) {
            System.out.println(jobs.get(i).getName() + " " + jobs.get(i).getDay2());
            exact_day_possible_classes.add(jobs.get(i));
        }
        //return final list
        return exact_day_possible_classes;
    }

    private List<Classes> getClasses() {
        return dataGenerator.getClasses();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Classes> setUpClasses(List<Classes> arr) {

        List<Classes> final_classes = new ArrayList<>();
        List<String> final_classes_name = new ArrayList<>();

        //final list to show!
        List<Classes> possible_classes_final_result = new ArrayList<>();


        List<Classes> arr_day_saturday_1 = new ArrayList<>();
        List<Classes> arr_day_saturday_2 = new ArrayList<>();
        List<Classes> sat1 = new ArrayList<>();
        List<Classes> sat2 = new ArrayList<>();

        List<Classes> arr_day_sunday_1 = new ArrayList<>();
        List<Classes> arr_day_sunday_2 = new ArrayList<>();
        List<Classes> sun1 = new ArrayList<>();
        List<Classes> sun2 = new ArrayList<>();

        List<Classes> arr_day_monday_1 = new ArrayList<>();
        List<Classes> arr_day_monday_2 = new ArrayList<>();
        List<Classes> mon1 = new ArrayList<>();
        List<Classes> mon2 = new ArrayList<>();

        List<Classes> arr_day_tuesday_1 = new ArrayList<>();
        List<Classes> arr_day_tuesday_2 = new ArrayList<>();
        List<Classes> tuse1 = new ArrayList<>();
        List<Classes> tuse2 = new ArrayList<>();

        List<Classes> arr_day_wednesday_1 = new ArrayList<>();
        List<Classes> arr_day_wednesday_2 = new ArrayList<>();
        List<Classes> wen1 = new ArrayList<>();

        List<Classes> wen2 = new ArrayList<>();
        int maxUnits = 0;

//for saturday
        System.out.println("\nSaturday:\n");
        for (Classes value : arr) {
            if (value.getDay1().equals("شنبه")) {
                arr_day_saturday_1.add(value);
            } else if (value.getDay2().equals("شنبه")) {
                arr_day_saturday_2.add(value);
            }
        }
        for (Classes classes : arr_day_saturday_1) {
            System.out.println("------------------------------"+classes.getName());
        }

        for (Classes classes : arr_day_saturday_2) {
            System.out.println("------------------------------"+classes.getName());
        }

        //find the max possible class in saturday
        //for both arrays
        if (!arr_day_saturday_1.isEmpty()) {
            sat1 = findMaxProfitJobs_forDay1(arr_day_saturday_1);
        }
        if (!arr_day_saturday_2.isEmpty())
            sat2 = findMaxProfitJobs_forDay2(arr_day_saturday_2);

//for sunday
        System.out.println("\nSunday:\n");
        for (Classes value : arr) {
            if (value.getDay1().equals("یکشنبه")) {
                arr_day_sunday_1.add(value);
            } else if (value.getDay2().equals("یکشنبه")) {
                arr_day_sunday_2.add(value);
            }
        }
        for (Classes classes : arr_day_sunday_1) {
            System.out.println("------------------------------"+classes.getName());
        }

        for (Classes classes : arr_day_sunday_2) {
            System.out.println("------------------------------"+classes.getName());
        }
        if (!arr_day_sunday_1.isEmpty())
            sun1 = findMaxProfitJobs_forDay1(arr_day_sunday_1);
        if (!arr_day_sunday_2.isEmpty())
            sun2 = findMaxProfitJobs_forDay2(arr_day_sunday_2);


//for Monday
        System.out.println("\nMonday:\n");
        for (Classes value : arr) {
            if (value.getDay1().equals("دوشنبه")) {
                arr_day_monday_1.add(value);
            } else if (value.getDay2().equals("دوشنبه")) {
                arr_day_monday_2.add(value);
            }
        }
        for (Classes classes : arr_day_monday_1) {
            System.out.println("------------------------------"+classes.getName());
        }

        for (Classes classes : arr_day_monday_2) {
            System.out.println("------------------------------"+classes.getName());
        }
        if (!arr_day_monday_1.isEmpty())
            mon1 = findMaxProfitJobs_forDay1(arr_day_monday_1);
        if (!arr_day_monday_2.isEmpty())
            mon2 = findMaxProfitJobs_forDay2(arr_day_monday_2);

//for tuesday
        System.out.println("\nTuesday:\n");
        for (Classes value : arr) {
            if (value.getDay1().equals("سه شنبه")) {
                arr_day_tuesday_1.add(value);
            } else if (value.getDay2().equals("سه شنبه")) {
                arr_day_tuesday_2.add(value);
            }
        }
        for (Classes classes : arr_day_tuesday_1) {
            System.out.println("------------------------------"+classes.getName());
        }

        for (Classes classes : arr_day_tuesday_2) {
            System.out.println("------------------------------"+classes.getName());
        }
        if (!arr_day_tuesday_1.isEmpty())
            tuse1 = findMaxProfitJobs_forDay1(arr_day_tuesday_1);
        if (!arr_day_tuesday_2.isEmpty())
            tuse2 = findMaxProfitJobs_forDay2(arr_day_tuesday_2);

//for Wednesday
        System.out.println("\nWednesday:\n");
        for (Classes value : arr) {
            if (value.getDay1().equals("چهارشنبه")) {
                arr_day_wednesday_1.add(value);
            } else if (value.getDay2().equals("چهارشنبه")) {
                arr_day_wednesday_2.add(value);
            }
        }
        for (Classes classes : arr_day_wednesday_1) {
            System.out.println("------------------------------"+classes.getName());
        }

        for (Classes classes : arr_day_wednesday_2) {
            System.out.println("------------------------------"+classes.getName());
        }
        if (!arr_day_wednesday_1.isEmpty())
            wen1 = findMaxProfitJobs_forDay1(arr_day_wednesday_1);
        if (!arr_day_wednesday_2.isEmpty())
            wen2 = findMaxProfitJobs_forDay2(arr_day_wednesday_2);


        //combine days all possible classes
        sat1.addAll(sat2);
        sun1.addAll(sun2);
        mon1.addAll(mon2);
        tuse1.addAll(tuse2);
        wen1.addAll(wen2);

        System.out.println("\n");
        for (int i = 0; i < arr.size(); i++) {
            for (int j = 0; j < sat1.size(); j++) {
                if (arr.get(i).getName().equals(sat1.get(j).getName())) {
                    System.out.println(sat1.get(j).getName());
                    final_classes.add(sat1.get(j));
                    final_classes_name.add(sat1.get(j).getName());
                }
            }
            for (int j = 0; j < sun1.size(); j++) {
                if (arr.get(i).getName().equals(sun1.get(j).getName())) {
                    System.out.println(sun1.get(j).getName());
                    final_classes.add(sun1.get(j));
                    final_classes_name.add(sun1.get(j).getName());

                }
            }
            for (int j = 0; j < mon1.size(); j++) {
                if (arr.get(i).getName().equals(mon1.get(j).getName())) {
                    System.out.println(mon1.get(j).getName());
                    final_classes.add(mon1.get(j));
                    final_classes_name.add(mon1.get(j).getName());
                }
            }
            for (int j = 0; j < tuse1.size(); j++) {
                if (arr.get(i).getName().equals(tuse1.get(j).getName())) {
                    System.out.println(tuse1.get(j).getName());
                    final_classes.add(tuse1.get(j));
                    final_classes_name.add(tuse1.get(j).getName());

                }
            }
            for (int j = 0; j < wen1.size(); j++) {
                if (arr.get(i).getName().equals(wen1.get(j).getName())) {
                    System.out.println(wen1.get(j).getName());
                    final_classes.add(wen1.get(j));
                    final_classes_name.add(wen1.get(j).getName());

                }
            }
        }

        for (int i = 0; i < final_classes.size(); i++) {
            int count = Collections.frequency(final_classes_name, final_classes.get(i).getName());
            if (final_classes.get(i).getUnit() == 3 && count == 2) {
                possible_classes_final_result.add(final_classes.get(i));
            } else if (final_classes.get(i).getUnit() == 2 && count == 1) {
                possible_classes_final_result.add(final_classes.get(i));
            }
        }

        //remove duplicate without changing orders
        possible_classes_final_result = possible_classes_final_result.stream().distinct().collect(Collectors.toList());

        //sort inorder to remove items with less unit first if needed
        possible_classes_final_result.sort(Comparator.comparingInt(Classes::getUnit));

        //calculate max unit before check for MAX_POSSIBLE_UNIT
        for (Classes classes : possible_classes_final_result) {
            maxUnits += classes.getUnit();
        }


        for (int i = 0; i < possible_classes_final_result.size(); i++) {
            if (maxUnits > MAX_POSSIBLE_UNITS) {
                possible_classes_final_result.remove(i);
                maxUnits -= possible_classes_final_result.get(i).getUnit();
            }
        }

        int mxun=0;
        for (int i = 0; i <possible_classes_final_result.size() ; i++) {
            mxun += possible_classes_final_result.get(i).getUnit();
        }

        //print result with max possible lesson with limit of MAX_POSSIBLE_UNIT
        System.out.println("\n2نتیجه نهایی");
        for (Classes classes : possible_classes_final_result) {
            System.out.println(classes.getName()
                    + " (" + classes.getDay1() + " " + classes.getTime1_start() + " تا " + classes.getTime1_end() + ") "
                    + " (" + classes.getDay2() + " " + classes.getTime2_start() + " تا " + classes.getTime2_end() + ") "
                    + classes.getUnit());
        }

        System.out.println("بیشترین تعداد واحد قابل اخذ:" + maxUnits);

        maxUnit.setText(Integer.toString(mxun));

        progressBar.setVisibility(View.INVISIBLE);

        return possible_classes_final_result;
    }
}
