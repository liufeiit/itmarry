package com.ze.familydayverlenovo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dodowaterfall.LazyScrollView;
import com.dodowaterfall.LazyScrollView.OnScrollListener;
import com.dodowaterfall.widget.FlowTag;
import com.dodowaterfall.widget.FlowView;
import com.ze.commontool.NetHelper;
import com.ze.commontool.StringTools;
import com.ze.commontool.WebTools;
import com.ze.familydayverlenovo.userinfo.UserInfoManager;
import com.ze.model.DataModel;
import com.ze.model.PhotoModel;
import com.ze.model.PhotoModel.PicInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class PicFallViewActivity extends Activity implements OnClickListener{

	private LazyScrollView waterfall_scroll;  //根视图
	private LinearLayout waterfall_container;  //内容视图
	private ArrayList<LinearLayout> waterfall_items;  //三个瀑布流
	
	private LinearLayout llLoading;
	
	private LinearLayout llBottom;
	private ImageButton btnBack;
	
	private LinearLayout llBottom2;
	private ImageButton btnLogin;
	private ImageButton btnSignin;
	
	
	
	private Display display;
	private AssetManager asset_manager;
	//private List<String> image_filenames;  //图片文件名
	private final String image_path = "images";  //图片路径
	private Handler handler;
	private int item_width; //每个瀑布流的宽度

	private int column_count = 2;// 显示列数
	private int page_count = 20;// 每次加载20张图片

	private int current_page = 0;// 当前页数
	private int last_index = 0;// 当前页数

	private int[] topIndex;  //三列瀑布流的最顶图片的Index
	private int[] bottomIndex; //三列瀑布流的最底图片的Index
	private int[] lineIndex; //三列瀑布流的最底行数的Index
	private int[] column_height;// 每列的高度

	private HashMap<Integer, String> pins;  // id 与  图片文件路径FileName 的 map表

	private int loaded_count = 0;// 已加载数量

	private HashMap<Integer, Integer>[] pin_mark = null; //每列瀑布流 的item index 与 所在高度height 的 map表

	private Context context;

	private HashMap<Integer, FlowView> iviews; //id 与  图片view 的 map表
	private int range;
	int scroll_height;  //根视图高度 屏幕高度？
	
	private List<Map<String, Object>> topicList = new ArrayList<Map<String,Object>>();

	private boolean fromLaunch = false;
	
//	public  JSONArray array;
//	public ArrayList<DataModel> arrayList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daily_topic_waterfall);

		display = this.getWindowManager().getDefaultDisplay();
		item_width = display.getWidth() / column_count; // 根据屏幕大小计算每列大小
//		item_width = item_width - 10*2 - ( column_count - 1 ) * 10;
		asset_manager = this.getAssets();

		column_height = new int[column_count];
		context = this;
		iviews = new HashMap<Integer, FlowView>();
		pins = new HashMap<Integer, String>();
		pin_mark = new HashMap[column_count];

		this.lineIndex = new int[column_count];
		this.bottomIndex = new int[column_count];
		this.topIndex = new int[column_count];

		for (int i = 0; i < column_count; i++) {
			lineIndex[i] = -1;
			bottomIndex[i] = -1;
			pin_mark[i] = new HashMap();
		}
		
		initView();
		initEvent();

	}

	private void initEvent() {
		btnBack.setOnClickListener(this);
//		btnLogin.setOnClickListener(this);
//		btnSignin.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		if (v == btnBack) {
			finish();
		}else if (v == btnLogin) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}else if (v == btnSignin) {
//			Intent intent = new Intent(this, SignInActivity.class);
//			startActivity(intent);
//			finish();
		}
