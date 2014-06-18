package com.itjiehun.bootmanager.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.itjiehun.bootmanager.R;
import com.itjiehun.bootmanager.ui.ActionListener;

public class AutoRunPage extends FrameLayout implements ActionListener {
	private ActionBar mActionBar;
	private AutoRunListView mAutoRunListView;
	private View mKeyLayoutView;
	private EditText mKeyWordView;

	public AutoRunPage(Context context, boolean isUserApp, ActionBar paramActionBar) {
		super(context);
		View.inflate(context, R.layout.autorun_page, this);
		LinearLayout localLinearLayout = (LinearLayout) findViewById(R.id.page);
		View.inflate(context, R.layout.search_layout, localLinearLayout);
		mActionBar = paramActionBar;
		mAutoRunListView = new AutoRunListView(context, isUserApp);
		mAutoRunListView.loadPage(1, 0x7fffffff);// 2147483647);
		localLinearLayout.addView(mAutoRunListView, -1, -1);
		mKeyLayoutView = findViewById(R.id.keyLayout);
		mKeyLayoutView.setVisibility(GONE);
		mKeyWordView = ((EditText) findViewById(R.id.keyWord));
		mKeyWordView.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable paramEditable) {
				if (mAutoRunListView != null)
					mAutoRunListView.updateKeyWord(paramEditable.toString());
			}

			public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
			}

			public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
			}
		});
		findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
				AutoRunPage.this.mActionBar.getSearchCheckBox().setChecked(false);
			}
		});
		View localView = findViewById(R.id.btnAppOffer);
		localView.setVisibility(View.GONE);
	}

	@Override
	public void onSearch(boolean paramBoolean) {
		if (paramBoolean) {
			this.mKeyLayoutView.setVisibility(0);
			return;
		}
		this.mKeyLayoutView.setVisibility(8);
		this.mKeyWordView.setText("");
	}

	@Override
	public void onSort() {
		this.mAutoRunListView.onSort();
	}

}
