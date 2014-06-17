package com.jingtuo.android.common.os;

import java.lang.ref.WeakReference;




import com.jingtuo.android.common.CacheManager;
import com.jingtuo.android.common.utils.ImageUtils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class AsyncBitmap extends AsyncTask<BitmapInfo, Void, Bitmap> {
	
    private final WeakReference<ImageView> imageViewReference;
    
    private CacheManager cacheManager;
    
    private BitmapInfo bitmapInfo;
	
	public AsyncBitmap(CacheManager cacheManager, ImageView imageView){
		this.cacheManager = cacheManager;
		this.imageViewReference = new WeakReference<ImageView>(imageView);
	}

	@Override
	protected Bitmap doInBackground(BitmapInfo... params) {
		// TODO Auto-generated method stub
		Bitmap bitmap = null;
		if(params[0]!=null){
			if(params[0].getType()==BitmapInfo.TYPE_RES){
				
			}else if(params[0].getType()==BitmapInfo.TYPE_ASSETS){
				
			}else if(params[0].getType()==BitmapInfo.TYPE_FILE){
				bitmap = cacheManager.get(params[0].getPath());
				if(bitmap==null){
					bitmap = ImageUtils.decodeFile(params[0].getPath(), params[0].getDstWidth());
				}
			}else if(params[0].getType()==BitmapInfo.TYPE_URL){
				
			}
			this.bitmapInfo = params[0];
		}
		return bitmap;
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(isCancelled()){
			result = null;
		}
		if(imageViewReference!=null&&result!=null){
			ImageView imageView = imageViewReference.get();
			if(imageView!=null){
				if(bitmapInfo.getType()==BitmapInfo.TYPE_RES){
					
				}else if(bitmapInfo.getType()==BitmapInfo.TYPE_ASSETS){
					
				}else if(bitmapInfo.getType()==BitmapInfo.TYPE_FILE){
					cacheManager.put(bitmapInfo.getPath(), result);
				}else if(bitmapInfo.getType()==BitmapInfo.TYPE_URL){
					
				}
				imageView.setImageBitmap(result);
			}
		}
		
	}



}
