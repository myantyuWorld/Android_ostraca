package com.example.yuichi_oba.ostraca.models;

import java.io.Serializable;

/**
 * Created by Yuichi-Oba on 2017/06/13.
 */

public class Student implements Serializable {

    public String stu_id;
    public String stu_name;
    public String stu_rubi;
    public String stu_mailaddr;
    public String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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

    public String getStu_rubi() {
        return stu_rubi;
    }

    public void setStu_rubi(String stu_rubi) {
        this.stu_rubi = stu_rubi;
    }

    public String getStu_mailaddr() {
        return stu_mailaddr;
    }

    public void setStu_mailaddr(String stu_mailaddr) {
        this.stu_mailaddr = stu_mailaddr;
    }

    public Student(String stu_id, String stu_name, String stu_rubi, String stu_mailaddr) {
        this.stu_id = stu_id;
        this.stu_name = stu_name;
        this.stu_rubi = stu_rubi;
        this.stu_mailaddr = stu_mailaddr;
    }
}
