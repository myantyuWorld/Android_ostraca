package com.example.yuichi_oba.ostraca.tools;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by Yuichi-Oba on 2017/05/31.
 */

public class Util {

    static final String SCOPE = "oauth2:profile email";
    static final String PARAM_1 = "?accessToken=";
    static final String GET = "GET";

    /***
     * HTTPの接続状態の設定を行うメソッド
     * @param urlStr
     * @param token
     * @return
     * @throws IOException
     */
    public static HttpURLConnection setConnectURL(String urlStr, String token) throws IOException {
        Log.d("call", "call Util.setConnectURL() ");
        HttpURLConnection c = null;
        URL url = new URL(urlStr + PARAM_1 + token);
        Log.d("call", urlStr + PARAM_1 + token);

        c = (HttpURLConnection) url.openConnection();
        c.setRequestMethod(GET);
        c.setRequestProperty("Content-type", "text/plain");

        return c;
    }


    /***
     * HTTPリクエストをだしてHTTP_OKだった場合のレスポンスを文字列で返すメソッド
     * @param c
     * @return
     * @throws IOException
     */
    public static String makeRequestToString(HttpURLConnection c) throws IOException {
        Log.d("call", "Util--makeRequestToString()");
        StringBuilder sb = new StringBuilder();
        if (c.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStreamReader isr = new InputStreamReader(c.getInputStream(),
                    StandardCharsets.UTF_8);
                 BufferedReader reader = new BufferedReader(isr)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    System.out.println(line);
                }
            }
        }
        Log.d("call", sb.toString());
        return sb.toString();
    }
}
