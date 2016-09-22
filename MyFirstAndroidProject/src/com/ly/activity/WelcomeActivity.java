package com.ly.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.ly.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class WelcomeActivity extends Activity {

	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ر�����
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����״̬��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.welcome);
		final Intent it = new Intent(this, GuideActivity.class); //��Ҫת���Activity   
		final Intent it2 = new Intent(this, VPFragmentActivity.class); //��Ҫת���Activity   
		Timer timer = new Timer(); 
		TimerTask task = new TimerTask() {  
			@Override  
			public void run() {
				sp = getSharedPreferences("wel_setting",Context.MODE_PRIVATE);
				
				if (sp.getInt("VERSION", 0) == 0) {
					startActivity(it); //ִ��  
				}else {
					startActivity(it2); //ִ��  
				}
				WelcomeActivity.this.finish();
			} 
		};
		timer.schedule(task, 1000 * 3); //10���
	}

}
