package com.example.classscheduling;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UploadInterface {

    String url="http://192.168.56.1/classScheduling/";
    @FormUrlEncoded
    @POST("AddClass.php")
    Call<String> addClass(
            @Field("name") String name,
            @Field("unit") int unit,
            @Field("day1") String day1,
            @Field("day1_start") int day1_start,
            @Field("day1_end") int day1_end,
            @Field("day2") String day2,
            @Field("day2_start") int day2_start,
            @Field("day2_end") int day2_end
    );
}
