package com.ly.fragment;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import android.R.integer;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import android.widget.Toast;

import com.ly.R;
import com.ly.activity.SelectPicPopupWindowActivity;
import com.ly.po.FormFile;
import com.ly.utils.UploadFileUtil;

public class WeChatFragment extends Fragment implements OnClickListener{

	private Button button, uploadBtn, cameraBtn, netWrokBtn;
	private ImageView imageView;
	private View view;
	private static final int REQUEST_IMAGE_CODE = 0;
	private String srcPath;
	private File tempFile;
	private String PHOTO_FILE_NAME = "BB.jpg";
	
	private static final int NETWORK_WIFI = 1;
	private static final int NETWORK_CMWAP = 2;
	private static final int NETWORK_CMNET = 3;


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
		
		button.setOnClickListener(this);
		uploadBtn.setOnClickListener(this);
		netWrokBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bj_photo:
			//getImageFromAlbum();
			startActivityForResult(new Intent(getActivity(), SelectPicPopupWindowActivity.class), REQUEST_IMAGE_CODE);
			break;
		case R.id.upload_btn:
			UploadFileUtil uploadFileUtil = new UploadFileUtil();
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
			}
			break;
		case R.id.net_status_btn:
			getNetwork();
			break;
		default:
			break;
		}

	}

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
	
	
}
