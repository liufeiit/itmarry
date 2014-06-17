package com.haier.control.application.test;

import java.util.ArrayList;
import java.util.List;

import com.haier.control.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

public class MySurfaceView extends SurfaceView {

	private ArcTrace mTrace;
	private double currentDegree = START_ARC;
	private float imageScallMax = 1.0f;
	private float imageScallCope = 0.6f;
	private static final double START_ARC = 0d;
	private static final int visibleCount = 5;
	private int[] indexs = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	private double arc;
	private double circleDegree;
	private double totalDegree;
	private List<RotaryItem> rotaryItems = new ArrayList<RotaryItem>(
			visibleCount);

	public MySurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public MySurfaceView(Context context) {
		super(context);
		initialize();
	}

	private void initialize() {
		getHolder().addCallback(mCallBack);
	}

	private SurfaceHolder.Callback mCallBack = new SurfaceHolder.Callback() {

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			Log.d(VIEW_LOG_TAG, " surfaceCreated.. ");
			startDraw();
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			Log.d(VIEW_LOG_TAG, " surfaceChanged.. ");
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			mDrawing = false;
			mThread.interrupt();
			Log.d(VIEW_LOG_TAG, " surfaceDestroyed.. ");
		}
	};

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		for (int i = 0; i < visibleCount; i++) {
			rotaryItems.add(new RotaryItem());
		}
		String[] icons = getResources().getStringArray(R.array.icon_array);
		mImages = new ImageView[icons.length + 1];
		for (int i = 0; i < icons.length; i++) {

			ImageView image = new ImageView(getContext());
			int identifier = getResources().getIdentifier(icons[i], "drawable",
					getContext().getPackageName());
			image.setImageResource(identifier);
			if (i == 1) {
				image.setBackgroundColor(Color.BLACK);
			}
			image.measure(200, 200);
			image.layout(0, 0, 200, 200);
			image.requestLayout();
			mImages[i] = image;
		}
		ImageView image = new ImageView(getContext());
		int identifier = getResources().getIdentifier(icons[1], "drawable",
				getContext().getPackageName());
		image.setImageResource(identifier);
		image.setBackgroundColor(Color.RED);
		image.measure(200, 200);
		image.layout(0, 0, 200, 200);
		image.requestLayout();
		mImages[4] = image;

		Log.d(VIEW_LOG_TAG, " onAttachedToWindow.. ");
	}

	private Thread mThread;
	private boolean mDrawing;
	int height = 0;
	private ImageView[] mImages;

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

	private void startDraw() {
		mDrawing = true;
		mThread = new Thread() {

			@Override
			public void run() {
				while (true) {
					if (mDrawing) {
						doDraw();
						// try {
						// Thread.sleep(500);
						// } catch (InterruptedException e) {
						// // TODO Auto-generated catch block
						// e.printStackTrace();
						// }
					}
				}
			}

		};
		mThread.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	private void doDraw() {
		SurfaceHolder holder = getHolder();
		if (mDrawing) {
			Canvas canvas = holder.lockCanvas(null);
			if (canvas != null) {
				if (mTrace == null) {
					mTrace = new ArcTrace(
							canvas.getWidth()
									- (int) (mImages[0].getWidth() * (imageScallMax - imageScallCope)),
							canvas.getHeight()
									- (int) (mImages[0].getHeight() * (imageScallMax - imageScallCope)));
					arc = (mTrace.getArcDegree() - START_ARC * 2)
							/ (visibleCount - 1);
					currentDegree += arc * 8;
					circleDegree = visibleCount * arc;
					totalDegree = indexs.length * arc;
				}
				canvas.drawColor(Color.BLUE);

				canvas.save();
				currentDegree -= 0.2;
				if (currentDegree > totalDegree) {
					currentDegree %= totalDegree;
				} else if (currentDegree < 0) {
					currentDegree += totalDegree;
				}
				double degree = currentDegree % circleDegree;
				for (int i = 0; i < visibleCount; i++) {
					canvas.save();
					double tempDegree = degree + i * arc;
					if (tempDegree > circleDegree) {
						tempDegree %= (circleDegree);
					} else if (tempDegree < -arc + 1) {
						tempDegree += circleDegree;
					}
					Point p = mTrace
							.getDevicePoint(tempDegree % (circleDegree));
					canvas.translate(p.x + 10, p.y + 40);
					float scale = imageScallMax
							- (float) (Math.abs((tempDegree - (mTrace
									.getArcDegree() / 2))
									/ (mTrace.getArcDegree() / 2)))
							* imageScallCope;
					canvas.scale(scale, scale);

					Paint pa = new Paint();
					pa.setColor(Color.RED);
					canvas.drawCircle(0, 0, 80, pa);
					pa.setColor(Color.BLACK);
					pa.setTextSize(40);
					int tempIndex = getItemIndex(i);
					canvas.drawText(String.valueOf(indexs[tempIndex]), -12, 15,
							pa);

					canvas.restore();
				}

				holder.unlockCanvasAndPost(canvas);
			}
		}
	}

	public double targetDegree;

	public void revolveTo(int index) {
		targetDegree = (index + 2) * arc;
	}

	private int getItemIndex(int i) {
		double degree = currentDegree % circleDegree;
		int index = (int) ((currentDegree) / arc);
		int n = (int) (Math.abs(degree) / arc);
		int tempIndex = (index / visibleCount) * visibleCount;
		if (i < visibleCount - n) {
			tempIndex += i;
		} else {
			tempIndex += (-visibleCount + i);
		}
		if (tempIndex < 0) {
			tempIndex += indexs.length;
		} else if (tempIndex >= indexs.length) {
			tempIndex -= indexs.length;
		}
		System.out.println("ffffffff i =" + i + "  index = " + tempIndex
				+ " n = " + n + " currentDegree = " + currentDegree);
		return tempIndex;
	}
}
