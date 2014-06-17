package com.ze.familydayverlenovo;



import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.ze.commontool.DisplayUtil;
import com.ze.commontool.NetHelper;
import com.ze.commontool.PublicInfo;
import com.ze.commontool.ToastUtil;
import com.ze.familydayverlenovo.adapter.MyGridViewAdapter;
import com.ze.familydayverlenovo.userinfo.Componet;
import com.ze.familydayverlenovo.userinfo.UserInfoManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public 		static 	MainActivity		MAIN_ACTIVITY;
//	private 	ListView 							mListView;
	public 	GridView							mGridView;
//	private 	MainListViewAdapter		mAdapter;
	private 	MyGridViewAdapter 				mGridAdapter;
	private 	List<Map<String, Object>> mList;	
	private 	TextView							mTempTextView;
	private 	Button 							mWeatherIcon;
	private 	View 								mWeatherDetailView;
	private 	TextView							mTopLabel;
	private 	TextView							mTempLabel;
	private 	TextView							mConditionLabel;
	private 	TextView							mTipsLabel;
//	private   	View 								mLabelClose;
	private 	AlertDialog						mWeatherDetailViewAlertDialog;
	private		View								mLayoutTitle;
	private 	static final float LAYOUT_SIZE_PROPORTION = 0.37f;  // 标题尺寸占屏幕0.37
	private 	final int BUTTON_COUNT = 9;
	public static boolean isExist = false;
	private SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Display display = getWindowManager().getDefaultDisplay();
		PublicInfo.SCREEN_W = display.getWidth();
		PublicInfo.SCREEN_H = display.getHeight();
		sharedPreferences = MainActivity.this.getSharedPreferences("weather.cfg",
				Context.MODE_WORLD_WRITEABLE );
		if( ! isConnectingToInternet() )
		{
			new AlertDialog.Builder(this).setMessage(R.string.dialog_msg_nonettips).setPositiveButton(R.string.dialog_button_sure, dialogClickListener)
			.setNegativeButton(R.string.dialog_button_cancel, null).create().show();
		}
		initWidget();
		wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		PublicInfo.SCREEN_W = wm.getDefaultDisplay().getWidth();//屏幕宽度
		PublicInfo.SCREEN_H = wm.getDefaultDisplay().getHeight();//屏幕高度
		String locationString = getLoaction();
		if( locationString != null )
		{
			getWeatherInfo(locationString);
		}else
		{
			mTempTextView.setText("     未设置");
		}
//		getWeatherInfo();
		loadTagInfo();
		isExist = true;
		MAIN_ACTIVITY = this;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isExist = false;
	}
	public void loadTagInfo()
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Componet componet = UserInfoManager.getInstance(MainActivity.this).getItem("m_auth");
				if (componet != null) {
					String responString = NetHelper.getResponByHttpClient(getResources().getString(R.string.http_zone_list), 
							componet.getValue()
							, 1 +  "",""
							);
					Componet tagComponet = new Componet("tag");
					tagComponet.setValue(responString);
					 UserInfoManager.getInstance(MainActivity.this).add(tagComponet);
					 UserInfoManager.getInstance(MainActivity.this).save(tagComponet);
				}
				
			}
		}).start();
	}
	public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null)
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null)
                  for (int i = 0; i < info.length; i++)
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
 
          }
          return false;
    }
	private WifiManager wifiManager ;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		checkNewsNum();
