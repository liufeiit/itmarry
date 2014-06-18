package com.itjiehun.bootmanager.widget;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itjiehun.bootmanager.ConfigHelper;
import com.itjiehun.bootmanager.R;
import com.itjiehun.bootmanager.ui.MainActivity;

public class SetPage implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
	private static final String ABOUT_US = "aboutUs";
	static final String AD_FREE = "adFree";
	public static String APP_LINK = "";
	private static final String CHECK_ROOT = "checkRoot";
	private static final String CHECK_UPDATE = "checkUpdate";
	private static final String FEED_BACK = "feedBack";
	private static final String HIAPK = "hiapk";
	private static final String HOW_TO_ROOT = "howToRoot";
	private static final String SHARE_FRIEND = "shareFriend";
	private static final String SHORT_CUT = "SHORT_CUT";
	private static final String SHOW_OFFER = "showOffer";
	private static final String STAR_RATE = "starRate";
	private static final String WAPTW = "waptw";
	private static final String WHY_ROOT = "whyRoot";
	ViewGroup baseView;
	MainActivity mActivity;
	ConfigHelper mConfigHelper;
	LinearLayout mLinearLayout;
	int point;
	TextView reverTextView;
	TextView updateDetailTextView;
	TextView updateTextView;

	public SetPage(MainActivity paramMainActivity) {
		this.mActivity = paramMainActivity;
		this.baseView = ((ViewGroup) LayoutInflater.from(this.mActivity).inflate(R.layout.set_layout, null));
	}

	private View createCheckBox(String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
		View localView = ((LayoutInflater) this.mActivity.getSystemService("layout_inflater")).inflate(
				R.layout.checkbox_item, null, false);
		((TextView) localView.findViewById(R.id.textView1)).setText(paramString2);
		((TextView) localView.findViewById(R.id.textView2)).setText(paramString3);
		CheckBox localCheckBox = (CheckBox) localView.findViewById(R.id.checkBox1);
		localCheckBox.setTag(paramString1);
		localCheckBox.setChecked(this.mConfigHelper.getBoolean(paramString1, paramBoolean));
		localCheckBox.setOnCheckedChangeListener(this);
		return localView;
	}

	private View createDivider() {
		return ((LayoutInflater) this.mActivity.getSystemService("layout_inflater")).inflate(R.layout.divider_item,
				null, true);
	}

	private ViewGroup createGroup() {
		return (ViewGroup) ((LayoutInflater) this.mActivity.getSystemService("layout_inflater")).inflate(
				R.layout.group_item, null, false);
	}

	private TextView createLabel(String paramString) {
		TextView localTextView = (TextView) ((LayoutInflater) this.mActivity.getSystemService("layout_inflater"))
				.inflate(R.layout.label_item, null, false);
		localTextView.setText(paramString);
		return localTextView;
	}

	private View createText(String paramString1, String paramString2, String paramString3, Drawable paramDrawable) {
		View localView = ((LayoutInflater) this.mActivity.getSystemService("layout_inflater")).inflate(
				R.layout.text_item, null, false);
		((TextView) localView.findViewById(R.id.textView1)).setText(paramString2);
		((TextView) localView.findViewById(R.id.textView2)).setText(paramString3);
		ImageView localImageView = (ImageView) localView.findViewById(R.id.imageView1);
		if (paramDrawable == null)
			localImageView.setVisibility(8);
		localView.setTag(paramString1);
		localView.setOnClickListener(this);
		return localView;
	}

	private void setupViews() {
		this.mLinearLayout = ((LinearLayout) findViewById(R.id.baseLayout));
	}

	private void showToast(String paramString) {
		Toast.makeText(this.mActivity, paramString, 0).show();
	}

	protected View findViewById(int paramInt) {
		return this.baseView.findViewById(paramInt);
	}

	public View getBaseView() {
		return this.baseView;
	}

	public void onCreate() {
		this.mConfigHelper = new ConfigHelper(this.mActivity);
		setupViews();
		Drawable localDrawable = this.mActivity.getResources().getDrawable(R.drawable.right_arrow);
		ViewGroup localViewGroup2 = createGroup();
		localViewGroup2.addView(createLabel("关于ROOT权限"));
		localViewGroup2.addView(createText("checkRoot", "ROOT权限检测", "检测权限是否已获得root权限", localDrawable));
		localViewGroup2.addView(createDivider());
		localViewGroup2.addView(createText("whyRoot", "为什么要获取ROOT权限", "为什么软件需要获取root权限", localDrawable));
		localViewGroup2.addView(createDivider());
		localViewGroup2.addView(createText("howToRoot", "ROOT教程", "怎样让手机获得root权限", localDrawable));
		this.mLinearLayout.addView(localViewGroup2);
		ViewGroup localViewGroup3 = createGroup();
		localViewGroup3.addView(createLabel("其他"));
		String str = "0.0";
		try {
			str = this.mActivity.getPackageManager().getPackageInfo(this.mActivity.getPackageName(), 0).versionName;
			localViewGroup3.addView(createDivider());
			localViewGroup3.addView(createText("aboutUs", "关于", "关于" + this.mActivity.getString(R.string.app_name)
					+ str, localDrawable));
			this.mLinearLayout.addView(localViewGroup3);
			return;
		} catch (PackageManager.NameNotFoundException localNameNotFoundException) {
			while (true)
				localNameNotFoundException.printStackTrace();
		}
	}

	@Override
	public void onClick(View paramView) {
		String str = (String) paramView.getTag();
		do {
			if ("checkRoot".equals(str)) {
				this.mActivity.checkRoot();
				return;
			}
			if ("whyRoot".equals(str)) {
				mActivity.whyRoot();
				return;
			}
			if ("howToRoot".equals(str)) {
				mActivity.howToRoot();
				return;
			}
			if ("SHORT_CUT".equals(str)) {
				return;
			}
			if (!"showOffer".equals(str))
				continue;

			if ("feedBack".equals(str)) {
				return;
			}
			if ("checkUpdate".equals(str)) {
				return;
			}
			if ("starRate".equals(str)) {
				return;
			}
			if ("shareFriend".equals(str)) {
				return;
			}
			if ("waptw".equals(str)) {
				Intent localIntent1 = new Intent("android.intent.action.VIEW");
				localIntent1.setData(Uri.parse("http://android.waptw.com"));
				try {
					this.mActivity.startActivity(localIntent1);
					return;
				} catch (Exception localException1) {
					return;
				}
			}
			if (!"hiapk".equals(str))
				continue;
			try {
				Intent localIntent2 = new Intent("android.intent.action.VIEW");
				localIntent2.setData(Uri.parse(APP_LINK));
				this.mActivity.startActivity(localIntent2);
				return;
			} catch (Exception localException2) {
				return;
			}
		} while (!"aboutUs".equals(str));
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.mActivity);
		localBuilder.setTitle("关于");
		localBuilder.setMessage(R.string.about);
		localBuilder.setNegativeButton("返回", null);
		localBuilder.show();
	}

	@Override
	public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean) {
		String str = (String) paramCompoundButton.getTag();
		if ((!"silentMove".equals(str)) || (!paramBoolean))
			"checkMove".equals(str);
		this.mConfigHelper.putBoolean(str, paramBoolean);
	}
}
