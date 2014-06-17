package com.guide.geek.activity;

import com.guide.geek.R;
import com.guide.geek.R.id;
import com.guide.geek.R.layout;
import com.guide.geek.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

public class BrowserActivity extends Activity {
	private WebView webView;
	private String url = "http://www.baidu.com";
	private ImageView webview_reload;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		// Request a window feature to show progress bar in the application
		// title
		requestWindowFeature(Window.FEATURE_PROGRESS);

		setContentView(R.layout.browser_nav_bar);
		setProgressBarVisibility(true);

		webView = (WebView) findViewById(R.id.webview);
	    webview_reload = (ImageView) findViewById(R.id.webview_reload);
		webView.setWebViewClient(new WebViewClient() {
			// Load opened URL in the application instead of standard browser
			// application
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			} 
			@Override   //转向错误时的处理  
            public void onReceivedError(WebView view, int errorCode,  
                    String description, String failingUrl) {  
                // TODO Auto-generated method stub 
				webView.setVisibility(View.GONE);
				webView.setBackgroundColor(0);//先设置背景色为transparent
				webView.setBackgroundResource(R.drawable.reload);//然后设置背景图片

				webview_reload.setVisibility(View.VISIBLE);//让隐藏的点击刷新的图片展示出来
                Toast.makeText(BrowserActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();  
            }  
		});

		webView.setWebChromeClient(new WebChromeClient() {
			// Set progress bar during loading
			public void onProgressChanged(WebView view, int progress) {
				BrowserActivity.this.setProgress(progress * 100);
			}
		});

		// Enable some feature like Javascript and pinch zoom
		WebSettings websettings = webView.getSettings();
		websettings.setJavaScriptEnabled(true);						// Warning! You can have XSS vulnerabilities!
		//websettings.setBuiltInZoomControls(true);

	
		webView.loadUrl(url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Create a menu that is append to main activity
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection (every case is a different option ID)
		switch (item.getItemId()) {
//		case R.id.bookmark:
//			Intent intentBookmark = new Intent(BrowserActivity.this,
//					BookmarkActivity.class);
//			startActivity(intentBookmark);
//			return true;
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@Override   //默认点回退键，会退出Activity，需监听按键操作，使回退在WebView内发生  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        // TODO Auto-generated method stub  
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {  
            webView.goBack();  
            return true;  
        }  
        return super.onKeyDown(keyCode, event);  
    }  
	@Override
	public void onResume() {
		super.onResume();
		// Reload URL
		String bookmarkUrl = getIntent().getStringExtra("url");
		if (bookmarkUrl == null) {
			webView.loadUrl(url);
		} else {
			webView.loadUrl(bookmarkUrl);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/*
	 * WebView methods
	 */

	public void go(View v) {
		webView.loadUrl(url);
	}
	public void commentPost(View v) {
		//webView.loadUrl(url);
		Toast.makeText(getApplicationContext(), "评论", Toast.LENGTH_SHORT).show();
	}
	public void reload(View v) {
		webView.reload();
	}

	public void stop(View v) {
		webView.stopLoading();
	}

	public void back(View v) {
		webView.goBack();
	}

	public void forward(View v) {
		webView.goForward();
	}
}
