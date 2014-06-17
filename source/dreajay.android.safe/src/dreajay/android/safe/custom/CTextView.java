package dreajay.android.safe.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class CTextView extends TextView {

	public CTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean isFocused() {
		return true;
	}

	
}
