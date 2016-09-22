package com.ly.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;

import com.ly.R;

public class AutoListView extends ListView implements OnScrollListener{

	//���ֵ�ǰ��ˢ�»��Ǽ��ز���
	private static final int REFRESH = 0;
	private static final int LOAD = 1;
	
	private int curOperation = REFRESH;

	//����refresh �� pull�ľ����С
	private static final int SPACE = 20;
	
	private int totalItem;

	//����״̬ �� ��ǰ״̬
	private static final int NONE = 0;
	private static final int PULL = 1;
	private static final int RELEASE = 2;
	private static final int REFRESHING = 3;
	private int state = 0;


	private LayoutInflater inflater;
	private View header;
	private View footer;
	private TextView tip;
	private TextView lastUpdate;
	private ImageView arrow;
	private ProgressBar refreshingBar;


	private TextView noData;
	private TextView loadFull;
	private TextView more;
	private ProgressBar loadingBar;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	private int startY;

	private int firstVisibleItem;
	private int scrollState;
	private int headerContentInitialHeight;  
	private int headerContentHeight;  


	private boolean isRecorded;
	private boolean isLoading;  //�ж��Ƿ����ڼ���
	private boolean loadEnable = true;  //���á��رռ��ظ��๦��
	private boolean isLoadFull;
	private int pageSize = 10;

	private OnRefreshListener onRefreshListener;
	private OnLoadListener onLoadListener;

	private Scroller mScroller;

	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	public AutoListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public AutoListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public AutoListView(Context context) {
		super(context);
		initView(context);
	}

	//����ˢ�¼���
	public void setOnRefreshListener(OnRefreshListener onRefreshListener){
		this.onRefreshListener = onRefreshListener;
	}

	//���ظ������
	public void setOnLoadListener(OnLoadListener onLoadListener){
		this.onLoadListener = onLoadListener;
	}

	public boolean isLoadEnable(){
		return loadEnable;
	}


	// ����Ŀ������߹رռ��ظ��࣬����֧�ֶ�̬����  
	public void setLoadEnable(boolean loadEnable) {  
		this.loadEnable = loadEnable;  
		this.removeFooterView(footer);  
	}  

	public int getPageSize() {  
		return pageSize;  
	}  

	public void setPageSize(int pageSize) {  
		this.pageSize = pageSize;  
	}  


	public void initView(Context context){

		mScroller = new Scroller(context, new DecelerateInterpolator());
		super.setOnScrollListener(this);

		//������ͷ����
		animation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(100);
		animation.setInterpolator(new LinearInterpolator());
		animation.setFillAfter(true);

		//��ԭ��ͷ����
		reverseAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setDuration(100);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setFillAfter(true);


		inflater = LayoutInflater.from(context); 

		header = inflater.inflate(R.layout.listview_header, null);
		tip = (TextView) header.findViewById(R.id.xlistview_header_hint_textview);
		lastUpdate = (TextView) header.findViewById(R.id.xlistview_header_time);
		lastUpdate.setText(format.format(new Date()));
		arrow = (ImageView) header.findViewById(R.id.xlistview_header_arrow);
		refreshingBar = (ProgressBar) header.findViewById(R.id.xlistview_header_progressbar);

		footer = inflater.inflate(R.layout.listview_footer, null);
		more = (TextView) footer.findViewById(R.id.xlistview_footer_hint_textview);
		loadingBar = (ProgressBar) footer.findViewById(R.id.xlistview_footer_progressbar);

		headerContentInitialHeight = header.getPaddingTop();
		measureView(header);
		headerContentHeight = header.getMeasuredHeight();
		topPadding(-headerContentHeight);
		this.addHeaderView(header);  
		this.addFooterView(footer);  
		this.setOnScrollListener(this); 

	}

	public void onRefresh() {  
		if (onRefreshListener != null) {  
			onRefreshListener.onRefresh();  
		}  
	}  

	public void onLoad() {  
		if (onLoadListener != null) {  
			onLoadListener.onLoad();  
		}  
	}  

	public void onRefreshComplete(String updateTime) { 
		Log.i("refresh", "ˢ�����");
		lastUpdate.setText(updateTime);  
		state = NONE;  
		refreshHeaderViewByState();  
	}  

	// ��������ˢ�½�����Ļص�  
	public void onRefreshComplete() {  
		String currentTime = format.format(new Date());  
		onRefreshComplete(currentTime);  
	}  

