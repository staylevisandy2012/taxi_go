package com.dk.main;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by DK on 2017/7/6.
 */

public class phpConnection {
    public static String createConnection(String urlString, ArrayList<String> key, ArrayList<String> value) {
        String responseString = null;
//        String urlString = "http://140.128.98.46/taxi_go/query_user.php";
        HttpURLConnection connection = null;

        try {
            // 初始化 URL
            URL url = new URL(urlString);
            // 取得連線物件
            connection = (HttpURLConnection) url.openConnection();
            // 設定 request timeout
            connection.setReadTimeout(1500);
            connection.setConnectTimeout(1500);
            connection.setDoOutput(true);// 設置此方法,允許向伺服器輸出內容

            // post請求的參數
//            String data = String.format("account=%s&password=%s", account, password);

            //取得外部傳入的參數
            //key:value

            String data = "";
//            account=%s&password=%s
            for (int i = 0; i < key.size(); i++) {
                data += String.format("%s=%s", key.get(i), value.get(i));
                if (i != key.size() - 1) {
                    data += "&";
                }
            }
//            Log.i("phpConnection",urlString);
            Log.i("phpConnection",data);
            OutputStream out = connection.getOutputStream();// 產生一個OutputStream，用來向伺服器傳數據
            out.write(data.getBytes());
            out.flush();
            out.close();
            // 模擬 Chrome 的 user agent, 因為手機的網頁內容較不完整
//            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36");
            // 設定開啟自動轉址
            connection.setInstanceFollowRedirects(true);

            // 若要求回傳 200 OK 表示成功取得網頁內容
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                // 讀取網頁內容
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String tempStr;
                StringBuffer stringBuffer = new StringBuffer();

                while ((tempStr = bufferedReader.readLine()) != null) {
                    stringBuffer.append(tempStr);
                }

                bufferedReader.close();
                inputStream.close();

                /*
                // 取得網頁內容類型
                String mime = connection.getContentType();
                boolean isMediaStream = false;

                // 判斷是否為串流檔案
                if (mime.indexOf("audio") == 0 || mime.indexOf("video") == 0) {
                    isMediaStream = true;
                }
                 */
                // 網頁內容字串

                responseString = stringBuffer.toString();
                Log.i("phpConnection",responseString);
            }
            else{
                Log.i("test","Connection fail");

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 中斷連線
            if (connection != null) {
                connection.disconnect();
            }
        }
        return responseString;
    }

    }
