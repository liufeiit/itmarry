package com.ze.familydayverlenovo.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ze.commontool.DisplayUtil;
import com.ze.commontool.LoadImageMgr;
import com.ze.commontool.NetHelper;
import com.ze.commontool.PublicInfo;
import com.ze.commontool.LoadImageMgr.ImageCallBack;
import com.ze.commontool.StringTools;
import com.ze.familydayverlenovo.PhotoShowActivity;
import com.ze.familydayverlenovo.R;
import com.ze.familydayverlenovo.adapter.PicListViewAdapter;
import com.ze.familydayverlenovo.userinfo.UserInfoManager;
import com.ze.model.PhotoModel.PicInfo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PicFragment extends RelativeLayout  implements RelativeLayoutImp {
	
	private 	Context mContext;
	private	ListView 					mListView;
	private 	BaseAdapter			mAdapter;
//	private 	LinearLayout			mPicLayout;
//	private 	ImageView 				mPic_content;
	private 	TextView					mTvSay[];
	private 	TextView					mTvSay_back[];
	
	private    TextView					mTvName[];
	private    TextView					mTvName_back[];
	
//	private 	TextView					mTvTopic;
	private 	TextView					mTvTime[];
	private 	TextView					mTvTime_back[];
	
	TextPaint textPaint_name[];
	TextPaint textPaint_time[];
	TextPaint textPaint_say[];
	
	View 		layout_info[];
	View		lyoaut_info_context[];
	private 	View 						describFlagView[];
//	private 	View						mBack2Line;
//	private 	TextView					mTvFromName;
	private 	Button 					mBtnShowDisscuss;
	private 	TextView					mTvEncourage;
	private 	ProgressBar				mProgressBar;
	private 	final String[]			FLAGS =
																{
									
																	"name",
																	"say",
																	"uid"
																};
	private 	static final 	int[]				IDS = {R.id.pic_item_name,R.id.pic_item_say};
	private 	 View 						mHeadView;
	private 	 View 						mFootView;
	private 	List<Map<String, Object>> mList;
	private 	JSONObject mObject;
	private 	List< ImageView>  imageViews;
	private 	ImageView			mHeadImageViews[];
	private 	ImageView			mHeadImageViews_default[];
	
	private 	View 					mHeadImageViewLayouts[];
	private 	TextView				pic_textall;
//	private 	View 					mHeadImageViewPbs[];
	
	public static final String 	IMG_DETAIL_TAIL = "!580";
	public static final int 		ONCE_GET = 5;
	public PicFragment(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		mContext = context;
		imageViews = new ArrayList<ImageView>();
		setupViews();
	}
	 public PicFragment(Context context, AttributeSet attrs) {  
	        super(context, attrs);  
	        setupViews();  
	    } 
	//初始化View.  
    public void setupViews(){  
        LayoutInflater inflater = LayoutInflater.from( getContext() );  
        View rootView = inflater.inflate(R.layout.fragment_pic, null);  
        mHeadView		= LayoutInflater.from(mContext).inflate(R.layout.photoshow_listviewhead, null);
        mListView 			= (ListView)rootView.findViewById(R.id.pic_listview);
		mList 					= new ArrayList<Map<String,Object>>();
		mTvName 			= 	new TextView[3];
		mTvName_back	= new TextView[3];
		mTvSay				= new TextView[3];
		mTvSay_back		= new TextView[3];
		mTvTime				= new TextView[3];
		mTvTime_back	= new TextView[3];
		
		textPaint_name	= new TextPaint[3];
		textPaint_time	= new TextPaint[3];
		textPaint_say		= new TextPaint[3];
		
		describFlagView	= new View[3];
		
		layout_info = new View[3];
		layout_info[0]		= mHeadView.findViewById(R.id.photoframe_info1_layout);
		layout_info[1]		= mHeadView.findViewById(R.id.photoframe_info2_layout);
		layout_info[2]		= mHeadView.findViewById(R.id.photoframe_info3_layout);
		layout_info[0].setOnClickListener(ButtonOnclickListener);
		layout_info[1].setOnClickListener(ButtonOnclickListener);
		layout_info[2].setOnClickListener(ButtonOnclickListener);
		
		lyoaut_info_context = new View[3];
		lyoaut_info_context[0]		= mHeadView.findViewById(R.id.photoframe_info1_layout_context);
		lyoaut_info_context[1]		= mHeadView.findViewById(R.id.photoframe_info2_layout_context);
		lyoaut_info_context[2]		= mHeadView.findViewById(R.id.photoframe_info3_layout_context);
		
		mTvName[0] 			= (TextView)mHeadView.findViewById(R.id.pic1_desc_name);
		mTvName_back[0]	= 	(TextView)mHeadView.findViewById(R.id.pic1_desc_name_back);
		textPaint_name[0]		= mTvName_back[0].getPaint();
		mTvSay[0] 				= (TextView)mHeadView.findViewById(R.id.pic1_desc);
		mTvSay_back[0]		= 	(TextView)mHeadView.findViewById(R.id.pic1_desc_back);
		textPaint_say[0]		= mTvSay_back[0].getPaint();
		mTvTime[0] 				= (TextView)mHeadView.findViewById(R.id.pic1_desc_time);
		mTvTime_back[0]		= 	(TextView)mHeadView.findViewById(R.id.pic1_desc_time_back);
		textPaint_time[0]		= mTvTime_back[0].getPaint();
		
		mTvName[1] 			= (TextView)mHeadView.findViewById(R.id.pic2_desc_name);
		mTvName_back[1]	= 	(TextView)mHeadView.findViewById(R.id.pic2_desc_name_back);
		textPaint_name[1]		= mTvName_back[1].getPaint();
		mTvSay[1] 				= (TextView)mHeadView.findViewById(R.id.pic2_desc);
		mTvSay_back[1]		= 	(TextView)mHeadView.findViewById(R.id.pic2_desc_back);
		textPaint_say[1]		= mTvSay_back[1].getPaint();
		mTvTime[1] 				= (TextView)mHeadView.findViewById(R.id.pic2_desc_time);
		mTvTime_back[1]		= 	(TextView)mHeadView.findViewById(R.id.pic2_desc_time_back);
		textPaint_time[1]		= mTvTime_back[1].getPaint();
		
		mTvName[2] 			= (TextView)mHeadView.findViewById(R.id.pic3_desc_name);
		mTvName_back[2]	= 	(TextView)mHeadView.findViewById(R.id.pic3_desc_name_back);
		textPaint_name[2]		= mTvName_back[2].getPaint();
		mTvSay[2] 				= (TextView)mHeadView.findViewById(R.id.pic3_desc);
		mTvSay_back[2]		= 	(TextView)mHeadView.findViewById(R.id.pic3_desc_back);
		textPaint_say[2]		= mTvSay_back[2].getPaint();
		mTvTime[2] 				= (TextView)mHeadView.findViewById(R.id.pic3_desc_time);
		mTvTime_back[2]		= 	(TextView)mHeadView.findViewById(R.id.pic3_desc_time_back);
		textPaint_time[2]		= mTvTime_back[2].getPaint();
		
		describFlagView[0]		= mHeadView.findViewById(R.id.pic1_desc_img);
		describFlagView[1]		= mHeadView.findViewById(R.id.pic2_desc_img);
		describFlagView[2]		= mHeadView.findViewById(R.id.pic3_desc_img);
		
		mAdapter			= new PicListViewAdapter(mContext, mList, R.layout.pic_listview_item, FLAGS, IDS);
		mFootView			= LayoutInflater.from(mContext).inflate(R.layout.pic_listview_foot, null);
		mFootView.setOnClickListener(ButtonOnclickListener);
		mBtnShowDisscuss = (Button)mFootView.findViewById(R.id.pic_lv_foot_show_disscuss);
		mBtnShowDisscuss.setOnClickListener(ButtonOnclickListener);
		mTvEncourage = (TextView)mFootView.findViewById(R.id.pic_lv_foot_encourage);
		mProgressBar		= (ProgressBar)mFootView.findViewById(R.id.pic_lv_foot_progressbar);
		
//		mPicLayout			= (LinearLayout)mHeadView.findViewById(R.id.pic_content_layout);
		mHeadImageViews = new ImageView[3];
		mHeadImageViews[0] = (ImageView)mHeadView.findViewById(R.id.pic_content_1);
		mHeadImageViews[1] = (ImageView)mHeadView.findViewById(R.id.pic_content_2);
		mHeadImageViews[2] = (ImageView)mHeadView.findViewById(R.id.pic_content_3);
		
		mHeadImageViews_default = new ImageView[3];
		mHeadImageViews_default[0] = (ImageView)mHeadView.findViewById(R.id.pic_content_1_default);
		mHeadImageViews_default[1] = (ImageView)mHeadView.findViewById(R.id.pic_content_2_default);
		mHeadImageViews_default[2] = (ImageView)mHeadView.findViewById(R.id.pic_content_3_default);
		
		mHeadImageViewLayouts = new View[3];
		mHeadImageViewLayouts[0] = mHeadView.findViewById(R.id.pic_content_1_layout);
		mHeadImageViewLayouts[1] = mHeadView.findViewById(R.id.pic_content_2_layout);
		mHeadImageViewLayouts[2] = mHeadView.findViewById(R.id.pic_content_3_layout);
		mHeadImageViews[0].setOnClickListener(ButtonOnclickListener);
		mHeadImageViews[1].setOnClickListener(ButtonOnclickListener);
		mHeadImageViews[2].setOnClickListener(ButtonOnclickListener);
		
		mHeadImageViews_default[0].setOnClickListener(ButtonOnclickListener);
		mHeadImageViews_default[1].setOnClickListener(ButtonOnclickListener);
		mHeadImageViews_default[2].setOnClickListener(ButtonOnclickListener);
		
		pic_textall				= (TextView) mHeadView.findViewById(R.id.pic_content_textall);
		pic_textall.setOnClickListener(ButtonOnclickListener);
//		mHeadImageViewPbs = new View[3];
//		mHeadImageViewPbs[0] = mHeadView.findViewById(R.id.pic_content_1_pb);
//		mHeadImageViewPbs[1] = mHeadView.findViewById(R.id.pic_content_2_pb);
//		mHeadImageViewPbs[2] = mHeadView.findViewById(R.id.pic_content_3_pb);
		
		mListView.addHeaderView(mHeadView);
		mListView.addFooterView(mFootView);
		mListView.setAdapter(mAdapter);
		mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				((PhotoShowActivity)mContext).resetPlayHandler();
			}
		});
		mListView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
