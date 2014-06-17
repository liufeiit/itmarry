package com.dangf.tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Message;

public class HttpThread extends Thread{
	private HttpHandler httpHandler=new HttpHandler();;
	private String url;
	private List<NameValuePair> params;
	
	/**
	 * Post请求
	 * @return
	 */
	public HttpThread(String url,List<NameValuePair> params){
		this.url=url;
		this.params=params;
		this.start();
	}
	
	//开始线程获取数据
	public void run(){
		System.out.println("开始准备资源");
		try {
			HttpPost request=new HttpPost(url);
			request.setEntity(new UrlEncodedFormEntity(params));
			HttpClient client=new DefaultHttpClient();
			
			System.out.println("准备好资源");
			//发送请求并获取返回的InputStream
			HttpResponse response=client.execute(request);
			System.out.println("发送请求并成功获取返回资源");
			HttpEntity entity=response.getEntity();
			InputStream input=entity.getContent();
			System.out.println("成功回去返回的InputStream");
			
			BufferedReader buf=new BufferedReader(new InputStreamReader(input));
			
			String json="";	//用来接收返回的数据
			String line="";
			while((line=buf.readLine())!=null){
				json=json+line;
			}
			
			Message msg=httpHandler.obtainMessage();
			msg.obj=json;
			httpHandler.sendMessage(msg);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
}
