package com.example.yuichi_oba.ostraca.models;

/**
 * Created by Yuichi-Oba on 2017/06/13.
 */

import java.io.Serializable;

/***
 * 科目JSONからJavaに変換するJackson処理に必要なclass
 */
public class Subject implements Serializable {

    private String sub_id;
    private String sub_name;

    public String getSub_id() {
        return sub_id;
    }

    public void setSub_id(String sub_id) {
        this.sub_id = sub_id;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }
}