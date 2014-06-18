package com.itjiehun.bootmanager.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.itjiehun.bootmanager.BootApplication;
import com.itjiehun.bootmanager.R;
import com.itjiehun.bootmanager.SortMethods;
import com.itjiehun.bootmanager.apk.Uitils;
import com.itjiehun.bootmanager.ui.ActionListener;

public class AutoRunListView extends AsynListView<Item> implements ActionListener {
	private boolean mUserApk;
	private String mkeyWord = "";

	public AutoRunListView(Context paramContext, boolean isUserApp) {
		super(paramContext);
		this.mUserApk = isUserApp;
	}

	@Override
	protected View getView(int position, View convertView, ViewGroup parent, boolean isbusy) {
		ItemView localItemView;
		if (convertView == null)
			localItemView = new ItemView(getContext());
		else
			localItemView = (ItemView) convertView;

		localItemView.bindView(position, (Item) this.mAdapter.getItem(position));
		return localItemView;
	}

	public AsynListView.Adapter initAdapter() {
		return new AutoRunAdapter(getContext());
	}

	protected int lastIndexOf(Item paramItem) {
		return paramItem.apkInfo.name.toUpperCase().lastIndexOf(this.mkeyWord);
	}

	public void onLoadComplited(int paramInt1, int paramInt2, ArrayList<Item> paramArrayList) {
		getEmptyView().showEmptyText(null, "\n\n未找到任何应用");
		super.onLoadComplited(paramInt1, paramInt2, paramArrayList);
		this.mAdapter.sort(SortMethods.getComparator(BootApplication.getInstance().getConfigHelper().getSortMode()));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected ArrayList<Item> onLoading(int paramInt1, int paramInt2) {
		HashMap localHashMap = new HashMap();
		Drawable localDrawable = getContext().getResources().getDrawable(17301651);// android.R.drawable.ic_delete);//
																					// 17301651);
		Intent localIntent = new Intent("android.intent.action.BOOT_COMPLETED");
		Iterator localIterator = getContext().getPackageManager()
				.queryBroadcastReceivers(localIntent, PackageManager.GET_DISABLED_COMPONENTS).iterator();
		ResolveInfo localResolveInfo = null;
		String str = null;

		while (localIterator.hasNext()) {
			localResolveInfo = (ResolveInfo) localIterator.next();
			str = localResolveInfo.activityInfo.packageName;

			boolean isOK = false;
			// System.out.println(localResolveInfo);
			// sb.append(localResolveInfo);
			// sb.append("\n");

			if (mUserApk) { // only show user apk
				if ((0x1 & localResolveInfo.activityInfo.applicationInfo.flags) != 1)
					// sb.append("user   ---- ");
					isOK = true;// localHashMap.put(str, new
								// Item(Uitils.createApkInfo(getContext().getPackageManager(),
								// str, localDrawable)));
			} else {
				if ((0x1 & localResolveInfo.activityInfo.applicationInfo.flags) == 1)
					isOK = true;// localHashMap.put(str, new
								// Item(Uitils.createApkInfo(getContext().getPackageManager(),
								// str, localDrawable)));
			}

			if (isOK) {
				Item item = new Item(Uitils.createApkInfo(getContext().getPackageManager(), str, localDrawable));
				ComponentName component = new ComponentName(str, localResolveInfo.activityInfo.name);
				boolean bool2 = false;
				if (getContext().getPackageManager().getComponentEnabledSetting(component) != PackageManager.COMPONENT_ENABLED_STATE_DISABLED)
					bool2 = true;
				item.addComponent(bool2, component);
				localHashMap.put(str, item);
			}
		}
		return new ArrayList(localHashMap.values());
		/*
		 * do { if (!localIterator.hasNext()) return new
		 * ArrayList(localHashMap.values());
		 * 
		 * localResolveInfo = (ResolveInfo) localIterator.next();
		 * System.out.println(localResolveInfo); str =
		 * localResolveInfo.activityInfo.packageName;
		 * 
		 * if ((0x1 & localResolveInfo.activityInfo.applicationInfo.flags) != 1)
		 * break;
		 * 
		 * Item localItem2 = (Item) localHashMap.get(str); if (localItem2 ==
		 * null) { localItem2 = new Item(Uitils.createApkInfo(getContext()
		 * .getPackageManager(), str, localDrawable)); localHashMap.put(str,
		 * localItem2); }
		 * 
		 * ComponentName localComponentName2 = new
		 * ComponentName(str,localResolveInfo.activityInfo.name); boolean bool2
		 * = false; if
		 * (getContext().getPackageManager().getComponentEnabledSetting(
		 * localComponentName2) != 2) bool2 = true;
		 * localItem2.addComponent(bool2, localComponentName2); } while
		 * (!this.mUserApk);
		 * 
		 * Item localItem1 = (Item) localHashMap.get(str); if (localItem1 ==
		 * null) { localItem1 = new Item(Uitils.createApkInfo(getContext()
		 * .getPackageManager(), str, localDrawable)); localHashMap.put(str,
		 * localItem1); } ComponentName localComponentName1 = new
		 * ComponentName(str, localResolveInfo.activityInfo.name); boolean bool1
		 * = false; if
		 * (getContext().getPackageManager().getComponentEnabledSetting(
		 * localComponentName1) != 2) bool1 = true;
		 * 
		 * localItem1.addComponent(bool1, localComponentName1);
		 * 
		 * return new ArrayList(localHashMap.values());
		 */
	}

	@Override
	public void onSearch(boolean paramBoolean) {

	}

	@Override
	public void onSort() {
		this.mAdapter.sort(SortMethods.getComparator(BootApplication.getInstance().getConfigHelper().getSortMode()));
		this.mAdapter.notifyDataSetChanged();
	}

	public void updateKeyWord(String paramString) {
		if (!this.mkeyWord.equals(paramString.trim().toUpperCase())) {
			this.mkeyWord = paramString.trim().toUpperCase();
			this.mAdapter.notifyDataSetChanged();
		}
	}

	class AutoRunAdapter extends AsynListView<Item>.Adapter {

		public AutoRunAdapter(Context localContext) {
			super(localContext);
		}

		public int getCount() {
			if (!TextUtils.isEmpty(AutoRunListView.this.mkeyWord)) {
				int i = 0;
				for (int j = 0;; j++) {
					if (j >= super.getCount()) {
						return i;
					}
					Item localItem = (Item) super.getItem(j);
					if ((localItem == null) || (AutoRunListView.this.lastIndexOf(localItem) == -1))
						continue;
					i++;
				}
			}

			return super.getCount();
		}

		public Item getItem(int paramInt) {
			if (TextUtils.isEmpty(AutoRunListView.this.mkeyWord)) {
				return (Item) super.getItem(paramInt);
			}
			int i = 0;
			for (int j = paramInt;; j++) {
				if (j >= super.getCount())
					return (Item) super.getItem(paramInt);
				Item localItem = (Item) super.getItem(j);
				if ((localItem == null) || (AutoRunListView.this.lastIndexOf(localItem) == -1))
					continue;
				else if (i++ < paramInt)
					continue;
				else
					return (Item) super.getItem(j);
			}
		}

		public Item getItem2(int paramInt) {
			/*
			 * int i; if (!TextUtils.isEmpty(AutoRunListView.this.mkeyWord)) i =
			 * -1; for (int j = 0; ; j++) { Item localItem; if (j >=
			 * super.getCount()) localItem = (Item)super.getItem(paramInt); do {
			 * // return localItem; localItem = (Item)super.getItem(j); if
			 * ((localItem == null) ||
			 * (AutoRunListView.this.lastIndexOf(localItem) == -1)) break; i++;
			 * } while (i == paramInt); } return null;
			 */Item localItem = (Item) super.getItem(paramInt);
			if (localItem != null)
				return localItem;
			else {
				System.out.println("##MEME##, AutoRunListView.java getItem(), localItem == null");
				return null;
			}
		}
	}

	class ComponentSettingTask extends AsyncTask<Void, Void, Boolean> {
		private boolean enable;
		private Item item;
		private ProgressDialog progressDialog;

		public ComponentSettingTask(AutoRunListView view, Item item, boolean arg3) {
			this.item = item;
			this.enable = arg3;
			this.progressDialog = new ProgressDialog(AutoRunListView.this.getContext());
			this.progressDialog.setProgressStyle(0);
			this.progressDialog.setCancelable(false);
			this.progressDialog.setMessage("正在操作，请稍侯...");
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			Iterator iterator = item.componentNames.iterator();
			if (iterator.hasNext()) {
				// System.out.println("--------\n" +
				// (ComponentName)iterator.next() + " enable: " + enable);
				BootApplication.getInstance().getShellCommand()
						.setComponentSetting((ComponentName) iterator.next(), enable);
			}
			return enable;
		}

		/*
		 * protected Boolean doInBackground2(Void... params) { Iterator
		 * localIterator1 = this.item.componentNames.iterator();
		 * 
		 * Item localItem; while (true) { Iterator localIterator2; if
		 * (!localIterator1.hasNext()) { localItem = null; Intent localIntent =
		 * new Intent( "android.intent.action.BOOT_COMPLETED");
		 * localIntent.setPackage(this.item.apkInfo.packageName); localIterator2
		 * = AutoRunListView.this.getContext() .getPackageManager()
		 * .queryBroadcastReceivers(localIntent,
		 * PackageManager.GET_DISABLED_COMPONENTS) .iterator(); if
		 * (!localIterator2.hasNext()) { if (localItem != null) break; return
		 * Boolean.valueOf(false); } } else { ComponentName localComponentName1
		 * = (ComponentName) localIterator1 .next(); MyApplication
		 * .getInstance() .getShellCommand()
		 * .setComponentSetting(localComponentName1, this.enable); continue; }
		 * ResolveInfo localResolveInfo = (ResolveInfo) localIterator2 .next();
		 * localItem = new Item(null); ComponentName localComponentName2 = new
		 * ComponentName( localResolveInfo.activityInfo.packageName,
		 * localResolveInfo.activityInfo.name);
		 * 
		 * boolean bool = false; if
		 * (AutoRunListView.this.getContext().getPackageManager()
		 * .getComponentEnabledSetting(localComponentName2) != 2) bool = true;
		 * 
		 * localItem.addComponent(bool, localComponentName2); // break; } return
		 * Boolean.valueOf(localItem.enable); }
		 */
		protected void onPostExecute(Boolean paramBoolean) {
			super.onPostExecute(paramBoolean);
			this.progressDialog.dismiss();
			this.item.enable = paramBoolean.booleanValue();
			AutoRunListView.this.mAdapter.sort(SortMethods.getComparator(BootApplication.getInstance()
					.getConfigHelper().getSortMode()));
		}

		protected void onPreExecute() {
			super.onPreExecute();
			this.progressDialog.show();
		}
	}

	class ItemView extends FrameLayout implements CompoundButton.OnCheckedChangeListener {
		private TextView appName;
		private CheckBox checkBox;
		private ImageView icon;
		private Item item;
		private TextView state;
		private TextView tips;

		public ItemView(Context localContext) {
			super(localContext);
			View.inflate(localContext, R.layout.apk_item, this);
			this.icon = ((ImageView) findViewById(R.id.icon));
			this.appName = ((TextView) findViewById(R.id.appname));
			this.state = ((TextView) findViewById(R.id.state));
			this.tips = ((TextView) findViewById(R.id.tips));
			this.checkBox = ((CheckBox) findViewById(R.id.btnEnable));
		}

		public void bindView(int position, Item paramItem) {
			this.item = paramItem;
			this.icon.setImageDrawable(paramItem.apkInfo.icon);
			this.appName.setText(paramItem.apkInfo.name);
			TextView localTextView = this.state;
			String str = null;
			if (paramItem.enable)
				str = "[未禁止]";
			else
				str = "[已禁止]";

			localTextView.setText(str);
			this.state.setEnabled(paramItem.enable);
			this.checkBox.setOnCheckedChangeListener(null);
			this.checkBox.setChecked(paramItem.enable);
			this.checkBox.setOnCheckedChangeListener(this);
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			new AutoRunListView.ComponentSettingTask(AutoRunListView.this, this.item, isChecked).execute(new Void[0]);
		}
	}
}
