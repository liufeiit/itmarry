package org.anddev.amatidev.pvb.card;

import org.anddev.amatidev.pvb.hero.Hero;
import org.anddev.amatidev.pvb.hero.PlantMelon;
import org.anddev.amatidev.pvb.singleton.GameData;

public class CardMelon extends Card {

	public CardMelon() {
		super(GameData.getInstance().mCardMelon);
		
		this.mRecharge = 18f;
		this.mPrice = 10;
	}
	
	public Hero getPlant() {
		return new PlantMelon();
	}
	
}
