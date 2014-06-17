package dreajay.android.safe.activities;

import android.os.Bundle;
import dreajay.android.safe.R;

public class SJFDStep2Activity extends BaseStepActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sjfd_step2);
	}

	@Override
	protected void moveNext() {
		loadActivity(SJFDStep3Activity.class);
		overridePendingTransition(R.anim.anim_tran_right_in, R.anim.anim_tran_left_out);
	}

	@Override
	protected void movePre() {
		loadActivity(SJFDStep1Activity.class);
		overridePendingTransition(R.anim.anim_tran_left_in, R.anim.anim_tran_right_out);
	}
	
}
