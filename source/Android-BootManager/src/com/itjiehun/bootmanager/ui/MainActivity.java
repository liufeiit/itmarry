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
		SpannableStringBuilder localSpannableStringBuilder1 = new SpannableStringBuilder("����ϸ�Ķ���������");
		localSpannableStringBuilder1.setSpan(new ForegroundColorSpan(-65536) {
			public void updateDrawState(TextPaint paramTextPaint) {
				super.updateDrawState(paramTextPaint);
				paramTextPaint.setFakeBoldText(true);
			}
		}, 0, localSpannableStringBuilder1.length(), 33);
		localBuilder.setTitle(localSpannableStringBuilder1.toString());
		SpannableStringBuilder localSpannableStringBuilder2 = new SpannableStringBuilder(
				"������֧�ֽ�ֹ��������������ܣ���ʡ�ڴ棬���ù�����ҪrootȨ�޲�������ʹ�á���������ֹ��������������������ֻ��Ѿ�ӵ��rootȨ�ޣ��ڵ��ȷ�ϼ�����Ȩ�޹����߻ᵯ���Ի���ѯ���Ƿ�����������rootȨ�ޣ�����");
		int i = localSpannableStringBuilder2.length();
		localSpannableStringBuilder2.append("����(allow)");
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
		localSpannableStringBuilder2.append("\n��������ֻ�δroot��鿴���������еĻ�ȡrootȨ�޽̡̳�");
		localBuilder.setMessage(localSpannableStringBuilder2.toString());
		localBuilder.setPositiveButton("ȷ�ϼ���", new DialogInterface.OnClickListener() {
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
		localBuilder.setTitle("��ȡROOTȨ��ʧ��");
		localBuilder.setMessage("��ȡROOTȨ��ʧ�ܣ��������������ֻ�δӵ��ROOTȨ�޻��߻�ȡROOTȨ����ʾʱ��ѡ���˾ܾ���");
		localBuilder.setPositiveButton("ROOT�̳�", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				MainActivity.this.howToRoot();
			}
		});
		localBuilder.setNeutralButton("���»�ȡ", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				MainActivity.this.showRerootDlg();
			}
		});
		localBuilder.show();
	}

	private void showRerootDlg() {
		CustomDialog.Builder localBuilder = new CustomDialog.Builder(this);
		localBuilder.setTitle("���»�ȡROOTȨ��");
		localBuilder.setMessage("֪�����ֻ��е�Ȩ�޹������(Superuser)����" + getString(R.string.app_name) + "�ھܾ��б���ɾ����"
				+ "���½����Դ�������ж�������ȡȨ�޼���!");
		localBuilder.setPositiveButton("��֪����", null);
		localBuilder.show();
	}

	private void showShortCutDlg() {
		CustomDialog.Builder localBuilder = new CustomDialog.Builder(this);
		localBuilder.setTitle("��ʾ");
		localBuilder.setMessage("�Ƿ񴴽������ݷ�ʽ?");
		localBuilder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				createShortCut();
			}
		});
		localBuilder.setNegativeButton("ȡ��", null);
		localBuilder.show();
	}

	public void showToast(String paramString) {
		Toast.makeText(this, paramString, 0).show();
	}

	public void checkRoot() {
		if (!this.shellCommand.canSU()) {
			showToast("�޷���ȡROOTȨ��");
			return;
		}
		showToast("��ȡROOTȨ�޳ɹ�");
	}

	public void createShortCut() {
		Intent localIntent = new Intent(this, MainActivity.class);
		localIntent.setAction("android.intent.action.MAIN");
		Uitils.installShortcut(this, localIntent, getString(R.string.launch_name), R.drawable.logo);
	}

	public void howToRoot() {
		search(SearchType.baidu, Build.MODEL + " root�̳�");
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
		localBuilder.setTitle("ΪʲôҪ��ȡROOTȨ��");
		localBuilder.setMessage("������֧�ֽ�ֹ��������������ܣ���ʡ�ڴ棬���ù�����ҪrootȨ�޲�������ʹ�á�\n����" + getString(R.string.app_name)
				+ "��Ҫ��ȡROOTȨ���Ա������õ����飡");
		localBuilder.setPositiveButton("��֪����", null);
		localBuilder.show();
	}

	enum SearchType {
		easou, soso, baidu, google, market;
	}
}
