package com.example.yuichi_oba.ostraca.models;

import android.util.Log;

/**
 * Created by Yuichi-Oba on 2017/06/20.
 */

public class StudentListItem {

    private static final String TAG = StudentListItem.class.getSimpleName();
    private long id;
    private String sub_name;
    private String attend;
    private String total_lesson;
    private String attend_rate;

    public StudentListItem(long id, String sub_name, String attend, String total_lesson, String attend_rate) {
        this.id = id;
        this.sub_name = sub_name;
        this.attend = attend;
        this.total_lesson = total_lesson;
        this.attend_rate = attend_rate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static String getTAG() {
        return TAG;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getAttend() {
        return attend;
    }

    public void setAttend(String attend) {
        this.attend = attend;
    }

    public String getTotal_lesson() {
        return total_lesson;
    }

    public void setTotal_lesson(String total_lesson) {
        this.total_lesson = total_lesson;
    }

    public String getAttend_rate() {
        return attend_rate;
    }

    public void setAttend_rate(String attend_rate) {
        this.attend_rate = attend_rate;
    }

    public void show()
    {
        Log.d(TAG, "_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/");
        Log.d(TAG, getSub_name());
        Log.d(TAG, getAttend());
        Log.d(TAG, getTotal_lesson());
        Log.d(TAG, getAttend_rate());
        Log.d(TAG, "_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/");
    }
}
