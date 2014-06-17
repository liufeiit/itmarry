package com.dodowaterfall.widget;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import com.dodowaterfall.LazyScrollView;
import com.ze.commontool.ToastUtil;
import com.ze.commontool.WebTools;
import com.ze.familydayverlenovo.MainActivity;
import com.ze.familydayverlenovo.PhotoShowActivity;
import com.ze.familydayverlenovo.PicFallViewActivity;
import com.ze.familydayverlenovo.PublishActivity;
import com.ze.familydayverlenovo.R;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class FlowView extends LinearLayout implements View.OnClickListener,
		View.OnLongClickListener {

	private AnimationDrawable loadingAnimation;
	private FlowTag flowTag;
	private Context context;
	public Bitmap bitmap;
	//private ImageLoaderTask task;
	private int columnIndex;// 图片属于第几列
	private int rowIndex;// 图片属于第几行
	private Handler viewHandler;
	
//	private TextView tvTopicNum;
	private LinearLayout llPage;
	private ImageView ivPic;
	private TextView tvDesp;
	private TextView	tvName;
	private ImageView ivJoin;

	/*public FlowView(Context c, AttributeSet attrs, int defStyle) {
		super(c, attrs, defStyle);
		this.context = c;
		Init();
	}*/

	public FlowView(Context c, AttributeSet attrs) {
		super(c, attrs);
		this.context = c;
		Init();
	}

	public FlowView(Context c) {
		super(c);
		this.context = c;
		Init();
	}

	private void Init() {
		View view = LayoutInflater.from(context).inflate(R.layout.daily_topic_waterfall_item, null);

//		tvTopicNum = (TextView)view.findViewById(R.id.tvTopicNum);
		llPage = (LinearLayout)view.findViewById(R.id.llPage);
		ivPic = (ImageView)view.findViewById(R.id.ivPic);
		tvDesp = (TextView)view.findViewById(R.id.tvDesp);
		tvName = (TextView)view.findViewById(R.id.tvName);
//		ivJoin = (ImageView)view.findViewById(R.id.ivJoin);
		addView(view);

		setOnClickListener(this);
		this.setOnLongClickListener(this);
		ivPic.setAdjustViewBounds(true);

	}

	@Override
	public boolean onLongClick(View v) {
		Log.d("FlowView", "LongClick");
		/*Toast.makeText(context, "长按：" + this.flowTag.getFlowId(),
				Toast.LENGTH_SHORT).show();*/
		return true;
	}

	@Override
	public void onClick(View v) {
		Log.d("FlowView", "Click");
		/*Toast.makeText(context, "单击：" + this.flowTag.getFlowId() + " rowIndex:" + rowIndex,
				Toast.LENGTH_SHORT).show();*/
//		ToastUtil.show(context, "onclick");
		Intent intent = new Intent();
		intent.setClass( ( (PicFallViewActivity)context ), PhotoShowActivity.class);
		intent.putExtra("waterfall", true);
		intent.putExtra("type", PhotoShowActivity.DETAIL_PIC);
		intent.putExtra("pos", flowTag.getPos());
//		intent.putExtra("array", value)
		context.startActivity(intent);
	}

	/**
	 * 加载图片
	 */
	public void LoadImage() {
		if (getFlowTag() != null) {
			Map<String, Object> map  = flowTag.getDataMap();
			if (map != null) {
				tvDesp.setText((String)map.get("subject"));
				tvName.setText((String)map.get("name"));
				llPage.setBackgroundColor(Color.parseColor("#FFFFFF"));
//				tvTopicNum.setVisibility(View.GONE);
				
//				String topicid = (String)map.get("topicid");
//				if (!topicid.equals("")) {
//					ivJoin.setVisibility(View.VISIBLE);
//					llPage.setBackgroundColor(Color.parseColor("#9ED54A"));
//					if (flowTag.getFlowId() <= 1) {
////						tvTopicNum.setVisibility(View.VISIBLE);
//					}
////					ivJoin.setOnClickListener(
////					});
//				}
			}
			new LoadImageThread().start();
		}
	}

	/**
	 * 重新加载图片
	 */
	public void Reload() {
		if (this.bitmap == null && getFlowTag() != null) {

			new ReloadImageThread().start();
		}
	}

	/**
	 * 回收内存
	 */
	public void recycle() {
		ivPic.setImageBitmap(null);
		if ((this.bitmap == null) || (this.bitmap.isRecycled()))
			return;
		this.bitmap.recycle();
		this.bitmap = null;
	}

	public FlowTag getFlowTag() {
		return flowTag;
	}

	public void setFlowTag(FlowTag flowTag) {
		this.flowTag = flowTag;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public Handler getViewHandler() {
		return viewHandler;
	}

	public FlowView setViewHandler(Handler viewHandler) {
		this.viewHandler = viewHandler;
		return this;
	}

	class ReloadImageThread extends Thread {

		@Override
		public void run() {
			if (flowTag != null) {

				BufferedInputStream buf;
				try {
					/*buf = new BufferedInputStream(flowTag.getAssetManager()
							.open(flowTag.getFileName()));*/

					/*URL url = new URL(flowTag.getFileName());
					
					buf = new BufferedInputStream(url.openConnection().getInputStream());
					bitmap = BitmapFactory.decodeStream(buf);*/
					
					bitmap = WebTools.getBitmapByUrlCache(context,flowTag.getFileName());

				} catch (Exception e) {

					e.printStackTrace();
				}

				((Activity) context).runOnUiThread(new Runnable() {
					public void run() {
						if (bitmap != null) {// 此处在线程过多时可能为null
							ivPic.setImageBitmap(bitmap);
						}
					}
				});
			}

		}
	}

	class LoadImageThread extends Thread {
		LoadImageThread() {
		}

		public void run() {

			if (flowTag != null) {

				BufferedInputStream buf;
				try {
					/*buf = new BufferedInputStream(flowTag.getAssetManager()
							.open(flowTag.getFileName()));*/
					/*URL url = new URL(flowTag.getFileName());
					
					buf = new BufferedInputStream(url.openConnection().getInputStream());
					bitmap = BitmapFactory.decodeStream(buf);*/
					
					bitmap = WebTools.getBitmapByUrlCache(context,flowTag.getFileName());
				} catch (Exception e) {

					e.printStackTrace();
				}
				// if (bitmap != null) {

				// 此处不能直接更新UI，否则会发生异常：
				// CalledFromWrongThreadException: Only the original thread that
				// created a view hierarchy can touch its views.
				// 也可以使用Handler或者Looper发送Message解决这个问题

				((Activity) context).runOnUiThread(new Runnable() {
					public void run() {
						if (bitmap != null) {// 此处在线程过多时可能为null
							int width = bitmap.getWidth();// 获取真实宽高
							int height = bitmap.getHeight();
							
							int picItemWidth = 0;
							picItemWidth = flowTag.getItemWidth() - 20 ; // 调整宽度
							LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( picItemWidth , 0);
							lp.width = picItemWidth;
//							int layoutHeight = (height * flowTag.getItemWidth())
//									/ width+5;// 调整高度
							int layoutHeight = (height*picItemWidth) / width ;
							lp.height = layoutHeight;
//							if (lp == null) {
//								lp = new LayoutParams(picItemWidth,
//										layoutHeight);
//							}
							ivPic.setLayoutParams(lp);

							ivPic.setImageBitmap(bitmap);
							measure(0, 0);
							int measuredHeigh = getMeasuredHeight();
							
							Handler h = getViewHandler();
							Message m = h.obtainMessage(flowTag.what, width,
									measuredHeigh, FlowView.this);
							h.sendMessage(m);
						}
					}
				});

				// }

			}

		}
	}
}
