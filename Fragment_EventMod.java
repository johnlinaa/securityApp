package com.khh.gjun.security;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.khh.gjun.security.apputility.AppAdapter;
import com.khh.gjun.security.apputility.AppUtility;
import com.khh.gjun.security.apputility.EventList;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_EventMod#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_EventMod extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //自訂物件UI
    private Button btnEventEnter_M;
    private Button btnBack_M;
    private Spinner spinRFID_M;
    private EditText editEventName_M;
    private EditText editEventLocation_M;
    private Spinner spinEventLevel_M;
    private Spinner spinIsNot_M;
    private EditText editEventContent_M;
    private EventList eventList;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_EventMod.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_EventMod newInstance(String param1, String param2) {
        Fragment_EventMod fragment = new Fragment_EventMod();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_EventMod() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getActivity().setTitle("事件回報修改");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eventmod, container, false);

        spinRFID_M = (Spinner)view.findViewById(R.id.spinRFID_M);
        spinEventLevel_M = (Spinner)view.findViewById(R.id.spinEventLevel_M);
        spinIsNot_M = (Spinner)view.findViewById(R.id.spinIsNot_M);
        editEventName_M = (EditText)view.findViewById(R.id.editEventName_M);
        editEventLocation_M = (EditText)view.findViewById(R.id.editEventLocation_M);
        editEventContent_M = (EditText)view.findViewById(R.id.editEventContent_M);
        btnEventEnter_M = (Button)view.findViewById(R.id.btnEventEnter_M);
        btnBack_M = (Button)view.findViewById(R.id.btnBack_M);


        spinEventLevel_M.setAdapter(AppAdapter.spinEventLevel(getActivity()));
        spinIsNot_M.setAdapter(AppAdapter.spinIsNot(getActivity()));

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.RFIDarray,android.R.layout.simple_dropdown_item_1line);
        spinRFID_M.setAdapter(arrayAdapter);

        //把上一層的資訊帶過來鋪畫面
        Intent intent = getActivity().getIntent();
        eventList = (EventList)intent.getSerializableExtra("event");

        //設定EventMod初始畫畫面
        editEventName_M.setText(eventList.getEventName());
        editEventLocation_M.setText(eventList.getEventLocation());
        editEventContent_M.setText(eventList.getEventContent());

        spinRFID_M.setSelection(eventList.getRFID_ID()-1);

        //預設spinner選項
        //事件緊急程度spinner
        if(eventList.getEventLevel().equals("輕度")) {

            spinEventLevel_M.setSelection(0);
        }else if(eventList.getEventLevel().equals("中度")){

            spinEventLevel_M.setSelection(1);
        }else if(eventList.getEventLevel().equals("重度")){

            spinEventLevel_M.setSelection(2);
        }
        //事件處理狀況spinner
        if(eventList.getIsNot().equals("未處理")) {
            spinIsNot_M.setSelection(0);
        }else if(eventList.getIsNot().equals("已處理")){
            spinIsNot_M.setSelection(1);
        }
        //RFID地點spinner
        //還沒寫...................................







        //TODO 按鈕:確認修改
        //1.非同步送資料到資料庫
        //2.切換畫面
        btnEventEnter_M.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.非同步送資料到資料庫
                if (editEventName_M.getText().toString().isEmpty() ||
                        editEventLocation_M.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "欄位不能為空", Toast.LENGTH_SHORT).show();
                }else if(editEventContent_M.getText().toString().length() < 5) {
                        Toast.makeText(getActivity(), "事件內容不能少於5個字", Toast.LENGTH_SHORT).show();
                }else if(editEventName_M.getText().toString().length() > 10){
                    Toast.makeText(getActivity(), "事件名稱不能大於10個字", Toast.LENGTH_SHORT).show();
                }else if(editEventLocation_M.getText().toString().length() > 20){
                    Toast.makeText(getActivity(), "事件地點不能大於20個字", Toast.LENGTH_SHORT).show();
                }else if(editEventContent_M.getText().toString().length() > 100){
                    Toast.makeText(getActivity(), "事件內容不能大於100個字", Toast.LENGTH_SHORT).show();
                }
                else {
                        BaseAdapter RFIDAdapter = AppAdapter.spinRFIDAdapter(getActivity());
                        BaseAdapter eventLevelAdapter = AppAdapter.spinEventLevel(getActivity());
                        BaseAdapter isNotAdapter = AppAdapter.spinIsNot(getActivity());


                        String url = null;
                        try {
                            url = String.format(AppUtility.HOST + AppUtility.URL_EventMod,
                                    eventList.getEventID(),
                                    spinRFID_M.getSelectedItemPosition()+1,
                                    URLEncoder.encode(editEventName_M.getText().toString(), "UTF-8"),
                                    URLEncoder.encode(editEventLocation_M.getText().toString(), "UTF-8"),
                                    URLEncoder.encode((String) eventLevelAdapter.getItem(spinEventLevel_M.getSelectedItemPosition()), "UTF-8"),
                                    spinIsNot_M.getSelectedItemPosition(),
                                    URLEncoder.encode(editEventContent_M.getText().toString(), "UTF-8")
                            );
                        } catch (UnsupportedEncodingException e) {
                            Log.i("編碼錯誤", e.getMessage());
                        }

                        EventModTask eventModTask = new EventModTask();
                        eventModTask.execute(url);

                    }

                }
            }
        );

        //按鈕:返回
        btnBack_M.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment_EventList fragment_eventList = Fragment_EventList.newInstance("", "");
                fragmentTransaction.replace(R.id.contain, fragment_eventList);
                fragmentTransaction.commit();


                //廢棄寫法,疊頁面會出問題
                /*
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment_EventList fragment_eventList = Fragment_EventList.newInstance("", "");
                fragmentTransaction.remove(Fragment_EventMod.this);
                //讓原本消失的EventList頁面顯示
                RelativeLayout layout_EventList = (RelativeLayout) getActivity().findViewById(R.id.layout_EventList);
                layout_EventList.setVisibility(RelativeLayout.VISIBLE);
                fragmentTransaction.commit();
                */


            }
        });

        return view;

        }

    //非同步:上傳EventMod到資料庫
    public class EventModTask extends AsyncTask<String,Void,String>{
        private ProgressDialog dialog;
          @Override
          protected String doInBackground(String... params) {
              String status = AppUtility.streamToString(AppUtility.openConnection(params[0], "GET"),"UTF-8");



              return status;
          }

          //事前
          @Override
          protected void onPreExecute() {
              dialog = AppUtility.createDialog(getActivity(),"更新中...");
              dialog.show();
          }

          //事後
          @Override
          protected void onPostExecute(String s) {
              dialog.dismiss();

              if(s.equals("Success")){
                  Toast.makeText(getActivity(),"更新成功",Toast.LENGTH_LONG).show();
              }else{
                  Toast.makeText(getActivity(),"更新失敗",Toast.LENGTH_LONG).show();
              }

              //切換畫面
              FragmentManager fragmentManager = getFragmentManager();
              FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
              Fragment_EventList fragment_eventList = Fragment_EventList.newInstance("", "");
              fragmentTransaction.replace(R.id.contain,fragment_eventList);
              fragmentTransaction.commit();

              /*廢棄寫法
              //2.切換畫面
              FragmentManager fragmentManager = getFragmentManager();
              FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
              Fragment_EventList fragment_eventList = Fragment_EventList.newInstance("", "");
              fragmentTransaction.remove(Fragment_EventMod.this);
              //讓原本消失的EventList頁面顯示
              RelativeLayout layout_EventList = (RelativeLayout) getActivity().findViewById(R.id.layout_EventList);
              layout_EventList.setVisibility(RelativeLayout.VISIBLE);
              fragmentTransaction.commit();
              */
          }
      }
    }





