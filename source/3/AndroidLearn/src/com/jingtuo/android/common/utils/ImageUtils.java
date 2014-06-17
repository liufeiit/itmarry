package com.jingtuo.android.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.media.ExifInterface;
import android.provider.MediaStore;
import android.widget.ImageView;

/**
 * 一个图片是有点阵和每一点上的颜色信息组成的
 * ColorMatrix会将array转成5*4的矩阵与原来图片的每个颜色5*1的矩阵({RGBA1})进行相乘,
 * 最后获得新图片的颜色,简单的理解就是处理图片的颜色.
 * Matrix会将数组a转成3*3的矩阵与原来图片的点位置3*1的矩阵相乘,最后获得新图片的位置,
 * 简单的理解就是处理图片的形状位置.如果布局文件中的长宽设置成固定值,移动了图片的位置之后,会有部分看不见.
 * 更具体的话有待研究
 *           Matrix m = new Matrix();
             float[] a = {
            		 0, 0, 100,
            		 2, 1, 120,
            		 0, 0, 1};
             m.setValues(a);
 */
public class ImageUtils {

	/**
	 * 将图片的直角改成圆角,本方法是通过代码实现,
	 * 另一种方式：使用图片覆盖的原理,将一个中间透明四个直角不能是透明的图片盖在目标图片上面.
	 * @param bitmap
	 * @param roundRect
	 * @return
	 */
	public static Bitmap changeRightAngleToRoundedCorners(Bitmap bitmap, float roundRect) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		Paint paint = new Paint();
		Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		Rect dst = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.YELLOW);
		canvas.drawRoundRect(rectF, roundRect, roundRect, paint);
		/**
		 * Mode的常量,以下所写只是在当前环境的效果
		 * SRC_IN:会将原图片的四个直角去掉
		 * SRC_OUT:会保留原图片的四个直角,与SRC_IN相反
		 * SRC:则不会对原图片做处理
		 * DST_IN:会用画笔paint的颜色(默认是黑色)绘制圆角矩形
		 * DST:同上
		 * MULTIPLY:也可以去掉四个直角,但是必须设置画笔paint的颜色,
		 * 这种方式是在已经形成圆角图形上面再用画笔的颜色绘制一层,如果不设置画笔的颜色,而默认是黑色的,结果就形成了一个黑色圆角矩形
		 * XOR与SRC_OUT一样
		 */
		paint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	/**
	 * 获得图片的倒影图片
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getReflectionBitmap(Bitmap bitmap) {
		int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(3, -3);

		Bitmap reflectionBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				width, height, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height/2), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);
		canvas.drawBitmap(reflectionBitmap, 0, height + reflectionGap, null);
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}
	
	
	/**
	 * {bucket_id=-1928128949, orientation=null, date_modified=1970-01-16 11:08:03, 
	 * picasa_id=null, bucket_display_name=download, title=6176737036536355860, 
	 * mini_thumb_magic=5227899009342766363, mime_type=image/jpeg, _id=10, isprivate=null, 
	 * _display_name=6176737036536355860.jpg, date_added=1970-01-15 14:11:33, 
	 * description=null, _size=84416, longitude=null, latitude=null, 
	 * datetaken=2011-06-05 22:19:48, _data=/mnt/sdcard/download/6176737036536355860.jpg}
	 * @param context
	 * @return
	 */
	public static List<Map<String, String>> getLocalImage(Context context){
		ContentResolver contentResolver = context.getContentResolver();
		String selection = MediaStore.Images.Media.MIME_TYPE + "=?";
		String[] selectionArgs = {"image/jpeg"};
		Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, selection, selectionArgs, null);
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		Map<String, String> map = null;
		if (cursor != null) {
			cursor.moveToFirst();
			while (cursor.getPosition() != cursor.getCount()) {
				map = new HashMap<String, String>();
				map.put(MediaStore.Images.Media._ID, cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
				map.put(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)));
				map.put(MediaStore.Images.Media.BUCKET_ID, cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)));
				map.put(MediaStore.Images.Media.DATA, cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
				
				Long date_added = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
				map.put(MediaStore.Images.Media.DATE_ADDED, DateTimeUtils.toString(new Date(date_added), null));
				
				Long date_modified = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
				map.put(MediaStore.Images.Media.DATE_MODIFIED, DateTimeUtils.toString(new Date(date_modified), null));
				
				Long date_taken = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
				map.put(MediaStore.Images.Media.DATE_TAKEN, DateTimeUtils.toString(new Date(date_taken), null));
				
				map.put(MediaStore.Images.Media.DESCRIPTION, cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION)));
				map.put(MediaStore.Images.Media.DESCRIPTION, cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION)));
				map.put(MediaStore.Images.Media.DISPLAY_NAME, cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
				map.put(MediaStore.Images.Media.IS_PRIVATE, cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.IS_PRIVATE)));
				map.put(MediaStore.Images.Media.LATITUDE, cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE)));
				map.put(MediaStore.Images.Media.LONGITUDE, cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE)));
				map.put(MediaStore.Images.Media.MIME_TYPE, cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)));
				map.put(MediaStore.Images.Media.MINI_THUMB_MAGIC, cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MINI_THUMB_MAGIC)));
				map.put(MediaStore.Images.Media.ORIENTATION, cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION)));
				map.put(MediaStore.Images.Media.PICASA_ID, cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.PICASA_ID)));
				map.put(MediaStore.Images.Media.SIZE, cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE)) + "");
				map.put(MediaStore.Images.Media.TITLE, cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE)));
				list.add(map);
				cursor.moveToNext();
			}
			cursor.close();
		}
		return list;
	}
	
	/**
	 * 
	 * @param bitmap
	 * @param maxsize 以KB为单位
	 * @return
	 */
	public static Bitmap compressAtQuality(Bitmap bitmap, int maxsize) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
		System.out.println("old size:" + baos.toByteArray().length);
		while(true){
			if(baos.toByteArray().length > maxsize){
				if(options<=50){
					break;
					
				}
				baos.reset();
				options -= 10;// 每次都减少10
				bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
			}else{
				break;
			}
		}
		System.out.println("new size:" + baos.toByteArray().length);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(bais, null, null);
		return bitmap;
	}
	
	/**
	 * 将彩色图转换为灰度图
	 * 
	 * @param bitmap
	 *            位图
	 * @return 返回转换好的位图
	 */
	public static Bitmap convertGreyImg(Bitmap bitmap) {
		int width = bitmap.getWidth(); // 获取位图的宽
		int height = bitmap.getHeight(); // 获取位图的高

		int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		int alpha = 0xFF << 24;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int grey = pixels[width * i + j];

				int red = ((grey & 0x00FF0000) >> 16);
				int green = ((grey & 0x0000FF00) >> 8);
				int blue = (grey & 0x000000FF);

				grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
				grey = alpha | (grey << 16) | (grey << 8) | grey;
				pixels[width * i + j] = grey;
			}
		}
		Bitmap result = Bitmap.createBitmap(width, height, Config.RGB_565);
		result.setPixels(pixels, 0, width, 0, 0, width, height);
		return result;
	}

	/**
	 * 通过url地址获取位图
	 * @param urlpath
	 * @return
	 */
	public Bitmap getBitmap(String urlpath) {
		URL url = null;
		Bitmap bitmap = null;
		try {
			url = new URL(urlpath);
			HttpURLConnection conn = (HttpURLConnection) url
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	/**
	 * 修改位图的宽度和高低
	 * @param bitmap
	 * @param newWidth	如果为0,缩放比例按照高度的缩放比例
	 * @param newHeight 如果为0,缩放比例按照宽度的缩放比例
	 * @return
	 */
	public static Bitmap modifyBitmap(Bitmap bitmap, int newWidth, int newHeight) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		
		float scaleWidth = 0;
		float scaleHeight = 0;
		if(newWidth==0){
			scaleWidth = ((float) newHeight) / height;
		}else{
			scaleWidth = ((float) newWidth) / width;
		}
		if(newHeight==0){
			scaleHeight = ((float) newWidth) / width;
		}else{
			scaleHeight = ((float) newHeight) / height;
		}

		Matrix matrix = new Matrix();
		// resize the Bitmap
		matrix.postScale(scaleWidth, scaleHeight);
		// if you want to rotate the Bitmap
		// matrix.postRotate(45);

		Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, width,
				height, matrix, true);
		return result;

	}
	
	/**
	 * 
	 * @param imageView
	 * @param pathName
	 */
	public static void setImageView(ImageView imageView, String pathName) {
	    // Get the dimensions of the View
		if(imageView==null||pathName==null||pathName.equals("")){
			return ;
		}
	    int targetW = imageView.getWidth();
	    int targetH = imageView.getHeight();
	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(pathName, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;
	    // Determine how much to scale down the image
	    int scaleFactor = 1;
	    if(targetW!=0&&targetH!=0){
	    	scaleFactor = Math.min(photoW/targetW, photoH/targetH);
	    }
	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(pathName, bmOptions);
	    imageView.setImageBitmap(bitmap);
	}
	
	/**
	 * 判断图片是横向还是纵向,返回接口参加ExifInterface.ORIENTATION_xxxxx
	 * @param pathName
	 * @return
	 */
	private static int getOrientation(String pathName){
		int orientation = ExifInterface.ORIENTATION_NORMAL;
		try {
			ExifInterface exifInterface = new ExifInterface(pathName);
			orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orientation;
	}

	/**
	 * 从文件路径读取图片,按照宽度比例进行缩小
	 * @param pathName
	 * @param dstWidth
	 * @param targetH
	 * @return
	 */
	public static Bitmap decodeFile(String pathName, int dstWidth){
		if(pathName==null||pathName.equals("")){
			return null;
		}
		//获取图片的水平还是垂直
	    int orientation = getOrientation(pathName);
	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(pathName, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;
	    // Determine how much to scale down the image
	    int srcWidth;
	    int srcHeight;
    	if(orientation==ExifInterface.ORIENTATION_NORMAL||orientation==ExifInterface.ORIENTATION_ROTATE_180){//旋转180对缩放无影响
    		srcWidth = photoW;
    		srcHeight = photoH;
	    }else{
	    	srcWidth = photoH;
	    	srcHeight = photoW;
	    }
	    int inSampleSize = 1;
    	 if (srcWidth > dstWidth) {
    	        int halfWidth = srcWidth / 2;
    	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
    	        // height and width larger than the requested height and width.
    	        while ((halfWidth / inSampleSize) > dstWidth) {
    	            inSampleSize *= 2;
    	        }
    	    }
	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = inSampleSize;
	    bmOptions.inPurgeable = true;
	    Bitmap bitmap = BitmapFactory.decodeFile(pathName, bmOptions);
	    
	    //进行旋转
    	Matrix m = new Matrix();
	    if(orientation==ExifInterface.ORIENTATION_NORMAL){
	    	m.setRotate(0);
	    }else if(orientation==ExifInterface.ORIENTATION_ROTATE_90){
	    	m.setRotate(90);
	    }else if(orientation==ExifInterface.ORIENTATION_ROTATE_180){
	    	m.setRotate(180);
	    }else if(orientation==ExifInterface.ORIENTATION_ROTATE_270){
	    	m.setRotate(270);
	    }
    	bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
    	int disHeight = srcHeight * dstWidth / srcWidth;
    	
    	bitmap = Bitmap.createScaledBitmap(bitmap, dstWidth, disHeight, true);
	    return bitmap;
	}
	
	/**
	 * 获取bitmap的bytes
	 * @param bitmap
	 * @return
	 */
	public static int getBytes(Bitmap bitmap){
		int size = 0;
		if(bitmap!=null){
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			if(Bitmap.Config.ALPHA_8==bitmap.getConfig()){
				size = width*height;
			}else if(Bitmap.Config.ARGB_4444==bitmap.getConfig()){
				size = width*height * 2;
			}else if(Bitmap.Config.ARGB_8888==bitmap.getConfig()){
				size = width*height * 4;
			}else if(Bitmap.Config.RGB_565==bitmap.getConfig()){
				size = width*height * 2;
			}
		}
		return size;
	}
}
