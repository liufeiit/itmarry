package org.anddev.amatidev.pvb.obj;

import org.anddev.amatidev.pvb.card.Card;
import org.anddev.amatidev.pvb.singleton.GameData;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.loon.anddev.utils.AndEnviroment;

public class Dialog extends Sprite {

	private Text mText;

	public Dialog(final String pText) {
		super(0, 0, GameData.getInstance().mDialog);
		setPosition(AndEnviroment.getInstance().getScreenWidth() / 2 - getWidthScaled() / 2, AndEnviroment.getInstance().getScreenHeight() / 2 - getHeightScaled() / 2);
		
		this.mText = new Text(0, 0, GameData.getInstance().mFontEvent, pText);
		this.mText.setColor(1.0f, 0.3f, 0.3f);
		this.mText.setPosition(getWidthScaled() / 2 - this.mText.getWidthScaled() / 2, 40);
		attachChild(this.mText);
		
		registerEntityModifier(new ScaleModifier(0.5f, 0f, 1f));
	}

	public void add(Card pCard) {
		pCard.fullRecharge();
		pCard.setPosition(getWidthScaled() / 2 - pCard.getWidthScaled() / 2, 115);
		attachChild(pCard);
	}
	
	public String getTitle() {
		return this.mText.getText();
	}
	
}
