package com.khh.gjun.security.boss;


import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.khh.gjun.security.Fragment_EventList;
import com.khh.gjun.security.Fragment_Map;
import com.khh.gjun.security.R;
import com.khh.gjun.security.apputility.AppUtility;
import com.khh.gjun.security.apputility.EventList;
import com.khh.gjun.security.apputility.JsonAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_EventRecord#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_EventRecord extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //自訂UI物件
    private EditText editEventStartDate;
    private EditText editEventEndDate;
    private Spinner spinIsNot;
    private Button btnEventSearch;
    private ListView listEventRecord;
    private List<EventList> data;
    private LinearLayout layout_eventrecord;

    private String endDate;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_EventRecord.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_EventRecord newInstance(String param1, String param2) {
        Fragment_EventRecord fragment = new Fragment_EventRecord();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_EventRecord() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getActivity().setTitle("事件報表");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eventrecord, container, false);

        editEventStartDate = (EditText)view.findViewById(R.id.editEventStartDate);
        editEventEndDate = (EditText)view.findViewById(R.id.editEventEndDate);
        spinIsNot = (Spinner)view.findViewById(R.id.spinIsNot);
        btnEventSearch = (Button)view.findViewById(R.id.btnEventSearch);
        listEventRecord = (ListView)view.findViewById(R.id.listEventRecord);
        layout_eventrecord = (LinearLayout)view.findViewById(R.id.layout_eventrecord);

        //TODO 按鈕:listView的item按鈕
        listEventRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //1.把詳細資訊放入Intent
                Intent intent = new Intent(getActivity(),Fragment_EventRecord_Detail.class);
                EventList eventList = data.get(position);
                intent.putExtra("eventrecord",eventList);
                getActivity().setIntent(intent);

                //2.再切畫面
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment_EventRecord_Detail fragment_eventRecord_detail = Fragment_EventRecord_Detail.newInstance("","");
                layout_eventrecord.setVisibility(View.INVISIBLE);
                fragmentTransaction.add(R.id.containBoss, fragment_eventRecord_detail);
                fragmentTransaction.commit();

                AppUtility.curentFragment = fragment_eventRecord_detail;
            }
        });

        //TODO 按鈕:開始時間 - 出現日曆選擇日期
        editEventStartDate.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                DatePickerDialog dateDialog = new DatePickerDialog(getActivity(), DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                editEventStartDate.setText(DateFormat.format("yyyy/MM/dd", calendar));




                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));

                dateDialog.show();
            }
        });

        //TODO 按鈕:結束時間 - 出現日曆選擇日期
        editEventEndDate.setOnClickListener(new View.OnClickListener(){
            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                DatePickerDialog dateDialog = new DatePickerDialog(getActivity(), DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                editEventEndDate.setText(DateFormat.format("yyyy/MM/dd", calendar));

                                //因為SQL的結束時間只能找前一天(12/24~12/25)==(12/24),所以才要再加一天
                                calendar.set(year, monthOfYear, dayOfMonth + 1);
                                endDate = (String)DateFormat.format("yyyy/MM/dd", calendar);

                            }
                        },c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));

                dateDialog.show();
            }
        });

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.spinnerHandleState, R.layout.list_layout_spinner);

        spinIsNot.setAdapter(arrayAdapter);

        //TODO 按鈕:事件報表搜尋
        btnEventSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editEventEndDate.getText().toString().isEmpty() || editEventStartDate.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"請輸入開始時間和結束時間",Toast.LENGTH_LONG).show();
                }else {
                    //非同步搜尋資料,並舖到ListView
                    EventListTask eventListTask = new EventListTask();

                    //URL1:搜尋所有事件
                    String url1 = String.format(AppUtility.HOST + AppUtility.EventListAll
                            , editEventStartDate.getText().toString()
                            , endDate);

                    //URL2:搜尋以處理或未處理的事件
                    String url2 = String.format(AppUtility.HOST + AppUtility.EventListS
                            , editEventStartDate.getText().toString()
                            , endDate
                            ,spinIsNot.getSelectedItemPosition());

                    if(spinIsNot.getSelectedItemPosition() == 2) {
                        eventListTask.execute(url1);
                    }else{
                        eventListTask.execute(url2);
                    }
                }
            }
        });

        return view;

    }

    private class EventListTask extends AsyncTask<String,Void,String>{
        private ProgressDialog dialog;

        @Override
        protected String doInBackground(String... params) {

            Log.i("事件報表URL",params[0]);
            return AppUtility.streamToString(AppUtility.openConnection(params[0], "GET"), "UTF-8");
        }

        //事前
        @Override
        protected void onPreExecute() {
            dialog = AppUtility.createDialog(getActivity(),"資料下載中...");
            dialog.show();
        }

        //事後
        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();

            Log.i("事件報表詳細資料", s);

            data = new ArrayList<>();
            JSONObject jsonObject = null;
            try {
                JSONArray jsonArray = new JSONArray(s);
                for(int i = 0; i < jsonArray.length(); i++){
                    EventList eventList = new EventList();
                    jsonObject = jsonArray.getJSONObject(i);
                    eventList.setEventID(Integer.parseInt(jsonObject.getString("eventID")));
                    eventList.setEventName(jsonObject.getString("eventName"));
                    eventList.setUserName(jsonObject.getString("UserName"));
                    eventList.setLocation(jsonObject.getString("location"));
                    eventList.setEventLocation(jsonObject.getString("eventLocation"));
                    //eventList.setEventTime(jsonObject.getString("eventTime"));
                    eventList.setEventLevel(jsonObject.getString("eventLevel"));
                    eventList.setRFID_ID(jsonObject.getInt("RFID_ID"));



                    java.text.DateFormat dateF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = dateF.parse(jsonObject.getString("eventTime"));
                    eventList.setEventTime(dateF.format(date));


                    if(jsonObject.getString("isNot").equals("true")) {
                        eventList.setIsNot("已處理");
                    }else{
                        eventList.setIsNot("未處理");
                    }
                    eventList.setEventContent(jsonObject.getString("eventContent"));

                    data.add(eventList);
                }


            } catch (JSONException e) {
                Log.i("JSON錯誤",e.getMessage());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            EventListAdapter adapter = new EventListAdapter();
            listEventRecord.setAdapter(adapter);

        }
    }

    public class EventListAdapter extends BaseAdapter{
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
                //生產初始的view
                convertView = inflater.inflate(R.layout.list_eventrecord,null);

                tag.EtxtEventName = (TextView)convertView.findViewById(R.id.EtxtEventName);
                tag.EtxtEventTime = (TextView)convertView.findViewById(R.id.EtxtEventTime);
                tag.EtxtEventLocation = (TextView)convertView.findViewById(R.id.EtxtEventLocation);
                tag.EtxtUserName = (TextView)convertView.findViewById(R.id.EtxtUserName);
                tag.EtxtEventLevel = (TextView)convertView.findViewById(R.id.EtxtEventLevel);
                tag.EtxtIsNot = (TextView)convertView.findViewById(R.id.EtxtIsNot);

                convertView.setTag(tag);

            }else{
                tag = (ViewTag)convertView.getTag();

            }

            tag.EtxtEventName.setText(data.get(position).getEventName());
            tag.EtxtEventTime.setText(data.get(position).getEventTime());
            tag.EtxtEventLocation.setText(data.get(position).getEventLocation());
            tag.EtxtUserName.setText(data.get(position).getUserName());
            tag.EtxtEventLevel.setText(data.get(position).getEventLevel());
            if(data.get(position).getIsNot().equals("已處理")) {
                tag.EtxtIsNot.setText(data.get(position).getIsNot());
                tag.EtxtIsNot.setTextColor(Color.GREEN);
            }else{
                tag.EtxtIsNot.setText(data.get(position).getIsNot());
                tag.EtxtIsNot.setTextColor(Color.RED);
            }




            return convertView;
        }

        public class ViewTag{
            private TextView EtxtEventName;
            private TextView EtxtEventTime;
            private TextView EtxtEventLocation;
            private TextView EtxtUserName;
            private TextView EtxtEventLevel;
            private TextView EtxtIsNot;
        }

    }

}
