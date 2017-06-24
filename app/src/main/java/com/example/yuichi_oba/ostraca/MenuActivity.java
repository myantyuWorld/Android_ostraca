package com.example.yuichi_oba.ostraca;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.yuichi_oba.ostraca.models.Student;
import com.example.yuichi_oba.ostraca.models.Teacher;
import com.example.yuichi_oba.ostraca.tools.Enum_URL;
import com.example.yuichi_oba.ostraca.tools.Util;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private static final String TAG = MenuActivity.class.getSimpleName();
    public static final String TEACHER = "teacher";

    private Teacher teacher;

    /***
     * [ 非同期処理 ]ログインした教員の受け持ち学生リストを取得するクラス
     */
    private class TakingChargeStudentAsync extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            Log.d(TAG, "call TakingChargeStudentAsync--doInBackGround()");

            try {
                // 1
                HttpURLConnection c = Util.setConnectURL(Enum_URL.TEACHERS_CLASS.getText(), teacher.getAccessToken() + "&tea_id=" + teacher.getTea_id());
                // 2
                String result = Util.makeRequestToString(c);
                Log.d(TAG, result);

                if (!result.isEmpty()) {
                    return result;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "call onPostExecute()");
            super.onPostExecute(result);

            /***
             *  結果がNULLでなければ、JSONを変換して、teacherオブジェクトにセットする
             */
            List<Student> students = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();
            try {
                // JSON=>Java
                students = mapper.readValue(result, new TypeReference<List<Student>>() {
                });
                // 変数にセットする
                teacher.setStudentList(students);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // debug
            teacher.show();
            /***
             *  「教員用」出席状況アクティビティに遷移させる
             */
            Intent intent = new Intent(getApplicationContext(), StudentActivity.class);
            intent.putExtra("number", 1);
            intent.putExtra("teacher", teacher);
            startActivity(intent);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "call MenuActivity-onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        teacher = (Teacher) getIntent().getSerializableExtra(TEACHER);
        ((TextView) findViewById(R.id.txt_menu_tea_id)).setText(teacher.getTea_id());
        ((TextView) findViewById(R.id.txt_menu_tea_name)).setText(teacher.getTea_name());

        /***
         * 「出席記録」ボタン押下時の処理
         */
        ImageButton ibn_input = (ImageButton) findViewById(R.id.ibn_input);
        ibn_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 出席記録アクティビティに遷移させる
                Intent intent = new Intent(getApplicationContext(), TeacherActivity.class);
                intent.putExtra(TEACHER, teacher);
                startActivity(intent);
            }
        });
//        Button bnMenuInput = (Button) findViewById(R.id.bn_menu_attendinput);
//        bnMenuInput.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });

        /***
         * 「出席状況」ボタン押下時の処理
         */
        ImageButton ibn_attend = (ImageButton) findViewById(R.id.ibn_attend);
        ibn_attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /***
                 * 教員用の 出席状況アクティビティに遷移させる
                 */
                // DO: 2017/06/22 非同期処理 --- 当該教員IDの受け持ち学生リストを取得
                new TakingChargeStudentAsync().execute();
            }
        });
//        Button bnMenuAttendList = (Button) findViewById(R.id.bn_menu_attendlist);
//        bnMenuAttendList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });

    }
}
