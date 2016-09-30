package com.ly.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import android.R.integer;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ly.R;
import com.ly.activity.SelectPicPopupWindowActivity;
import com.ly.po.FormFile;
import com.ly.utils.UploadFileUtil;

public class WeChatFragment extends Fragment implements OnClickListener{

	private Button button, uploadBtn, cameraBtn, netWrokBtn, dialogBtn, dialogFragmentBtn;
	private ImageView imageView;
	private TextView textView;
	private ProgressBar pb;
	private View view;
	private static final int REQUEST_IMAGE_CODE = 0;
	private String srcPath;
	private File tempFile;
	private String PHOTO_FILE_NAME = "BB.jpg";

	private static final int NETWORK_WIFI = 1;
	private static final int NETWORK_CMWAP = 2;
	private static final int NETWORK_CMNET = 3;
	private static final String TAG = "WeChat";


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.wechat_fragment, container, false);
		initUI();
		return view;
	}

	public void initUI(){
		button = (Button) view.findViewById(R.id.bj_photo);
		uploadBtn = (Button) view.findViewById(R.id.upload_btn);
		imageView = (ImageView) view.findViewById(R.id.img_prev);
		netWrokBtn = (Button) view.findViewById(R.id.net_status_btn);
		textView = (TextView) view.findViewById(R.id.wechat_pb_text);
		pb = (ProgressBar) view.findViewById(R.id.wechat_pb);

		dialogBtn = (Button) view.findViewById(R.id.wechat_dialog);
		dialogFragmentBtn = (Button) view.findViewById(R.id.wechat_dialog_fragment);

		button.setOnClickListener(this);
		uploadBtn.setOnClickListener(this);
		netWrokBtn.setOnClickListener(this);
		dialogBtn.setOnClickListener(this);
		dialogFragmentBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bj_photo:
			//getImageFromAlbum();
			startActivityForResult(new Intent(getActivity(), SelectPicPopupWindowActivity.class), REQUEST_IMAGE_CODE);
			break;
		case R.id.upload_btn:
			/*UploadFileUtil uploadFileUtil = new UploadFileUtil();
			try {
				File file = new File(srcPath);
				String fileName=file.getName();
				String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
				FormFile formFile = new FormFile();
				formFile.setFile(file);
				formFile.setContentType("image/jpeg");
				formFile.setFilename(fileName);
				formFile.setName("bjimage");
				uploadFileUtil.upload(new HashMap<String, Object>(), new FormFile[]{formFile}, "");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i("upload",e.getMessage());
			}*/
			MyTask myTask = new MyTask();
			myTask.execute("http://www.baidu.com");
			uploadBtn.setEnabled(false);
			break;
		case R.id.net_status_btn:
			getNetwork();
			break;
		case R.id.wechat_dialog:
			dialog1();
			break;
		case R.id.wechat_dialog_fragment:
			//MyDialogFragment myDialogFragment = new MyDialogFragment();
			//myDialogFragment.show(getFragmentManager(), "MyDialogFragment");
			
			MyLoginDialogFragment loginDialogFragment = new MyLoginDialogFragment();
			loginDialogFragment.show(getFragmentManager(), "MyLoginDialogFragment");
			
			break;
		default:
			break;
		}

	}

	/**
	 * 相册获取
	 */
	protected void getImageFromAlbum() {  
		Intent intent = new Intent(Intent.ACTION_PICK);  
		intent.setType("image/*");//相片类型  
		startActivityForResult(intent, REQUEST_IMAGE_CODE);  
	}  

	/*
	 * 从相机获取
	 */
	public void camera(View view) {
		// 激活相机
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// 判断存储卡是否可以用，可用进行存储
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			tempFile = new File(Environment.getExternalStorageDirectory(),
					PHOTO_FILE_NAME);
			Log.i("WeChat","====ddd==222222222");
			// 从文件中创建uri
			Uri uri = Uri.fromFile(tempFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		}
		Log.i("WeChat","====ddd==33333333333");
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
		startActivityForResult(intent, 1);
	}


	/*@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("WeChat",requestCode + "=====" + resultCode);
		if (requestCode == REQUEST_IMAGE_CODE) {
			Log.i("WeChat","=====11111111111" );
			if (data != null) {
				Log.i("WeChat","=====22222222222" );
				imageView.setImageURI(data.getData());
				ContentResolver cr = getActivity().getContentResolver();  
				Cursor c = cr.query(data.getData(), null, null, null, null);  
				c.moveToFirst();  
				//这是获取的图片保存在sdcard中的位置  
				srcPath = c.getString(c.getColumnIndex("_data"));  
				System.out.println(srcPath+"----------保存路径2"); 
				Log.i("WeChat","=====22222222222"+ srcPath );
			}
		}else if (requestCode == 1) {
			Bitmap bitmap;
			try {
				bitmap = BitmapFactory.decodeStream(new FileInputStream(tempFile));
				imageView.setImageBitmap(bitmap);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}*/


	/**
	 * 回调
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("WeChat",requestCode + "=====" + resultCode);
		if (requestCode == REQUEST_IMAGE_CODE && resultCode == 0) {
			Log.i("WeChat","=====11111111111" );
			if (data != null) {
				Log.i("WeChat","=====22222222222" );
				imageView.setImageURI(data.getData());
				ContentResolver cr = getActivity().getContentResolver();  
				Cursor c = cr.query(data.getData(), null, null, null, null);  
				c.moveToFirst();  
				//这是获取的图片保存在sdcard中的位置  
				srcPath = c.getString(c.getColumnIndex("_data"));  
				System.out.println(srcPath+"----------保存路径2"); 
				Log.i("WeChat","=====22222222222"+ srcPath );
			}
		}else if (requestCode == REQUEST_IMAGE_CODE && resultCode == 1) {
			Bitmap bitmap;
			try {
				Toast.makeText(getActivity(),data.getExtras().getString("filepath"), Toast.LENGTH_SHORT).show();
				bitmap = BitmapFactory.decodeFile(data.getExtras().getString("filepath"));
				imageView.setImageBitmap(bitmap);
				//imageView.setImageURI(data.getData());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 获取网络状态
	 * @return 状态值
	 */
	public int getNetwork(){
		int netType = 0;
		ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info == null) {
			Toast.makeText(getActivity(), "无网络连接！", Toast.LENGTH_SHORT).show();
			return netType;
		}
		netType = info.getType();
		if (netType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = info.getExtraInfo();
			if (!extraInfo.isEmpty()) {
				if (extraInfo.toLowerCase().equals("cmnet")) {
					Toast.makeText(getActivity(), "cmnet连接！", Toast.LENGTH_SHORT).show();
					netType = NETWORK_CMNET;
				}else {
					Toast.makeText(getActivity(), "cmwap连接！", Toast.LENGTH_SHORT).show();
					netType = NETWORK_CMWAP;
				}
			}

		}else if (netType == ConnectivityManager.TYPE_WIFI) {
			Toast.makeText(getActivity(), "Wifi连接！", Toast.LENGTH_SHORT).show();
			netType = NETWORK_WIFI;
		}
		return netType;
	}


	public class MyTask extends AsyncTask<String, Integer, String>{

		//执行耗时任务前更新ui
		@Override
		protected void onPreExecute() {
			Log.i(TAG, "onPreExecute");
			textView.setText("Loding...");
		}

		//执行后台任务  不能在此更新ui
		@Override
		protected String doInBackground(String... params) {
			Log.i(TAG, "doInBackground(Params... params) called"); 
			try {
				URL url = new URL(params[0]);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				//不设置这个参数将无法获取contentLength 即一直返回-1
				conn.setRequestProperty("Accept-Encoding", "identity");
				conn.connect();
				InputStream in = conn.getInputStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int total = conn.getContentLength();
				Log.i(TAG, "total=="+total);
				int count = 0;
				byte[] buff = new byte[1024];
				int len = 0;
				while((len = in.read(buff)) != -1){
					out.write(buff, 0, len);
					count += len;
					Log.i(TAG, "count=="+count);
					publishProgress((int)((count/(float)total)*100));
					Thread.sleep(1000);
				}
				return new String(out.toByteArray(), "utf-8");

			} catch (Exception e) {
				Log.e(TAG, e.getMessage()); 
			}
			return null;
		}

		//更新进度信息
		@Override
		protected void onProgressUpdate(Integer... progresses) {
			Log.i(TAG, "onProgressUpdate(Progress... progresses) called");  
			pb.setProgress(progresses[0]);  
			textView.setText("loading..." + progresses[0] + "%"); 
		}

		//执行完后台任务后被调用 更新ui信息
		@Override
		protected void onPostExecute(String result) {
			Log.i(TAG, "onPostExecute(Result result) called");  
			textView.setText(result);  

			uploadBtn.setEnabled(true);  

		}

		//用于取消任务时更新ui信息
		@Override
		protected void onCancelled() {
			Log.i(TAG, "onCancelled() called");  
			textView.setText("cancelled");  
			pb.setProgress(0);
		}

	}


	public void dialog1(){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("提示");
		builder.setMessage("是否确认退出？");
		builder.setIcon(R.drawable.ic_launcher);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Toast.makeText(getActivity(), "确认"+which, Toast.LENGTH_SHORT).show();
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Toast.makeText(getActivity(), "取消"+which, Toast.LENGTH_SHORT).show();
			}
		});
		
		builder.setNeutralButton("忽略", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Toast.makeText(getActivity(), "忽略"+which, Toast.LENGTH_SHORT).show();
			}
		});
		
		builder.create().show();
	}

}


