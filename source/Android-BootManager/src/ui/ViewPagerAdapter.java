package ui;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ViewPagerAdapter extends PagerAdapter {
	private List<View> mListViews;

	public ViewPagerAdapter(List<View> paramList) {
		this.mListViews = paramList;
	}

	public void destroyItem(View paramView, int paramInt, Object paramObject) {
		((ViewPager) paramView).removeView((View) paramObject);
	}

	public void finishUpdate(View paramView) {
	}

	public int getCount() {
		return this.mListViews.size();
	}

	public int getItemPosition(Object paramObject) {
		int i = this.mListViews.indexOf(paramObject);
		if (i < 0)
			i = -2;
		return i;
	}

	public Object instantiateItem(View paramView, int paramInt) {
		((ViewPager) paramView).addView((View) this.mListViews.get(paramInt), 0);
		return this.mListViews.get(paramInt);
	}

	public boolean isViewFromObject(View paramView, Object paramObject) {
		return paramView == paramObject;
	}

	public void removeView(View paramView) {
		this.mListViews.remove(paramView);
	}

	public void restoreState(Parcelable paramParcelable, ClassLoader paramClassLoader) {
	}

	public Parcelable saveState() {
		return null;
	}

	public void startUpdate(View paramView) {
	}
}