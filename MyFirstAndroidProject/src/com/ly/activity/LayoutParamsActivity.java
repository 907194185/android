package com.ly.activity;

import com.ly.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class LayoutParamsActivity extends Activity implements OnClickListener{
	
	LinearLayout layout;
	Button button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layoutparams);
		
		layout = (LinearLayout) findViewById(R.id.lp_ll);
		
		button = new Button(this);
		button.setText("button");
		
		button.setOnClickListener(this);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		//LinearLayout.LayoutParams params = (LayoutParams) button.getLayoutParams();
		params.topMargin = -20;
		button.setLayoutParams(params);
		
		layout.addView(button);
	}
	@Override
	public void onClick(View v) {
		Toast.makeText(LayoutParamsActivity.this, "aaaaaaaaaa", Toast.LENGTH_LONG).show();
	}
}
