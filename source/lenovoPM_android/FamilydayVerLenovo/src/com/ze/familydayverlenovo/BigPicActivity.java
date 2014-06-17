package com.ze.familydayverlenovo;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.umeng.analytics.MobclickAgent;
import com.ze.commontool.LoadImageMgr;
import com.ze.commontool.NetHelper;
import com.ze.commontool.PublicInfo;
import com.ze.commontool.LoadImageMgr.ImageCallBack;
import com.ze.familydayverlenovo.adapter.BlogViewPagerAdapter;
import com.ze.familydayverlenovo.adapter.FamilyListViewAdapter;
import com.ze.familydayverlenovo.adapter.ViewPagerAdapter;
import com.ze.familydayverlenovo.userinfo.UserInfoManager;
import com.ze.model.BlogModel;
import com.ze.model.ModelDataMgr;
import com.ze.model.PhotoModel;
import com.ze.model.PhotoModel.PicInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class BigPicActivity extends Activity {
	private 	Button 					mBtn_back;
	private 	ImageView			mPicLayout;
	private 	int 							mWidth;
	private 	View						mLayoutView;
	private	String 						mPicIdString;
//	private 	int 							arrayIndex;
	private 	int 							imgPos;
	private 	String 						mPicString;
	private 	Button 					mNext;
	private 	Button 					mPrevious;
	private 	String[] 					picUrls;
	private 	ProgressDialog		mProgressDialog;
	private 	boolean 			shuflag;		// 横屏时竖图
	@Override
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onResume(this);
	}
	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_bigpic);
//			shuflag 			= getIntent().getBooleanExtra("shu", false);
//			arrayIndex 			= getIntent().getIntExtra("arrayIndex", 0);
			picUrls 				=	getIntent().getStringArrayExtra("urls");
			imgPos 				= getIntent().getIntExtra("imgPos", 0);
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage(getResources().getString( R.string.loadbigpic) );
			
