package org.anddev.amatidev.pvb;

import java.util.LinkedList;
import org.anddev.amatidev.pvb.card.Card;
import org.anddev.amatidev.pvb.card.CardBag;
import org.anddev.amatidev.pvb.card.CardMelon;
import org.anddev.amatidev.pvb.card.CardOrange;
import org.anddev.amatidev.pvb.card.CardPotato;
import org.anddev.amatidev.pvb.card.CardTomato;
import org.anddev.amatidev.pvb.hero.BugBeetle;
import org.anddev.amatidev.pvb.hero.BugCaterpillar;
import org.anddev.amatidev.pvb.hero.BugLadybug;
import org.anddev.amatidev.pvb.hero.BugSnail;
import org.anddev.amatidev.pvb.hero.Hero;
import org.anddev.amatidev.pvb.obj.Dialog;
import org.anddev.amatidev.pvb.singleton.GameData;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXProperties;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTile;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTileProperty;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.anddev.andengine.entity.layer.tiled.tmx.util.exception.TMXLoadException;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.util.MathUtils;
import org.anddev.andengine.util.SimplePreferences;
import org.loon.anddev.utils.AndPrefs;
import org.loon.anddev.utils.AndScene;
import org.loon.anddev.utils.AndEnviroment;



public class MainGame extends AndScene {

	public static int PRESHOT_GAME_LAYER = 5;
	public static int GUI2_LAYER = 10;
	
	public static int FIELDS = 36;
	public static int ENEMIES = 4;
	
	protected Card mSelect;
	protected boolean mGameOver = false;
	protected boolean mLevelFinish = false;
	
	private TMXTiledMap mWAVTMXMap;
	private TMXLayer tmxLayer;
	private TMXTile tmxTile;
	
	public MainGame() {
        //测试
		super();
		attachChild(new Entity());
		attachChild(new Entity());
		attachChild(new Entity());
		attachChild(new Entity());
		attachChild(new Entity());
		attachChild(new Entity());
	}
	
	@Override
	public void createScene() {
		final TMXLoader tmxLoader = new TMXLoader(AndEnviroment.getInstance().getContext(), AndEnviroment.getInstance().getEngine().getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, new ITMXTilePropertiesListener() {
			@Override
			public void onTMXTileWithPropertiesCreated(final TMXTiledMap pTMXTiledMap, final TMXLayer pTMXLayer, final TMXTile pTMXTile, final TMXProperties<TMXTileProperty> pTMXTileProperties) {
			}
		});
		
		try {
			this.mWAVTMXMap = tmxLoader.loadFromAsset(AndEnviroment.getInstance().getContext(), "gfx/WAV/WAVmapEx.tmx");
		} catch (TMXLoadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tmxLayer = this.mWAVTMXMap.getTMXLayers().get(0);
		getChild(BACKGROUND_LAYER).attachChild(tmxLayer);
		// sfondo e tabellone
//		Sprite back = new Sprite(0, 0, GameData.getInstance().mBackground);
		Sprite table = new Sprite(0, 0, GameData.getInstance().mTable);
//		getChild(BACKGROUND_LAYER).attachChild(back);
		getChild(BACKGROUND_LAYER).attachChild(table);
		
		Sprite seed = new Sprite(25, 14, GameData.getInstance().mSeed);
		table.attachChild(seed);
		
		GameData.getInstance().mMySeed.setParent(null);
		table.attachChild(GameData.getInstance().mMySeed);
		
		//GameData.getInstance().mMyScore.setColor(1.0f, 0.3f, 0.3f);
		GameData.getInstance().mMyScore.setParent(null);
		getChild(BACKGROUND_LAYER).attachChild(GameData.getInstance().mMyScore);
		
		//GameData.getInstance().mMyLevel.setColor(1.0f, 0.3f, 0.3f);
		GameData.getInstance().mMyLevel.setParent(null);
		getChild(BACKGROUND_LAYER).attachChild(GameData.getInstance().mMyLevel);
		
		// field position
		for (int i = 0; i < FIELDS; i++) {
			int x = i % 9;
			int y = (int)(i / 9);
			Rectangle field = new Rectangle(0, 0, 68, 74);
			field.setColor(1f, 1f, 1f);
			if (i % 2 == 0)
				field.setAlpha(0.05f);
			else
				field.setAlpha(0.08f);
			field.setPosition(42 + x * 71,  96 + y * 77);
			getChild(GAME_LAYER).attachChild(field);
			
			registerTouchArea(field);
		}
	}

	protected void initLevel() {
		// contatori per individuare se in una riga c'e' un nemico
		AndPrefs.resetAccessCount(AndEnviroment.getInstance().getContext(), "enemy");
		AndPrefs.resetAccessCount(AndEnviroment.getInstance().getContext(), "enemy_killed");
		
		AndPrefs.resetAccessCount(AndEnviroment.getInstance().getContext(), "count96.0");
		AndPrefs.resetAccessCount(AndEnviroment.getInstance().getContext(), "count173.0");
		AndPrefs.resetAccessCount(AndEnviroment.getInstance().getContext(), "count250.0");
		AndPrefs.resetAccessCount(AndEnviroment.getInstance().getContext(), "count327.0");
		AndPrefs.resetAccessCount(AndEnviroment.getInstance().getContext(), "count404.0");
		
		GameData.getInstance().mMyLevel.addScore(1);
		if (AndPrefs.getValue(AndEnviroment.getInstance().getContext(), "level", 1) < GameData.getInstance().mMyLevel.getScore() - 1)
			AndPrefs.setValue(AndEnviroment.getInstance().getContext(), "level", GameData.getInstance().mMyLevel.getScore() - 1);
		
		GameData.getInstance().mMySeed.resetScore();
		
		//GameData.getInstance().mMySeed.addScore(20);
		//GameData.getInstance().mMyLevel.addScore(14);
		
		LinkedList<Card> cards = GameData.getInstance().mCards;
		cards.clear();
		cards.add(new CardTomato());
		if (GameData.getInstance().mMyLevel.getScore() > 1)
			cards.add(new CardBag());
		if (GameData.getInstance().mMyLevel.getScore() > 4)
			cards.add(new CardPotato());
		if (GameData.getInstance().mMyLevel.getScore() > 9)
			cards.add(new CardOrange());
		if (GameData.getInstance().mMyLevel.getScore() > 14)
			cards.add(new CardMelon());
	}

	@Override
	public void startScene() {
		initLevel();
		
		// add card
		LinkedList<Card> cards = GameData.getInstance().mCards;
		int start_x = 106;
		for (int i = 0; i < cards.size(); i++) {
			Card c = cards.get(i);
			c.setPosition(start_x + i * 69, 7);
			getChild(BACKGROUND_LAYER).attachChild(c);
		}
		
		registerUpdateHandler(new TimerHandler(5f, false, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				MainGame.this.firstRushEnemy();
			}
		}));
		
