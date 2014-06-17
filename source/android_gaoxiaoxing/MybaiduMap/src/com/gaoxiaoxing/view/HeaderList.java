package com.gaoxiaoxing.view;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaoxiaoxing.R;
import com.gaoxiaoxing.model.SchoolInfo;

public class HeaderList extends RelativeLayout{

	private TextView msg;
	private SchoolInfo data;
	private String text;


	public HeaderList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public HeaderList(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HeaderList(Context context) {
		super(context);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		msg=(TextView) findViewById(R.id.school_msg);
	}

	private void initText(String title) {
		String a = "´Ó";
		String b = "³ö·¢";
		CharSequence cs = Html.fromHtml(a
				+ "<html><font color=\"#ffffff\"><h4>" + title
				+ "</h4><font></html>" + b);
		msg.setText(cs);
	}


	public void setData(SchoolInfo data) {
		this.data = data;
		initText(data.getTitle());
	}

}
