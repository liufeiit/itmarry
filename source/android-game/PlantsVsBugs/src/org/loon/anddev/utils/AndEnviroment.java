package org.loon.anddev.utils;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.SimplePreferences;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;

public class AndEnviroment {
	
	private static AndEnviroment mInstance = null;
	
	private Context mContext = null;
	
	private AudioManager mAudioManager = null;
	
	private int mScreenWidth = 480;
	
	private int mScreenHeight = 720;
	
	private AndEnviroment() {
		
	}
	
	public void showMessage(String message){
		
	}
	
	public static synchronized AndEnviroment getInstance() {
		if (mInstance == null)
			mInstance = new AndEnviroment();
		return mInstance;
	}
	
	public static Engine createEngine(final ScreenOrientation pScreenOrientation, final boolean pEnableSound, final boolean pEnableMusic) {
		int pScreenWidth = 480;
		int pScreenHeight = 720;
		if (pScreenOrientation == ScreenOrientation.LANDSCAPE) {
			pScreenWidth = 720;
			pScreenHeight = 480;
		}
		return createEngine(pScreenOrientation, pScreenWidth, pScreenHeight, pEnableSound, pEnableMusic);
	}
	
	public static Engine createEngine(final ScreenOrientation pScreenOrientation, final int pScreenWidth, final int pScreenHeight, final boolean pEnableSound, final boolean pEnableMusic) {
		Camera camera = new Camera(0, 0, pScreenWidth, pScreenHeight);
		EngineOptions engineOptions = new EngineOptions(true, pScreenOrientation, new FillResolutionPolicy(), camera);
		
		engineOptions.setNeedsSound(pEnableSound);
		engineOptions.setNeedsMusic(pEnableMusic);
		engineOptions.getTouchOptions().setRunOnUpdateThread(true);
		return new Engine(engineOptions);
	}
	
	public void initContext(final Context pCtx) {
		this.mContext = pCtx;
		
		this.mScreenWidth = (int) getEngine().getCamera().getWidth();
		this.mScreenHeight = (int) getEngine().getCamera().getHeight();
		
		this.mAudioManager = (AudioManager) this.mContext.getSystemService(Service.AUDIO_SERVICE);

		getEngine().enableVibrator(this.mContext);
	}
	
	public int getScreenWidth() {
		return this.mScreenWidth;
	}
	
	public int getScreenHeight() {
		return this.mScreenHeight;
	}
	
	public Context getContext() {
        return this.mContext;
	}
	
	public AudioManager getAudioManager() {
        return this.mAudioManager;
	}
	
	public Engine getEngine() {
		return ((BaseGameActivity) getContext()).getEngine();
	}
	
	public Scene getScene() {
		return getEngine().getScene();
	}
	
	public void nextScene() {
		((AndFadeLayer) getScene().getChild(AndScene.FADE_LAYER)).fadeIn();
	}
	
	public void setScene(final Scene pScene) {
		getEngine().setScene(pScene);
	}
	
	public void safeDetachEntity(final IEntity pItem) {
		getEngine().runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				pItem.detachSelf();
			}
		});
	}
	
	public boolean getAudio() {
		return SimplePreferences.getValue(this.mContext, "audio", true);
	}
	
	public void toggleAudio() {
		boolean value = !(getAudio());
		SimplePreferences.setValue(this.mContext, "audio", value);
	}
	
	public boolean getVibro() {
		return SimplePreferences.getValue(this.mContext, "vibro", true);
	}
	
	public void toggleVibro() {
		boolean value = !(getVibro());
		SimplePreferences.setValue(this.mContext, "vibro", value);
	}
	
}
