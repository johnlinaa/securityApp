package com.khh.gjun.security;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
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

import com.khh.gjun.security.apputility.AppUtility;
import com.khh.gjun.security.apputility.notifyService;
import com.khh.gjun.security.boss.Fragment_EventRecord;
import com.khh.gjun.security.boss.Fragment_TempRecord;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private TextView na_Name;
    private TextView na_Title;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&& event.getRepeatCount() == 0){
            dialog();
        }return false;
    }
    protected void dialog() {
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
//        builder.setTitle("提示");
                .setTitle("提示")
                .setMessage("確認退出嗎？")
                .setCancelable(false)
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        MainActivity.this.finish();
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
        setContentView(R.layout.activity_main);




        //設定初始頁面
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Fragment_BossMsg fragment_bossMsg = Fragment_BossMsg.newInstance("","");
        fragmentTransaction.replace(R.id.contain, fragment_bossMsg);
        fragmentTransaction.commit();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        //設定抽屜資訊
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerLayout =
                navigationView.inflateHeaderView(R.layout.nav_header_main);
        na_Name = (TextView)headerLayout.findViewById(R.id.na_name);
        na_Title = (TextView)headerLayout.findViewById(R.id.na_title);
        na_Name.setText(AppUtility.PREFER_Name);
        na_Title.setText(AppUtility.PREFER_Title);

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
        getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.nav_bossmsg) {
            //主管提醒頁面
            Fragment_BossMsg fragment_bossMsg = Fragment_BossMsg.newInstance("", "");
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contain, fragment_bossMsg);
            fragmentTransaction.commit();


        } else if (id == R.id.nav_nohandle) {
            //未處理頁面
            Fragment_NoHandle fragment_noHandle = Fragment_NoHandle.newInstance("", "");
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contain, fragment_noHandle);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_event) {
            //事件回報頁面
            Fragment_EventList fragment_eventList = Fragment_EventList.newInstance("","");
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contain, fragment_eventList);
            fragmentTransaction.commit();


        } else if (id == R.id.nav_temphandle) {
            //溫度異常頁面
            Fragment_TempProblemList fragment_tempProblemList = Fragment_TempProblemList.newInstance("","");
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contain, fragment_tempProblemList);
            fragmentTransaction.commit();



        } else if (id == R.id.nav_map) {
            //地圖
            Fragment_Map fragment_map = Fragment_Map.newInstance("", "");
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contain,fragment_map);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_logout) {
            //登出
            Intent intent = new Intent(this,Login2Activity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
