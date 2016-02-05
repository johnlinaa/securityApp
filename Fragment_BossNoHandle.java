package com.khh.gjun.security.boss;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.khh.gjun.security.R;
import com.khh.gjun.security.apputility.AppUtility;
import com.khh.gjun.security.apputility.BossNoHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Fragment_BossNoHandle extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView listBossNoHandle;
    private List<BossNoHandle> data;
    private Button btnIsNot;
    private SwipeRefreshLayout mSwipeLayout;


    // TODO: Rename and change types and number of parameters
    public static Fragment_BossNoHandle newInstance(String param1, String param2) {
        Fragment_BossNoHandle fragment = new Fragment_BossNoHandle();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_BossNoHandle() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            this.getActivity().setTitle("未處理事件");
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_bossnohandle, container, false);

        listBossNoHandle=(ListView)view.findViewById(R.id.NBtxtEeventContent);
        
        JsonNoHandleTask jsonNoHandleTask = new JsonNoHandleTask();
        jsonNoHandleTask.execute(AppUtility.HOST+AppUtility.NOHANDLE);

        mSwipeLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeLayout.setRefreshing(false);
                        JsonNoHandleTask jsonNoHandleTask = new JsonNoHandleTask();
                        jsonNoHandleTask.execute(AppUtility.HOST + AppUtility.NOHANDLE);
                    }
                }, 3000);
            }
        });

        return view;
    }


    public class BossNoHandleAdater extends BaseAdapter {

        private LayoutInflater inflater=LayoutInflater.from(getActivity());
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

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            lisNoHandleTag noHandleTag=null;

            if(convertView==null){
                convertView=inflater.inflate(R.layout.list_bossnohandle,null);
                noHandleTag=new lisNoHandleTag();
                noHandleTag.NBtxtEeventName=(TextView)convertView.findViewById(R.id.NBtxtEeventName);
                noHandleTag.NBtxtEeventContent=(TextView)convertView.findViewById(R.id.NBtxtEeventContent);
                noHandleTag.NBtxtEventLevel=(TextView)convertView.findViewById(R.id.NBtxtEventLevel);
                noHandleTag.BEventIsNot=(TextView)convertView.findViewById(R.id.BspinIsNot);
                noHandleTag.NBbtnIsNot=(Button)convertView.findViewById(R.id.NBbtnIsNot);
                noHandleTag.NBtxtEventLocation = (TextView)convertView.findViewById(R.id.NBtxtEeventLocation);
                noHandleTag.NBtxtEventTime = (TextView)convertView.findViewById(R.id.NBtxtEeventTime);
                convertView.setTag(noHandleTag);
            }else{
                noHandleTag=(lisNoHandleTag)convertView.getTag();
            }
            //抓取JsonBossMsgTask裡面的onPostExecute中的data
            noHandleTag.NBtxtEeventName.setText(data.get(position).getEeventName());
            noHandleTag.NBtxtEeventContent.setText(data.get(position).getEeventContent());
            noHandleTag.NBtxtEventLevel.setText(data.get(position).getEventLevel());
            noHandleTag.NBtxtEventLocation.setText(data.get(position).getEventLocation());
            noHandleTag.NBtxtEventTime.setText(data.get(position).getEventTime());

            String isNot = null;
            if(data.get(position).getEventIsNot().equals("false")){
                isNot = "未處理";
                noHandleTag.BEventIsNot.setText(isNot);
                noHandleTag.BEventIsNot.setTextColor(Color.RED);
            }else{
                isNot = "已處理";
                noHandleTag.BEventIsNot.setText(isNot);
                noHandleTag.BEventIsNot.setTextColor(Color.GREEN);
            }

            if(data.get(position).getEventLevel().equals("輕度")){
                noHandleTag.NBtxtEventLevel.setTextColor(Color.GREEN);
            }else if(data.get(position).getEventLevel().equals("中度")){
                noHandleTag.NBtxtEventLevel.setTextColor(Color.YELLOW);
            }else if(data.get(position).getEventLevel().equals("重度")){
                noHandleTag.NBtxtEventLevel.setTextColor(Color.RED);
            }

            //int posit = position;

            noHandleTag.NBbtnIsNot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog dialog = new AlertDialog.Builder(Fragment_BossNoHandle.this.getActivity())
                            .setTitle("確認")
                            .setMessage("請再次確認是否已經處理了")
                            .setCancelable(false)
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String url = String.format(AppUtility.HOST+AppUtility.NOHANDLE_MOD
                                            , data.get(position).getEventID());
                                    NoHandleModTask noHandleModTask = new NoHandleModTask();
                                    noHandleModTask.execute(url);
                                }
                            })
                            .setNegativeButton("結束", null)
                            .create();
                    dialog.show();
                }
            });
            return convertView;
        }

        public class lisNoHandleTag{
            private TextView NBtxtEeventName;
            private TextView NBtxtEeventContent;
            private TextView NBtxtEventLevel;
            private TextView BEventIsNot;
            private Button NBbtnIsNot;
            private TextView NBtxtEventLocation;
            private TextView NBtxtEventTime;
        }
    }

    public class JsonNoHandleTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            Log.i("url", params[0]);
            InputStream is = AppUtility.openConnection(params[0], "GET");
            String content =AppUtility.streamToString(is,"UTF-8");
            return content;
        }
        @Override
        protected void onPostExecute(String s) {
            data=new ArrayList<>();
            //JS11
            Log.i("data", s);
            try {
                //JS12
                JSONArray jsonArray=new JSONArray(s);
                //JS13
                for(int p=0;p<jsonArray.length();p++){
                    JSONObject jsonObject =jsonArray.getJSONObject(p);
                    BossNoHandle jsonnohandle =new BossNoHandle();
                    //跟json的名稱對應
                    jsonnohandle.setEeventName(jsonObject.getString("eventName"));
                    jsonnohandle.setEeventContent(jsonObject.getString("eventContent"));
                    jsonnohandle.setEventLevel(jsonObject.getString("eventLevel"));
                    jsonnohandle.setEventIsNot(jsonObject.getString("isNot"));
                    jsonnohandle.setEventID(jsonObject.getString("eventID"));
                    jsonnohandle.setEventLocation(jsonObject.getString("eventLocation"));
                    DateFormat dateF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date date = dateF.parse(jsonObject.getString("eventTime"));
                        jsonnohandle.setEventTime(dateF.format(date));


                    } catch (ParseException e) {
                        Log.i("日期錯誤",e.getMessage());
                    }


                    data.add(jsonnohandle);
                }

                BossNoHandleAdater adater=new BossNoHandleAdater();
                listBossNoHandle.setAdapter(adater);
            } catch (JSONException e) {
                Log.i("jsonArray格式錯誤", e.getMessage());
            }
        }
    }

    public class NoHandleModTask extends AsyncTask<String,Void,String>{

        private ProgressDialog dialog;

        @Override
        protected String doInBackground(String... params) {
            return AppUtility.streamToString(AppUtility.openConnection(params[0], "GET"), "UTF-8");
        }

        @Override
        protected void onPreExecute() {
            dialog = AppUtility.createDialog(getActivity(),"更新中...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();

            if(s.equals("Success")){
                Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();

                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment_BossNoHandle bossNoHandle = Fragment_BossNoHandle.newInstance("","");
                fragmentTransaction.replace(R.id.containBoss, bossNoHandle);
                fragmentTransaction.commit();
            }else{
                Toast.makeText(getActivity(),"更新失敗",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
