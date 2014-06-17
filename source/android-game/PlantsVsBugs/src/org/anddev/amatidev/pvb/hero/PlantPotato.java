package org.anddev.amatidev.pvb.hero;

import org.anddev.amatidev.pvb.obj.Camp;
import org.anddev.amatidev.pvb.singleton.GameData;

public class PlantPotato extends Hero {
	
	public PlantPotato() {
		super(GameData.getInstance().mPlantPotato);
		this.setCamp(Camp.TIANZAI);
	}
	
}
