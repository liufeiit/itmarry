package org.loon.anddev.utils;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.AlphaModifier;
import org.anddev.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.util.modifier.IModifier;

public class AndFadeLayer extends Entity {
	
	private Rectangle mScreenBlack;
	
	private float mDelay = 0.2f;
	private float mDuration = 0.5f;
	
	public AndFadeLayer() {
		this.mScreenBlack = new Rectangle(0, 0, AndEnviroment.getInstance().getScreenWidth(), AndEnviroment.getInstance().getScreenHeight());
		this.mScreenBlack.setColor(0f, 0f, 0f);
		attachChild(this.mScreenBlack);
	}
	
	public void setFadeDelay(final float pDelay) {
		this.mDelay = pDelay;
	}
	
	public void setFadeDuration(final float pDuration) {
		this.mDuration = pDuration;
	}
	
	public void setTransparency(final float pAlpha) {
		this.mScreenBlack.setAlpha(pAlpha);
	}
	
	public void fadeOut() {
		registerUpdateHandler(new TimerHandler(AndFadeLayer.this.mDelay + 1f, false, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				AndFadeLayer.this.mScreenBlack.registerEntityModifier(
						new AlphaModifier(AndFadeLayer.this.mDuration, 1f, 0f, new IEntityModifierListener() {
							@Override
							public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
								AndFadeLayer.this.setTransparency(0f);
								((AndScene) AndEnviroment.getInstance().getScene()).startScene();
							}
							
							@Override
							public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
								
							}
						})
				);
			}
		}));
	}
	
	public void fadeIn() {
		registerUpdateHandler(new TimerHandler(AndFadeLayer.this.mDelay, false, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				AndFadeLayer.this.mScreenBlack.registerEntityModifier(
						new AlphaModifier(AndFadeLayer.this.mDuration, 0f, 1f, new IEntityModifierListener() {
							@Override
							public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
								AndFadeLayer.this.setTransparency(1f);
								((AndScene) AndEnviroment.getInstance().getScene()).endScene();
							}
							
							@Override
							public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
								
							}
						})
				);
			}
		}));
	}
	
}