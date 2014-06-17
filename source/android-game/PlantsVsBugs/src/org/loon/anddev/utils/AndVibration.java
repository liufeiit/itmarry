package org.loon.anddev.utils;

import android.media.AudioManager;

public class AndVibration {
	
	public static void duration(final int pMilliseconds) {
		int mode = AndEnviroment.getInstance().getAudioManager().getRingerMode();
		
		if (mode >= AudioManager.RINGER_MODE_VIBRATE && AndEnviroment.getInstance().getVibro()) {
			try {
				AndEnviroment.getInstance().getEngine().vibrate(pMilliseconds);
			} catch (Exception e) {
				
			}
		}
	}
	
}