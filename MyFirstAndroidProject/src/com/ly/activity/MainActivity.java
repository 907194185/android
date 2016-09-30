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
			//透明状态栏  
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); 
			//透明导航栏  
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
	 * 点击第一个tab 
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
	 * 点击第二个tab 
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
	 * 点击第三个tab 
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
	 * 添加或者显示碎片 
	 *  
	 * @param transaction 
	 * @param fragment 
	 */  
	private void addOrShowFragment(FragmentTransaction transaction,  
			Fragment fragment) {  
		if (currentFragment == fragment)  
			return;  

		if (!fragment.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中  
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
		 * add()方法的四个参数，依次是：
		 * 
		 * 1、组别，如果不分组的话就写Menu.NONE,
		 * 
		 * 2、Id，这个很重要，Android根据这个Id来确定不同的菜单
		 * 
		 * 3、顺序，那个菜单现在在前面由这个参数的大小决定
		 * 
		 * 4、文本，菜单的显示文本
		 */

		menu.add(Menu.NONE, Menu.FIRST + 1, 5, "删除").setIcon(

				android.R.drawable.ic_menu_delete);

		// setIcon()方法为菜单设置图标，这里使用的是系统自带的图标，同学们留意一下,以

		// android.R开头的资源是系统提供的，我们自己提供的资源是以R开头的

		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "保存").setIcon(

				android.R.drawable.ic_menu_edit);

		menu.add(Menu.NONE, Menu.FIRST + 3, 6, "帮助").setIcon(

				android.R.drawable.ic_menu_help);

		menu.add(Menu.NONE, Menu.FIRST + 4, 1, "添加").setIcon(

				android.R.drawable.ic_menu_add);

		menu.add(Menu.NONE, Menu.FIRST + 5, 4, "详细").setIcon(

				android.R.drawable.ic_menu_info_details);

		menu.add(Menu.NONE, Menu.FIRST + 6, 3, "发送").setIcon(

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
		case R.id.home_layout: // 知道  
			clickTab1Layout();  
			break;  
		case R.id.wechat_layout: // 我想知道  
			clickTab2Layout();  
			break;  
		case R.id.phone_layout: // 我的  
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
