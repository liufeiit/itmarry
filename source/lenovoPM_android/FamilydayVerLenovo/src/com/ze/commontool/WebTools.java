/**
  * Copyright 2011 PoQoP
  * Created on 2011-7-8 下午04:43:20
  */
package com.ze.commontool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

/**
 * @author huaxin
 * WebTools.java
 */
public class WebTools {
	public final static String PIC_CACHE_PATH 
	    = Environment.getExternalStorageDirectory() + "/family/pic_cache/";
	/**
	 * 通过httpGet形式获取服务器端数据
	 * @param url，服务器端网址
	 * @return 字符串形式的数据， 可以转换成对应的格式如json, xml等
	 */
	public static String getDateByHttpClient(String url) {
		Log.i("URL", url);
		String strResult = "";
		try {
			// HttpGet连接对象
			HttpGet httpRequest = new HttpGet(url);
			// 取得HttpClient对象
			HttpClient httpClient = new DefaultHttpClient();
			
			HttpParams params = new BasicHttpParams(); 
            HttpConnectionParams.setConnectionTimeout(params, 10000); //设置连接超时
            HttpConnectionParams.setSoTimeout(params, 10000); //设置请求超时
            httpRequest.setParams(params);

			// 请求HttpClient, 取得HttpResponse
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得返回的字符串
				/*strResult = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");
				strResult = strResult.substring(strResult.indexOf("{"), 
						strResult.lastIndexOf("}") + 1);*/
				try {
					strResult = inStream2String(httpResponse.getEntity().getContent());
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			Log.i("Err",e.toString());
		}

		return strResult;
	}
	
	/**
	 * get bitmap from sns server param path, 图片地址
	 */
	public static Bitmap getBitmapFromServer(String path) {
		Log.i("getBitmapFromServer", path);
		Bitmap bm = null;
		try {
			URL url = new URL(path);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(1000);
			conn.connect();

			byte[] bt = getBytes(conn.getInputStream());
			bm = BitmapFactory.decodeByteArray(bt, 0, bt.length);
			if(bm != null){
				Bitmap temp = Bitmap.createScaledBitmap(bm, 300, 400, false);
				bm.recycle();
				return temp;
			}
			return bm;
		} catch (Exception e) {
			Log.i("Err",e.toString() + "--" + path);
			return null;
		}
	}
	
	/**
	 * get bitmap by url param path, 图片地址
	 */
	public static Bitmap getBitmapByUrl(String path) {
		Log.i("getBitmapByUrl", path);
		Bitmap bm = null;
		try {
			URL url = new URL(path);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(1000);
			conn.connect();

			byte[] bt = getBytes(conn.getInputStream());
			bm = BitmapFactory.decodeByteArray(bt, 0, bt.length);
			return bm;
		} catch (Exception e) {
			Log.i("Err",e.toString() + "--" + path);
			return null;
		}
	}

	/**
	 * 将网络输入流获取的数据转换为byte[]数组，这样对于大图片比较合适 params is, 输入流变量
	 */
	private static byte[] getBytes(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len = 0;
		while ((len = is.read(b, 0, 1024)) != -1) {
			baos.write(b, 0, len);
			baos.flush();
		}
		byte[] bytes = baos.toByteArray();
		return bytes;
	}
	
	//获得圆角图片的方法     
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx){     
             
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap     
                .getHeight(), Config.ARGB_8888);     
        Canvas canvas = new Canvas(output);     
      
