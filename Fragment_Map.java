package com.khh.gjun.security;


import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import android.text.format.DateFormat;
import android.widget.ImageView;

import com.khh.gjun.security.apputility.AppUtility;
import com.khh.gjun.security.apputility.JsonAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Map#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Map extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //自訂UI物件
    private ImageView imgMap;
    private ImageView imgA;
    private ImageView imgB;
    private ImageView imgC;
    private ImageView imgD;
    private List<JsonAll> data;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Map.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Map newInstance(String param1, String param2) {
        Fragment_Map fragment = new Fragment_Map();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_Map() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getActivity().setTitle("地圖");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        imgMap = (ImageView)view.findViewById(R.id.imgMap);
        imgA = (ImageView)view.findViewById(R.id.imgA);
        imgB = (ImageView)view.findViewById(R.id.imgB);
        imgC = (ImageView)view.findViewById(R.id.imgC);
        imgD = (ImageView)view.findViewById(R.id.imgD);

        data = new ArrayList<>();
        //LOOP:
        //1.抓Record資料
        //2.鋪畫面
        MapTask mapTask = new MapTask();
        mapTask.execute(AppUtility.HOST+AppUtility.MAP);

        //TODO 按鈕:刷新地圖
        imgMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction FragmentTransaction = fragmentManager.beginTransaction();
                Fragment_Map fragment_map = Fragment_Map.newInstance("", "");
                FragmentTransaction.replace(R.id.contain, fragment_map);
                FragmentTransaction.commit();
            }
        });


        return view;

        }

    public class MapTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {

            return AppUtility.streamToString(AppUtility.openConnection(params[0], "GET"), "UTF-8");
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject = new JSONObject();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JsonAll jsonAll = new JsonAll();
                    jsonObject = jsonArray.getJSONObject(i);

                    jsonAll.setRFID_ID(jsonObject.getString("RFID_ID"));
                    jsonAll.setNumber(jsonObject.getString("number"));

                    data.add(jsonAll);
                }


            } catch (JSONException e) {
                Log.i("JSON問題", e.getMessage());
            }


            if (data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    String RFID = data.get(i).getRFID_ID();

                    switch (RFID) {
                        case "1":
                            imgA.setBackgroundResource(R.drawable.red_point);
                            break;
                        case "2":
                            imgB.setBackgroundResource(R.drawable.red_point);
                            break;
                        case "3":
                            imgC.setBackgroundResource(R.drawable.red_point);
                            break;
                        case "4":
                            imgD.setBackgroundResource(R.drawable.red_point);
                            break;
                    }


                }
            }





        }
    }




    }