//				if( event.getAction() == MotionEvent.ACTION_DOWN )
//				{
//					touchY = event.getY();
//				}else if ( event.getAction() == MotionEvent.ACTION_MOVE  )
//				{
//					if( touchY - event.getY() > 20  ){
//						
//					}else if( event.getY() - touchY  > 20 )
//					{
//						mListView.scrollBy(0, mListView.getScrollY() - PublicInfo.SCREEN_H);
//					}
//				}
				return false;
			}
		});
        addView(rootView);  
//        Toast.makeText(getContext(), "rootView.getMeasuredHeight:"+rootView.getMeasuredHeight() + "rootView.getHeight() " + rootView.getHeight() , Toast.LENGTH_SHORT).show();
    }  
    public void pageDown()
    {
    }
    public void pageUp()
    {
    }
    private float touchY;
    /** 
     * 填充数据，共外部调用. 
     * @param object 
     */  
    public void setData(JSONObject object){  
        this.mObject = object; 
        ArrayList<PicInfo> 	imgArray = null;
        int picnum = 0;
        try {  
            if( !object.isNull("imgarray"))
            {
            	imgArray = (ArrayList<PicInfo>) object.get("imgarray");
            	String imgUrl ;
            	ImageView imageView;
//            	android.widget.LinearLayout.LayoutParams params;
//            	RelativeLayout.LayoutParams headViewparams =  (LayoutParams) mHeadView.getLayoutParams();
//            	headViewparams.height = PublicInfo.DEFAULT_PIC_H;
            	int headHeight = 0;
            	picnum = imgArray.size();
            	for (int i = 0; i < picnum; i++) {
            		headHeight += PublicInfo.DEFAULT_PIC_H;
            		if ( i == 3 ) {
            			// 暂时只发布三张。超过三张忽略不计
						break;
					}
            		
            		imgUrl = imgArray.get(i).url;
            		int endFlag = -1;
	          		int img_w = imgArray.get(i).width;
	          		int img_h  = imgArray.get(i).height;
	          		android.widget.RelativeLayout.LayoutParams params = 
            				new android.widget.RelativeLayout.LayoutParams(PublicInfo.SCREEN_H, PublicInfo.SCREEN_W);
	          		endFlag = imgUrl.indexOf("!");
	          		android.widget.RelativeLayout.LayoutParams layout_infoParams = null;
	          		android.widget.RelativeLayout.LayoutParams layout_infoParams_1 = null;
	          		mTvTime_back[i].setVisibility(View.VISIBLE);
					mTvName_back[i].setVisibility(View.VISIBLE);
	          		if ( endFlag != -1 || imgUrl.contains(PublicInfo.picID_default_head) )
	          		{
	          			if( ! imgUrl.contains(PublicInfo.picID_default_head))
	          			{
	          				imgUrl = imgUrl.substring(0, endFlag);
							if( img_w >= img_h )
							{
								imgUrl = imgUrl + PublicInfo.PHOTO_SIZE_LARGE;
							}else
							{
								imgUrl = imgUrl + PublicInfo.PHOTO_SIZE_HALF;
							}
	          			}
	          		}
						if ( ( img_w >= img_h && PhotoShowActivity.isLandscape) || ( img_w < img_h && !PhotoShowActivity.isLandscape) ) {
							Log.v("test1", "next is heng");
							params.height = PublicInfo.SCREEN_H;
							params.width = PublicInfo.SCREEN_W;
							layout_infoParams_1
			          		= new android.widget.RelativeLayout.LayoutParams(PublicInfo.SCREEN_W, PublicInfo.SCREEN_H / 3);
							layout_infoParams
			          		= new android.widget.RelativeLayout.LayoutParams(PublicInfo.SCREEN_W, PublicInfo.SCREEN_H / 2);
							// 适配横屏状态下，图片全屏文字描述被截的情况
							if( img_w >= img_h && PhotoShowActivity.isLandscape ){
//								int minheight = (int) (2 * StringTools.getFontHeight(30)+ 40);
								
								if( PublicInfo.SCREEN_H / 3 <= 150 ){
									layout_infoParams_1.height = 150;
									if( 150 > layout_infoParams.height )
									{
										layout_infoParams.height = 150 + 40;
									}
								}
							}
							layout_infoParams_1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
							
							layout_infoParams.addRule(RelativeLayout.ALIGN_BOTTOM,mHeadImageViews[i].getId());
							layout_infoParams.addRule(RelativeLayout.ALIGN_LEFT,mHeadImageViews[i].getId());
							mTvSay[i].setTextColor(Color.WHITE);
							mTvName[i].setTextColor(Color.WHITE);
							mTvTime[i].setTextColor(Color.WHITE);
							textPaint_name[i].setStrokeWidth(1.0f);
							textPaint_name[i].setStyle(Style.STROKE);
							textPaint_say[i].setStrokeWidth(1.0f);
							textPaint_say[i].setStyle(Style.STROKE);
							textPaint_time[i].setStrokeWidth(1.0f);
							textPaint_time[i].setStyle(Style.STROKE);
//							layout_info_img.setVisibility(View.INVISIBLE);
							describFlagView[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.photo_img_2));
							
//							layout_infoParams.setMargins(20, 20, 20, 100);
							
							mTvSay_back[i].setMaxLines(1);
							mTvSay[i].setMaxLines(1);
						}else
						{
							Log.v("test1", "next is shu");
//							imgUrl = imgUrl + PublicInfo.PHOTO_SIZE_HALF;
//							params.height = PublicInfo.SCREEN_H;
//							params.width = PublicInfo.SCREEN_W / 2;
							mTvTime_back[i].setVisibility(View.INVISIBLE);
							mTvName_back[i].setVisibility(View.INVISIBLE);
							layout_infoParams_1
			          		= new android.widget.RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
							if( img_w < img_h )
							{
								params.height = PublicInfo.SCREEN_H;
								params.width = PublicInfo.SCREEN_W / 2;
								layout_infoParams
				          		= new android.widget.RelativeLayout.LayoutParams(PublicInfo.SCREEN_W/2, PublicInfo.SCREEN_H);
								layout_infoParams.addRule(RelativeLayout.CENTER_VERTICAL);
								int random = (int) (Math.random()*10);
								if(random % 2== 0) 
								{
									params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT) ;
									layout_infoParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
									layout_infoParams_1.addRule(RelativeLayout.CENTER_VERTICAL);
								}else{
									params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
									layout_infoParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
									layout_infoParams_1.addRule(RelativeLayout.CENTER_VERTICAL);
								}
							}else {
								params.height = PublicInfo.SCREEN_H/2;
								params.width = PublicInfo.SCREEN_W ;
								if( i== imgArray.size() -1 )
								{
									layout_infoParams
					          		= new android.widget.RelativeLayout.LayoutParams(PublicInfo.SCREEN_W, 
					          				PublicInfo.SCREEN_H / 3 );
								}else
								{
									layout_infoParams
					          		= new android.widget.RelativeLayout.LayoutParams(PublicInfo.SCREEN_W, PublicInfo.SCREEN_H/2);
								}
								
//								layout_infoParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
								layout_infoParams.addRule(RelativeLayout.BELOW, mHeadImageViews[i].getId());
							}
							mTvSay[i].setTextColor(Color.BLACK);
							mTvName[i].setTextColor(getResources().getColor(R.color.picinfo_name));
							mTvTime[i].setTextColor(getResources().getColor(R.color.picinfo_time));
							textPaint_name[i].setStrokeWidth(0.0f);
							textPaint_name[i].setStyle(Style.STROKE);
							textPaint_say[i].setStrokeWidth(0.0f);
							textPaint_say[i].setStyle(Style.STROKE);
							textPaint_time[i].setStrokeWidth(0.0f);
							textPaint_time[i].setStyle(Style.STROKE);
//							layout_info_img.setVisibility(View.VISIBLE);
							describFlagView[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.photo_img_1));
							mTvSay[i].setMaxLines(3);
							mTvSay_back[i].setMaxLines(3);
						}
//					}
	          		if( layout_infoParams != null )
	          		{
	          			layout_info[i].setLayoutParams(layout_infoParams);
	          		}
	          		if( layout_infoParams_1 != null )
	          		{
	          			lyoaut_info_context[i].setLayoutParams(layout_infoParams_1);
	          		}
	          		mHeadImageViews[i].setLayoutParams(params);
	          		mHeadImageViews[i].setVisibility(View.VISIBLE);
	      			mHeadImageViews_default[i].setLayoutParams(params);
	      			mHeadImageViews_default[i].setVisibility(View.VISIBLE);
            		mHeadImageViewLayouts[i].setVisibility(View.VISIBLE);
            		mHeadImageViews[i].setVisibility(View.VISIBLE);
        			 Drawable imageDrawable = LoadImageMgr.getInstance().loadDrawble(imgUrl, 
            				mHeadImageViews[i], 
                			imageCallBack);
        			 if( imageDrawable != null )
    				 {
        				 mHeadImageViews[i].setImageDrawable(imageDrawable)  ;
						 if( mHeadImageViews_default[i] != null )
						 {
							 mHeadImageViews_default[i].setVisibility(View.INVISIBLE);
						 }
    				 }else
        				 {
//        					 mHeadImageViewPbs[i].setVisibility(View.VISIBLE);
        					 mHeadImageViews_default[i].setVisibility(View.VISIBLE);
        				 }
//            		mHeadImageViews[i].setOnClickListener(ButtonOnclickListener);
            		imageViews.add(mHeadImageViews[i]);
				}
            	 mHeadView.setMinimumHeight(headHeight);;
            }