//		if (wifiManager.WIFI_STATE_ENABLED == wifiManager.getWifiState()) {
//			// 
//			Log.v("test", "wifi onStart");
//			Intent intent = new Intent();
//			intent.setClass(this, NetHelper.class);
//			startService(intent);
//		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
	public XmlSaxParserCallback callback = new XmlSaxParserCallback() {
		
		@Override
		public void onFind(String uri, String localName, String qName,
				Attributes attributes) {
			// TODO Auto-generated method stub
			String cityCode = attributes.getValue("code");
			for (int i = 0; i < cityCode.length(); i++) {
				if( cityCode.charAt(i) < '0' || cityCode.charAt(i) >'9' )
				{
					cityCode = cityCode.replace(cityCode.charAt(i), ((char)' '));
				}
			}
			cityCode = cityCode.trim();
//			Componet location = new Componet("location");   //  citycode
//			location.setValue(cityCode);
//			UserInfoManager.getInstance(MainActivity.this).add(location);
//			UserInfoManager.getInstance(MainActivity.this).save(location);
			sharedPreferences.edit().putString("location", cityCode).commit();
			getWeatherInfo(cityCode);
		}
	};
	public AlertDialog setLocationAlertDialog;
	public EditText 		locationEditText;
	public void setLocation()
	{
		if( setLocationAlertDialog == null )
		{
			if( locationEditText == null )
			{
				locationEditText = new EditText(this);
				locationEditText.setHint("请输入你要查询的城市名字！例如：北京");
				locationEditText.setPadding(20, 50, 20, 50);
			}
			setLocationAlertDialog = new AlertDialog.Builder(this).setNegativeButton("取消", null).
					setPositiveButton("确定",new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							final String str_city = locationEditText.getText().toString().trim();
							if( ! str_city.equals("") )
							{
								ToastUtil.show(MainActivity.this, "正在努力获取天气信息...");
								new Thread(new Runnable() {
									public void run() {
										SAXParserFactory parserFactory = SAXParserFactory.newInstance();
										// 2.构建并实例化SAXPraser对象
								        try {
											SAXParser saxParser = parserFactory.newSAXParser();
											// 3.构建XMLReader解析器
									        XMLReader xmlReader = saxParser.getXMLReader();
									        xmlReader.setContentHandler(new CityCodeParserHandler(str_city, callback));
									        InputStream stream = getResources().openRawResource(R.raw.citycodes);
									        InputSource is = new InputSource(stream);
									     // 6.解析文件
									        xmlReader.parse(is);
										} catch (ParserConfigurationException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (SAXException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									};
								}).start();
							}else {
								ToastUtil.show(MainActivity.this, "城市名称不能输入为空");
							}
						}
					}).
					setView(locationEditText).create();
		}
		if( mWeatherDetailViewAlertDialog !=null  && mWeatherDetailViewAlertDialog.isShowing() )
		{
			mWeatherDetailViewAlertDialog.dismiss();
		}
		setLocationAlertDialog.show();
	}
	public String getLoaction()
	{
		String locationString = sharedPreferences.getString("location", null);
//		Componet loactionComponet = UserInfoManager.getInstance(this).getItem("location") ;
		if ( locationString != null ) {
			return locationString;
		}
//		else
//		{
//			
//			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					Componet username = UserInfoManager.getInstance(MainActivity.this).getItem("username");
//					if( username != null )
//					{
//						String respon = NetHelper.getLocation(getResources().getString(R.string.http_phonenumber_from),
//								username.getValue() );
//						int find_s = respon.indexOf("<location>") + 10;
//						int find_e = respon.indexOf("</location>");
//						if(find_e == -1 | find_s == -1 )
//						{
//							return;
//						}else
//						{
//							respon = respon.substring(find_s, find_e);
//							int find_name = respon.indexOf(" ")+1;
//							respon = respon.substring(find_name);
//							SAXParserFactory parserFactory = SAXParserFactory.newInstance();
//							 // 2.构建并实例化SAXPraser对象
//						        try {
//									SAXParser saxParser = parserFactory.newSAXParser();
//									// 3.构建XMLReader解析器
//							        XMLReader xmlReader = saxParser.getXMLReader();
//							        xmlReader.setContentHandler(new CityCodeParserHandler(respon, callback));
//							        InputStream stream = getResources().openRawResource(R.raw.citycodes);
//							        InputSource is = new InputSource(stream);
//							     // 6.解析文件
//							        xmlReader.parse(is);
//								} catch (ParserConfigurationException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								} catch (SAXException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								} catch (IOException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//						}
//					}
//				}
//			}).start();
			return null;
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				// weather
				try {
					String result = (String) msg.obj;
					JSONObject object = new JSONObject(result);
					object = object.getJSONObject("weatherinfo");
					String temp = object.getString("temp1");
					mTempLabel.setText(String.format(getResources().getString(R.string.weather_temp), temp));
					
					String date = object.getString("date_y");
					String city = object.getString("city");
					mTopLabel.setText(String.format(getResources().getString(R.string.weather_label), date,city));
					
//					SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("weather.cfg",
//							Context.MODE_WORLD_WRITEABLE );
					String cityNameString = sharedPreferences.getString("cityName", null);
					if( cityNameString != null )
					{
						mTempTextView.setText( city + "  " + temp);
					}
					
					String weather = object.getString("weather1");
					mConditionLabel.setText(String.format(getResources().getString(R.string.weather_condition), weather));
					
					String cur_weather = object.getString("img_title1");
					if( cur_weather.equals("晴") )
					{
						mWeatherIcon.setBackgroundResource(R.drawable.w_qing);
					}else if( cur_weather.contains("雨") ){
						if( cur_weather.contains("小"))
						{
							mWeatherIcon.setBackgroundResource(R.drawable.w_xiaoyu);
						}else if(cur_weather.contains("雷"))
						{
							mWeatherIcon.setBackgroundResource(R.drawable.w_leizhenyu);
						}else if(cur_weather.contains("雪"))
						{
							mWeatherIcon.setBackgroundResource(R.drawable.w_yujiaxue);
						}else {
							mWeatherIcon.setBackgroundResource(R.drawable.w_dayu);
						}
					}else if( cur_weather.contains("雪") ){
						if( cur_weather.contains("小"))
						{
							mWeatherIcon.setBackgroundResource(R.drawable.w_xiaoxue);
						}else {
							mWeatherIcon.setBackgroundResource(R.drawable.w_daxue);
						}
					}else if( cur_weather.contains("阴") ){
							mWeatherIcon.setBackgroundResource(R.drawable.w_yin);
					}
					String tips = object.getString("index_d");
					mTipsLabel.setText(String.format(getResources().getString(R.string.weather_tips), tips));
					isReadWeather = true;
					// update date
					sharedPreferences.edit().putString("updatew", new Date().getDate() + "").commit();
//					Componet updatewComponet = new Componet("updatew");
//					updatewComponet.setValue(new Date().getDate() + "");
//					UserInfoManager.getInstance(MainActivity.this).add( updatewComponet );
//					UserInfoManager.getInstance(MainActivity.this).save(updatewComponet);
					// weather info
					sharedPreferences.edit().putString("weather", result).commit();
//					Componet weatherComponet = new Componet("weather");
//					weatherComponet.setValue(result);
//					UserInfoManager.getInstance(MainActivity.this).add( weatherComponet );
//					UserInfoManager.getInstance(MainActivity.this).save(weatherComponet);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						ToastUtil.show(MainActivity.this, "网络不好，无法显示获取天气！");
					}finally{
						isReadWeather = true;
					}
				break;
			case 1:
				ToastUtil.show(MainActivity.this, "查不到您所输入的地名，请输入正确的城市名称（汉字），或者输入所在地的市级城市名！");
				break;
			default:
				break;
			}
		};
	};
	public void getWeatherInfo(final String citycode)
	{
		if( citycode != null && citycode.length() > 0 )
		{
			
		new Thread( new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String result = "";
				String weatherCache = sharedPreferences.getString("weather", null);
//				Componet weatherComponet = UserInfoManager.getInstance(MainActivity.this).getItem("weather");
				if( weatherCache != null && ! weatherCache.equals("") )
				{
					// 天气信息不为空
					String updatewString = sharedPreferences.getString("updatew", "");
//					Componet updatewComponet = UserInfoManager.getInstance(MainActivity.this).getItem("updatew");
					if ( updatewString.equals(new Date().getDate() + "")) {
						// 更新时间没变化
						result = weatherCache;
						isReadWeather = true;
					}else
					{
						// 更新时间变化,请求新数据
						result = NetHelper.getResponByHttpClient(getResources().getString(R.string.http_weatherapi),
								citycode);
					}
				}else
				{
					result = NetHelper.getResponByHttpClient(getResources().getString(R.string.http_weatherapi),
							citycode);
				}
				Message msg = handler.obtainMessage(0);
				msg.obj = result;
				handler.sendMessage(msg);
			}
		}).start();
		}
	}
	private boolean isReadWeather = false;
	public void checkNewsNum()
	{
		new AsyncTask<String, String, String>(){

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Componet m_auth = UserInfoManager.getInstance(MainActivity.this).getItem("m_auth");
				String string = null;
				if ( m_auth != null ) {
					string = NetHelper.getResponByHttpClient(getResources().getString(R.string.http_news_num), 
							m_auth.getValue());
//					Log.v("test", "read news:"+UserInfoManager.getInstance(MainActivity.this).getItem("m_auth").getValue());
				}
				return string;
			}
			protected void onPostExecute(String result) {
				if( result == null )
				{
					return;
				}
				try {
					JSONObject object = new JSONObject(result);
					if ( ! object.isNull("data") )
					{
						object = object.getJSONObject("data");
						int photo = object.getInt("addphoto");
						int addvideo = object.getInt("addvideo");
						int addevent = object.getInt("addevent");
						int addblog = object.getInt("addblog");
						int requestcount = object.getInt("applycount");
						// TODO: 
						int pmcount = Integer.parseInt( object.getString("pmcount") );
						Map<String, Object> map ;
						mList.clear();
						for(int i=0; i<BUTTON_COUNT;i++)
						{
							map = new HashMap<String, Object>();
							if( i == 0 )
							map.put("count", photo);
							else if( i== 1 )
							{
								map.put("count", addblog);
							}else if( i== 2 )
							{
								map.put("count", addevent);
							}else if( i== 3 )
							{
								map.put("count", addvideo);
							}else if( i== 4 )
							{
								map.put("count", pmcount);
							}else if (i== 6){
								map.put("count", requestcount);
							}else
							{
								map.put("count", 0);
							}
//							map.put("label", getResources().getString(STRING_ID[i]));
							mList.add(map);
						}
						mGridAdapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.v("test", e.toString());
				}
				
			};
			}.execute("");
	}
	public boolean isLandscape = false;
	private void initWidget()
	{
		mLayoutTitle	= findViewById(R.id.main_logo);
		mTempTextView = (TextView)findViewById(R.id.main_weather_temp);
		mWeatherIcon	= (Button)findViewById(R.id.main_weather_type);
		
		mWeatherIcon.setOnClickListener(ButtonOnclickListener);
		mTempTextView.setOnClickListener(ButtonOnclickListener);
		if( mWeatherDetailViewAlertDialog == null ){
			mWeatherDetailView = MainActivity.this.getLayoutInflater().inflate(R.layout.weatherpulldown_layout, null);
//			mLabelClose	= mWeatherDetailView.findViewById(R.id.main_weather_label_close);
//			mLabelClose.setOnClickListener(ButtonOnclickListener);
			mTopLabel		= (TextView)mWeatherDetailView.findViewById(R.id.main_weather_label_top);
			mTopLabel.setOnClickListener(ButtonOnclickListener);
			mTempLabel	= (TextView)mWeatherDetailView.findViewById(R.id.main_weather_label_temp);
			mConditionLabel	= (TextView)mWeatherDetailView.findViewById(R.id.main_weather_label_condition);
			mTipsLabel		=(TextView)mWeatherDetailView.findViewById(R.id.main_weather_label_tips);
			mWeatherDetailViewAlertDialog = new AlertDialog.Builder(MainActivity.this).setView(mWeatherDetailView).
					setNeutralButton(R.string.dialog_button_close, null).setTitle(R.string.dialog_title_weather_detail).create();
		}
		
//		mListView 			= (ListView)findViewById(R.id.main_listview);
		mGridView			= (GridView)findViewById(R.id.main_gridview);
		
		mList 					= new ArrayList<Map<String,Object>>();
		Map<String, Object> map ;
		
		for(int i=0; i<BUTTON_COUNT;i++)
		{
			map = new HashMap<String, Object>();
		
				map.put("count", 0);
//				map.put("icon", getResources().getDrawable(ICON_ID[i]));
//			map.put("label", getResources().getString(STRING_ID[i]));
			mList.add(map);
		}
		
		mGridAdapter 			= new MyGridViewAdapter(this, mList,R.layout.main_gridview_item );
		/**
		 *  	代码初始化天气图片的大小（1：1）
		 *  	代码初始化GridView大小
		 */
		int len = 0;
		int weatherIconLen = 0;
		int logoWidth = 0;	// 230x167
		int logoHeight = 0;	// 230x167
		
		LayoutParams logoLayoutParams = (LayoutParams) mLayoutTitle.getLayoutParams();
		LayoutParams weatherIconParams = new LayoutParams(
				0,
				0);
		weatherIconParams.addRule(RelativeLayout.ABOVE, mTempTextView.getId());
		if( PublicInfo.SCREEN_H < PublicInfo.SCREEN_W )
		{
			// 横屏
			len = PublicInfo.SCREEN_H;
			
			if( PublicInfo.SCREEN_W * LAYOUT_SIZE_PROPORTION > 
				DisplayUtil.dip2px(230f + 20f, DisplayUtil.getDefaultScale(MainActivity.this)  )  )
			{
				logoWidth = DisplayUtil.dip2px(230f, DisplayUtil.getDefaultScale(MainActivity.this)  );
			}else
			{
				logoWidth = (int) (PublicInfo.SCREEN_W * LAYOUT_SIZE_PROPORTION - DisplayUtil.dip2px(
						20f,
						DisplayUtil.getDefaultScale(MainActivity.this)  ));
			}
			logoHeight = (int) ((167f/230f)*logoWidth);
			
			if( PublicInfo.SCREEN_W * LAYOUT_SIZE_PROPORTION > 
				DisplayUtil.dip2px(120f, DisplayUtil.getDefaultScale(MainActivity.this)  )  )
			{
				weatherIconLen = DisplayUtil.dip2px((120f-20f), DisplayUtil.getDefaultScale(MainActivity.this)  );
			}else
			{
				weatherIconLen = (int) (PublicInfo.SCREEN_W * LAYOUT_SIZE_PROPORTION - DisplayUtil.dip2px(
						 20f,
						DisplayUtil.getDefaultScale(MainActivity.this)  ));
			}
			weatherIconParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		}else {
			// 竖屏
			 len = PublicInfo.SCREEN_W ;
			 
			 if( PublicInfo.SCREEN_H * LAYOUT_SIZE_PROPORTION > 
				DisplayUtil.dip2px(167f + 40f, DisplayUtil.getDefaultScale(MainActivity.this)  )  )
			 {
				logoHeight = DisplayUtil.dip2px(167f, DisplayUtil.getDefaultScale(MainActivity.this)  );
			 }else
			 {
				 logoHeight = (int) (PublicInfo.SCREEN_H * LAYOUT_SIZE_PROPORTION -DisplayUtil.dip2px(
						 40f,
						DisplayUtil.getDefaultScale(MainActivity.this)  ));
			 }
			 logoWidth = (int) ((230f/167f)*logoHeight);
			 
			 weatherIconParams.addRule(RelativeLayout.CENTER_VERTICAL);
			 weatherIconParams.rightMargin = 
				 DisplayUtil.dip2px(10f, DisplayUtil.getDefaultScale(MainActivity.this)  );
			 if( PublicInfo.SCREEN_H * LAYOUT_SIZE_PROPORTION > 
				DisplayUtil.dip2px(150f, DisplayUtil.getDefaultScale(MainActivity.this)  )  )
			{
				 weatherIconLen = DisplayUtil.dip2px((150f-50f), DisplayUtil.getDefaultScale(MainActivity.this)  );
			}else
			{
				weatherIconLen = (int) (PublicInfo.SCREEN_H * LAYOUT_SIZE_PROPORTION -DisplayUtil.dip2px(
						 50f,
						DisplayUtil.getDefaultScale(MainActivity.this)  ));
			}
			 weatherIconParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		}
		weatherIconParams.width = (int) (weatherIconLen*0.8);
		weatherIconParams.height	= (int) (weatherIconLen*0.8);
		mWeatherIcon.setLayoutParams(weatherIconParams);
		
//		logoLayoutParams.width = logoWidth0;
//		logoLayoutParams.height = logoHeight/2;
//		mLayoutTitle.setLayoutParams(logoLayoutParams);
		
		LayoutParams params = new LayoutParams(len, len);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		mGridView.setLayoutParams(params);
		mGridView.setAdapter(mGridAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if ( arg2 == 0) {
					// to pic 
					Intent intent = new Intent();
					intent.putExtra("type", PhotoShowActivity.DETAIL_PIC);
					if ( (Integer)mList.get(0).get("count") > 0 ) {
						intent.putExtra("new", true);
					}
//					intent.setClass(MainActivity.this, PhotoShowActivity.class);
					intent.setClass(MainActivity.this, PicFallViewActivity.class);
					MainActivity.this.startActivity(intent);
				}else if( arg2 == 1  )
				{
					// to note
					Intent intent = new Intent();
					intent.putExtra("type", PhotoShowActivity.DETAIL_BLOG);
					intent.setClass(MainActivity.this, PhotoShowActivity.class);
					MainActivity.this.startActivity(intent);
				}else if( arg2 == 2 )
				{
					// to activity
					Intent intent = new Intent();
					intent.putExtra("type", PhotoShowActivity.DETAIL_EVNET);
					intent.setClass(MainActivity.this, PhotoShowActivity.class);
					MainActivity.this.startActivity(intent);
				}else if( arg2 == 3 )
				{
					// to video
					Intent intent = new Intent();
					intent.putExtra("type", PhotoShowActivity.DETAIL_VIDEO);
					intent.setClass(MainActivity.this, PhotoShowActivity.class);
					MainActivity.this.startActivity(intent);
//					Toast.makeText(MainActivity.this, R.string.notopen, Toast.LENGTH_SHORT).show();
				}else if( arg2 == 4 )
				{
					// to dialog
					Intent intent = new Intent();
					intent.putExtra("frommain", true);
					intent.setClass(MainActivity.this, DialogActivity.class);
					MainActivity.this.startActivity(intent);
				}else if( arg2 == 5 )
				{
					// to space
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, SpaceActivity.class);
					MainActivity.this.startActivity(intent);
				}else if( arg2 == 6 )
				{
					// to family
					Intent intent = new Intent();
					intent.putExtra("count", (Integer)mList.get(arg2).get("count"));
					intent.setClass(MainActivity.this, FamilyActivity.class);
					MainActivity.this.startActivity(intent);
				}else if( arg2 == 8 )
				{
					// to setting
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, SettingActivity.class);
					MainActivity.this.startActivity(intent);
				}else if( arg2 == 7 )
				{
					// to publish
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, PublishActivity.class);
					MainActivity.this.startActivity(intent);
				}
					
			}
		});
	}
	public void unLogin()
	{
		FamilyDayVerPMApplication.unBindJpush(MainActivity.this, UserInfoManager.getInstance(MainActivity.this).getItem("uid").getValue());
		UserInfoManager.getInstance(MainActivity.this).deleteAll();
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, LoginActivity.class);
		MainActivity.this.startActivity(intent);
		MainActivity.this.finish();
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.activity_login, menu);
		menu.add(menu.NONE, 0, 0, R.string.exit).setIcon(R.drawable.icon_exit);
		menu.add(menu.NONE, 1, 1, R.string.unsign).setIcon(R.drawable.icon_exit);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
			case 0:
				MainActivity.this.finish();
				break;
			case 1:
				unLogin();
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if( keyCode == KeyEvent.KEYCODE_BACK )
		{
			Intent intent= new Intent(Intent.ACTION_MAIN); 
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	class CityCodeParserHandler extends DefaultHandler {
		private String find_key;
		private XmlSaxParserCallback callback;
		private boolean isFind = false;
		public CityCodeParserHandler(String key,XmlSaxParserCallback callback)
		{
			find_key = key;
			this.callback = callback;
		}
		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			if( isFind == false )
			{
				Message message = handler.obtainMessage(1);
				handler.sendMessage(message);
			}
			super.endDocument();
		}
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			// TODO Auto-generated method stub
			String node = localName.length() != 0 ? localName : qName;
			if( node.equals("string"))
			{
				if( attributes.getValue("name").equals(find_key) ){
//					Componet cityNameComponet = new Componet("cityName");
//					cityNameComponet.setValue(find_key);
					sharedPreferences.edit().putString("cityName", find_key).commit();
//					UserInfoManager.getInstance(MainActivity.this).add(cityNameComponet);
//					UserInfoManager.getInstance(MainActivity.this).save(cityNameComponet);
					
					if( sharedPreferences.getString("weather", null) != null )
					{
						sharedPreferences.edit().putString("weather", null).commit();
					}
					isFind = true;
					callback.onFind(uri, localName, qName, attributes);
				}
			}
			super.startElement(uri, localName, qName, attributes);
		}
	}
	
	interface XmlSaxParserCallback {
		public void onFind(String uri, String localName, String qName,
				Attributes attributes);
	}
	private Animation weatherdetailOpenAnimation = null;
	private Animation	weatherdetailCloseAnimation = null;
	private OnClickListener ButtonOnclickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if ( v == mWeatherIcon ) {
				// open detail
				if( isReadWeather )
				{
//					if( weatherdetailOpenAnimation == null ){
//						weatherdetailOpenAnimation = new AlphaAnimation(0.0f, 1.0f);
//						weatherdetailOpenAnimation.setDuration(500);
//					}
//					mWeatherDetailView.startAnimation(weatherdetailOpenAnimation);
					mWeatherDetailViewAlertDialog.show();
				}else if( getLoaction() == null )
				{
					setLocation();
				}
				else
				{
					Toast.makeText(MainActivity.this, "正在为你获取天气信息，请稍等！", Toast.LENGTH_LONG).show();
				}
//			}else if( v == mLabelClose )
//			{
//				// close detail
////				if( weatherdetailCloseAnimation == null ){
////					weatherdetailCloseAnimation = new AlphaAnimation(1.0f, 0.0f);
////					weatherdetailCloseAnimation.setDuration(500);
////					weatherdetailCloseAnimation.setAnimationListener(new AnimationListener() {
////						
////						@Override
////						public void onAnimationStart(Animation animation) {
////							// TODO Auto-generated method stub
////							
////						}
////						
////						@Override
////						public void onAnimationRepeat(Animation animation) {
////							// TODO Auto-generated method stub
////							
////						}
////						
////						@Override
////						public void onAnimationEnd(Animation animation) {
////							// TODO Auto-generated method stub
////							mWeatherDetailView.setVisibility(View.INVISIBLE);
////						}
////					});
////				}
////				mWeatherDetailView.startAnimation(weatherdetailCloseAnimation);
//				mWeatherDetailViewAlertDialog.dismiss();
			}else if( v == mTopLabel )
			{
				setLocation();
			}else if( v == mTempTextView )
			{
				setLocation();
			}
		}
	};
	private DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			if ( which == dialog.BUTTON_POSITIVE ) {
				startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
			}
		}
	};
}
