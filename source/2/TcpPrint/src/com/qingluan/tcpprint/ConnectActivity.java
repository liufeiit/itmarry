package com.qingluan.tcpprint;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ConnectActivity extends Activity {
	private static String HOST ;
	private ListView listview_connect;
	private MyListViewAdapter adapter;
	private TextView display;
	private static final int GET = 1;
	private static final int GET_F = 0;
	private static final int GET_RESET = 10000;
	private Thread netThread ;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
		
		Bundle bundle = getIntent().getExtras();
		HOST = bundle.getString("host");
		listview_connect = (ListView)findViewById(R.id.Connect_list);
		display = (TextView)findViewById(R.id.display);
		adapter = new MyListViewAdapter();
		listview_connect.setAdapter(adapter);
		
		/*
		 * this is a test data
		 * */
		/*
		String test1= "Date";
		String test2 = HOST;
		String[] test_data = {test1 ,test2};
		String[] test3 = {"assa","asgasf"};
		adapter.addData(test_data);
		adapter.addData(test3);
		*/
		
		if (netThread == null){
			netThread = new Thread(runnable);
			netThread.start();
			
		}else{
			netThread.destroy();
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			/*
			try {
				netThread.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.connect, menu);
		return true;
	}
	
	@Override
	protected void onDestroy(){
		handle.removeCallbacks(netThread);
		super.onDestroy();
	}
	/*this is a adapter for my listview
	 * 
	 * */
	public class MyListViewAdapter extends BaseAdapter {
		ArrayList<String[]> Data = new ArrayList<String[]>();
		private Context context  = ConnectActivity.this;
		private TextView time ,connect;
		public void addData(String[] date){
			boolean isSame = false;
			for(String[] some : Data){
				if (some[0].equals(date[0]) && some[1].equals(date[1])){
					isSame = true;
				}
			}
			if (! isSame){
				Data.add(date);
				String total = "Total : "+String.valueOf(getCount());
				display.setText((CharSequence)total);
			}
			
			notifyDataSetChanged();
		}
		public void re_struct(){
			Data.clear();
			notifyDataSetChanged();
		}
		
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return Data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflate = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
			//LayoutInflater inflater = (LayoutInflater)getLayoutInflater();
			convertView = inflate.inflate(R.layout.list_item_connect, null);
			time = (TextView)convertView.findViewById(R.id.dis_time);
			connect = (TextView)convertView.findViewById(R.id.Content);
			time.setText((CharSequence)Data.get(position)[1]);
			connect.setText((CharSequence)Data.get(position)[0]);
			return convertView;
		}

	}
	/*
	class GetInfo extends AsyncTask<Object,Void,Object>{

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			String HOST = (String) params[0];
			String result = null;
			String time = null;
			int port = 50000;
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
			try{
				Socket socket = new Socket(HOST,port);
				BufferedReader br = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				result = br.readLine();
				if (result != null){
					Date date = new Date(System.currentTimeMillis());
					time = format.format(date);
					Log.d("time",time);
				}
			}	catch (IOException e){
				e.printStackTrace();
			}
			
			return new String[] { result ,time};
		}
		
		
		@Override
		protected void  onPostExecute(Object res){
			if (((String[])res)[0] != null){
				adapter.addData((String[])res);
			}
		}
		
	}  
	*/
	
	
	private  Handler handle = new Handler(){
		@Override
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			Log.d("time","update UI");
			
			String[] res = (String[])msg.obj;
			Toast.makeText(getApplicationContext(), res[0], Toast.LENGTH_SHORT).show();
			switch (msg.what){
			case GET:
				adapter.addData(res);
				break;
			case GET_F:
				//Toast.makeText(getApplication(),"some",Toast.LENGTH_SHORT).show();
				break;
			case GET_RESET:
				adapter.re_struct();
			}
			listview_connect.setSelection(adapter.getCount() -1);
		}
	};
	Runnable runnable = new Runnable(){
		@Override
		public void run(){
			String host = HOST;
			
			String time = null;
			int port = 50000;
			
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
			int num_turn = 1;
			Socket socket;
			try {
				
				socket = new Socket(host,port);
				while (true){
					Log.d("time","turn :"+String.valueOf(num_turn));
					String result = null;
					DataInputStream  din =null;
					Log.d("time","initialing");
					try{
						
						while (true){
							din = new DataInputStream (socket.getInputStream());
							Thread.sleep(1);
							if(din.available()>0){
								byte[] buffer=new byte[din.available()];
								//System.out.println(din.available());
								din.read(buffer);
								result = new String(buffer);
								if (result != null){
									
									break;
								}
							}
							continue ;
						}
						
						//BufferedReader br = new BufferedReader(
							//	new InputStreamReader(socket.getInputStream()));
						//result = br.readLine();
						if (result != null){
							Log.d("time",result);
						}
						if (result != null){
							Date date = new Date(System.currentTimeMillis());
							time = format.format(date);
							Log.d("time",time);
						}
						
						String[] res = {result,time};
						if (res[0]!= null){
							Message msg = new Message();
							msg.what = GET;
							msg.obj = res;
							handle.sendMessage(msg);
							num_turn ++;
						}else if(num_turn >= 10000){
							Message msg = new Message();
							msg.what = 10000; //this is a RESET Signal
							
						}else{
							Message msg = new Message();
							msg.what = GET_F;
							handle.sendMessage(msg);
						}
						//din.close();
						din.reset();
						result = null;
						Log.d("time","close din");
						//socket.close();
					}catch (IOException e){
						e.printStackTrace();
					}catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						
						
					}
					
				}//while end here
				
			//} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
			//	e1.printStackTrace();
			//} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	finally{//try end here
				
			}
			
		}
	};
	
	@Override
	public boolean onKeyDown(int keycode, KeyEvent keyevent){
		if (keycode == KeyEvent.KEYCODE_BACK){
			new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				
					ConnectActivity.this.finish();
					//handle.
				}
				
			};
			AlertDialog.Builder choose = new AlertDialog.Builder(this).setMessage("sure to exit?")
					.setPositiveButton("ok",new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							ConnectActivity.this.finish();
							
						}
						
					});
			choose.show();
		}
		
		return false;
		
	}

}
