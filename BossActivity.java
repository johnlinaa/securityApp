package com.khh.gjun.security.boss;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.khh.gjun.security.Login2Activity;
import com.khh.gjun.security.R;
import com.khh.gjun.security.apputility.AppUtility;

public class BossActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView na_Name2;
    private TextView na_Title2;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&& event.getRepeatCount() == 0){
            dialog();
        }return false;
    }
    protected void dialog() {
        AlertDialog dialog = new AlertDialog.Builder(BossActivity.this)
//        builder.setTitle("提示");
                .setTitle("提示")
                .setMessage("確認退出嗎？")
                .setCancelable(false)
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                       BossActivity.this.finish();
                    }
                })
                .setNegativeButton("返回", null)
                .create();
        WindowManager.LayoutParams lp=dialog.getWindow().getAttributes();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.getWindow().setAttributes(lp);
        lp.alpha=0.8f;//透明度，黑暗度为lp.dimAmount=1.0f;



        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss);
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment_BossNoHandle bossNoHandle = Fragment_BossNoHandle.newInstance("", "");
        fragmentTransaction.add(R.id.containBoss, bossNoHandle);
        fragmentTransaction.commit();
        AppUtility.curentFragment = bossNoHandle;


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_boss);
        na_Name2 = (TextView)headerLayout.findViewById(R.id.na_name2);
        na_Title2 = (TextView)headerLayout.findViewById(R.id.na_title2);

        na_Name2.setText(AppUtility.PREFER_Name);
        na_Title2.setText(AppUtility.PREFER_Title);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.boss, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_nohandle) {
            //未處理分頁
            FragmentManager fragmentManager = this.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment_BossNoHandle bossNoHandle = Fragment_BossNoHandle.newInstance("", "");

            if(AppUtility.curentFragment instanceof Fragment_EventRecord_Detail){
                fragmentTransaction.remove(AppUtility.curentFragment);
                fragmentTransaction.replace(R.id.containBoss,bossNoHandle);
                fragmentTransaction.commit();
            }else {
                fragmentTransaction.replace(R.id.containBoss, bossNoHandle);
                fragmentTransaction.commit();
            }
            AppUtility.curentFragment = bossNoHandle;

        } else if(id == R.id.nav_bosswritemsg){
            //主管寫提醒分頁
            FragmentManager fragmentManager = this.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment_BossWriteMsgList fragment_bossWriteMsgList = Fragment_BossWriteMsgList.newInstance("","");

            if(AppUtility.curentFragment instanceof Fragment_EventRecord_Detail){
                fragmentTransaction.remove(AppUtility.curentFragment);
                fragmentTransaction.replace(R.id.containBoss,fragment_bossWriteMsgList);
                fragmentTransaction.commit();
            }else {
                fragmentTransaction.replace(R.id.containBoss, fragment_bossWriteMsgList);
                fragmentTransaction.commit();
            }

            AppUtility.curentFragment = fragment_bossWriteMsgList;

        } else if (id == R.id.nav_map) {
            //地圖分頁
            FragmentManager fragmentManager = this.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment_BossMap bossMap = Fragment_BossMap.newInstance("", "");

            if(AppUtility.curentFragment instanceof Fragment_EventRecord_Detail){
                fragmentTransaction.remove(AppUtility.curentFragment);
                fragmentTransaction.replace(R.id.containBoss,bossMap);
                fragmentTransaction.commit();
            }else{

                fragmentTransaction.replace(R.id.containBoss,bossMap);
                fragmentTransaction.commit();
            }

            AppUtility.curentFragment = bossMap;

        } else if (id == R.id.nav_eventrecord) {
            //事件報表分頁
            FragmentManager fragmentManager = this.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment_EventRecord fragment_eventRecord = Fragment_EventRecord.newInstance("", "");

            if(AppUtility.curentFragment instanceof Fragment_EventRecord_Detail){
                fragmentTransaction.remove(AppUtility.curentFragment);
                fragmentTransaction.replace(R.id.containBoss,fragment_eventRecord);
                fragmentTransaction.commit();
            }else {
                fragmentTransaction.replace(R.id.containBoss, fragment_eventRecord);
                fragmentTransaction.commit();
            }

            AppUtility.curentFragment = fragment_eventRecord;

        } else if (id == R.id.nav_temprecord) {
            //溫度報表分頁
            FragmentManager fragmentManager = this.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment_TempRecord fragment_tempRecord = Fragment_TempRecord.newInstance("","");

            if(AppUtility.curentFragment instanceof Fragment_EventRecord_Detail){
                fragmentTransaction.remove(AppUtility.curentFragment);
                fragmentTransaction.replace(R.id.containBoss,fragment_tempRecord);
                fragmentTransaction.commit();
            }else {
                fragmentTransaction.replace(R.id.containBoss, fragment_tempRecord);
                fragmentTransaction.commit();
            }

            AppUtility.curentFragment = fragment_tempRecord;

        } else if (id == R.id.nav_logout) {
            //登出
            //清除手機上的全球唯一識別碼
            //切到登入的機動,並釋放本機動
            Intent intent = new Intent(this, Login2Activity.class);
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
