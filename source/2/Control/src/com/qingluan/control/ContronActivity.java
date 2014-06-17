package com.qingluan.control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qingluan.control.var;
import com.qingluan.control.AES;

public class ContronActivity extends Activity {
	private TextView device_name ;
	private Button calculate;
	private ListView DevList;
	private Adapter_my adapter_my;
	private static String Dev_Off = "off";
	private static String Dev_On ="on";
	private Update_Device UpDate;
	private AES encry ;
	private int STATE_CONN ;
	private String HOST = null;
	private Check_Udp udp_get = null;
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contron);
		Bundle ex = getIntent().getExtras();
		HOST = ex.getString("host");
		STATE_CONN = 1;
		DevList=  (ListView)findViewById(R.id.Deverse);
		adapter_my = new Adapter_my();
		SimpleDateFormat formatter =new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		//encry = new AES("qingluan","123");
		//AlertDialog.Builder Test = new AlertDialog.Builder(this);
		//Test.setMessage(""+encry.getString()[0]+"\n"+encry.getString()[1]).show();
		/* this is a test data
		 * 
		 * */
		ArrayList<String[]>testData = new ArrayList<String[]>();
		//Date currentDate   =   new Date(System.currentTimeMillis());
		//String time = formatter.format(currentDate);
		String[] Dev_00 = { "First deve" ,"on"};
		String[] Dev_01 = { "second Deve","off"};
		testData.add(Dev_01);
		testData.add(Dev_00);
		
		DevList.setAdapter(adapter_my);
		for (String[] item : testData ){
			
			adapter_my.AddDevive(item);
			
		}
		/*getData =  new getDataFromNet();
		getData.execute();
		*/
		udp_get = new Check_Udp();
		udp_get.execute(HOST);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contron, menu);
		return true;
	}
	
	class Adapter_my extends BaseAdapter {
		/*for Devices Integer is number ,String[0] is Device name,
		 *  String[1] is Device Statue that be represented  by String.
		 * 
		 * for Device_state  first String is device name , second String is Device Statue 
		 * all these from Devices.
		 * */
		private ArrayList<String[]> Devices = new ArrayList<String[]>();
		private Map<String,String> Device_state = new HashMap<String,String>();
		private Map<String,String> getDevTime = new HashMap<String,String>();
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Devices.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return Devices.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		public void getTime(String name ,String time){
			getDevTime.put(name, time);
		}
		/*
		 * first get information about remote device
		 * */
		public void AddDevive(String[] information){
			if(Device_state.get(information[0]) == null){
				for(int i =0 ; i <Devices.size(); i++){
					if (Devices.get(i)[0].equals(information[0])){
						Devices.remove(i);
						Device_state.remove(information[0]);
					}
				}
				Devices.add(information);
				Device_state.put(information[0],information[1]);	
				//getTime(information[0],information[2]);
				notifyDataSetChanged();
			}
		}
		
		/*
		 * to update device information
		 * */
		public void update(String name ,String statue){
			String statue_this =null;
			if (statue != null){
				statue_this= statue;
			}
			if(Device_state.get(name) != null){
				Device_state.remove(name);
				Device_state.put(name, statue_this);
				notifyDataSetChanged();
			}else if (Device_state.get(name) ==null){
				Device_state.put(name, statue_this);
				notifyDataSetChanged();
			}
			notifyDataSetChanged();
			//AlertDialog.Builder log = new AlertDialog.Builder(ContronActivity.this.getApplicationContext());
			//log.setMessage(name+"/"+statue).show();
			Log.d("some",name+statue);
			Toast.makeText(getApplicationContext(), name+"/"+statue_this, Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.control_list_itme, null);
			device_name= (TextView)convertView.findViewById(R.id.dev_name);
			calculate = (Button)convertView.findViewById(R.id.control);
			if (Devices.get(position) != null){
				device_name.setText(Devices.get(position)[0] +"/"+ Devices.get(position)[1]);
				if (Device_state.get(Devices.get(position)[0]).equals(Dev_Off)){
					calculate.setText("To Start");
					
				}else if (Device_state.get(Devices.get(position)[0]).equals(Dev_On)){
					calculate.setText("To Stop");
				}
			}
			calculate.setOnClickListener(new ButtonListener(position));
			return convertView;
		}
		
		class ButtonListener implements OnClickListener {
			int position;
			ButtonListener (int id){
				position = id;
			}
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String new_state =null;
				UpDate = new Update_Device();
				String name =  Devices.get(position)[0];
				String old_state = Devices.get(position)[1];
				if (old_state.equals("on")){
					new_state = "off";	
				}else if (old_state.equals("off")){
					new_state = "on";
				}
				JSONObject dev = new JSONObject();
				try {
					dev.put("name", name);
					dev.put("state", new_state);
					dev.put("request", "update");
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Object[] objects_js = { HOST, dev.toString()};
				UpDate.execute(objects_js);
				Toast.makeText(getApplicationContext(),String.valueOf(dev), Toast.LENGTH_SHORT).show();
				Toast.makeText(getApplicationContext(), Devices.get(position)[0], Toast.LENGTH_SHORT).show();
			}
			
		}
		
	}
	@Override
	public boolean onKeyDown(int keycode, KeyEvent keyevent){
		if (keycode == KeyEvent.KEYCODE_BACK){
			new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ContronActivity.this.finish();
				}
				
			};
			AlertDialog.Builder choose = new AlertDialog.Builder(this).setMessage("sure to exit?")
					.setPositiveButton("ok",new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							ContronActivity.this.finish();
							
						}
						
					});
			choose.show();
		}
		
		return false;
		
	}
	
	class getDataFromNet extends AsyncTask <Object,Void,Object> {
		Context context = ContronActivity.this.getApplicationContext();
		Map<String,String> DevData = new HashMap<String,String>();
		Socket socket = null;
		BufferedReader br;
		BufferedWriter bw;
		//JSONObject json = new JSONObject();
		

		@Override
		protected Object doInBackground(Object... urls) {
			
			// TODO Auto-generated method stub
			String json =null;
			try{
				if(STATE_CONN ==1){
					socket =  new Socket(HOST,50000);
					br = new BufferedReader(new InputStreamReader (socket.getInputStream()) );
				}else if (STATE_CONN ==0){
					bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()) );
				}
				//test =  br.readLine();
				if (STATE_CONN == 1){
					json = br.readLine();
					Log.d("some",json);
					STATE_CONN = 0;
				}else if (STATE_CONN == 0){
					String json2 = (String)urls[0];
					bw.write(json2);
					bw.close();
					json = br.readLine();
					Log.d("some",json);
					if (json != null){
						parseJson(json);
					}
				}
				//socket.close();
				if (json != null){
					parseJson(json);
					//DevData.put("name", json);
				}
				
			} catch (UnknownHostException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
						
			
			return DevData;
			
		}
		/*protected void onProgressUpdate(Integer... progresses) {  
			 AlertDialog.Builder loader =new  AlertDialog.Builder(context);
			 loader.setMessage("Loading ... " + progresses[0]+"%").show();
		 	
		}*/
		
		protected void onPostExecute(Object test){
			@SuppressWarnings("unchecked")
			Map<String,String>result = (Map<String,String>)test;
			
			String name = result.get("name");
			String state = result.get("state");
			
			//String test1 = (String )test;
			//AlertDialog.Builder info = new AlertDialog.Builder(context);
			//info.setMessage(test1);
			//info.show();
			if (name != null && state != null){
				String[] dev = {name,state};
				adapter_my.AddDevive(dev);
			}
			Toast.makeText(getApplicationContext(), name+"/"+state, Toast.LENGTH_SHORT).show();
		}
		
		private void parseJson(String json2) { 
			try { 
				JSONTokener jsonparse = new JSONTokener(json2);
				
				JSONObject Dev = (JSONObject) jsonparse.nextValue();
				String name = Dev.getString("name");
				String state = Dev.getString("state");
				DevData.put("name", name);
				DevData.put("state", state);
			} catch (JSONException e) { 
				System.out.println("Json parse error"); 
				e.printStackTrace(); 
			} 
		} 
		
	}
	/*This will update each_data by pressing button
	 * one-Connection-one-closing mode
	 * Object of Enter is String { HOST,JSONObject }
	 * */
	class Update_Device extends AsyncTask<Object,Void,Object>{
		Map<String,String> Temp_each = new HashMap<String,String>();
		String HOST = null;
		static final int PORT = 50000; 
		private Udp udp = null;
		@Override
		protected Object doInBackground(Object...objects){
			HOST = (String)objects[0];
			String js_data = (String)objects[1];
			try {
				udp = new Udp();
				udp.send(HOST, PORT, js_data.getBytes());
				String temp_get_js = udp.recive(HOST, PORT);
				if (parRequest(temp_get_js).equals("ok")){
					parJson(js_data);
				}else{
					Log.d("some",temp_get_js);
				}
			
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e){
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				udp.close();
			}
			return Temp_each;
		}
		public void  parJson(String OneInformation) throws JSONException{
			JSONTokener jsonParser = new JSONTokener(OneInformation);
			JSONObject object =  (JSONObject) jsonParser.nextValue();
			String dev_name = object.getString("name");
			String dev_state = object.getString("state");
			Temp_each.put("name", dev_name);
			Temp_each.put("state", dev_state);
		}
		public String parRequest(String Information)throws JSONException {
			JSONTokener jsonParser = new JSONTokener(Information);
			JSONObject object =  (JSONObject) jsonParser.nextValue();
			String value = object.getString("request");
			return value;
		}
		
		@Override
		protected void onPostExecute(Object object){
			@SuppressWarnings("unchecked")
			Map<String,String> result =  (Map<String,String>)object;
			if(result != null){
				String temp_name = (String)result.get("name");
				String temp_state = (String)result.get("state");
				String[] dev =  {temp_name,temp_state};
				adapter_my.AddDevive(dev);
			}else{
				Toast.makeText(getApplicationContext(), "No Data Update ", Toast.LENGTH_SHORT).show();
			}
		}
		

	}
	
	
	/*
	 * For initialization of Data
	 * get all Device Information 
	 * This will get a big data 
	 * then close connect
	 * 
	 * */
	class Check_Udp extends AsyncTask<Object,Void,Object> {
		private Udp udp = null;
		private ArrayList<Map<String, String>> DevData = new ArrayList<Map<String,String>>();

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			String host = (String)params[0];
			try {
				udp  = new Udp();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try{
				String[] info  ={ "request",null,null};
				JSONObject check = StringtoJson(info);
				udp.send(host, 50000, (check.toString()).getBytes());
				String ip = udp.getIp();
				
				String temp_get = udp.recive(ip, 50000);
				if (get_Json_request(temp_get).equals("ok")){
					String Info_get = udp.recive(host, 50000);
					Log.d("some",Info_get);
					paraJson(Info_get);
				}else{
					return "no";
				}
			
			} catch (IOException e){
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				udp.close();
			}
			return DevData;
		}
		
		@Override
		protected void onPostExecute(Object some){
			@SuppressWarnings("unchecked")
			ArrayList<Map<String,String>> result = (ArrayList<Map<String,String>>)some;
			
			for (Map<String,String> each_map : result){
				String name = each_map.get("name");
				String state = each_map.get("state");
				if (name != null && state != null){
					String[] dev = {name,state};
					adapter_my.AddDevive(dev);
					Toast.makeText(getApplicationContext(), name+"/"+state, Toast.LENGTH_SHORT).show();
				}
			}
			
		}
		
		public String get_Json_request(String string){
			JSONTokener jsonpar = new JSONTokener(string);
			try{
				JSONObject object = (JSONObject) jsonpar.nextValue();
				String request = object.getString("request");
				return request;
			}catch (JSONException e){
				e.printStackTrace();
			}
			return null;
		}
		
		public JSONObject StringtoJson(String[] string){
			JSONObject object= new JSONObject();
			try{
				object.put("request", string[0]);	//string[0]  is request
				object.put("name", string[1]);		//string[1] is name
				object.put("state", string[2]);			//string[2] is state
			} catch (JSONException e){
				e.printStackTrace();
			}
			return object;
		}
		@SuppressWarnings("null")
		public void  paraJson(String AllInformation) throws JSONException{
			JSONTokener jsonParser = new JSONTokener(AllInformation);
			JSONObject object =  (JSONObject) jsonParser.nextValue();
			JSONArray nameList = object.getJSONArray("namelist");
			ArrayList<String> namelist = null;
			for (int i= 0; i< nameList.length(); i++){
				namelist.add(nameList.getString(i));
			}
			JSONObject Devs = object.getJSONObject("devs");
			for (String name : namelist){
				Map<String,String> EachDev = new HashMap<String,String>();
				JSONObject each_dev = Devs.getJSONObject(name);
				String dev_name = each_dev.getString("name");
				String dev_state = each_dev.getString("state");
				EachDev.put(dev_name, dev_state);
				DevData.add(EachDev);
			}
		}
	}
	class Udp {
		private DatagramSocket dgram  = null;
		private byte buffer[] = new byte [1024];
		Map<String,String> DevData = new HashMap<String,String>();
		
		public Udp() throws SocketException{
			dgram = new DatagramSocket();
		}
		public final DatagramPacket send(final String host,final int port , final byte[] bytes )throws IOException{
			DatagramPacket dp =  new DatagramPacket(bytes,bytes.length,InetAddress.getByName(host),port);
			dgram.send(dp);
			return dp;
		}
	
		public final String recive (final String host,final int port) throws Exception{
			DatagramPacket dp = new DatagramPacket(buffer,buffer.length);
			dgram.receive(dp);
			String info  = new String(dp.getData(),0,dp.getLength());
			return info;
		}
		public void setTimeout (final int time) throws SocketException{
			dgram.setSoTimeout(time);
		}
		public final void close(){
			try{
				dgram.close();						
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		public String getIp(){
			InetAddress address =  new DatagramPacket(buffer,buffer.length).getAddress();
			
			return address.toString();
		}
		private void parseJson(String json2) { 
			try { 
				JSONTokener jsonparse = new JSONTokener(json2);
				
				JSONObject Dev = (JSONObject) jsonparse.nextValue();
				String name = Dev.getString("name");
				String state = Dev.getString("state");
				DevData.put("name", name);
				DevData.put("state", state);
				DevData.put("request", "update");
			} catch (JSONException e) { 
				System.out.println("Json parse error"); 
				e.printStackTrace(); 
			} 
		} 
	}
}
