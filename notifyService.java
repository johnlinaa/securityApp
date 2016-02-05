package com.khh.gjun.security.apputility;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.khh.gjun.security.MainActivity;
import com.khh.gjun.security.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class notifyService extends Service {
    private Timer timer;
    private NotificationManager notificationManager;
    private ArrayList<TempProblemList> arrayList;


    //建構子
    public notifyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //建構時鐘器的機殼
        timer = new Timer(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //時鐘器裝上機芯,機芯裡有一條執行續,執行服務要的東西
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                ServiceTask serviceTask = new ServiceTask();
                serviceTask.execute(AppUtility.HOST+AppUtility.Tempproblemlist);
            }
        },5000,5000);


        return super.onStartCommand(intent, flags, startId);
    }

    public class ServiceTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            String msg = AppUtility.streamToString(AppUtility.openConnection(params[0], "GET"),"UTF-8");

            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            //把抓到的資訊鋪成通知


            arrayList = new ArrayList<>();
            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);


            try {
                JSONArray jsonArray = new JSONArray(s);
                for(int i = 0; i < jsonArray.length(); i++){
                    //物件必須宣告在裡面,否則ADD到同一物件,其欄位的值都會一樣
                    TempProblemList tempProblemList = new TempProblemList();

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    tempProblemList.setTempLocation(jsonObject.getString("tempLocation"));
                    tempProblemList.setTemperature(jsonObject.getString("temperature"));
                    tempProblemList.setWet(jsonObject.getString("wet"));
                    tempProblemList.setTempTime(jsonObject.getString("tempTime"));
                    tempProblemList.setTempRecordID(jsonObject.getString("tempRecordID"));
                    if(jsonObject.getString("tempNotify").equals("true")){
                        tempProblemList.setTempNotify(1);
                    }else{
                        tempProblemList.setTempNotify(0);
                    }


                    arrayList.add(tempProblemList);


                }

            } catch (JSONException e) {
                Log.i("JSON問題",e.getMessage());
            }


            Log.i("array長度",arrayList.size()+"");
            for(int i = 0; i < arrayList.size(); i++) {

                TempProblemList record = arrayList.get(i);
                if(record.getTempNotify() == 0) {
                    String content = String.format("地點:%s 溫度:%s 濕度:%s"
                            , record.getTempLocation()
                            , record.getTemperature()
                            , record.getWet());



                    Notification notify = new Notification.Builder(getBaseContext())
                            .setSmallIcon(R.mipmap.alert)
                            .setContentTitle("溫度異常")
                            .setContentText(content)
                            .setOnlyAlertOnce(false)

                            .build();
                    notificationManager.notify(i, notify);


                }
            }
        }
    }

    public class NotifyModTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            return AppUtility.streamToString(AppUtility.openConnection(params[0], "GET"),"UTF-8");
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i("稽核服務",s);
        }
    }





}
