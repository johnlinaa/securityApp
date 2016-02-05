package com.khh.gjun.security;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.khh.gjun.security.apputility.AppUtility;
import com.khh.gjun.security.apputility.EventList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_EventList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_EventList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //自訂UI物件
    private ListView listEvent;
    private Button btnNewEvent;
    private ArrayList<EventList> arrayList = new ArrayList<>();
    private View CurrentView;//這個fragment的view
    private SwipeRefreshLayout mSwipeLayout;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_EventList.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_EventList newInstance(String param1, String param2) {
        Fragment_EventList fragment = new Fragment_EventList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    public Fragment_EventList() {
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
        View view = inflater.inflate(R.layout.fragment_eventlist, container, false);
        listEvent = (ListView)view.findViewById(R.id.listEvent);
        btnNewEvent = (Button)view.findViewById(R.id.btnNewEvent);

        //按鈕:新增事件回報
        btnNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment_EventReport fragment_eventReport = Fragment_EventReport.newInstance("", "");
                fragmentTransaction.replace(R.id.contain, fragment_eventReport);
                fragmentTransaction.commit();

            }
        });

        //TODO 按鈕:設定ListView上每列都是Button
        //1.夾帶資訊到下個畫面
        //2.切換畫面
        listEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //1.夾帶資訊到下個畫面
                Intent intent = new Intent(getActivity(),Fragment_EventMod.class);
                ListEventAdapter adapter = new ListEventAdapter();
                EventList eventList = adapter.getItem(position);

                Log.i("事件ID1",eventList.getEventID()+"");
                intent.putExtra("event",eventList);
                getActivity().setIntent(intent);

                //廢棄寫法,疊頁面會出問題
                /*
                //2.切換畫面
                FragmentManager fragmentManager = Fragment_EventList.this.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //讓當前頁面消失
                RelativeLayout layout_EventList = (RelativeLayout)CurrentView.findViewById(R.id.layout_EventList);
                layout_EventList.setVisibility(RelativeLayout.INVISIBLE);
                Fragment_EventMod fragment_eventMod = Fragment_EventMod.newInstance("", "");

                fragmentTransaction.add(R.id.contain, fragment_eventMod);

                AppUtility.curentFragment = fragment_eventMod;

                Log.i("toString", layout_EventList.toString());
                fragmentTransaction.commit();
                */


                //2.切換畫面
                FragmentManager fragmentManager = Fragment_EventList.this.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment_EventMod fragment_eventMod = Fragment_EventMod.newInstance("","");
                fragmentTransaction.replace(R.id.contain,fragment_eventMod);
                fragmentTransaction.commit();


            }
        });

        //非同步下載EventList資料,要先下載完資料再鋪ListView
        EventListTask eventListTask = new EventListTask();
        eventListTask.execute(AppUtility.HOST+AppUtility.URL_EventList);

        CurrentView = view;

        mSwipeLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeLayout.setRefreshing(false);
                        EventListTask eventListTask = new EventListTask();
                        eventListTask.execute(AppUtility.HOST + AppUtility.URL_EventList);
                    }
                },3000);
            }
        });


        return view;

    }


    private class ListEventAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public EventList getItem(int position) {
            EventList eventList = arrayList.get(position);


            return eventList;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewTag tag = new ViewTag();
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                convertView = inflater.inflate(R.layout.list_eventlist, null);
                tag.LtxtEventName = (TextView)convertView.findViewById(R.id.LtxtEventName);
                tag.LtxtEventTime = (TextView)convertView.findViewById(R.id.LtxtEventTime);
                tag.LtxtEventLevel = (TextView)convertView.findViewById(R.id.LtxtEventLevel);
                tag.LtxtIsNot = (TextView)convertView.findViewById(R.id.LtxtIsNot);
                tag.LtxtEventLocation = (TextView)convertView.findViewById(R.id.LtxtEventLocation);
                convertView.setTag(tag);


            }else{
                tag = (ViewTag)convertView.getTag();
            }

            EventList eventList = arrayList.get(position);




            tag.LtxtEventName.setText(eventList.getEventName());
            tag.LtxtEventTime.setText(eventList.getEventTime());
            tag.LtxtEventLevel.setText(eventList.getEventLevel());
            if(eventList.getEventLevel().equals("輕度")){
                tag.LtxtEventLevel.setTextColor(Color.GREEN);
            }else if(eventList.getEventLevel().equals("中度")){
                tag.LtxtEventLevel.setTextColor(Color.YELLOW);
            }else if(eventList.getEventLevel().equals("重度")){
                tag.LtxtEventLevel.setTextColor(Color.RED);
            }
            tag.LtxtIsNot.setText(eventList.getIsNot());
            if(eventList.getIsNot().equals("已處理")){
                tag.LtxtIsNot.setTextColor(Color.GREEN);
            }else if(eventList.getIsNot().equals("未處理")){
                tag.LtxtIsNot.setTextColor(Color.RED);
            }
            tag.LtxtEventLocation.setText(eventList.getEventLocation());

            //EventList的按鈕,也是可以用只是以上面的為準
            /*
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Fragment_EventMod fragment_eventMod = Fragment_EventMod.newInstance("","");
                    fragmentTransaction.replace(R.id.contain,fragment_eventMod);
                    fragmentTransaction.commit();
                }
            });
            */
            return convertView;
        }


        private class ViewTag{
            private TextView LtxtEventName;
            private TextView LtxtEventTime;
            private TextView LtxtEventLevel;
            private TextView LtxtIsNot;
            private TextView LtxtEventLocation;

        }


    }

    private class EventListTask extends AsyncTask<String,Void,String>{
        private ProgressDialog dialog;

        @Override
        protected String doInBackground(String... params) {
            InputStream is = AppUtility.openConnection(params[0], "GET");
            String jsonString = AppUtility.streamToString(is,"UTF-8");

            try {
                is.close();
            } catch (IOException e) {
                Log.i("IO錯誤", e.getMessage());
            }


            Log.i("json資訊",jsonString);
            return jsonString;

        }


        //事前處理
        @Override
        protected void onPreExecute() {
            dialog = AppUtility.createDialog(getActivity(),"下載中...");
            dialog.show();

        }

        //事後處理
        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();

            //***若少了這行會切畫面會導致ListView越來越多
            arrayList.clear();




            try {
                JSONArray jsonArray = new JSONArray(s);

                for(int i = 0; i < jsonArray.length(); i++) {
                    EventList eventList = new EventList();

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    //收集所有事件的詳細資訊,但部分顯示,詳細資訊帶到另一個fragment

                    //判斷已處理,未處理
                    if(jsonObject.getString("isNot").equals("true")){
                        eventList.setIsNot("已處理");
                    }else{
                        eventList.setIsNot("未處理");
                    }


                    eventList.setEventID(Integer.parseInt(jsonObject.getString("eventID")));
                    eventList.setRFID_ID(Integer.parseInt(jsonObject.getString("RFID_ID")));
                    eventList.setEventName(jsonObject.getString("eventName"));
                    eventList.setEventLevel(jsonObject.getString("eventLevel"));
                    //eventList.setEventTime(jsonObject.getString("eventTime"));
                    DateFormat dateF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = dateF.parse(jsonObject.getString("eventTime"));
                    eventList.setEventTime(dateF.format(date));

                    eventList.setEventLocation(jsonObject.getString("eventLocation"));
                    eventList.setEventContent(jsonObject.getString("eventContent"));

                    arrayList.add(eventList);
                }
            } catch (JSONException e) {
                Log.i("Json問題",e.getMessage());
            } catch (ParseException e) {
                Log.i("日期錯誤", e.getMessage());
            }


            //完全下載完資料再鋪listView,如果寫在Fragment層級,
            // 因為非同步會不管資料沒跑完就直接鋪畫面,導致一片空白
            ListEventAdapter adapter = new ListEventAdapter();
            listEvent.setAdapter(adapter);

        }
    }



}
