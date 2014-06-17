package org.anddev.amatidev.pvb.card;

import java.util.LinkedList;

import org.anddev.amatidev.pvb.hero.Hero;
import org.anddev.amatidev.pvb.singleton.GameData;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.util.modifier.IModifier;
import org.loon.anddev.utils.AndEnviroment;

public abstract class Card extends Sprite {

	protected float mRecharge = 10f;
	protected boolean mReady = false;
	protected int mPrice = 2;

	private Rectangle mBlack;
	
	public Card(final TextureRegion pTextureRegion) {
		super(0, 0, GameData.getInstance().mCard);
		Sprite image = new Sprite(4, 4, pTextureRegion);
		attachChild(image);
	}
	
	public void onAttached() {
		ChangeableText value = new ChangeableText(0, 0, GameData.getInstance().mFontCard, Integer.toString(this.mPrice), 3);
		value.setPosition(31 - value.getWidthScaled() / 2 , 66 - value.getHeightScaled() / 2);
		attachChild(value);
		
		this.mBlack = new Rectangle(1, 1, this.mBaseWidth - 2, this.mBaseHeight - 2);
		this.mBlack.setColor(0f, 0f, 0f);
		this.mBlack.setAlpha(0.5f);
		this.mBlack.setScaleCenter(0, 0);
		attachChild(this.mBlack);
		
		AndEnviroment.getInstance().getScene().registerTouchArea(this);
		
		startRecharge();
	}
	
	public void fullRecharge() {
		this.mRecharge = 0f;
	}
	
	public void startRecharge() {
		this.mReady = false;
		this.mBlack.setScaleY(1f);

		if (this.mRecharge != 0f) {
			this.mBlack.registerEntityModifier(new ScaleModifier(this.mRecharge, 1f, 1f, 1f, 0f, new IEntityModifierListener() {
				@Override
				public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
					Card.this.mReady = true;
				}
				
				@Override
				public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
					
				}
			}));
		} else
			this.mBlack.setScaleY(0f);
	}

	public boolean isReady() {
		return this.mReady;
	}

	public int getPrice() {
		return this.mPrice;
	}

	public Card makeSelect() {
		Card sel = null;
		LinkedList<Card> cards = GameData.getInstance().mCards;
		for (int i = 0; i < cards.size(); i++) {
			Card c = cards.get(i);
			if (c == this) {
				if (getChildCount() == 3) {
					attachChild(new Sprite(0, 0, GameData.getInstance().mCardSelected));
					sel = c;
				} else
					c.setUnselect();
			} else
				c.setUnselect();
		}
		return sel;
	}

	protected void setUnselect() {
		if (getChildCount() > 3)
			getLastChild().detachSelf();
	}

	public abstract Hero getPlant();
	
}
