package com.dyj.dialog;

import com.dyj.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class CustomerDialog extends AlertDialog {

	private Context context;
	private int viewInt=R.layout.dialog_customer;
	public String message,text_btn_ok;
	public String getText_btn_ok() {
		return text_btn_ok;
	}

	public void setText_btn_ok(String text_btn_ok) {
		this.text_btn_ok = text_btn_ok;
	}

	private TextView text_msg;
	private Button btn_ok,btn_canle;
	public android.view.View.OnClickListener okListener=null;

	public android.view.View.OnClickListener getOkListener() {
		return okListener;
	}

	public void setOkListener(android.view.View.OnClickListener okListener) {
		this.okListener = okListener;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public CustomerDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	public CustomerDialog(Context context,int Viewid) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.viewInt=Viewid;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(viewInt);
		this.setTitle("зЂвт");
		
		this.text_msg=(TextView) findViewById(R.id.text_msg);
		this.btn_ok=(Button) findViewById(R.id.btn_ok);
		this.btn_canle=(Button) findViewById(R.id.btn_canle);
		this.btn_canle.setOnClickListener(new android.view.View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}

			
			
		});
		this.text_msg.setText(this.getMessage());
		this.btn_ok.setText(this.getText_btn_ok());
		this.btn_ok.setOnClickListener(this.okListener);
		
	}

}
