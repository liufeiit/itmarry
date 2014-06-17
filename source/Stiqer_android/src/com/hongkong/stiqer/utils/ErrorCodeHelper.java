package com.hongkong.stiqer.utils;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.ui.GuideActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

public class ErrorCodeHelper {
   private final static int    SERVER_INNER_ERROR = 500;
   private final static int    PARAM_ERROR        = 400;
   private final static int    SOURCE_NOT_FOUND   = 404;
   private final static int    TOKEN_EXPIRES      = 1009;
   
   private final static int    SCAN_MULTIPLE      = 1014;
   private final static int    CODE_NOT_FOUND     = 1015;
   private final static int    USER_NOT_FOUND     = 1012;
   private final static int    STORE_NOT_FOUND    = 1013;
   private final static int    CONNECTION_ERROR   = 999;
   private final static int    UNKNOWN_ERROR      = 1999;
   private final static int    PROMO_NOT_FOUND    = 1026;
   private final static int    PROMO_NOT_REDEEM   = 1027;
   private final static int    PROMO_BE_REDEEMED  = 1028;
   
   private Context mContext;
   
   public ErrorCodeHelper(Context c){
	   this.mContext = c;
   }
   
   public void showToast(int Resource) {
	   Toast.makeText(mContext, mContext.getString(Resource), Toast.LENGTH_LONG).show();
	}
   
   public boolean CommonCode(int code){
	   boolean flag = false;
	   switch(code){
	   case SERVER_INNER_ERROR:
		   showToast(R.string.server_inner_error);
	       break;
	   case PARAM_ERROR:
		   showToast(R.string.params_error);
		   break;
	   case SOURCE_NOT_FOUND:
		   showToast(R.string.resource_not_found);
		   break;
	   case TOKEN_EXPIRES:
		   
		   SharedPreferences sharedPreferences = mContext.getSharedPreferences(Util.TAG, 0);
           Editor editor = sharedPreferences.edit();
           editor.putString("token", "");
           editor.putString("uid", "");
           editor.commit();
           
		   Intent t = new Intent(mContext,GuideActivity.class);
		   mContext.startActivity(t);
		   showToast(R.string.token_expires);
		   break;
	   case USER_NOT_FOUND:
		   showToast(R.string.user_not_found);
		   break;
	   case STORE_NOT_FOUND:
		   showToast(R.string.store_not_found);
		   break;
	   case CONNECTION_ERROR:
		   showToast(R.string.connection_error);
		   break;
	   case UNKNOWN_ERROR:
		   showToast(R.string.unknown_error);
	   default:
		   flag = true;
		   break;
	   }
	   return flag;
   }
   
   public void ScanCode(int code){
	   if(CommonCode(code)){
		   switch(code){
		   case SCAN_MULTIPLE:
			   showToast(R.string.scan_multiple);
		       break;
		   case CODE_NOT_FOUND:
			   showToast(R.string.code_not_found);
		       break;
		   }
	   }
   }
   
   public void connectError(){
	   showToast(R.string.connection_error);
   }
   
   public void loadOver(){
	   showToast(R.string.load_over);
   }

   public void RedeemCode(int code) {
	   if(CommonCode(code)){
		   switch(code){
		   case PROMO_NOT_FOUND:
			   showToast(R.string.promo_not_found);
		       break;
		   case PROMO_NOT_REDEEM:
			   showToast(R.string.promo_not_redeem);
		       break;
		   case PROMO_BE_REDEEMED:
			   showToast(R.string.promo_be_redeemed);
		       break;
		   }
	   }
   }
}
