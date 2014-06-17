package com.gaoxiaoxing.view.edit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class HeaderView extends View {
	
	private float diameter=20;
	
	public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public HeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HeaderView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawOval(canvas);
	}


	private void drawOval(Canvas can) {
		Paint paint = new Paint();
		paint.setColor(Color.GREEN);
		RectF oval = new RectF();
		oval.top = 0;
		oval.left = getLeft() + getWidth() / 4 ;
		oval.right = oval.left +diameter;
		oval.bottom = diameter;
		can.drawOval(oval, paint);
	}

}
