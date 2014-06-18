package com.example.text;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.way.draglistview.DragSortListView;
import com.way.menu.adapter.Category;
import com.way.menu.adapter.Item;
import com.way.menu.adapter.MenuAdapter;
import com.way.pulltorefresh.PullToRefreshBase;
import com.way.pulltorefresh.PullToRefreshBase.Mode;
import com.way.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.way.pulltorefresh.PullToRefreshBase.State;
import com.way.pulltorefresh.PullToRefreshScrollView;
import com.way.slidinglayer.SlidingLayer;
import com.way.slidinglayer.SlidingLayer.OnInteractListener;
import com.way.slidingmenu.BaseSlidingFragmentActivity;
import com.way.slidingmenu.SlidingMenu;

public class MainActivity extends BaseSlidingFragmentActivity implements
		OnInteractListener {
	private DragSortListView mDslv;
	private WeatherAdapter mWeatherAdapter;
	PullToRefreshScrollView mPullRefreshScrollView;

	private ImageView mNormalImageView;
	private ImageView mBlurredImageView;
	private View mHeaderView;

	private boolean mFirstGlobalLayoutPerformed;
	private int mLastDampedScroll;
	private int mHeaderHeight = -1;

	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;

	private String mCurrentFragmentTag;
	private SlidingLayer mSlidingLayer;
	private MenuAdapter mMenuAdapter;
	private ListView mMenuListView;
	private SlidingMenu mSlidingMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initSlidingMenu();
		setContentView(R.layout.above_slidingmenu);
		initView();
	}

	private void initSlidingMenu() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
		setBehindContentView(R.layout.behind_slidingmenu);
		// customize the SlidingMenu
		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setShadowWidth(mScreenWidth / 40);// 设置阴影宽度
		mSlidingMenu.setBehindOffset(mScreenWidth / 8);// 设置菜单宽度
		mSlidingMenu.setFadeDegree(0.35f);// 设置淡入淡出的比例
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		mSlidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);// 设置左菜单阴影图片
		mSlidingMenu.setFadeEnabled(true);// 设置滑动时菜单的是否淡入淡出
		mSlidingMenu.setBehindScrollScale(0.333f);// 设置滑动时拖拽效果

	}

	private void initView() {
		mFragmentManager = getSupportFragmentManager();
		mDslv = (DragSortListView) findViewById(R.id.drag_list);
		mNormalImageView = (ImageView) findViewById(R.id.weather_background);
		mBlurredImageView = (ImageView) findViewById(R.id.weather_background_blurred);
		mBlurredImageView.getDrawable().setAlpha((int) (0 * 255));// 设置透明

		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(
				"最近更新：刚刚");
		mPullRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						new GetDataTask().execute();
					}
				});
		mHeaderView = LayoutInflater.from(this).inflate(
				R.layout.current_condition, null);
		mHeaderHeight = getDisplayHeight(this)
				- getResources().getDimensionPixelOffset(
						R.dimen.abs__action_bar_default_height);
		mHeaderView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				mHeaderHeight));
		mDslv.addHeaderView(mHeaderView, null, false);
		mWeatherAdapter = new WeatherAdapter(this);
		mDslv.setAdapter(mWeatherAdapter);
		mDslv.setDropListener(onDrop);
		mDslv.setOnScrollListener(mOnScrollListener);// 监听滑动

		findViewById(R.id.top_part).getViewTreeObserver()
				.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (!mFirstGlobalLayoutPerformed) {
							ViewGroup.LayoutParams norParams = (ViewGroup.LayoutParams) mNormalImageView
									.getLayoutParams();
							norParams.height = getDisplayHeight(MainActivity.this)
									+ mHeaderHeight / 8;
							mNormalImageView.setLayoutParams(norParams);

							ViewGroup.LayoutParams blurParams = (ViewGroup.LayoutParams) mBlurredImageView
									.getLayoutParams();
							blurParams.height = getDisplayHeight(MainActivity.this)
									+ mHeaderHeight / 8;
							mBlurredImageView.setLayoutParams(blurParams);
							mFirstGlobalLayoutPerformed = true;
						}
					}
				});
		mSlidingLayer = (SlidingLayer) findViewById(R.id.sliding_layer);
		mSlidingLayer.setOnInteractListener(this);
		mMenuListView = (ListView) findViewById(R.id.menu_list);
		initAdapter();
	}

	private void initAdapter() {
		List<Object> items = new ArrayList<Object>();
		items.add(new Category("城市管理"));
		items.add(new Item("编辑地点", R.drawable.editloc));
		items.add(new Item("深圳", R.drawable.loc));
		items.add(new Item("长沙", R.drawable.loc));
		items.add(new Category("工具"));
		items.add(new Item("设置", R.drawable.sidebar_icon_settings_dark));
		items.add(new Item("意见与建议", R.drawable.sidebar_icon_send_feedback_dark));
		items.add(new Item("分享", R.drawable.sidebar_icon_share_dark));
		items.add(new Item("打分", R.drawable.sidebar_icon_rate_dark));
		mMenuAdapter = new MenuAdapter(this, items);
		mMenuListView.setAdapter(mMenuAdapter);
		mMenuListView.setOnItemClickListener(mItemClickListener);
	}

	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Object item = mMenuAdapter.getItem(position);
			if (item instanceof Category) {
				return;
			}
			onMenuItemClicked(position, (Item) item);
		}
	};

	protected void onMenuItemClicked(int position, Item item) {
		if (mCurrentFragmentTag != null
				&& !TextUtils.equals(mCurrentFragmentTag, item.mTitle))
			detachFragment(getFragment(mCurrentFragmentTag));
		attachFragment(R.id.sliding_layer_frame, getFragment(item.mTitle),
				item.mTitle);
		mCurrentFragmentTag = item.mTitle;
		mSlidingMenu.showContent(true);
		mSlidingLayer.openLayer(true);
		commitTransactions();
	}

	public int getDisplayHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int displayHeight = wm.getDefaultDisplay().getHeight();
		return displayHeight;
	}

	private OnScrollListener mOnScrollListener = new OnScrollListener() {
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			View topChild = view.getChildAt(0);
			if (topChild == null) {
				onNewScroll(0);

			} else if (topChild != mHeaderView) {
				onNewScroll(mHeaderView.getHeight());
				// Log.i("lwp",
				// "topChild != mHeaderView =" + mHeaderView.getHeight());
			} else {
				onNewScroll(-topChild.getTop());
				// Log.i("lwp", "-topChild.getTop() = " + (-topChild.getTop()));
			}

		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}
	};

	private void onNewScroll(int scrollPosition) {
		if (scrollPosition == 0) {
			mPullRefreshScrollView.setMode(Mode.PULL_FROM_START);
		} else {
			// if (mPullRefreshScrollView.getState() != State.REFRESHING) {
			// mPullRefreshScrollView.onCancleRefresh();
			// } else {
			// if (scrollPosition > mHeaderHeight / 2) {
			// // mPullRefreshScrollView.onRefreshComplete();
			// }
			// }
			if (mPullRefreshScrollView.getState() == State.RESET) {
				mPullRefreshScrollView.onCancleRefresh();
			} else if (mPullRefreshScrollView.getState() == State.REFRESHING
					|| mPullRefreshScrollView.getState() == State.MANUAL_REFRESHING) {
				if (scrollPosition > mHeaderHeight / 2) {
					mPullRefreshScrollView.onRefreshComplete();
				}
			}
		}
		float ratio = Math.min(
				(1.5f * (float) -mHeaderView.getTop() / (float) mHeaderHeight),
				1);
		int newAlpha = (int) (ratio * 255);
		// Apply on the ImageView if needed
		mBlurredImageView.getDrawable().setAlpha(newAlpha);

		int dampedScroll = (int) (scrollPosition * 0.125f);
		int offset = mLastDampedScroll - dampedScroll;
		mBlurredImageView.offsetTopAndBottom(offset);
		mNormalImageView.offsetTopAndBottom(offset);
		if (mFirstGlobalLayoutPerformed)
			mLastDampedScroll = dampedScroll;
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			// Do some stuff here
			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshScrollView.onRefreshComplete();
			mPullRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(
					"最近更新：刚刚");
			super.onPostExecute(result);
		}
	}

	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			mPullRefreshScrollView.onCancleRefresh();
			if (from == 0 || to == 0)
				return;
			if (from != to) {
				Integer item = mWeatherAdapter.getItem(from);
				mWeatherAdapter.remove(item);
				mWeatherAdapter.insert(item, to);
			}
		}
	};

	@Override
	public void onOpen() {
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
	}

	@Override
	public void onClose() {
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
	}

	@Override
	public void onOpened() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClosed() {

	}

	protected FragmentTransaction ensureTransaction() {
		if (mFragmentTransaction == null) {
			mFragmentTransaction = mFragmentManager.beginTransaction();
			mFragmentTransaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		}

		return mFragmentTransaction;
	}

	private Fragment getFragment(String tag) {
		Fragment f = mFragmentManager.findFragmentByTag(tag);
		if (f == null) {
			// 在这里判断tag,不同则实例化对应的fragment，
			f = SampleFragment.newInstance(tag);
		}

		return f;
	}

	protected void attachFragment(int layout, Fragment f, String tag) {
		if (f != null) {
			if (f.isDetached()) {
				ensureTransaction();
				mFragmentTransaction.attach(f);
			} else if (!f.isAdded()) {
				ensureTransaction();
				mFragmentTransaction.add(layout, f, tag);
			}
		}
	}

	protected void detachFragment(Fragment f) {
		if (f != null && !f.isDetached()) {
			ensureTransaction();
			mFragmentTransaction.detach(f);
		}
	}

	protected void commitTransactions() {
		if (mFragmentTransaction != null && !mFragmentTransaction.isEmpty()) {
			mFragmentTransaction.commit();
			mFragmentTransaction = null;
		}
	}

	public static class SampleFragment extends Fragment {

		private static final String ARG_TEXT = "way";

		public static SampleFragment newInstance(String text) {
			SampleFragment f = new SampleFragment();

			Bundle args = new Bundle();
			args.putString(ARG_TEXT, text);
			f.setArguments(args);

			return f;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_sample, container,
					false);

			((TextView) v.findViewById(R.id.text)).setText(getArguments()
					.getString(ARG_TEXT));

			return v;
		}
	}

	/**
	 * 连续按两次返回键就退出
	 */
	private long firstTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mSlidingLayer.isOpened()) {
				mSlidingLayer.closeLayer(true);
			} else {
				if (System.currentTimeMillis() - firstTime < 3000) {
					finish();
				} else {
					firstTime = System.currentTimeMillis();
					Toast.makeText(
							this,
							getResources().getString(R.string.press_again_exit),
							Toast.LENGTH_SHORT).show();
				}
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {

			if (mSlidingMenu.isMenuShowing()) {
				mSlidingMenu.showContent(true);
			} else {
				showMenu();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
