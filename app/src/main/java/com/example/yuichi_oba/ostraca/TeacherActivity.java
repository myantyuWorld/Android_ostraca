package com.example.yuichi_oba.ostraca;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuichi_oba.ostraca.models.Subject;
import com.example.yuichi_oba.ostraca.models.Teacher;
import com.example.yuichi_oba.ostraca.tools.Enum_URL;
import com.example.yuichi_oba.ostraca.tools.Util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TeacherActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = TeacherActivity.class.getSimpleName();

    /***
     * 当該教員と選択した科目IDを基に出席すべき学生リストを取得する非同期処理クラス
     */
    // TODO: 2017/06/13  
    private class StudentListAsync extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... objects) {
            Log.d(TAG, "StudentListAsync--doInBackGround");
            String sub_id = (String) objects[0];
            try {
                // 1
                HttpURLConnection c = Util.setConnectURL(Enum_URL.TEACHERS_CLASS_STUDENTLIST + sub_id, teacher.getAccessToken());

                // 2
                String result = Util.makeRequestToString(c);
                Log.d(TAG, result);
                // 結果がNULLでなければ実行する
                if (!result.isEmpty()) {
                    return result;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(TAG, "StudentListAsync--onPostExcute");
            super.onPostExecute(s);

            

        }
    }

    private Teacher teacher;
    private TextView text_date;
    private Spinner sp_subject;
    //    private Spinner sp_period;
    private ListView listView_stulist;
    private Button bn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("call", "=========================================");
        Log.d("call", "TeacherActivity start");
        Log.d("call", "=========================================");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        // ログイン画面からのインスタンスの受け取り
        teacher = (Teacher) getIntent().getSerializableExtra("teacher");
        teacher.show();

        // ウィジェットの処理化処理
        init();
    }

    @Override
    public void onClick(View view) {

    }

    private void init() {
        Log.d(TAG, "TeacherActivity--init()");
        /********************************
         *  日付の設定
         *******************************/
        text_date = (TextView) findViewById(R.id.text_date);
        text_date.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        /********************************
         *  科目スピナーの設定
         *******************************/
        sp_subject = (Spinner) findViewById(R.id.sp_subject);
        List<String> list = new ArrayList<>();
        for (Subject s : teacher.getSubjects()) {
            list.add(s.getSub_id() + ":" + s.getSub_name());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                list
        );
        sp_subject.setAdapter(adapter);
        // 科目スピナーのリスナ
        sp_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "spinner_subject--onItemSelected()");
                Spinner spinner = (Spinner) adapterView;

                String sub_id = spinner.getSelectedItem().toString();
                // TODO: 2017/06/13 当該教員と選択した科目IDに出席すべき学生リストを取得する非同期処理クラスの実装
                Toast.makeText(TeacherActivity.this, sub_id.split(":")[0], Toast.LENGTH_SHORT).show();
//                new StudentListAsync().execute(sub_id.split(":")[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


}
