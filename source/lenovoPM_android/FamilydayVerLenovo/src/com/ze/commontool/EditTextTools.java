package com.ze.commontool;

import android.app.Activity;
import android.text.Selection;
import android.text.Spannable;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


public class EditTextTools {
	public static void hideInputMethod(Activity activity,EditText editText)
	{
		InputMethodManager immInputMethodManager = 
			(InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		if( editText == null )
		{
			immInputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 
					InputMethodManager.HIDE_NOT_ALWAYS);
		}else
		{
			immInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
//		if( immInputMethodManager.isActive() )
//			immInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
	}
	public static void showInputMethod(Activity activity,EditText editText, int select)
	{
		InputMethodManager inputMethodManager = (InputMethodManager)
			activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

		// 接受软键盘输入的编辑文本或其它视图

		
		CharSequence text = editText.getText();
		if( select == -1 || select >= text.length())
		{
			select = text.length();
		}
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable)text;
			Selection.setSelection(spanText, select);
		}
		
		inputMethodManager.showSoftInput(editText,InputMethodManager.SHOW_FORCED);
	}
}
