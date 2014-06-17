package com.hongkong.stiqer.fragment;

import java.util.ArrayList;
import java.util.List;

import com.google.zxing.CaptureActivity;
import com.hongkong.stiqer.R;
import com.hongkong.stiqer.adapter.FavAdapter;
import com.hongkong.stiqer.db.FavDao;
import com.hongkong.stiqer.entity.Fav;
import com.hongkong.stiqer.entity.ScanResult;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.ui.StoreDetailActivity;
import com.hongkong.stiqer.utils.CacheHelper;
import com.hongkong.stiqer.utils.ErrorCodeHelper;
import com.hongkong.stiqer.utils.MyLocation;
import com.hongkong.stiqer.widget.CustomDialog;
import com.hongkong.stiqer.widget.DialogListener;
import com.hongkong.stiqer.widget.ExpandableHeightListView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class FavFragment extends Fragment {
	public static final String TAG = "Siqer";  
	private final static int    PROMOTION_SAVE_SUCCESS = 1001;
	private final static int    PROMOTION_SCAN_SUCCESS = 1000;
	private final static int    FAVOR_NOT_FOUND = 1024;
	private final static int    PERMISSION_DENY = 1025;
	private final static int    DELETE_SUCESS = 1030;
	private final static int    REQUEST_CODE_FAV = 107;
	private final static int    DELETE_STORE_FAV_TYPE = 1;
	private final static int    DELETE_PROM_FAV_TYPE = 2;
    private View view;
    private ExpandableHeightListView  listStore,listPromotion;
    ProgressBar             progressbar;
    LinearLayout            fav_wrap_layout;
    Context                 mContext;
    FavDao                  favDao;
    List<Fav>               storeList, promList;
    CustomDialog            PromDialog,deleteDialog;
	DialogListener          dialogListener,deleteListener;
	FavAdapter              storeAdapter,promAdapter;
	int                     delete_position=0;
	EditText                fav_search_edit;
	DialogListener          addDialogListener;
	CustomDialog            progressDialog,returnDialog;
	MyLocation              myLocation;
	ScanResult              scanResult;
	double   longitude = 0.0,latitude = 0.0;
	int                     delete_type;
	String                  delete_id;
	
    public static FavFragment newInstance(String content){
    	FavFragment fragment = new FavFragment();  
        Bundle args = new Bundle();  
        args.putString(TAG, content);  
        fragment.setArguments(args);  
        return fragment;  
    }  
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        Bundle bundle = getArguments();  
    }  
  
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        view = inflater.inflate(R.layout.fragment_fav, container, false);
        initView();
        if(false){
        	ShowFromDatabase();
        }else{
        	favDao.deleteAll();
        	new GetDataTask().execute();
        }
        return view;  
    }  
   
    private void initView() {  
    	mContext = getActivity();
    	favDao = new FavDao(mContext);
    	fav_search_edit = (EditText) view.findViewById(R.id.fav_search_edit);
    	listStore = (ExpandableHeightListView) view.findViewById(R.id.fav_listview_store);
    	listPromotion = (ExpandableHeightListView) view.findViewById(R.id.fav_listview_promotion);
    	progressDialog = CustomDialog.createProgressDialog(getActivity(),"扫描成功，请稍后~");
    	progressbar = (ProgressBar) view.findViewById(R.id.fav_progressbar);
    	fav_wrap_layout = (LinearLayout) view.findViewById(R.id.fav_wrap_layout); 	
    	
    	listStore.setOnItemClickListener(new StoreOnItemClickListener());
    	listPromotion.setOnItemClickListener(new PromOnItemClickListener());
    	
    	myLocation = new MyLocation(mContext);
		longitude = myLocation.getLatitude();
		latitude = myLocation.getLongitude();
		
    	listStore.setOnItemLongClickListener(new StoreOnItemLongClickListener());
    	listPromotion.setOnItemLongClickListener(new PromOnItemLongClickListener());
    	
    	fav_search_edit.addTextChangedListener(new MyTextWatcher());
    	dialogListener = new DialogListener(){
			public void showDialog(Object o) {
				String p[] = ((String) o).split(",");
				if(p[0].equals("save")){
					//doPromSave(p[1]);
				}else{
					doPromRedeem(p[1]);
				}
			}
		};
		deleteListener = new DialogListener(){
			public void showDialog(Object o) {
				new Thread(){
					public void run(){
						int code = APIManager.sharedInstance().DeleteFavItem(delete_id);
						Message msg = mHandler.obtainMessage();
						if(code == 1000){
							code = DELETE_SUCESS;
						}
						msg.what = code;
			            mHandler.sendMessage(msg);
					}
				}.start();
			}
		};
    } 
    
    public void doPromRedeem(String pid){
    	Intent intent = new Intent(getActivity(), CaptureActivity.class);
    	getActivity().startActivityForResult(intent,REQUEST_CODE_FAV);
    }
    
    class MyTextWatcher implements TextWatcher{

		@Override
		public void afterTextChanged(Editable s) {
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			//Log.d("Stiqer", "beforeTextChanged:" + s + "-" + start + "-" + count + "-" + after);  
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before,int count) {
			//Log.d("Stiqer", "onTextChanged:" + s + "-" + "-" + start + "-" + before + "-" + count);
			searchFav(s);
		}
    	
    }
    
    public void searchFav(CharSequence keyword){
    	storeList = favDao.query(1,keyword.toString());
    	promList = favDao.query(2,keyword.toString());
        listStore.setAdapter(new FavAdapter(mContext,storeList,1));
    	listPromotion.setAdapter(new FavAdapter(mContext,promList,2));
    	listStore.setExpanded(true);
    	listPromotion.setExpanded(true);
    }
    
    
    class StoreOnItemClickListener implements OnItemClickListener{
		public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
			Intent t = new Intent(getActivity(),StoreDetailActivity.class);
			t.putExtra("store_id", storeList.get(position).getStore_id());
			t.putExtra("store_name", storeList.get(position).getStore_name());
			getActivity().startActivity(t);
		}
    }
    
    class PromOnItemClickListener implements OnItemClickListener{
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
			PromDialog = CustomDialog.createPromDialog(mContext,promList.get(position).getPromo_id(),promList.get(position).getPromo_des(),promList.get(position).getPromo_img(),dialogListener,1);
      	    PromDialog.show();
      	    
		}
    }
    
    class StoreOnItemLongClickListener implements OnItemLongClickListener{
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
			deleteDialog = CustomDialog.createFavDeleteDialog(mContext,deleteListener);
			deleteDialog.show();
			delete_position = position;
			delete_type = DELETE_STORE_FAV_TYPE;
			delete_id = storeList.get(position).getFavor_id();
			return false;
		}
    }
    
    class PromOnItemLongClickListener implements OnItemLongClickListener{
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
			deleteDialog = CustomDialog.createFavDeleteDialog(mContext,deleteListener);
			deleteDialog.show();
			delete_position = position;
			delete_type = DELETE_PROM_FAV_TYPE;
			delete_id = promList.get(position).getFavor_id();
			return false;
		}
    }
    
    private class GetDataTask extends AsyncTask<Integer[],Void,List<Fav>>{
		@Override
		protected void onPostExecute(List<Fav> result) {
			ErrorCodeHelper errorHelper = new ErrorCodeHelper(mContext);
			if(result!=null){
				if(result.size()>0){
					if(errorHelper.CommonCode(result.get(0).getError_code())){
						
						favDao.add(result);
					}
				}
			}else{
				errorHelper.connectError();
			}
			ShowFromDatabase();
			super.onPostExecute(result);
		}
		@Override
		protected List<Fav> doInBackground(Integer[]... params) {
			return APIManager.sharedInstance().getFavList();
		}
    }
    
    private void ShowFromDatabase(){
    	storeList = favDao.query(1,"");
    	//promList = favDao.query(2,"");
    	//if(storeList.size()==0){
    		//Toast.makeText(mContext, "No favorite now...", Toast.LENGTH_SHORT).show();
    	//}else{
    		listStore.setAdapter(new FavAdapter(mContext,storeList,1));
    	//}
    	//listPromotion.setAdapter(new FavAdapter(mContext,promList,2));
    	listStore.setExpanded(true);
    	//listPromotion.setExpanded(true);
        progressbar.setVisibility(View.GONE);
    	fav_wrap_layout.setVisibility(View.VISIBLE);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CODE_FAV:
			ShowScanDialog(data.getExtras().getString("result"));
			break;
    	}
	}	
	
	 private void initShare() {
	 		addDialogListener = new DialogListener()
	         {
	             public void showDialog(Object o)
	             {
	                 Log.e("Stiqer","share="+ (String) o);
	             }
	         };
	 	}
	 
	 private void ShowScanDialog(final String result){
		 
		 progressDialog.show();
		 new Thread(){
	    	 public void run(){
	    		 scanResult = APIManager.sharedInstance().scanResult(result,latitude,longitude);
	    		 Message msg = mHandler.obtainMessage();
	             msg.what = PROMOTION_SCAN_SUCCESS;
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
        	 ErrorCodeHelper errorHelper = new ErrorCodeHelper(getActivity());
        	 errorHelper.ScanCode(msg.what);
        	 super.handleMessage(msg);
             switch (msg.what)
             {
                 case PROMOTION_SCAN_SUCCESS:
                	returnDialog = CustomDialog.createPromRedeemDialog(getActivity(),scanResult.getTrans_des());
                 	returnDialog.show();
                    break;
                 case FAVOR_NOT_FOUND:
                	 Toast.makeText(mContext, getString(R.string.favor_not_found), Toast.LENGTH_SHORT).show();
                	 break;
                 case PERMISSION_DENY:
                	 Toast.makeText(mContext, getString(R.string.permission_deny), Toast.LENGTH_SHORT).show();
                	 break;
                 case DELETE_SUCESS:
                	 Toast.makeText(mContext, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                	 if(delete_type == DELETE_STORE_FAV_TYPE){
     					storeList.remove(delete_position);
     					listStore.setAdapter(new FavAdapter(mContext,storeList,1));
     					listStore.setExpanded(true);
     				}else{
     					promList.remove(delete_position);
     					listPromotion.setAdapter(new FavAdapter(mContext,promList,2));
                     	listPromotion.setExpanded(true);
     				}
                	break;
             }
         };
     };

}
