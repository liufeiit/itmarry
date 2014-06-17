package com.test.baidumap.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.RelativeLayout;

import com.gaoxiaoxing.utils.AnimUtils;

public class ButtonGroup extends RelativeLayout {

	private int ChildCount;
	private OnClickListener itemlistener;

	public ButtonGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ButtonGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		init();
	}

	public ButtonGroup(Context context) {
		super(context);
	}

	private void init() {
		ChildCount = getChildCount();
	}
	
	public void setItemOnClickListener(OnClickListener listener){
		this.itemlistener=listener;
		for(int i=0;i<ChildCount;i++){
			getChildAt(i).setOnClickListener(itemlistener);
		}
	}
	
	public void showButton() {
		for (int i = 0; i < ChildCount; i++) {
			AnimationSet set = new AnimationSet(false);
			set.addAnimation(AnimUtils.makeTranslate(i));
			set.addAnimation(AnimUtils.makeAlpha());
			getChildAt(i).setVisibility(View.VISIBLE);
			getChildAt(i).setClickable(true);
			getChildAt(i).startAnimation(set);
		}
	}

	public void hideButton() {
		for (int i = 0; i < ChildCount; i++) {
			getChildAt(i).setVisibility(View.INVISIBLE);
			getChildAt(i).setClickable(false);
		}
	}
	
}