//            if ( !object.isNull("img")) {
//         	  // mPic_content.setImageResource(object.getInt("img"));
//            	mPic_content.setImageDrawable(LoadImageMgr.getInstance().loadDrawble(object.getString("img"), mPic_content, 
//            			LoadImageMgr.getInstance().imageCallBack)) ;
//            }
           
            String str_temp;
            if ( !object.isNull("say")) {
            	str_temp = object.optString("say", "无标题");
            	if(str_temp.equals(""))
            	{
            		str_temp = "无标题";
            	}
            	mTvSay[0].setText( str_temp );
            	mTvSay[1].setText( str_temp );
            	mTvSay[2].setText( str_temp );
            	
            	mTvSay_back[0].setText( str_temp );
            	mTvSay_back[1].setText( str_temp );
            	mTvSay_back[2].setText( str_temp );
            	
            	/**
            	 *   2013.9.2 增加显示文字过长时的全文
            	 */
            	int temp_w;
            	int temp_h;
            	boolean needTextAll = false;
            	for( int i=0; i< picnum; i++ )
            	{
            		temp_w = imgArray.get(i).width;
            		temp_h = imgArray.get(i).height;
            		if (PhotoShowActivity.isLandscape) {
						// 横屏
            			if ( temp_w < temp_h ) {
							// 图片高度大于宽度，显示半屏，文字已经全部显示，跳过
            				needTextAll = false;
            				break;
						}else
						{
							//判断是否文字被截取
							needTextAll = isTextCut(mTvSay[i], str_temp);
						}
					}else {
						// 竖屏
						if ( temp_w > temp_h ) {
							// 图片高度小于宽度，显示半屏，文字已经全部显示，跳过
							needTextAll = false;
            				break;
						}else
						{
							//判断是否文字被截取
							//By mouse
							if (i < mTvSay.length)
								needTextAll = isTextCut( mTvSay[i], str_temp );
						}
					}
            	}
            	if( needTextAll )
            	{
            		pic_textall.setVisibility(View.VISIBLE);
            		pic_textall.setText(str_temp);
            	}else
            	{
            		pic_textall.setVisibility(View.INVISIBLE);
            		pic_textall.setText("");
            	}
            }
            if ( !object.isNull("name")) {
            	str_temp = object.optString("name", "无名");
            	mTvName[0].setText(str_temp);
            	mTvName[1].setText(str_temp);
            	mTvName[2].setText(str_temp);
            	
            	mTvName_back[0].setText(str_temp);
            	mTvName_back[1].setText(str_temp);
            	mTvName_back[2].setText(str_temp);
            	
            }
            if ( !object.isNull("time")) {
            	str_temp = NetHelper.transTime(Long.parseLong( object.getString("time")) );
            	mTvTime[0].setText( str_temp );
            	mTvTime[1].setText( str_temp );
            	mTvTime[2].setText( str_temp );
            	
            	mTvTime_back[0].setText( str_temp );
            	mTvTime_back[1].setText( str_temp );
            	mTvTime_back[2].setText( str_temp );
            	
            }
            
			
           if ( !object.isNull("listview")) {
        	   if( object.get("listview") == null )
        	   {
//        		   loadDisscussTask();
        	   }else
        	   {
        		   mList.addAll(0,(List<Map<String,Object>>)object.get("listview"));
        		   mAdapter.notifyDataSetChanged();
                   mListView.setSelection(1);
        	   }
           }else
           {
//        	   loadDisscussTask();
           }
