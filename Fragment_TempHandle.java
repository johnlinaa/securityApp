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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.khh.gjun.security.apputility.AppAdapter;
import com.khh.gjun.security.apputility.AppUtility;
import com.khh.gjun.security.apputility.TempProblemList;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;



public class Fragment_TempHandle extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //自訂物件UI
    private Spinner spinHandleState;
    private EditText editHandleContent;
    private Button btnTempEnter;
    private Button btnBack_T;


    // TODO: Rename and change types and number of parameters
    public static Fragment_TempHandle newInstance(String param1, String param2) {
        Fragment_TempHandle fragment = new Fragment_TempHandle();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_TempHandle() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getActivity().setTitle("溫度回報");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_temphandle, container, false);
        spinHandleState = (Spinner)view.findViewById(R.id.spinHandleState);
        editHandleContent = (EditText)view.findViewById(R.id.editHandleContent);
        btnTempEnter = (Button)view.findViewById(R.id.btnTempEnter);
        btnBack_T = (Button)view.findViewById(R.id.btnBack_T);

        spinHandleState.setAdapter(AppAdapter.spinIsNot(getActivity()));

        //TODO 按鈕:送出溫度處理表
        btnTempEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editHandleContent.getText().toString().length() < 5){

                    Toast.makeText(getActivity(),"輸入內容不能少於5個字",Toast.LENGTH_SHORT).show();
                }else if(editHandleContent.getText().toString().length() > 100){
                    Toast.makeText(getActivity(),"輸入內容不能大於100個字",Toast.LENGTH_SHORT).show();
                }
                else {
                    //非同步傳送溫度處理表
                    TempHandleTask tempHandleTask = new TempHandleTask();

                    TempProblemList tempProblemList = (TempProblemList) getActivity().getIntent().getSerializableExtra("temphandle");
                    String tempRecordID = tempProblemList.getTempRecordID();

                    //這也是一種抓spinner的選項文字的方法
                    //TextView StxtIsNot = (TextView)spinHandleState.getSelectedView().findViewById(R.id.StxtIsNot);
                    //String handleState = StxtIsNot.getText().toString();

                    int handleState = spinHandleState.getSelectedItemPosition();

                    String url = null;
                    try {
                        url = String.format(AppUtility.HOST + AppUtility.TempHandle
                                , tempRecordID
                                , AppUtility.PREFER_UserID
                                , handleState
                                , URLEncoder.encode(editHandleContent.getText().toString(), "UTF-8")
                        );
                    } catch (UnsupportedEncodingException e) {
                        Log.i("格式錯誤", e.getMessage());
                    }
                    tempHandleTask.execute(url);
                }
            }
        });

        //TODO 按鈕:返回
        btnBack_T.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切換畫面
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment_TempProblemList fragment_tempProblemList = Fragment_TempProblemList.newInstance("","");
                fragmentTransaction.replace(R.id.contain, fragment_tempProblemList);
                fragmentTransaction.commit();
            }
        });


        return view;
    }

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
                Toast.makeText(getActivity(),"更新成功",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(),"更新失敗",Toast.LENGTH_SHORT).show();
            }

            //切換畫面
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment_TempProblemList fragment_tempProblemList = Fragment_TempProblemList.newInstance("","");
            fragmentTransaction.replace(R.id.contain,fragment_tempProblemList);
            fragmentTransaction.commit();
        }
    }


}
