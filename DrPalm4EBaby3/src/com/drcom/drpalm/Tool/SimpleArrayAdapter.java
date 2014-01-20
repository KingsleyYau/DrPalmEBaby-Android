package com.drcom.drpalm.Tool;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public abstract class SimpleArrayAdapter<T> extends ArrayAdapter<T> {
	protected Context mContext;
	protected int mRowResourceId;
	boolean mHasHeader;
	
	public SimpleArrayAdapter(Context context, List<T> items) {
		super(context, 0, 0, items);
		mContext = context;		
		mHasHeader = false;
	}
	public SimpleArrayAdapter(Context context, List<T> items, int rowResourceId) {
		super(context, 0, 0, items);
		mContext = context;
		mRowResourceId = rowResourceId;
		mHasHeader = false;
	}
	
	@Override 
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null) {
			LinearLayout layout = new LinearLayout(mContext);
			layout.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT));
			layout.setOrientation(LinearLayout.HORIZONTAL);
			convertView = layout;
		}
		
		final T item = getItem(position);
		
		updateView(item, convertView);
		
		return convertView;
	}
		
	public abstract void updateView(T item, View view);
	
	public static interface OnItemClickListener<T> {
		public void onItemSelected(T item);
	}
	
	public void setOnItemClickListener(AdapterView<?> adapterView, final OnItemClickListener<T> listener) {
		adapterView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if(mHasHeader) {
					position = position - 1;
				}
				
				T item = getItem(position);
				listener.onItemSelected(item);
			}
		});
	}
	
	public void setHasHeader(boolean hasHeader) {
		mHasHeader = true;
	}
}
