package com.hongkong.stiqer.ui;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.flurry.android.FlurryAgent;
import com.google.zxing.CaptureActivity;
import com.hongkong.stiqer.AppContext;
import com.hongkong.stiqer.R;
import com.hongkong.stiqer.adapter.CheckInAdapter;
import com.hongkong.stiqer.adapter.SdMissionAdapter;
import com.hongkong.stiqer.adapter.SdPaiAdapter;
import com.hongkong.stiqer.db.FavDao;
import com.hongkong.stiqer.entity.CheckIn;
import com.hongkong.stiqer.entity.Fav;
import com.hongkong.stiqer.entity.Mission;
import com.hongkong.stiqer.entity.MissionResult;
import com.hongkong.stiqer.entity.Rank;
import com.hongkong.stiqer.entity.ScanResult;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.ui.base.BaseActivity;
import com.hongkong.stiqer.utils.ErrorCodeHelper;
import com.hongkong.stiqer.utils.MyLocation;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.CustomDialog;
import com.hongkong.stiqer.widget.DialogListener;
import com.hongkong.stiqer.widget.ExpandableHeightListView;
import com.hongkong.stiqer.widget.LeftGallery;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoTools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class StoreDetailActivity extends BaseActivity {
	public  static final String TAG = "Stiqer";  
	private static final int    TAKE_PICTURE = 0;
	private static final int    CHOOSE_PICTURE = 1;
	private static final int    TAKE_PICTURE_CODE = 102;
	private static final int    CHOOSE_PICTURE_CODE = 103;
	private static final int    STORE_LIKE_SUCCESSFUL = 1040;
	private static final int    STORE_LIKE_HAVE = 1041;
	private final static int    SCAN_SUCCESS = 1000;
	private final static int    SCAN_RETURN= 101;
	private final static int    REQUEST_CODE_MISSION = 201;
	private final static int    MISSION_NOT_FOUND = 1029;
	private final static int    MISSION_NOT_FULFILLED = 1030;
	private final static int    MISSION_ALREADY_COMPLETED = 1031;
    private final static int    WRONG_QR = 1032;
	private final static int    MISSION_SCAN_SUCCESS = 2001;
	public static final int     COMMENT_NUM_REFRESH = 113;
    
    CustomDialog                progressDialog,returnDialog,classDialog,levelDialog; 
    View                        view;
    ProgressBar                 progressbar;
    Context                     mContext;
    LinearLayout                wrap_layout,photo_btn,comment_btn,like_btn;
    ImageView                   level_icon,store_image,next_class_icon,des_first_dot,des_second_dot,des_third_dot;
    Button                      btnBack,btn_scan;
    TextView                    level_name,next_class_text,stiqer_num,like_num,comment_num,sd_title,des_first_text,des_second_text,des_third_text,btn_more,store_des_title,store_des_con;
    ExpandableHeightListView    mission_list;
    Gallery                     photo_gallery,costomer_gallery;
    Map<String, String>         map;
    DialogListener              addDialogListener,classUpListener,levelUpListener;
    int                         store_like_num, store_comment_num;
    String                      store_name,checkin_str,rank_str,store_id;
    MyLocation                  myLocation;
	ScanResult                  scanResult;
	ErrorCodeHelper             errorHelper;
	String                      filePath = Environment.getExternalStorageDirectory().getPath() + "/stiqer/";
	String                      tempFile = "checkin.jpg";
	MissionResult               missionResult;
	double                      longitude = 0.0,latitude = 0.0;
    AppContext                  application; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
		  setContentView(R.layout.activity_storedetail);	
		}catch(Exception e){
			StoreDetailActivity.this.finish();
		}
		initIntent();
		initView();
		initPath();
		new GetDataTask().execute();
		initDialog();
	}
	
	private void initDialog(){
		scanResult = (ScanResult) getIntent().getSerializableExtra("scanResult");
		missionResult = (MissionResult) getIntent().getSerializableExtra("missionResult");
		if(scanResult != null){
			returnDialog = CustomDialog.createScanDialog(mContext,addDialogListener,scanResult.getTrans_stiqer_num(),scanResult.getTrans_egg_num());
            returnDialog.show();
		}
		if(missionResult != null){
			CustomDialog v = CustomDialog.createMissionRedeemDialog(mContext, missionResult);
    		v.show();
		}
	}
	
	private void initPath() {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
	protected void onStart()
	{
		super.onStart();
		FlurryAgent.onStartSession(this, "32YTPN6M7T4TW3DH38P5");
	}
	
	protected void onStop()
	{
		super.onStop();		
		FlurryAgent.onEndSession(this);
	}
	
	  private void initIntent() {
		Intent t = getIntent();
		store_id = t.getStringExtra("store_id");
		store_name = t.getStringExtra("store_name");
	}

	private void initView() {
		application = (AppContext)getApplication();
        application.init();  
        application.addActivity(this);  
     
		sd_title = (TextView) findViewById(R.id.sd_title);
		if(store_name.length()>20){
			sd_title.setText(store_name.substring(0,18)+"..");
		}else{
			sd_title.setText(store_name);
		}
		
        mission_list = (ExpandableHeightListView) findViewById(R.id.store_mission_listview);
        costomer_gallery = (Gallery) findViewById(R.id.sd_customer_gallery);
        map = new HashMap<String , String>();  
        mContext = this;
        errorHelper = new ErrorCodeHelper(mContext);
        next_class_icon = (ImageView) findViewById(R.id.next_class_icon);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        wrap_layout = (LinearLayout) findViewById(R.id.connect_wrap);
        level_name = (TextView) findViewById(R.id.store_level_name);
        level_icon = (ImageView) findViewById(R.id.store_level_icon);
        store_image = (ImageView) findViewById(R.id.store_image);
        next_class_text = (TextView) findViewById(R.id.next_class_text);
        stiqer_num = (TextView) findViewById(R.id.stiqer_num_text);
        btn_more = (TextView) findViewById(R.id.btn_more);
        
        store_des_title =  (TextView) findViewById(R.id.store_des_title);
        store_des_con =  (TextView) findViewById(R.id.store_des_con);
        
        des_first_dot = (ImageView) findViewById(R.id.des_first_dot);
        des_second_dot = (ImageView) findViewById(R.id.des_second_dot);
        des_third_dot = (ImageView) findViewById(R.id.des_third_dot);
        des_first_text = (TextView) findViewById(R.id.des_first_text);
        des_second_text = (TextView) findViewById(R.id.des_second_text);
        des_third_text = (TextView) findViewById(R.id.des_third_text);
        
        myLocation = new MyLocation(mContext);
		longitude = myLocation.getLatitude();
		latitude = myLocation.getLongitude();
        
        comment_num = (TextView) findViewById(R.id.comment_num);
        like_num = (TextView) findViewById(R.id.like_num);
        photo_btn = (LinearLayout) findViewById(R.id.enter_photo_btn);
        comment_btn = (LinearLayout) findViewById(R.id.enter_comment_btn);
        like_btn = (LinearLayout) findViewById(R.id.enter_like_btn);

        photo_gallery = (Gallery) findViewById(R.id.sd_photo_gallery);
        MarginLayoutParams mlp = (MarginLayoutParams) photo_gallery.getLayoutParams();
        mlp.setMargins(-(Util.SCREENWIDTH/9*7), 
                       mlp.topMargin, 
                       mlp.rightMargin, 
                       mlp.bottomMargin
        );
      
        MarginLayoutParams mlp2 = (MarginLayoutParams) costomer_gallery.getLayoutParams();
        mlp2.setMargins(-(Util.SCREENWIDTH/9*7), 
                       mlp.topMargin, 
                       mlp.rightMargin, 
                       mlp.bottomMargin
        );
        
        btnBack = (Button) findViewById(R.id.sd_back_btn);
        btn_scan = (Button) findViewById(R.id.sd_btn_scan);
        progressDialog = CustomDialog.createProgressDialog(this,"Scan successful. Please wait ... ");
         
        levelUpListener  = new DialogListener(){
			 public void showDialog(Object o) {
			 }
	     };
	     
	     classUpListener  = new DialogListener(){
			 public void showDialog(Object o) {
				 if(scanResult.getNew_level()>0){
					 levelDialog = CustomDialog.createLevelUpDialog(mContext,scanResult.getNew_level(),levelUpListener);
	            	 levelDialog.show();
				 }
			 }
	     };
	     
		addDialogListener = new DialogListener()
        {
            public void showDialog(Object o)
            {
            	if(scanResult.getNew_class()>0){
                    //class升级
             	   classDialog = CustomDialog.createClassUpDialog(mContext,scanResult.getNew_class(),scanResult.getTrans_store_name(),classUpListener);
             	   classDialog.show();
                }else if(scanResult.getNew_level()>0){
             	   // level升级
             	   levelDialog = CustomDialog.createLevelUpDialog(mContext,scanResult.getNew_level(),levelUpListener);
             	   levelDialog.show();
                }
            }
        };
        btnBack.setOnClickListener(new MyOnClickListener());
        comment_btn.setOnClickListener(new MyOnClickListener());
        photo_btn.setOnClickListener(new MyOnClickListener());
        btn_scan.setOnClickListener(new MyOnClickListener());
        like_btn.setOnClickListener(new MyOnClickListener());
        btn_more.setOnClickListener(new MyOnClickListener());
        
        btnBack.setOnTouchListener(Util.TouchDark);
        photo_btn.setOnTouchListener(Util.TouchDark);
        btn_scan.setOnTouchListener(Util.TouchDark);
        btn_more.setOnTouchListener(Util.TouchDark);
    }  
	  
	class MyOnClickListener implements OnClickListener{
		public void onClick(View v) {
			switch(v.getId()){
			
			case R.id.sd_btn_scan:
				Intent intent = new Intent(StoreDetailActivity.this, CaptureActivity.class);
				startActivityForResult(intent,SCAN_RETURN);
			    break;
			
			case R.id.sd_back_btn:
				StoreDetailActivity.this.finish();
				break;
		
			case R.id.enter_comment_btn:
				Intent t_comment = new Intent(StoreDetailActivity.this, CommentActivity.class);
				t_comment.putExtra("type", 1);
				t_comment.putExtra("tid", store_id);
				t_comment.putExtra("comment_num", store_comment_num);
				startActivityForResult(t_comment,COMMENT_NUM_REFRESH);
				break;
				
			case R.id.enter_photo_btn:
				showPicturePicker(mContext);
				break;
				
			case R.id.enter_like_btn:
				new Thread(){
					public void run(){
						int code = APIManager.sharedInstance().addFav(store_id,1);
						Message msg = mHandler.obtainMessage();
						if(code == 1000){
							code = STORE_LIKE_SUCCESSFUL;
						}
			            msg.what = code;
			            mHandler.sendMessage(msg);
					}
				}.start();
				break;
			
			case R.id.btn_more:
				Intent t = new Intent(StoreDetailActivity.this,TabStoreActivity.class);
				t.putExtra("store_id", store_id);
				t.putExtra("rank", rank_str);
				t.putExtra("checkin", checkin_str);
				startActivity(t);
				break;
			}
		}
	}
	    
	  
	private class GetDataTask extends AsyncTask<Integer[],Void,JSONObject>{
		@Override
		protected void onPostExecute(JSONObject obj) {
			ErrorCodeHelper errorHelper = new ErrorCodeHelper(StoreDetailActivity.this);
			progressbar.setVisibility(View.GONE);
			if(obj == null){
				errorHelper.connectError();
			}else{	
			    try {
			      if(errorHelper.CommonCode(obj.getInt("error_code"))){
			        JSONObject store_info = obj.getJSONObject("store_info");
				    store_like_num = store_info.getInt("like_num");
				    comment_num.setText(store_info.getInt("comment_num")+" Comments");
				    store_comment_num = store_info.getInt("comment_num");
				    like_num.setText(store_info.getInt("like_num")+" Likes");
				    Picasso.with(mContext).load(store_info.getString("store_img_url")).into(store_image);
				    if(!store_info.getString("des_first").equals("")){
					   des_first_dot.setVisibility(View.VISIBLE);
					   des_first_text.setText(store_info.getString("des_first"));
				    }
				    if(!store_info.getString("des_second").equals("")){
					    des_second_dot.setVisibility(View.VISIBLE);
					     des_second_text.setText(store_info.getString("des_second"));
				    }
				    if(!store_info.getString("des_third").equals("")){
				    	store_des_title.setVisibility(View.VISIBLE);
				    	store_des_con.setText(store_info.getString("des_third"));
				    }
				    
				    JSONObject user_info = obj.getJSONObject("user_info");
				    stiqer_num.setText(user_info.getInt("stiqer_num")+" Stiqers");
				    level_name.setText(level_name_array[user_info.getInt("level")]);
				    level_icon.setImageResource(level_image_array[user_info.getInt("level")]);
				    next_class_text.setText(next_class_array[user_info.getInt("levelup")]);
				    next_class_icon.setImageResource(next_class_ids[user_info.getInt("levelup")]);
				    
				    if(!obj.getString("rank_list").equals("[]")){
					   costomer_gallery.setAdapter(new SdPaiAdapter(mContext,(List<Rank>) Util.readJson2EntityList(obj.getString("rank_list"), new Rank())));
				    }
				    
				    if(!obj.getString("mission_list").equals("[]")){
					   mission_list.setAdapter(new SdMissionAdapter(mContext,(List<Mission>) Util.readJson2EntityList(obj.getString("mission_list"), new Mission()),StoreDetailActivity.this));
					   mission_list.setExpanded(true);
				    }
				        
				    if(!obj.getString("photo_list").equals("[]")){
					   photo_gallery.setAdapter(new CheckInAdapter(mContext,(List<CheckIn>) Util.readJson2EntityList(obj.getString("photo_list"), new CheckIn())));
				    }
				    //传入值
				    checkin_str = obj.getString("photo_list");
				    rank_str = obj.getString("rank_list");    
				    wrap_layout.setVisibility(View.VISIBLE);

			      }
			    } catch (JSONException e) {

				    e.printStackTrace();
			    }  
			}
			super.onPostExecute(obj);
		}
		
		@Override
		protected JSONObject doInBackground(Integer[]... params) {
			return APIManager.sharedInstance().getStoreDetail(store_id);
		}
    	
    }
	  
    Integer[] level_image_array = {
		R.drawable.ic_class_bronze_feed,
		R.drawable.ic_class_bronze_feed,
		R.drawable.ic_class_sliver_feed,
		R.drawable.ic_class_gold_feed,
		R.drawable.ic_class_diamond_feed,
		R.drawable.ic_class_vip_feed,
		R.drawable.ic_class_vvip_feed,
	};
     
    String[] level_name_array = {
   	    "Bronze","Bronze","Sliver","Gold","Diamond","Vip","Vvip"
    };
    
    String[] next_class_array = {
    	"0% to Next Class","25% to Next Class","50% to Next Class","75% to Next Class"
    };
    
    Integer[] next_class_ids = {
        R.drawable.level_bar_0,R.drawable.level_bar_25, R.drawable.level_bar_50,
		R.drawable.level_bar_75
    };
	    
    public void showPicturePicker(Context context){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setNegativeButton("Cancel", null);
		builder.setItems(new String[]{"Take a new picture","From gallery"}, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				
				case TAKE_PICTURE:
					Uri imageUri = null;
					Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					imageUri = Uri.fromFile(new File(filePath,tempFile));
					//指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
					openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(openCameraIntent, TAKE_PICTURE_CODE);
					break;

				case CHOOSE_PICTURE:					
					Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    openAlbumIntent.setType("image/*");
					startActivityForResult(openAlbumIntent, CHOOSE_PICTURE_CODE);					
					/*
					Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
					openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
					
					Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
				    intent.addCategory(Intent.CATEGORY_OPENABLE);
				    intent.setType("image/*");
				    intent.putExtra("return-data", true);
				    
					Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); 
				    startActivityForResult(intent, CHOOSE_PICTURE_CODE);
					break;
                  */
					
				default:
					break;
				}
			}
		});
		builder.create().show();
	}
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
        	
        	String sdState = Environment.getExternalStorageState();
            if(!(sdState.equals(Environment.MEDIA_MOUNTED)))
            {
                Toast.makeText(this, "sd is not available/writeable right now ", Toast.LENGTH_SHORT).show();
                return;
            }
            
        	switch (requestCode) {
        	
        	case SCAN_RETURN:
        		 String result = data.getExtras().getString("result");
 	            if (TextUtils.isEmpty(result)) {
 	            	Toast.makeText(getApplication(), "扫描出错", 1000).show();
 	                return;
 	            }
 	            ShowScanDialog(result);
 	            break;
 	            
        	case REQUEST_CODE_MISSION:
        		ShowMissionDialog(data.getExtras().getString("pid"),data.getExtras().getString("result"));
        		break;
        		
        	case TAKE_PICTURE_CODE:
        		PicassoTools.clearCache(Picasso.with(mContext));
        		Intent intent = new Intent(StoreDetailActivity.this, CameraActivity.class);
        		intent.putExtra("type", "take_picture");
        		intent.putExtra("store_id", store_id);
        		intent.putExtra("store_name", store_name);
				startActivityForResult(intent,3);
        		break;
        		
        	case CHOOSE_PICTURE_CODE: 
        		PicassoTools.clearCache(Picasso.with(mContext));
        		Intent i = new Intent(StoreDetailActivity.this, CameraActivity.class);
        		Uri originalUri = data.getData(); 
        		i.putExtra("image_uri", originalUri.toString());
        		i.putExtra("type", "choose_picture");
        		i.putExtra("store_id", store_id);
        		i.putExtra("store_name", store_name);
				startActivityForResult(i,3);
        		break;
        		
        	case COMMENT_NUM_REFRESH:
        		 comment_num.setText(data.getExtras().getInt("comment_num")+" Comments");
        		 store_comment_num = data.getExtras().getInt("comment_num");
        		break;
        	}
           
        }
    }
    
    private void ShowScanDialog(final String result){
		 progressDialog.show();
		 new Thread(){
	    	 public void run(){
	    		 scanResult = APIManager.sharedInstance().scanResult(result,latitude,longitude);
	    		 Message msg = mHandler.obtainMessage();
	             msg.what = scanResult.getError_code();
	             mHandler.sendMessage(msg);
	    	 }
	     }.start();
	}
    
    private void ShowMissionDialog(final String mid, final String result){
		 progressDialog.show();
		 new Thread(){
	    	 public void run(){
	    		 missionResult = APIManager.sharedInstance().missionResult(result,mid);
	    		 missionResult.setUid(APIManager.UserID);
	    		 Message msg = mHandler.obtainMessage();
	    		 
	    		 if(missionResult.getError_code() == SCAN_SUCCESS){
	    			 msg.what = MISSION_SCAN_SUCCESS;
	    		 }else{
	    			 msg.what = missionResult.getError_code();
	    		 }
	             mHandler.sendMessage(msg);
	    	 }
	     }.start();
	}
	
	private Handler mHandler = new Handler()
    {
		 
        public void handleMessage(Message msg)
        {
       	 if(progressDialog.isShowing()){
       		 progressDialog.cancel(); 
       	 }
   	     errorHelper.ScanCode(msg.what);
       	 super.handleMessage(msg);
            switch (msg.what)
            {
                case SCAN_SUCCESS:
                  
                  finish();  
                  Intent intent = new Intent(StoreDetailActivity.this, StoreDetailActivity.class);  
                  Bundle mBundle = new Bundle();
                  mBundle.putSerializable("scanResult", scanResult);    
                  mBundle.putSerializable("missionResult", null);
                  mBundle.putString("store_id",scanResult.getTrans_store_id());
                  mBundle.putString("store_name",scanResult.getTrans_store_name());
                  intent.putExtras(mBundle);    
                  startActivity(intent);
                  
                  break;
                  
                case STORE_LIKE_SUCCESSFUL:
                 	like_num.setText((store_like_num+1)+" Likes");
                    break;
                    
                case STORE_LIKE_HAVE:
                	Toast.makeText(mContext, "Have liked", Toast.LENGTH_LONG).show();
                	break;
                	
                case MISSION_NOT_FOUND:
                	Toast.makeText(mContext, "mission not found", Toast.LENGTH_LONG).show();
                	break;
                	
                case MISSION_NOT_FULFILLED:
                	Toast.makeText(mContext, "mission requirement not fulfilled", Toast.LENGTH_LONG).show();
                	break;
                	
                case MISSION_ALREADY_COMPLETED:
                	Toast.makeText(mContext, "mission already completed", Toast.LENGTH_LONG).show();
                	break;
                	
                case WRONG_QR:
                	Toast.makeText(mContext, "wrong store qr for verification", Toast.LENGTH_LONG).show();
                	break;
                	
                case MISSION_SCAN_SUCCESS:
                	finish();  
                    Intent intents = new Intent(StoreDetailActivity.this, StoreDetailActivity.class);  
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("scanResult", null);    
                    bundle.putSerializable("missionResult", missionResult);
                    bundle.putString("store_id",store_id);
                    bundle.putString("store_name",store_name);
                    intents.putExtras(bundle);    
                    startActivity(intents);
                	break;
            }
        };
    };

}
