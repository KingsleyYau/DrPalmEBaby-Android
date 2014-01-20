package com.drcom.ui;
//Download by http://www.codefans.net
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drcom.ui.R;
import com.drcom.ui.View.controls.GesturesListview.RefreshListView;
import com.drcom.ui.View.controls.GesturesListview.RefreshListView.OnRefreshListener;

public class ViewHolder1 extends LinearLayout {
	private List<String> data;
	private BaseAdapter adapter;

	public ViewHolder1(final Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setBackgroundColor(Color.RED);
		
		data = new ArrayList<String>();
		LayoutParams p =new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
		final RefreshListView listview = new RefreshListView(context);
		listview.setLayoutParams(p);
		listview.setResId(R.drawable.arrow, 
				R.string.pull_to_refresh_release_label, 
				R.string.pull_to_refresh_pull_label, 
				R.string.pull_to_refresh_refreshing_label, 
				R.string.pull_to_refresh_tap_label, 
				R.string.pull_to_refresh_latest_label);
		listview.setCacheColorHint(Color.BLACK);
		adapter = new BaseAdapter() {
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView tv = new TextView(context);
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
		
		this.addView(listview);
	}


}
