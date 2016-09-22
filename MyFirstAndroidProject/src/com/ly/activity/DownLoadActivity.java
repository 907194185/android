package com.ly.activity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.BreakIterator;

import android.R.integer;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ly.R;
import com.ly.utils.DownloadUtil;

public class DownLoadActivity extends Activity implements OnClickListener{

	private TextView textView, textView2;
	private ProgressBar progressBar;
	private Button download, download2, download3;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				imageView.setImageBitmap(bitmap);
			}else if(msg.what == 2){
				textView.setText(msg.obj.toString());
			}else if(msg.what == 0){
				Log.i("ww", "666666666666");
				Loading = true;
				progressBar.setVisibility(View.VISIBLE);
				progressBar.setMax(fileSize);
			}else if(msg.what == 11){
				Log.i("ww", "7777777==="+downloadFileSize+"====="+fileSize);
				progressBar.setProgress(downloadFileSize);
				int result = (downloadFileSize*100)/fileSize;
				textView2.setText(result + "%");
				
			}else if(msg.what == 22){
				//progressBar.setVisibility(View.GONE);
				//textView2.setVisibility(View.INVISIBLE);
				/*FileInputStream fis;
				try {
					fis = new FileInputStream(path + filename);
					imageView.setImageBitmap(BitmapFactory.decodeStream(fis));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				Loading = false;
				Toast.makeText(DownLoadActivity.this, "下载完成！", Toast.LENGTH_SHORT).show();
			}
		}
	};;
	DownloadUtil downloadUtil;
	private Bitmap bitmap;
	private ImageView imageView;

	private int fileSize;
	private String path = Environment.getExternalStorageDirectory().getPath() + "/";
	private String filename;
	private int downloadFileSize = 0;
	private boolean Loading = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_activity);
		initUI();

	}

	public void initUI(){
		downloadUtil = new DownloadUtil();

		imageView = (ImageView) this.findViewById(R.id.download_img);
		textView = (TextView) this.findViewById(R.id.download_tv);
		textView2 = (TextView) this.findViewById(R.id.download_pb_tv);
		progressBar = (ProgressBar) this.findViewById(R.id.download_pb);
		download = (Button) this.findViewById(R.id.download_btn);
		download2 = (Button) this.findViewById(R.id.download_btn2);
		download3 = (Button) this.findViewById(R.id.download_btn3);

		download.setOnClickListener(this);
		download2.setOnClickListener(this);
		download3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.download_btn:
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//这里下载数据
					downloadUtil.downloadAsString("http://192.168.1.77:81/fsxf/file/a.txt",handler);
				}
			}).start();
			break;
		case R.id.download_btn2:
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//这里下载数据
					try{
						URL  url = new URL("https://upload.wikimedia.org/wikipedia/commons/thumb/e/ea/Hukou_Waterfall.jpg/800px-Hukou_Waterfall.jpg");
						HttpURLConnection conn  = (HttpURLConnection)url.openConnection();
						conn.setDoInput(true);
						conn.connect(); 
						InputStream inputStream=conn.getInputStream();
						bitmap = BitmapFactory.decodeStream(inputStream); 
						Message msg=new Message();
						msg.what=1;
						handler.sendMessage(msg);

					} catch (MalformedURLException e1) { 
						e1.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
				}
			}).start();
			break;
		case R.id.download_btn3:
			if (!Loading) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//这里下载数据
						try{
							String a = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/ea/Hukou_Waterfall.jpg/800px-Hukou_Waterfall.jpg";
							filename = a.substring(a.lastIndexOf("/") + 1);
							URL  url = new URL(a);
							HttpURLConnection conn  = (HttpURLConnection)url.openConnection();
							conn.connect(); 
							InputStream inputStream=conn.getInputStream();
							fileSize = conn.getContentLength();
							Log.i("ww", "111111111111111==="+fileSize);
							if (fileSize <= 0) {
								Log.i("ww", "22222222");
								throw new RuntimeException("无法获取文件大小！");
							}
							Log.i("ww", "3333333");
							if (inputStream == null) {
								Log.i("ww", "444444444444");
								throw new RuntimeException("in stream is null");
							}
							FileOutputStream fos = new FileOutputStream(path + filename);
							byte[] data = new byte[1024];
							downloadFileSize = 0;
							int len = 0;
							sendMsg(0);
							Log.i("ww", "5555555555555");
							while((len = inputStream.read(data)) != -1){
								fos.write(data, 0, len);
								downloadFileSize += len;
								sendMsg(11);
							}
							sendMsg(22);

							inputStream.close();

						} catch (MalformedURLException e1) { 
							e1.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}  
					}
				}).start();
			}

			break;
		default:
			break;
		}
	}

	public void sendMsg(int flag){
		Message message = new Message();
		message.what = flag;
		handler.sendMessage(message);
	}

}
