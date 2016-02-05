package com.khh.gjun.security.apputility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.khh.gjun.security.R;

/**
 * Created by student on 2015/12/7.
 */
public class AppAdapter{


    public static BaseAdapter spinRFIDAdapter(Context context){
        SpinRFIDAdapter spinRFIDAdapter = new SpinRFIDAdapter(context);
        return spinRFIDAdapter;
    }

    public static BaseAdapter spinEventLevel(Context context){
        SpinEventLevel spinEventLevel = new SpinEventLevel(context);
        return spinEventLevel;
    }

    public static BaseAdapter spinIsNot(Context context){
        SpinIsNot spinIsNot = new SpinIsNot(context);
        return spinIsNot;
    }

    //TODO SpinRFIDAdapter
    private static class SpinRFIDAdapter extends BaseAdapter{
        private LayoutInflater inflater;

        public SpinRFIDAdapter(Context context){
            inflater = LayoutInflater.from(context);;
        }

        @Override
        public int getCount() {
            return 5;
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
                convertView = inflater.inflate(R.layout.spin_rfid,null);
                tag.StxtRFID = (TextView)convertView.findViewById(R.id.StxtRFID);
                convertView.setTag(tag);

            }else{
                tag = (ViewTag)convertView.getTag();

            }

            return convertView;
        }

        private class ViewTag{
            private TextView StxtRFID;
        }
    }

    //TODO SpinEventLevel
    private static class SpinEventLevel extends BaseAdapter{
        private LayoutInflater inflater;

        //建構子注入context
        public SpinEventLevel(Context context){
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object getItem(int position) {
            if(position == 0){
                return "輕度";
            }else if(position == 1){
                return "中度";
            }else{
                return "重度";
            }

        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewTag tag = new ViewTag();
            String[] eventLevel = {"輕度", "中度", "重度"};
            if(convertView == null){
                convertView = inflater.inflate(R.layout.spin_eventlevel,null);
                tag.StxtEventLevel = (TextView)convertView.findViewById(R.id.StxtEventLevel);
                convertView.setTag(tag);

            }else{
                tag = (ViewTag)convertView.getTag();

            }

            tag.StxtEventLevel.setText(eventLevel[position]);

            return convertView;

        }
        private class ViewTag{
            private TextView StxtEventLevel;
        }
    }

    //TODO spinIsNot
    private static class SpinIsNot extends BaseAdapter{
        private LayoutInflater inflater;

        //建構子注入context
        public SpinIsNot(Context context){
            inflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int position) {
            if(position == 0){
                return "未處理";
            }else{
                return "已處理";
            }
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewTag tag = new ViewTag();
            String[] IsNot = {"未處理", "已處理"};
            if(convertView == null){
                convertView = inflater.inflate(R.layout.spin_isnot,null);
                tag.StxtIsNot = (TextView)convertView.findViewById(R.id.StxtIsNot);
                convertView.setTag(tag);

            }else{
                tag = (ViewTag)convertView.getTag();

            }

            tag.StxtIsNot.setText(IsNot[position]);

            return convertView;

        }
        private class ViewTag{
            private TextView StxtIsNot;
        }



        }
    }

