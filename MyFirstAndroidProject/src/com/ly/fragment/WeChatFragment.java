package com.ly.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.ly.R;
import com.ly.po.FormFile;
import com.ly.utils.UploadFileUtil;

public class WeChatFragment extends Fragment implements OnClickListener{

	private Button button, uploadBtn, cameraBtn;
	private ImageView imageView;
	private View view;
	private static final int REQUEST_IMAGE_CODE = 0;
	private String srcPath;
	private File tempFile;
	private String PHOTO_FILE_NAME = "BB.jpg";


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
		cameraBtn = (Button) view.findViewById(R.id.camera_photo);
		imageView = (ImageView) view.findViewById(R.id.img_prev);
		button.setOnClickListener(this);
		uploadBtn.setOnClickListener(this);
		uploadBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bj_photo:
			getImageFromAlbum();
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
		case R.id.camera_photo:

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
			// 从文件中创建uri
			Uri uri = Uri.fromFile(tempFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		}
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
		startActivityForResult(intent, 1);
	}


	@Override
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
	}
}
