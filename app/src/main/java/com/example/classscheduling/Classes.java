package com.example.classscheduling;

import java.util.Comparator;

public class Classes {

    private String name;
    private String day1;
    private int time1_start;
    private int time1_end;
    private String day2;
    private int time2_start;
    private int time2_end;
    private int unit;

    static class JobComparator implements Comparator<Classes> {

        @Override
        public int compare(Classes o1, Classes o2) {
            return o1.getTime1_end() < o2.getTime1_start() ? -1 : o1.getTime1_end() == o2.getTime1_end() ? 0 : 1;
        }
    }
        public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getDay1() {
        return day1;
    }

    void setDay1(String day1) {
        this.day1 = day1;
    }


    public String getDay2() {
        return day2;
    }

    public void setDay2(String day2) {
        this.day2 = day2;
    }

    public int getTime1_start() {
        return time1_start;
    }

    public void setTime1_start(int time1_start) {
        this.time1_start = time1_start;
    }

    public int getTime1_end() {
        return time1_end;
    }

    public void setTime1_end(int time1_end) {
        this.time1_end = time1_end;
    }

    public int getTime2_start() {
        return time2_start;
    }

    public void setTime2_start(int time2_start) {
        this.time2_start = time2_start;
    }

    public int getTime2_end() {
        return time2_end;
    }

    public void setTime2_end(int time2_end) {
        this.time2_end = time2_end;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }
}
