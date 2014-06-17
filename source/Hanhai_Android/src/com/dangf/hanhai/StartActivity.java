package com.dangf.hanhai;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.WindowManager;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		//设置 图片为全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_start);
		
		//设置N秒后自动跳转
		new Handler().postDelayed(new Runnable() {
			// 为了减少代码使用匿名Handler创建一个延时的调用
			public void run() {
				Intent i = new Intent(StartActivity.this, MainActivity.class);
				// 通过Intent打开最终真正的主界面Main这个Activity
				StartActivity.this.startActivity(i); // 启动Main界面
				StartActivity.this.finish(); // 关闭自己这个开场屏
			}
		}, 3000); // 3秒，够用了吧

	}

}
