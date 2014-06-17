package com.guide.geek.app;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.guide.geek.R;
import com.guide.geek.R.dimen;
import com.guide.geek.R.drawable;
import com.guide.geek.R.id;
import com.guide.geek.R.layout;
import com.guide.geek.R.string;
import com.guide.geek.utils.ActivityUtils;
import com.guide.geek.utils.WarnUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
public class MainBakActivity extends FragmentActivity {
	private SlidingMenu menu;
	
	private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slidingmenu_main);
        
        menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		menu.setFadeDegree(0.35f);
		menu.setBehindOffset(dm.widthPixels*30/100);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		
		
		menu.setMode(SlidingMenu.LEFT_RIGHT);//这里模式设置为左右都有菜单，RIGHT显示右菜单，LEFT显示左菜单，LEFT_RIGH显示左右菜单
		menu.setContent(R.layout.slidingmenu_content);
		menu.setMenu(R.layout.slidingmenu_menu);
		
		menu.setSecondaryMenu(R.layout.slidingmenu_menu_2);
		//menu.setSecondaryShadowDrawable(R.drawable.slidingmenu_shadow_2);
		
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.slidingmenu_content, new MainFragment(menu))
			.commit();//设置主页面内容
		
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.slidingmenu_menu, new MainLeftFragment())
			.commit();//设置左菜单内容
		
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.slidingmenu_menu_2, new MainRightFragment(menu))
			.commit();//设置右菜单内容
       
    }

    @Override
    protected void onResume() {
    	super.onResume();
    	ActivityUtils.clearAll();
    }
    
    private long currentTime;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(System.currentTimeMillis() - currentTime < 2000){
				this.finish();
			}else{
				WarnUtils.toast(this, R.string.warn_wether_to_exit);
			}
			currentTime = System.currentTimeMillis();
			return true;
		}
		return false;
	}

}