        final int color = 0xff424242;     
        final Paint paint = new Paint();     
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());     
        final RectF rectF = new RectF(rect);     
      
        paint.setAntiAlias(true);     
        canvas.drawARGB(0, 0, 0, 0);     
        paint.setColor(color);     
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);     
      
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));     
        canvas.drawBitmap(bitmap, rect, rect, paint);     
      
        return output;     
    }
    
    /**
     * 加载并缓存图片
     * @param ctx
     * @param url
     */
    public static Bitmap getBitmapByUrlCache(Context ctx, String url){
    	Bitmap tempBMP = null;
    	try {
			Uri uri = FilesTool.cacheFile(url, PIC_CACHE_PATH);
			/*InputStream is = ctx.getContentResolver().openInputStream(uri);
			tempBMP = BitmapFactory.decodeStream(is);
			return (tempBMP != null)?tempBMP:getBitmapByUrlNoCache(ctx, url);*/
			tempBMP = FilesTool.scaleImage(uri.getPath());
			return tempBMP;
		} catch (Exception e) {
			deleteCache(url, PIC_CACHE_PATH);
			e.printStackTrace();
		}
    	return getBitmapByUrlNoCache(ctx, url);
    }
    
    public static Bitmap getShareBitmapByUrlCache(Context ctx, String url){
    	try {
			Uri uri = FilesTool.cacheFile(url, PIC_CACHE_PATH);
			File file = new File(uri.getPath());
			if (file.exists()) {
				BitmapFactory.Options options = new BitmapFactory.Options(); 
				options.inSampleSize = 7;
				return BitmapFactory.decodeFile(uri.getPath(),options);
			}
		} catch (Exception e) {
			deleteCache(url, PIC_CACHE_PATH);
			e.printStackTrace();
		}
    	return null;
    }
    
    
    /** 
	 * 清除空的缓存文件
	 */
	public static boolean deleteCache(String path, String saveDir){
		boolean stat = false;
		if(path == null || path.trim().equals("")){
			return stat;
		}
		if(path.lastIndexOf('.') < 0){
			return stat;
		}
		File file = new File(saveDir, MD5.MD5Encode(path)+ path.substring(path.lastIndexOf('.')));
		if(file.exists()){
			stat = file.delete();
		}
		return stat;
	}
	
    public static Bitmap getBitmapByUrlNoCache(Context ctx, String url){
    	Bitmap tempBMP = null;
    	InputStream is = null;
		BufferedInputStream bis = null;
		try{
            URL iconURL = new URL(url.trim());
        	URLConnection conn = iconURL.openConnection();
        	HttpURLConnection httpCon = (HttpURLConnection)conn;
        	httpCon.setConnectTimeout(1000*5);
        	httpCon.setRequestMethod("GET");
        	//httpCon.connect();
			if(httpCon.getResponseCode()==200){
				is = httpCon.getInputStream();
	            bis = new BufferedInputStream(is);
	            tempBMP = BitmapFactory.decodeStream(bis);
			}
		} catch (Exception e){
			e.printStackTrace();
		}finally{
			if (bis != null && is != null) {
				try {
					bis.close();
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return tempBMP;
    }
    
    /**
	 * 测试网络是否连接
	 * @param context, Activity对应的上下文变量
	 * @return isNetworkAvailable
	 */
	public static boolean isNetworkConnected(Context context){
		ConnectivityManager cm = (ConnectivityManager)context
			.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null && info.isAvailable() && info.isConnected());
	}
	
	/* 上传文件至Server的方法 */
    public static boolean uploadFile(String actionUrl, 
    		String newName, String uploadFile){
    	boolean status = false;
	    String end = "\r\n";
	    String twoHyphens = "--";
	    String boundary = "*****";
	    try{
	    	URL url =new URL(actionUrl);
	        HttpURLConnection con=(HttpURLConnection)url.openConnection();
	        /* 允许Input、Output，不使用Cache */
	        con.setDoInput(true);
	        con.setDoOutput(true);
	        con.setUseCaches(false);
	        /* 设置传送的method=POST */
	        con.setRequestMethod("POST");
	        /* setRequestProperty */
	        con.setRequestProperty("Connection", "Keep-Alive");
	        con.setRequestProperty("Charset", "UTF-8");
	        con.setRequestProperty("Content-Type",
	                           "multipart/form-data;boundary="+boundary);
	        /* 设置DataOutputStream */
	        DataOutputStream ds = 
	          new DataOutputStream(con.getOutputStream());
	        ds.writeBytes(twoHyphens + boundary + end);
	        ds.writeBytes("Content-Disposition: form-data; " +
	                      "name=\"file1\";filename=\"" +
	                      newName +"\"" + end);
	        ds.writeBytes(end);   
	
	        /* 取得文件的FileInputStream */
	        FileInputStream fStream = new FileInputStream(uploadFile);
	        /* 设置每次写入1024bytes */
	        int bufferSize = 1024;
	        byte[] buffer = new byte[bufferSize];
	
	        int length = -1;
	        /* 从文件读取数据至缓冲区 */
	        while((length = fStream.read(buffer)) != -1)
	        {
	          /* 将资料写入DataOutputStream中 */
	          ds.write(buffer, 0, length);
	        }
	        ds.writeBytes(end);
	        ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
	
	        /* close streams */
	        fStream.close();
	        ds.flush();
	
	        /* 取得Response内容 */
	        InputStream is = con.getInputStream();
	        int ch;
	        StringBuffer b =new StringBuffer();
	        while( ( ch = is.read() ) != -1 )
	        {
	          b.append( (char)ch );
	        }
	        /* 关闭DataOutputStream */
	        ds.close();
	        status = true;
      }
      catch(Exception e)
      {
    	  status = false;
    	  e.printStackTrace();
      }
      return status;
    }
    
    
    /**
     * 通过http post 上传图片
     * @param url
     * @param nameValuePairs
     */
    public static String postWithDrawable(String url, List<NameValuePair> nameValuePairs, String drawKey, Drawable drawable) {
    	Log.i("URL", url);
    	String resultStr = "";
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);

        ByteArrayOutputStream baos = null;
		
        try {
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            for(int index=0; index < nameValuePairs.size(); index++) {
                if(nameValuePairs.get(index).getName().equalsIgnoreCase("Filedata")) {
                    // If the key equals to "image", we use FileBody to transfer the data
                    entity.addPart(nameValuePairs.get(index).getName(), 
                    		new FileBody(new File (
                    				nameValuePairs.get(index).getValue())));
                } else {
                    // Normal string data
                    entity.addPart(nameValuePairs.get(index).getName(), 
                    		new StringBody(nameValuePairs.get(index).getValue(),Charset.defaultCharset()));
                }
            }
            File file = null;
            if( drawable != null ){
            	baos = new ByteArrayOutputStream();
				Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
				byte[] bytes= baos.toByteArray();
				file = byteArrayToFile(bytes);
				entity.addPart(drawKey, new FileBody(file));
			}

            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost, localContext);
            /* 若状态码为200 ok */
            if(response.getStatusLine().getStatusCode()==200){
            	/* 取出答应字符串 */
            	/*resultStr=EntityUtils.toString(response.getEntity());
            	resultStr = resultStr.substring(resultStr.indexOf("{"), 
						resultStr.lastIndexOf("}") + 1);*/
            	try {
					resultStr = inStream2String(response.getEntity().getContent());
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
            }else{
            	resultStr = "";
            }
            if (file!=null && file.exists()) {
            	file.delete();
			}
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
			if( baos != null ){
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
        return resultStr;
    }

	public static File byteArrayToFile( byte[] b){
		BufferedOutputStream stream = null;
	    File file = null;
	    try {
	      file = new File(FilesTool.ROOT_PATH + FilesTool.ICON_UPLOAD);
	           FileOutputStream fstream = new FileOutputStream(file);
	           stream = new BufferedOutputStream(fstream);
	           stream.write(b);
	       } catch (Exception e) {
	           e.printStackTrace();
	      } finally {
	          if (stream != null) {
	               try {
	                  stream.close();
	               } catch (IOException e1) {
	                  e1.printStackTrace();
	              }
	          }
	      }
	       return file;
	}
	
    /**
     * 通过http post 上传图片
     * @param url
     * @param nameValuePairs
     */
    public static String post(String url, List<NameValuePair> nameValuePairs) {
    	Log.i("POST_URL", url);
    	String resultStr = "";
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);
        try {
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            for(int index=0; index < nameValuePairs.size(); index++) {
                if(nameValuePairs.get(index).getName().equalsIgnoreCase("Filedata")) {
                    // If the key equals to "image", we use FileBody to transfer the data
                    entity.addPart(nameValuePairs.get(index).getName(), 
                    		new FileBody(new File (
                    				nameValuePairs.get(index).getValue())));
                } else {
                    // Normal string data
                    entity.addPart(nameValuePairs.get(index).getName(), 
                    		new StringBody(nameValuePairs.get(index).getValue(),Charset.defaultCharset()));
                }
            }

            //httpPost.setEntity( new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            httpPost.setEntity(entity);
            
            /*// 连接超时
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
            // 读取超时
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);*/

            HttpResponse response = httpClient.execute(httpPost, localContext);
            /* 若状态码为200 ok */
            if(response.getStatusLine().getStatusCode()==200){
            	/* 取出答应字符串 */
            	/*resultStr=EntityUtils.toString(response.getEntity());
            	resultStr = resultStr.substring(resultStr.indexOf("{"), 
						resultStr.lastIndexOf("}") + 1);*/
            	try {
					resultStr = inStream2String(response.getEntity().getContent());
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
            }else{
            	resultStr = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultStr;
    }

  //将输入流转换成字符串 
  	private static String inStream2String(InputStream is) throws Exception { 
  		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
  		byte[] buf = new byte[1024]; 
  		int len = -1; 
  		while ((len = is.read(buf)) != -1) { 
  			baos.write(buf, 0, len); 
  		} 
  		return new String(baos.toByteArray()); 
  	} 
}
