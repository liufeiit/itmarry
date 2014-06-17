package com.ze.familydayverlenovo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mouse.widget.WidgetPicInfo;
import com.ze.commontool.NetHelper;
import com.ze.commontool.StringTools;
import com.ze.familydayverlenovo.userinfo.UserInfoManager;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.ze.model.DataModel;
import com.ze.model.PhotoModel;
import com.ze.model.PhotoModel.PicInfo;

public class RefreshService extends Service {

	private WidgetLoaderBitmapMrg WidgetLoader;
	public static final String TAG = "RefreshService";
	private boolean isRun = false;
	public static String packageName;
	public static boolean flag_regi = false;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	int position = 0;
	RemoteViews rViews;
	ComponentName cName;
	AppWidgetManager manager;
	int [] pic = new int[]{R.drawable.default_1, R.drawable.default_2, 
			R.drawable.default_3, R.drawable.default_4};
	String [] picStr = new String[]{"爸爸妈妈，我们永远在一起", "亲爱的宝贝，爷爷奶奶想你了", 
			"我家的幸福时光", "外面的世界真精彩"};
	@Override
	public void onStart(Intent intent, int startId) {
		Log.v(TAG, "调用onStart(),开启服务");
		super.onStart(intent, startId);
		//横竖屏切换监听
		registerReceiver(screenOrientationReceiver,new IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED));
		flag_regi = true;
		if (!WidgetDataMrg.FLAG_WEDGET){//判断桌面是否存在widget，不存在则终止服务
			onDestroy();
			Log.v(TAG, "桌面widget未启动，关闭服务");
			return;
		}
		//设置一些基本值
		showBm = null;
		Flag_firstPic = true;
		layoutid = 1;
		pagefinish = true;
		
		/*if (!WidgetDataMrg.FLAG_SERVICE)
			return;*/
		isRun = true;//服务启动
		packageName = this.getPackageName();
		rViews = new RemoteViews(this.getPackageName(), 
				R.layout.layout_widget);
		cName = new ComponentName(this, AppWidget.class);
		manager = AppWidgetManager.getInstance(this);
		mList = new ArrayList<Bitmap>();//
		mInfo = new ArrayList<WidgetPicInfo>();//初始化
		array = new JSONArray();
		posPic = 0;
		WidgetLoader = WidgetLoaderBitmapMrg.getInstance();
		
