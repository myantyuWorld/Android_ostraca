package com.example.yuichi_oba.ostraca;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuichi_oba.ostraca.models.Student;
import com.example.yuichi_oba.ostraca.models.StudentListItem;
import com.example.yuichi_oba.ostraca.models.Teacher;
import com.example.yuichi_oba.ostraca.models.V_stu_attend_status;
import com.example.yuichi_oba.ostraca.tools.Enum_URL;
import com.example.yuichi_oba.ostraca.tools.Util;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/***
 * [{"stu_id":"5151021","stu_name":"大馬 裕一","sub_name":"システム構築","attend":"2","total_lesson":"60","attend_rate":"3"}]
 */
public class StudentActivity extends AppCompatActivity {

    /********************************
     * 非同期処理class
     * <p>
     * 学生の出席状況をRESTに問い合わせる
     * return
     *******************************/
    private class StudentAttendAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String stu_id = strings[0];
            String result = strings[1];
            String accessToken = strings[2];

            Log.d(TAG, "call doInBackGround()");
            String json = null;
            try {
                // GET /students/:stu_id
//                HttpURLConnection c = Util.setConnectURL(Enum_URL.STUDENTS.getText() + student.getStu_id(), student.getAccessToken());
                // 1
                HttpURLConnection c = Util.setConnectURL(Enum_URL.STUDENTS.getText() + stu_id, accessToken);
                // 2
                String s = Util.makeRequestToString(c);
                Log.d(TAG, s);
                // 問い合わせ結果が、空か、NULLでない
                if (!s.isEmpty() && !s.equals("[]")) {
                    json = s;     // リターン用変数に問い合わせ結果を代入する
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // [{"stu_id":"5151021","stu_name":"大馬 裕一","sub_id" : "0001","sub_name":"システム構築","attend":"2","total_lesson":"60","attend_rate":"3"}]
            /***
             * JSON-->Java変換
             */
            if (!json.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    // JSON --> javaobject に 変換する
                    List<V_stu_attend_status> v_stu_attend_statuses = mapper.readValue(json, new TypeReference<List<V_stu_attend_status>>() {
                    });
                    // リストビュー表示用クラスに変換する
                    data = new ArrayList<>();
                    for (V_stu_attend_status v : v_stu_attend_statuses) {
                        StudentListItem item = new StudentListItem(new Random().nextLong(), v.sub_name, v.attend, v.total_lesson, v.attend_rate);
                        item.show();

                        data.add(item); // リストに追加
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        /***
         * 結果の文字列（JSON）を Javaオブジェクトに変換する
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "call onPostExecute()");
            super.onPostExecute(result);
            /***
             * リストビュー表示のための処理
             */
            switch (result) {
                case "student":
                    Log.d(TAG, "学生用---リストビュー生成");
                    StudentListAdapter adapter = new StudentListAdapter(StudentActivity.this, data, R.layout.stu_list_item);
                    stu_listview.setAdapter(adapter);
                    break;
                case "teacher":
                    Log.d(TAG, "教員用---リストビュー生成");
                    StudentListAdapter adapter1 = new StudentListAdapter(StudentActivity.this, data, R.layout.stu_list_item);
                    listview_ts.setAdapter(adapter1);
            }
        }
    }

    /********************************
     * 自作アダプタ
     * <p>
     * StudentListItem格納用のアダプタ
     *******************************/
    private class StudentListAdapter extends BaseAdapter {

        private Context context;
        private List<StudentListItem> data = null;
        private int resouce = 0;

        public StudentListAdapter(Context context, List<StudentListItem> data, int resouce) {
            this.context = context;
            this.data = data;
            this.resouce = resouce;
        }

        @Override
        public int getCount() {
            return this.data.size();
        }

        @Override
        public Object getItem(int i) {
            return this.data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return this.data.get(i).getId();
        }

        @Override
        public View getView(int position, View contentView, ViewGroup parent) {
            Log.d(TAG, "call StudentListAdapter--getView()");
            Activity activity = (Activity) this.context;
            StudentListItem item = this.data.get(position);
            item.show();
            Log.d(TAG, item.getSub_name() + " : " + item.getAttend());

            if (contentView == null) {
                contentView = activity.getLayoutInflater().inflate(resouce, null);
            }

            TextView txtsub_name = (TextView) contentView.findViewById(R.id.txt_sub_name);
            txtsub_name.setText(item.getSub_name());

            TextView txtattend = (TextView) contentView.findViewById(R.id.txt_attend);
            txtattend.setText(item.getAttend());

            TextView txttotal_lesson = (TextView) contentView.findViewById(R.id.txt_total_lesson);
            txttotal_lesson.setText(item.getTotal_lesson());

            TextView txtattend_rate = (TextView) contentView.findViewById(R.id.txt_attend_rate);
            txtattend_rate.setText(item.getAttend_rate() + "%");

            return contentView;
        }
    }

    private static final String TAG = StudentActivity.class.getSimpleName();
    public static final String TEACHER = "teacher";
    private Student student;
    private Teacher teacher;
    private List<StudentListItem> data;
    private ListView stu_listview;
    private ListView listview_ts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "call StudentActivity--onCreate()");
        super.onCreate(savedInstanceState);

        Log.d(TAG, String.valueOf(getIntent().getIntExtra("number", 0)));

        switch (getIntent().getIntExtra("number", 0)) {
            case 0:
                init();
                break;
            case 1:
                init_teacher_version();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_tea_menu, menu);
        if (getIntent().getIntExtra("number", 0) == 1) {
            return true;

        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        // 選択されたメニューが 「出席記録」ならば実行
        if (item.getTitle().equals("出席記録")) {
            // 出席記録アクティビティに遷移させる
            Intent intent = new Intent(getApplicationContext(), TeacherActivity.class);
            intent.putExtra(TEACHER, teacher);
            startActivity(intent);
        }
        return true;
    }

    private void init_teacher_version() {
        setContentView(R.layout.activity_tea_student);
        teacher = (Teacher) getIntent().getSerializableExtra("teacher");
        // 教員名を表示する
        ((TextView) findViewById(R.id.txt_ts_tea_name)).setText(teacher.getTea_name());

        /***
         * 学生スピナーの設定
         */
        // データの設定
        List<String> list = new ArrayList<>();
        for (Student t : teacher.getStudentList()) {
            list.add(t.getStu_id() + ":" + t.getStu_name());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        Spinner spinner = (Spinner) findViewById(R.id.sp_ts_students);
        spinner.setAdapter(adapter);
        // リスナー登録
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner sp = (Spinner) adapterView;
                Toast.makeText(StudentActivity.this, sp.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                new StudentAttendAsync().execute(sp.getSelectedItem().toString().split(":")[0], "teacher", teacher.getAccessToken());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /***
         * リストビューの設定
         */
        listview_ts = (ListView) findViewById(R.id.listview_ts);


    }


    private void init() {
        setContentView(R.layout.activity_student);
        student = (Student) getIntent().getSerializableExtra("student");

        Log.d(TAG, "call init()");
        TextView txt_stu_id = (TextView) findViewById(R.id.txt_stu_id);
        txt_stu_id.setText(student.getStu_id());

        TextView txt_stu_name = (TextView) findViewById(R.id.txt_stu_name);
        txt_stu_name.setText(student.getStu_name());

        // 非同期処理を開始する
        new StudentAttendAsync().execute(student.getStu_id(), "student", student.getAccessToken());
        stu_listview = (ListView) findViewById(R.id.listview_ts);
    }
}
