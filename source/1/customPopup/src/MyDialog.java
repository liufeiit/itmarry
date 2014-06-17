import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupWindow;

import com.unity3d.player.UnityPlayer;
import com.zhm.custompopup.R;

/**
 * Custom popup dialog
 * 
 * @author zhangming
 */
public class MyDialog {
	private static String gameObjectName;
	private static String callback;
	private int realX;
	private int realY;
	private int realWidth;
	private int realHeight;
	private Activity unityActivity;
	private PopupWindow popupWindow;
	private WebView webView;
	private String currentUrl;
	private ProgressDialog progressDialog;

	public void setGameObjectName(String objectName) {
		gameObjectName = objectName;
	}

	public void setCallbackFunc(String callbackFunc) {
		callback = callbackFunc;
	}

	public void setSize(float p_x, float p_y, float p_width, float p_height) {
		realX = (int) p_x;
		realY = (int) p_y;
		realWidth = (int) p_width;
		realHeight = (int) p_height;
	}

	public void initWebView(String p_url) {
		currentUrl = p_url;
		if (unityActivity == null) {
			unityActivity = UnityPlayer.currentActivity;
			// Init popupWindow
			LayoutInflater inflater = (LayoutInflater) unityActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.popupwindow, null);
			popupWindow = new PopupWindow(view, realWidth, realHeight, false);
			popupWindow.setOutsideTouchable(false);
			// Cannot focus on this popupWindow, or the untiyActivity is unable,
			// if have these require , use fragment.
			popupWindow.setFocusable(false);
			popupWindow.update(realX, realY, realWidth, realHeight);
			// Init webview
			webView = (WebView) view.findViewById(R.id.contentWebView);
			webView.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					loadUrl(url);
					return true;
				}

				@Override
				public void onPageFinished(WebView view, String url) {
					if (null != progressDialog) {
						progressDialog.dismiss();
					}
				};
			});
			webView.getSettings().setJavaScriptEnabled(true);
			popupWindow.showAtLocation(unityActivity.getWindow().getDecorView(), Gravity.NO_GRAVITY, realX, realY);
		}
	}

	public void loadUrl(String url) {
		currentUrl = url;
		if(null == unityActivity)
		{
			initWebView(url);
		}
		if (null == progressDialog){
			progressDialog = ProgressDialog.show(unityActivity, null, "页面加载中，请稍后..");
			progressDialog.setCanceledOnTouchOutside(true);
			webView.loadUrl(url);
		}
		/*
		 * if (progress == null) { progress = ProgressDialog.show(unityActivity,
		 * null, "Loading..."); } progress.show(); currentUrl = url;
		 * webView.loadUrl(url);
		 */
	}
}
