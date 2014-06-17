package com.example.custompopup;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.PopupWindow;

public class MainActivity extends Activity {

	private Activity mine;
	private Handler mHandler = new Handler();
	PopupWindow pop;
	WebView webview;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mine = this;
		Button btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != pop && pop.isShowing()) {
					// 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
					pop.dismiss();
				} else {
					// 弹出PopupWindow的具体代码
					LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
					View view = inflater.inflate(R.layout.popup, null);
					// 创建PopupWindow对象
					pop = new PopupWindow(view, 500, 700, false);
					// 需要设置一下此参数，点击外边可消失
					// 设置点击窗口外边窗口消失
					pop.setOutsideTouchable(false);
					
					// 设置此参数获得焦点，否则无法点击
					webview = (WebView) view.findViewById(R.id.webView1);
					webview.requestFocusFromTouch();
					webview.setWebViewClient(new WebViewClient() {
						@Override
						public void onPageFinished(WebView view, String url) {
							if (null != progressDialog) {
								pop.update(150, 0, 800, 1000);
								progressDialog.dismiss();
							}
						};
					});
					// 设置WebView属性，能够执行Javascript脚本
					webview.getSettings().setJavaScriptEnabled(true);
					// 加载需要显示的网页
					webview.loadUrl("http://www.baidu.com/");
					progressDialog = ProgressDialog.show(view.getContext(),
							null, "页面加载中，请稍后..");
					progressDialog.setCanceledOnTouchOutside(true);
					//pop.showAsDropDown(v);
					pop.showAtLocation(mine.getWindow().getDecorView(),Gravity.NO_GRAVITY , 0, 0);
				}
			}
		});
	}
}
