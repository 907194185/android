package com.ly.activity;

import java.io.File;
import java.io.IOException;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ly.R;
import com.ly.constant.Constant;

public class SelectPicPopupWindowActivity extends Activity implements OnClickListener{

	LinearLayout layout;
	Button bjBtn, cameraBtn, cancelBtn;
	private String srcPath;
	private File tempFile;
	private String PHOTO_FILE_NAME = "BB.jpg";
	private static final int REQUEST_IMAGE_CODE = 0;
	private static final int REQUEST_CAMERA_CODE = 1;
	private String filepath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popupwindow);
		initUI();
	}

	public void initUI(){
		layout = (LinearLayout) this.findViewById(R.id.pop_layout);
		bjBtn = (Button) this.findViewById(R.id.pop_btn1);
		cameraBtn = (Button) this.findViewById(R.id.pop_btn2);
		cancelBtn = (Button) this.findViewById(R.id.pop_cancel);

		layout.setOnClickListener(this);
		bjBtn.setOnClickListener(this);
		cameraBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pop_layout:
			Toast.makeText(SelectPicPopupWindowActivity.this, "提示：点击窗口外部关闭窗口！", Toast.LENGTH_SHORT).show();
			break;
		case R.id.pop_btn1:
			getImageFromAlbum();
			break;
		case R.id.pop_btn2:
			try {
				camera(v);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.pop_cancel:
			finish();
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}



	protected void getImageFromAlbum() {  
		Intent intent = new Intent(Intent.ACTION_PICK);  
		intent.setType("image/*");//相片类型  
		startActivityForResult(intent, REQUEST_IMAGE_CODE);  
	}  

	/*
	 * 从相机获取
	 */

	public void camera(View view) throws Exception {
		//takePhoto();
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, 
					new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
		}else {
			takePhoto();
		}


	}

	public void takePhoto(){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 判断存储卡是否可以用，可用进行存储
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			//File dir = this.getExternalFilesDir("/myimage/");
			//公共储存
			filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.IMG_DIR;
			//tempFile = new File(dir, System.currentTimeMillis()+".jpg");
			
			//filepath = tempFile.getPath();
			File imgDir = new File(filepath);
			if (!imgDir.exists()) {
				imgDir.mkdir();
			}
			filepath = filepath + System.currentTimeMillis()+".jpg";
			tempFile = new File(filepath);
			if (!tempFile.exists()) {  
				try {
					tempFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(this, "error="+e.getMessage(), Toast.LENGTH_LONG).show();
				}  
			}  
			// 从文件中创建uri
			Uri uri = Uri.fromFile(tempFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		}
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
		startActivityForResult(intent, REQUEST_CAMERA_CODE);
		//}

	}

	/**
	 * android6.0 以上动态请求权限回调
	 */
	@TargetApi(23)
	@Override
	public void onRequestPermissionsResult(int requestCode,
			String[] permissions, int[] grantResults) {
		// TODO Auto-generated method stub
		if (requestCode == 1) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				try {
					takePhoto();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
			}
			return;
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CODE) {
			if (data != null) {
				//imageView.setImageURI(data.getData());
				ContentResolver cr = SelectPicPopupWindowActivity.this.getContentResolver();  
				Cursor c = cr.query(data.getData(), null, null, null, null);  
				c.moveToFirst();  
				//这是获取的图片保存在sdcard中的位置  
				srcPath = c.getString(c.getColumnIndex("_data"));  
				Intent result = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("srcPath", srcPath);
				result.putExtras(bundle);

				setResult(0, data);
				finish();
			}
		}else if (requestCode == 1) {
			Bitmap bitmap;
			if (data != null) {
				try {
					//bitmap = BitmapFactory.decodeStream(new FileInputStream(tempFile));
					//imageView.setImageBitmap(bitmap);
					//Toast.makeText(SelectPicPopupWindowActivity.this,data.getData().toString(), Toast.LENGTH_SHORT).show();
					Bundle bundle = new Bundle();
					bundle.putString("filepath", filepath);
					data.putExtras(bundle);
					setResult(1, data);
					finish();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				Bundle bundle = new Bundle();
				bundle.putString("filepath", filepath);
				data = new Intent();
				data.putExtras(bundle);
				setResult(1, data);
				finish();
			}


		}
		super.onActivityResult(requestCode, resultCode, data);
	}



}
