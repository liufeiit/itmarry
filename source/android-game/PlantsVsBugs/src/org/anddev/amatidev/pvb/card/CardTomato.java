package org.anddev.amatidev.pvb.card;

import org.anddev.amatidev.pvb.hero.Hero;
import org.anddev.amatidev.pvb.hero.PlantTomato;
import org.anddev.amatidev.pvb.singleton.GameData;

public class CardTomato extends Card {

	public CardTomato() {
		super(GameData.getInstance().mCardTomato);
		
		this.mRecharge = 7f;
		this.mPrice = 4;
	}
	
	public Hero getPlant() {
		return new PlantTomato();
	}
	
}
