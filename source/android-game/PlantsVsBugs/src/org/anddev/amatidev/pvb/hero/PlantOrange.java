package org.anddev.amatidev.pvb.hero;

import org.anddev.amatidev.pvb.obj.Camp;
import org.anddev.amatidev.pvb.singleton.GameData;

public class PlantOrange extends Hero {
	
	public PlantOrange() {
		super(GameData.getInstance().mPlantOrange);
		this.setCamp(Camp.TIANZAI);
		this.mShotDouble = true;
	}
	
}
