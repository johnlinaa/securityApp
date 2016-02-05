package com.khh.gjun.security;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.khh.gjun.security.apputility.AppUtility;
import com.khh.gjun.security.apputility.BossMsg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;



public class Fragment_BossMsg extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView listBossMsg;
    private List<BossMsg> data;
    private SwipeRefreshLayout mSwipeLayout;


    // TODO: Rename and change types and number of parameters
    public static Fragment_BossMsg newInstance(String param1, String param2) {
        Fragment_BossMsg fragment = new Fragment_BossMsg();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_BossMsg() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            this.getActivity().setTitle("提醒事項");
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_bossmsg, container, false);

        listBossMsg=(ListView)view.findViewById(R.id.listBossMsg);
        /*
        BossMsgAdater adater=new BossMsgAdater();
        listBossMsg.setAdapter(adater);
        */

        JsonBossMsgTask jsonDataTask = new JsonBossMsgTask();
        jsonDataTask.execute(AppUtility.HOST+AppUtility.BOSSMSG);

        mSwipeLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeLayout.setRefreshing(false);
                        JsonBossMsgTask jsonDataTask = new JsonBossMsgTask();
                        jsonDataTask.execute(AppUtility.HOST + AppUtility.BOSSMSG);
                    }
                }, 3000);
            }
        });

        return  view;
    }

    // F_BossMsg 和 lis_main Adapter，即為listview鋪上元件
    public class BossMsgAdater extends BaseAdapter{

        private LayoutInflater inflater= LayoutInflater.from(getActivity());
        //顯示數量，測試實自己打數字
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        //找listview裡面的元件
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            lisBossTag bossTag=null;
            if(convertView==null){
                convertView=inflater.inflate(R.layout.list_main,null);
                bossTag=new lisBossTag();
                bossTag.txtBossID=(TextView)convertView.findViewById(R.id.NtxtEeventContent);
                bossTag.txtBossMsg=(TextView)convertView.findViewById(R.id.txtBossMsg);
                convertView.setTag(bossTag);
            }else{
                bossTag=(lisBossTag)convertView.getTag();
            }
            //抓取JsonBossMsgTask裡面的onPostExecute中的data
            bossTag.txtBossID.setText(data.get(position).getUserName());
            bossTag.txtBossMsg.setText(data.get(position).getMemoContent());
            return convertView;
        }
        public class lisBossTag{
            public TextView txtBossID;
            public TextView txtBossMsg;
        }
    }

    //JS3.建立巢狀類別
    public class JsonBossMsgTask extends AsyncTask<String,Void,String>{

        //JS4.讀取json編碼為UTF-8
        @Override
        protected String doInBackground(String... params) {
            //JS5.稽核網路url是否有通
            Log.e("url稽核",params[0]);
            InputStream is = AppUtility.openConnection(params[0],"GET");
            String content=AppUtility.streamToString(is,"UTF-8");
            return content;
        }
        @Override
        protected void onPostExecute(String s) {
            data=new ArrayList<>();
            Log.i("data", s);
            try {
                JSONArray jsonArray=new JSONArray(s);
                for(int p=0;p<jsonArray.length();p++){
                    JSONObject jsonObject =jsonArray.getJSONObject(p);
                    BossMsg jsonbossmsg =new BossMsg();
                    //跟json的名稱對應，後面跟"這裡"資料庫對應
                    jsonbossmsg.setUserName(jsonObject.getString("userName"));
                    jsonbossmsg.setMemoContent(jsonObject.getString("memoContent"));
                    data.add(jsonbossmsg);

                }

                BossMsgAdater adater=new BossMsgAdater();
                listBossMsg.setAdapter(adater);

            } catch (JSONException e) {
                Log.i("jsonArray格式錯誤", e.getMessage());
            }
        }
    }
}
