package com.khh.gjun.security.boss;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.khh.gjun.security.R;
import com.khh.gjun.security.apputility.AppUtility;
import com.khh.gjun.security.apputility.JsonAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class Fragment_TempRecord extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private TextView B_txtHandle;
    private Spinner B_spinHandleState;
    private Spinner spinTempState;
    private EditText editTempStartDate;
    private EditText editTempEndDate;
    private Button btnTempSearch;
    private TextView txt1;
    private TextView txt2;
    private TextView txt3;
    private TextView txt4;
    private Button btnre;
    private ListView listTempRecord;
    private List<JsonAll> data;

    // TODO: Rename and change types and number of parameters
    public static Fragment_TempRecord newInstance(String param1, String param2) {
        Fragment_TempRecord fragment = new Fragment_TempRecord();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_TempRecord() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            //設定畫面抬頭
            this.getActivity().setTitle("溫濕度報表");
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_temprecord, container, false);


        //spinner 溫度處理狀態 文字內容在資源檔->spinner.xml
        B_spinHandleState=(Spinner)view.findViewById(R.id.B_spinHandleState);
        spinTempState=(Spinner)view.findViewById(R.id.spinTempState);

        ArrayAdapter<CharSequence> adapter1 =  ArrayAdapter.createFromResource(getActivity(), R.array.spinnerHandleState,
                R.layout.list_layout_spinner);
        adapter1.setDropDownViewResource( R.layout.list_layout_spinner);
        B_spinHandleState.setAdapter(adapter1);
        ArrayAdapter<CharSequence> adapter2 =  ArrayAdapter.createFromResource(getActivity(), R.array.TempStateItem,
                R.layout.list_layout_spinner);
        adapter2.setDropDownViewResource( R.layout.list_layout_spinner);
        spinTempState.setAdapter(adapter2);
        //Ap2
        listTempRecord=(ListView)view.findViewById(R.id.listTempRecord);

        editTempStartDate = (EditText)view.findViewById(R.id.editTempStartDate);
        editTempEndDate = (EditText)view.findViewById(R.id.editTempEndDate);

        B_txtHandle = (TextView)view.findViewById(R.id.B_txtHandle);

        //spinner不能寫OnItemClick
     /*   spinTempState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    B_spinHandleState.setVisibility(View.INVISIBLE);
                    B_txtHandle.setVisibility(View.INVISIBLE);
                }else{
                    B_spinHandleState.setVisibility(View.VISIBLE);
                    B_txtHandle.setVisibility(View.VISIBLE);
                }
            }
        });
*/
        spinTempState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    B_spinHandleState.setVisibility(View.INVISIBLE);
                    B_txtHandle.setVisibility(View.INVISIBLE);
                }else{
                    B_spinHandleState.setVisibility(View.VISIBLE);
                    B_txtHandle.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //TODO Item按鈕:溫度報表詳細
        listTempRecord.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (!data.get(position).getHandleState().equals("沒有回報")) {
                            JsonAll temprecord = (JsonAll) parent.getItemAtPosition(position);
                            if (!(temprecord.getHandleState() == null)) {

                                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                                View layout = layoutInflater.inflate(R.layout.list_dialog_temprecord, null);
                                final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                        .setView(layout)
                                        .setCancelable(false)
                                        .create();
                                dialog.show();
                                //dialog.getWindow().setLayout(1300, 1800);
                                txt1 = (TextView) layout.findViewById(R.id.txt1);
                                txt2 = (TextView) layout.findViewById(R.id.txt2);
                                txt3 = (TextView) layout.findViewById(R.id.txt3);
                                txt4 = (TextView) layout.findViewById(R.id.txt4);
                                btnre = (Button) layout.findViewById(R.id.btnre);
                                btnre.setOnClickListener(
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                            }

                                        }
                                );

                                txt1.setText(data.get(position).getUserName());
                                txt2.setText(data.get(position).getHandleTime());
                                txt3.setText(data.get(position).getHandleState());
                                txt4.setText(data.get(position).getHandleContent());

                            }
                        }
                    }
                }
        );

        btnTempSearch=(Button)view.findViewById(R.id.btnTempSearch);

        //TODO 按鈕:溫度報表搜尋
        btnTempSearch.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listtemprecordTask temprecordTask = new listtemprecordTask();

                        //判斷時間輸入是否為空的防呆機制
                        if(!editTempStartDate.getText().toString().isEmpty() && !editTempEndDate.getText().toString().isEmpty()) {
                            //先做判斷,判斷是選些什麼spinner選項,再非同步
                            //選項:全部 --> 溫度狀態全部、處理狀態全部
                            if (spinTempState.getSelectedItemPosition() == 2 && B_spinHandleState.getSelectedItemPosition() == 2) {
                                String url = String.format(AppUtility.HOST + AppUtility.TempListAll
                                        , editTempStartDate.getText().toString()
                                        , editTempEndDate.getText().toString());
                                Log.i("稽核URL",url);
                                temprecordTask.execute(url);
                            }

                            //URL丟三個參數
                            if((spinTempState.getSelectedItemPosition() == 1 && B_spinHandleState.getSelectedItemPosition() == 2)
                                    || spinTempState.getSelectedItemPosition() == 0){
                                String url = String.format(AppUtility.HOST+AppUtility.TempListE
                                        , editTempStartDate.getText().toString()
                                        , editTempEndDate.getText().toString()
                                        , spinTempState.getSelectedItemPosition());
                                temprecordTask.execute(url);
                            }

                            //URL丟四個參數
                            if((spinTempState.getSelectedItemPosition() == 1 && B_spinHandleState.getSelectedItemPosition() == 0)
                                    || spinTempState.getSelectedItemPosition() == 1 && B_spinHandleState.getSelectedItemPosition() == 1
                                    || spinTempState.getSelectedItemPosition() == 2 && B_spinHandleState.getSelectedItemPosition() == 0
                                    || spinTempState.getSelectedItemPosition() == 2 && B_spinHandleState.getSelectedItemPosition() == 1){

                                String url = String.format(AppUtility.HOST+AppUtility.TempListES
                                        , editTempStartDate.getText().toString()
                                        , editTempEndDate.getText().toString()
                                        , spinTempState.getSelectedItemPosition()
                                        ,B_spinHandleState.getSelectedItemPosition());
                                temprecordTask.execute(url);
                            }


                        }else{
                            Toast.makeText(getActivity(),"請輸入開始時間和結束時間",Toast.LENGTH_SHORT).show();
                        }



                    }
                }
        );



        //TODO 按鈕:開始時間 - 出現日曆選擇日期
        editTempStartDate.setOnClickListener(new OnClickListener() {
            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                DatePickerDialog dateDialog = new DatePickerDialog(getActivity(), DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                editTempStartDate.setText(DateFormat.format("yyyy/MM/dd", calendar));


                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));

                dateDialog.show();
            }
        });

        //TODO 按鈕:結束時間 - 出現日曆選擇日期
        editTempEndDate.setOnClickListener(new OnClickListener(){
            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                DatePickerDialog dateDialog = new DatePickerDialog(getActivity(), DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                editTempEndDate.setText(DateFormat.format("yyyy/MM/dd", calendar));


                            }
                        },c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));

                dateDialog.show();
            }
        });






        return view;
    }

    public class TempRecordAdater extends BaseAdapter {
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
        public View getView(int position, View convertView, ViewGroup parent) {

            listTempRecordTag tempRecordTag=null;
            if(convertView ==null){
                convertView=inflater.inflate(R.layout.list_temprecord,null);
                tempRecordTag=new listTempRecordTag();
                tempRecordTag.TtxtTempLocation=(TextView)convertView.findViewById(R.id.TtxtTempLocation);
                tempRecordTag.TtxtTempTime=(TextView)convertView.findViewById(R.id.TtxtTempTime);
                tempRecordTag.txtHandleState=(TextView)convertView.findViewById(R.id.TtxtHandleState);
                tempRecordTag.txtTemperature=(TextView)convertView.findViewById(R.id.txtTemperature);
                tempRecordTag.txtWet=(TextView)convertView.findViewById(R.id.txtWet);
                convertView.setTag(tempRecordTag);
            }else {
                tempRecordTag=(listTempRecordTag)convertView.getTag();
            }
            tempRecordTag.TtxtTempLocation.setText(data.get(position).getTempLocation());
            tempRecordTag.TtxtTempTime.setText(data.get(position).getTempTime());
            tempRecordTag.txtTemperature.setText(data.get(position).getTemperature());
            tempRecordTag.txtWet.setText(data.get(position).getWet());


            if (data.get(position).getHandleState().equals("已處理")) {
                tempRecordTag.txtHandleState.setText(data.get(position).getHandleState());
                tempRecordTag.txtHandleState.setTextColor(Color.GREEN);
            } else if (data.get(position).getHandleState().equals("未處理")) {
                tempRecordTag.txtHandleState.setText(data.get(position).getHandleState());
                tempRecordTag.txtHandleState.setTextColor(Color.RED);
            } else {
                tempRecordTag.txtHandleState.setText(data.get(position).getHandleState());
                tempRecordTag.txtHandleState.setTextColor(Color.BLACK);
            }

            return convertView;
        }
        public class listTempRecordTag{
            public TextView TtxtTempLocation;
            public TextView TtxtTempTime;
            public TextView txtTemperature;
            public TextView txtWet;
            public TextView txtHandleState;
        }
    }


    public class listtemprecordTask extends AsyncTask<String,Void,String>

    {
        private ProgressDialog dialog;

        @Override
        protected String doInBackground(String... params) {
            //JS5.稽核網路url是否有通
            Log.i("url網址", params[0]);
            InputStream is = AppUtility.openConnection(params[0],"GET");
            String content=AppUtility.streamToString(is,"UTF-8");
            return content;
        }

        @Override
        protected void onPreExecute() {
            dialog=AppUtility.createDialog(Fragment_TempRecord.this.getActivity(), "資料下載中...");
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            data=new ArrayList<>();
            Log.i("json字串", s);
            try {
                JSONArray jsonArray=new JSONArray(s);
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    JsonAll jsonall = new JsonAll();
                    jsonall.setTempLocation(jsonObject.getString("tempLocation"));
                    //jsonall.setTempTime(jsonObject.getString("tempTime"));
                    java.text.DateFormat dateT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date1=dateT.parse(jsonObject.getString("tempTime"));
                    jsonall.setTempTime(dateT.format(date1));


                    jsonall.setTemperature(jsonObject.getString("temperature"));
                    jsonall.setWet(jsonObject.getString("wet"));
                    jsonall.setTempError(jsonObject.getString("tempError"));



                    //溫度處理報表不一定有,需判斷,再去抓值,否則可能抓不到
                    //true和false轉為看得懂的中文
                    if(!jsonObject.getString("handleState").equals("null")) {

                        if(jsonObject.getString("handleState").equals("true")){
                            jsonall.setHandleState("已處理");
                        }else if(jsonObject.getString("handleState").equals("false")) {
                            jsonall.setHandleState("未處理");
                        }
                        jsonall.setUserName(jsonObject.getString("userName"));
                        jsonall.setHandleContent(jsonObject.getString("HandleContent"));

                        //jsonall.setHandleTime(jsonObject.getString("HandleTime"));
                        java.text.DateFormat dateF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = dateF.parse(jsonObject.getString("HandleTime"));
                        jsonall.setHandleTime(dateF.format(date));

                    }else{
                        jsonall.setHandleState("沒有回報");
                    }
                    data.add(jsonall);
                }

                TempRecordAdater adapter=new TempRecordAdater();
                listTempRecord.setAdapter(adapter);

            } catch (JSONException e) {
                Log.i("格式錯誤", e.getMessage());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }



}
