package com.ly.activity;

import java.util.ArrayList;

import com.ly.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuideActivity extends Activity implements OnClickListener, OnPageChangeListener{


	private ViewPager viewPager; 
	private ArrayList<View> pageViews = new ArrayList<View>(); 
	private boolean misScrolled = false;

	private ImageView[] points;
	private int currentIndex;
	private View view3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guide_main);
		init();
		initPoint();
		viewPager.setAdapter(new GuidePagerAdapter());
		viewPager.setOnPageChangeListener(this);
	}


	private void init(){
		this.getView();
	}


	private void getView(){
		viewPager = (ViewPager) this.findViewById(R.id.viewPager);

		LayoutInflater layoutInflater = getLayoutInflater();
		View view1 = layoutInflater.inflate(R.layout.item01, null);
		View view2 = layoutInflater.inflate(R.layout.item02, null);
		view3 = layoutInflater.inflate(R.layout.item03, null);

		pageViews.add(view1);
		pageViews.add(view2);
		pageViews.add(view3);
	}

	private void initPoint(){
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll); 
		points = new ImageView[pageViews.size()];
		for(int i = 0; i<points.length; i++){
			points[i] = (ImageView) ll.getChildAt(i);
			points[i].setEnabled(true);
			points[i].setOnClickListener(this);
			points[i].setTag(i);
		}

		currentIndex = 0;
		points[currentIndex].setEnabled(false);

	}



	class GuidePagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager)container).addView(pageViews.get(position));
			return pageViews.get(position);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {

			((ViewPager)container).removeView(pageViews.get(position));
		}

	}



	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		setCurView(position);
	}

	private void setCurView(int position){
		if (position < 0 || position > pageViews.size()) {
			return;
		}

		viewPager.setCurrentItem(position);
	}

	private void setCurPoint(int position){
		if (position < 0 || position > pageViews.size() - 1 || position == currentIndex) {
			return;
		}

		points[position].setEnabled(false);
		points[currentIndex].setEnabled(true);

		currentIndex = position;
		
	}


	@Override
	public void onPageSelected(int arg0) {
		setCurPoint(arg0);
		
		if (arg0 == pageViews.size() - 1) {
			Button startBtn = (Button) view3.findViewById(R.id.btn);
			startBtn.setVisibility(View.VISIBLE);
			startBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(GuideActivity.this, VPFragmentActivity.class);
					startActivity(intent);
					GuideActivity.this.finish();
				}
			});
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageScrollStateChanged(int state) {
		if (state == ViewPager.SCROLL_STATE_DRAGGING) {
			//正在滑动   pager处于正在拖拽中
			misScrolled = false;
			Log.d("测试代码", "onPageScrollStateChanged=======正在滑动" + "SCROLL_STATE_DRAGGING");

		} else if (state == ViewPager.SCROLL_STATE_SETTLING) {
			misScrolled = false;
			//pager正在自动沉降，相当于松手后，pager恢复到一个完整pager的过程
			Log.d("测试代码", "onPageScrollStateChanged=======自动沉降" + "SCROLL_STATE_SETTLING");

		} else if (state == ViewPager.SCROLL_STATE_IDLE) {
			//空闲状态  pager处于空闲状态
			Log.d("测试代码", "onPageScrollStateChanged=======空闲状态" + "SCROLL_STATE_IDLE");
			if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !misScrolled) {
				/*Intent intent = new Intent(GuideActivity.this, MainActivity.class);
				startActivity(intent);
				GuideActivity.this.finish();*/
			}
			misScrolled = true;
		} 




	}

}
