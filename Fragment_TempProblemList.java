package com.khh.gjun.security;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Color;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.khh.gjun.security.apputility.AppAdapter;
import com.khh.gjun.security.apputility.AppUtility;
import com.khh.gjun.security.apputility.TempProblemList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class Fragment_TempProblemList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment currentFragment;
    private ListView listTempProblem;
    private List<TempProblemList> data;
    private SwipeRefreshLayout mSwipeLayout;
    private Spinner spinHandleState;
    private EditText editHandleContent;
    private Button btnTempEnter;
    private Button btnBack_T;

    private Spinner spinHandleState_M;
    private EditText editHandleContent_M;
    private Button btnTempEnter_M;
    private Button btnBack_TM;


    // TODO: Rename and change types and number of parameters
    public static Fragment_TempProblemList newInstance(String param1, String param2) {
        Fragment_TempProblemList fragment = new Fragment_TempProblemList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_TempProblemList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            this.getActivity().setTitle("溫度回報");
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tempproblemlist, container, false);

        //TODO 按鈕:溫度異常ListItem按鈕(顯示未處理的或null的)
        listTempProblem = (ListView) view.findViewById(R.id.listTempProblem);
        listTempProblem.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //假設處理狀況為空,新增一個空白DiaLog
                Log.i("稽核內容",data.get(position).getHandleState());

                if(data.get(position).getHandleState().equals("NULL")) {

                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                    View layout = layoutInflater.inflate(R.layout.list_dialog_temphandle, null);
                    final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setView(layout)
                            .setCancelable(false)
                            .create();
                    dialog.show();
                    //dialog.getWindow().setLayout(1400, 1800);

                    spinHandleState = (Spinner) layout.findViewById(R.id.spinHandleState);
                    editHandleContent = (EditText) layout.findViewById(R.id.editHandleContent);
                    btnTempEnter = (Button) layout.findViewById(R.id.btnTempEnter);
                    spinHandleState.setAdapter(AppAdapter.spinIsNot(getActivity()));

                    btnTempEnter.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(editHandleContent.getText().toString().length() < 5){
                                        Toast.makeText(getActivity(),"輸入內容不能小於5字",Toast.LENGTH_SHORT).show();
                                    }else if(editHandleContent.getText().toString().length() > 100){
                                        Toast.makeText(getActivity(),"輸入內容不能大於100字",Toast.LENGTH_SHORT).show();
                                    }else {
                                        TempHandleTask tempHandleTask = new TempHandleTask();

                                        int handleState = spinHandleState.getSelectedItemPosition();

                                        String url = null;
                                        try {
                                            url = String.format(AppUtility.HOST + AppUtility.TempHandle
                                                    , data.get(position).getTempRecordID()
                                                    , AppUtility.PREFER_UserID
                                                    , handleState
                                                    , URLEncoder.encode(editHandleContent.getText().toString(), "UTF-8")
                                            );
                                        } catch (UnsupportedEncodingException e) {
                                            Log.i("編碼錯誤",e.getMessage());
                                        }
                                        tempHandleTask.execute(url);

                                        //修改通知
                                        NotifyModTask notifyModTask = new NotifyModTask();
                                        String urlstring = String.format(AppUtility.HOST + AppUtility.URL_NotifyMod, data.get(position).getTempRecordID());
                                        notifyModTask.execute(urlstring);
                                        dialog.dismiss();
                                    }


                                }
                            }
                    );
                    btnBack_T = (Button) layout.findViewById(R.id.btnBack_T);
                    btnBack_T.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            }
                    );

                    //假設處理狀況不為空，修改其處理狀況
                }else{
                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                    View layout = layoutInflater.inflate(R.layout.list_dialog_temphandle_mod, null);
                    final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setView(layout)
                            .setCancelable(false)
                            .create();
                    dialog.show();
                    //dialog.getWindow().setLayout(1400, 1800);

                    spinHandleState_M = (Spinner) layout.findViewById(R.id.spinHandleState_M);
                    editHandleContent_M = (EditText) layout.findViewById(R.id.editHandleContent_M);
                    btnTempEnter_M = (Button) layout.findViewById(R.id.btnTempEnter_M);
                    spinHandleState_M.setAdapter(AppAdapter.spinIsNot(getActivity()));

                    //設定初始的內容
                    editHandleContent_M.setText(data.get(position).getHandleContent());

                    //設定初始Spinner
                    if(data.get(position).getHandleState().equals("false")) {
                        spinHandleState_M.setSelection(0);
                    }else if(data.get(position).getHandleState().equals("true")) {
                        spinHandleState_M.setSelection(1);
                    }
                    btnTempEnter_M.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(editHandleContent_M.getText().toString().length() < 5){
                                        Toast.makeText(getActivity(),"輸入內容不能小於5字",Toast.LENGTH_SHORT).show();
                                    }else if(editHandleContent_M.getText().toString().length() > 100){
                                        Toast.makeText(getActivity(),"輸入內容不能大於100字",Toast.LENGTH_SHORT).show();
                                    }else {

                                        TempHandleTask tempHandleTask = new TempHandleTask();

                                        int handleState = spinHandleState_M.getSelectedItemPosition();

                                        String url = null;
                                        try {
                                            url = String.format(AppUtility.HOST + AppUtility.TempHandle_Mod
                                                    , data.get(position).getTempRecordID()
                                                    , AppUtility.PREFER_UserID
                                                    , handleState
                                                    , URLEncoder.encode(editHandleContent_M.getText().toString(), "UTF-8"));
                                        } catch (UnsupportedEncodingException e) {
                                            Log.i("編碼錯誤",e.getMessage());
                                        }

                                        tempHandleTask.execute(url);

                                        //修改通知
                                        NotifyModTask notifyModTask = new NotifyModTask();
                                        String urlstring = String.format(AppUtility.HOST + AppUtility.URL_NotifyMod, data.get(position).getTempRecordID());
                                        notifyModTask.execute(urlstring);
                                        dialog.dismiss();
                                    }

                                }
                            }
                    );
                    btnBack_TM = (Button) layout.findViewById(R.id.btnBack_TM);
                    btnBack_TM.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            }
                    );
                }
               /*
               //Fragment 切換用
                //把TempProblemList class裝入intent
                Intent intent = new Intent(getActivity(),Fragment_TempHandle.class);
                TempProblemList tempProblemList = data.get(position);
                intent.putExtra("temphandle", tempProblemList);
                getActivity().setIntent(intent);

                //切換畫面
                if(data.get(position).getHandleState().equals("false")){
                    fragmentManager = Fragment_TempProblemList.this.getFragmentManager();
                    Fragment_TempHandle_Mod fragment_tempHandle_mod = Fragment_TempHandle_Mod.newInstance("","");
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.contain, fragment_tempHandle_mod);
                    currentFragment = fragment_tempHandle_mod;
                    fragmentTransaction.commit();

                }else {

                    fragmentManager = Fragment_TempProblemList.this.getFragmentManager();
                    Fragment_TempHandle tempHandle = Fragment_TempHandle.newInstance("", "");
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.contain, tempHandle);
                    currentFragment = tempHandle;
                    fragmentTransaction.commit();
                }
                */
            }
        });

        JsonTempproblemlistTask jsonDataTask = new JsonTempproblemlistTask();
        jsonDataTask.execute(AppUtility.HOST + AppUtility.Tempproblemlist);

        mSwipeLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeLayout.setRefreshing(false);
                        JsonTempproblemlistTask jsonDataTask = new JsonTempproblemlistTask();
                        jsonDataTask.execute(AppUtility.HOST + AppUtility.Tempproblemlist);
                    }
                }, 3000);
            }
        });

        return view;
    }


    //TODO:主畫面Adater - 鋪tempproblemlist的listview畫面
    public class TempproblemlistAdater extends BaseAdapter {
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
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            tempproblemTag tempproblemTag = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_temprecord, null);
                tempproblemTag = new tempproblemTag();
                tempproblemTag.TtxtTempLocation = (TextView) convertView.findViewById(R.id.TtxtTempLocation);
                tempproblemTag.TtxtTempTime = (TextView) convertView.findViewById(R.id.TtxtTempTime);
                tempproblemTag.txtTemperature = (TextView) convertView.findViewById(R.id.txtTemperature);
                tempproblemTag.txtWet = (TextView) convertView.findViewById(R.id.txtWet);
                tempproblemTag.TtxtHandleState = (TextView) convertView.findViewById(R.id.TtxtHandleState);
                convertView.setTag(tempproblemTag);
            } else {
                tempproblemTag = (tempproblemTag) convertView.getTag();
            }
            //抓取JsonTempproblemlistTask裡面的onPostExecute中的data
            tempproblemTag.TtxtTempLocation.setText(data.get(position).getTempLocation());
            tempproblemTag.TtxtTempTime.setText(data.get(position).getTempTime());
            tempproblemTag.txtTemperature.setText(data.get(position).getTemperature());
            tempproblemTag.txtWet.setText(data.get(position).getWet());
            if(data.get(position).getHandleState().equals("false")){
                tempproblemTag.TtxtHandleState.setText("未處理");
                tempproblemTag.TtxtHandleState.setTextColor(Color.RED);
            }else if(data.get(position).getHandleState().equals("true")){
                tempproblemTag.TtxtHandleState.setText("已處理");
                tempproblemTag.TtxtHandleState.setTextColor(Color.GREEN);
            }else{
                tempproblemTag.TtxtHandleState.setText("沒有回報");
                tempproblemTag.TtxtHandleState.setTextColor(Color.BLACK);
            }
            return convertView;
        }
        public class tempproblemTag {
            public TextView TtxtTempLocation;
            public TextView TtxtTempTime;
            public TextView txtTemperature;
            public TextView txtWet;
            public TextView TtxtHandleState;
        }
    }

    //TODO:主要畫面溫度的Task
    public class JsonTempproblemlistTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Log.e("url", params[0]);
            InputStream is = AppUtility.openConnection(params[0], "GET");
            String content=AppUtility.streamToString(is, "UTF-8");
            return content;
        }

        @Override
        protected void onPostExecute(String s) {
            data=new ArrayList<>();
            Log.i("data", s);
            try {
                //JS12
                JSONArray jsonArray=new JSONArray(s);
                //JS13
                for(int p=0;p<jsonArray.length();p++){
                    JSONObject jsonObject =jsonArray.getJSONObject(p);
                    TempProblemList jsonTempproblemlist =new TempProblemList();
                    //跟json的名稱對應
                    jsonTempproblemlist.setTempLocation(jsonObject.getString("tempLocation"));
                    //jsonTempproblemlist.setTempTime(jsonObject.getString("tempTime"));
                    DateFormat dateF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = dateF.parse(jsonObject.getString("tempTime"));
                    jsonTempproblemlist.setTempTime(dateF.format(date));

                    jsonTempproblemlist.setTemperature(jsonObject.getString("temperature"));
                    jsonTempproblemlist.setWet(jsonObject.getString("wet"));
                    jsonTempproblemlist.setTempRecordID(jsonObject.getString("tempRecordID"));
                    jsonTempproblemlist.setHandleState(jsonObject.getString("handleState"));
                    jsonTempproblemlist.setHandleContent(jsonObject.getString("handleContent"));
                    data.add(jsonTempproblemlist);
                }
                TempproblemlistAdater adater = new TempproblemlistAdater();
                listTempProblem.setAdapter(adater);
            } catch (JSONException e) {
                Log.i("jsonArray格式錯誤", e.getMessage());
            } catch (ParseException e) {
                Log.i("日期錯誤", e.getMessage());
            }
        }
    }

    //TODO:一下按鈕已處理Task
    public class TempHandleTask extends AsyncTask<String,Void,String>{
        private ProgressDialog dialog;

        @Override
        protected String doInBackground(String... params) {
            Log.i("URL",params[0]);
            return AppUtility.streamToString(AppUtility.openConnection(params[0], "GET"),"UTF-8");

        }
        //事前
        @Override
        protected void onPreExecute() {
            dialog = AppUtility.createDialog(getActivity(),"上傳中...");
            dialog.show();
        }
        //事後
        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();

            if(s.equals("Success")){
                Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();

                fragmentManager = Fragment_TempProblemList.this.getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                Fragment_TempProblemList fragment_tempProblemList = Fragment_TempProblemList.newInstance("", "");
                fragmentTransaction.replace(R.id.contain, fragment_tempProblemList);
                fragmentTransaction.commit();
            }else{
                Toast.makeText(getActivity(),"更新失敗",Toast.LENGTH_SHORT).show();

                fragmentManager = Fragment_TempProblemList.this.getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                Fragment_TempProblemList fragment_tempProblemList = Fragment_TempProblemList.newInstance("", "");
                fragmentTransaction.replace(R.id.contain, fragment_tempProblemList);
                fragmentTransaction.commit();
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
            Log.i("稽核服務", s);
        }
    }

}