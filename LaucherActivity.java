package com.khh.gjun.security;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.khh.gjun.security.apputility.notifyService;

import java.util.List;

public class LaucherActivity extends AppCompatActivity {

    //Wifi掃描、開啟
    private String SSID;
    private int LEVEL;
    private int NETWORKID;
    private int IPADRRESS;
    private String IP;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laucher);


        //首先取得Wi-Fi服務控制Manager
        WifiManager mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        //先判斷是否有開啟Wi-Fi，有開啟則回傳true沒有則回傳false
        if(mWifiManager.isWifiEnabled()) {
            //重新掃描Wi-Fi資訊
            mWifiManager.startScan();
            //偵測周圍的Wi-Fi環境(因為會有很多組Wi-Fi，所以型態為List)
            List<ScanResult> mWifiScanResultList = mWifiManager.getScanResults();
            //手機內已存的Wi-Fi資訊(因為會有很多組Wi-Fi，所以型態為List)
            List<WifiConfiguration> mWifiConfigurationList = mWifiManager.getConfiguredNetworks();
            Log.i("手機內已存的 Wi-Fi資訊", mWifiConfigurationList + "");
            //目前已連線的Wi-Fi資訊
            WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
            Log.i("目前已連線的 Wi-Fi資訊",mWifiInfo+"");

            for(int i = 0 ; i < mWifiScanResultList.size() ; i++ ) {
                //手機目前周圍的Wi-Fi環境----Wi-Fi名稱、Wi-Fi訊號強弱
                SSID  = mWifiScanResultList.get(i).SSID ;
                LEVEL = mWifiScanResultList.get(i).level ;
                Log.i("周圍的 Wi-Fi名稱",SSID+"");
                Log.i("周圍的 Wi-Fi訊號強弱",LEVEL+"");
            }

            for(int i = 0 ; i < mWifiConfigurationList.size() ; i++ ) {
                //手機內已儲存(已連線過)的Wi-Fi資訊----Wi-Fi名稱、Wi-Fi連線ID
                SSID  = mWifiConfigurationList.get(i).SSID ;
                NETWORKID = mWifiConfigurationList.get(i).networkId ;
                Log.i("已連線過的 Wi-Fi名稱",SSID+"");
                Log.i("已連線過的 Wi-Fi連線ID",NETWORKID+"");
            }

            //目前手機已連線(現在連線)的Wi-Fi資訊----(Wi-Fi名稱)(Wi-Fi連線ID)(Wi-Fi連線位置)(Wi-Fi IP位置)
            SSID  = mWifiInfo.getSSID() ;
            NETWORKID  = mWifiInfo.getNetworkId() ;
            IPADRRESS  = mWifiInfo.getIpAddress() ;
            IP  = String.format("%d.%d.%d.%d", (IPADRRESS & 0xff), (IPADRRESS >> 8 & 0xff), (IPADRRESS >> 16 & 0xff), (IPADRRESS >> 24 & 0xff)) ;
            Log.i("現在連線的 Wi-Fi名稱",SSID+"");
            Log.i("現在連線的 Wi-Fi連線ID",NETWORKID+"");
            Log.i("現在連線的 Wi-Fi連線位置",IPADRRESS+"");
            Log.i("現在連線的 Wi-Fi IP位置",IP+"");

        }else {
            //把Wi-Fi開啟
            mWifiManager.setWifiEnabled(true);
            Toast.makeText(LaucherActivity.this, "Wi-Fi開啟中", Toast.LENGTH_SHORT).show();
        }


        //開啟服務
        Intent intent = new Intent(this, notifyService.class);
        startService(intent);

        handler=new Handler();

        handler.postDelayed(

                new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(LaucherActivity.this, Login2Activity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                , 3000);

    }

}
