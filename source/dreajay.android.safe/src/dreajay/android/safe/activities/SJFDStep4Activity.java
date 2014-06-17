package dreajay.android.safe.activities;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import dreajay.android.safe.R;

public class SJFDStep4Activity extends BaseStepActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sjfd_step4);
	}

	@Override
	protected void moveNext() {
		Editor editor = sp.edit();
		editor.putBoolean(SJFDActivity.SJFDSTEPCONF, true);
		editor.commit();
		loadActivity(SJFDActivity.class);
		overridePendingTransition(R.anim.anim_tran_right_in, R.anim.anim_tran_left_out);
	}

	@Override
	protected void movePre() {
		loadActivity(SJFDStep3Activity.class);
		overridePendingTransition(R.anim.anim_tran_left_in, R.anim.anim_tran_right_out);
	}
	
}
