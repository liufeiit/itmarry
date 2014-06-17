package dreajay.android.safe.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import dreajay.android.safe.R;
import dreajay.android.safe.common.Constants;

public abstract class BaseStepActivity extends Activity {
	protected SharedPreferences sp;

	protected GestureDetector mGestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences(Constants.SYSTEM_SETTINGS, MODE_PRIVATE);
		mGestureDetector = new GestureDetector(getApplicationContext(),
				new MyOnGestureListener());

	}

	protected void loadActivity(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
		finish();
	}

	class MyOnGestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			
			if ((e1.getRawX() - e2.getRawX()) > 50) {
				moveNext();
				return true;
			}

			if ((e2.getRawX() - e1.getRawX()) > 50) {
				movePre();
				return true;
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}

	protected abstract void moveNext();

	protected abstract void movePre();

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}


	
}
