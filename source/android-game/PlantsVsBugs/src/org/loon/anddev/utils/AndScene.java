package org.loon.anddev.utils;

import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.input.touch.TouchEvent;

public abstract class AndScene extends Scene implements IAndScene,
		IOnAreaTouchListener, IOnSceneTouchListener {

	public static int BACKGROUND_LAYER = 0;
	public static int GAME_LAYER = 1;
	public static int EXTRA_GAME_LAYER = 2;
	public static int GUI_LAYER = 3;
	public static int FADE_LAYER = 4;

	public AndScene() {
		super();

		attachChild(new Entity());
		attachChild(new Entity());
		attachChild(new Entity());
		attachChild(new Entity());
		attachChild(new AndFadeLayer());

		setOnAreaTouchListener(this);
		setOnSceneTouchListener(this);

		createScene();
		((AndFadeLayer) getChild(FADE_LAYER)).fadeOut();
	}

	public void replaceLayer(final int pLayerID, final IEntity pLayer) {
		detachChild(getChild(pLayerID));
		insertChild(pLayerID, pLayer);
	}

	public void setFadeDelay(final float pDelay) {
		((AndFadeLayer) getChild(FADE_LAYER)).setFadeDelay(pDelay);
	}

	public void setFadeDuration(final float pDuration) {
		((AndFadeLayer) getChild(FADE_LAYER)).setFadeDuration(pDuration);
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
			final ITouchArea pTouchArea, final float pTouchAreaLocalX,
			final float pTouchAreaLocalY) {
		if (pSceneTouchEvent.isActionDown()) {
			manageAreaTouch(pTouchArea);
			downSceneTouch(pSceneTouchEvent);
			return true;
		} else if (pSceneTouchEvent.isActionMove()) {
			moveSceneTouch(pSceneTouchEvent);
			return true;
		} else if (pSceneTouchEvent.isActionUp()) {
			upSceneTouch(pSceneTouchEvent);
			return true;
		}
		return false;
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene,
			final TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.isActionDown()) {
			manageSceneTouch(pSceneTouchEvent);
			downSceneTouch(pSceneTouchEvent);
			return true;
		} else if (pSceneTouchEvent.isActionMove()) {
			moveSceneTouch(pSceneTouchEvent);
			return true;
		} else if (pSceneTouchEvent.isActionUp()) {
			upSceneTouch(pSceneTouchEvent);
			return true;
		}
		return false;
	}

	public abstract void downSceneTouch(TouchEvent pSceneTouchEvent);

	public abstract void moveSceneTouch(TouchEvent pSceneTouchEvent);

	public abstract void upSceneTouch(TouchEvent pSceneTouchEvent);
}
