package com.khh.gjun.security.boss;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.khh.gjun.security.R;
import com.khh.gjun.security.apputility.AppUtility;
import com.khh.gjun.security.apputility.JsonAll;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_BossWriteMsg_Mod#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_BossWriteMsg_Mod extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //自訂UI物件
    private EditText editBossWriteMsg_M;
    private Button btnBossMsgEnter_M;
    private Button btnBack_BwriteM;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_BossWriteMsg_Mod.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_BossWriteMsg_Mod newInstance(String param1, String param2) {
        Fragment_BossWriteMsg_Mod fragment = new Fragment_BossWriteMsg_Mod();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_BossWriteMsg_Mod() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getActivity().setTitle("修改主管提醒");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bosswritemsg_mod, container, false);
        editBossWriteMsg_M = (EditText)view.findViewById(R.id.editBossWriteMsg_M);
        btnBossMsgEnter_M = (Button)view.findViewById(R.id.btnBossMsgEnter_M);
        btnBack_BwriteM = (Button)view.findViewById(R.id.btnBack_BwriteM);

        //先鋪Mod頁面的畫面
        final JsonAll jsonAll = (JsonAll)getActivity().getIntent().getSerializableExtra("bossmsg");
        editBossWriteMsg_M.setText(jsonAll.getMemoContent());

        //TODO 按鈕:確定修改主管提醒
        btnBossMsgEnter_M.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先判斷至少輸入幾字,以防沒有輸入就上傳資料庫
                if(editBossWriteMsg_M.getText().toString().length() < 5){
                    Toast.makeText(getActivity(),"輸入至少5字以上",Toast.LENGTH_SHORT).show();
                }else{
                    //非同步上傳主管提醒訊息
                    String url = null;
                    try {
                        url = String.format(AppUtility.HOST+AppUtility.BossWriteMsgMod
                                ,jsonAll.getMemoID()
                                , URLEncoder.encode(editBossWriteMsg_M.getText().toString(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        Log.i("編碼錯誤",e.getMessage());
                    }


                    BossWriteMsgTask bossWriteMsgTask = new BossWriteMsgTask();
                    bossWriteMsgTask.execute(url);
                }
            }
        });


        //TODO 按鈕:返回
        btnBack_BwriteM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //切畫面
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

    public class BossWriteMsgTask extends AsyncTask<String,Void,String>{

        private ProgressDialog dialog;

        @Override
        protected String doInBackground(String... params) {
            Log.i("URL稽核",params[0]);
            return AppUtility.streamToString(AppUtility.openConnection(params[0], "GET"), "UTF-8");
        }

        //事前
        @Override
        protected void onPreExecute() {
            dialog = AppUtility.createDialog(getActivity(),"上傳更新中...");
            dialog.show();
        }

        //事後
        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();

            Log.i("is成功", s);

            if(s.equals("Success")){
                Toast.makeText(getActivity(),"更新成功",Toast.LENGTH_LONG).show();

                //切換畫面
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment_BossWriteMsgList fragment_bossWriteMsgList = Fragment_BossWriteMsgList.newInstance("","");
                fragmentTransaction.replace(R.id.containBoss,fragment_bossWriteMsgList);
                fragmentTransaction.commit();

                AppUtility.curentFragment = fragment_bossWriteMsgList;

            }else{
                Toast.makeText(getActivity(),"更新失敗",Toast.LENGTH_LONG).show();
            }
        }
    }

}
