package com.example.classscheduling;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DataGenerator {

    List<Classes> All=new ArrayList<>();

    public List<Classes> getClasses() {
        List<Classes> All_classes = new ArrayList<>();
        Classes classes = new Classes();
        classes.setName("الگوریتم");
        classes.setDay1("شنبه");
        classes.setTime1_start(8);
        classes.setTime1_end(10);
        classes.setDay2("یکشنبه");
        classes.setTime2_start(14);
        classes.setTime2_end(15);
        classes.setUnit(3);
        All_classes.add(classes);


        classes = new Classes();
        classes.setName("معماری کامپیوتر");
        classes.setDay1("شنبه");
        classes.setTime1_start(12);
        classes.setTime1_end(14);
        classes.setDay2("دوشنبه");
        classes.setTime2_start(16);
        classes.setTime2_end(17);
        classes.setUnit(3);
        All_classes.add(classes);


        classes = new Classes();
        classes.setName("ادبیات فارسی");
        classes.setDay1("شنبه");
        classes.setTime1_start(9);
        classes.setTime1_end(11);
        classes.setDay2("چهارشنبه");
        classes.setTime2_start(11);
        classes.setTime2_end(12);
        classes.setUnit(3);
        All_classes.add(classes);


        classes = new Classes();
        classes.setName("دانش خانواده و جمعیت");
        classes.setDay1("سه شنبه");
        classes.setTime1_start(8);
        classes.setTime1_end(10);
        //2 unit lesson have only one day class!
        classes.setDay2("");
        classes.setUnit(2);
        All_classes.add(classes);


        classes = new Classes();
        classes.setName("نظریه زبان ها");
        classes.setDay1("دوشنبه");
        classes.setTime1_start(9);
        classes.setTime1_end(11);
        classes.setDay2("سه شنبه");
        classes.setTime2_start(11);
        classes.setTime2_end(13);
        classes.setUnit(3);
        All_classes.add(classes);


        classes = new Classes();
        classes.setName("زبان تخصصی");
        classes.setDay1("یکشنبه");
        classes.setTime1_start(9);
        classes.setTime1_end(11);
        classes.setDay2("چهارشنبه");
        classes.setTime2_start(16);
        classes.setTime2_end(18);
        classes.setUnit(3);
        All_classes.add(classes);


        classes = new Classes();
        classes.setName("ریاضیات مهندسی");
        classes.setDay1("سه شنبه");
        classes.setTime1_start(9);
        classes.setTime1_end(11);
        classes.setDay2("چهارشنبه");
        classes.setTime2_start(8);
        classes.setTime2_end(9);
        classes.setUnit(3);


        classes = new Classes();
        classes.setName("ورزش2");
        classes.setDay1("شنبه");
        classes.setTime1_start(15);
        classes.setTime1_end(16);
        //2 unit lesson have only one day class!
        classes.setDay2("");
        classes.setUnit(2);
        All_classes.add(classes);


        classes = new Classes();
        classes.setName("مدارمنطقی");
        classes.setDay1("دوشنبه");
        classes.setTime1_start(12);
        classes.setTime1_end(13);
        classes.setDay2("چهارشنبه");
        classes.setTime2_start(7);
        classes.setTime2_end(8);
        classes.setUnit(3);

        classes = new Classes();
        classes.setName("مدار الکتریکی");
        classes.setDay1("یکشنبه");
        classes.setTime1_start(14);
        classes.setTime1_end(16);
        classes.setDay2("سه شنبه");
        classes.setTime2_start(17);
        classes.setTime2_end(18);
        classes.setUnit(3);

        All_classes.add(classes);
        All.addAll(All_classes);
        return All_classes;
    }

    public void AddClass(String name, String day1, int day1_start, int day1_end, String day2, int day2_start, int day2_end, int unit) {
        Classes classes = new Classes();
        classes.setName(name);
        classes.setDay1(day1);
        classes.setTime1_start(day1_start);
        classes.setTime1_end(day1_end);
        classes.setDay2(day2);
        classes.setTime2_start(day2_start);
        classes.setTime2_end(day2_end);
        classes.setUnit(unit);
        All.add(classes);
    }

    public void AddClass(String name, String day1, int day1_start, int day1_end, String day2, int unit) {
        Classes classes = new Classes();
        classes.setName(name);
        classes.setDay1(day1);
        classes.setTime1_start(day1_start);
        classes.setTime1_end(day1_end);
        classes.setDay2(day2);
        classes.setUnit(unit);
        All.add(classes);
    }
}