//           if ( !object.isNull("fuid")) {
//        	   if( !object.getString("fuid").equals("0") )
//        	   {
//        		   mTvFromName.setText(String.format(getResources().getString(R.string.itemfrom),object.getString("fname") ));
//        	   }
//        		 
//           }
            //mListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {  
            e.printStackTrace();  
        }  
          
    }
    public boolean isTextCut(TextView tv, String string)
    {
    	Paint paint  = tv.getPaint(); 
        paint.setTextSize(tv.getTextSize());
        // px = dp * (dpi / 160)
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int avail = PublicInfo.SCREEN_W - DisplayUtil.dip2px((40 + 18 + 10), dm.density);
    	String changeString = 
    		TextUtils.ellipsize(string, (TextPaint) paint, avail, TextUtils.TruncateAt.END).toString();
    	if (string.length() > changeString.length() ) {
			return true;
		}
    	return false;
    }
    /** 
     * 这里内存回收.外部调用. 
     */  
    
public ImageCallBack imageCallBack = new ImageCallBack() {
		
		@Override
		public void setImage(Drawable d, String url, ImageView view) {
			// TODO Auto-generated method stub
			if( /*mActiveImages.contains(view) &&*/ url.equals((String)view.getTag()))
			{
				view.setImageDrawable(d);
				for (int i = 0; i < mHeadImageViews.length; i++) {
					if (view == mHeadImageViews[i] && mHeadImageViews_default[i] != null) {
						mHeadImageViews_default[i].setVisibility(View.INVISIBLE);
					}
				}
			}
		}
	};
    public void recycle(){  
//        mAlbumImageView.setImageBitmap(null);  
//        if ((this.mBitmap == null) || (this.mBitmap.isRecycled()))  
//            return;  
//        this.mBitmap.recycle();  
//        this.mBitmap = null;  
    }  
    /** 
     * 重新加载.外部调用. 
     */  
    public void reload(){  
//        try {  
//            int resId = mObject.getInt("resid");  
            //实战中如果图片耗时应该令其一个线程去拉图片异步,不然把UI线程卡死.  
//            mAlbumImageView.setImageResource(resId);  
            setData(mObject);
//        }catch (JSONException e) {  
//            e.printStackTrace();  
//        }  
    }
