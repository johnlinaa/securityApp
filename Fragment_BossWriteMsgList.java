package com.khh.gjun.security.boss;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.khh.gjun.security.R;
import com.khh.gjun.security.apputility.AppUtility;
import com.khh.gjun.security.apputility.JsonAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Fragment_BossWriteMsgList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    //自訂UI物件
    private ListView listBossWriteMsg;
    private Button btnNewBossWriteMsg;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private List<JsonAll> data;
    private SwipeRefreshLayout mSwipeLayout;
    private EditText editBossWriteMsg;
    private Button btnBack_Bwrite;
    private Button btnBossMsgEnter;
    private EditText editBossWriteMsg_M;
    private Button btnBossMsgEnter_M;
    private Button btnBack_BwriteM;

    // TODO: Rename and change types and number of parameters
    public static Fragment_BossWriteMsgList newInstance(String param1, String param2) {
        Fragment_BossWriteMsgList fragment = new Fragment_BossWriteMsgList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_BossWriteMsgList() {
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
        View view = inflater.inflate(R.layout.fragment_bosswritemsglist, container, false);
        listBossWriteMsg = (ListView)view.findViewById(R.id.listBossWriteMsg);
        btnNewBossWriteMsg = (Button)view.findViewById(R.id.btnNewBossWriteMsg);


        //TODO 按鈕:新增主管提醒-Dialog
        btnNewBossWriteMsg.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //配置dialog的Layout
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View layout = layoutInflater.inflate(R.layout.list_dialog_bosswrotemsg,null);
                final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setView(layout)
                        .setCancelable(false)
                        .create();
                dialog.show();
                //dialog.getWindow().setLayout(1400, 1800);
                //問出成員
                editBossWriteMsg=(EditText)layout.findViewById(R.id.editBossWriteMsg);
                btnBack_Bwrite=(Button)layout.findViewById(R.id.btnBack_Bwrite);
                btnBossMsgEnter=(Button)layout.findViewById(R.id.btnBossMsgEnter);
                //按下返回鍵
                btnBack_Bwrite.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                //按下確定送出
                btnBossMsgEnter.setOnClickListener(
                        new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //非同步上傳資訊,之後送吐司+切畫面
                        BossWriteTask bossWriteTask = new BossWriteTask();
                        //輸入先防呆
                        if(editBossWriteMsg.getText().toString().length() < 5){
                            Toast.makeText(getActivity(),"請輸入5字以上",Toast.LENGTH_SHORT).show();
                        }else if(editBossWriteMsg.getText().toString().length() > 100){
                            Toast.makeText(getActivity(),"輸入內容不能超過100字",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String url = null;
                            try {
                                url = String.format(AppUtility.HOST + AppUtility.BossWriteMsgNew
                                        , AppUtility.PREFER_UserID
                                        , URLEncoder.encode(editBossWriteMsg.getText().toString(), "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                Log.i("編碼錯誤", e.getMessage());
                            }
                            bossWriteTask.execute(url);
                            dialog.dismiss();
                        }
                    }
                });
                /*
                //切換fragment
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment_BossWriteMsg fragment_bossWriteMsg = Fragment_BossWriteMsg.newInstance("", "");
                fragmentTransaction.replace(R.id.containBoss, fragment_bossWriteMsg);
                fragmentTransaction.commit();
                AppUtility.curentFragment = fragment_bossWriteMsg;
                */
            }
        });

        //TODO 按鈕:修改主管提醒
        listBossWriteMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View a1, final int position, long id) {

                Log.i("修改", parent + "###" + position + "");

                LayoutInflater layoutInflater = LayoutInflater.from(Fragment_BossWriteMsgList.this.getActivity());
                View layout = layoutInflater.inflate(R.layout.list_dialog_bosswritemsg_mod, null);
                final AlertDialog dialog = new AlertDialog.Builder(Fragment_BossWriteMsgList.this.getActivity())
                        .setView(layout)
                        .setCancelable(false)
                        .create();
                dialog.show();
                //dialog.getWindow().setLayout(1400, 1800);
                editBossWriteMsg_M = (EditText) layout.findViewById(R.id.editBossWriteMsg_M);


                btnBossMsgEnter_M = (Button) layout.findViewById(R.id.btnBossMsgEnter_M);
                btnBack_BwriteM = (Button) layout.findViewById(R.id.btnBack_BwriteM);
                editBossWriteMsg_M.setText(data.get(position).getMemoContent());

                //修改主管提醒-返回
                btnBack_BwriteM.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                //修改主管提醒-確定修改
                btnBossMsgEnter_M.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnBossMsgEnter_M.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //先判斷至少輸入幾字,以防沒有輸入就上傳資料庫
                                if (editBossWriteMsg_M.getText().toString().length() < 5) {
                                    Toast.makeText(getActivity(), "輸入至少5字以上", Toast.LENGTH_SHORT).show();
                                }else if(editBossWriteMsg_M.getText().toString().length() > 100){
                                    Toast.makeText(getActivity(),"輸入內容不能超過100字",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    //非同步上傳主管提醒訊息
                                    String url = null;
                                    try {
                                        url = String.format(AppUtility.HOST + AppUtility.BossWriteMsgMod,
                                                data.get(position).getMemoID(),
                                                URLEncoder.encode(editBossWriteMsg_M.getText().toString(), "UTF-8"));
                                    } catch (UnsupportedEncodingException e) {
                                        Log.i("編碼錯誤",e.getMessage());
                                    }
                                    BossWriteMsgTask bossWriteMsgTask = new BossWriteMsgTask();
                                    bossWriteMsgTask.execute(url);
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });

                    /*
                //1.把資料包進Intent
                Intent intent = new Intent(getActivity(),Fragment_BossWriteMsg_Mod.class);
                JsonAll jsonAll = data.get(position);
                intent.putExtra("bossmsg",jsonAll);
                getActivity().setIntent(intent);

                //2.切畫面
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment_BossWriteMsg_Mod fragment_bossWriteMsg_mod = Fragment_BossWriteMsg_Mod.newInstance("","");
                fragmentTransaction.replace(R.id.containBoss,fragment_bossWriteMsg_mod);
                fragmentTransaction.commit();

                AppUtility.curentFragment = fragment_bossWriteMsg_mod;
                 */
                    }
                });

        //非同步下載資料,再鋪畫面
        BossMsgTask bossMsgTask = new BossMsgTask();
        bossMsgTask.execute(AppUtility.HOST+AppUtility.BossWriteMsgList);

        mSwipeLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeLayout.setRefreshing(false);
                        //非同步下載資料,再鋪畫面
                        BossMsgTask bossMsgTask = new BossMsgTask();
                        bossMsgTask.execute(AppUtility.HOST + AppUtility.BossWriteMsgList);
                    }
                }, 1000);
            }
        });


        return view;
    }

    public class BossMsgTask extends AsyncTask<String,Void,String>{
        private ProgressDialog dialog;

        @Override
        protected String doInBackground(String... params) {
            Log.i("URL稽核",params[0]);

            return AppUtility.streamToString(AppUtility.openConnection(params[0], "GET"), "UTF-8");
        }

        //事前
        @Override
        protected void onPreExecute() {
            dialog = AppUtility.createDialog(getActivity(),"下載中...");
            dialog.show();
        }

        //事後
        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            Log.i("JSON稽核", s);

            data = new ArrayList<>();
            //把下載到的資訊鋪成畫面
            try {
                JSONArray jsonArray = new JSONArray(s);
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JsonAll jsonAll = new JsonAll();

                    jsonAll.setMemoID(jsonObject.getString("memoID"));
                    jsonAll.setUserID(jsonObject.getString("userID"));
                    jsonAll.setMemoContent(jsonObject.getString("memoContent"));
                    jsonAll.setUserName(jsonObject.getString("userName"));


                    DateFormat dateF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try {
                        date = dateF.parse(jsonObject.getString("createTime"));
                    } catch (ParseException e) {
                        Log.i("日期錯誤", e.getMessage());
                    }
                    jsonAll.setMemoTime(dateF.format(date));

                    data.add(jsonAll);
                }



            } catch (JSONException e) {
                Log.i("JSON問題", e.getMessage());
            }

            BossMsgAdapter bossMsgAdapter = new BossMsgAdapter();
            listBossWriteMsg.setAdapter(bossMsgAdapter);
        }
    }

    public class BossMsgAdapter extends BaseAdapter{
        private LayoutInflater inflater = LayoutInflater.from(getActivity());

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewTag tag = new ViewTag();

            if(convertView == null){
                convertView = inflater.inflate(R.layout.list_bosswritemsglist,null);
                tag.txtMemoTime = (TextView)convertView.findViewById(R.id.txtMemoTime);
                tag.txtMemoContent = (TextView)convertView.findViewById(R.id.txtMemoContent);
                convertView.setTag(tag);
            }else{
                tag = (ViewTag)convertView.getTag();
            }
            tag.txtMemoTime.setText(data.get(position).getMemoTime());
            tag.txtMemoContent.setText(data.get(position).getMemoContent());

            return convertView;
        }

        public class ViewTag{
            private TextView txtMemoTime;
            private TextView txtMemoContent;
        }
    }

    //TODO 新增主管提醒的Task
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

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment_BossWriteMsgList fragment_bossWriteMsgList = Fragment_BossWriteMsgList.newInstance("","");
            fragmentTransaction.replace(R.id.containBoss, fragment_bossWriteMsgList);
            fragmentTransaction.commit();
            AppUtility.curentFragment = fragment_bossWriteMsgList;

        }
    }

    //TODO 修改主管提醒的Task
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