//		overridePendingTransition(R.anim.zoom_enter,R.anim.zoomout_exit);
	}
	
	private void initView() {
		waterfall_scroll = (LazyScrollView) findViewById(R.id.waterfall_scroll);
		waterfall_container = (LinearLayout) this.findViewById(R.id.waterfall_container);
		
		llLoading = (LinearLayout) this.findViewById(R.id.footer_loading_layout);
		
//		llBottom = (LinearLayout) findViewById( R.id.llBottom );
		btnBack = (ImageButton) findViewById( R.id.picfall_back );
//		llBottom2 = (LinearLayout) findViewById( R.id.llBottom2 );
//		btnLogin = (ImageButton) findViewById( R.id.btnLogin );
//		btnSignin = (ImageButton) findViewById( R.id.btnSignin );
		range = waterfall_scroll.computeVerticalScrollRange();//

		waterfall_scroll.getView();
		waterfall_scroll.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onTop() {
				// 滚动到最顶端
				Log.d("LazyScroll", "Scroll to top");
				waterfall_scroll.scrollBy(0, 10);
			}

			@Override
			public void onScroll() {
			}

			@Override
			public void onBottom() {
				// 滚动到最低端
				Log.d("LazyScroll", "Scroll to Bottom");
				if (!isLoading) {
					new GetTodayTopicTask().execute();
				}
				//AddItemToContainer(++current_page, page_count);
			}

			@Override
			public void onAutoScroll(int l, int t, int oldl, int oldt) {

				// Log.d("MainActivity",
				// String.format("%d  %d  %d  %d", l, t, oldl, oldt));

				// Log.d("MainActivity", "range:" + range);
				// Log.d("MainActivity", "range-t:" + (range - t));
				scroll_height = waterfall_scroll.getMeasuredHeight();
				Log.d("MainActivity", "scroll_height:" + scroll_height);

				for (int k = 0; k < column_count; k++) {
					if (pin_mark[k].size() <= 0) {
						 return;
					}
				}
				
				if (t > oldt) {// 向下滚动
					if (t > 2 * scroll_height) {// 超过两屏幕后

						for (int k = 0; k < column_count; k++) {

							LinearLayout localLinearLayout = waterfall_items
									.get(k);

							if (pin_mark[k].get(Math.min(bottomIndex[k] + 1,
									lineIndex[k])) <= t + 3 * scroll_height) {// 最底部的图片位置小于当前t+3*屏幕高度

								((FlowView) waterfall_items.get(k)
										.getChildAt(
												Math.min(1 + bottomIndex[k],
														lineIndex[k])))
										.Reload();

								bottomIndex[k] = Math.min(1 + bottomIndex[k],
										lineIndex[k]);

							}
							Log.d("MainActivity",
									"headIndex:" + topIndex[k]
											+ "  footIndex:" + bottomIndex[k]
											+ "  headHeight:"
											+ pin_mark[k].get(topIndex[k]));
//							if (pin_mark[k].get(topIndex[k]) < t - 2
							if ( pin_mark != null && pin_mark[k] != null && pin_mark[k].get(topIndex[k]) != null 
									&& pin_mark[k].get(topIndex[k]) < t - 2
									* scroll_height) {// 未回收图片的最高位置<t-两倍屏幕高度

								int i1 = topIndex[k];
								topIndex[k]++;
								((FlowView) localLinearLayout.getChildAt(i1))
										.recycle();
								Log.d("MainActivity", "recycle,k:" + k
										+ " headindex:" + topIndex[k]);

							}
						}

					}
				} else {// 向上滚动

					for (int k = 0; k < column_count; k++) {
						LinearLayout localLinearLayout = waterfall_items.get(k);
						if (pin_mark[k].get(bottomIndex[k]) > t + 3
								* scroll_height) {
							((FlowView) localLinearLayout
									.getChildAt(bottomIndex[k])).recycle();

							bottomIndex[k]--;
						}

						if (pin_mark[k].get(Math.max(topIndex[k] - 1, 0)) >= t
								- 2 * scroll_height) {
							((FlowView) localLinearLayout.getChildAt(Math.max(
									-1 + topIndex[k], 0))).Reload();
							topIndex[k] = Math.max(topIndex[k] - 1, 0);
						}
					}

				}
				if( waterfall_scroll.getScrollY() == 0 )
				{
					waterfall_scroll.scrollBy(0, 5);
				}
			}
		});

		handler = new Handler() {

			@Override
			public void dispatchMessage(Message msg) {
				super.dispatchMessage(msg);
			}

			@Override
			public void handleMessage(Message msg) {
				// super.handleMessage(msg);
				switch (msg.what) {
				case 1:

					FlowView v = (FlowView) msg.obj;
					int w = msg.arg1;
					int h = msg.arg2;
					// Log.d("MainActivity",
					// String.format(
					// "获取实际View高度:%d,ID：%d,columnIndex:%d,rowIndex:%d,filename:%s",
					// v.getHeight(), v.getId(), v
					// .getColumnIndex(), v.getRowIndex(),
					// v.getFlowTag().getFileName()));
					String f = v.getFlowTag().getFileName();

					// 此处计算列值
					int columnIndex = GetMinValue(column_height);

					v.setColumnIndex(columnIndex);

					column_height[columnIndex] += h;

					pins.put(v.getId(), f);
					iviews.put(v.getId(), v);
//					android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) v.getLayoutParams();
//					params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, 10);
//					v.setLayoutParams(params);
					waterfall_items.get(columnIndex).addView(v);

					lineIndex[columnIndex]++;

					pin_mark[columnIndex].put(lineIndex[columnIndex],
							column_height[columnIndex]);
					bottomIndex[columnIndex] = lineIndex[columnIndex];
					break;
				}

			}

			@Override
			public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
				return super.sendMessageAtTime(msg, uptimeMillis);
			}
		};

		waterfall_items = new ArrayList<LinearLayout>();

		for (int i = 0; i < column_count; i++) {
			LinearLayout itemLayout = new LinearLayout(this);
			LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(
					item_width, LayoutParams.WRAP_CONTENT);
			//itemLayout.setPadding(2, 2, 2, 2);
			itemLayout.setOrientation(LinearLayout.VERTICAL);
			if( i <= column_count - 1 )
			{
				itemParam.setMargins(0, 10, 0, 0);
			}else
			{
				itemParam.setMargins(0, 10, 0, 0);
			}
			itemLayout.setLayoutParams(itemParam);
			waterfall_items.add(itemLayout);
			waterfall_container.addView(itemLayout);
		}

		// 加载所有图片路径

		new GetTodayTopicTask().execute();
		
		/*try {
			image_filenames = Arrays.asList(asset_manager.list(image_path));

		} catch (IOException e) {
			e.printStackTrace();
		}
		// 第一次加载
		AddItemToContainer(current_page, page_count);*/
	}

	private void AddItemToContainer(int pageindex, int pagecount) {
		/*int currentIndex = pageindex * pagecount;

		int imagecount = topicList.size(); //10000;// image_filenames.size();
		for (int i = currentIndex; i < pagecount * (pageindex + 1)
				&& i < imagecount; i++) {
			loaded_count++;
			Random rand = new Random();
			int r = rand.nextInt(image_filenames.size());
			AddImage(image_filenames.get(r),
					(int) Math.ceil(loaded_count / (double) column_count),
					loaded_count);
			AddImage(topicList.get(i),
					(int) Math.ceil(loaded_count / (double) column_count),
					loaded_count);
		}*/
		
		int currentIndex = last_index;

		int imagecount = topicList.size(); //10000;// image_filenames.size();
		for (int i = currentIndex; i < imagecount; i++) {
			loaded_count++;
			AddImage(topicList.get(i),
					(int) Math.ceil(loaded_count / (double) column_count),
					loaded_count, i);
		}
		
		last_index = imagecount;

	}
	
	private void AddImage(final Map<String, Object> map, int rowIndex, int id , int pos) {

		FlowView item = new FlowView(context);
		// item.setColumnIndex(columnIndex);

		item.setRowIndex(rowIndex);
		item.setId(id);
		item.setViewHandler(this.handler);
		// 多线程参数
		FlowTag param = new FlowTag();
		param.setFlowId(id);
		param.setAssetManager(asset_manager);
		param.setFileName((String)map.get("image_1"));/*image_path + "/" + */
		param.setItemWidth(item_width);
		param.setDataMap(map);
		param.setViewPos(pos);
		if (id <= 1) {
			param.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!fromLaunch) {
//						Intent intent = new Intent(context, CustomCameraActivity.class);
//						intent.putExtra("pidtype", "photoid");
//						intent.putExtra("topicid", (String)map.get("topicid"));
//						intent.putExtra("fromTopic", true);
//						startActivity(intent);
						
						/*Intent intent = new Intent(context, PublishActivity.class);
						intent.putExtra("idtype", "photoid");
						intent.putExtra("topicid", (String)map.get("topicid"));
						intent.putExtra("fromTopic", true);
						context.startActivity(intent);*/
					}else {
						Intent intent = new Intent(context, LoginActivity.class);
						startActivity(intent);
					}
					finish();
//					overridePendingTransition(R.anim.zoom_enter,R.anim.zoomout_exit);
				}
			});
		}
		

		item.setFlowTag(param);
		item.LoadImage();
		// waterfall_items.get(columnIndex).addView(item);

	}
	
	
	 public void deleteItems(FlowView v) {
         int rowIndex = v.getRowIndex();
         int columnIndex = v.getColumnIndex();

         int height = v.getHeight();
         waterfall_items.get(columnIndex).removeView(v);
         this.pin_mark[columnIndex].remove(rowIndex);
         for (int i = rowIndex; i < pin_mark[columnIndex].size(); i++) {
                 this.pin_mark[columnIndex].put(i,
                                 this.pin_mark[columnIndex].get(i + 1) - height);
                 this.pin_mark[columnIndex].remove(i + 1);
                 ((FlowView) this.waterfall_items.get(columnIndex).getChildAt(i))
                                 .setRowIndex(i);
         }

         lineIndex[columnIndex]--;
         column_height[columnIndex] -= height;
         if (this.bottomIndex[columnIndex] > this.lineIndex[columnIndex]) {
                 bottomIndex[columnIndex]--;
         }
 }


	private int GetMinValue(int[] array) {
		int m = 0;
		int length = array.length;
		for (int i = 0; i < length; ++i) {

			if (array[i] < array[m]) {
				m = i;
			}
		}
		return m;
	}
	
	private int page = 1;
	private boolean isLoading = false;
	private boolean isOver = false;
	private String nexttopicid = "";
	private String topicid = "";
	
	class GetTodayTopicTask extends AsyncTask<String, Void, Bitmap>{
		String resultStr;  
		List<Map<String, Object>> tempList;
		protected void onPreExecute() {
		    isLoading = true;
		    if (!nexttopicid.equals("")) {
		    	page = 1;
		    	topicid = nexttopicid;
			}
			super.onPreExecute();
		}
		
		protected Bitmap doInBackground(String... params) {
//			resultStr = WebTools.getDateByHttpClient(Urls.getTodayTopic(page, topicid));
//			int fallPage = 1;
			
			resultStr = 
//			NetHelper.getResponByHttpClient("http://www.familyday.com.cn/dapi/space.php?do=topic&topicid=&perpage=20&page=0&m_auth="
//					+ UserInfoManager.getInstance(PicFallViewActivity.this).getItem("m_auth").getValue());
					
					NetHelper.getResponByHttpClient( getString(R.string.http_picfalldata),
						 UserInfoManager.getInstance(PicFallViewActivity.this).getItem("m_auth").getValue(),
						 page + "" );
			
			if ( page == 1 ) {
				((FamilyDayVerPMApplication)getApplication()).wf_arrayList = new ArrayList<DataModel>();
				((FamilyDayVerPMApplication)getApplication()).wf_array		= new JSONArray();
			}
			if(resultStr != null && !"".equals(resultStr.trim())){
				try {
					JSONObject resultData = new JSONObject(resultStr);
					int error = resultData.getInt("error");
					if(error == 0){
//						JSONObject data = resultData.getJSONObject("data");
//						nexttopicid = data.getString("nexttopicid");
						
						tempList = new ArrayList<Map<String, Object>>();
//						if (page == 1) {
//							Map<String, Object> topicItem;
//							topicItem = new HashMap<String, Object>();
////							topicItem.put("topicid", data.getString("topicid"));
////							topicItem.put("image_1", data.getString("pic")+"!210");
////							topicItem.put("subject", data.getString("subject"));
////							topicItem.put("message", data.getString("message"));
//							topicItem.put("picid", data.getString("id"));
//							topicItem.put("image_1", data.getString("image_1"));
//							topicItem.put("uid", data.getString("uid"));
//							topicItem.put("subject", data.getString("subject"));
//							topicItem.put("name", data.getString("name"));
//							tempList.add(topicItem);
//							
//							WebTools.getBitmapByUrlCache(context,StringTools.parseUrltoFallView(data.getString("image_1")));
//						}
						
						JSONArray contentArr = resultData.getJSONArray("data");
						int num = contentArr.length();
						JSONObject temp;
						Map<String, Object> item;
						DataModel model ;
						JSONObject object ;
						ArrayList<PicInfo> picInfoArrayList;
						PicInfo picInfo;
						int picnum = 0;
						for(int i=0; i<num; i++){
							temp = contentArr.getJSONObject(i);
							item = new HashMap<String, Object>();
							model = new DataModel();
							object = new JSONObject();
							model.type = "photoid";
							item.put("picid", temp.getString("id"));
							model.id = temp.getString("id");
							item.put("image_1", StringTools.parseUrltoFallView( temp.getString("image_1") ));
							item.put("uid", temp.getString("uid"));
							model.uid = temp.getString("uid");
							item.put("subject", temp.getString("subject").equals("") ? "无标题" : temp.getString("subject") );
							item.put("name", temp.getString("name"));
							tempList.add(item);
							((FamilyDayVerPMApplication)getApplication()).wf_arrayList.add(model);
							picInfoArrayList = new ArrayList<PhotoModel.PicInfo>();
							picnum = Integer.parseInt(temp.getString("picnum"));
							//  接口有问题 暂时指支持接收前3张
							if( picnum > 3 )
							{
								picnum = 3;
							}
							for (int j = 0; j < picnum; j++) {
								picInfo = new PicInfo();
								picInfo.url = temp.getString("image_" + (j+1) );
								picInfo.height = Integer.parseInt( temp.getString("image_" + (j+1) + "_height") );
								picInfo.width = Integer.parseInt( temp.getString("image_" + (j+1) + "_width") );
								picInfoArrayList.add(picInfo);
							}
							object.put("id", temp.getString("id") );
							object.put("type", "photoid");
							object.put("love",  temp.getInt("mylove"));
							object.put("uid", temp.getString("uid"));
							object.put("imgarray",picInfoArrayList );
							object.put("say",  temp.getString("message") );
							object.put("listview", null);
							object.put("time",  temp.getString("dateline") );
							object.put("name",  temp.getString("name"));
							
							((FamilyDayVerPMApplication)getApplication()).wf_array.put(object);
						}
						page++;
					}
					
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			isLoading = false;
			if (tempList != null) {
				topicList.addAll(tempList);
				
				if (!topicid.equals("") && tempList.size()<=1 && nexttopicid.equals("")
						|| topicid.equals("") && tempList.size()<=1 && nexttopicid.equals("")) {
					isOver = true;
					llLoading.setVisibility(View.GONE);
				}
				
//				if (topicList.size() <= 11) {
//					new GetTodayTopicTask().execute();
//				}
				AddItemToContainer(current_page++, page_count);
			}
			
		}
	}
}
