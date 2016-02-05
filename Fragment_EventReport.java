package com.khh.gjun.security;


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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.khh.gjun.security.apputility.AppAdapter;
import com.khh.gjun.security.apputility.AppUtility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_EventReport#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_EventReport extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //自訂UI物件
    private Button btnEventEnter;
    private Button btnBack_E;
    private Spinner spinRFID;
    private EditText editEventName;
    private EditText editEventLocation;
    private Spinner spinEventLevel;
    private Spinner spinIsNot;
    private EditText editEventContent;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_EventReport.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_EventReport newInstance(String param1, String param2) {
        Fragment_EventReport fragment = new Fragment_EventReport();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_EventReport() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getActivity().setTitle("事件回報");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eventreport, container, false);

        spinRFID = (Spinner)view.findViewById(R.id.spinRFID);
        spinEventLevel = (Spinner)view.findViewById(R.id.spinEventLevel);
        spinIsNot = (Spinner)view.findViewById(R.id.spinIsNot);
        editEventName = (EditText)view.findViewById(R.id.editEventName);
        editEventLocation = (EditText)view.findViewById(R.id.editEventLocation);
        editEventContent = (EditText)view.findViewById(R.id.editEventContent);
        btnEventEnter = (Button)view.findViewById(R.id.btnEventEnter);
        btnBack_E = (Button)view.findViewById(R.id.btnBack_E);


        spinEventLevel.setAdapter(AppAdapter.spinEventLevel(getActivity()));
        spinIsNot.setAdapter(AppAdapter.spinIsNot(getActivity()));

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.RFIDarray,android.R.layout.simple_dropdown_item_1line);
        spinRFID.setAdapter(arrayAdapter);

        /*
        Button btnSpin = (Button)view.findViewById(R.id.btnSpin);
        btnSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseAdapter adapter = AppAdapter.spinEventLevel(getActivity());
                String msg = (String)adapter.getItem(spinEventLevel.getSelectedItemPosition());
                Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();

                //Log.i("spinner選單", spinEventLevel.getContentDescription().toString());
            }
        });
        */
        /*
        Button btnSpin = (Button)view.findViewById(R.id.btnSpin);
        btnSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),editEventName.getText().toString(),Toast.LENGTH_LONG).show();
                Log.i("測試",editEventName.getText().toString());
                if(editEventName.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"NULL",Toast.LENGTH_LONG).show();
                }

            }
        });
        */

        //按鈕:送出Event報表,並切回EventList畫面
        //送出報表的部分要1.非同步傳送 2.再切畫面
        btnEventEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.非同步傳送
                if (editEventName.getText().toString().isEmpty() ||
                        editEventLocation.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "欄位不能為空", Toast.LENGTH_SHORT).show();
                }else if(editEventContent.getText().toString().length() < 5){
                    Toast.makeText(getActivity(), "事件內容不能少於5個字", Toast.LENGTH_SHORT).show();
                }else if(editEventName.getText().toString().length() > 10){
                    Toast.makeText(getActivity(), "事件名稱不能大於10個字", Toast.LENGTH_SHORT).show();
                }else if(editEventLocation.getText().toString().length() > 20){
                    Toast.makeText(getActivity(), "事件地點不能大於20個字", Toast.LENGTH_SHORT).show();
                }else if(editEventContent.getText().toString().length() > 100){
                    Toast.makeText(getActivity(), "事件內容不能大於100個字", Toast.LENGTH_SHORT).show();
                }
                else {
                        BaseAdapter adapter = AppAdapter.spinEventLevel(getActivity());
                        String url = null;
                        try {
                            url = String.format(AppUtility.HOST + AppUtility.URL_EventReportEnter,
                                    AppUtility.PREFER_UserID,
                                    spinRFID.getSelectedItemPosition()+1,
                                    URLEncoder.encode(editEventName.getText().toString(), "UTF-8"),
                                    URLEncoder.encode(editEventLocation.getText().toString(), "UTF-8"),
                                    URLEncoder.encode((String) adapter.getItem(spinEventLevel.getSelectedItemPosition()), "UTF-8"),
                                    spinIsNot.getSelectedItemPosition(),
                                    URLEncoder.encode(editEventContent.getText().toString(), "UTF-8")
                            );
                        } catch (UnsupportedEncodingException e) {
                            Log.i("編碼錯誤", e.getMessage());
                        }

                        EventReportTask eventReportTask = new EventReportTask();
                        eventReportTask.execute(url);
                    }
                }

            }
        );

        //按鈕:返回
        btnBack_E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //切畫面
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Fragment_EventList fragment_eventList = Fragment_EventList.newInstance("","");
                fragmentTransaction.replace(R.id.contain, fragment_eventList);
                fragmentTransaction.commit();

            }
        });




        return view;
    }



    public class EventReportTask extends AsyncTask<String,Void,String>{

        private ProgressDialog dialog;
        //private String status;

        @Override
        protected String doInBackground(String... params) {
            //把EventReport寫入資料庫
            String status = AppUtility.streamToString(AppUtility.openConnection(params[0], "GET"), "UTF-8");
            Log.i("更新狀態",status);
            return status;
        }

        //事前
        @Override
        protected void onPreExecute() {
            dialog = AppUtility.createDialog(getActivity(),"上傳到資料庫...");
            dialog.show();
        }

        //事後
        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();

            if(s.equals("Success")){
                Toast.makeText(getActivity(),"新增成功",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(),"新增失敗",Toast.LENGTH_LONG).show();
            }

            //2.再切畫面
            FragmentManager fragmentManager = getActivity().getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment_EventList fragment_eventList = Fragment_EventList.newInstance("","");
            fragmentTransaction.replace(R.id.contain, fragment_eventList);
            fragmentTransaction.commit();





        }
    }

}
