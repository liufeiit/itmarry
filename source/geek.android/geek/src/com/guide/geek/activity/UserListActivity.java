package com.guide.geek.activity;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.galhttprequest.GalHttpRequest;
import com.galhttprequest.GalHttpRequest.GalHttpLoadTextCallBack;
import com.guide.geek.R;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.guide.geek.adapter.ListViewAdapter;
import com.guide.geek.app.AppConfig;
import com.guide.geek.ui.UserListView;
import com.guide.geek.ui.UserListView.OnRefreshListener;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnKeyListener;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;



public class UserListActivity extends Activity {
	
	private static final int CUSTOM_DIALOG = 0;

	private static final int DEFAULT_DIALOG = 1;

	private static final String TAG = "UserListActivity";
	
	private static int lastestTopicid = 0;
	private TextView main_uiNickname;
	private List<String> data;
	private Button publishTopicButton;
	private boolean isMore = false;

  
    private UserListView userList;
    private ListViewAdapter listViewAdapter;
    private List<Map<String, Object>> listItems;
  
	AlertDialog menuDialog;// menu菜单Dialog
	
	GridView menuGrid, toolbarGrid;
	View menuView;
//	定义线程
//	mThread mthread;
//	消息监听
	 Handler mHandler = new Handler() {
   		public void handleMessage(Message msg) {
   			switch (msg.what) {
   			case 1:
   				Toast.makeText(UserListActivity.this, "toast",Toast.LENGTH_SHORT).show();
   				break;
               }
              super.handleMessage(msg);
   	 }
   	};

	protected Context context = this;
	
	protected ContextWrapper ctx = this;
   	          
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_list);
		
//   	启动线程
//      mthread = new mThread();
//		mthread.start();
//		SharedPreferences sp = this.getSharedPreferences("UserInfo",  
//                MODE_PRIVATE); 
//		String username =sp.getString("USERNAME", "");
//		main_uiNickname = (TextView)findViewById(R.id.main_uiNickname);
//		main_uiNickname.setText(username);
//	
       
		userList = (UserListView) findViewById(R.id.user_list_view);
		listItems = getListItems();//getListItems();//getTopic();//
		//start here norm
	    listViewAdapter = new ListViewAdapter(UserListActivity.this, listItems,mHandler); //创建适配器	   
	    userList.setAdapter(listViewAdapter);
		//listView.setAdapter(getMenuAdapter(menu_name_array2, menu_image_array2));
	    //add  click 
	    userList.setOnItemClickListener(new OnItemClickListener() {  	  	  
            @Override  
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
                    long arg3) {  
                setTitle("点击第"+arg2+"个项目");  
            }  
        });  
	    // long click
	    userList.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {  
            
            @Override  
            public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
                menu.setHeaderTitle("长按菜单-ContextMenu");     
                menu.add(0, 0, 0, "弹出长按菜单0");  
                menu.add(0, 1, 0, "弹出长按菜单1");     
            }  
        });
	    //pull refresh
		userList.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						boolean isTrue =getMoreTopic();
