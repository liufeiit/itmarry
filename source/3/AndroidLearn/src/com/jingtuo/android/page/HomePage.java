package com.jingtuo.android.page;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import com.jingtuo.android.R;
import com.jingtuo.android.common.app.BaseActivity;
import com.jingtuo.android.fragment.NavigationFragment;

public class HomePage extends BaseActivity {
	
	private DrawerLayout drawerLayout;
	
	private NavigationFragment navigationFragment;
	
	private FragmentManager fm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		init();
	}
	
	public void init(){
		fm = getFragmentManager();
		drawerLayout = (DrawerLayout)findViewById(R.id.home_drawer_layout);
		navigationFragment = (NavigationFragment)fm.findFragmentById(R.id.home_navigation);
		
	}
	
}
