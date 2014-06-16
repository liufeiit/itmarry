package com.nuatransmedia.androidbook;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

@SuppressWarnings("deprecation")
public class ScreenBrightness extends Plugin {
	private class SetTask implements Runnable{
		private Activity target = null;
		private float tdata=0.5F;
		@Override
		public void run() {
			WindowManager.LayoutParams layoutParams = target.getWindow().getAttributes();
			layoutParams.screenBrightness=tdata;// 0.5F; // set 50% brightness
			target.getWindow().setAttributes(layoutParams);
			target.getWindow().makeActive();
			target.onWindowAttributesChanged(layoutParams);	
			target.getWindow().getDecorView().invalidate();
		}
		
		public void setParams(Activity act,float data ){
			this.target = act;
			this.tdata=data;
		}
	}
	
	@Override
	public PluginResult execute(String arg0, JSONArray arg1, String arg2) {
		// TODO Auto-generated method stub
		Log.e("tag","excute");

		float temp=0.5F;
		try {
			Log.e("tag", arg1.getJSONObject(0).getString("number"));
			temp=Float.valueOf(arg1.getJSONObject(0).getString("number"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SetTask task=new SetTask();
		task.setParams(this.cordova.getActivity(), temp);
		this.cordova.getActivity().runOnUiThread(task);
//		int brightnessMode;
//		try {
//			brightnessMode = Settings.System.getInt(this.cordova.getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
//			if (brightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
//			    Settings.System.putInt(this.cordova.getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
//			}
//		} catch (SettingNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		android.provider.Settings.System.putInt(this.cordova.getActivity().getContentResolver(),android.provider.Settings.System.SCREEN_BRIGHTNESS, (int)(temp*100));
		
		
		
		 PluginResult mPlugin = new PluginResult(PluginResult.Status.OK);
		return mPlugin;
	}

}


