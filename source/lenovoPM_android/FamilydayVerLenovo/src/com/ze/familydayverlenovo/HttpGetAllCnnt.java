package com.ze.familydayverlenovo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HttpGetAllCnnt {
	private static final int REQUEST_TIMEOUT = 5*1000;//设置请求超时10秒钟
	private static final int SO_TIMEOUT = 5*1000;  //设置等待数据超时时间10秒钟
	
	static String url = "http://www.familyday.com.cn/dapi/space.php?do=pmfeed&m_auth=" +
			"12d721tdDOv71KPT8bb5XwlyvMkF0Bu6Cn%2BppSXFUJTHzCV8bzCAdDGw8Ww7HlQqkAjIlFs7q35UJNv4lpWtU9P6" +
			"&idtype=photoid&perpage=5&page=1";
	
	public static JSONObject getPhotoID(int perpage, int page){
		
		JSONObject obj = null;
		//int state = 0;
		try{
			if (WidgetDataMrg.M_AUTH == null){
				return null;
			}
			url = "http://www.familyday.com.cn/dapi/space.php?do=pmfeed&m_auth=" +
					WidgetDataMrg.M_AUTH +
					"&idtype=photoid&perpage="+perpage+"&page="+page;
		
			HttpClient client = getHttpClient();
			
			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);
			StringBuilder str = new StringBuilder();
			if (response.getStatusLine().getStatusCode() == 200) {//������ж��Ƿ������
				BufferedReader buffer = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				for (String s = buffer.readLine(); s != null; s = buffer
						.readLine()) {
					str.append(s);
				}
				buffer.close();
				obj = new JSONObject(str.toString());
			}
		} catch(JSONException e){
			e.printStackTrace();
			obj = null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			obj = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			obj = null;
		}
		return obj;
	}
	
	public static JSONObject getPicDetail(int id, int uid){
		if (WidgetDataMrg.M_AUTH == null)
			return null;
		url = "http://www.familyday.com.cn/dapi/space.php?do=photo&id="+id+
				"&m_auth="+
				WidgetDataMrg.M_AUTH +
				"&uid="+uid;
		JSONObject obj = null;
		try{
			HttpClient client = getHttpClient();
			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);
			StringBuilder str = new StringBuilder();
			if (response.getStatusLine().getStatusCode() == 200) {//������ж��Ƿ������
				BufferedReader buffer = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				for (String s = buffer.readLine(); s != null; s = buffer
						.readLine()) {
					str.append(s);
				}
				buffer.close();
				obj = new JSONObject(str.toString());
			}
		}catch(Exception e){
			obj = null;
			e.printStackTrace();
		}
		return obj;
	}
	
	public static HttpClient getHttpClient(){
		
	    BasicHttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
	    HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
	    HttpClient client = new DefaultHttpClient(httpParams);
	    return client;
	}

	/**
     * 判断网络是否可用
     * @param context
     * @return 
     */
    public static boolean isNetworkConnected(Context context) {  
        if (context != null) {  
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
                    .getSystemService(Context.CONNECTIVITY_SERVICE);  
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
            if (mNetworkInfo != null) {  
                return mNetworkInfo.isAvailable();  
            }  
        }  
        return false;  
    }
    /**
     * 设置服务类型，WIFI or NO_WIFI
     */
    public static void setServiceType(Context context){
    	if (isNetworkConnected(context))
    		WidgetDataMrg.serviceType = WidgetDataMrg.WIFI_SERVICE;
    	else
    		WidgetDataMrg.serviceType = WidgetDataMrg.NO_WIFI_SERVICE;
    }
	
}
