package com.jingtuo.android.fragment;

import com.jingtuo.android.common.app.BaseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class NavigationFragment extends BaseFragment{
	
	public static final String KEY_TARGET_FRAGMENT_CLASS_NAME = "Target Fragment Class Name";

	protected String targetFragmentClassName = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(arguments!=null){
			targetFragmentClassName = arguments.getString(KEY_TARGET_FRAGMENT_CLASS_NAME, "");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	public String getTargetFragmentClassName(){
		return targetFragmentClassName;
	}
	
	public void select(String tagName, Class<BaseFragment> target){
		
	}
	
}
