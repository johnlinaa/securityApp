package com.khh.gjun.security.apputility;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.Window;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by student on 2015/12/3.
 */
public class AppUtility {
    //TODO 偏好設定儲存區
    public static String PREFER_UserID;
    public static String PREFER_UserName;
    public static String PREFER_Name;
    public static String PREFER_Title;
    public static String PREFER_Unique; //全球唯一識別碼

    public static Fragment curentFragment;

    //public static ArrayList<JsonAll> MapData = new ArrayList<>();
    //TODO URL區域
    //伺服器路徑
    public static final String HOST = "http://192.168.0.102:8080/GjunService/rest";
    //public static final String HOST = "http://192.168.12.148:8080/Security/service";
    //登入
    public static final String URL_Verify = "/UserLogin/UserCheck?Account=%s&Password=%s";

    //服務URL
    public static final String URL_NotifyMod = "/Preservation/SetNotify?TempRecordID=%s";

    //MAP
    public static final String MAP = "/UserLogin/MapCheck";
    //========================<保全URL>=================================================
    //主管提醒
    public static final String BOSSMSG="/Preservation/tip";

    //未處理
    public static final String NOHANDLE="/Preservation/Untreated";
    public static final String NOHANDLE_MOD="/Preservation/ChangeIsNot?EventID=%s";

    //事件回報
    public static final String URL_EventList = "/Preservation/ListEventReport";//今日事件列表
    public static final String URL_EventReportEnter = "/Preservation/AddEvent?UserID=%s&RFID_ID=%d&EventName=%s&EventLocation=%s&EventLevel=%s&IsNot=%d&EventContent=%s";//事件回報
    public static final String URL_EventMod = "/Preservation/EditEvent?EventID=%d&RFID_ID=%d&EventName=%s&EventLocation=%s&EventLevel=%s&IsNot=%d&EventContent=%s";//事件修改

    //溫度異常回報
    public static final String Tempproblemlist="/Preservation/TempError"; //溫度異常列表
    public static final String TempHandle = "/Preservation/AddTempHandle?TempRecordID=%s&UserID=%s&HandleState=%s&HandleContent=%s";
    public static final String TempHandle_Mod = "/Preservation/EditTempHandle?TempRecordID=%s&UserID=%s&HandleState=%s&HandleContent=%s";



    //========================<主管URL>=================================================
    //主管提醒
    public static final String BossWriteMsgList = "/Boss/tip";
    public static final String BossWriteMsgNew = "/Boss/addtip?UserID=%s&MemoContent=%s";
    public static final String BossWriteMsgMod = "/Boss/changetip?MemoID=%s&MemoContent=%s";

    //主管事件報表
    public static final String EventListAll = "/Boss/alltimeevent?StarDay=%s&EndDay=%s";
    public static final String EventListS = "/Boss/alltimeevent?StarDay=%s&EndDay=%s&IsNot=%s";
    //主管溫度報表
    public static final String TempListAll = "/Boss/alltimetemp?StarDay=%s&EndDay=%s";
    public static final String TempListE = "/Boss/alltimetemp?StarDay=%s&EndDay=%s&Error=%s";
    public static final String TempListES = "/Boss/alltimetemp?StarDay=%s&EndDay=%s&Error=%s&State=%s";


    //其他
    //public static String TEMP2="https://28d9c41f-a-62cb3a1a-s-sites.googlegroups.com/site/betterjson/data/Temp3.json";
    //public static final String HOST = "http://192.168.12.55:8080/Security/service";

    //public static final String EVENTLIST_RECORD = "https://af31add1-a-62cb3a1a-s-sites.googlegroups.com/site/evenjson2/data/EventList.json";
    //public static final String URL_EventList = "http://192.168.12.209:8080/Security/service/Preservation/Untreated";
    //public static final String URL_EventList =  "https://af31add1-a-62cb3a1a-s-sites.googlegroups.com/site/evenjson2/data/URL_EventList.json";
    //public static final String URL_login_Error = "https://af31add1-a-62cb3a1a-s-sites.googlegroups.com/site/evenjson2/data/login_Error.json";
    //public static final String URL_login_succ = "https://af31add1-a-62cb3a1a-s-sites.googlegroups.com/site/evenjson2/data/login_succ.json";
    //通知:抓溫度異常
    //public static final String URL_TempError = "https://af31add1-a-62cb3a1a-s-sites.googlegroups.com/site/evenjson2/data/tempError.json";



    public static ProgressDialog createDialog(Context context,String msg){
        ProgressDialog dialog = new ProgressDialog(context,android.R.style.Theme_Holo_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setMessage(msg);
        //dialog.setCancelable(false);
        dialog.show();

        return dialog;
    }

    public static InputStream openConnection(String urlString,String method){
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpUrl = (HttpURLConnection)url.openConnection();
            httpUrl.setRequestMethod(method);
            is = httpUrl.getInputStream();

        } catch (MalformedURLException e) {
            Log.i("URL格式錯誤",e.getMessage());
        } catch (IOException e) {
            Log.i("IO錯誤喔", e.getMessage());
        }


        return is;

    }

    //InputStream轉String
    public static String streamToString(InputStream is, String encode){
        StringBuilder sb = null;
        InputStreamReader reader = null;



        try {
            reader = new InputStreamReader(is, encode);
            //讀取步幅
            char[] step = new char[10];
            sb = new StringBuilder();

            int realLength = -1;
            while((realLength = reader.read(step)) > -1){
                sb.append(step,0,realLength);
            }


        } catch (UnsupportedEncodingException e) {
            Log.i("編碼錯誤", e.getMessage());
        } catch (IOException e) {
            Log.i("IO錯誤", e.getMessage());
        }

        try {
            reader.close();
        } catch (IOException e) {
            Log.i("IO錯誤", e.getMessage());
        }

        return sb.toString();
    }


    //TODO 把時間修改成年月日 時分秒格式
    public static String covertDate(String dateString){

        DateFormat dateF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateF.parse(dateString);

            return dateF.format(date);

        } catch (ParseException e) {
            Log.i("日期錯誤",e.getMessage());

            return null;
        }
    }


}