//						try {
//							Thread.sleep(1000);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}						
//						 List<Map<String, Object>> listdata; 
//						 
//				        for(int i = 0; i < goodsNames.length; i++) {
//				            Map<String, Object> map = new HashMap<String, Object>(); 
////				            map.put("image", imgeIDs[i]);                //图片资源
////				            map.put("title", "名称：");                //物品标题
////				            map.put("info", goodsNames[i]);        //名称
////				            map.put("detail", goodsDetails[i]);    //详情
//				            
//				            map.put("name", "心得");                //图片资源
//				            map.put("create_time", "2013.4.30");                //物品标题
//				            map.put("content","我的心情不太好！");        //名称
//				           // map.put("detail", goodsDetails[i]);    //详情
//				            listItems.add(map);
//				            //System.out.println(listItems.toString());
//				        }    
				        
						//data.add("刷新后添加的内容");
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						listViewAdapter.notifyDataSetChanged();
						userList.onRefreshComplete();
					}

				}.execute();
			}
		});
		
		System.out.println(listItems.toString());
		
	}
	/*
	 * get more topics
	 * 
	 */
	
	private boolean getMoreTopic(){
		boolean isTrue = false;
		 // 异步请求String  
		GalHttpRequest request;  
		String requestUrl = AppConfig.API_URL + "Topic/getTopic/topicid/"+lastestTopicid;
		
		Log.i(TAG,requestUrl);
		   
	        // 第一次调用startAsynRequestString或者startAsynRequestBitmap必须在主线程调用  
	        // 因为只有在主线程中调用才可以初始化GalHttprequest内部的全局句柄Handler 
			try{
				request = GalHttpRequest.requestWithURL(this,requestUrl);
			request.startAsynRequestString(new GalHttpLoadTextCallBack()  
	        {  
	            @Override  
	            public void textLoaded(String text)  
	            {  
	            	Log.i(TAG,text);
	                // 该部分允许于UI线程  
	            	try{
	            		//清空listItems中的数据
	            		
	            		JSONObject result = new JSONObject(text);
	            	    
	            		String info = result.getString("info");
	            		int status = result.getInt("status");
	            		if(1 == status){
	            			JSONArray  data =  result.getJSONArray("data");
			                for(int i = 0; i < data.length(); i++) {
			                	 JSONObject item = data.getJSONObject(i); //每条记录又由几个Object对象组成  
			                     int uid = item.getInt("uid");// 获取对象对应的值 
			                     int topic_id = item.getInt("topic_id");// 获取对象对应的值 
			                     if(i == 0) lastestTopicid = topic_id; //记录最后更新的topic_id
			                     String username = item.getString("username");
			                     String content = item.getString("content");
			                     int content_type = item.getInt("content_type");// 获取对象对应的值
			                     String create_time = item.getString("create_time");
			                     if( username == "" ){
		   	                    	 continue;
		   	                     }
		   	                    
//		   	                     if(null == avatar){
//		   	                    	signature = "avatar.png";
//		   	                     }
			                     //Log.i("TAG", content);
			                    Map<String, Object> map = new HashMap<String, Object>(); 
			                    map.put("content", content);
			                    map.put("username", username);  
			                   // map.put("avatar", avatar);                //图片资源地址
			                    map.put("uid", uid);        //名称                   
		//	                    map.put("topicId", topicId);
			                    map.put("content_type", content_type);
			                    map.put("create_time", create_time);
			                    map.put("topic_id", topic_id);
			                    listItems.add(map);
			                 }
	            		}else{
	            			new AlertDialog.Builder(context )
					        .setTitle("服务器返回结果")
					        .setMessage(info)              
					        .setPositiveButton("确定", null)
					        .show();
	            		}
	                   
	                }catch(Exception e){
	                	Log.i(TAG, "result:请求连接网络的过程中发生错误！");
	                	e.printStackTrace(); 
	                }
	               Log.i(TAG,listItems.toString());
   
	            }

				@Override
				public void loadFailed() {
					// TODO Auto-generated method stub
					new AlertDialog.Builder(context)
			        .setTitle("获取数据失败")
			        .setMessage("从网络上获取数据失败，请检查网络连接")              
			        .setPositiveButton("确定", null)
			        .show();
				}  
	        });
			}catch(Exception e){
				e.printStackTrace(); 
			}
			Log.i(TAG,listItems.toString());
		isTrue = true;
		return isTrue;
		
	}
	
	private List<Map<String, Object>> getTopic()
    {
		
		 // 异步请求String  
		GalHttpRequest request;  
		String requestUrl = AppConfig.API_URL + "User/searchUserByUserName/123";
		String xxxx =executeHttpGet();
		Log.i(TAG,xxxx);
		try {  
			HttpGet myrequest = new HttpGet(requestUrl);
			// 先封装一个 JSON 对象
//			JSONObject param = new JSONObject();
////			param.put("name", "rarnu");
////			param.put("password", "123456");
////			// 绑定到请求 Entry
////			StringEntity se = new StringEntity(param.toString()); 
////			myrequest.setEntity(se);
			// 发送请求
			HttpResponse httpResponse = new DefaultHttpClient().execute(myrequest);
			// 得到应答的字符串，这也是一个 JSON 格式保存的数据
			String retSrc = EntityUtils.toString(httpResponse.getEntity());
			Log.i(TAG,retSrc);
//			// 生成 JSON 对象
//			JSONObject result = new JSONObject( retSrc);
//			String token = myrequest.get("token");
		} catch (Exception e) {  
		    // TODO: handle exception  
		}  
		Log.i(TAG,requestUrl);
		    try{
				request = GalHttpRequest.requestWithURL(this,requestUrl); 
				String text = request.startSyncRequestString(); 
				//System.out.println(string==null);
				Log.i(TAG,text);
			}catch(Exception e){
				e.printStackTrace();
			}
	        // 第一次调用startAsynRequestString或者startAsynRequestBitmap必须在主线程调用  
	        // 因为只有在主线程中调用才可以初始化GalHttprequest内部的全局句柄Handler 
			try{
				request = GalHttpRequest.requestWithURL(this,requestUrl);
			request.startAsynRequestString(new GalHttpLoadTextCallBack()  
	        {  
	            @Override  
	            public void textLoaded(String text)  
	            {  
	            	Log.i(TAG,text);
	                // 该部分允许于UI线程  
	            	try{
	            		//清空listItems中的数据
	            		
	            		JSONObject result = new JSONObject(text);
	            	    
	            		String info = result.getString("info");
	            		int status = result.getInt("status");
	            		if(1 == status){
	            			JSONArray  data =  result.getJSONArray("data");
			                for(int i = 0; i < data.length(); i++) {
			                	 JSONObject item = data.getJSONObject(i); //每条记录又由几个Object对象组成  
			                     int uid = item.getInt("uid");// 获取对象对应的值 
			                     int topic_id = item.getInt("topic_id");// 获取对象对应的值 
			                     String username = item.getString("username");
			                     String content = item.getString("content");
			                     int content_type = item.getInt("content_type");// 获取对象对应的值
			                     String create_time = item.getString("create_time");
			                     if( username == "" ){
		   	                    	 continue;
		   	                     }
		   	                    
//		   	                     if(null == avatar){
//		   	                    	signature = "avatar.png";
//		   	                     }
			                     //Log.i("TAG", content);
			                    Map<String, Object> map = new HashMap<String, Object>(); 
			                    map.put("content", content);
			                    map.put("username", username);  
			                   // map.put("avatar", avatar);                //图片资源地址
			                    map.put("uid", uid);        //名称                   
		//	                    map.put("topicId", topicId);
			                    map.put("content_type", content_type);
			                    map.put("create_time", create_time);
			                    map.put("topic_id", topic_id);
			                    listItems.add(map);
			                 }
	            		}else{
	            			new AlertDialog.Builder(context )
					        .setTitle("服务器返回结果")
					        .setMessage(info)              
					        .setPositiveButton("确定", null)
					        .show();
	            		}
	                   
	                }catch(Exception e){
	                	Log.i(TAG, "result:请求连接网络的过程中发生错误！");
	                	e.printStackTrace(); 
	                }
	               Log.i(TAG,listItems.toString());
    
	            }

				@Override
				public void loadFailed() {
					// TODO Auto-generated method stub
					new AlertDialog.Builder(context)
			        .setTitle("获取数据失败")
			        .setMessage("从网络上获取数据失败，请检查网络连接")              
			        .setPositiveButton("确定", null)
			        .show();
				}  
	        });
			}catch(Exception e){
				e.printStackTrace(); 
			}
			Log.i(TAG,listItems.toString());
        return listItems;               
    }
	
	
	/*
	 * 
	 * */
    private List<Map<String, Object>> getListItems() {
    	List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for(int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<String, Object>(); 
//            map.put("image", imgeIDs[i]);                //图片资源
//            map.put("title", "名称：");                //物品标题
//            map.put("info", goodsNames[i]);        //名称
//            map.put("detail", goodsDetails[i]);    //详情
            
            map.put("name", "推信");               
            map.put("create_time", "2013.5.20 15:20:12");                
            map.put("content_type", 1);
            map.put("content", "本应用是基于Android的移动社交应用！");        //名称
           // map.put("detail", goodsDetails[i]);    //详情
            listItems.add(map);
            System.out.println(map.toString());
        }    
        return listItems;
    }

	
