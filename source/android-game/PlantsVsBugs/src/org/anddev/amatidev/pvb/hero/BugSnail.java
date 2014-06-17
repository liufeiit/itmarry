package org.anddev.amatidev.pvb.hero;

import org.anddev.amatidev.pvb.obj.Camp;
import org.anddev.amatidev.pvb.singleton.GameData;

public class BugSnail extends Hero {
	
	public BugSnail() {
		super(GameData.getInstance().mBugSnail);
		this.setCamp(Camp.JINWEI);
	}
	
}
