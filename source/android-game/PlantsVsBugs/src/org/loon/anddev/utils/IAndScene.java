package org.loon.anddev.utils;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.input.touch.TouchEvent;

public interface IAndScene {
	
	public void createScene();
	public void startScene();
	public void endScene();
	
	public MenuScene createMenu();
	
	public void manageAreaTouch(final ITouchArea pTouchArea);
	public void manageSceneTouch(final TouchEvent pSceneTouchEvent);
	
	public void replaceLayer(final int pLayerID, final IEntity pLayer);
	
	public void setFadeDelay(final float pDelay);
	public void setFadeDuration(final float pDuration);
	
}
