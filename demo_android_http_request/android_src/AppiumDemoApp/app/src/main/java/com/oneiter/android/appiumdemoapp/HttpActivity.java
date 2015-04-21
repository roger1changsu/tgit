package com.oneiter.android.appiumdemoapp;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HttpActivity extends ActionBarActivity {

    private ListView mReqDemoListView;

    private List<Map<String, String>> mData;

    private ReqDemoListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);

        initData();
        initView();
        initEvent();

    }

    private void initData() {
        mData = initReqDemoListViewData();
    }

    private void initView() {
        mReqDemoListView = (ListView) findViewById(R.id.lv_req_demo);

        mAdapter = new ReqDemoListViewAdapter(this, mData);

        mReqDemoListView.setAdapter(mAdapter);
    }

    private void initEvent() {
        mReqDemoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> item = (Map<String, String>) view.getTag();
                Toast.makeText(HttpActivity.this.getApplicationContext(), item.get("name"), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_http, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<Map<String, String>> initReqDemoListViewData() {
        List<Map<String, String>> lst = new ArrayList<Map<String, String>>();

        Map<String, String> item1 = new HashMap<String, String>();
        item1.put("name", "with jsessionid request");

        // TODO

        lst.add(item1);

        return lst;
    }

    private class ReqDemoListViewAdapter extends BaseAdapter {

        private List<Map<String, String>> mData;

        private LayoutInflater mLayoutInflater;

        public ReqDemoListViewAdapter(Context context, List<Map<String, String>> data) {

            mData = data;
            mLayoutInflater = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView tvItemName = null;

            Map<String, String> o = mData.get(position);

            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.activity_req_demo_listview_item, null);
                tvItemName = (TextView) convertView.findViewById(R.id.tv_item_name);

                convertView.setTag(o);
            }

            tvItemName.setText(o.get("name"));

            return convertView;
        }
    }
}