//    public void loadDisscussTask(boolean isInited){
//    	if (isInited) {
//			return;
//		}else
//		{
//			loadDisscussTask();
//		}
//    }
    private boolean loading = false;
    public void loadDisscussTask()
    {
    	new AsyncTask<String, String, String> ()
    	{
    		boolean isLoad = true;
    		protected void onPreExecute() {
    			loading = true;
    			mBtnShowDisscuss.setVisibility(View.VISIBLE);
    			mTvEncourage.setVisibility(View.INVISIBLE);
    			mProgressBar.setVisibility(View.VISIBLE);
    			mBtnShowDisscuss.setOnClickListener(null);
    		};
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				String respon = null;
				int page = 1;
				if ( mList.size() != 0 && mList.size()%PublicInfo.PER_LOAD != 0 )
				{
					isLoad = false;
					return "";
				}else {
					page = mList.size()/PublicInfo.PER_LOAD + 1;
				}
				try{
						respon = NetHelper.getResponByHttpClient(getResources().getString(R.string.http_photo_disscuss),
							mObject.getString("id"),
							UserInfoManager.getInstance(null).getItem("m_auth").getValue(),page + "");
				}catch (Exception e) {
					// TODO: handle exception
				}
				return respon;
			}
    		protected void onPostExecute(String result) {
    			mProgressBar.setVisibility(View.INVISIBLE);
    			mBtnShowDisscuss.setOnClickListener(ButtonOnclickListener);
    			if( !isLoad)
    			{
    				Toast.makeText(mContext, getResources().getString(R.string.dialog_msg_nomoredata), Toast.LENGTH_SHORT).show();
    				return;
    			}
    			try {
					JSONObject object = new JSONObject(result);
					if ( object.getInt("error") == 0 ) {
						JSONArray array = object.getJSONArray("data");
						int size = array.length();
						JSONObject temp;
						Map<String, Object> map;
						for (int i = 0; i < size; i++) {
							map = new HashMap<String, Object>();
							temp = array.getJSONObject(i);
							map.put(FLAGS[0], temp.getString("authorname") + ": ");
							map.put(FLAGS[1], temp.getString("message"));
							map.put(FLAGS[2], temp.getString("uid"));
							mList.add(map);
						}
						if( size == 0 || size % ONCE_GET != 0)
						{
							mTvEncourage.setVisibility(View.VISIBLE);
							mBtnShowDisscuss.setVisibility(View.INVISIBLE);
						}else
						{
							mTvEncourage.setVisibility(View.INVISIBLE);
							mBtnShowDisscuss.setVisibility(View.VISIBLE);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
//    			Toast.makeText(mContext, getResources().getString(R.string.tips_load_finished), Toast.LENGTH_SHORT).show();
    			mAdapter.notifyDataSetChanged();
    			
    		};
    	}.execute("");
    }
    private OnClickListener ButtonOnclickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if ( v == mBtnShowDisscuss ) {
//				mProgressBar.setVisibility(View.VISIBLE);
				loadDisscussTask();
			}else 
			{
				if( ( (PhotoShowActivity)mContext ).mMenuView.getVisibility() == View.VISIBLE )
				{	
//					( (PhotoShowActivity)mContext ).mMenuView.setVisibility(View.GONE);
					( (PhotoShowActivity)mContext ).mMenuView.startAnimation( ( (PhotoShowActivity)mContext ).menuOutAnimation);
				}else 
				{
//					( (PhotoShowActivity)mContext ).mMenuView.setVisibility(View.VISIBLE);
					( (PhotoShowActivity)mContext ).mMenuView.startAnimation( ( (PhotoShowActivity)mContext ).menuInAnimation);
				}
//				for( int i=0; i<imageViews.size(); i++ )
//				{
//					if (v == imageViews.get(i)  ) {
//							Intent intent = new Intent();
//							String imgUrl = (String)imageViews.get(i).getTag();
//							intent.putExtra("image", imgUrl);
//							try {
//								intent.putExtra("id", mObject.getString("id") );
//							ArrayList<PicInfo> pics = (ArrayList<PicInfo>) mObject.get("imgarray");
//							 String picstemp;
//							 int find;
//							 ArrayList<String> picUrls = new ArrayList<String>();
//							 for (int j = 0; j < pics.size(); j++) {
//								picstemp = pics.get(j).url	;
//								find = picstemp.indexOf("!");
//								if( find != -1 )
//								{
//									picstemp = picstemp.substring(0, find+1);
//								}
//								picstemp = picstemp + SpaceDetailActivity.REBLOG_PIC_TAIL;
//								picUrls.add(picstemp);
//							}
//							 intent.putStringArrayListExtra("imgarray", picUrls);
//							 intent.putExtra("uid",mObject.getString("uid"));
//							intent.setClass(mContext, BigPicActivity.class);
//							mContext.startActivity(intent);
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}}
				}
		}
	};
	@Override
	public boolean isLoadDisscussed() {
		// TODO Auto-generated method stub
		return loading;
	}
}
