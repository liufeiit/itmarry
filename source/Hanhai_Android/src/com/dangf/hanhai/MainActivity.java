package com.dangf.hanhai;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.app.TabActivity;
import android.content.Intent;
/**
 * 主 布局 界面
 * @author Hello_海生
 * @date 2014-3-28
 */
@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity implements android.widget.RadioGroup.OnCheckedChangeListener {
	private TextView main_title;
	private RadioGroup radioGroup;
	private TabHost mtabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//设置服务路径
		//String url="http://192.168.1.10/DangFServer/article/list";
		//List<NameValuePair> params=new ArrayList<NameValuePair>();
		//params.add(new BasicNameValuePair("start","0"));
		//params.add(new BasicNameValuePair("count","10"));
		//new HttpThread(url, params);	//启动线程
		
		main_title=(TextView) findViewById(R.id.tiltleText);	//获取标题
		radioGroup=(RadioGroup) findViewById(R.id.main_menu);	//获取菜单
		
		mtabHost=this.getTabHost();
		mtabHost.addTab(mtabHost.newTabSpec("NewArticle").setIndicator("NewArticle")
				.setContent(new Intent(this, NewArticleActivity.class)));
		
		mtabHost.addTab(mtabHost.newTabSpec("TWO").setIndicator("TWO")
				.setContent(new Intent(this, erActivity.class)));
		
		mtabHost.addTab(mtabHost.newTabSpec("THREE").setIndicator("THREE")
				.setContent(new Intent(this, sanActivity.class)));
		
		mtabHost.addTab(mtabHost.newTabSpec("FOUR").setIndicator("FOUR")
				.setContent(new Intent(this, siActivity.class)));
		
		mtabHost.addTab(mtabHost.newTabSpec("FIVE").setIndicator("FIVE")
				.setContent(new Intent(this, wuActivity.class)));
		
		radioGroup.setOnCheckedChangeListener(MainActivity.this);// 为RadioGroup设置监听事件
		
		
	}
	
	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		switch (arg1)
		{
		case R.id.radio0:
			mtabHost.setCurrentTabByTag("NewArticle");// 设置为当前选中的页面
			main_title.setText("最新内容");// 设置标题
			break;
		case R.id.radio1:
			mtabHost.setCurrentTabByTag("TWO");
			main_title.setText("嘻嘻");
			
			break;
		case R.id.radio2:
			mtabHost.setCurrentTabByTag("THREE");
			main_title.setText("嘎嘎");
			// SearchAty.searchAty.mHandler.sendEmptyMessage(1);

			break;
		case R.id.radio3:
			mtabHost.setCurrentTabByTag("FOUR");
			main_title.setText("哼哼");

			break;
		case R.id.radio4:
			mtabHost.setCurrentTabByTag("FIVE");
			main_title.setText("哇哇");
			break;
		
	}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {       

        return false;
        //return super.onKeyDown(keyCode, event);       
    }
	
}
