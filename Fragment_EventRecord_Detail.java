package com.khh.gjun.security.boss;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.khh.gjun.security.R;
import com.khh.gjun.security.apputility.AppUtility;
import com.khh.gjun.security.apputility.EventList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_EventRecord_Detail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_EventRecord_Detail extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //自訂UI物件
    private TextView DtxtEventName;
    private TextView DtxtEventTime;
    private TextView DtxtUserName;
    private TextView DtxtRFID;
    private TextView DtxtEventLocation;
    private TextView DtxtEventLevel;
    private TextView DtxtIsNot;
    private TextView DtxtEventContent;
    private Button btnBack_ErecordD;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_EventRecord_Detail.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_EventRecord_Detail newInstance(String param1, String param2) {
        Fragment_EventRecord_Detail fragment = new Fragment_EventRecord_Detail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_EventRecord_Detail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eventrecord_detail, container, false);
        DtxtEventName = (TextView)view.findViewById(R.id.DtxtEventName);
        DtxtEventTime = (TextView)view.findViewById(R.id.DtxtEventTime);
        DtxtUserName = (TextView)view.findViewById(R.id.DtxtUserName);
        DtxtRFID = (TextView)view.findViewById(R.id.DtxtRFID);
        DtxtEventLocation = (TextView)view.findViewById(R.id.DtxtEventLocation);
        DtxtEventLevel = (TextView)view.findViewById(R.id.DtxtEventLevel);
        DtxtIsNot = (TextView)view.findViewById(R.id.DtxtIsNot);
        DtxtEventContent = (TextView)view.findViewById(R.id.DtxtEventContent);
        btnBack_ErecordD = (Button)view.findViewById(R.id.btnBack_ErecordD);

        EventList eventList = (EventList)getActivity().getIntent().getSerializableExtra("eventrecord");
        DtxtEventName.setText(eventList.getEventName());
        DtxtEventTime.setText(eventList.getEventTime());
        DtxtUserName.setText(eventList.getUserName());

        if(eventList.getRFID_ID()==1){
            DtxtRFID.setText("A區");
        }else if(eventList.getRFID_ID()==2){
            DtxtRFID.setText("B區");
        }else if(eventList.getRFID_ID()==3){
            DtxtRFID.setText("C區");
        }else if(eventList.getRFID_ID()==4){
            DtxtRFID.setText("D區");
        }

        DtxtEventLocation.setText(eventList.getEventLocation());
        DtxtEventLevel.setText(eventList.getEventLevel());
        DtxtIsNot.setText(eventList.getIsNot());
        DtxtEventTime.setText(eventList.getEventTime());
        DtxtEventContent.setText(eventList.getEventContent());


        //TODO 按鈕:返回
        btnBack_ErecordD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //移除該頁面
                fragmentTransaction.remove(AppUtility.curentFragment);
                fragmentTransaction.commit();

                //顯示隱藏的畫面
                LinearLayout layout_eventrecord = (LinearLayout)getActivity().findViewById(R.id.layout_eventrecord);
                layout_eventrecord.setVisibility(View.VISIBLE);

                //雖然不是原來的那個消失的Fragment,但是為了給抽屜判別還是要做這個動作
                Fragment_EventRecord fragment_eventRecord = Fragment_EventRecord.newInstance("","");
                AppUtility.curentFragment = fragment_eventRecord;
            }
        });

        return view;
    }


}