		//判断是否为本地全部播放
		if (WidgetDataMrg.playAllLocal) {
			Log.v(TAG, "启动本地图片播放");
			if (WidgetDataMrg.flagLoadingWidget){
				rViews.setViewVisibility(R.id.widget_bar, View.VISIBLE);
				rViews.setViewVisibility(R.id.widget_textBar, View.VISIBLE);
				manager.updateAppWidget(cName, rViews);
			}
			playAllLocalPic();
			return;
		}
		if (WidgetDataMrg.serviceType == WidgetDataMrg.WIFI_SERVICE){
			Log.v(TAG, "启动wifi服务");
			if (WidgetDataMrg.flagLoadingWidget){
				rViews.setViewVisibility(R.id.widget_bar, View.VISIBLE);
				rViews.setViewVisibility(R.id.widget_textBar, View.VISIBLE);
				manager.updateAppWidget(cName, rViews);
			}
			posPic = 0;
			if (WidgetDataMrg.POS_PAGE >= 2)
				WidgetDataMrg.POS_PAGE = 1;
			//playFamilyPic();
			//playFamilyDefalutTask();
			if (((FamilyDayVerPMApplication)getApplication()).widget_array == null)
				((FamilyDayVerPMApplication)getApplication()).widget_array = new JSONArray();
			if (((FamilyDayVerPMApplication)getApplication()).widget_arrayList == null)
				((FamilyDayVerPMApplication)getApplication()).widget_arrayList = new ArrayList<DataModel>();
			new LoadPiclistTask().execute("");
			return;
		} else{
			Log.v(TAG, "启动离线服务");
			//showLocalPicList();
			playFamilyDefalutTask();
		}
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg){
			switch(msg.what){
			case 1:
				if (rViews != null && manager != null){
					rViews.setViewVisibility(R.id.widget_bar, View.GONE);
					rViews.setViewVisibility(R.id.widget_textBar, View.GONE);
					manager.updateAppWidget(cName, rViews);
				}
				WidgetDataMrg.flagLoadingWidget = false;
				break;
			case 2:
				break;
			default:
				break;
			}
		}
	};
	
	
	
	@Override
	public void onDestroy() {
		Log.v(TAG, "onDestroy");
		//myDelay(1000);
		//WidgetDataMrg.FLAG_SERVICE = false;
		if (flag_regi){
			unregisterReceiver(screenOrientationReceiver);
			flag_regi = false;
		}
		isRun = false;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mList == null)
					return;
				for (int i = 0; i < mList.size(); i ++){
					myDelay(500);
					if (mList.get(i) != null && mList.get(i).isRecycled()){
						mList.get(i).recycle();
						mList.get(i);
					}
				}
				System.gc();
			}
		}).start();
		
	}
	
	class LoadPicTask extends AsyncTask<Object, Object, Object>{

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		protected void onPostExecute(Object arg0){
			
		}
	}
	
	boolean error_LoadPiclist = false;
	boolean flag_noPic = false;
	boolean flag_stopLoadList = false;
	JSONArray array;
	/**
	 * 判断用户是否存有用户信息
	 */
	boolean flag_userinfo = true;
	/**
	 * 下载图片列表	
	 * @author mouse
	 *
	 */
	class LoadPiclistTask extends AsyncTask<Object, Object, JSONObject>{
		String resultStr;
		List<Map<String, Object>> tempList;
		
		@Override
		protected JSONObject doInBackground(Object... params) {
			// TODO Auto-generated method stub
			if (WidgetDataMrg.M_AUTH == null){
				flag_userinfo = false;
				return null;
			}
			
			//为widget的跳转到客户端播放作准备
			resultStr = NetHelper.getResponByHttpClient( getString(R.string.http_picfalldata),
								 WidgetDataMrg.M_AUTH,1 + "" );
			((FamilyDayVerPMApplication)getApplication()).widget_array = new JSONArray();
			((FamilyDayVerPMApplication)getApplication()).widget_arrayList = new ArrayList<DataModel>();
			
			if(resultStr != null && !"".equals(resultStr.trim())){
				try {
					JSONObject resultData = new JSONObject(resultStr);
					int error = resultData.getInt("error");
					if(error == 0){
						tempList = new ArrayList<Map<String, Object>>();
						JSONArray contentArr = resultData.getJSONArray("data");
						int num = contentArr.length();
						JSONObject temp;
						Map<String, Object> item;
						DataModel model ;
						JSONObject object ;
						ArrayList<PicInfo> picInfoArrayList;
						PicInfo picInfo;
						int picnum = 0;
						for(int i=0; i<num; i++){
							temp = contentArr.getJSONObject(i);
							item = new HashMap<String, Object>();
							model = new DataModel();
							object = new JSONObject();
							model.type = "photoid";
							item.put("picid", temp.getString("id"));
							model.id = temp.getString("id");
							item.put("image_1", StringTools.parseUrltoFallView( temp.getString("image_1") ));
							item.put("uid", temp.getString("uid"));
							model.uid = temp.getString("uid");
							item.put("subject", temp.getString("subject").equals("") ? "无标题" : temp.getString("subject") );
							item.put("name", temp.getString("name"));
							tempList.add(item);
							((FamilyDayVerPMApplication)getApplication()).widget_arrayList.add(model);
							picInfoArrayList = new ArrayList<PhotoModel.PicInfo>();
							picnum = Integer.parseInt(temp.getString("picnum"));
							//  接口有问题 暂时指支持接收前3张
							if( picnum > 3 )
							{
								picnum = 3;
							}
							for (int j = 0; j < picnum; j++) {
								picInfo = new PicInfo();
								picInfo.url = temp.getString("image_" + (j+1) );
								picInfo.height = Integer.parseInt( temp.getString("image_" + (j+1) + "_height") );
								picInfo.width = Integer.parseInt( temp.getString("image_" + (j+1) + "_width") );
								picInfoArrayList.add(picInfo);
							}
							object.put("id", temp.getString("id") );
							object.put("type", "photoid");
							object.put("love",  temp.getInt("mylove"));
							object.put("uid", temp.getString("uid"));
							object.put("imgarray",picInfoArrayList );
							object.put("say",  temp.getString("message") );
							object.put("listview", null);
							object.put("time",  temp.getString("dateline") );
							object.put("name",  temp.getString("name"));
							
							((FamilyDayVerPMApplication)getApplication()).widget_array.put(object);
						}
					}
					
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
			
			JSONObject obj = HttpGetAllCnnt.getPhotoID(15, 1/*WidgetDataMrg.POS_PAGE ++*/);
			if (obj == null)
			{
				Log.v("Test", "服务器请求发生错误！posPage：" + (WidgetDataMrg.POS_PAGE - 1));
				error_LoadPiclist = true; 
				return null;
			}
			return obj;
			
		}
		
		protected void onPostExecute(JSONObject obj){
			super.onPostExecute(obj);
			//如果不存在用户信息
			if (!flag_userinfo){
				//showLocalPicList();
				if (barFlag){
					barFlag = false;
					Message msg = handler.obtainMessage(1);
					handler.sendMessage(msg);
				}
				playFamilyDefalutTask();
				return;
			}
			//如果是网络错误或者服务器请求错误！
			if (error_LoadPiclist){
				//showLocalPicList();
				if (barFlag){
					barFlag = false;
					Message msg = handler.obtainMessage(1);
					handler.sendMessage(msg);
				}
				playFamilyDefalutTask();
				return;
			}
			try{
				if (!obj.isNull("data"))
					array = obj.getJSONArray("data");
				if (array.length() == 0){//用户还没有一张图片，此时播放默认图片
					//showLocalPicList();
					if (barFlag){
						barFlag = false;
						Message msg = handler.obtainMessage(1);
						handler.sendMessage(msg);
					}
					playFamilyDefalutTask();
					return;
				}
				Log.v("RefreshService", "加载最新列表成功" );
				loadPicDetailTask(array.getJSONObject(posPic ++));
				return;
			}catch(JSONException e){
				Log.v("RefreshService", "array获取数据失败");
				//showLocalPicList();
				playFamilyDefalutTask();
				e.printStackTrace();
			}
			
		}
    }
	
	public static int posPic = 0;
//	public int getPicPos()
//	{
//		return posPic;
//	}
	boolean flag_stopLoading = false;
	Bitmap currentBm;
	List<Bitmap> mList = null;
	List<WidgetPicInfo> mInfo = null;
	boolean isPush = false;
	boolean LEFT = true;
	
	String title = "";
	String name = "";
	String time = "";
	
	/**
	 * 图片列表是否加载完成的标志
	 */
	boolean LPDTaskFinish = false;
	boolean preLoadingFlag = false;
	/**
	 * 下载图片详情
	 * @param arg0
	 */
	public void loadPicDetailTask (final JSONObject arg0)
    {
		/*if (!WidgetDataMrg.FLAG_SERVICE)//如果服务关闭了，就提前终止进程
			return;*/
		if (!isRun)
			return;
		
    	new AsyncTask<Object, Object, String>(){

			@Override
			protected String doInBackground(Object... params) {
				// TODO Auto-generated method stub
				/*if (!WidgetDataMrg.FLAG_SERVICE){
		    		return null;
		    	}*/
				//Log.v(TAG, "正在加载第" + ((WidgetDataMrg.POS_PAGE-2)*10+posPic) + "张图片");
				Log.v(TAG, "正在加载第" + posPic + "张图片");
				if (!isRun)
					return null;
				//先事先加载一张图片详情
				if (!preLoadingFlag) {
					Log.v(TAG, "预加载图片！");
					preLoadingFlag = true;
					loadBm = loadBitmapFromimgUrl(arg0);
					showBm = loadBm;
					return "preImage";
				}
				if (!pagefinish){
					myDelay(4000);
					loadBm = loadBitmapFromimgUrl(arg0);
				} else {
					myDelay(2500);
					showBm = loadBm;
				}
				return null;
				
			}
			
			@Override
			protected void onPostExecute(String arg0){
				/*if (!WidgetDataMrg.FLAG_SERVICE){
		    		return;
		    	}*/
				if (!isRun)
					return;
				if (showBm != null && barFlag){
					barFlag = false;
					Message msg = handler.obtainMessage(1);
					handler.sendMessage(msg);
				}
				showPicType(1);
				try{	
					if (posPic < array.length()) {
						if (!pagefinish)
							loadPicDetailTask(array.getJSONObject(posPic ++));
						else
							loadPicDetailTask(array.getJSONObject(posPic));
					}
					else{
						LPDTaskFinish = true;
						Intent intent = new Intent();
						intent.setAction("com.qlf.showDt");
						sendBroadcast(intent);
						WidgetDataMrg.flag_send ++;
						return;
						
					}
				
					//Thread.sleep(1500);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
    		
    	}.execute("");
    }
	
	public Bitmap loadBitmapFromimgUrl(JSONObject arg0){
		Bitmap bm = null;
		JSONObject obj = null;
		try{
			Thread.sleep(1000);
			//如果收到推送
			if (WidgetDataMrg.FLAG_PUSH){
				WidgetDataMrg.FLAG_PUSH = false;
				isPush = true;
				obj = LoadPicDetail(WidgetDataMrg.pushJSON);
				if (posPic > 0)
					posPic --;
			}else{
				obj = LoadPicDetail(arg0);
			}
			//存入id 和 uid 方便定位
			if (!obj.isNull("id"))
				WidgetDataMrg.widgetPic.id = obj.getInt("id");
			if (!obj.isNull("uid"))
				WidgetDataMrg.widgetPic.uid = obj.getInt("uid");
			String picUrl = null;
			if (!obj.isNull("picUrl"))
				picUrl = obj.getString("picUrl");
			if (picUrl != null){
				//判断是否缓存过
				bm = WidgetLoader.LoadBitmap(picUrl);
				if (bm != null){
					//延迟2秒
					myDelay(2000);
					mList.add(bm);
					WidgetPicInfo info = new WidgetPicInfo(title, name, time, bm);
					mInfo.add(info);
					return bm;
				}
				//如果没有缓存,则下载图片
				URL url = new URL(picUrl);
                InputStream in=url.openStream();
                BitmapFactory.Options opt = new BitmapFactory.Options(); 
                opt.inPreferredConfig = Bitmap.Config.RGB_565; 
                opt.inPurgeable = true; 
                opt.inInputShareable = true; 
                opt.inSampleSize = computeSampleSize(opt, -1, 128*128);  //计算出图片使用的inSampleSize
                opt.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeStream(in, null, opt);
                WidgetLoader.putBitmapToCache(picUrl, bm);
                if (bm != null){
                	mList.add(bm);
                	WidgetPicInfo info = new WidgetPicInfo(title, name, picUrl, bm);
					mInfo.add(info);
                	return bm;
                }
                //return bm;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		Bitmap errorBm = BitmapFactory.decodeStream(
				getResources().openRawResource(pic[posPic % 4]));
		mList.add(errorBm);
		myDelay(2000);
		return errorBm;
	}
	
	
	int posInFamilyPic = 0;
	/**
	 * 播放family图片
	 */
	private void playFamilyNetPic(){
		if (!isRun)
			return;
		int i = 0;
		BitmapFactory.Options options = new BitmapFactory.Options(); 
		options.inSampleSize = 3;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		if (mInfo.size() == 0){
			for (i = 0; i < 10; i ++){
			//Bitmap bm = BitmapFactory.decodeStream(getResources().openRawResource(pic[i % 4]));
			Bitmap bm = BitmapFactory.decodeResource(getResources(), pic[i % 4], options);
			mList.add(bm);
			WidgetPicInfo info = new WidgetPicInfo(picStr[i % 4], "Family", "", bm);
			mInfo.add(info);
			}
		}
		//playFamilyTask();
	}
	
	public void showDefaluType(){
		
		if (layoutid == 1){
			RemoteViews subViews = new RemoteViews(packageName, R.layout.relative_in);
			rViews.removeAllViews(R.id.relative_parent);
			rViews.addView(R.id.relative_parent, subViews);
			
			rViews.setViewVisibility(R.id.showPicLeft, View.INVISIBLE);
			//rViews.setViewVisibility(R.id.showPicRight, View.INVISIBLE);
			rViews.setViewVisibility(R.id.showPic, View.VISIBLE);
			rViews.setViewVisibility(R.id.info, View.VISIBLE);
			rViews.setViewVisibility(R.id.infoRight, View.INVISIBLE);
			//rViews.setViewVisibility(R.id.infoLeft, View.INVISIBLE);
			rViews.setImageViewResource(R.id.showPic, pic[posPic % 4]);
			//rViews.setViewVisibility(R.id.widget_icon, View.VISIBLE);
			rViews.setTextViewText(R.id.showTitle, picStr[(posPic) % 4]);
			rViews.setTextViewText(R.id.showName, "Family");
			rViews.setTextViewText(R.id.showTime, "");
			
			pagefinish = false;
			layoutid = 2;
		} else {
			RemoteViews subViews = new RemoteViews(packageName, R.layout.relative_out);
			rViews.removeAllViews(R.id.relative_parent);
			rViews.addView(R.id.relative_parent, subViews);
			
			rViews.setViewVisibility(R.id.showPicLeft, View.INVISIBLE);
			//rViews.setViewVisibility(R.id.showPicRight, View.INVISIBLE);
			rViews.setViewVisibility(R.id.showPic, View.VISIBLE);
			rViews.setViewVisibility(R.id.info, View.VISIBLE);
			rViews.setViewVisibility(R.id.infoRight, View.INVISIBLE);
			//rViews.setViewVisibility(R.id.infoLeft, View.INVISIBLE);
			rViews.setImageViewResource(R.id.showPic, pic[posPic % 4]);
			//rViews.setViewVisibility(R.id.widget_icon, View.VISIBLE);
			rViews.setTextViewText(R.id.showTitle, picStr[(posPic) % 4]);
			rViews.setTextViewText(R.id.showName, "Family");
			rViews.setTextViewText(R.id.showTime, "");
			
			posPic ++;
			pagefinish = true;
			layoutid = 1;
		}
		//rViews.setImageViewResource(R.id.showPic, pic[posPic % 4]);
		
		manager.updateAppWidget(cName, rViews);
	}
	private void playFamilyDefalutTask(){
		
		if (!isRun)
			return;
		new AsyncTask<Object, Object, Object>(){
			
			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				Log.v(TAG, "测试点1");
				if (!isRun)
					return null;
				if (!pagefinish){
					myDelay(3000);
					Log.v(TAG, "正在播放Family默认第" + posPic + "张图片");
					return null;
					
				}
				myDelay(2500);
				return null;
			}

			
			@Override
			protected void onPostExecute(Object result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (!isRun)
					return;
				showDefaluType();
				if (posPic < 10){
					playFamilyDefalutTask();
				} else {
					Intent intent = new Intent();
					intent.setAction("com.qlf.showDt");
					sendBroadcast(intent);
					WidgetDataMrg.flag_send ++;
					return;
				}
			}

		}.execute("");
	}
	
		
		
    public JSONObject LoadPicDetail(JSONObject obj)
    {
    	try
    	{
    		JSONObject resultData = HttpGetAllCnnt.getPicDetail(obj.getInt("id"),
					obj.getInt("uid"));
			
			if (resultData != null)
			{
				resultData = resultData.getJSONObject("data");
				if (!resultData.isNull("name")){
					//obj.put("name", picDt).getString("name");
					name = resultData.getString("name");
					
				}
				if (!resultData.isNull("title")){
					//obj.put("title", picDt.getString("title"));
					title = resultData.getString("title");
					if (title.equals(""))
						title = "无标题";
				}
				JSONArray piclist = resultData.getJSONArray("piclist");
				resultData = piclist.getJSONObject(0);
				String picUrl = resultData.getString("pic");
				if (!resultData.isNull("dateline")){
					time = resultData.getString("dateline");
					time = NetHelper.transTime(Long.parseLong(time));
				}
				//String time = picDt.getString("dateline");
				obj.put("picUrl", picUrl);
				//obj.put("time", time);
			}
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    	if (obj == null)
    		obj = new JSONObject();
    	return obj;
    }
    
    static boolean flag_showPicDetail = true;
    
    
    public static int computeSampleSize(BitmapFactory.Options options,
    		int minSideLength, int maxNumOfPixels) {
    	int initialSize = computeInitialSampleSize(options, minSideLength,maxNumOfPixels);
    	int roundedSize;
    	if (initialSize <= 8 ) {
    		roundedSize = 1;
    		while (roundedSize < initialSize) {
    			roundedSize <<= 1;
    		}
    	} else {
    		roundedSize = (initialSize + 7) / 8 * 8;
    	}
    	return roundedSize;
    }
    
    private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
    	double w = options.outWidth;
    	double h = options.outHeight;
    	int lowerBound = (maxNumOfPixels == -1) ? 1 :
    	(int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
    	int upperBound = (minSideLength == -1) ? 128 :
    	(int) Math.min(Math.floor(w / minSideLength),
    	Math.floor(h / minSideLength));
    	if (upperBound < lowerBound) {
    	// return the larger one when there is no overlapping zone.
    		return lowerBound;
    	}
    	if ((maxNumOfPixels == -1) &&
    	(minSideLength == -1)) {
    		return 1;
    	} else if (minSideLength == -1) {
    		return lowerBound;
    	} else {
    		return upperBound;
    	}
    }
    
   
    
    /**
     * 延时
     * @param time
     */
    private void myDelay(long time){
    	try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * 定义一个横竖屏的监听
     */
    private BroadcastReceiver screenOrientationReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Intent dialogIntent = new Intent();
			//WidgetDataMrg.wd_type = 1;
	    	dialogIntent.setClass(context, WidgetDialogActivity.class);
	    	PendingIntent dpIntent = PendingIntent.getActivity(context, 0, dialogIntent, 0);
	    	rViews.setOnClickPendingIntent(R.id.layout, dpIntent);
	    	AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
	    	ComponentName cName = new ComponentName(context, AppWidget.class);
	    	appWidgetManager.updateAppWidget(cName, rViews);
		}
	};
	
	public static List<String> mAllLocalPicList = null;
	
	int layoutid = 1;
	/**
	 * 如果选择的是播放本地的图片
	 */
	boolean Flag_firstPic = true;
	Bitmap showBm = null;
	Bitmap loadBm = null;
	boolean pagefinish = true;
	boolean localNOpic = true;
	boolean barFlag = true;
	private void playAllLocalPic(){
		if (!isRun)
			return;
		if (mAllLocalPicList == null || mAllLocalPicList.size() == 0) {
			//没有本地图片
			playFamilyDefalutTask();
			return;
		}
		new AsyncTask<Object, Object, Bitmap>(){

			@Override
			protected Bitmap doInBackground(Object... params) {
				// TODO Auto-generated method stub
				
				if (!isRun)
					return null;
				if (!pagefinish){
					Log.v(TAG, "播放本地全部图片：" + WidgetDataMrg.posAllLocalPicList);
					try {
						Thread.sleep(4500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
					
				}
				if (WidgetDataMrg.posAllLocalPicList < mAllLocalPicList.size()){
					String path = mAllLocalPicList.get(WidgetDataMrg.posAllLocalPicList ++);
					Log.v(TAG, "图片路径" + path);
					String [] fileArray = path.split("/");
					if (5 < fileArray.length && fileArray[4].equals("familydayVerPm") && 
							fileArray[5].equals("pic_cache")){
						Log.v(TAG, "读到是缓存文件夹pic_cache的内容,放弃!");
						showBm = null;
						return null;
					}
					
					File file = new File(path);
					if (file.exists()) {
						//Log.v(TAG, "字节数:" + file.length());
						if (file.length() < 6000)
							return null;
						int size = (int)file.length()/(1024*1024);
						BitmapFactory.Options options = new BitmapFactory.Options(); 
						if (size == 2) {
							size = 3;
						}
						if ((size == 0||size == 1) && file.length() > 512*1024) {
							size = 2;
						}
						//options.inSampleSize = size!=0?size:1;
						options.inSampleSize = 2 + size;
						options.inPreferredConfig = Bitmap.Config.RGB_565;
						try {
							Bitmap bm = BitmapFactory.decodeStream(new FileInputStream(file), 
									null, options);
							mList.add(bm);
							
							if (bm != null){
								localNOpic = false;
								myDelay(2000);
							}
							showBm = bm;
							return bm;
						} catch (Exception e) { 
							e.printStackTrace();
						}
						//return BitmapFactory.decodeFile(path,options);
					}

					return null;
				}
				return null;
				
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (!isRun)
					return;
				
				if (barFlag && result != null){
					barFlag = false;
					Message msg = handler.obtainMessage(1);
					handler.sendMessage(msg);
				}
				//播放图片
				showPicType(0);
				
				//如果来到最后一张图片，重启服务
				if (WidgetDataMrg.posAllLocalPicList == mAllLocalPicList.size()) {
					WidgetDataMrg.posAllLocalPicList = 0;
					if (localNOpic){
						if (barFlag){
							barFlag = false;
							Message msg = handler.obtainMessage(1);
							handler.sendMessage(msg);
						}
						//localNOpic = true;
						Log.v(TAG, "本地没有可以播放的图片，播放Family默认图片！");
						playFamilyDefalutTask();
						return;
					}
					showBm = null;
					Intent intent = new Intent();
					intent.setAction("com.qlf.showDt");
					sendBroadcast(intent);
					WidgetDataMrg.flag_send ++;
					return;
				}
				//如果已经播放了10张图片，则重启服务，清理内容
				if (WidgetDataMrg.posAllLocalPicList % 10 == 0){
					showBm = null;
					Intent intent = new Intent();
					intent.setAction("com.qlf.showDt");
					sendBroadcast(intent);
					WidgetDataMrg.flag_send ++;
					return;
				}
				if (WidgetDataMrg.posAllLocalPicList < mAllLocalPicList.size()) {
					playAllLocalPic();
				}
			}
			
		}.execute("");
	}
	
	private void showPicType(int type){
		String [] info = new String[]{"", "", ""};
		switch (type) {
		//播放本地全部图片
		case 0:
			info[0] = "我家幸福时光";
			info[1] = "Family";
			info[2] = "";
			break;
		case 1:
			info[0] = title;
			info[1] = name;
			info[2] = time;
			break;
		case 2:
			
			break;
		default:
			break;
		}
		if (showBm != null) {
			int bmWidth = showBm.getWidth();
			int bmHeight = showBm.getHeight();
			if (bmWidth < bmHeight){
				
				if (layoutid == 1){
					RemoteViews subViews = new RemoteViews(packageName, R.layout.relative_in);
					rViews.removeAllViews(R.id.relative_parent);
					rViews.addView(R.id.relative_parent, subViews);
					
					rViews.setViewVisibility(R.id.showPicLeft, View.VISIBLE);
					rViews.setViewVisibility(R.id.infoRight, View.VISIBLE);
					rViews.setViewVisibility(R.id.showPic, View.INVISIBLE);
					rViews.setViewVisibility(R.id.info, View.INVISIBLE);
					
					//rViews.setViewVisibility(R.id.widget_icon, View.VISIBLE);
					tempinfo0 = info[0];
					tempinfo1 = info[1];
					tempinfo2 = info[2];
					tempBitmap = showBm;
					
					rViews.setTextViewText(R.id.showTitleRight, info[0]);
					rViews.setTextViewText(R.id.showNameRight, info[1]);
					rViews.setTextViewText(R.id.showTimeRight, info[2]);
					rViews.setImageViewBitmap(R.id.showPicLeft, showBm);
					
					pagefinish = false;
					layoutid = 2;
				} else {
					RemoteViews subViews = new RemoteViews(packageName, R.layout.relative_out);
					rViews.removeAllViews(R.id.relative_parent);
					rViews.addView(R.id.relative_parent, subViews);
					
					rViews.setViewVisibility(R.id.showPicLeft, View.VISIBLE);
					rViews.setViewVisibility(R.id.infoRight, View.VISIBLE);
					rViews.setViewVisibility(R.id.showPic, View.INVISIBLE);
					rViews.setViewVisibility(R.id.info, View.INVISIBLE);
					
					//rViews.setViewVisibility(R.id.widget_icon, View.VISIBLE);
					
					rViews.setTextViewText(R.id.showTitleRight, tempinfo0);
					rViews.setTextViewText(R.id.showNameRight, tempinfo1);
					rViews.setTextViewText(R.id.showTimeRight, tempinfo2);
					rViews.setImageViewBitmap(R.id.showPicLeft, tempBitmap);
					
					pagefinish = true;
					layoutid = 1;
				}
				
				//rViews.setImageViewBitmap(R.id.showPicLeft, showBm);
				
				manager.updateAppWidget(cName, rViews);
			} else{
				
				if (layoutid == 1){
					RemoteViews subViews = new RemoteViews(packageName, R.layout.relative_in);
					rViews.removeAllViews(R.id.relative_parent);
					rViews.addView(R.id.relative_parent, subViews);
					
					rViews.setViewVisibility(R.id.showPic, View.VISIBLE);
					rViews.setViewVisibility(R.id.showPicLeft, View.INVISIBLE);
					//rViews.setViewVisibility(R.id.showPicRight, View.INVISIBLE);
					//rViews.setViewVisibility(R.id.infoLeft, View.INVISIBLE);
					rViews.setViewVisibility(R.id.info, View.VISIBLE);
					rViews.setViewVisibility(R.id.infoRight, View.INVISIBLE);
					
					//rViews.setViewVisibility(R.id.widget_icon, View.VISIBLE);
					tempinfo0 = info[0];
					tempinfo1 = info[1];
					tempinfo2 = info[2];
					tempBitmap = showBm;
					
					rViews.setTextViewText(R.id.showTitle, info[0]);
					rViews.setTextViewText(R.id.showName, info[1]);
					rViews.setTextViewText(R.id.showTime, info[2]);
					rViews.setImageViewBitmap(R.id.showPic, showBm);
					
					
					pagefinish = false;
					layoutid = 2;
				} else {
					RemoteViews subViews = new RemoteViews(packageName, R.layout.relative_out);
					rViews.removeAllViews(R.id.relative_parent);
					rViews.addView(R.id.relative_parent, subViews);
					
					rViews.setViewVisibility(R.id.showPic, View.VISIBLE);
					rViews.setViewVisibility(R.id.showPicLeft, View.INVISIBLE);
					//rViews.setViewVisibility(R.id.showPicRight, View.INVISIBLE);
					//rViews.setViewVisibility(R.id.infoLeft, View.INVISIBLE);
					rViews.setViewVisibility(R.id.info, View.VISIBLE);
					rViews.setViewVisibility(R.id.infoRight, View.INVISIBLE);
					
					//rViews.setViewVisibility(R.id.widget_icon, View.VISIBLE);
					
					rViews.setTextViewText(R.id.showTitle, tempinfo0);
					rViews.setTextViewText(R.id.showName, tempinfo1);
					rViews.setTextViewText(R.id.showTime, tempinfo2);
					rViews.setImageViewBitmap(R.id.showPic, tempBitmap);
					
					pagefinish = true;
					layoutid = 1;
				}
				//rViews.setImageViewBitmap(R.id.showPic, result);
				
				
				manager.updateAppWidget(cName, rViews);
			}
			
		}
		
	}
	
	String tempinfo0;
	String tempinfo1;
	String tempinfo2;
	Bitmap tempBitmap;
	
	public boolean isExitPhoto(String id){
		int length = ((FamilyDayVerPMApplication)getApplication()).widget_arrayList.size();
		for (int i = 0; i < length; i ++){
			if (id.equals(((FamilyDayVerPMApplication)getApplication()).widget_arrayList.get(i).id))
				return true;
		}
		return false;
	}
    
}

