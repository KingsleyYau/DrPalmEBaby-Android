package com.drcom.ui;

import java.util.ArrayList;
import java.util.List;

import com.drcom.ui.R;
import com.drcom.ui.View.controls.GesturesListview.RefreshListView;
import com.drcom.ui.View.controls.GesturesListview.RefreshListView.OnRefreshListener;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RefreshListviewActivity extends Activity{
	private List<String> data;
	private BaseAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewrefreshlistview);
		
		data = new ArrayList<String>();
//		data.add("a");
//		data.add("b");
//		data.add("c");
		
		final RefreshListView listview = (RefreshListView)findViewById(R.id.refreshListView);
		listview.setResId(R.drawable.arrow, 
				R.string.pull_to_refresh_release_label, 
				R.string.pull_to_refresh_pull_label, 
				R.string.pull_to_refresh_refreshing_label, 
				R.string.pull_to_refresh_tap_label, 
				R.string.pull_to_refresh_latest_label);
		listview.setCacheColorHint(Color.BLACK);
		adapter = new BaseAdapter() {
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView tv = new TextView(getApplicationContext());
				tv.setText(data.get(position));
				return tv;
			}

			public long getItemId(int position) {
				return 0;
			}

			public Object getItem(int position) {
				return null;
			}

			public int getCount() {
				return data.size();
			}
		};
		listview.setAdapter(adapter);

		listview.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						data.add("刷新后添加的内容");	//注释掉此句,可看到加载数据为空的效果
						
						//
						if(data.size() == 0){
							listview.clearPullToRefresh(true);
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						adapter.notifyDataSetChanged();
						listview.onRefreshComplete();
					}

				}.execute(null);
			}
		});
		
		//
		if(data.size() == 0){
			listview.setHeadViewVisible();
		}
	}
}
