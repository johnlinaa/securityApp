package com.khh.gjun.security.boss;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.khh.gjun.security.Fragment_EventList;
import com.khh.gjun.security.R;
import com.khh.gjun.security.apputility.AppUtility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class Fragment_BossWriteMsg extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //自訂UI物件
    private EditText editBossWriteMsg;
    private Button btnBossMsgEnter;
    private Button btnBack_Bwrite;

    // TODO: Rename and change types and number of parameters
    public static Fragment_BossWriteMsg newInstance(String param1, String param2) {
        Fragment_BossWriteMsg fragment = new Fragment_BossWriteMsg();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_BossWriteMsg() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getActivity().setTitle("主管提醒");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bosswritemsg, container, false);
        editBossWriteMsg = (EditText)view.findViewById(R.id.editBossWriteMsg);
        btnBossMsgEnter = (Button)view.findViewById(R.id.btnBossMsgEnter);
        btnBack_Bwrite = (Button)view.findViewById(R.id.btnBack_Bwrite);


        //TODO 按鈕:主管提醒訊息送出
        btnBossMsgEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //非同步上傳資訊,之後送吐司+切畫面
                BossWriteTask bossWriteTask = new BossWriteTask();

                //輸入先防呆
                if(editBossWriteMsg.getText().toString().length() < 5){
                    Toast.makeText(getActivity(),"請輸入5字以上",Toast.LENGTH_SHORT).show();
                }else {
                    String url = null;
                    try {
                        url = String.format(AppUtility.HOST + AppUtility.BossWriteMsgNew
                                , AppUtility.PREFER_UserID
                                , URLEncoder.encode(editBossWriteMsg.getText().toString(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        Log.i("編碼錯誤", e.getMessage());
                    }
                    bossWriteTask.execute(url);
                }
            }
        });

        //TODO 按鈕:返回
        btnBack_Bwrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment_BossWriteMsgList fragment_bossWriteMsgList = Fragment_BossWriteMsgList.newInstance("","");
                fragmentTransaction.replace(R.id.containBoss,fragment_bossWriteMsgList);
                fragmentTransaction.commit();

                AppUtility.curentFragment = fragment_bossWriteMsgList;

            }
        });

        return view;
    }

    public class BossWriteTask extends AsyncTask<String,Void,String>{
        private ProgressDialog dialog;

        @Override
        protected String doInBackground(String... params) {
            Log.i("URL稽核",params[0]);
            return AppUtility.streamToString(AppUtility.openConnection(params[0], "GET"), "UTF-8");
        }
        //事前
        @Override
        protected void onPreExecute() {
            dialog = AppUtility.createDialog(getActivity(), "上傳中...");
            dialog.show();
        }
        //事後
        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            Log.i("JSON內容稽核", s);

            if(s.equals("Success")){
                Toast.makeText(getActivity(), "新增成功", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(),"新增失敗",Toast.LENGTH_LONG).show();
            }

            //2.再切畫面
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment_BossWriteMsgList fragment_bossWriteMsgList = Fragment_BossWriteMsgList.newInstance("","");
            fragmentTransaction.replace(R.id.containBoss, fragment_bossWriteMsgList);
            fragmentTransaction.commit();

            AppUtility.curentFragment = fragment_bossWriteMsgList;
        }
    }

}
