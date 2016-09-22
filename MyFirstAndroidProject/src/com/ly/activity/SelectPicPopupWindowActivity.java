package com.ly.activity;

import java.io.File;
import java.io.IOException;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ly.R;

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
			Toast.makeText(SelectPicPopupWindowActivity.this, "��ʾ����������ⲿ�رմ��ڣ�", Toast.LENGTH_SHORT).show();
			break;
		case R.id.pop_btn1:
			getImageFromAlbum();
			break;
		case R.id.pop_btn2:
			try {
				camera(v);
			} catch (IOException e) {
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
		intent.setType("image/*");//��Ƭ����  
		startActivityForResult(intent, REQUEST_IMAGE_CODE);  
	}  

	/*
	 * �������ȡ
	 */
	public void camera(View view) throws IOException {
		// �������
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// �жϴ洢���Ƿ�����ã����ý��д洢
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			filepath = Environment.getExternalStorageDirectory() + "/myimage/" + System.currentTimeMillis()+".jpg";
			tempFile = new File(filepath);  
            if (!tempFile.exists()) {  
            	tempFile.createNewFile();  
            }  
			Log.i("WeChat","====ddd==222222222");
			// ���ļ��д���uri
			Uri uri = Uri.fromFile(tempFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		}
		Log.i("WeChat","====ddd==33333333333");
		// ����һ�����з���ֵ��Activity��������ΪPHOTO_REQUEST_CAREMA
		startActivityForResult(intent, REQUEST_CAMERA_CODE);
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("WeChat",requestCode + "=====" + resultCode);
		if (requestCode == REQUEST_IMAGE_CODE) {
			Log.i("WeChat","=====11111111111" );
			if (data != null) {
				Log.i("WeChat","=====22222222222" );
				//imageView.setImageURI(data.getData());
				ContentResolver cr = SelectPicPopupWindowActivity.this.getContentResolver();  
				Cursor c = cr.query(data.getData(), null, null, null, null);  
				c.moveToFirst();  
				//���ǻ�ȡ��ͼƬ������sdcard�е�λ��  
				srcPath = c.getString(c.getColumnIndex("_data"));  
				System.out.println(srcPath+"----------����·��2"); 
				Intent result = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("srcPath", srcPath);
				result.putExtras(bundle);
				
				setResult(0, data);
				Log.i("WeChat","=====22222222222"+ srcPath );
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
				Toast.makeText(SelectPicPopupWindowActivity.this,"data is null", Toast.LENGTH_LONG).show();
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
