package org.anddev.amatidev.pvb.card;

import org.anddev.amatidev.pvb.hero.Hero;
import org.anddev.amatidev.pvb.hero.PlantOrange;
import org.anddev.amatidev.pvb.singleton.GameData;

public class CardOrange extends Card {

	public CardOrange() {
		super(GameData.getInstance().mCardOrange);
		
		this.mRecharge = 14f;
		this.mPrice = 8;
	}
	
	public Hero getPlant() {
		return new PlantOrange();
	}
	
}
