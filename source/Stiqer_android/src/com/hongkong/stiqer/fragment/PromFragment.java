package com.hongkong.stiqer.fragment;

import java.util.List;

import com.aphidmobile.flip.FlipViewController;
import com.hongkong.stiqer.R;
import com.hongkong.stiqer.adapter.PromAdapter;
import com.hongkong.stiqer.entity.Prom;
import com.hongkong.stiqer.entity.Redeem;
import com.hongkong.stiqer.entity.ScanResult;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.utils.ErrorCodeHelper;
import com.hongkong.stiqer.utils.MyLocation;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.CustomDialog;
import com.hongkong.stiqer.widget.DialogListener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PromFragment extends Fragment {
	
	private FlipViewController  flipView;
	
	public static final String  TAG = "Siqer";  
	private final static int    REQUEST_CODE_PROMOTION = 106;
	private final static int    PROMOTION_SCAN_SUCCESS = 1000;
	CustomDialog                progressDialog,returnDialog;
	MyLocation                  myLocation;
	DialogListener              addDialogListener;
	ErrorCodeHelper             errorHelper;
	Redeem                      redeemResult;
	double                      longitude = 0.0,latitude = 0.0;

    public static PromFragment newInstance(String content) {  
    	PromFragment fragment = new PromFragment();  
        Bundle args = new Bundle();  
        args.putString(TAG, content);  
        fragment.setArguments(args);  
        return fragment;  
    }  
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
    }  
  
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {  
    	if (flipView != null)
        {
            ((ViewGroup) flipView.getParent()).removeView(flipView);
            return flipView;
        }
    	initView();
    	new GetDataTask().execute();
        return flipView; 
    }  

    
    private void initView() {
    	flipView = new FlipViewController(getActivity());
        flipView.setAnimationBitmapFormat(Bitmap.Config.RGB_565);
        errorHelper = new ErrorCodeHelper(getActivity());
        progressDialog = CustomDialog.createProgressDialog(getActivity(),"Loading Data...");
        progressDialog.show();		
        myLocation = new MyLocation(getActivity());
		longitude = myLocation.getLatitude();
		latitude = myLocation.getLongitude();
        initShare();
	}

	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	private class GetDataTask extends AsyncTask<Integer[],Void,List<Prom>>{

		@Override
		protected List<Prom> doInBackground(Integer[]... params) {
			return APIManager.sharedInstance().GetPromList();
		}
		
		protected void onPostExecute(List<Prom> result) {
			progressDialog.dismiss();
			if(result!=null){
				if(result.size() == 0){
	               Prom prom = new Prom();
	               prom.setDefault_img(R.drawable.promotion_dummy);
	               prom.setError_code(100);
	               result.add(prom);
		       	}
				
				if(errorHelper.CommonCode(result.get(0).getError_code())){
					flipView.setAdapter(new PromAdapter(getActivity(),result, getActivity()));
				}
			 }else{
				errorHelper.connectError();
			 }
			
			if(result!=null && result.size()>0){
				if(errorHelper.CommonCode(result.get(0).getError_code())){
					
				}
			}
			super.onPostExecute(result);
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CODE_PROMOTION:
			if(data != null){
				ShowScanDialog(data.getExtras().getString("result"), data.getExtras().getString("pid"));
			}
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
	 
	 private void ShowScanDialog(final String result, final String pid){
		 progressDialog = CustomDialog.createProgressDialog(getActivity(),"Redeem Successful, please Waiting~");
		 progressDialog.show();
		 new Thread(){
	    	 public void run(){
	    		 redeemResult = APIManager.sharedInstance().getRedeem(result,pid);
	    		 Message msg = mHandler.obtainMessage();
	             msg.what = redeemResult.getError_code();
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
        	 errorHelper.RedeemCode(msg.what);
        	 super.handleMessage(msg);
             switch (msg.what)
             {
                 case PROMOTION_SCAN_SUCCESS:
                	returnDialog = CustomDialog.createPromRedeemDialog(getActivity(),"Redeem Successful");
                 	returnDialog.show();
                    break;
             }
         };
     };
}
