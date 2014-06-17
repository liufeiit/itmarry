package dreajay.android.safe.activities;

import dreajay.android.safe.R;
import dreajay.android.safe.common.Constants;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SJFDActivity extends Activity {
	private SharedPreferences sp;
	public final static String SJFDSTEPCONF = "SJFDSTEPCONF";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences(Constants.SYSTEM_SETTINGS, MODE_PRIVATE);
		
		if(sp.getBoolean(SJFDSTEPCONF, false)) {
			setContentView(R.layout.activity_sjfd);
		} else {
			Intent intent = new Intent(this,SJFDStep1Activity.class);
			startActivity(intent);
			finish();
		}
		
		
	}
}
