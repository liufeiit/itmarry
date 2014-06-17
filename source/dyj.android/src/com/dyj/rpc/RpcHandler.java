package com.dyj.rpc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.phprpc.PHPRPC_Client;
import org.phprpc.util.AssocArray;
import org.phprpc.util.Cast;
import org.phprpc.util.PHPSerializer;

import com.dyj.app.Global;
import com.dyj.bean.beanLsRw;
import com.dyj.bean.beanMessage;
import com.dyj.bean.beanRwgg;
import com.dyj.tabs.LsRwTab;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class RpcHandler {
	public PHPRPC_Client client;
	public RpcClass rpc;
	public String url = "http://dyj.ntcome.com/Rpc";
	private Context context;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public RpcHandler(Context context) {
		this.context = context;
		SharedPreferences userInfo = context.getSharedPreferences("setting",
				Context.MODE_PRIVATE);
		String serverUrl = userInfo.getString("serverUrl", "");
		if (!("".equals(serverUrl))) {
			setUrl(serverUrl);
		}
	}

	public void contentRpc() {
		if (Global.CheckNetwork(context)) {
			this.client = new PHPRPC_Client(getUrl());

			try {
				this.rpc = (RpcClass) client.useService(RpcClass.class);
			} catch (Exception e) {
				Log.e("error", "无法连接到服务器！");
				//Toast.makeText(context, "无法连接到服务器！", Toast.LENGTH_LONG).show();
			}
		} else {
			Log.e("error", "无法连接到服务器！");
			//Toast.makeText(context, "无法连接网络,请检查网络设置！", Toast.LENGTH_LONG).show();
			return;
		}

	}

	public HashMap checkLogin(String user_mc, String password) {
		this.contentRpc();
		HashMap alist = rpc.checkLogin(user_mc, password);
		if (alist != null) {
			return alist;
		} else {
			return null;
		}
	}

	public beanRwgg getRwDisp(String rw_dm) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		this.contentRpc();
		HashMap<?, ?> alist = rpc.getRwDisp(rw_dm);

		if (null == alist) {
			//Log.d("=======>null", "null");
			return null;
		}
		AssocArray List = (AssocArray) alist.get("list");
		if (null == List) {
			//Log.d("=======>null", "null");
			return null;
		}
		AssocArray data;
		beanRwgg br;
		data = (AssocArray) List;
		if (data.size() > 0) {
			//Log.d("rw_dm=======", Cast.toString(data.toHashMap().get("rw_dm")));

			// 动态赋值
			Class<?> beanClass = Class.forName("com.dyj.bean.beanRwgg");
			br = (beanRwgg) beanClass.newInstance();

			Field[] fileds = beanClass.getDeclaredFields();
			for (int j = 0; j < fileds.length; j++) {
				String fieldName = fileds[j].getName();
				String fieldType = fileds[j].getType().getSimpleName();

				if (null != data.get(fieldName.toLowerCase())) {

					if (fileds[j].getType() == String.class) {
						// Log.d("fieldName is:", fieldName);
						// Log.d("fieldType is:", fieldType);
						fileds[j].setAccessible(true);

						fileds[j].set(
								br,
								Cast.toString(data.toHashMap().get(
										fieldName.toLowerCase())));
						// Log.d("value is:",fileds[j].get(br).toString());
					}
				}

			}
			return br;
		} else {
			return null;
		}
	}

	// 获取任务公告列表
	public HashMap<String, Object> getNewRwList(HashMap<String, Object> keys,int pageIndex, int size)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		this.contentRpc();
		PHPSerializer phpSerializer = new PHPSerializer();
		HashMap<?, ?> alist=null;
		try {
			alist = rpc.getNewRwList(phpSerializer.serialize(keys),pageIndex, size);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (null == alist) {
			//Log.d("=======>null", "null");
			return null;
		}
		AssocArray List = (AssocArray) alist.get("list");

		if (null == List) {
			// Log.d("=======>null", "null");
			return null;
		}
		String page = Cast.toString(alist.get("totalPages"));
		map.put("page", page);
		// Log.d("alist size==============", List.size() + "");
		ArrayList<beanRwgg> res = new ArrayList<beanRwgg>();
		AssocArray data;
		beanRwgg br;
		for (int i = 0; i < List.size(); i++) {
			data = (AssocArray) List.toArrayList().get(i);

			//Log.d("rw_dm=======", Cast.toString(data.toHashMap().get("rw_dm")));

			// 动态赋值

			Class<?> beanClass = Class.forName("com.dyj.bean.beanRwgg");
			br = (beanRwgg) beanClass.newInstance();

			Field[] fileds = beanClass.getDeclaredFields();
			for (int j = 0; j < fileds.length; j++) {
				String fieldName = fileds[j].getName();
				String fieldType = fileds[j].getType().getSimpleName();

				if (null != data.get(fieldName.toLowerCase())) {

					if (fileds[j].getType() == String.class) {
						// Log.d("fieldName is:", fieldName);
						// Log.d("fieldType is:", fieldType);
						fileds[j].setAccessible(true);

						fileds[j].set(
								br,
								Cast.toString(data.toHashMap().get(
										fieldName.toLowerCase())));
						// Log.d("value is:",fileds[j].get(br).toString());
					}
				}

			}

			res.add(br);
		}
		if (res.size() == 0) {
			return null;
		} else {
			map.put("list", res);
			return map;
		}
	}

	// 获取历史任务列表
	@SuppressLint("DefaultLocale")
	public HashMap<String, Object> getLsRwList(HashMap<String, Object> keys, int pageIndex,
			int size) throws ClassNotFoundException, IllegalArgumentException,
			IllegalAccessException, InstantiationException {
		this.contentRpc();
		PHPSerializer phpSerializer = new PHPSerializer();
		HashMap<?, ?> alist=null;
		try {
			alist = rpc.getLsRwList(phpSerializer.serialize(keys), pageIndex, size);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (null == alist) {
			//Log.d("=======>null", "null");
			return null;
		}
		AssocArray List = (AssocArray) alist.get("list");

		if (null == List) {
			//Log.d("=======>null", "null");
			return null;
		}
		String page = Cast.toString(alist.get("totalPages"));
		map.put("page", page);
		//Log.d("alist size==============", List.size() + "");
		ArrayList<beanLsRw> res = new ArrayList<beanLsRw>();
		AssocArray data;
		beanLsRw br;
		for (int i = 0; i < List.size(); i++) {
			data = (AssocArray) List.toArrayList().get(i);
			// 动态赋值

			Class<?> beanClass = Class.forName("com.dyj.bean.beanLsRw");
			br = (beanLsRw) beanClass.newInstance();

			Field[] fileds = beanClass.getDeclaredFields();
			for (int j = 0; j < fileds.length; j++) {
				String fieldName = fileds[j].getName();

				if (null != data.get(fieldName.toLowerCase())) {

					if (fileds[j].getType() == String.class) {
						fileds[j].setAccessible(true);

						fileds[j].set(
								br,
								Cast.toString(data.toHashMap().get(
										fieldName.toLowerCase())));
					}
				}

			}
			res.add(br);
		}
		if (res.size() == 0) {
			return null;
		} else {
			map.put("list", res);
			return map;
		}
	}

	public HashMap<?, ?> downRw(int user_dm, int rw_dm) {
		this.contentRpc();
		HashMap<?, ?> alist = rpc.downRw(user_dm, rw_dm);
		if (null == alist) {
			//Log.d("=======>null", "null");
			return null;
		} else {
			return alist;
		}
	}

	public HashMap<?, ?> reDownRw(int user_dm, int rw_dm) {
		this.contentRpc();
		HashMap<?, ?> alist = rpc.reDownRw(user_dm, rw_dm);
		if (null == alist) {
			//Log.d("=======>null", "null");
			return null;
		} else {
			return alist;
		}
	}

	public HashMap<?, ?> rw_submit(int rw_dm, int sgry_dm,int is_gxdlxx, String sg_wd,
			String sg_jd, String wcqk, int is_ghsb, String new_zcbh,
			String old_jsr, String old_tel, String old_cfdd, String image_name,
			byte[] image_data) {
		this.contentRpc();
		HashMap<?, ?> alist = rpc.rw_submit1(rw_dm, sgry_dm,is_gxdlxx, sg_wd, sg_jd, wcqk,
				is_ghsb, new_zcbh, old_jsr, old_tel, old_cfdd, image_name,
				image_data);
		if (null == alist) {
			//Log.d("=======>null", "null");
			return null;
		} else {
			return alist;
		}

	}
	//获取服务器的我的任务列表
	public HashMap getMyRwList(HashMap<String, Object> keys,int pageIndex,
			int size) throws IllegalArgumentException, IllegalAccessException{
		this.contentRpc();
		PHPSerializer phpSerializer = new PHPSerializer();
		HashMap<?, ?> alist=null;
		try {
			alist = rpc.myRw(phpSerializer.serialize(keys), pageIndex, size);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (alist != null) {
			return alist;
		} else {
			return null;
		}
		
	}
	//更新用户的push_user_id
	public HashMap updatePushUserId(String user_dm,String push_user_id){
		this.contentRpc();
		HashMap alist = rpc.updatePushUserId(user_dm, push_user_id);
		if (alist != null) {
			return alist;
		} else {
			return null;
		}
	}
	//退回任务
	public HashMap<String, ?> returnRw(int rw_dm){
		this.contentRpc();
		HashMap<String, ?> alist = rpc.returnRw(rw_dm);
		if (alist != null) {
			return alist;
		} else {
			return null;
		}
	}
	//返回任务统计
	public HashMap<String,?>getTotalRw(String bm_dm,long begin,long end){
		this.contentRpc();
		HashMap<String,?> alist = rpc.getTotalRw(bm_dm,begin,end);
		if (alist != null) {
			return alist;
		} else {
			return null;
		}
		
		
	}
	//获取通知列表
	public ArrayList<beanMessage> getContentList(String bm_dm,int pageIndex ,int size){
		this.contentRpc();
		HashMap<?, ?> alist=null;
		alist=rpc.getContentList(bm_dm, pageIndex, size);
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (null == alist) {
			//Log.d("=======>null", "null");
			return null;
		}
		AssocArray List = (AssocArray) alist.get("list");
		if (null == List) {
			//Log.d("=======>null", "null");
			return null;
		}
		AssocArray data;
		beanMessage br;
		ArrayList<beanMessage> res = new ArrayList<beanMessage>();
		for (int i = 0; i < List.size(); i++) {
			data = (AssocArray) List.toArrayList().get(i);

			//Log.d("rw_dm=======", Cast.toString(data.toHashMap().get("rw_dm")));

			// 动态赋值

			Class<?> beanClass;
			try {
				beanClass = Class.forName("com.dyj.bean.beanMessage");
				br = (beanMessage) beanClass.newInstance();
				Field[] fileds = beanClass.getDeclaredFields();
				for (int j = 0; j < fileds.length; j++) {
					String fieldName = fileds[j].getName();
					String fieldType = fileds[j].getType().getSimpleName();
					
					if (null != data.get(fieldName.toLowerCase())) {

						Log.d("fieldName is:", fieldName);
						Log.d("fieldType is:", fileds[j].getType().getCanonicalName());
						if(fileds[j].getType().getName().equals("int")){
							fileds[j].setAccessible(true);

							fileds[j].set(
									br,Integer.parseInt(Cast.toString(data.toHashMap().get(
											fieldName.toLowerCase()))));
							Log.d("value is:",fileds[j].get(br).toString());
							 
						}
						if (fileds[j].getType() == String.class) {
							 
							fileds[j].setAccessible(true);

							fileds[j].set(
									br,
									Cast.toString(data.toHashMap().get(
											fieldName.toLowerCase())));
							Log.d("value is:",fileds[j].get(br).toString());
						}
						
					}

				}

				res.add(br);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			
		}
		return res;
		
	}

}