//			try {
//				picInfos 			= 	
//						( (ArrayList<PicInfo>)	((FamilyDayVerPMApplication)getApplication()).wf_array.getJSONObject(arrayIndex).get("imgarray") );
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
			mBtn_back 			= (Button)findViewById(R.id.bigpic_back);
			mBtn_back.setOnClickListener(ButtonListener);
			mPrevious 			= (Button)findViewById(R.id.bigpic_prev);
			mPrevious.setOnClickListener(ButtonListener);
			mNext 			= (Button)findViewById(R.id.bigpic_next);
			mNext.setOnClickListener(ButtonListener);
			mPicLayout			= (ImageView)findViewById(R.id.bigpic_pic);
			mLayoutView = findViewById(R.id.bigpic_liearlayout);
			
			if( imgPos == 0 )
			{
				mPrevious.setVisibility(View.INVISIBLE);
			}
			if( picUrls!= null && imgPos == picUrls.length-1 )
			{
				mNext.setVisibility(View.INVISIBLE);
			}
			
			initImage();
		}
		private void initImage()
		{
//			if( LoadImageMgr.getInstance().imageCache.containsKey(mPicIdString)){
			if( imgPos < 0 || imgPos >=picUrls.length )
			{
				mPicString = picUrls[0];
			}else
			{
				mPicString =  picUrls[imgPos];
			}
			if( mPicString == null )
			{
				return;
			}
			Drawable pre_drawable  = LoadImageMgr.getInstance().loadDrawble(mPicString, mPicLayout, 
		        			imageCallBack);
			if( pre_drawable == null )
			{
				mProgressDialog.show();
			}else
			{
				initImgSize(pre_drawable);
			}
	
		}
		public void initImgSize( Drawable d )
		{
			if( d != null )
			{
//				matrix.postTranslate((PublicInfo.SCREEN_W/2- pre_drawable.getIntrinsicWidth()/2), ( PublicInfo.SCREEN_H/2 - pre_drawable.getIntrinsicHeight()/2) );
				shuflag = false;
				PublicInfo.SCREEN_W = this.getWindowManager().getDefaultDisplay().getWidth();
				PublicInfo.SCREEN_H	= this.getWindowManager().getDefaultDisplay().getHeight();
				if ( this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE )
				{
					
					if (d.getIntrinsicHeight() >  d.getIntrinsicWidth() ) {
						shuflag = true;
					}
				}
				float height;
				if( shuflag )
				{
					height = PublicInfo.SCREEN_H;
				}else
				{
					height =  (  (float)d.getIntrinsicHeight() * ( (float)PublicInfo.SCREEN_W /  (float)d.getIntrinsicWidth() ));
				}
				float y =  (PublicInfo.SCREEN_H-100)/2 - height/2;
				y = y<0?0:y;
				float scale = 1.0f;
				if(  shuflag )
				{
					scale = ((float)PublicInfo.SCREEN_H / (float)d.getIntrinsicHeight());
				}else
				{
					scale = ((float)PublicInfo.SCREEN_W / (float)d.getIntrinsicWidth());
				}
				matrix.setScale(scale, scale);
				float x = (PublicInfo.SCREEN_W)/2 - (d.getIntrinsicWidth() *  scale)/2;
				x = x<0?0:x;
				matrix.preTranslate(0,0);
				if( shuflag )
				{
					matrix.postTranslate(x,0);
				}else
				{
					matrix.postTranslate(0,y);
				}
				
				mPicLayout.setImageDrawable(d);
				mPicLayout.setImageMatrix(matrix);
			}
			mPicLayout.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					
					switch (event.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_DOWN:
							savedMatrix.set(matrix); //把原始  Matrix对象保存起来
							start.set(event.getX(), event.getY());  //设置x,y坐标
							mode = DRAG;
							break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
							mode = NONE;
							break;
				case MotionEvent.ACTION_POINTER_DOWN:
							oldDist = spacing(event);
							if (oldDist > 10f) {
								savedMatrix.set(matrix);
								midPoint(mid, event);  //求出手指两点的中点
								mode = ZOOM;
							}
							break;
				case MotionEvent.ACTION_MOVE:
						if (mode == DRAG) {
							matrix.set(savedMatrix);
							matrix.postTranslate(event.getX() - start.x, event.getY()
									- start.y);
						} else if (mode == ZOOM) {
								float newDist = spacing(event);
								if (newDist > 10f) {
									matrix.set(savedMatrix);
									float scale = newDist / oldDist;
									matrix.postScale(scale, scale, mid.x, mid.y);
								}
							}
						break;
				}


				mPicLayout.setImageMatrix(matrix);
				return true;
				}
			});
		}
		public ImageCallBack imageCallBack = new ImageCallBack() {
			
			@Override
			public void setImage(Drawable d, String url, ImageView view) {
				// TODO Auto-generated method stub
				if( /*mActiveImages.contains(view) &&*/ url.equals((String)view.getTag()))
				{
					view.setImageDrawable(d);
					mProgressDialog.dismiss();
					initImgSize(d);
				}
			}
		};
		
		private Matrix savedMatrix = new Matrix();
		private Matrix matrix = new Matrix();
		private static final int NONE = 0;
		private static final int DRAG = 1;
		private static final int ZOOM = 2;
		
		private int mode = NONE;
		private float oldDist;
		
		private PointF start = new PointF();
		private PointF mid = new PointF();
		
		//求两点距离
		private float spacing(MotionEvent event) {
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return FloatMath.sqrt(x * x + y * y);
		}
		//求两点间中点
		private void midPoint(PointF point, MotionEvent event) {
			float x = event.getX(0) + event.getX(1);
			float y = event.getY(0) + event.getY(1);
			point.set(x / 2, y / 2);
		};
		private OnClickListener ButtonListener = new OnClickListener()
		{
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (v == mBtn_back) {
						BigPicActivity.this.finish();
					}else if( v == mPrevious )
					{
						Intent intent = new Intent();
						intent.putExtra("urls", picUrls);
						intent.putExtra("imgPos", imgPos-1);
						intent.setClass(BigPicActivity.this, BigPicActivity.class);
						startActivity(intent);
						BigPicActivity.this.finish();
					}else if( v == mNext )
					{
						Intent intent = new Intent();
						intent.putExtra("urls", picUrls);
						intent.putExtra("imgPos", imgPos+1);
						intent.setClass(BigPicActivity.this, BigPicActivity.class);
						startActivity(intent);
						BigPicActivity.this.finish();
					}
		
			}
		};
}
