package com.example.yuichi_oba.ostraca;

import android.app.Activity;
import android.app.DialogFragment;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuichi_oba.ostraca.models.ListItem;
import com.example.yuichi_oba.ostraca.models.Subject;
import com.example.yuichi_oba.ostraca.models.Teacher;
import com.example.yuichi_oba.ostraca.tools.Enum_URL;
import com.example.yuichi_oba.ostraca.tools.MyDialogFragment;
import com.example.yuichi_oba.ostraca.tools.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TeacherActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = TeacherActivity.class.getSimpleName();

    /********************************
     * -----JSON変換用class(出席すべき学生リストの変換用)
     *******************************/
    private static class Student implements Serializable {
        private String stu_id;
        private String stu_name;

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
    }

    /********************************
     * -----JSON変換用class(RESTへPOSTリクエストする用)
     *******************************/
    private static class AttendList implements Serializable {

        private static class Student {
            private String stu_id;
            private int att_attend;

            public Student(String stu_id, int att_attend) {
                this.stu_id = stu_id;
                this.att_attend = att_attend;
            }

            public Student() {

            }

            public String getStu_id() {
                return stu_id;
            }

            public void setStu_id(String stu_id) {
                this.stu_id = stu_id;
            }

            public int getAtt_attend() {
                return att_attend;
            }

            public void setAtt_attend(int att_attend) {
                this.att_attend = att_attend;
            }
        }

        private static class Common {
            private String att_date;
            private int att_period;
            private String sub_id;

            public Common(String att_date, int att_period, String sub_id) {
                this.att_date = att_date;
                this.att_period = att_period;
                this.sub_id = sub_id;
            }

            public String getAtt_date() {
                return att_date;
            }

            public void setAtt_date(String att_date) {
                this.att_date = att_date;
            }

            public int getAtt_period() {
                return att_period;
            }

            public void setAtt_period(int att_period) {
                this.att_period = att_period;
            }

            public String getSub_id() {
                return sub_id;
            }

            public void setSub_id(String sub_id) {
                this.sub_id = sub_id;
            }
        }

        private Common common;
        private List<Student> students;

        /********************************
         * Constractor
         *******************************/
        public AttendList(Common common, List<ListItem> students) {
            this.common = common;
            /* ListItem -- > innerのStudentクラスに変換 */
            List<Student> studentList = new ArrayList<>();
            for (int i = 0; i < students.size(); i++) {
                Student student = new Student();
                student.setStu_id(students.get(i).getStu_id());
                for (int j = 0; j < students.get(i).getAttend().length; j++) {
                    // 出席フラグがtrueのところで実行（出席、欠席、遅刻、公欠）
                    if (students.get(i).getAttend()[j]) {
                        student.setAtt_attend(j + 1); // TODO: 2017/06/18 出席区分の記録の再確認を行え
                    }
                }
                studentList.add(student);
            }
            /* ここまで */
            this.students = studentList;
        }

        public Common getCommon() {
            return common;
        }

        public void setCommon(Common common) {
            this.common = common;
        }

        public List<Student> getStudents() {
            return students;
        }

        public void setStudents(List<Student> students) {
            this.students = students;
        }

        /***
         * debug
         */
        public void show() {
            Log.d(TAG, this.common.getAtt_date() + " : " + this.common.getSub_id() + " : " + this.common.getAtt_period());
            for (Student s : this.students) {
                Log.d(TAG, s.getStu_id() + " : " + s.getAtt_attend());
            }
        }
    }

    /********************************
     * -----非同期処理class
     * 当該教員と選択した科目IDを基に出席すべき学生リストを取得する
     * DO: 2017/06/13
     *******************************/
    private class StudentListAsync extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... objects) {
            Log.d(TAG, "StudentListAsync--doInBackGround");
            String sub_id = (String) objects[0];
            try {
                // 1
                HttpURLConnection c = Util.setConnectURL(Enum_URL.TEACHERS_CLASS_STUDENTLIST.getText() + sub_id, teacher.getAccessToken());

                // 2
                String result = Util.makeRequestToString(c);
                // [{"stu_name":"石山大樹","stu_id":"5151002"}, {"stu_name":"大馬 裕一","stu_id":"5151021"}, {"stu_name":"後藤聖登","stu_id":"5151042"}]
                Log.d(TAG, result);
                // 結果がNULLでなければ実行する
                if (!result.isEmpty()) {
                    Log.d(TAG, "reuslt is not null");
                    return result;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "StudentListAsync--onPostExcute");
            super.onPostExecute(result);

            /***
             * 結果がNULLでなければ、リストに表示するデータを準備する
             */
//            List<ListItem> data = new ArrayList<>();
            List<Student> students = new ArrayList<>();
            if (!result.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    // JSON＝＝＞ Javaに変換する
                    students = mapper.readValue(result, new TypeReference<List<Student>>() {
                    });
                    // CardView 表示用のクラスに変換する
                    data = new ArrayList<>();
                    for (Student s : students) {
                        ListItem item = new ListItem(
                                new Random().nextLong(),
                                s.getStu_id(),
                                s.getStu_name(),
                                new boolean[]{true, false, false, false}
                        );
                        data.add(item);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /***
                 * リストビューに表示するための処理
                 */
                MyListAdapter adapter = new MyListAdapter(
                        TeacherActivity.this, data, R.layout.lite_item
                );
                listView.setAdapter(adapter);

            }
        }
    }

    /********************************
     * -----非同期処理class
     * RESTへ出席記録をPOSTする
     *******************************/
    private class PostAttendAsync extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... objects) {
            Log.d(TAG, "call PostAttendAsync--doInBackGround()");
            String json = objects[0].toString();
            Log.d(TAG, json);
            String result = null;

            try {
                // 1
//                HttpURLConnection c = Util.setConnectURL(Enum_URL.TEACHERS_CLASS.getText(), teacher.getAccessToken());
                // POST /teachers/class
                // データは、↓
                // {"common":{"att_date":"2017-06-20","att_period":1,"sub_id":"0001"},
                // "students":[{"att_attend":1,"stu_id":"5151002"},{"att_attend":1,"stu_id":"5151021"},{"att_attend":1,"stu_id":"5151042"}]}
                HttpURLConnection c = Util.setConnectURLPost(Enum_URL.TEACHERS_CLASS.getText(), teacher.getAccessToken(), json);
                // 2
                result = Util.makeRequestToString(c);
                // 3
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "call PostAttendAsync--onPostExecute()");
            super.onPostExecute(result);

//            Toast.makeText(TeacherActivity.this, "result --- " + result, Toast.LENGTH_SHORT).show();
            DialogFragment dialogFragment = new MyDialogFragment();
            dialogFragment.show(getFragmentManager(), "dialog_basic");
        }
    }

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
            List<com.example.yuichi_oba.ostraca.models.Student> students = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();
            try {
                // JSON=>Java
                students = mapper.readValue(result, new TypeReference<List<com.example.yuichi_oba.ostraca.models.Student>>() {
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

    /********************************
     * リストビュー用の自作アダプタ
     *******************************/
    private class MyListAdapter extends BaseAdapter {

        private int index;
        private Context context;
        private List<ListItem> items = null;
        private int resource = 0;

        public MyListAdapter(Context context, List<ListItem> items, int resource) {
            this.context = context;
            this.items = items;
            this.resource = resource;
        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        @Override
        public Object getItem(int i) {
            return this.items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return this.items.get(i).getId();
        }

        @Override
        public View getView(int position, View contentView, ViewGroup viewGroup) {
            Log.d(TAG, "call MyListAdaoter--getView()");
            Activity activity = (Activity) this.context;
            final ListItem item = (ListItem) getItem(position);
            Log.d(TAG, item.getStu_id() + " : " + item.getStu_name());

            if (contentView == null) {
                contentView = activity.getLayoutInflater().inflate(resource, null);
            }

            /***
             * リストビューに表示するデータの設定
             */
            TextView stu_id = (TextView) contentView.findViewById(R.id.stu_id);
            stu_id.setText(item.getStu_id());

            TextView stu_name = (TextView) contentView.findViewById(R.id.stu_name);
            stu_name.setText(item.getStu_name());

            RadioGroup radioGroup = (RadioGroup) contentView.findViewById(R.id.rbn_group);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int position) {
                    Log.d(TAG, "select rbn resouceId" + String.valueOf(position));

                    // ラジオボタンの選択を記憶して設定する
                    boolean[] booleen = new boolean[4];
                    switch (position) {
                        case R.id.rbn_1:
                            booleen[0] = true;
                            break;
                        case R.id.rbn_2:
                            booleen[1] = true;
                            break;
                        case R.id.rbn_3:
                            booleen[2] = true;
                            break;
                        case R.id.rbn_4:
                            booleen[3] = true;
                            break;
                    }

                    for (int i = 0; i < data.size(); i++) {
                        if (item.getId() == data.get(i).getId()) {
                            data.get(i).setAttend(booleen);
                        }
                    }
                    // --- ここまで ---

                }
            });
            return contentView;
        }
    }

    /********************************
     * -----非同期処理class
     * // TODO: 2017/06/18  RESTへ出席記録をPOSTするメソッド
     *******************************/

    private Teacher teacher;
    private TextView text_date;
    private Spinner sp_subject;
    private Spinner sp_period;
    private ListView listView;
    private Button bn_send;
    private List<ListItem> data;

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

    /********************************
     * メニュー定義ファイルを基にオプションメニューを生成
     *******************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_tea_menu, menu);
        return true;
    }

    /********************************
     * メニュー選択時の処理
     *******************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        // 選択されたメニューが 「出席状況」ならば実行
        if (item.getTitle().equals("出席状況") && teacher.getTea_flg() == 1) {
            new TakingChargeStudentAsync().execute();
        }
        return true;
    }

    /***
     * 送信ボタン押下時の処理
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        Log.d(TAG, "TeacherActivity--onclick()");
        debugOutPut();

        // 2017-06-16 1 0001
        AttendList.Common common = new AttendList.Common(
                text_date.getText().toString(),
                Integer.parseInt(sp_period.getSelectedItem().toString()),
                sp_subject.getSelectedItem().toString().split(":")[0]
        );
        AttendList attendList = new AttendList(common, data);
        // debug
        // これを、アクセストークン付きで、RESTへPOSTする
        attendList.show();
        // DO: 2017/06/18 attendList の JSON化
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(attendList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Log.d(TAG, json);
        // DO: 2017/06/20 非同期処理で、JSONをPOSTする
        new PostAttendAsync().execute(json);


    }

    private void debugOutPut() {
        StringBuffer stringBuffer = new StringBuffer();
        for (ListItem item : data) {
            stringBuffer.append("[  " + item.getStu_id() + " : ");
            stringBuffer.append(item.getStu_name() + " :");
            Log.d(TAG, item.getStu_id() + " : " + item.getStu_name());
            String[] strings = new String[]{"出席", "欠席", "遅刻", "公欠"};
            for (int i = 0; i < item.getAttend().length; i++) {
                if (item.getAttend()[i]) {
                    stringBuffer.append(strings[i] + "  ]");
                }
            }
            stringBuffer.append("\n");
        }
        Log.d(TAG, stringBuffer.toString());
//        Toast.makeText(this, stringBuffer.toString(), Toast.LENGTH_SHORT).show();
    }

    private void init() {
        Log.d(TAG, "TeacherActivity--init()");
        /********************************
         *  日付の設定
         *******************************/
        text_date = (TextView) findViewById(R.id.text_date);
        text_date.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        /********************************
         *  時限スピナーの設定
         *******************************/
        sp_period = (Spinner) findViewById(R.id.sp_period);

        /********************************
         *  科目スピナーの設定
         *******************************/
        sp_subject = (Spinner) findViewById(R.id.sp_subject);
        List<String> list = new ArrayList<>();
        for (Subject s : teacher.getSubjects()) {
            list.add(s.getSub_id() + ":" + s.getSub_name());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
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
                // DO: 2017/06/13 当該教員と選択した科目IDに出席すべき学生リストを取得する非同期処理クラスの実装
                Toast.makeText(TeacherActivity.this, sub_id.split(":")[0], Toast.LENGTH_SHORT).show();
                new StudentListAsync().execute(sub_id.split(":")[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /********************************
         *  学生リストビューの設定
         *******************************/
        listView = (ListView) findViewById(R.id.listview);
        /********************************
         *  送信ボタンの初期設定
         *******************************/
//        bn_send = (Button) findViewById(R.id.bn_send);
//        bn_send.setOnClickListener(this);
        ImageButton ibn_post = (ImageButton) findViewById(R.id.ibn_postRest);
        ibn_post.setOnClickListener(this);
    }


}
