package com.khh.gjun.security;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.khh.gjun.security.apputility.AppUtility;
import com.khh.gjun.security.boss.BossActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Login2Activity extends AppCompatActivity {
    private EditText userID2;
    private EditText password2;
    private Button btnLogin2;
    //驗證成功或失敗
    private String verify = "Error";


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        setTitle("登入");

        userID2 = (EditText)findViewById(R.id.userID2);
        password2 = (EditText)findViewById(R.id.password2);
        btnLogin2 = (Button)findViewById(R.id.btnLogin2);

        //按鈕:登入
        //1.非同步傳送帳密到資料庫做比對,等他回傳訊息
        //2-1.假設驗證成功回傳正確訊息和全球唯一識別碼存入偏好設定,並切換機動
        //2-2.假設驗證失敗回傳錯誤訊息,不切換機動
        btnLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userID2.getText().toString().isEmpty() || password2.getText().toString().isEmpty()){
                    Toast.makeText(getBaseContext(),"帳號密碼不能為空",Toast.LENGTH_SHORT).show();
                }else if((userID2.getText().toString().length() > 10) || (password2.getText().toString().length() > 10)) {
                    Toast.makeText(getBaseContext(),"帳號或密碼不能大於10個字",Toast.LENGTH_SHORT).show();
                }else {
                    //非同步
                    String id = userID2.getText().toString();
                    String pass = password2.getText().toString();
                    String url = String.format(AppUtility.HOST + AppUtility.URL_Verify, id, pass);
                    //String url = AppUtility.URL_login_succ;

                    LoginTask loginTask = new LoginTask();
                    loginTask.execute(url);
                }
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });




    }
    //非同步驗證帳密
    private class LoginTask extends AsyncTask<String,Void,String>{
        private ProgressDialog dialog;

        @Override
        protected String doInBackground(String... params) {
            String jsonString = AppUtility.streamToString(AppUtility.openConnection(params[0], "GET"),"UTF-8");
            Log.i("URL登入",jsonString);
            return jsonString;
        }

        //事前
        @Override
        protected void onPreExecute() {
            dialog =AppUtility.createDialog(Login2Activity.this,"驗證中...");
            dialog.show();
        }

        //事後
        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();

            JSONArray jsonArray = null;
            JSONObject jsonObject = null;
            //解析json資料,並把(userID,userName,職稱,全球唯一識別碼)存入偏好設定
            try {
                jsonArray = new JSONArray(s);
                jsonObject = jsonArray.getJSONObject(0);
                verify = jsonObject.getString("verify");


            } catch (JSONException e) {
                Log.i("Json問題",e.getMessage());
            }
            //2-1 驗證成功
            if (verify.equals("Success")) {
                //資訊先放入AppUtility
                try {
                    AppUtility.PREFER_UserID = jsonObject.getString("userID");
                    AppUtility.PREFER_Name = jsonObject.getString("userName");
                    AppUtility.PREFER_Title = jsonObject.getString("title");
                    //AppUtility.PREFER_Unique = jsonObject.getString("shareCode");

                } catch (JSONException e) {
                    Log.i("Json問題", e.getMessage());
                }
                /*
                //把資訊存入偏好設定
                SharedPreferences preferences = getSharedPreferences("preferLogin",MODE_PRIVATE);
                String msg = preferences.getString("PREFER_Unique", "none");
                if(msg.equals("none")){
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("PREFER_UserID",AppUtility.PREFER_UserID);
                    editor.putString("PREFER_UserName",AppUtility.PREFER_UserName);
                    editor.putString("PREFER_Title",AppUtility.PREFER_Title);
                    editor.putString("PREFER_Unique",AppUtility.PREFER_Unique);
                    editor.commit();
                }else{
                    //把資訊鋪到抽屜的顯示

                }
                */
                Toast.makeText(Login2Activity.this,"登入成功",Toast.LENGTH_LONG).show();

                //切換機動
                if(AppUtility.PREFER_Title.equals("保全")) {
                    Intent intent = new Intent(Login2Activity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if(AppUtility.PREFER_Title.equals("主管") || AppUtility.PREFER_Title.equals("總經理")){
                    Intent intent = new Intent(Login2Activity.this, BossActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getBaseContext(),"身份未認可,請詢問人事部",Toast.LENGTH_LONG);
                }

            //2-2 驗證失敗
            } else if (verify.equals("Error")) {
                Toast.makeText(Login2Activity.this, "登入失敗,請重新輸入", Toast.LENGTH_LONG).show();
            }


        }



    }


}
