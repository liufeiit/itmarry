package com.dangf.hanhai;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.annotation.SuppressLint;
import android.app.Activity;

/**
 * 最新文章布局、操作
 * @author Hello_海生
 * @date 2014-3-28
 */
@SuppressLint("SetJavaScriptEnabled")
public class NewArticleActivity extends Activity {
	private WebView newListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_article);
		
		newListView = (WebView)findViewById(R.id.new_article_webView);
		newListView.getSettings().setJavaScriptEnabled(true);  
		
        //wView.loadUrl("file:///android_asset/index.html");    
        //wView.loadUrl("content://com.android.htmlfileprovider/sdcard/index.html");  
		newListView.loadUrl("file:///android_asset/newArticle.html");
        //newListView.loadUrl("http://wap.baidu.com");
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {       
        if ((keyCode == KeyEvent.KEYCODE_BACK) && newListView.canGoBack()) {       
        	newListView.goBack();       
                   return true;       
        }     
        return false;
        //return super.onKeyDown(keyCode, event);       
    }   

}
