package org.anddev.amatidev.pvb.card;

import org.anddev.amatidev.pvb.hero.Hero;
import org.anddev.amatidev.pvb.hero.PlantBag;
import org.anddev.amatidev.pvb.singleton.GameData;

public class CardBag extends Card {

	public CardBag() {
		super(GameData.getInstance().mCardBag);
		
		this.mRecharge = 7f;
		this.mPrice = 2;
	}
	
	public Hero getPlant() {
		return new PlantBag();
	}
	
}
