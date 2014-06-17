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
import com.hongkong.stiqer.ui.GuideActivity;
import com.hongkong.stiqer.ui.StoreDetailActivity;
import com.hongkong.stiqer.utils.CacheHelper;
import com.hongkong.stiqer.utils.ErrorCodeHelper;
import com.hongkong.stiqer.utils.MyLocation;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.CustomDialog;
import com.hongkong.stiqer.widget.DialogListener;
import com.hongkong.stiqer.widget.ExpandableHeightListView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SettingFragment extends Fragment {
	View view;
	CacheHelper cacheHelper;
	CustomDialog  progressDialog;
    public static SettingFragment newInstance(String content){
    	SettingFragment fragment = new SettingFragment();  
        Bundle args = new Bundle();  
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
        view = inflater.inflate(R.layout.fragment_setting, container, false);  
        initView();
        return view;  
    }  
   
    private void initView() {  
    	cacheHelper = new CacheHelper(getActivity());
    	progressDialog = CustomDialog.createProgressDialog(getActivity(), "Logout.....");
    	Button btnLogout = (Button) view.findViewById(R.id.setting_logout);
    	btnLogout.setOnTouchListener(Util.TouchDark);
        btnLogout.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				progressDialog.show();
				new Thread(){
				   public void run(){ 
					   int code = APIManager.sharedInstance().uesrLogout();
					   Message msg = mHandler.obtainMessage();
			    	   msg.what = code;
			           mHandler.sendMessage(msg);
				   }
				}.start();	
			}
        });
    } 
   
    private Handler mHandler = new Handler()
    {
		 
        public void handleMessage(Message msg)
        {

       	    super.handleMessage(msg);
       	    progressDialog.cancel();
       	    ErrorCodeHelper errorHelper = new ErrorCodeHelper(getActivity());
       	    if(errorHelper.CommonCode(msg.what)){
       	     switch (msg.what)
             {
             case 1000:
             	SharedPreferences loginPreferences = getActivity().getSharedPreferences(Util.TAG, 0);
 				Editor editor = loginPreferences.edit();
 				editor.putString("uid", "");
 				editor.putString("token", "");
 				editor.putString("phone_verified", "");
 				editor.commit();
 				cacheHelper.clearCacheTotal();
 				Intent t = new Intent(getActivity(),GuideActivity.class);
 				getActivity().startActivity(t);
 				getActivity().finish();
 				System.exit(0);
             	break;
             }	
       	   }
        }
    };


}
