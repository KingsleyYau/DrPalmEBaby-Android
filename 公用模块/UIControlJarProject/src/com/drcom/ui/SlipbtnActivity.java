package com.drcom.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.drcom.ui.R;
import com.drcom.ui.View.controls.SlipBtn.SlipButton;
import com.drcom.ui.View.controls.SlipBtn.SlipButton.OnChangedListener;

public class SlipbtnActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewslipbtn);
		
		SlipButton slipbtn = (SlipButton)findViewById(R.id.slipbutton);
		slipbtn.SetResid (R.drawable.switch_on,R.drawable.switch_off, R.drawable.switch_slider);
		
		slipbtn.SetOnChangedListener(new OnChangedListener() {
			public void OnChanged(boolean CheckState) {
				if (CheckState) {
					Toast.makeText(SlipbtnActivity.this, "open",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(SlipbtnActivity.this, "close",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
