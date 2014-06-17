package com.hongkong.stiqer.utils;

import java.io.IOException;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.hongkong.stiqer.entity.Noti;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


public class Util {
	public static String           TAG  = "Stiqer";
	public static int              SCREENHEIGHT;
	public static int              SCREENWIDTH;
	public static boolean          hasLogin = false;
	public static Context          context;
	public static List<Noti>       notiList = null;
	public static JSONObject       profile = null;
	public static boolean          isAvatarChange = false;
	
	public Util(Context context)
    {
        Util.context = context;
    }
	
	public static final OnTouchListener TouchDark = new OnTouchListener()
    {
        @SuppressLint("NewApi")
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                v.setAlpha(0.7f);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
            {
                v.setAlpha(1.0f);
            }
            return false;
        }
    };
    
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
   }

   public static String changeImageUrl(String url, int from_code, int to_code){
	    String[] sizeArray = {"thumb","TINY","MED","LARGE"};
	    return url.replaceAll(sizeArray[from_code], sizeArray[to_code]);
   }
	
	public static <T> Object readJson2Entity(String json, T t)
    {
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Object entity = objectMapper.readValue(json, t.getClass());
            return entity;
        }
        catch (JsonParseException e)
        {
            e.printStackTrace();
        }
        catch (JsonMappingException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public static <T> List<?> readJson2EntityList(String json, T t)
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<Object> list = new ArrayList<Object>();
        try
        {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++)
            {
                Object pojo = mapper.readValue(array.getString(i), t.getClass());
                list.add(pojo);
            }
        }
        catch (Exception e)
        {
            try
            {
                Object entity = mapper.readValue(json, t.getClass());
                list.add(entity);
            }
            catch (JsonParseException e1)
            {
                e1.printStackTrace();
            }
            catch (JsonMappingException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            catch (IOException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return list;
    }
    
    public static boolean isConnection(){
    	ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	connectivityManager.getActiveNetworkInfo().isAvailable();
        return false;
    }
    
    public static String MD5(String str)
    {
        MessageDigest md5 = null;
        try
        {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
        
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        
        for (int i = 0; i < charArray.length; i++)
        {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++)
        {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
            {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

	public static String listToString(List<String> stringList){
        if (stringList==null) {
            return null;
        }
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            }else {
                flag=true;
            }
            result.append(string);
        }
        return result.toString();
	}
	
	public static String transTime(String format_time){
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");     
	    String nowDate = sDateFormat.format(new java.util.Date()); 

		Date d = null;
		Date nd = null;
		try {
			d = sDateFormat.parse(format_time);
			nd = sDateFormat.parse(nowDate);
		} catch (ParseException e) {
		}
		long cha_time = nd.getTime()/1000 - d.getTime()/1000;
		
        if(cha_time<0){
        	return "Just now";
        }else if(cha_time < 60){
        	if(cha_time == 1){
        		return cha_time + " second ago";
        	}else{
        		return cha_time + " seconds ago";
        	}
        }else if(cha_time < 60*60){
        	if(cha_time/60 == 1){
        		return cha_time/60 + " minute ago";
        	}else{
        		return cha_time/60 + " minutes ago";
        	}
        }else if(cha_time < 60*60*24){
        	if(cha_time/(60*60) == 1){
        		return cha_time/(60*60) + " hour ago";
        	}else{
        		return cha_time/(60*60) + " hours ago";
        	}
        }else if(cha_time < 60*60*24*7){
        	if(cha_time/(60*60*24) == 1){
        		return cha_time/(60*60*24) + " day ago";
        	}else{
        		return cha_time/(60*60*24) + " days ago";
        	}
        }else if(cha_time < 60*60*24*30){
        	if(cha_time/(60*60*24*7) == 1){
        		return cha_time/(60*60*24*7) + " week ago";
        	}else{
        		return cha_time/(60*60*24*7) + " weeks ago";
        	}
        }else if(cha_time < 60*60*24*30*12){
        	if(cha_time/(60*60*24*30) == 1){
        		return cha_time/(60*60*24*30) + " month ago";
        	}else{
        		return cha_time/(60*60*24*30) + " months ago";
        	}
        }else{
        	if(cha_time/(60*60*24*30*12) == 1){
        		return cha_time/(60*60*24*30*12) + " year ago";
        	}else{
        		return cha_time/(60*60*24*30*12) + " years ago";
        	}
        }
	}

}