		registerUpdateHandler(new TimerHandler(6f, true, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				MainGame.this.createSeed();
			}
		}));
	}

	public void checkLevelFinish() {
		int diff = (int) (GameData.getInstance().mMyLevel.getScore() / 20);
		
		if (this.mGameOver == false && this.mLevelFinish == false && 
				SimplePreferences.getAccessCount(AndEnviroment.getInstance().getContext(), "enemy_killed") >= 22 + 4 * diff) {
			
			Dialog dialog = new Dialog("Level Complete");
			getChild(GUI2_LAYER).attachChild(dialog);
			
			if (GameData.getInstance().mMyLevel.getScore() == 1)
				dialog.add(new CardBag());
			if (GameData.getInstance().mMyLevel.getScore() == 4)
				dialog.add(new CardPotato());
			if (GameData.getInstance().mMyLevel.getScore() == 9)
				dialog.add(new CardOrange());
			if (GameData.getInstance().mMyLevel.getScore() == 14)
				dialog.add(new CardMelon());
			
			clearScene();
			
			registerUpdateHandler(new TimerHandler(6, false, new ITimerCallback() {
				@Override
				public void onTimePassed(TimerHandler pTimerHandler) {
					AndEnviroment.getInstance().nextScene();
				}
			}));
			
			this.mLevelFinish = true;
		}
	}
	
	public void gameOver() {
		if (this.mGameOver == false && this.mLevelFinish == false) {
			Dialog dialog = new Dialog("Game Over");
			getChild(GUI2_LAYER).attachChild(dialog);
			
			clearScene();
			
			registerUpdateHandler(new TimerHandler(6, false, new ITimerCallback() {
				@Override
				public void onTimePassed(TimerHandler pTimerHandler) {
					AndEnviroment.getInstance().nextScene();
				}
			}));
			
			this.mGameOver = true;
			/*
			try {
				Score s = new Score(GameData.getInstance().mMyScore.getScore());
				Leaderboard l = new Leaderboard(AdEnviroment.getInstance().getContext().getString(R.string.score));
				s.submitTo(l, new Score.SubmitToCB() {
					@Override public void onSuccess(boolean newHighScore) {
						((Activity) AdEnviroment.getInstance().getContext()).setResult(Activity.RESULT_OK);
					}
					
					@Override public void onFailure(String exceptionMessage) {
						((Activity) AdEnviroment.getInstance().getContext()).setResult(Activity.RESULT_CANCELED);
					}
				});
			} catch (Exception e) {
				
			}
			
			try {
				Score s = new Score(GameData.getInstance().mMyLevel.getScore());
				Leaderboard l = new Leaderboard(AdEnviroment.getInstance().getContext().getString(R.string.level));
				s.submitTo(l, new Score.SubmitToCB() {
					@Override public void onSuccess(boolean newHighScore) {
						((Activity) AdEnviroment.getInstance().getContext()).setResult(Activity.RESULT_OK);
					}
					
					@Override public void onFailure(String exceptionMessage) {
						((Activity) AdEnviroment.getInstance().getContext()).setResult(Activity.RESULT_CANCELED);
					}
				});
			} catch (Exception e) {
				
			}*/
		}
	}

	public void clearScene() {
		AndEnviroment.getInstance().getEngine().clearUpdateHandlers();
        clearUpdateHandlers();
        for (int i = 0; i < getChild(GAME_LAYER).getChildCount(); i++) {
        	IEntity elem = getChild(GAME_LAYER).getChild(i);
        	if (elem.getChildCount() > 0 && elem.getFirstChild() instanceof Hero)
        		elem.getFirstChild().clearUpdateHandlers();
        	if (elem instanceof Hero) {
        		elem.clearUpdateHandlers();
        		((Hero)elem).setmCollide(false);
        		elem.clearEntityModifiers();
        	}
        }
	}

	@Override
	public void endScene() {
		if (this.mGameOver)
			AndEnviroment.getInstance().setScene(new MainMenu());
		else
			AndEnviroment.getInstance().setScene(new MainGame());
	}

	@Override
	public void manageAreaTouch(ITouchArea pTouchArea) {
		if (pTouchArea instanceof Card) {
			GameData.getInstance().mSoundCard.play();
			this.mSelect = ((Card) pTouchArea).makeSelect();
		} else {
			IEntity field = (IEntity) pTouchArea;
			if (field.getChildCount() == 1 && !(field.getFirstChild() instanceof Hero)) {
				GameData.getInstance().mSoundSeed.play();
				GameData.getInstance().mMySeed.addScore(1);
				field.getFirstChild().detachSelf();
			} else {
				if (this.mSelect != null && this.mSelect.isReady() && field.getChildCount() == 0) {
					if (GameData.getInstance().mMySeed.getScore() >= this.mSelect.getPrice()) {
						GameData.getInstance().mMySeed.addScore(-this.mSelect.getPrice());
						this.mSelect.startRecharge();
//						field.attachChild(this.mSelect.getPlant());
						getChild(GAME_LAYER).attachChild(this.mSelect.getPlant());
					}
				}
			}
		}
	}

	private void addMonster(final int pEnemyIndex, final int pY) {
		Hero bug = null;
		switch (pEnemyIndex) {
		case 0:
			bug = new BugBeetle();
			break;
		case 1:
			bug = new BugLadybug();
			break;
		case 2:
			bug = new BugCaterpillar();
			break;
		case 3:
			bug = new BugSnail();
			break;
		default:
			bug = new BugBeetle();
		}
		
		if (bug != null)
			getChild(GAME_LAYER).attachChild(bug);
	}
	
	
	private void firstRushEnemy() {
		// tipi di nemici
		int ee = (int) (GameData.getInstance().mMyLevel.getScore() / 5);
		if (ee >= ENEMIES)
			ee = ENEMIES - 1;
		
		int enemyIndex = MathUtils.random(0, ee);
		int y = 96 + MathUtils.random(0, FIELDS / 9 - 1) * 77;
		addMonster(enemyIndex, y);
		
		registerUpdateHandler(new TimerHandler(12f, false, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				MainGame.this.secondRushEnemy(3);
			}
		}));
	}

	private void secondRushEnemy(final int pNum) {
		int diff = (int) (GameData.getInstance().mMyLevel.getScore() / 20);
		
		// tipi di nemici
		int ee = (int) (GameData.getInstance().mMyLevel.getScore() / 5);
		if (ee >= ENEMIES)
			ee = ENEMIES - 1;
		//ee = 3; 
		
		int n = pNum + diff;
		for (int i = 0; i < n; i++) {
			int delay = MathUtils.random(3, 15);
			final int enemyIndex = MathUtils.random(0, ee);
			final int y = 96 + MathUtils.random(0, FIELDS / 9 - 1) * 77;
			
			registerUpdateHandler(new TimerHandler(delay, false, new ITimerCallback() {
				@Override
				public void onTimePassed(TimerHandler pTimerHandler) {
					MainGame.this.addMonster(enemyIndex, y);
				}
			}));
		}
		
		if (SimplePreferences.getAccessCount(AndEnviroment.getInstance().getContext(), "enemy") < 16 - diff + 4 * diff) {
			registerUpdateHandler(new TimerHandler(20f, false, new ITimerCallback() {
				@Override
				public void onTimePassed(TimerHandler pTimerHandler) {
					MainGame.this.secondRushEnemy(6);
				}
			}));
		}
	}
	
	private void createSeed() {
		int i = MathUtils.random(0, 8) * MathUtils.random(1, FIELDS/ 9);
		final Sprite e = new Sprite(12, 25, GameData.getInstance().mSeed);
		IEntity field = getChild(GAME_LAYER).getChild(i);
		if (field.getChildCount() == 0)
			field.attachChild(e);
		
		registerUpdateHandler(new TimerHandler(4f, true, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				e.detachSelf();
			}
		}));
	}

	@Override
	public MenuScene createMenu() {
		if (this.mGameOver)
			return null;
		else
			return new GameMenu();
	}

	@Override
	public void manageSceneTouch(TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		
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
}
