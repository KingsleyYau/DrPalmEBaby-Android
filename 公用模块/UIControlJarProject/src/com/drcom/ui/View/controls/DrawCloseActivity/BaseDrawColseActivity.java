package com.drcom.ui.View.controls.DrawCloseActivity;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class BaseDrawColseActivity extends Activity{
	private Context mContext;
	
	private String Color_halfalpha = "#55000000";	//��͸������ɫ
	// �ؼ�
	private ActivityDrawViewGroup mViewGroup;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mContext = BaseDrawColseActivity.this;
        
        setContentView(initMainView());
    }
    
    /**
     * ��ʼ��VIEW�Ļ�������
     */
    private LinearLayout initMainView(){
    	LinearLayout mainLLayout = new LinearLayout(mContext);
    	
    	// ��͸����VIEW,����͸����������һ���ACTIVITY
    	View vnull = new View(mContext);
    	
    	LayoutParams p = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    	mViewGroup = new ActivityDrawViewGroup(mContext);
    	mViewGroup.setLayoutParams(p);
    	mViewGroup.setBackgroundColor(Color.parseColor(Color_halfalpha));
    	mViewGroup.addView(vnull);
    	mViewGroup.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(mViewGroup.getChildCount()>1){
					mViewGroup.scrollToScreen(1, 0);
				}
			}
		}, 200);
        
        mViewGroup.SetonFlingListener(new onFlingListener() {

			@Override
			public boolean onFling() {
				if(mViewGroup.getChildCount()>1){
					if(mViewGroup.getCurrentScreenIndex()==0){
						finish();
					}
				}
				return false;
			}
        });
    	
        mainLLayout.addView(mViewGroup);
    	return mainLLayout;
    }

    /**
     * ���Զ����ּ���Activity��
     * @param view
     */
    protected void SetView(View view){
    	mViewGroup.addView(view);
    	mViewGroup.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(mViewGroup.getChildCount()>1){
					mViewGroup.scrollToScreen(1, 0);
				}
			}
		}, 200);
    }
    
    /**
     * ���Զ����ּ���Activity��
     * @param resid
     */
    protected void SetView(int resid){
//    	SetView(findViewById(resid));
    	View view=LayoutInflater.from(this).inflate(resid, null); 
    	SetView(view);
    }
}