//// 实例化抽象AsyncTask 
//   private void connURL(){     
//	   TopicAsyncTask topicAsyncTask = new TopicAsyncTask(this);    // 实例化抽象AsyncTask     
//	  // topicAsyncTask.execute(metURL.getText().toString().trim());    // 调用AsyncTask，传入url参数     
//    }  
   
   
//   //启动子线程，并你在子线程中使用Handler回调，
//   class mThread extends Thread
//   {        
//       @Override
//       public void run() {
//           // TODO Auto-generated method stub
//           super.run();
//          
//           Toast.makeText(MainActivity.this, "toast", Toast.LENGTH_SHORT).show();
//           try {
//               Thread.sleep(5000);
//               Message msg = new Message();
//               msg.what = 1;
//               mHandler.sendMessage(msg);
//           } catch (InterruptedException e) {
//               // TODO Auto-generated catch block
//               e.printStackTrace();
//           }
//           
//       }
//       
//   }
   public String executeHttpGet() {
		String result = null;
		URL url = null;
		HttpURLConnection connection = null;
		InputStreamReader in = null;
		try {
			url = new URL("http://192.168.1.100/tuixin1/index.php");
			connection = (HttpURLConnection) url.openConnection();
			in = new InputStreamReader(connection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(in);
			StringBuffer strBuffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				strBuffer.append(line);
			}
			result = strBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return result;
	}
}
