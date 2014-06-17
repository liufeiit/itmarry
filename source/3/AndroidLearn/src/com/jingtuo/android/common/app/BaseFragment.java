package com.jingtuo.android.common.app;

import android.app.Fragment;
import android.os.Bundle;

public class BaseFragment extends Fragment {

	public static final String KEY_TAG_NAME = "tag name";
	
	protected String tagName = "";
	
	protected Bundle arguments;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		arguments = getArguments();
		if(arguments!=null){
			tagName = arguments.getString(KEY_TAG_NAME, "");
		}
	}

	public String getTagName() {
		return tagName;
	}
	
	
	public interface OnFragmentListener{
		
	}
	
}
