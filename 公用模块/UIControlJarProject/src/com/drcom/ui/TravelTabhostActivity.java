package com.drcom.ui;

import java.util.ArrayList;

import com.drcom.ui.R;
import com.drcom.ui.View.controls.TravelTab.TravelTabHost;
import com.drcom.ui.View.controls.TravelTab.TraveltabItem;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class TravelTabhostActivity extends Activity{
	TravelTabHost  travelTabHost;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewtraveltabhost);

		travelTabHost = (TravelTabHost)findViewById(R.id.travelTabHost);
		init();
	}
	
	private void init(){
		TraveltabItem it1 = new TraveltabItem();
		it1.mItemPicIDs = R.drawable.ic_launcher;
		it1.mItemNames = "1";
		it1.mSelectable = true;
		
		TraveltabItem it2 = new TraveltabItem();
		it2.mItemPicIDs = R.drawable.ic_launcher;
		it2.mItemNames = "2";
		it2.mSelectable = true;
		
		TraveltabItem it3 = new TraveltabItem();
		it3.mItemPicIDs = R.drawable.ic_launcher;
		it3.mItemNames = "3";
		it3.mSelectable = true;
		
		TraveltabItem it4 = new TraveltabItem();
		it4.mItemPicIDs = R.drawable.ic_launcher;
		it4.mItemNames = "4";
		it4.mSelectable = true;
		
		TraveltabItem it5 = new TraveltabItem();
		it5.mItemPicIDs = R.drawable.ic_launcher;
		it5.mItemNames = "5";
		it5.mSelectable = true;
		
		ArrayList<TraveltabItem> traveltabItemList = new ArrayList<TraveltabItem>(); 
		traveltabItemList.add(it1);
		traveltabItemList.add(it2);
		traveltabItemList.add(it3);
		traveltabItemList.add(it4);
		traveltabItemList.add(it5);
		
		ArrayList<View> viewList = new ArrayList<View>();
		viewList.add(new ViewHolder0(this));
		viewList.add(new ViewHolder1(this));
		viewList.add(new ViewHolder2(this));
		
		travelTabHost.initView(R.drawable.bg, R.drawable.cover,
				R.drawable.arrow_left,R.drawable.arrow_right,
				traveltabItemList,viewList);
	}
}
