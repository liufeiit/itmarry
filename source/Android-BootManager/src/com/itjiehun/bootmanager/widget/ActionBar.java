package com.itjiehun.bootmanager.widget;

import java.util.ArrayList;
import java.util.Iterator;

import ui.CustomDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.itjiehun.bootmanager.BootApplication;
import com.itjiehun.bootmanager.R;
import com.itjiehun.bootmanager.ui.ActionListener;

public class ActionBar extends FrameLayout implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

	private ArrayList<ActionListener> actionListeners = new ArrayList<ActionListener>();
	private CheckBox search;
	private Button sort;
	private TextView title;

	public ActionBar(Context context) {
		this(context, null);
	}

	public ActionBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ActionBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		View.inflate(context, R.layout.head_layout, this);
		this.search = ((CheckBox) findViewById(R.id.search));
		this.sort = ((Button) findViewById(R.id.sort));
		this.title = ((TextView) findViewById(R.id.title));
		this.search.setOnCheckedChangeListener(this);
		this.sort.setOnClickListener(this);
	}

	public CheckBox getSearchCheckBox() {
		return this.search;
	}

	@Override
	public void onClick(View paramView) {
		if (paramView.getId() == R.id.sort) {
			CustomDialog.Builder localBuilder = new CustomDialog.Builder(getContext());
			localBuilder.setTitle("请选择软件列表排序方式");
			localBuilder.setSingleChoiceItems(R.array.sort_array, BootApplication.getInstance().getConfigHelper()
					.getSortMode(), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
					BootApplication.getInstance().getConfigHelper().setSortMode(paramAnonymousInt);
					paramAnonymousDialogInterface.dismiss();
					Iterator<ActionListener> localIterator = ActionBar.this.actionListeners.iterator();
					while (localIterator.hasNext()) {
						((ActionListener) localIterator.next()).onSort();
					}
				}
			});
			localBuilder.show();
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		Iterator<ActionListener> localIterator = this.actionListeners.iterator();
		while (localIterator.hasNext()) {
			((ActionListener) localIterator.next()).onSearch(arg1);
		}
	}

	public void onSetPage() {
		this.title.setVisibility(0);
		this.search.setVisibility(4);
		this.sort.setVisibility(4);
	}

	public void onSystemPage() {
		this.search.setVisibility(0);
		this.sort.setVisibility(0);
		this.title.setVisibility(4);
	}

	public void onUserPage() {
		this.search.setVisibility(0);
		this.sort.setVisibility(0);
		this.title.setVisibility(4);
	}

	public void regirsterActionListener(ActionListener paramActionListener) {
		this.actionListeners.add(paramActionListener);
	}

	public void unregirsterActionListener(ActionListener paramActionListener) {
		this.actionListeners.remove(paramActionListener);
	}
}
