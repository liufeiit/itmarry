package com.hongkong.stiqer.https;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.hongkong.stiqer.entity.Avatar;
import com.hongkong.stiqer.entity.CheckIn;
import com.hongkong.stiqer.entity.Comment;
import com.hongkong.stiqer.entity.Fav;
import com.hongkong.stiqer.entity.Feed;
import com.hongkong.stiqer.entity.FeedLike;
import com.hongkong.stiqer.entity.Friend;
import com.hongkong.stiqer.entity.FriendStatus;
import com.hongkong.stiqer.entity.MissionResult;
import com.hongkong.stiqer.entity.Noti;
import com.hongkong.stiqer.entity.NotiNum;
import com.hongkong.stiqer.entity.PSetting;
import com.hongkong.stiqer.entity.Pactivity;
import com.hongkong.stiqer.entity.Prom;
import com.hongkong.stiqer.entity.Redeem;
import com.hongkong.stiqer.entity.ReturnCheckIn;
import com.hongkong.stiqer.entity.ScanResult;
import com.hongkong.stiqer.entity.SearchFriend;
import com.hongkong.stiqer.entity.Storelist;
import com.hongkong.stiqer.entity.TokenResult;
import com.hongkong.stiqer.entity.UpdateFriend;
import com.hongkong.stiqer.entity.SUser;
import com.hongkong.stiqer.utils.Util;


public class APIManager {
	
	private static APIManager sharedInstance    =  null;
    //private String            BASEURL           =  "http://private-f924d-stiqer.apiary-proxy.com";
    //private String            BASEURL           =  "http://www.dachenzhai.com/wochang/api/";
	private String            BASEURL           =  "http://app.stiqer.com/";
    public static SUser        user;
    public static String      UserID            =  "";
    public static String      Token             =  "";
    public static String      responseString    =  "";
    public static final int   LOGIN_SUCCESS     =  1001;
	public static final int   REGISTER_SUCCESS  =  1006;
	public static final int   PROFILE_COMPLETED =  1008;
	
    public static APIManager sharedInstance()
    {
        if (sharedInstance == null)
        {
            sharedInstance = new APIManager();
        }
        return sharedInstance;
    }
    
    /**
     * 登陆方法
     * @param userName（用户名）
     * @param passWord（密码）
     * @return SUser (用户对象)
     * @throws JSONException 
     */
    
