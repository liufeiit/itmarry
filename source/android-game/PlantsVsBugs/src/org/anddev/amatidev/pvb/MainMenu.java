package org.anddev.amatidev.pvb;

import org.anddev.amatidev.pvb.singleton.GameData;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.util.modifier.IModifier;
import org.loon.anddev.utils.AndPrefs;
import org.loon.anddev.utils.AndScene;
import org.loon.anddev.utils.AndEnviroment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

public class MainMenu extends AndScene {
	
	private int mIndex;
	private boolean mContinue;
	
	@Override
	public MenuScene createMenu() {
		return null;
	}

	@Override
	public void createScene() {
		Sprite back = new Sprite(0, 0, GameData.getInstance().mMainBackground);
		getChild(BACKGROUND_LAYER).attachChild(back);
		
		Sprite title = new Sprite(3, 9, GameData.getInstance().mMainTitle);
		getChild(BACKGROUND_LAYER).attachChild(title);
		title.registerEntityModifier(
				new LoopEntityModifier(
						null, 
						-1, 
						null,
						new SequenceEntityModifier(
								new ScaleModifier(0.7f, 1f, 1.02f),
								new ScaleModifier(0.7f, 1.02f, 1f)
						)
				)
		);
		
		int x = AndEnviroment.getInstance().getScreenWidth() / 2;
		
		this.mIndex = 159;
		
    	Text play = new Text(0, 0, GameData.getInstance().mFontMainMenu, "PLAY");
    	//play.setColor(1.0f, 1.0f, 0.6f);
    	play.setPosition(x - play.getWidthScaled() / 2, this.mIndex);
    	
    	Text cont = new Text(0, 0, GameData.getInstance().mFontMainMenu, "CONTINUE");
    	//more.setColor(1.0f, 1.0f, 0.6f);
    	cont.setPosition(x - cont.getWidthScaled() / 2, this.mIndex + 90);
    	
    	Text score = new Text(0, 0, GameData.getInstance().mFontMainMenu, "SCORE");
    	//score.setColor(1.0f, 1.0f, 0.6f);
    	score.setPosition(60, this.mIndex + 180);
    	
    	Text sep = new Text(0, 0, GameData.getInstance().mFontMainMenu, "/");
    	//score.setColor(1.0f, 1.0f, 0.6f);
    	sep.setPosition(250, this.mIndex + 180);
    	
    	Text more = new Text(0, 0, GameData.getInstance().mFontMainMenu, "MORE GAME");
    	//score.setColor(1.0f, 1.0f, 0.6f);
    	more.setPosition(300, this.mIndex + 180);
    	
    	getChild(AndScene.GAME_LAYER).attachChild(play);
    	getChild(AndScene.GAME_LAYER).attachChild(cont);
    	getChild(AndScene.GAME_LAYER).attachChild(sep);
    	getChild(AndScene.GAME_LAYER).attachChild(score);
    	getChild(AndScene.GAME_LAYER).attachChild(more);
    	
    	registerTouchArea(play);
    	registerTouchArea(cont);
    	registerTouchArea(score);
    	registerTouchArea(more);
	}

	@Override
	public void endScene() {
		GameData.getInstance().mMyLevel.resetScore();
		GameData.getInstance().mMyScore.resetScore();
		GameData.getInstance().mMySeed.resetScore();
		
		if (this.mContinue) {
			int lev = AndPrefs.getValue(AndEnviroment.getInstance().getContext(), "level", 0);
			GameData.getInstance().mMyLevel.addScore(lev);
			AndEnviroment.getInstance().setScene(new MainGame());
		} else {
			AndEnviroment.getInstance().setScene(new Tutorial());
		}
	}

	@Override
	public void manageAreaTouch(final ITouchArea pTouchArea) {
		final Text item = (Text) pTouchArea;
		item.setColor(1f, 0.7f, 0.7f);
		
		item.registerEntityModifier(
				new SequenceEntityModifier(
						new IEntityModifierListener() {
							@Override
							public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
								item.setColor(1.0f, 1.0f, 1.0f);
								MainMenu.this.execute(pTouchArea);
							}

							@Override
							public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
								
							}
						},
						new ScaleModifier(0.1f, 1f, 1.3f),
						new ScaleModifier(0.1f, 1.3f, 1f)
		));
	}

	private void execute(ITouchArea pTouchArea) {
		GameData.getInstance().mSoundMenu.play();
		Text item = (Text) pTouchArea;
		if ((int) item.getY() == this.mIndex) {
			this.mContinue = false;
			AndEnviroment.getInstance().nextScene();
		} else if ((int) item.getY() == this.mIndex + 90) {
			this.mContinue = true;
			AndEnviroment.getInstance().nextScene();
		} else if ((int) item.getY() == this.mIndex + 180) {
			if ((int) item.getX() > 100) {
				try{
					AndEnviroment.getInstance().getContext().startActivity(new Intent (Intent.ACTION_VIEW, Uri.parse("market://details?id=org.anddev.andengine.braingamelite")));
				} catch (ActivityNotFoundException e) {
				}
			} else {
				/*try {
					Dashboard.open();
				} catch (Exception e) {
				}*/
			}
		}
	}
	
	@Override
	public void startScene() {
		
	}

	@Override
	public void downSceneTouch(TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveSceneTouch(TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void upSceneTouch(TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void manageSceneTouch(TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		
	}

}
