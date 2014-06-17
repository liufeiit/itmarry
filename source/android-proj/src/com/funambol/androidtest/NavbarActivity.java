package com.funambol.androidtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

public class NavbarActivity extends LinearLayout {
	private Context context;
	boolean check;
	private Activity activity;

	public NavbarActivity(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		activity = (Activity) context;

		if (isInEditMode()) {
			return;
		}

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.actnavbar, this);

		RadioButton radioButton;
		radioButton = (RadioButton) findViewById(R.id.btnAll);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
		radioButton = (RadioButton) findViewById(R.id.btnPicture);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
		radioButton = (RadioButton) findViewById(R.id.btnVideo);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
		radioButton = (RadioButton) findViewById(R.id.btnFile);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
		radioButton = (RadioButton) findViewById(R.id.btnMore);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
	}

	private CompoundButton.OnCheckedChangeListener btnNavBarOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {

				if (buttonView.getId() == R.id.btnAll) {
					Intent intent = new Intent(NavbarActivity.this.context,
							All.class);
					NavbarActivity.this.context.startActivity(intent);
				}
				if (buttonView.getId() == R.id.btnPicture) {
					Intent intent = new Intent(NavbarActivity.this.context,
							Pictrues.class);
					NavbarActivity.this.context.startActivity(intent);
				}
				
				
				

			}
		}
	};
}