    public SUser login(String userName, String passWord, String device_token)
    {
    	JSONObject json = new JSONObject();
    	try {
			json.put("login_name", userName);
			json.put("password", passWord);
			json.put("apid",device_token);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
        String response = null;
        try
        {
        	response = postJsonData(BASEURL + "/api/rest/user/login", setToken(null, json));
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getInt("error_code")==LOGIN_SUCCESS)
            {
                UserID = jsonObject.getString("uid");
                Token = jsonObject.getString("token");
            }
            user=(SUser) Util.readJson2Entity(response, (new SUser()));
            return user;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 第三方登陆方法
     * @param userName（用户名）
     * @param passWord（密码）
     * @return SUser (用户对象)
     */
    public SUser login_third(String oauth_id , int type, String username, String profile_image_url, String device_token)
    {
    	JSONObject json = new JSONObject();
    	try {
			json.put("third_uid", oauth_id.trim());
			json.put("type", type);
			json.put("username", username);
			json.put("third_profile_img_url", profile_image_url.trim());
			json.put("apid",device_token);
		 } catch (JSONException e1) {
			e1.printStackTrace();
		  }
        String response = null;
        try
        {
            response = postJsonData(BASEURL + "/api/rest/user/login_third", setToken(null, json));
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getInt("error_code")==LOGIN_SUCCESS)
            {
                UserID = jsonObject.getString("uid");
                Token = jsonObject.getString("token");
            }
            user=(SUser) Util.readJson2Entity(response, (new SUser()));
            return user;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 注册方法
     * 
     * @param userName（用户名）
     * @param passWord（密码）
     * @param Email（邮箱）
     * @return responseJson
     */
    public SUser register(String email, String passWord, String device_token)
    {
    	JSONObject json = new JSONObject();
    	try {
			json.put("email", email);
			json.put("password", passWord);
			json.put("apid",device_token);
			
		 } catch (JSONException e1) {
			e1.printStackTrace();
		  }
        try
        {
        	String response = postJsonData(BASEURL + "/api/rest/user/register", setToken(null, json));
            return (SUser) Util.readJson2Entity(response, (new SUser()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    public int uesrLogout() {
    	JSONObject json = new JSONObject();
    	try
        {
        	String response = postJsonData(BASEURL + "/api/rest/user/logout", setToken(Token, json));
        	JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getInt("error_code");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 999;
        }
	}
    
    /**
     * 注册方法
     * @param token2 
     * @param gender 
     * @param birth_year 
     * @param birth_month 
     * @param birth_day 
     * @param userName（用户名）
     * @param passWord（密码）
     * @param Email（邮箱）
     * @return responseJson
     */
    
    public SUser lcomplete(String name, String username, int birth_day, int birth_month, int birth_year, int gender, String uid, String token)
    {
    	JSONObject json = new JSONObject();
    	try {
			json.put("name", name);
			json.put("username", username);
			json.put("birth_day", birth_day);
			json.put("birth_month", birth_month);
			json.put("birth_year", birth_year);
			json.put("gender", gender);
			if(uid.equals("")){
				json.put("uid", UserID);
				json.put("token", Token);
			}else{
				json.put("uid", uid);
				json.put("token", token);
			}
		 } catch (JSONException e1) {
			e1.printStackTrace();
		 }
        try
        {
            String response = postJsonData(BASEURL + "/api/rest/user/login_complete", setToken(null, json));
            JSONObject jsonObject = new JSONObject(response);
 
            if (jsonObject.getInt("error_code") == PROFILE_COMPLETED)
            {
                UserID = jsonObject.getString("uid");
                Token = jsonObject.getString("token");
            }
            user= (SUser) Util.readJson2Entity(response, (new SUser()));
            return user;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
   //Store list
  	public List<Storelist> getStoreList(double longitude, double latitude, int start, int offset) {
  		  JSONObject json = new JSONObject();
    	  try {
			 json.put("longitude", longitude);
			 json.put("latitude", latitude);
			 json.put("start", start);
			 json.put("offset", offset);
		  } catch (JSONException e1) {
			 e1.printStackTrace();
		  }
    	
          try
          {
        	  String response = postJsonData(BASEURL + "/api/rest/store/list", setToken(Token, json));
              JSONObject jsonObject = new JSONObject(response);
              if (jsonObject.has("store_list"))
              {
                  return (List<Storelist>) Util.readJson2EntityList(jsonObject.getString("store_list"), new Storelist());
              }
              /*
              else
              {
                  List<Storelist> list = new ArrayList<Storelist>();
                  Storelist p = new Storelist();
                  p.setError_code(jsonObject.getInt("error_code"));
                  p.setError_msg(jsonObject.getString("error_msg"));
                  list.add(p);
                  return list;
              }*/
          }
          catch (Exception e)
          {   
              e.printStackTrace();
          }
          return null;
  	   }
  	
  	   //获得	store detail
	   public JSONObject getStoreDetail(String store_id) {
		    JSONObject json = new JSONObject();
	    	try
	          {
	        	  String response = postJsonData(BASEURL + "/api/rest/store/detail/"+store_id, setToken(Token, json));
	              JSONObject jsonObject = new JSONObject(response);
	              return jsonObject;
	          }
	          catch (Exception e)
	          {   
	              e.printStackTrace();
	          }
	        return null;
		}
	   
	   //获得个人profile信息
	   public JSONObject getProfileDetail(String username) {
		   String userName = username;
		    userName =  Uri.encode(username, "utf-8");
		    Log.e("Stiqer","username="+userName);
		    JSONObject json = new JSONObject();
	        try
	        {
	           String response = postJsonData(BASEURL + "/api/rest/user/profile/"+userName, setToken(Token, json));
	           JSONObject jsonObject = new JSONObject(response);
	           return jsonObject;
	        }
	        catch (Exception e)
	        {   
	            e.printStackTrace();
	        }
			return null;
	   }
	   
	   /**
	     * 获得Feed
	     * @param  requsetURL
	     * @param  start,offset, uid, token
	     * @return response
	     * @throws Exception
	     */
		@SuppressWarnings("unchecked")
		public List<Feed> getFeedList(int start, int offset)
	    {
			JSONObject json = new JSONObject();
    	    
			try {
			   json.put("start", start);
			   json.put("offset", offset);
		    } catch (JSONException e1) {
			   e1.printStackTrace();
		    }
    	    
	        try
	        {
	            String response = postJsonData(BASEURL + "/api/rest/social/feed/list", setToken(Token, json));
	            JSONObject jsonObject = new JSONObject(response);
	            if (jsonObject.has("feed_list"))
	            {
	                return (List<Feed>) Util.readJson2EntityList(jsonObject.getString("feed_list"), new Feed());
	            }
	            else
	            {
	                List<Feed> list = new ArrayList<Feed>();
	                Feed p = new Feed();
	                p.setError_code(jsonObject.getInt("error_code"));
	                p.setError_msg(jsonObject.getString("error_msg"));
	                list.add(p);
	                return list;
	            }
	        }
	        catch (Exception e)
	        {   
	            e.printStackTrace();
	        }
	        return null;
	    }
		
		//刷新 feed
		public List<Feed> getFeedRefresh(String head_id) {
			JSONObject json = new JSONObject();
    	    try {
			   json.put("head_id", head_id);
		    } catch (JSONException e1) {
			 e1.printStackTrace();
		    }
	        try
	        {
	            String response = postJsonData(BASEURL + "/api/rest/social/feed/refresh", setToken(Token, json));
	            JSONObject jsonObject = new JSONObject(response);
	            if (jsonObject.has("feed_list"))
	            {
	                return (List<Feed>) Util.readJson2EntityList(jsonObject.getString("feed_list"), new Feed());
	            }
	            else
	            {
	                List<Feed> list = new ArrayList<Feed>();
	                Feed p = new Feed();
	                p.setError_code(jsonObject.getInt("error_code"));
	                p.setError_msg(jsonObject.getString("error_msg"));
	                list.add(p);
	                return list;
	            }
	        }
	        catch (Exception e)
	        {   
	            e.printStackTrace();
	        }
	        return null;
		}
		
		//获得好友
		public List<Friend> getFriendList() {
			JSONObject json = new JSONObject();
	        try
	        {
	            String response = postJsonData(BASEURL + "/api/rest/social/friend/list", setToken(Token, json));

	            JSONObject jsonObject = new JSONObject(response);
	            if (jsonObject.has("friend_list"))
	            {
	                return (List<Friend>) Util.readJson2EntityList(jsonObject.getString("friend_list"), new Friend());
	            }
	            else
	            {
	                List<Friend> list = new ArrayList<Friend>();
	                Friend p = new Friend();
	                p.setError_code(jsonObject.getInt("error_code"));
	                p.setError_msg(jsonObject.getString("error_msg"));
	                list.add(p);
	                return list;
	            }
	        }
	        catch (Exception e)
	        {   
	            e.printStackTrace();
	        }
			return null;
		}
		 /**
	     * 请求扫描返回结构
	     * 
	     * @param result
	     * @param longitude 
	     * @param latitude 
	     * @return hashmap
	     */
	    public ScanResult scanResult(String result, double latitude, double longitude)
	    {
	    	JSONObject json = new JSONObject();
	    	  try {
				 json.put("longitude", longitude);
				 json.put("latitude", latitude);
				 json.put("qr_string", result);
			  } catch (JSONException e1) {
				 e1.printStackTrace();
			  }
	        try
	        {
	            String response = postJsonData(BASEURL + "/api/rest/purchase/qr", setToken(Token, json));
	            return (ScanResult) Util.readJson2Entity(response, (new ScanResult()));
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	            ScanResult scanResult = new ScanResult();
	            scanResult.setError_code(999);
	            return scanResult;
	        }
	    }
	    
	    //send phone number
	    public int sendPhoneNumber(String phoneNumber) {
	    	JSONObject json = new JSONObject();
    	    try {
			   json.put("phone_number", phoneNumber);
		    } catch (JSONException e1) {
			    e1.printStackTrace();
		    }
    	    try
	        {
	            String response = postJsonData(BASEURL + "/api/rest/user/tel/submit", setToken(Token, json));
		        JSONObject jsonObject = new JSONObject(response);
		        return jsonObject.getInt("error_code");
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
				return 999;
	        }
		}
	    
	    //check the phone number
	    public int sendCheckPhoneNumber(String checkNumber){
	    	JSONObject json = new JSONObject();
    	    try {
			   json.put("veri_code", checkNumber);
		    } catch (JSONException e1) {
			    e1.printStackTrace();
		    }
    	    try
	        {
	            String response = postJsonData(BASEURL + "/api/rest/user/tel/verify", setToken(Token, json));
		        JSONObject jsonObject = new JSONObject(response);
		        return jsonObject.getInt("error_code");
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
				return 999;
	        }
	    }
	    
	    //mission scan
	    public MissionResult missionResult(String result, String mid)
	    {
	    	JSONObject json = new JSONObject();
	    	  try {
				 json.put("store_qr", result);
				 json.put("mission_id", mid);
			  } catch (JSONException e1) {
				 e1.printStackTrace();
			  }
	        try
	        {
	            String response = postJsonData(BASEURL + "/api/rest/store/mission/complete", setToken(Token, json));
	            return (MissionResult) Util.readJson2Entity(response, (new MissionResult()));
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	            MissionResult missionResult = new MissionResult();
	            missionResult.setError_code(999);
	            return missionResult;
	        }
	    }  
	  
	  // checkin第一次握手
      public ReturnCheckIn sendCheckin(String store_name, String store_id, String atString, int num_photo,String message) {
    	  JSONObject json = new JSONObject();
    	  try {
			 json.put("store_name", store_name);
			 json.put("store_id", store_id);
			 json.put("num_photo", num_photo);
			 json.put("at_users", atString);
			 json.put("msg", message);
		  } catch (JSONException e1) {
			 e1.printStackTrace();
		  }
    	   try
	        {
    		    ReturnCheckIn check = new ReturnCheckIn();
	            String response = postJsonData(BASEURL + "/api/rest/social/checkin", setToken(Token, json));
	            JSONObject jsonObject = new JSONObject(response);
	           
	            check.setError_code(jsonObject.getInt("error_code"));
	            check.setError_msg(jsonObject.getString("error_msg"));
	            JSONObject list = new JSONObject(jsonObject.getString("img_url_list"));
	            if(list.has("img_url_0")){
	            	check.setImg_url_1(list.getString("img_url_0"));
	            }
	            if(list.has("img_url_1")){
	            	check.setImg_url_2(list.getString("img_url_1"));
	            }
	            return check;
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	            ReturnCheckIn check = new ReturnCheckIn();
	            check.setError_code(999);
	            return check;
	        }
		
	  }
	    
      //添加喜欢
      public int addFav(String item_id, int item_type){
    	  JSONObject json = new JSONObject();
    	  try {
			 json.put("item_id", item_id);
			 json.put("item_type", item_type);
		  } catch (JSONException e1) {
			 e1.printStackTrace();
		  }
    	  try
          {
              String response = postJsonData(BASEURL + "/api/rest/favor/add", setToken(Token, json));
              JSONObject jsonObject = new JSONObject(response);
              return jsonObject.getInt("error_code");
          }
          catch (Exception e)
          {
             e.printStackTrace();
             return 999;
          }
      }
      
      //添加喜欢动作
  	  public FeedLike addFeedLike(String feed_id) {
  		  JSONObject json = new JSONObject();
  	      try {
			 json.put("feed_id", feed_id);
		  } catch (JSONException e1) {
			 e1.printStackTrace();
		  }
          try
          {
        	  String response = postJsonData(BASEURL + "/api/rest/social/feed/like", setToken(Token, json));
        	  return (FeedLike) Util.readJson2Entity(response, (new FeedLike()));
          }
          catch (Exception e)
          {   
              e.printStackTrace();
              FeedLike feedLike = new FeedLike();
              feedLike.setError_code(999);
              return feedLike;
          }
  	  }
      
  	//删除喜欢动作
  	  public FeedLike deleteFeedLike(String feed_id) {
  		  JSONObject json = new JSONObject();
  	      try {
			 json.put("feed_id", feed_id);
		  } catch (JSONException e1) {
			 e1.printStackTrace();
		  }
          try
          {
        	  String response = postJsonData(BASEURL + "/api/rest/social/feed/unlike", setToken(Token, json));
        	  return (FeedLike) Util.readJson2Entity(response, (new FeedLike()));
          }
          catch (Exception e)
          {   
              e.printStackTrace();
              FeedLike feedLike = new FeedLike();
              feedLike.setError_code(999);
              return feedLike;
          }
  	  }
  	  
  	  //删除好友
  	  public FriendStatus deleteFriend(String username){
  		JSONObject json = new JSONObject();
	      try {
			 json.put("to_username", username);
		  } catch (JSONException e1) {
			 e1.printStackTrace();
		  }
          try
          {
      	     String response = postJsonData(BASEURL + "/api/rest/social/friend/drop", setToken(Token, json));
      	     return (FriendStatus) Util.readJson2Entity(response, (new FriendStatus()));
          }
          catch (Exception e)
          {   
             e.printStackTrace();
             FriendStatus friendStatus = new FriendStatus();
             friendStatus.setError_code(999);
             return friendStatus;
          }
  	 }
  	
  	 public FriendStatus addFriend(String username){
  		JSONObject json = new JSONObject();
	      try {
			 json.put("to_username", username);
		  } catch (JSONException e1) {
			 e1.printStackTrace();
		  }
          try
          {
      	     String response = postJsonData(BASEURL + "/api/rest/social/friend/add", setToken(Token, json));
      	     return (FriendStatus) Util.readJson2Entity(response, (new FriendStatus()));
          }
          catch (Exception e)
          {   
             e.printStackTrace();
             FriendStatus friendStatus = new FriendStatus();
             friendStatus.setError_code(999);
             return friendStatus;
          }
  	  }
	  
	//通知回复
	  public int sendCommentReply(String message, String reply_node_id) {
			// TODO Auto-generated method stub
			return 1000;
      }
	  
	  public int sendInvitation(int is_receive, String reply_node_id) {
			// TODO Auto-generated method stub
			return 1000;
      }    
	 
	  //获得更多用户checkin
	 public List<CheckIn> getUserCheckIn(String to_uid, int start, int offset) {
		 JSONObject json = new JSONObject();
		 try {
			 json.put("start", start);
			 json.put("offset", offset);
		  } catch (JSONException e1) {
			 e1.printStackTrace();
		  }
        try
        {
            String response = postJsonData(BASEURL + "/api/rest/user/profile/"+to_uid+"/more_photo", setToken(Token, json));
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("photo_list"))
            {
                return (List<CheckIn>) Util.readJson2EntityList(jsonObject.getString("photo_list"), new CheckIn());
            }
            else
            {
                List<CheckIn> list = new ArrayList<CheckIn>();
                CheckIn p = new CheckIn();
                p.setError_code(jsonObject.getInt("error_code"));
                p.setError_msg(jsonObject.getString("error_msg"));
                list.add(p);
                return list;
            }
        }
        catch (Exception e)
        {   
            e.printStackTrace();
        }
		return null;
	 } 
	 
	 public JSONObject getComment(int type, String tid, int start, int offset) {
		 
			JSONObject json = new JSONObject();
			try {
				 json.put("item_type", type);
				 json.put("item_id", tid);
				 json.put("start", start);
				 json.put("offset", offset);
			} catch (JSONException e1) {
				 e1.printStackTrace();
			}
	        try
	        {
	        	 String response = postJsonData(BASEURL + "/api/rest/social/cmt/list", setToken(Token, json));
	             JSONObject jsonObject = new JSONObject(response);
	             return jsonObject;
	        } catch (Exception e)
	        {   
	            e.printStackTrace();
	        }
	        return null;
	 }
	//comment list
	 /*
	public List<Comment> getComment(int type, String tid, int start, int offset) {
		JSONObject json = new JSONObject();
		 try {
			 json.put("item_type", type);
			 json.put("item_id", tid);
			 json.put("start", start);
			 json.put("offset", offset);
		  } catch (JSONException e1) {
			 e1.printStackTrace();
		  }
        try
        {
            String response = postJsonData(BASEURL + "/api/rest/social/cmt/list", setToken(Token, json));
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("comment_list"))
            {
                return (List<Comment>) Util.readJson2EntityList(jsonObject.getString("comment_list"), new Comment());
            }
            else
            {
                List<Comment> list = new ArrayList<Comment>();
                Comment p = new Comment();
                p.setError_code(jsonObject.getInt("error_code"));
                list.add(p);
                return list;
            }
        }
        catch (Exception e)
        {   
            e.printStackTrace();
        }
        return null;
	}
	*/
	//添加评论
	public int addComment(int type, String tid, String comment) {
		JSONObject json = new JSONObject();
		 try {
			 json.put("item_type", type);
			 json.put("item_id", tid);
			 json.put("message", comment);
		  } catch (JSONException e1) {
			 e1.printStackTrace();
		  }
		try
        {
            String response = postJsonData(BASEURL + "/api/rest/social/cmt/add", setToken(Token, json));
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getInt("error_code");
        }
        catch (Exception e)
        {   
            e.printStackTrace();
            return 999;
        }
		
	}
	
	// 刷新token
	public TokenResult refreshToken(String uid, String token){
		JSONObject json = new JSONObject();
		 try {
			 json.put("uid", uid);
			 json.put("token", token);
		  } catch (JSONException e1) {
			 e1.printStackTrace();
		  }
		   try
	          {
	      	     String response = postJsonData(BASEURL + "/api/rest/user/login/refresh", setToken(null, json));
	      	     return (TokenResult) Util.readJson2Entity(response, (new TokenResult()));
	          }
	          catch (Exception e)
	          {   
	             e.printStackTrace();
	             TokenResult tokenResult = new TokenResult();
	             tokenResult.setError_code(999);
	             return tokenResult;
	          }
	}
	
     //个人获得记录
	  public List<Pactivity> getActivity(String to_uid, int start , int offset) {
		  JSONObject json = new JSONObject();
	      try {
			 json.put("start", start);
			 json.put("offset", offset);
		  } catch (JSONException e1) {
			 e1.printStackTrace();
		  }
        try
        {
            String response = postJsonData(BASEURL + "/api/rest/user/profile/"+to_uid+"/more_act", setToken(Token, json));
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("activity_list"))
            {
                return (List<Pactivity>) Util.readJson2EntityList(jsonObject.getString("activity_list"), new Pactivity());
            }
            else
            {
                List<Pactivity> list = new ArrayList<Pactivity>();
                Pactivity p = new Pactivity();
                p.setError_code(jsonObject.getInt("error_code"));
                p.setError_msg(jsonObject.getString("error_msg"));
                list.add(p);
                return list;
            }
         }
         catch (Exception e)
         {   
            e.printStackTrace();
         }
         return null;
	   }
		
		//个人消息
		public List<Noti> getNotiList(int start, int offset) {
			
			JSONObject json = new JSONObject();
			try {
				 json.put("start", start);
				 json.put("offset", offset);
			} catch (JSONException e1) {
				 e1.printStackTrace();
		    }
			List<Noti> list = new ArrayList<Noti>();
	        try
	        {
	            String response = postJsonData(BASEURL + "/api/rest/noti/list", setToken(Token, json));
	          
	            JSONObject jsonObject = new JSONObject(response);
	            
	            if (jsonObject.has("noti_list"))
	            {
	            	JSONArray jArray = jsonObject.getJSONArray("noti_list");
	            	for(int i=0;i<jArray.length();i++){
	            		
	            		Noti p = new Noti();
	            		JSONObject json_data = jArray.getJSONObject(i);
	            		p.setNoti_id(json_data.getString("noti_id"));
	            		p.setNoti_type(json_data.getInt("noti_type"));
	            		
	            		p.setNoti_sender_type(json_data.getInt("noti_sender_type"));
	            		p.setNoti_sender_id(json_data.getString("noti_sender_id"));
	            		p.setNoti_sender_img(json_data.getString("noti_sender_img"));
	            		p.setNoti_sender_name(json_data.getString("noti_sender_name"));
	            		
	            		p.setNoti_time(json_data.getString("noti_time"));
	            		p.setNoti_message(json_data.getString("noti_message"));
	            		
	            		p.setNoti_extra(json_data.getJSONObject("noti_extra"));
	            		list.add(p);
	            	
	            	}
	            	return list;
	            }
	            else
	            {
	                Noti p = new Noti();
	                p.setError_code(jsonObject.getInt("error_code"));
	                list.add(p);
	                return list;
	            }
	        }
	        catch (Exception e)
	        {   
	            e.printStackTrace();
	            Noti p = new Noti();
                p.setError_code(999);
                list.add(p);
                return list;
	        }
	   }	
	   
		//个人消息
		public List<Noti> getNewNoti() {
			JSONObject json = new JSONObject();
			List<Noti> list = new ArrayList<Noti>();
	        try
	        {
	            String response = postJsonData(BASEURL + "/api/rest/noti/pull", setToken(Token, json));
	          
	            JSONObject jsonObject = new JSONObject(response);
	            
	            if (jsonObject.has("noti_list"))
	            {
	            	JSONArray jArray = jsonObject.getJSONArray("noti_list");
	            	for(int i=0;i<jArray.length();i++){
	            		
	            		Noti p = new Noti();
	            		JSONObject json_data = jArray.getJSONObject(i);
	            		p.setNoti_id(json_data.getString("noti_id"));
	            		p.setNoti_type(json_data.getInt("noti_type"));
	            		
	            		p.setNoti_sender_type(json_data.getInt("noti_sender_type"));
	            		p.setNoti_sender_id(json_data.getString("noti_sender_id"));
	            		p.setNoti_sender_img(json_data.getString("noti_sender_img"));
	            		p.setNoti_sender_name(json_data.getString("noti_sender_name"));
	            		
	            		p.setNoti_time(json_data.getString("noti_time"));
	            		p.setNoti_message(json_data.getString("noti_message"));
	            		
	            		p.setNoti_extra(json_data.getJSONObject("noti_extra"));
	            		list.add(p);
	            	
	            	}
	            	return list;
	            }
	            else
	            {
	                Noti p = new Noti();
	                p.setError_code(jsonObject.getInt("error_code"));
	                list.add(p);
	                return list;
	            }
	        }
	        catch (Exception e)
	        {   
	            e.printStackTrace();
	            Noti p = new Noti();
                p.setError_code(999);
                list.add(p);
                return list;
	        }
	   }	
		
	   //获得profile setting信息
	   public PSetting getProfileSetting(){
		   JSONObject json = new JSONObject();
		   try
	          {
	      	     String response = postJsonData(BASEURL + "/api/rest/user/digest", setToken(Token, json));
	      	     return (PSetting) Util.readJson2Entity(response, (new PSetting()));
	          }
	          catch (Exception e)
	          {   
	             e.printStackTrace();
	             PSetting psetting = new PSetting();
	             psetting.setError_code(999);
	             return psetting;
	          }
	   }
	   
	   public Redeem getRedeem(String result, String pid) {
		   JSONObject json = new JSONObject();
	       try {
			  json.put("promo_id", pid);
			  json.put("store_qr", result);
		   } catch (JSONException e1) {
			  e1.printStackTrace();
		   }
	       try
	          {
	      	     String response = postJsonData(BASEURL + "/api/rest/promo/redeem", setToken(Token, json));
	      	     return (Redeem) Util.readJson2Entity(response, (new Redeem()));
	          }
	          catch (Exception e)
	          {   
	             e.printStackTrace();
	             Redeem redeem = new Redeem();
	             redeem.setError_code(999);
	             return redeem;
	          }
		}
		
       // 获得promotion 列表
		public List<Prom> GetPromList() {
			JSONObject json = new JSONObject();
	    	  try {
				 json.put("start",0);
				 json.put("offset",50);
			  } catch (JSONException e1) {
				 e1.printStackTrace();
			  }
	        try
	        {
	            String response = postJsonData(BASEURL + "/api/rest/promo/list", setToken(Token, json));
	           
	            JSONObject jsonObject = new JSONObject(response);
	            if (jsonObject.has("promo_list"))
	            {
	                return (List<Prom>) Util.readJson2EntityList(jsonObject.getString("promo_list"), new Prom());
	            }
	            else
	            {
	                List<Prom> list = new ArrayList<Prom>();
	                Prom p = new Prom();
	                p.setError_code(jsonObject.getInt("error_code"));
	                p.setError_msg(jsonObject.getString("error_msg"));
	                list.add(p);
	                return list;
	            }
	        }
	        catch (Exception e)
	        {   
	            e.printStackTrace();
	        }
	        return null;
		} 

	  //获取favorite
	   public List<Fav> getFavList() {
		   JSONObject json = new JSONObject();
	        try
	        {
	        	String response = postJsonData(BASEURL + "/api/rest/favor/list", setToken(Token, json));
	            JSONObject jsonObject = new JSONObject(response);
	            if (jsonObject.has("favor_list"))
	            {
	                return (List<Fav>) Util.readJson2EntityList(jsonObject.getString("favor_list"), new Fav());
	            }
	            else
	            {
	                List<Fav> list = new ArrayList<Fav>();
	                Fav p = new Fav();
	                p.setError_code(jsonObject.getInt("error_code"));
	                list.add(p);
	                return list;
	            }
	        }
	        catch (Exception e)
	        {   
	            e.printStackTrace();
	        }
	        return null;
	 }
	 
	 //获得通知数量
	 public NotiNum getNotiNum() {
	   JSONObject json = new JSONObject();
	   try
          {
      	     String response = postJsonData(BASEURL + "/api/rest/noti/num", setToken(Token, json));
      	     return (NotiNum) Util.readJson2Entity(response, (new NotiNum()));
          }
          catch (Exception e)
          {   
             e.printStackTrace();
             NotiNum notiNum = new NotiNum();
             notiNum.setError_code(999);
             return notiNum;
          }
	   
	 }
	 
	 //删除Notification
	 public void notiDelete(String nowNotId) {
		 JSONObject json = new JSONObject();
   	     try {
			 json.put("noti_id",nowNotId);
		  } catch (JSONException e1) {
			 e1.printStackTrace();
		  }
   	     
   	   try
       {
  	     postJsonData(BASEURL + "/api/rest/noti/del", setToken(Token, json));
  	  
       }
       catch (Exception e)
      {   
          e.printStackTrace();
       }
     }  
	 //发送message
     public int sendMessage(String to_username, String msg) {
    	 JSONObject json = new JSONObject();
   	     try {
			 json.put("to_username",to_username);
			 json.put("msg_body",msg);
		  } catch (JSONException e1) {
			 e1.printStackTrace();
		  }
    	 try
	        {
	            String response = postJsonData(BASEURL + "/api/rest/msg/send", setToken(Token, json));
	            JSONObject jsonObject = new JSONObject(response);
	            return jsonObject.getInt("error_code");
	        }
	        catch (Exception e)
	        {   
	            e.printStackTrace();
	            return 999;
	        }
		
	 }
    
    //获得好友更新
     public List<UpdateFriend> getAddFriendUpadte(List<String> checkRowfid,int type,String myRowfid) { 
    	 JSONObject json = new JSONObject();
   	     try {
			 if(type == 1 || type == 2){
				 json.put("sns_ids",Util.listToString(checkRowfid));
				 json.put("type",type);
			 }else{
				 json.put("contacts",Util.listToString(checkRowfid));
			 }
		  } catch (JSONException e1) {
			 e1.printStackTrace();
		  }
   	  String response = "";
        try
        {
        	if(type == 1 || type == 2){
        		 response = postJsonData(BASEURL + "/api/rest/lookup/sns", setToken(Token, json));
        	}else{
        		 response = postJsonData(BASEURL + "/api/rest/lookup/contact", setToken(Token, json));
        	}
           
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("reg_user_list"))
            {
                return (List<UpdateFriend>) Util.readJson2EntityList(jsonObject.getString("reg_user_list"), new UpdateFriend());
            }
            else
            {
                List<UpdateFriend> list = new ArrayList<UpdateFriend>();
                UpdateFriend p = new UpdateFriend();
                p.setError_code(jsonObject.getInt("error_code"));
                list.add(p);
                return list;
            }
        }
        catch (Exception e)
        {   
            e.printStackTrace();
            List<UpdateFriend> list = new ArrayList<UpdateFriend>();
            UpdateFriend p = new UpdateFriend();
            p.setError_code(999);
            list.add(p);
            return list;
        }
 	}
     
    //改变用户隐私设置
    public int sendUserSetting(String to_username, int is_pulling, int is_pushing) {
    	JSONObject json = new JSONObject();
  	     try {
			 json.put("is_pulling",is_pulling);
			 json.put("is_pushing",is_pushing);
			 json.put("to_username",to_username);
		 } catch (JSONException e1) {
			 e1.printStackTrace();
		 }
  	     
		try {
			String response = postJsonData(BASEURL + "/api/rest/friend/privacy/set", setToken(Token, json));
			JSONObject jsonObject = new JSONObject(response);
			return jsonObject.getInt("error_code");
			
		} catch (Exception e) {
			e.printStackTrace();
			return 999;
		}
 	 }
    
    //邀请码
    public int checkInvitation(String code) {
    	JSONObject json = new JSONObject();
 	     try {
			 json.put("invi_code",code);
		 } catch (JSONException e1) {
			 e1.printStackTrace();
		 }
 	     
		try {
			String response = postJsonData(BASEURL + "/api/rest/alpha/inv/activate", setToken(null, json));
			JSONObject jsonObject = new JSONObject(response);
			return jsonObject.getInt("error_code");
			
		} catch (Exception e) {
			e.printStackTrace();
			return 999;
		}
	}

     //删除 Item
     public int DeleteFavItem(String delete_id) {
    	JSONObject json = new JSONObject();
  	    try {
		    json.put("favor_id",delete_id);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
  	     
  	 	try {
  	 		
  	 		String response = postJsonData(BASEURL + "/api/rest/favor/delete", setToken(Token, json));
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getInt("error_code");
			
		} catch (Exception e) {
			e.printStackTrace();
			return 999;
		}
	 }
     
     //search friend
     public List<SearchFriend> getSearchFriend(String username) {
    	 JSONObject json = new JSONObject();
   	  try {
			 json.put("search_name",username);
		  } catch (JSONException e1) {
			 e1.printStackTrace();
		  }
       try
       {
           String response = postJsonData(BASEURL + "/api/rest/social/friend/search", setToken(Token, json));
          
           JSONObject jsonObject = new JSONObject(response);
           if (jsonObject.has("scored_list"))
           {
               return (List<SearchFriend>) Util.readJson2EntityList(jsonObject.getString("scored_list"), new SearchFriend());
           }
           else
           {
               List<SearchFriend> list = new ArrayList<SearchFriend>();
               SearchFriend p = new SearchFriend();
               p.setError_code(jsonObject.getInt("error_code"));
               list.add(p);
               return list;
           }
       }
       catch (Exception e)
       {   
           e.printStackTrace();
       }
       return null;
 	}
    
     public List<CheckIn> getStoreCheckIn(int checkinTypeStore, String store_id,
 			int i, int page_number) {
 		// TODO Auto-generated method stub
 		return null;
 	}
	
	 public Avatar uploadProfileImage(File file, String uid, String token){
		 Map<String, String> param = new HashMap<String, String>();
		 if(uid.equals("")){
			 param = getToken();
		 }else{
			 param.put("uid",uid);
	         param.put("token", token); 
		 }
		 
    	try {
    		String response  = postFile(BASEURL + "/api/rest/test/upload_profile_img", param ,file);
    		JSONObject jsonObject = new JSONObject(response);
    		return (Avatar) Util.readJson2Entity(response, (new Avatar()));
    		
		} catch (Exception e) {
			
			e.printStackTrace();
			Avatar avatar = new Avatar();
			avatar.setError_code(999);
            return avatar;
		}
	 }
	    
	 public int uploadCheckinImage(String url ,String filepath){
    	File f = new File(filepath);
    	try {
    		String response = postFile(url, getToken(),f);
			JSONObject jsonObject = new JSONObject(response);
			return jsonObject.getInt("error_code");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return 0;
	 }
	
    /**
     * 访问网络的公共方法-Post数据
     * @param requsetURL
     * @param date
     * @return response
     * @throws Exception
     */
	 
    private String postJsonData(String requsetURL, JSONObject json) throws Exception
    {
    	Log.e("Stiqer","input="+json.toString());
    	HttpClient client = HttpClientHelper.getHttpClient();
        HttpPost post = new HttpPost(requsetURL);
        StringEntity se = new StringEntity(json.toString(),HTTP.UTF_8);  
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        post.setEntity(se);
        HttpResponse response = client.execute(post);
        /** 返回状态 **/
        int statusCode = response.getStatusLine().getStatusCode();
        StringBuffer sb = new StringBuffer();
        JSONObject Ejson = new JSONObject();
        
        if (statusCode == HttpStatus.SC_OK)
        {
            HttpEntity entity = response.getEntity();
            if (entity != null)
            {
                InputStream instream = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(instream));
                String tempLine;
                while ((tempLine = br.readLine()) != null)
                {
                    sb.append(tempLine);
                }
            }
            post.abort();
            responseString = sb.toString();
        }else{
      	   try {
      		  Ejson.put("error_code", statusCode);
  		   } catch (JSONException e1) {
  			  e1.printStackTrace();
  		   }
      	 post.abort();
      	 responseString = Ejson.toString();
       }
        Log.e("Stiqer","responseString="+responseString);
        return responseString;
    }
    
    private String postFile(String url, Map<String, String> param, File file) throws Exception
    {
        HttpPost post = new HttpPost(url);
        HttpClient client = new DefaultHttpClient();
        MultipartEntity entity = new MultipartEntity();
        if (param != null && !param.isEmpty())
        {
            for (Map.Entry<String, String> entry : param.entrySet())
            {
                entity.addPart(entry.getKey(), new StringBody(entry.getValue()));
            }
        }
        // 添加文件参数
        if (file != null && file.exists())
        {
            System.out.println(file.length()+"vvvvvvvvvvvvvv");
            entity.addPart("image", new FileBody(file));
        }
        post.setEntity(entity);
        Log.e("Stiqer","entity="+entity.toString());
        HttpResponse response = client.execute(post);
        int stateCode = response.getStatusLine().getStatusCode();
        StringBuffer sb = new StringBuffer();
        if (stateCode == HttpStatus.SC_OK)
        {
            HttpEntity result = response.getEntity();
            if (result != null)
            {
                InputStream is = result.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String tempLine;
                while ((tempLine = br.readLine()) != null)
                {
                    sb.append(tempLine);
                }
            }
        }
        post.abort();
        responseString = sb.toString();
        Log.e("Stiqer","responseString="+responseString);
        return sb.toString();
    }
    
    private String postData(String requsetURL, Map<String, String> param) throws Exception
    {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(requsetURL);
        if (param != null && param.size() > 0)
        {
            List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(param.size());
            Set<String> keys = param.keySet();
            for (Object o : keys)
            {
                String key = (String) o;
                nameValuePairs.add(new BasicNameValuePair(key, String.valueOf(param.get(key))));
            }
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
        }
       
        
        HttpResponse response = client.execute(post);
        /** 返回状态 **/
        int statusCode = response.getStatusLine().getStatusCode();
        StringBuffer sb = new StringBuffer();
        
        if (statusCode == HttpStatus.SC_OK)
        {
            HttpEntity entity = response.getEntity();
            if (entity != null)
            {
                InputStream instream = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(instream));
                String tempLine;
                while ((tempLine = br.readLine()) != null)
                {
                    sb.append(tempLine);
                }
            }
        }
        post.abort();
        responseString = sb.toString();
        System.out.println(sb.toString());
        return sb.toString();
    }
 
    private JSONObject setToken(String token, JSONObject data) throws JSONException
    {
        if (token != null)
        {
        	data.put("uid",UserID);
        	data.put("token", Token);
        }
        return data;
    }
	
    private Map<String, String> getToken(){
    	 Map<String, String> param = new HashMap<String, String>();
         param.put("uid",UserID);
         param.put("token", Token);
         return param;
    }

	

	

}
