package com.guide.geek.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

/**
 * 利用多线程异步加载图片并更新视图
 * 
 * @author xfzhang
 * 
 */
public final class AsyncImageLoader {

	private LoaderThread thread;// 加载图片并发消息通知更新界面的线程
	private HashMap<String, SoftReference<Bitmap>> imageCache;// 图片对象缓存，key:图片的url
	private Handler handler;// 界面Activity的Handler对象

	public AsyncImageLoader(Handler handler) {
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
		this.handler = handler;
	}

	/**
	 * 加载图片前显示到指定的ImageView中，图片的url保存在视图对象的Tag中
	 * 
	 * @param imageView
	 *            要显示图片的视图
	 * @param defaultBitmap
	 *            加载需要显示的提示正在加载的默认图片对象
	 */
	public void loadBitmap(ImageView imageView, Bitmap defaultBitmap) {
		// 图片所对应的url,这个值在加载图片过程中很可能会被改变
		String url = (String) imageView.getTag();
		if (imageCache.containsKey(url)) {// 判断缓存中是否有
			SoftReference<Bitmap> softReference = imageCache.get(url);
			Bitmap bitmap = softReference.get();
			if (bitmap != null) {// 如果图片对象不为空，则可挂接更新视图，并返回
				imageView.setImageBitmap(bitmap);
				return;
			} else {// 如果为空，需要将其从缓存中删除（其bitmap对象已被回收释放，需要重新加载）
				Log.e("TAG", "cache bitmap is null");
				imageCache.remove(url);
			}
		}
		imageView.setImageBitmap(defaultBitmap);// 先显示一个提示正在加载的图片
		if (thread == null) {// 加载线程不存在，线程还未启动，需要新建线程并启动
			thread = new LoaderThread(imageView, url);
			thread.start();
		} else {// 如果存在，就调用线程对象去加载
			thread.load(imageView, url);
		}

	}

	/**
	 * 释放缓存中所有的Bitmap对象，并将缓存清空
	 */
	public void releaseBitmapCache() {
		if (imageCache != null) {
			for (Entry<String, SoftReference<Bitmap>> entry : imageCache.entrySet()) {
				Bitmap bitmap = entry.getValue().get();
				if (bitmap != null) {
					bitmap.recycle();// 释放bitmap对象
				}
			}
			imageCache.clear();
		}
	}

	/**
	 * 加载图片并显示的线程
	 */
	private class LoaderThread extends Thread {

		LinkedHashMap<String, ImageView> mTaskMap;// 需要加载图片并显示的图片视图对象任务链
		private boolean mIsWait;// 标识是线程是否处于等待状态

		public LoaderThread(ImageView imageView, String url) {
			mTaskMap = new LinkedHashMap<String, ImageView>();
			mTaskMap.put(url, imageView);
		}

		/**
		 * 处理某个视图的更新显示
		 * 
		 * @param imageView
		 */
		public void load(ImageView imageView, String url) {
			mTaskMap.remove(imageView);// 任务链中可能有，得先删除
			mTaskMap.put(url, imageView);// 将其添加到任务中
			if (mIsWait) {// 如果线程此时处于等待得唤醒线程去处理任务队列中待处理的任务
				synchronized (this) {// 调用对象的notify()时必须同步
					this.notify();
				}
			}
		}

		@Override
		public void run() {
			while (mTaskMap.size() > 0) {// 当队列中有数据时线程就要一直运行,一旦进入就要保证其不会跳出循环
				mIsWait = false;
				final String url  = mTaskMap.keySet().iterator().next();
				final ImageView imageView = mTaskMap.remove(url);
				if (imageView.getTag() == url) {// 判断视图有没有复用（一旦ImageView被复用，其tag值就会修改变）
					final Bitmap bitmap = Image.getHttpBitmap(url);// 此方法应该是从网络或sd卡中加载
					try {
						Thread.sleep(1000);// 模拟网络加载数据时间
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					// 将加载的图片放入缓存map中
					imageCache.put(url, new SoftReference<Bitmap>(bitmap));
					if (url == imageView.getTag()) {// 再次判断视图有没有复用
						handler.post(new Runnable() {// 通过消息机制在主线程中更新UI
							@Override
							public void run() {
								imageView.setImageBitmap(bitmap);
							}
						});
					}
				}
				if (mTaskMap.isEmpty()) {// 当任务队列中没有待处理的任务时，线程进入等待状态
					try {
						mIsWait = true;// 标识线程的状态，必须在wait()方法之前
						synchronized (this) {
							this.wait();// 保用线程进入等待状态，直到有新的任务被加入时通知唤醒
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}