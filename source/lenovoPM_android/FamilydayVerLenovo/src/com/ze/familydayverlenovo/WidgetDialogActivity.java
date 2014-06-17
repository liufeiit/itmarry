package com.ze.familydayverlenovo;

import java.util.ArrayList;
import java.util.List;

import com.ze.familydayverlenovo.userinfo.Componet;
import com.ze.familydayverlenovo.userinfo.UserInfoManager;


import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class WidgetDialogActivity extends Activity {

	public final static String TAG = "WidgetDialogActivity" ;
	public final static int LOCAL = 0;
	public final static int FAMILY = 1;
	Activity act;
	TextView notice;
	TextView localTv;
	TextView familyTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_widget_dialog);
		act = this;
		notice = (TextView) findViewById(R.id.wd_notice);
		localTv = (TextView) findViewById(R.id.wd_local);
		localTv.setOnClickListener(onclicklisten);
		familyTv = (TextView) findViewById(R.id.wd_family);
		familyTv.setOnClickListener(onclicklisten);
		if (WidgetDataMrg.wd_type == FAMILY) {
			notice.setText("你要做什么？");
			localTv.setText("切换本地播放");
			familyTv.setText("进入我家吧");
		} else if ( WidgetDataMrg.wd_type == LOCAL) {
			notice.setText("你要做什么？");
			localTv.setText("进入我家吧");
			familyTv.setText("进入全屏浏览");
		}
		
	}

	boolean clickFlag = false;
	private OnClickListener onclicklisten = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if ( v == localTv){
				 if (WidgetDataMrg.wd_type == LOCAL) {
					WidgetDataMrg.wd_type = FAMILY;
					WidgetDataMrg.playAllLocal = false;
					WidgetDataMrg.flagLoadingWidget = true;
					
					//切换到Family播放模式
					Intent intent = new Intent();
					intent.setAction("com.qlf.showDt");
					sendBroadcast(intent);
					WidgetDataMrg.flag_send ++;
					WidgetDialogActivity.this.finish();
					return;
				} else if (WidgetDataMrg.wd_type == FAMILY) {
					WidgetDataMrg.wd_type = LOCAL;
					WidgetDataMrg.flagLoadingWidget = true;
					//切换到本地播放模式
					WidgetDataMrg.playAllLocal = true;
					
					playLocalAlbums();
					Intent intent = new Intent();
					intent.setAction("com.qlf.showDt");
					sendBroadcast(intent);
					WidgetDataMrg.flag_send ++;
					WidgetDialogActivity.this.finish();
					return;
				}
				
		    	
			} else if (v == familyTv){
				 if (WidgetDataMrg.wd_type == 0) {
					//全屏浏览
					/*Intent intent = new Intent(Intent.ACTION_PICK,
                			android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                	startActivityForResult (intent,1);*/
					Intent intent = new Intent(WidgetDialogActivity.this, PlayFullScreenActivity.class);
					startActivity(intent);
					WidgetDialogActivity.this.finish();
				} else if (WidgetDataMrg.wd_type == 1) {
					//进入family
					/*Intent wdIntent = new Intent(WidgetDialogActivity.this, WidgetDetailActivity.class);
					startActivity(wdIntent);*/
					UserInfoManager.getInstance(WidgetDialogActivity.this).saveAll();
					Componet authC = (UserInfoManager.getInstance(WidgetDialogActivity.this)
							.getItem("m_auth"));
					if (authC == null || WidgetDataMrg.serviceType != WidgetDataMrg.WIFI_SERVICE){
						Intent loginIntent = new Intent();
						loginIntent.setClass(WidgetDialogActivity.this, LoginActivity.class);
						startActivity(loginIntent);
						WidgetDialogActivity.this.finish();
						return;
					}
					// 参考FlowView.java 108行
					Intent mIntent = new Intent(WidgetDialogActivity.this, PhotoShowActivity.class);
//		        	mIntent.putExtra("frompublish", true);
//	        	    mIntent.putExtra("id", WidgetDataMrg.widgetPic.id + "");
//	        	    mIntent.putExtra("uid", WidgetDataMrg.widgetPic.uid + "");
	        	    mIntent.putExtra("type", "photoid");
	        	    // 添加标志第几张
	        	    int pos = 0;
	        	    if ( RefreshService.posPic > 1)
	        	    	pos = RefreshService.posPic - 2;
	        	    else{
	        	    	if (RefreshService.posPic - 1 >= 0)
	        	    		pos = RefreshService.posPic - 1;
	        	    }
	        	    mIntent.putExtra("pos", pos);
	        	    mIntent.putExtra("waterfall", true);
	        	 // 添加一个标志代表从widget进入  
	        	    mIntent.putExtra("fromwidget", true);
	                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                startActivity(mIntent);
					WidgetDialogActivity.this.finish();
					return;
				}
			
			}
		}
	};
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 获取SDcard图片的路径path
	 */
	public static  ArrayList<String> getImagesPathList(Activity activity) {
	 
		ArrayList<String> filePaths  = new ArrayList<String>();
		
		String path = "" + Environment.getExternalStorageDirectory().getAbsolutePath()
				 + "/DCIM/";
		Log.v(TAG, "查询路径：" + path);
		//Uri uri = Uri.fromFile(new File(path)) ;
		//Uri uri = Uri.parse("file://" + path) ;


		// 读取SD卡中所有图片
		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		String[] projection = { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DISPLAY_NAME,
				MediaStore.Images.Media.DATA, 
				MediaStore.Images.Media.SIZE };
		
		String selection = MediaStore.Images.Media.MIME_TYPE + "=?";
		String[] selectionArg = { "image/jpeg" };
		
		Cursor mCursor = activity.managedQuery(uri, projection, selection,
				selectionArg, MediaStore.Images.Media.DATE_MODIFIED + " desc");
		
		filePaths.clear();
		if (mCursor != null) {
			try {
				mCursor.moveToFirst();
				while (mCursor.getPosition() != mCursor.getCount() && !mCursor.isClosed()) {
					filePaths.add(mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA)));
					
					mCursor.moveToNext();
				}
				//4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)  
				if(Integer.parseInt(Build.VERSION.SDK) < 14){  
					mCursor.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return filePaths;
	}
	
	/**
	 * 播放本地相册
	 */
	private void playLocalAlbums(){
		//List<String> mList = getImagesPathList(act);
		RefreshService.mAllLocalPicList = getImagesPathList(act);
		WidgetDataMrg.playAllLocal = true;
		//启动服务
		SharedPreferences preferences = getSharedPreferences("user.config", 
    			MODE_WORLD_READABLE);
    	WidgetDataMrg.M_AUTH = preferences.getString("m_auth", null);
    	WidgetDataMrg.FLAG_WEDGET = true;
    	WidgetDataMrg.FLAG_SERVICE = true;//启动服务
    	//启动服务前，先判断网络状态
    	HttpGetAllCnnt.setServiceType(WidgetDialogActivity.this);
    	Intent startIntent = new Intent(WidgetDialogActivity.this, RefreshService.class);
    	startService(startIntent);
	}
	
}
