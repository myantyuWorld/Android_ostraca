package com.example.yuichi_oba.ostraca.models;

import android.util.Log;

/**
 * Created by Yuichi-Oba on 2017/06/18.
 */

public class ListItem {

    private static final String TAG = ListItem.class.getSimpleName();
    private long id;
    private String stu_id;
    private String stu_name;
    private boolean[] attend = new boolean[4];

    public ListItem(long id, String stu_id, String stu_name, boolean[] attend) {
        this.id = id;
        this.stu_id = stu_id;
        this.stu_name = stu_name;
        this.attend = attend;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStu_id() {
        return stu_id;
    }

    public void setStu_id(String stu_id) {
        this.stu_id = stu_id;
    }

    public String getStu_name() {
        return stu_name;
    }

    public void setStu_name(String stu_name) {
        this.stu_name = stu_name;
    }

    public boolean[] getAttend() {
        return attend;
    }

    public void setAttend(boolean[] attend) {
        this.attend = attend;
    }

    public void show()
    {
        Log.d(TAG, this.getStu_id() + " : " + this.getStu_name());
        for (boolean b : attend) {
            Log.d(TAG, String.valueOf(b));
        }
    }
}
