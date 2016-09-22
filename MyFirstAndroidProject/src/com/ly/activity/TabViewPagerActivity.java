package com.ly.activity;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ly.R;
import com.ly.adapter.TabAdapter;
import com.ly.fragment.HomeFragment;
import com.ly.fragment.PhoneFragment;
import com.ly.fragment.WeChatFragment;

public class TabViewPagerActivity extends FragmentActivity implements OnClickListener, OnPageChangeListener{

	private SharedPreferences sp;

	private RelativeLayout homeLayout,wechatLayout,phoneLayout;
	private Fragment homeFragment,wechatFragment,phoneFragment,currentFragment;
	private ImageView homeImageView;
	private TextView homeTextView, wechattTextView, phoneTextView;
	private ViewPager viewPager;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	
	//private TabPageIndicator tabPageIndicator;
	private FragmentPagerAdapter tabAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_vp_main);
		sp = getSharedPreferences("wel_setting", MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt("VERSION", 1);
		editor.commit();

		initUI();
		initData();
		//initTab();
	}

	private void initUI(){
		homeLayout = (RelativeLayout) findViewById(R.id.home_layout);
		wechatLayout = (RelativeLayout) findViewById(R.id.wechat_layout);
		phoneLayout = (RelativeLayout) findViewById(R.id.phone_layout);

		//homeLayout.setOnClickListener(this);
		//wechatLayout.setOnClickListener(this);
		//phoneLayout.setOnClickListener(this);

		viewPager = (ViewPager) findViewById(R.id.fragmentVP);

		homeImageView = (ImageView) findViewById(R.id.home_img);
		homeTextView = (TextView) findViewById(R.id.home_text);
		
		wechattTextView = (TextView) findViewById(R.id.wechat_text);
		phoneTextView = (TextView) findViewById(R.id.phone_text);
		
		//tabPageIndicator = (TabPageIndicator) findViewById(R.id.page_indicator);
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

	private void initData(){
		homeFragment = new HomeFragment();
		wechatFragment = new WeChatFragment();
		phoneFragment = new PhoneFragment();

		fragments.add(homeFragment);
		fragments.add(wechatFragment);
		fragments.add(phoneFragment);

		//FragmentPagerAdapter fragmentPaperAdapter = new MyFragmentPaperAdapter(getSupportFragmentManager(), fragments);
		tabAdapter = new TabAdapter(getSupportFragmentManager(),fragments);
		viewPager.setAdapter(tabAdapter);
		//viewPager.setCurrentItem(0);
		//viewPager.setOnPageChangeListener(this);
		//tabPageIndicator.setViewPager(viewPager);
		//tabPageIndicator.setOnClickListener(this);

	}


	/** 
	 * 点击第一个tab 
	 */  
	private void clickTab1Layout() {  
		viewPager.setCurrentItem(0);
	}  

	/** 
	 * 点击第二个tab 
	 */  
	private void clickTab2Layout() {  
		viewPager.setCurrentItem(1);
	}  

	/** 
	 * 点击第三个tab 
	 */  
	private void clickTab3Layout() {  
		viewPager.setCurrentItem(2);
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
		getMenuInflater().inflate(R.menu.main, menu);
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
		default:  
			break;  
		}  
	}



	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		Toast.makeText(getApplicationContext(), TabAdapter.titles[arg0], Toast.LENGTH_SHORT).show();  

	}
}
