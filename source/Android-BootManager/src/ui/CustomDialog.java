package ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.itjiehun.bootmanager.R;

public class CustomDialog extends Dialog {
	ListAdapter mAdapter;
	int mCheckedItem = -1;
	ListView mListView;

	public CustomDialog(Context paramContext) {
		super(paramContext);
	}

	public CustomDialog(Context paramContext, int paramInt) {
		super(paramContext, paramInt);
	}

	public ListView getListView() {
		return this.mListView;
	}

	public static class Builder {
		private LinearLayout contentPanel;
		private CharSequence[] items = null;
		private DialogInterface.OnClickListener itemsClickListener;
		ListAdapter mAdapter;
		public int mCheckedItem = -1;
		private Context mContext;
		private boolean mIsMultiChoice;
		private boolean mIsSingleChoice;
		private CharSequence message;
		private DialogInterface.OnClickListener negativeButtonClickListener;
		private CharSequence negativeButtonText;
		private DialogInterface.OnClickListener neutralButtonClickListener;
		private CharSequence neutralButtonText;
		private DialogInterface.OnClickListener positiveButtonClickListener;
		private CharSequence positiveButtonText;
		private CharSequence title;

		public Builder(Context paramContext) {
			this.mContext = paramContext;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		private void createListView(final CustomDialog paramCustomDialog, View paramView) {
			ListView localListView = new ListView(this.mContext);
			this.contentPanel.removeView(paramView.findViewById(R.id.scrollView));
			this.contentPanel.addView(localListView, new LinearLayout.LayoutParams(-1, -1));
			this.contentPanel.setLayoutParams(new LinearLayout.LayoutParams(-1, 0, 1.0F));
			int i;
			if (mIsSingleChoice) {
				i = 17367058; // 0x1090012
				if (mAdapter == null) {
					// return;// break label192;
					mAdapter = new ArrayAdapter(this.mContext, i, 16908308, this.items);
				}
				paramCustomDialog.mAdapter = ((ListAdapter) mAdapter);
				paramCustomDialog.mCheckedItem = this.mCheckedItem;
				if (itemsClickListener != null) {
					localListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							CustomDialog.Builder.this.itemsClickListener.onClick(paramCustomDialog, arg2);
							if (!CustomDialog.Builder.this.mIsSingleChoice) {
								paramCustomDialog.dismiss();
							}
						}

					});
				}
				localListView.setChoiceMode(1);
			} else if (mIsMultiChoice)
				localListView.setChoiceMode(2);
			if ((localListView != null) && (this.mAdapter != null)) {
				localListView.setAdapter(this.mAdapter);
				if (this.mCheckedItem > -1) {
					localListView.setItemChecked(this.mCheckedItem, true);
					localListView.setSelection(this.mCheckedItem);
				}
			}
			paramCustomDialog.mListView = localListView;
		}

		public CustomDialog create(final boolean isDisMiss) {
			LayoutInflater localLayoutInflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
			final CustomDialog localCustomDialog = new CustomDialog(this.mContext, R.style.Dialog);
			localCustomDialog.setCanceledOnTouchOutside(true);
			View localView = localLayoutInflater.inflate(R.layout.customdialog, null);

			localCustomDialog.addContentView(localView, new ViewGroup.LayoutParams(-1, -2));
			this.contentPanel = ((LinearLayout) localView.findViewById(R.id.contentPanel));

			((TextView) localView.findViewById(R.id.title)).setText(title);

			if (positiveButtonText != null) {
				TextView localTextView3 = (TextView) localView.findViewById(R.id.positiveText);
				localTextView3.setText(this.positiveButtonText);

				localTextView3.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (isDisMiss) {
							localCustomDialog.dismiss();
						}
						if (CustomDialog.Builder.this.positiveButtonClickListener != null) {
							CustomDialog.Builder.this.positiveButtonClickListener.onClick(localCustomDialog, -1);
						}
					}
				});
				/*
				 * if ((this.negativeButtonText == null) &&
				 * (this.neutralButtonText == null))
				 * localTextView3.setCompoundDrawables(null, null, null, null);
				 * // if (this.neutralButtonText == null) // break label349;
				 * TextView localTextView2 = (TextView) localView
				 * .findViewById(R.id.neutralText);
				 * localTextView2.setText(this.neutralButtonText); //
				 * localTextView2.setOnClickListener(new //
				 * View.OnClickListener(paramBoolean, localCustomDialog) // { //
				 * public void onClick(View paramView) // { // if
				 * (this.val$autoDismiss) // this.val$dialog.dismiss(); // if
				 * (CustomDialog.Builder.this.neutralButtonClickListener != //
				 * null) //
				 * CustomDialog.Builder.this.neutralButtonClickListener.
				 * onClick(this.val$dialog, // -3); // } // }); if
				 * (this.negativeButtonText == null)
				 * localTextView2.setCompoundDrawables(null, null, null, null);
				 * // label213: if (this.negativeButtonText == null) // break
				 * label364; TextView localTextView1 = (TextView) localView
				 * .findViewById(R.id.negativeText);
				 * localTextView1.setText(this.negativeButtonText); //
				 * localTextView1.setOnClickListener(new //
				 * View.OnClickListener(localCustomDialog) // { // public void
				 * onClick(View paramView) // { // this.val$dialog.dismiss(); //
				 * if (CustomDialog.Builder.this.negativeButtonClickListener !=
				 * // null) //
				 * CustomDialog.Builder.this.negativeButtonClickListener
				 * .onClick(this.val$dialog, // -2); // } // });
				 */
			}
			while (true) {
				if ((this.positiveButtonText == null) && (this.negativeButtonText == null)
						&& (this.neutralButtonText == null))
					localView.findViewById(R.id.footer).setVisibility(8);
				if (message != null)
					((TextView) localView.findViewById(R.id.message)).setText(this.message);
				if (items != null)
					createListView(localCustomDialog, localView);
				localCustomDialog.setContentView(localView);

				localView.findViewById(R.id.neutralText).setVisibility(View.GONE);
				localView.findViewById(R.id.negativeText).setVisibility(View.GONE);
				return localCustomDialog;
				// localView.findViewById(2131230810).setVisibility(8);
				// break;
				// label349:
				// localView.findViewById(2131230811).setVisibility(8);
				// break label213;
				// label364:
				// localView.findViewById(2131230812).setVisibility(8);
			}
		}

		public Builder setItems(int paramInt, DialogInterface.OnClickListener paramOnClickListener) {
			this.items = this.mContext.getResources().getTextArray(paramInt);
			this.itemsClickListener = paramOnClickListener;
			return this;
		}

		public Builder setItems(CharSequence[] paramArrayOfCharSequence,
				DialogInterface.OnClickListener paramOnClickListener) {
			this.items = paramArrayOfCharSequence;
			this.itemsClickListener = paramOnClickListener;
			return this;
		}

		public Builder setMessage(int paramInt) {
			this.message = this.mContext.getText(paramInt);
			return this;
		}

		public Builder setMessage(CharSequence paramCharSequence) {
			this.message = paramCharSequence;
			return this;
		}

		public Builder setNegativeButton(int paramInt, DialogInterface.OnClickListener paramOnClickListener) {
			this.negativeButtonText = this.mContext.getText(paramInt);
			this.negativeButtonClickListener = paramOnClickListener;
			return this;
		}

		public Builder setNegativeButton(CharSequence paramCharSequence,
				DialogInterface.OnClickListener paramOnClickListener) {
			this.negativeButtonText = paramCharSequence;
			this.negativeButtonClickListener = paramOnClickListener;
			return this;
		}

		public Builder setNeutralButton(CharSequence paramCharSequence,
				DialogInterface.OnClickListener paramOnClickListener) {
			this.neutralButtonText = paramCharSequence;
			this.neutralButtonClickListener = paramOnClickListener;
			return this;
		}

		public Builder setPositiveButton(int paramInt, DialogInterface.OnClickListener paramOnClickListener) {
			this.positiveButtonText = this.mContext.getText(paramInt);
			this.positiveButtonClickListener = paramOnClickListener;
			return this;
		}

		public Builder setPositiveButton(CharSequence paramCharSequence,
				DialogInterface.OnClickListener paramOnClickListener) {
			this.positiveButtonText = paramCharSequence;
			this.positiveButtonClickListener = paramOnClickListener;
			return this;
		}

		public Builder setSingleChoiceItems(int arrayId, int selectId, DialogInterface.OnClickListener OnClickListener) {
			this.items = this.mContext.getResources().getTextArray(arrayId);
			this.itemsClickListener = OnClickListener;
			this.mCheckedItem = selectId;
			this.mIsSingleChoice = true;
			return this;
		}

		public Builder setSingleChoiceItems(CharSequence[] paramArrayOfCharSequence, int paramInt,
				DialogInterface.OnClickListener paramOnClickListener) {
			this.items = paramArrayOfCharSequence;
			this.itemsClickListener = paramOnClickListener;
			this.mCheckedItem = paramInt;
			this.mIsSingleChoice = true;
			return this;
		}

		public Builder setTitle(int paramInt) {
			this.title = this.mContext.getText(paramInt);
			return this;
		}

		public Builder setTitle(CharSequence paramCharSequence) {
			this.title = paramCharSequence;
			return this;
		}

		public CustomDialog show() {
			return show(true);
		}

		public CustomDialog show(boolean isDisMiss) {
			CustomDialog localCustomDialog = create(isDisMiss);
			localCustomDialog.show();
			return localCustomDialog;
		}
	}
}