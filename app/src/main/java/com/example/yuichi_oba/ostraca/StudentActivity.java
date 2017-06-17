package com.example.yuichi_oba.ostraca;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.yuichi_oba.ostraca.models.Student;

public class StudentActivity extends AppCompatActivity {

    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        student = (Student) getIntent().getSerializableExtra("student");
        Log.d("call", student.getStu_id());
    }
}
