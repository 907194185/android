package com.ly.activity;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ly.R;
import com.ly.fragment.HomeFragment;
import com.ly.fragment.PhoneFragment;
import com.ly.fragment.WeChatFragment;

public class MainActivity extends FragmentActivity implements OnClickListener{

	private SharedPreferences sp;

	private RelativeLayout homeLayout,wechatLayout,phoneLayout, topTab;
	private Fragment homeFragment,wechatFragment,phoneFragment,currentFragment;
	private ImageView homeImageView;
	private TextView homeTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_main);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { 
			Log.i("ww", "aaaaa");
			//͸��״̬��  
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); 
			//͸��������  
			//getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);  
		}
		sp = getSharedPreferences("wel_setting", MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt("VERSION", 1);
		editor.commit();

		initUI();
		initTab();
	}

	private void initUI(){
		homeLayout = (RelativeLayout) findViewById(R.id.home_layout);
		wechatLayout = (RelativeLayout) findViewById(R.id.wechat_layout);
		phoneLayout = (RelativeLayout) findViewById(R.id.phone_layout);
		topTab = (RelativeLayout) findViewById(R.id.top_tab);

		homeLayout.setOnClickListener(this);
		wechatLayout.setOnClickListener(this);
		phoneLayout.setOnClickListener(this);
		topTab.setOnClickListener(this);

		homeImageView = (ImageView) findViewById(R.id.home_img);
		homeTextView = (TextView) findViewById(R.id.home_text);
	}

	private void initTab(){
		if (homeFragment == null) {
			homeFragment = new HomeFragment();
		}

		if (!homeFragment.isAdded()) {
			getSupportFragmentManager().beginTransaction().add(R.id.content_layout, homeFragment);
			currentFragment = homeFragment;
			getSupportFragmentManager().beginTransaction().replace(R.id.content_layout, homeFragment).commit();
		}
	}


	/** 
	 * �����һ��tab 
	 */  
	private void clickTab1Layout() {  
		if (homeFragment == null) {  
			homeFragment = new HomeFragment();  
		}  
		addOrShowFragment(getSupportFragmentManager().beginTransaction(), homeFragment);  
		homeImageView.setImageResource(R.drawable.home);
		homeTextView.setTextColor(android.graphics.Color.BLACK);
	}  

	/** 
	 * ����ڶ���tab 
	 */  
	private void clickTab2Layout() {  
		if (wechatFragment == null) {  
			wechatFragment = new WeChatFragment();  
		}  
		addOrShowFragment(getSupportFragmentManager().beginTransaction(), wechatFragment);
		homeImageView.setImageResource(R.drawable.home_blur);
		homeTextView.setTextColor(android.graphics.Color.GRAY);
	}  

	/** 
	 * ���������tab 
	 */  
	private void clickTab3Layout() {  
		if (phoneFragment == null) {  
			phoneFragment = new PhoneFragment();  
		}  
		addOrShowFragment(getSupportFragmentManager().beginTransaction(), phoneFragment); 
		homeImageView.setImageResource(R.drawable.home_blur);
		homeTextView.setTextColor(android.graphics.Color.GRAY);
	}  




	/** 
	 * ��ӻ�����ʾ��Ƭ 
	 *  
	 * @param transaction 
	 * @param fragment 
	 */  
	private void addOrShowFragment(FragmentTransaction transaction,  
			Fragment fragment) {  
		if (currentFragment == fragment)  
			return;  

		if (!fragment.isAdded()) { // �����ǰfragmentδ����ӣ�����ӵ�Fragment��������  
			transaction.hide(currentFragment)  
			.add(R.id.content_layout, fragment).commit();  
		} else {  
			transaction.hide(currentFragment).show(fragment).commit();  
		}  

		currentFragment = fragment;  
	}  


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		/*
		 * 
		 * add()�������ĸ������������ǣ�
		 * 
		 * 1��������������Ļ���дMenu.NONE,
		 * 
		 * 2��Id���������Ҫ��Android�������Id��ȷ����ͬ�Ĳ˵�
		 * 
		 * 3��˳���Ǹ��˵�������ǰ������������Ĵ�С����
		 * 
		 * 4���ı����˵�����ʾ�ı�
		 */

		menu.add(Menu.NONE, Menu.FIRST + 1, 5, "ɾ��").setIcon(

				android.R.drawable.ic_menu_delete);

		// setIcon()����Ϊ�˵�����ͼ�꣬����ʹ�õ���ϵͳ�Դ���ͼ�꣬ͬѧ������һ��,��

		// android.R��ͷ����Դ��ϵͳ�ṩ�ģ������Լ��ṩ����Դ����R��ͷ��

		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "����").setIcon(

				android.R.drawable.ic_menu_edit);

		menu.add(Menu.NONE, Menu.FIRST + 3, 6, "����").setIcon(

				android.R.drawable.ic_menu_help);

		menu.add(Menu.NONE, Menu.FIRST + 4, 1, "���").setIcon(

				android.R.drawable.ic_menu_add);

		menu.add(Menu.NONE, Menu.FIRST + 5, 4, "��ϸ").setIcon(

				android.R.drawable.ic_menu_info_details);

		menu.add(Menu.NONE, Menu.FIRST + 6, 3, "����").setIcon(

				android.R.drawable.ic_menu_send);

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {  
		case R.id.home_layout: // ֪��  
			clickTab1Layout();  
			break;  
		case R.id.wechat_layout: // ����֪��  
			clickTab2Layout();  
			break;  
		case R.id.phone_layout: // �ҵ�  
			clickTab3Layout();  
			break;
		case R.id.top_tab:
			openOptionsMenu();
			break;
		default:  
			break;  
		}  
	}
}
