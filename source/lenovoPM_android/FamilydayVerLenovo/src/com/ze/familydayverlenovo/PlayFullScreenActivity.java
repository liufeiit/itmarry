package com.ze.familydayverlenovo;

import java.io.File;
import java.io.FileInputStream;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.Toast;

public class PlayFullScreenActivity extends Activity {

	public final static String TAG = "PlayFullScreenActivity";
	public final static int [] defaultPic = new int[]{R.drawable.default_1, 
		R.drawable.default_2, R.drawable.default_3, R.drawable.default_4};
	ImageView img;
	AlphaAnimation indexAnim;
	AlphaAnimation dismissAnim;
	int pos = WidgetDataMrg.posAllLocalPicList - 1;
	Bitmap lastBm = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_full_screen);
		if (RefreshService.mAllLocalPicList.size() == 0){
			Toast.makeText(this, "没有本地图片！", Toast.LENGTH_SHORT).show();
			return;
		}
		flag_finish = false;
		img = (ImageView) findViewById(R.id.pfs_img);
		Bitmap bm = getPicDetail();
		if (bm != null) {
			img.setImageBitmap(bm);
			if (lastBm != null && !lastBm.isRecycled())
				lastBm.recycle();
			lastBm = bm;
		} else {
			Toast.makeText(PlayFullScreenActivity.this, "没有图片浏览！", Toast.LENGTH_SHORT).show();
			return;
		}
		//pos = WidgetDataMrg.posAllLocalPicList;
		//Log.v(TAG, "初始化的指针：" + pos);
		indexAnim = new AlphaAnimation(0.1f, 1.0f);
		indexAnim.setDuration(2000);
		
		dismissAnim = new AlphaAnimation(1.0f, 0.1f);
		dismissAnim.setDuration(2000);
		dismissAnim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				img.setVisibility(View.INVISIBLE);
				Bitmap bm = getPicDetail();
				if (bm != null) {
					img.setImageBitmap(bm);
					if (lastBm != null && !lastBm.isRecycled())
						lastBm.recycle();
					lastBm = bm;
				} else {
					Toast.makeText(PlayFullScreenActivity.this, "没有图片浏览！", Toast.LENGTH_SHORT).show();
					return;
				}
				//img.setImageResource(defaultPic[pos++ % 4]);
				img.startAnimation(indexAnim);
				img.setVisibility(View.VISIBLE);
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						img.startAnimation(dismissAnim);
					}
				}, 3500);
			}
		});
		img.startAnimation(indexAnim);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				img.startAnimation(dismissAnim);
			}
		}, 3500);
		
	}
	
	boolean flag_finish = false;
	private Bitmap getPicDetail() {
		if (pos >= RefreshService.mAllLocalPicList.size() || pos < 0){
			pos = 0;
			flag_finish = true;
		}
		Log.v(TAG, pos + "");
		String path = RefreshService.mAllLocalPicList.get(pos ++);
		Log.v(TAG, "图片路径" + path);
		String [] fileArray = path.split("/");
		if (5 < fileArray.length && fileArray[4].equals("familydayVerPm") && 
				fileArray[5].equals("pic_cache")){
			Log.v(TAG, "读到是缓存文件夹pic_cache的内容,放弃!");
			//return null;
			if (!flag_finish){
				return getPicDetail();
			} else{
				return null;
			}
		}
		File file = new File(path);
		if (file.exists()) {
			//Log.v(TAG, "");
			//图片太小的话，就不进行读取
			/*if (file.length() < 6000){
				if (pos < RefreshService.mAllLocalPicList.size())
					return null;
			}*/
				
			int size = (int)file.length()/(1024*1024);
			BitmapFactory.Options options = new BitmapFactory.Options(); 
			if (size == 2) {
				size = 3;
			}
			if ((size == 0||size == 1) && file.length() > 512*1024) {
				size = 2;
			}
			//options.inSampleSize = size!=0?size:1;
			options.inSampleSize = size;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			try {
				Bitmap bm = BitmapFactory.decodeStream(new FileInputStream(file), 
						null, options);
				if (bm != null){
					Log.v(TAG, "全屏浏览本地第" + pos + "张图片成功");
					return bm;
				} else {
					if (!flag_finish){
						return getPicDetail();
					} else{
						return null;
					}
				}
					
			} catch (Exception e) { 
				e.printStackTrace();
			}
			
		}
		Log.v(TAG, "全屏浏览本地第" + pos + "张图片失败");
		return null;
		
	}
	
}

	
