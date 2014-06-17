package org.anddev.amatidev.pvb.card;

import org.anddev.amatidev.pvb.hero.Hero;
import org.anddev.amatidev.pvb.hero.PlantPotato;
import org.anddev.amatidev.pvb.singleton.GameData;

public class CardPotato extends Card {

	public CardPotato() {
		super(GameData.getInstance().mCardPotato);
		
		this.mRecharge = 21f;
		this.mPrice = 2;
	}
	
	public Hero getPlant() {
		return new PlantPotato();
	}
	
}
