package com.gaoxiaoxing.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaoxiaoxing.R;

public class CustomActionBar extends RelativeLayout {

	private TextView left, right, title;

	public CustomActionBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomActionBar(Context context) {
		super(context);
	}

	private void init() {
		View view = View.inflate(getContext(), R.layout.actionbar_textbutton,
				null);
		this.addView(view);
		left = (TextView) view.findViewById(R.id.left_btn);
		right = (TextView) view.findViewById(R.id.right_btn);
		title = (TextView) view.findViewById(R.id.title);
	}

	public View getLeftView() {
		return left;
	}

	public View getRightView() {
		return right;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		init();
	}

	public void setTitle(String title) {
		this.title.setText(title);
	}

	public void setLeftText(String text) {
		left.setText(text);
	}

	public void setRightText(String text) {
		right.setText(text);
	}

	public void setLeftOnClickListener(OnClickListener l) {
		left.setOnClickListener(l);
	}

	public void setRightOnClickListener(OnClickListener l) {
		right.setOnClickListener(l);
	}

}
