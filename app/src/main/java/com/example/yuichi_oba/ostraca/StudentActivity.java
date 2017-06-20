package com.example.yuichi_oba.ostraca;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuichi_oba.ostraca.models.Student;
import com.example.yuichi_oba.ostraca.models.StudentListItem;
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
    private class StudentAttendAsync extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            Log.d(TAG, "call doInBackGround()");
            String result = null;
            try {
                // GET /students/:stu_id
                HttpURLConnection c = Util.setConnectURL(Enum_URL.STUDENTS.getText() + student.getStu_id(), student.getAccessToken());
                String s = Util.makeRequestToString(c);
                Log.d(TAG, s);
                // 問い合わせ結果が、空か、NULLでない
                if (!s.isEmpty() && !s.equals("[]")) {
                    result = s;     // リターン用変数に問い合わせ結果を代入する
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // [{"stu_id":"5151021","stu_name":"大馬 裕一","sub_id" : "0001","sub_name":"システム構築","attend":"2","total_lesson":"60","attend_rate":"3"}]
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
            // 引数がNULLまたは空文字でないならば実行する
            if (!result.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    // JSON --> javaobject に 変換する
                    List<V_stu_attend_status> v_stu_attend_statuses = mapper.readValue(result, new TypeReference<List<V_stu_attend_status>>() {
                    });
                    // リストビュー表示用クラスに変換する
                    data = new ArrayList<>();
                    for (V_stu_attend_status v : v_stu_attend_statuses) {
                        StudentListItem item = new StudentListItem(
                                new Random().nextLong(),
                                v.sub_name,
                                v.attend,
                                v.total_lesson,
                                v.attend_rate
                        );
                        item.show();

                        data.add(item); // リストに追加
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                /***
                 * リストビュー表示のための処理
                 */
                StudentListAdapter adapter = new StudentListAdapter(StudentActivity.this, data, R.layout.stu_list_item);
                stu_listview.setAdapter(adapter);
            } else {
                Toast.makeText(StudentActivity.this, "出席状況の取得に失敗", Toast.LENGTH_SHORT).show();
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
    private Student student;
    private List<StudentListItem> data;
    private ListView stu_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "call StudentActivity--onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        student = (Student) getIntent().getSerializableExtra("student");
        Log.d("call", student.getStu_id());

        init();
    }

    private void init() {
        Log.d(TAG, "call init()");
        TextView txt_stu_id = (TextView) findViewById(R.id.txt_stu_id);
        txt_stu_id.setText(student.getStu_id());

        TextView txt_stu_name = (TextView) findViewById(R.id.txt_stu_name);
        txt_stu_name.setText(student.getStu_name());

        new StudentAttendAsync().execute();
        stu_listview = (ListView) findViewById(R.id.stu_listview);
    }
}
