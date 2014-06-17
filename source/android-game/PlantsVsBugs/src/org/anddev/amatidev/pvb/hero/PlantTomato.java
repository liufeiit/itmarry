package org.anddev.amatidev.pvb.hero;

import org.anddev.amatidev.pvb.obj.Camp;
import org.anddev.amatidev.pvb.singleton.GameData;

public class PlantTomato extends Hero {
	
	public PlantTomato() {
		super(GameData.getInstance().mPlantTomato);
		this.setCamp(Camp.TIANZAI);
	}
	
}