	// ���ڼ��ظ��������Ļص�  
	public void onLoadComplete() {  
		Log.i("refresh", "�������");
		more.setText(R.string.xlistview_footer_hint_normal);
		loadingBar.setVisibility(View.GONE);
		loadEnable = true;
		isLoading = false;  
	}  


	// ����listview������״̬�ж��Ƿ���Ҫ���ظ���  
	private void ifNeedLoad(AbsListView view, int scrollState) {  
		if (!loadEnable) {   
			return;   
		}  
		try {  
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE  
					&& !isLoading  
					&& view.getLastVisiblePosition() == view  
					.getPositionForView(footer) && !isLoadFull) { 
				footer.setPadding(footer.getPaddingLeft(), footer.getPaddingTop(),  
						footer.getPaddingRight(), 50);  
				footer.invalidate();  
				onLoad();
				more.setText(R.string.xlistview_footer_hint_ready);
				loadingBar.setVisibility(View.VISIBLE);
				isLoading = true;  
			}  
		} catch (Exception e) {  
		}  
	}  

	/** 
	 * ���������¼���������� 
	 */  
	@Override  
	public boolean onTouchEvent(MotionEvent ev) {  
		switch (ev.getAction()) {  
		case MotionEvent.ACTION_DOWN:  
			
			if (firstVisibleItem == 0) {  
				isRecorded = true;  
				startY = (int) ev.getY();  
			}  
			Log.i("action", "----down--------"+startY);
			break;  
		case MotionEvent.ACTION_CANCEL:  
			Log.i("action", "----cancle--------");
		case MotionEvent.ACTION_UP:  
			Log.i("action", "----up--------");
			if(curOperation == REFRESH){
				if (state == PULL) {  
					state = NONE;  
					refreshHeaderViewByState();  
				} else if (state == RELEASE) {  
					state = REFRESHING;  
					refreshHeaderViewByState();  
					onRefresh();  
				}  
				isRecorded = false;
			}else if(curOperation == LOAD){
				more.setText("");
				loadingBar.setVisibility(View.VISIBLE);
				onLoad();
				isLoading = true;
			}
			  
			break;  
		case MotionEvent.ACTION_MOVE:  
			Log.i("action", "----move--------"+(int)ev.getY());
			Log.i("action", "----move1--------"+startY);
			Log.i("action", "----move2--------"+headerContentHeight);
			if (getFirstVisiblePosition() == 0 && curOperation == REFRESH && (ev.getY() - startY) > 0) {
				whenMove(ev); 
			}else if(getLastVisiblePosition() == totalItem - 1 && curOperation == LOAD){
				Log.i("load", "----move--------"+((int)ev.getY()-startY));
				int tmpY = (int) ev.getY();  
				int space = startY - tmpY;  
				if (space>0  && space > footer.getHeight() + space) {
					more.setText(R.string.xlistview_footer_hint_ready);
				}
				
				
			}

			break;  
		}  
		return super.onTouchEvent(ev);  
	}  

	// ������ƣ�ˢ��header״̬  
	private void whenMove(MotionEvent ev) {  
		if (!isRecorded) {  
			return;  
		}  
		int tmpY = (int) ev.getY();  
		int space = tmpY - startY;  
		int topPadding = space - headerContentHeight;  
		switch (state) {  
		case NONE:  
			if (space > 0) {  
				state = PULL;  
				refreshHeaderViewByState();  
			}  
			break;  
		case PULL:  
			topPadding(topPadding); 
			Log.i("scrollState", scrollState+"");
			if (scrollState == SCROLL_STATE_TOUCH_SCROLL  
					&& space > headerContentHeight + SPACE) {  
				state = RELEASE;  
				refreshHeaderViewByState();  
			}  
			break;  
		case RELEASE:  
			topPadding(topPadding); 
			setSelection(0);
			Log.i("release", "--  topPadding="+topPadding);
			Log.i("release", "--  space="+space);
			Log.i("release", "--  a="+(headerContentHeight));
			if (space > 0 && space < headerContentHeight + SPACE) {  
				state = PULL;  
				refreshHeaderViewByState();  
			} else if (space <= 0) {  
				state = NONE;  
				refreshHeaderViewByState();  
			} 
			break;  
		}  

	}  

	// ����header�Ĵ�С����ʵ������ֻ�Ǿ��붥���ĸ߶ȡ�  
	private void topPadding(int topPadding) {  
		header.setPadding(header.getPaddingLeft(), topPadding,  
				header.getPaddingRight(), header.getPaddingBottom());  
		header.invalidate();  
	}  


	/** 
	 * ��������Ǹ��ݽ���Ĵ�С������footer��ʾ�ġ� 
	 * <p> 
	 * ����ٶ�ÿ�����������Ϊ10�����������10��������Ϊ�������ݡ�����������10��������Ϊ�����Ѿ�ȫ�����أ���ʱfooter��ʾ�Ѿ�ȫ������ 
	 * </p> 
	 *  
	 * @param resultSize 
	 */  
	public void setResultSize(int resultSize) {  
		if (resultSize == 0) {  
			isLoadFull = true;  
			//loadFull.setVisibility(View.GONE);  
			loadingBar.setVisibility(View.GONE);  
			more.setVisibility(View.GONE);  
			//noData.setVisibility(View.VISIBLE);  
		} else if (resultSize > 0 && resultSize < pageSize) {  
			isLoadFull = true;  
			//loadFull.setVisibility(View.VISIBLE);  
			loadingBar.setVisibility(View.GONE);  
			more.setVisibility(View.GONE);  
			//noData.setVisibility(View.GONE);  
		} else if (resultSize == pageSize) {  
			isLoadFull = false;  
			//loadFull.setVisibility(View.GONE);  
			loadingBar.setVisibility(View.VISIBLE);  
			more.setVisibility(View.VISIBLE);  
			//noData.setVisibility(View.GONE);  
		}  

	}  

	// ���ݵ�ǰ״̬������header  
	private void refreshHeaderViewByState() {  
		switch (state) {  
		case NONE:  
			topPadding(-headerContentHeight);  
			tip.setText(R.string.xlistview_header_hint_normal);  
			refreshingBar.setVisibility(View.GONE);  
			arrow.clearAnimation();  
			arrow.setImageResource(R.drawable.xlistview_arrow);  
			break;  
		case PULL:  
			arrow.setVisibility(View.VISIBLE);  
			tip.setVisibility(View.VISIBLE);  
			lastUpdate.setVisibility(View.VISIBLE);  
			refreshingBar.setVisibility(View.GONE);  
			tip.setText(R.string.xlistview_header_hint_normal);  
			arrow.clearAnimation();  
			arrow.setAnimation(reverseAnimation);  
			break;  
		case RELEASE:  
			arrow.setVisibility(View.VISIBLE);  
			tip.setVisibility(View.VISIBLE);  
			lastUpdate.setVisibility(View.VISIBLE);  
			refreshingBar.setVisibility(View.GONE);  
			tip.setText(R.string.xlistview_header_hint_ready);  
			arrow.clearAnimation();  
			arrow.setAnimation(animation);  
			break;  
		case REFRESHING:  
			topPadding(headerContentInitialHeight);  
			refreshingBar.setVisibility(View.VISIBLE);  
			arrow.clearAnimation();  
			arrow.setVisibility(View.GONE);  
			tip.setVisibility(View.GONE);  
			lastUpdate.setVisibility(View.GONE);  
			break;  
		}  
	}  


	// ��������header��С�ġ��Ƚ����ޡ�  
	private void measureView(View child) {  
		ViewGroup.LayoutParams p = child.getLayoutParams();  
		if (p == null) {  
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,  
					ViewGroup.LayoutParams.WRAP_CONTENT);  
		}  
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);  
		int lpHeight = p.height;  
		int childHeightSpec;  
		if (lpHeight > 0) {  
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,  
					MeasureSpec.EXACTLY);  
		} else {  
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,  
					MeasureSpec.UNSPECIFIED);  
		}  
		child.measure(childWidthSpec, childHeightSpec);  
	}  

	/* 
	 * ��������ˢ�½ӿ� 
	 */  
	public interface OnRefreshListener {  
		public void onRefresh();  
	}  

	/* 
	 * ������ظ���ӿ� 
	 */  
	public interface OnLoadListener {  
		public void onLoad();  
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		Log.i("scroll","---statechange----");
		this.scrollState = scrollState;  
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){  //ֹͣ������
			if (getFirstVisiblePosition() == 0) {
				curOperation = REFRESH;
			}else if (view.getLastVisiblePosition() == totalItem - 1) {
				curOperation = LOAD;
			} 
		}
		//ifNeedLoad(view, scrollState); 
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		Log.i("scroll","---onscroll----");
		totalItem = totalItemCount;
		this.firstVisibleItem = firstVisibleItem;  
	}  


	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			topPadding(mScroller.getCurrY());
			Log.i("com", "---- "+headerContentHeight+"  ----");
		}
		postInvalidate();
		super.computeScroll();
	}



}
