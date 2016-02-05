package com.khh.gjun.security.boss;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.khh.gjun.security.R;
import com.khh.gjun.security.apputility.AppUtility;
import com.khh.gjun.security.apputility.JsonAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Fragment_BossMap extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView BimgMap;
    private ImageView BimgA;
    private ImageView BimgB;
    private ImageView BimgC;
    private ImageView BimgD;
    private List<JsonAll> data;

    // TODO: Rename and change types and number of parameters
    public static Fragment_BossMap newInstance(String param1, String param2) {
        Fragment_BossMap fragment = new Fragment_BossMap();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_BossMap() {
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
        View view= inflater.inflate(R.layout.fragment_bossmap, container, false);

        BimgMap = (ImageView)view.findViewById(R.id.BimgMap);
        BimgA = (ImageView)view.findViewById(R.id.BimgA);
        BimgB = (ImageView)view.findViewById(R.id.imgB);
        BimgC = (ImageView)view.findViewById(R.id.BimgC);
        BimgD = (ImageView)view.findViewById(R.id.imgD);

        data = new ArrayList<>();
        //LOOP:
        //1.抓Record資料
        //2.鋪畫面
        MapTask mapTask = new MapTask();
        mapTask.execute(AppUtility.HOST+AppUtility.MAP);


        //TODO 按鈕:刷新地圖
        BimgMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction FragmentTransaction = fragmentManager.beginTransaction();
                Fragment_BossMap bossMap = Fragment_BossMap.newInstance("地圖", "");
                FragmentTransaction.replace(R.id.containBoss, bossMap);
                FragmentTransaction.commit();
            }
        });
        return view;
    }


    public class MapTask extends AsyncTask<String,Void,String> {

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
                            BimgA.setBackgroundResource(R.drawable.red_point);
                            break;
                        case "2":
                            BimgB.setBackgroundResource(R.drawable.red_point);
                            break;
                        case "3":
                            BimgC.setBackgroundResource(R.drawable.red_point);
                            break;
                        case "4":
                            BimgD.setBackgroundResource(R.drawable.red_point);
                            break;
                    }
                }
            }

        }
    }

}
