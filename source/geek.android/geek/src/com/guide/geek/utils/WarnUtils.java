package com.guide.geek.utils;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * 所有提示都放到这里
 * @author liudongqiu
 *
 */
public class WarnUtils {
	
	
	
	public static final void showDialog(Context context, String text){
		new AlertDialog.Builder(context).setMessage(text).setNegativeButton("关闭", null).create().show();
	}
	public interface OnClickListener extends DialogInterface.OnClickListener {

	}

	public static final void toast(Context context, int textId){
		toast(context, context.getResources().getString(textId));
	}
	public static final void toast(Context context, String text){
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}
