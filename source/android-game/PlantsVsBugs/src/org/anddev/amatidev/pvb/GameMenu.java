package org.anddev.amatidev.pvb;

import org.anddev.amatidev.pvb.singleton.GameData;
import org.anddev.andengine.opengl.font.Font;
import org.loon.anddev.utils.AndMenuScene;
import org.loon.anddev.utils.AndEnviroment;

public class GameMenu extends AndMenuScene {
	
	private static final int MENU_AUDIO = 0;
	private static final int MENU_VIBRO = 1;
	private static final int MENU_EXIT = 2;
	
	
    
    
	@Override
	public void createMenuScene() {
		String audio = "";
		if (AndEnviroment.getInstance().getAudio())
			audio += "ON";
		else
			audio += "OFF";
		String vibro = "";
		if (AndEnviroment.getInstance().getVibro())
			vibro += "ON";
		else
			vibro += "OFF";
		Font font = GameData.getInstance().mFontGameMenu;
		
		addTextItem(MENU_AUDIO, font, "AUDIO " + audio, 1f, 0.7f, 0.7f, 1.0f, 1.0f, 1f);
		addTextItem(MENU_VIBRO, font, "VIBRO " + vibro, 1f, 0.7f, 0.7f, 1.0f, 1.0f, 1f);
		addTextItem(MENU_EXIT, font, "EXIT", 1f, 0.7f, 0.7f, 1.0f, 1.0f, 1f);
	}

	@Override
	public boolean manageMenuItemClicked(final int pItemID) {
		GameData.getInstance().mSoundMenu.play();
		switch(pItemID) {
		case MENU_AUDIO:
			AndEnviroment.getInstance().toggleAudio();
			closeMenuScene();
			return true;
		case MENU_VIBRO:
			AndEnviroment.getInstance().toggleVibro();
			closeMenuScene();
			return true;
		case MENU_EXIT:
			((MainGame) AndEnviroment.getInstance().getScene()).clearScene();
			AndEnviroment.getInstance().setScene(new MainMenu());
			return true;
		default:
			return false;
		}
	}

}