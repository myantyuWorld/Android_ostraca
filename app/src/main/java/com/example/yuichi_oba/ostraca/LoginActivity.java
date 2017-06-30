package com.example.yuichi_oba.ostraca;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.yuichi_oba.ostraca.models.Student;
import com.example.yuichi_oba.ostraca.models.Subject;
import com.example.yuichi_oba.ostraca.models.Teacher;
import com.example.yuichi_oba.ostraca.tools.Enum_URL;
import com.example.yuichi_oba.ostraca.tools.Util;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

/***
 * Google認証を行い、Emailアドレスによった画面に遷移するアクティビティ・処理
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {


    public static final String TAG = GetTokenAsync.class.getSimpleName();

    /***
     * Emailアドレスを基に当該ユーザー情報をRESTサーバーから取得する非同期処理
     */
    public class GetTokenAsync extends AsyncTask<Object, Void, Object> {

        public static final int STU_ID = 0;
        public static final int STU_NAME = 1;
        public static final int STU_RUBI = 2;
        public static final int STU_MAILADDR = 3;
        public static final int TEA_ID = 0;
        public static final int TEA_NAME = 1;
        public static final int TEA_FLG = 2;
        public static final int TEA_MAILADDR = 3;
        public static final String STUDENT_MAILADDR = "^.*@st.hsc.ac.jp$";
        public static final String TEACHER_MAILADDR = "^.*@gmail.com$"; // TODO: 2017/06/13 Debug用にGmailのドメインにしているよ！

        @Override
        protected Object doInBackground(Object... objects) {
            Log.d(TAG, "GetTokenAsync--doInBackGround");
            String email = objects[0].toString();
            String accessToken = "";
            try {
                Log.d("accessToken", GoogleAuthUtil.getToken(LoginActivity.this, email, SCOPE));
                accessToken = GoogleAuthUtil.getToken(LoginActivity.this, email, SCOPE);

                // 1
                // RESTサーバーにアクセスして、当該emailのユーザー情報を文字列で取得
                // 5151021, 大馬 裕一,      おおばゆういち,     5151021@st.hsc.ac.jp
                // 0000,    常勤 ツネオ,    1,                 5151021@hsc.ac.jp
                HttpURLConnection connection = Util.setConnectURL(Enum_URL.HOME_ANDROID.getText(), accessToken + "&email=" + email);
                // 2
                String result = Util.makeRequestToString(connection);
                Log.d("result", result);
                // 3
                // アクセス結果がnullまたはlengthが0でない
                if (!result.isEmpty()) {
                    String[] strings = result.split(",");
                    if (email.matches(STUDENT_MAILADDR)) {
                        // アドレスが学生のもの
                        Student student = new Student(strings[STU_ID], strings[STU_NAME], strings[STU_RUBI], strings[STU_MAILADDR]);
                        student.setAccessToken(accessToken);

                        return student;

                    } else if (email.matches(TEACHER_MAILADDR)) {
                        // アドレスが教員のもの
                        // 教員のインスタンス生成
                        Teacher teacher = new Teacher(strings[TEA_ID], strings[TEA_NAME], Integer.parseInt(strings[TEA_FLG]), strings[TEA_MAILADDR]);
                        teacher.setAccessToken(accessToken);

                        /***
                         * 教員の担当科目を取得するロジック
                         */
                        String json = null;
                        try {
                            // /teachers/class/subject/'tea_id'?=accessToken='token'
                            HttpURLConnection c = Util.setConnectURL(Enum_URL.TEACHERS_CLASS_SUBJECT.getText() + teacher.getTea_id(), teacher.getAccessToken());
                            // HTTP REQUEST の結果を文字列にして代入する
                            json = Util.makeRequestToString(c);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d("json", json);
                        /***
                         *
                         */
                        // result が nullまたは空文字でない
                        assert json != null;
                        if (!json.isEmpty()) {
                            ObjectMapper mapper = new ObjectMapper();
                            try {
                                // json --> java に変換する
                                List<Subject> subjects = mapper.readValue(json, new TypeReference<List<Subject>>() {
                                });
                                // 変換した科目リストをteacherインスタンスにセットする
                                teacher.setSubjects(subjects);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        teacher.show();
                        return teacher;
                    }
                }

            } catch (IOException | GoogleAuthException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            Log.d("call", "LoginActivity--GetTokenAsync--onPostExcute()");
            super.onPostExecute(o);
            // 引数がnullの場合エラー
            if (o == null) {
                // TODO: 2017/06/13 エラーメッセージの出力
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("警告")
                        .setMessage("ログインに失敗しました！")
                        .setPositiveButton("OK", null)
                        .show();

//                Toast.makeText(LoginActivity.this, "ログインに失敗しました", Toast.LENGTH_SHORT).show();
                Log.d("call", "o is null");
                return;
            }

            if (o instanceof Student) {           // 引数の型が　”Studentクラス”の場合
                Log.d("call", "result is student instance!");
                // DO: 2017/06/13   引数とともにStudentアクティビティに遷移する
                Intent intent = new Intent(getApplicationContext(), StudentActivity.class);
                intent.putExtra("number", 0);
                intent.putExtra("student", (Student) o);
                startActivity(intent);
            } else if (o instanceof Teacher) {    // 引数の型が　”Teacherクラス”の場合
                Log.d("call", "result is teacher instance!");
                Intent intent;
                switch (((Teacher) o).getTea_flg()) {
                    case 0:
                        Log.d(TAG, "--- 非常勤の教員 ---");
                        // DO: 2017/06/13   引数とともにTeacherアクティビティに遷移する
                        intent = new Intent(getApplicationContext(), TeacherActivity.class);
                        intent.putExtra("teacher", (Teacher) o);
                        startActivity(intent);
                        break;
                    case 1:
                        Log.d(TAG, "--- 常勤の教員 ---");
                        intent = new Intent(getApplicationContext(), TeacherActivity.class);
                        intent.putExtra("teacher", (Teacher) o);
                        startActivity(intent);
                        break;
                }
            }
        }
    }

    private static final int REQ_CODE = 9001;
    private static final String SCOPE = "oauth2:profile email";
    private SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初期化処理
        init();
    }

    // DO: 2017/06/13 アプリを落としたら、Googleからサインアウトする処理の実装
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Auth.GoogleSignInApi.signOut(googleApiClient);
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        Auth.GoogleSignInApi.signOut(googleApiClient);
//    }

    /// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // _/_/
    // _/_/ SignInボタン押下時の処理
    // _/_/
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    @Override
    public void onClick(View view) {
        signIn();
    }

    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // _/_/
    // _/_/ アクティビティ実行の結果の処理を行うオーバーライドメソッド
    // _/_/
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // _/_/
    // _/_/ 初期化メソッド
    // _/_/
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    private void init() {
        signInButton = (SignInButton) findViewById(R.id.bn_signIn);
        signInButton.setOnClickListener(this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
    }

    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // _/_/
    // _/_/ Googleサインインを行うメソッド
    // _/_/
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }

    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // _/_/
    // _/_/ Google認証を行った結果に応じた処理を行うメソッド
    // _/_/
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    private void handleResult(GoogleSignInResult result) {
        Log.d("call", "call handleResult()");
        // Google認証が成功した場合
        if (result.isSuccess()) {
            // Googleアカウントを取得する
            GoogleSignInAccount account = result.getSignInAccount();
            // アカウントからEmailアドレスを取得する
            assert account != null;
            String email = account.getEmail();
            Log.d("email", email);

            // email を基にして、RESTサーバーにGETリクエストを出して、
            // 当該emailアドレスのユーザーのインスタンスを生成する
            // email の ドメインによって教員、学生画面に遷移する
            new GetTokenAsync().execute(email);
        }
    }
}
