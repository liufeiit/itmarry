package com.ze.familydayverlenovo;

/**
 * 		2013.9. 		更新jpush客户端版本 @ze 
 * 		2013.9.15 		修复适配480x800的屏幕
 * 						修复第一张图片无法评论和喜欢的bug
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ze.commontool.NetHelper;
import com.ze.commontool.ToastUtil;
import com.ze.model.DataModel;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FamilyDayVerPMApplication extends Application {
	//By mouse 添加widget所需要的数据结构
	static Context appContext;
	public JSONArray widget_array;
	public ArrayList<DataModel> widget_arrayList;
	
	public  JSONArray wf_array;
	public ArrayList<DataModel> wf_arrayList;
	
	public  JSONArray wf_blogarray;
	public ArrayList<DataModel> wf_blogarrayList;
	
	public  JSONArray wf_temparray;
	public ArrayList<DataModel> wf_temparrayList;
	
	public  JSONArray wf_eventarray;
	public ArrayList<DataModel> wf_eventarrayList;
	
	public  JSONArray wf_videoarray;
	public ArrayList<DataModel> wf_videoarrayList;
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		appContext = this;
		JPushInterface.setDebugMode(false);
		JPushInterface.init(this);
		JPushInterface.setLatestNotifactionNumber(this, 3);			// 最多3个推送同时出现在通知栏
		
		clearFileUnExcept(Environment.getExternalStorageDirectory() + "/1/");
		clearFileUnExcept(Environment.getExternalStorageDirectory() + "/2/");
		
	}
	/**
	 * lenovo 无故出现文件夹1，2并且无内容，检查，如果出现，干掉！
	 * @return
	 */
	public void clearFileUnExcept(String str_dir)
	{
		File dir = new File(str_dir);
		if (dir.exists()) {
			if (dir.list().length == 0) {
				dir.delete();
			}
		}
	}
	public static void bindJpush(final Context context, final String uidString)
	{
		new Thread( new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				JPushInterface.setAliasAndTags(context, uidString,null, aliasCallback);
				String respon = NetHelper.getResponByHttpClient(String.format( context.getResources().getString( 
						R.string.http_bindjpush),uidString ));
				try {
					JSONObject object = new JSONObject(respon);
					if ( object.getInt("code") == 0 ) {
						Log.v("jpush","bind success");
					}else
					{
						Log.v("jpush","bind error");
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public static void unBindJpush(final Context context, final String uidString)
	{
		new Thread( new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				JPushInterface.setAliasAndTags(context, "", null);
				JPushInterface.clearAllNotifications(context);
				String respon = NetHelper.getResponByHttpClient(String.format( context.getResources().getString( 
						R.string.http_unbindjpush),uidString ));
				try {
					JSONObject object = new JSONObject(respon);
					if ( object.getInt("code") == 0 ) {
						Log.v("jpush","unbind success");
					}else
					{
						Log.v("jpush","unbind error");
						ToastUtil.show(appContext,"unbind error" );
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}).start();
	}
static TagAliasCallback aliasCallback = new TagAliasCallback() {
		
		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			// TODO Auto-generated method stub
			String logs ;
			switch (code) {
			case 0:
				logs = "Set tag and alias success, alias = " + alias + "; tags = " + tags;
				Log.i("jpush", logs);
				break;
			
			default:
				logs = "Failed with errorCode = " + code + " " + alias + "; tags = " + tags;
				Log.e("jpush", logs);
				ToastUtil.show(appContext,"绑定推送失败！请你重新登录重试！" );
			}
			

		}
	};
}
