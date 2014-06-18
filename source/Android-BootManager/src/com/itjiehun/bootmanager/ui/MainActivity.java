package com.itjiehun.bootmanager.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ui.CustomDialog;
import ui.ViewPagerAdapter;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.itjiehun.bootmanager.BootApplication;
import com.itjiehun.bootmanager.ConfigHelper;
import com.itjiehun.bootmanager.R;
import com.itjiehun.bootmanager.apk.Uitils;
import com.itjiehun.bootmanager.shell.ShellCommand;
import com.itjiehun.bootmanager.widget.ActionBar;
import com.itjiehun.bootmanager.widget.AutoRunPage;
import com.itjiehun.bootmanager.widget.SetPage;

public class MainActivity extends Activity implements ViewPager.OnPageChangeListener,
		RadioGroup.OnCheckedChangeListener {

	private ActionBar actionBar;
	private ConfigHelper configHelper;
	private int currentPage;
	private SetPage setPage;
	private ShellCommand shellCommand;
	private AutoRunPage systemAutoPage;
	private RadioGroup tabRadioGroup;
	private ImageView tagImage;
	private AutoRunPage userAutoRunPage;
	private ViewPager viewPager;
	private ViewPagerAdapter viewPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.shellCommand = BootApplication.getInstance().getShellCommand();
		this.configHelper = BootApplication.getInstance().getConfigHelper();
		setupViews();
		if (this.configHelper.isFirstRun()) {
			this.configHelper.setFirstRun(false);
			onFirstRunApp();
		}
	}

	private void onFirstRunApp() {
		CustomDialog.Builder localBuilder = new CustomDialog.Builder(this);
		SpannableStringBuilder localSpannableStringBuilder1 = new SpannableStringBuilder("请仔细阅读下面内容");
		localSpannableStringBuilder1.setSpan(new ForegroundColorSpan(-65536) {
			public void updateDrawState(TextPaint paramTextPaint) {
				super.updateDrawState(paramTextPaint);
				paramTextPaint.setFakeBoldText(true);
			}
		}, 0, localSpannableStringBuilder1.length(), 33);
		localBuilder.setTitle(localSpannableStringBuilder1.toString());
		SpannableStringBuilder localSpannableStringBuilder2 = new SpannableStringBuilder(
				"本程序支持禁止软件开机自启功能，节省内存，但该功能需要root权限才能正常使用。如果您想禁止软件开机自启并且您的手机已经拥有root权限，在点击确认继续后，权限管理工具会弹出对话框询问是否允许软件获得root权限，请点击");
		int i = localSpannableStringBuilder2.length();
		localSpannableStringBuilder2.append("允许(allow)");
		localSpannableStringBuilder2.setSpan(new StyleSpan(1) {
			public void updateDrawState(TextPaint paramTextPaint) {
				paramTextPaint.setFakeBoldText(true);
				super.updateDrawState(paramTextPaint);
			}

			public void updateMeasureState(TextPaint paramTextPaint) {
				paramTextPaint.setFakeBoldText(true);
				super.updateMeasureState(paramTextPaint);
			}
		}, i, localSpannableStringBuilder2.length(), 33);
		localSpannableStringBuilder2.append("\n如果您的手机未root请查看功能设置中的获取root权限教程。");
		localBuilder.setMessage(localSpannableStringBuilder2.toString());
		localBuilder.setPositiveButton("确认继续", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				new Thread() {
					public void run() {
						super.run();
						if ((shellCommand.canSU()) && (!configHelper.isSilentMove()))
							configHelper.setSilentMove(true);
					}
				}.start();
			}
		});
		localBuilder.show();
	}

	private void setupViews() {
		this.actionBar = ((ActionBar) findViewById(R.id.actionBar));
		this.viewPager = ((ViewPager) findViewById(R.id.viewPager));
		ArrayList<View> localArrayList = new ArrayList<View>();
		this.userAutoRunPage = new AutoRunPage(this, true, this.actionBar);
		this.systemAutoPage = new AutoRunPage(this, false, this.actionBar);
		this.setPage = new SetPage(this);
		this.setPage.onCreate();
		localArrayList.add(this.userAutoRunPage);
		localArrayList.add(this.systemAutoPage);
		localArrayList.add(this.setPage.getBaseView());
		this.actionBar.regirsterActionListener(this.userAutoRunPage);
		this.actionBar.regirsterActionListener(this.systemAutoPage);
		this.viewPagerAdapter = new ViewPagerAdapter(localArrayList);
		this.viewPager.setAdapter(this.viewPagerAdapter);
		this.viewPager.setOnPageChangeListener(this);
		this.tagImage = ((ImageView) findViewById(R.id.tagImage));
		this.tabRadioGroup = ((RadioGroup) findViewById(R.id.tabbar));
		this.tabRadioGroup.setOnCheckedChangeListener(this);
	}

	protected void showNoRootDlg() {
		CustomDialog.Builder localBuilder = new CustomDialog.Builder(this);
		localBuilder.setTitle("获取ROOT权限失败");
		localBuilder.setMessage("获取ROOT权限失败，可能由于您的手机未拥有ROOT权限或者获取ROOT权限提示时您选择了拒绝！");
		localBuilder.setPositiveButton("ROOT教程", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				MainActivity.this.howToRoot();
			}
		});
		localBuilder.setNeutralButton("重新获取", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				MainActivity.this.showRerootDlg();
			}
		});
		localBuilder.show();
	}

	private void showRerootDlg() {
		CustomDialog.Builder localBuilder = new CustomDialog.Builder(this);
		localBuilder.setTitle("重新获取ROOT权限");
		localBuilder.setMessage("知道您手机中的权限管理软件(Superuser)，将" + getString(R.string.app_name) + "在拒绝列表中删除，"
				+ "重新进入自带软件深度卸载软件获取权限即可!");
		localBuilder.setPositiveButton("我知道了", null);
		localBuilder.show();
	}

	private void showShortCutDlg() {
		CustomDialog.Builder localBuilder = new CustomDialog.Builder(this);
		localBuilder.setTitle("提示");
		localBuilder.setMessage("是否创建桌面快捷方式?");
		localBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				createShortCut();
			}
		});
		localBuilder.setNegativeButton("取消", null);
		localBuilder.show();
	}

	public void showToast(String paramString) {
		Toast.makeText(this, paramString, 0).show();
	}

	public void checkRoot() {
		if (!this.shellCommand.canSU()) {
			showToast("无法获取ROOT权限");
			return;
		}
		showToast("获取ROOT权限成功");
	}

	public void createShortCut() {
		Intent localIntent = new Intent(this, MainActivity.class);
		localIntent.setAction("android.intent.action.MAIN");
		Uitils.installShortcut(this, localIntent, getString(R.string.launch_name), R.drawable.logo);
	}

	public void howToRoot() {
		search(SearchType.baidu, Build.MODEL + " root教程");
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int paramInt) {
		int i = this.currentPage;
		if (i != paramInt) {
			int j = this.tagImage.getWidth();
			TranslateAnimation localTranslateAnimation = new TranslateAnimation(i * j, paramInt * j, 0.0F, 0.0F);
			localTranslateAnimation.setDuration(300L);
			localTranslateAnimation.setFillAfter(true);
			this.tagImage.startAnimation(localTranslateAnimation);
		}
		this.currentPage = paramInt;
		switch (paramInt) {
		default:
			return;
		case 0:
			this.actionBar.onUserPage();
			this.tabRadioGroup.check(R.id.radio0);
			return;
		case 1:
			this.actionBar.onSystemPage();
			this.tabRadioGroup.check(R.id.radio1);
			return;
		case 2:
		}
		this.actionBar.onSetPage();
		this.tabRadioGroup.check(R.id.radio2);
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int paramInt) {
		switch (paramInt) {
		default:
			return;
		case R.id.radio0:
			this.viewPager.setCurrentItem(0);
			return;
		case R.id.radio1:
			this.viewPager.setCurrentItem(1);
			return;
		case R.id.radio2:
		}
		this.viewPager.setCurrentItem(2);
	}

	public void search(SearchType paramSearchType, String paramString) {
		StringBuilder localStringBuilder = new StringBuilder();
		if (paramSearchType == SearchType.baidu) {
			localStringBuilder.append("http://m.baidu.com/s?from=1588a&word=");
			try {
				localStringBuilder.append(URLEncoder.encode(paramString, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		try {
			while (true) {
				Intent localIntent = new Intent("android.intent.action.VIEW");
				localIntent.setData(Uri.parse(localStringBuilder.toString()));
				startActivity(localIntent);
				return;
			}
		} catch (Exception localException) {
		}
	}

	public void whyRoot() {
		CustomDialog.Builder localBuilder = new CustomDialog.Builder(this);
		localBuilder.setTitle("为什么要获取ROOT权限");
		localBuilder.setMessage("本程序支持禁止软件开机自启功能，节省内存，但该功能需要root权限才能正常使用。\n所以" + getString(R.string.app_name)
				+ "需要获取ROOT权限以便您更好的体验！");
		localBuilder.setPositiveButton("我知道了", null);
		localBuilder.show();
	}

	enum SearchType {
		easou, soso, baidu, google, market;
	}
}
