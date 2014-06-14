package com.itjiehun.magic.zh;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cn.waps.AppConnect;

import com.itjiehun.magic.MainView;
import com.itjiehun.magic.Tile;
import com.itjiehun.magic.umeng.UmengStatic;
import com.itjiehun.magic.waps.WapsStatic;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends Activity {

	private MainView view;
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String SCORE = "score";
	public static final String HIGH_SCORE = "high score temp";
	public static final String UNDO_SCORE = "undo score";
	public static final String CAN_UNDO = "can undo";
	public static final String UNDO_GRID = "undo";
	public static final String GAME_STATE = "game state";
	public static final String UNDO_GAME_STATE = "undo game state";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		view = new MainView(getBaseContext());

		AppConnect.getInstance(this);
		AppConnect.getInstance(WapsStatic.APP_ID, WapsStatic.APP_PID, this);
		
		MobclickAgent.onResume(this, UmengStatic.UMENG_APPKEY, UmengStatic.UMENG_CHANNEL);

		UmengUpdateAgent.setAppkey(UmengStatic.UMENG_APPKEY);
		UmengUpdateAgent.setChannel(UmengStatic.UMENG_CHANNEL);
		UmengUpdateAgent.update(this);
		MobclickAgent.updateOnlineConfig(this);
		UmengUpdateAgent.setUpdateCheckConfig(true);
		UmengUpdateAgent.setUpdateAutoPopup(true);

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		view.hasSaveState = settings.getBoolean("save_state", false);
		if (savedInstanceState != null) {
			if (savedInstanceState.getBoolean("hasState")) {
				load();
			}
		}
		setContentView(view);
		String ads = MobclickAgent.getConfigParams(this, UmengStatic.V360_ADS_KEY);
		Log.e("umeng ads", "ads : " + ads);
//		if("1".equals(ads) || "on".equalsIgnoreCase(ads) || "true".equalsIgnoreCase(ads)) {
//			LinearLayout adlayout = new LinearLayout(this);
//			adlayout.setGravity(Gravity.BOTTOM);
//			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
//					ViewGroup.LayoutParams.MATCH_PARENT);
//			AppConnect.getInstance(this).showBannerAd(this, adlayout);
//			//layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);// 此代码可设置顶端或低端
//			addContentView(adlayout, layoutParams);
//		}
		/*MobclickAgent.setOnlineConfigureListener(new UmengOnlineConfigureListener() {
					public void onDataReceived(JSONObject data) {
					}
				});*/
		
		LinearLayout adlayout = new LinearLayout(this);
		adlayout.setGravity(Gravity.BOTTOM);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		AppConnect.getInstance(this).showBannerAd(this, adlayout);
		//layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);// 此代码可设置顶端或低端
		addContentView(adlayout, layoutParams);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			// Do nothing
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			view.game.move(2);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			view.game.move(0);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			view.game.move(3);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			view.game.move(1);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putBoolean("hasState", true);
		save();
	}

	protected void onPause() {
		super.onPause();
		save();
		// 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPageEnd("Super2048SplashScreen");
		MobclickAgent.onPause(this);
		AppConnect.getInstance(this).close();
	}

	private void save() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = settings.edit();
		Tile[][] field = view.game.grid.field;
		Tile[][] undoField = view.game.grid.undoField;
		editor.putInt(WIDTH, field.length);
		editor.putInt(HEIGHT, field.length);
		for (int xx = 0; xx < field.length; xx++) {
			for (int yy = 0; yy < field[0].length; yy++) {
				if (field[xx][yy] != null) {
					editor.putInt(xx + " " + yy, field[xx][yy].getValue());
				} else {
					editor.putInt(xx + " " + yy, 0);
				}

				if (undoField[xx][yy] != null) {
					editor.putInt(UNDO_GRID + xx + " " + yy, undoField[xx][yy].getValue());
				} else {
					editor.putInt(UNDO_GRID + xx + " " + yy, 0);
				}
			}
		}
		editor.putLong(SCORE, view.game.score);
		editor.putLong(HIGH_SCORE, view.game.highScore);
		editor.putLong(UNDO_SCORE, view.game.lastScore);
		editor.putBoolean(CAN_UNDO, view.game.canUndo);
		editor.putInt(GAME_STATE, view.game.gameState);
		editor.putInt(UNDO_GAME_STATE, view.game.lastGameState);
		editor.commit();
	}

	protected void onResume() {
		super.onResume();
		load();
		MobclickAgent.onPageStart("Super2048SplashScreen"); // 统计页面
		MobclickAgent.onResume(this);
	}

	private void load() {
		// Stopping all animations
		view.game.aGrid.cancelAnimations();

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		for (int xx = 0; xx < view.game.grid.field.length; xx++) {
			for (int yy = 0; yy < view.game.grid.field[0].length; yy++) {
				int value = settings.getInt(xx + " " + yy, -1);
				if (value > 0) {
					view.game.grid.field[xx][yy] = new Tile(xx, yy, value);
				} else if (value == 0) {
					view.game.grid.field[xx][yy] = null;
				}

				int undoValue = settings.getInt(UNDO_GRID + xx + " " + yy, -1);
				if (undoValue > 0) {
					view.game.grid.undoField[xx][yy] = new Tile(xx, yy, undoValue);
				} else if (value == 0) {
					view.game.grid.undoField[xx][yy] = null;
				}
			}
		}

		view.game.score = settings.getLong(SCORE, view.game.score);
		view.game.highScore = settings.getLong(HIGH_SCORE, view.game.highScore);
		view.game.lastScore = settings.getLong(UNDO_SCORE, view.game.lastScore);
		view.game.canUndo = settings.getBoolean(CAN_UNDO, view.game.canUndo);
		view.game.gameState = settings.getInt(GAME_STATE, view.game.gameState);
		view.game.lastGameState = settings.getInt(UNDO_GAME_STATE, view.game.lastGameState);
	}
}
