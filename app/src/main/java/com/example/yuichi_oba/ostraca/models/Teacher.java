package com.example.yuichi_oba.ostraca.models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Yuichi-Oba on 2017/06/13.
 */

/***
 * 教員クラス
 */
public class Teacher implements Serializable {

    /***
     * Field
     */
    private static final String TAG = "call";
    private String tea_id;                  // 教員ID（本システムでのみ使用する便宜上のもの）
    private String tea_name;                // 氏名
    private Integer tea_flg;                // 教員の権限： 常勤[1] 非常勤[0]
    private String tea_mailaddr;            // 教員メールアドレス
    private String accessToken;             // Googleアクセストークン
    private List<Subject> subjects;         // 担当科目リスト
    private List<Student> studentList = new ArrayList<>();      // 自分の受け持ち学生リスト

    /***
     * Constractor
     *
     * @param tea_id
     * @param tea_name
     * @param tea_flg
     * @param tea_mailaddr
     */
    public Teacher(String tea_id, String tea_name, Integer tea_flg, String tea_mailaddr) {
        this.tea_id = tea_id;
        this.tea_name = tea_name;
        this.tea_flg = tea_flg;
        this.tea_mailaddr = tea_mailaddr;
    }

    /***
     * GetterSetter
     *
     * @return
     */
    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTea_id() {
        return tea_id;
    }

    public void setTea_id(String tea_id) {
        this.tea_id = tea_id;
    }

    public String getTea_name() {
        return tea_name;
    }

    public void setTea_name(String tea_name) {
        this.tea_name = tea_name;
    }

    public Integer getTea_flg() {
        return tea_flg;
    }

    public void setTea_flg(Integer tea_flg) {
        this.tea_flg = tea_flg;
    }

    public String getTea_mailaddr() {
        return tea_mailaddr;
    }

    public void setTea_mailaddr(String tea_mailaddr) {
        this.tea_mailaddr = tea_mailaddr;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    /***
     * 教員のフィールドをデバッグ出力するメソッド
     */
    public void show() {
        Log.d(TAG, "_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/");
        Log.d(TAG, this.getTea_id() + " " + this.getTea_name());
        Log.d(TAG, this.getTea_mailaddr());
        Log.d(TAG, this.getAccessToken());

        for (Subject subject : this.getSubjects()) {
            Log.d(TAG, subject.getSub_id() + " : " + subject.getSub_name());
        }
        for (Student s : studentList) {
            Log.d(TAG, s.getStu_id());
        }
        Log.d(TAG, "_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/");
    }
}
