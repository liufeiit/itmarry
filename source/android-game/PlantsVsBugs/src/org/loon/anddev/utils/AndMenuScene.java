package org.loon.anddev.utils;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.opengl.font.Font;

public abstract class AndMenuScene extends MenuScene implements IAndMenuScene, IOnMenuItemClickListener {
	
	protected float mTransparency = 0.65f;
	
	public AndMenuScene() {
		super(AndEnviroment.getInstance().getEngine().getCamera());		
		setBackgroundEnabled(false);
		setOnMenuItemClickListener(this);
		
		createMenuScene();
		buildAnimations();
	}
	
	public float getTransparency() {
		return this.mTransparency;
	}
	
	public void setTransparency(final float pTransparency) {
		this.mTransparency = pTransparency;
	}
	
	protected void addTextItem(final int pItemID, final Font pFont, final String pLabel, final float pSelRed, final float pSelGreen, final float pSelBlue, final float pRed, final float pGreen, final float pBlue) {
		IMenuItem resetMenuItem = new ColorMenuItemDecorator(new TextMenuItem(pItemID, pFont, pLabel), pSelRed, pSelGreen, pSelBlue, pRed, pGreen, pBlue);
		resetMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		addMenuItem(resetMenuItem);
	}
	
	public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY) {
		return manageMenuItemClicked(pMenuItem.getID());
	}
	
	public void closeMenuScene() {
		AndScene scene = (AndScene) AndEnviroment.getInstance().getScene();
		scene.clearChildScene();
		((AndFadeLayer) scene.getChild(AndScene.FADE_LAYER)).setTransparency(0f);
	}
	
}
