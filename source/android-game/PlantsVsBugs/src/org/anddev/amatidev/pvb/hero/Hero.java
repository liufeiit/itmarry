package org.anddev.amatidev.pvb.hero;

import org.anddev.amatidev.pvb.MainGame;
import org.anddev.amatidev.pvb.obj.Blood;
import org.anddev.amatidev.pvb.obj.Camp;
import org.anddev.amatidev.pvb.obj.HeroState;
import org.anddev.amatidev.pvb.shot.BasicShot;
import org.anddev.amatidev.pvb.singleton.GameData;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.AlphaModifier;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.PathModifier;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.anddev.andengine.entity.modifier.PathModifier.Path;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.util.modifier.IModifier;
import org.loon.anddev.utils.AndVibration;
import org.loon.anddev.utils.AndEnviroment;

import android.util.Log;

public abstract class Hero extends Entity {
	protected Blood mBlood ;
	protected float mShotHeight = 28f;
	protected float mShotSpeed = 250f;
	protected boolean mShotDouble = false;
	protected TextureRegion plantTexture;
	protected HeroState heroState;
	//是否碰撞
	private boolean mCollide = true;
	//移动线路
	private Path mPath;
	//阵营
	private Camp camp;
	
	public Hero(final TextureRegion pTexture) {
		heroState = new HeroState();
		Sprite shadow = new Sprite(2, 55, GameData.getInstance().mPlantShadow);
		shadow.setAlpha(0.4f);
		if (pTexture != null){
			shadow.attachChild(new Sprite(0, -68, pTexture));
			plantTexture = pTexture;
		}
		attachChild(shadow);
	}
	
	/**
	 * 注册射击事件
	 */
	private void beginToShort(){
		registerUpdateHandler(new TimerHandler(this.getHeroState().getShotDelay(), true, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				Log.i("Game", "shot");
				if (Hero.this.canShot()) {
					Hero.this.shot(true);
					if (Hero.this.mShotDouble) {
						registerUpdateHandler(new TimerHandler(0.3f, false, new ITimerCallback() {
							@Override
							public void onTimePassed(TimerHandler pTimerHandler) {
								Log.i("Game", "2shot");
								Hero.this.shot(false);
							}
						}));
					}
				}
			}
		}));
	}
	
	public void onAttached() {
		int intBoold = heroState.getMaxBlood();
		heroState.setBlood(mBlood);
		this.mBlood = new Blood(10, -10, plantTexture.getWidth() - 20, 10,intBoold);
		heroState.setBlood(mBlood);
		attachChild(this.mBlood);
		GameData.getInstance().mSoundPlanting.play();
		beginToShort();
		registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				AndEnviroment.getInstance().getEngine().runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						Hero.this.checkCollisionShot(); // controlla danni da colpi
//						Hero.this.checkCollisionPlant(); // crea danni a piange se collide e se vince riparte
					}
				});
			}
			
			@Override
			public void reset() {
				
			}
		});
		reLive();
	}
	
	/**
	 * 满血复活并开始按path移动
	 */
	public void reLive() {
		this.mCollide = true;
		this.clearEntityModifiers();
		float duration = 680 / this.getHeroState().getSpeed();
		if(this.getCamp()==Camp.TIANZAI)
			mPath = new Path(2).to(getParent().getX() + 45, getParent().getY() + this.mShotHeight).to(680, getParent().getY() + this.mShotHeight);
		else
			mPath = new Path(2).to(680, getParent().getY() + this.mShotHeight).to(0, getParent().getY() + this.mShotHeight);
		registerEntityModifier(new PathModifier(duration, this.mPath, new IEntityModifierListener() {
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				AndEnviroment.getInstance().getEngine().runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						((MainGame) AndEnviroment.getInstance().getScene()).gameOver();
					}
				});
			}

			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			
			}
		}));
	}

	protected boolean canShot() {
			return true;
	}

	protected void colorDamage(int demageVal) {
		getFirstChild().getFirstChild().setColor(3f, 3f, 3f);
		AndVibration.duration(100);
		registerUpdateHandler(new TimerHandler(0.1f, false, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				Hero.this.getFirstChild().getFirstChild().setColor(1f, 1f, 1f);
			}
		}));
		int realDemage = this.heroState.physicDemage(demageVal);
		final Text demage;
		if(realDemage<=0){
			demage = new Text(10, 50, GameData.getInstance().mFontPushDemage, "MISS");
		}else{
			demage = new Text(10, 50, GameData.getInstance().mFontDemage, realDemage+"");
		}
		
		Path path = new Path(2).to(10, 50).to(10, 0);
		demage.registerEntityModifier(new PathModifier(0.5f, path, new IEntityModifierListener() {
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				AndEnviroment.getInstance().safeDetachEntity(pItem);
			}

			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				
			}
		}));
		attachChild(demage);
		
	}
	
	public void pushDamage(final Hero hero) {
		colorDamage(hero.getHeroState().getDamage());
		if (heroState.isDead()) {
			clearUpdateHandlers();
			hero.reLive();
			getFirstChild().getFirstChild().registerEntityModifier(
					new LoopEntityModifier(
							new IEntityModifierListener() {
							@Override
								public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
									
								}
								
								@Override
								public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
									Hero.this.detachSelf();
								}
							}, 
							3, 
							null,
							new SequenceEntityModifier(
									new AlphaModifier(0.2f, 1f, 0.5f),
									new AlphaModifier(0.2f, 0.5f, 1f)
							)
					));
		}
	}
	

	protected void shot(boolean pAnim) {
		// plant animation
		if (pAnim) {
			getFirstChild().registerEntityModifier(
					new SequenceEntityModifier(
							new ScaleModifier(0.3f, 1f, 1.1f),
							new ScaleModifier(0.3f, 1.1f, 1f)
					)
			);
		}
		
		// creazione shot
		
		BasicShot shot = new BasicShot(getX() + 45, getY() + this.mShotHeight, GameData.getInstance().mShot);
		shot.setOwner(this);
		Sprite shadow = new Sprite(0, 21, GameData.getInstance().mShotShadow);
		shadow.setAlpha(0.4f);
		shot.attachChild(shadow);
		Path path ;
		float duration ;
		if(getCamp()==Camp.TIANZAI){
			duration = (680 - getX() -  45) / this.mShotSpeed;
			path = new Path(2).to(getX() + 45, getY() + this.mShotHeight).to(680, getY() + this.mShotHeight);
		}else{
			duration = (getX()-45) / this.mShotSpeed;
			path = new Path(2).to(getX() - 45, getY() + this.mShotHeight).to(0, getY() + this.mShotHeight);
		}
		
		shot.registerEntityModifier(new PathModifier(duration, path, new IEntityModifierListener() {
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				AndEnviroment.getInstance().safeDetachEntity(pItem);
			}

			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				
			}
		}));
		
		int y = (int) getParent().getY() / 77;
		AndEnviroment.getInstance().getScene().getChild(MainGame.PRESHOT_GAME_LAYER + y).attachChild(shot);
	}
	
	public HeroState getHeroState(){
		return heroState;
	}
	
	protected IShape getBody() {
		return ((IShape) getFirstChild().getFirstChild());
	}
	
	public void stop() {
		this.mCollide = false;
		this.clearEntityModifiers();
	}
	
	protected void checkCollisionShot() {
		int y = (int) getY() / 77;
		
		// chiamare solo da thread safe
		IEntity shotLayer = AndEnviroment.getInstance().getScene().getChild(MainGame.PRESHOT_GAME_LAYER + y);
		for (int i = 0; i < shotLayer.getChildCount(); i++) {
			IShape body_shot = (IShape) shotLayer.getChild(i);
			if (this.getHeroState().isAlive() && getBody().collidesWith(body_shot)) {
				if(body_shot instanceof BasicShot){
					if(this.getCamp()!=((BasicShot)body_shot).getOwner().getCamp()){
						GameData.getInstance().mSoundPop.play();
						pushDamage(((BasicShot)body_shot).getOwner());
						body_shot.detachSelf();
						break;
					}
				}
			}
		}
	}
	
	/**
	 *  阵营相同的则为队友
	 * @param unknow
	 * @return
	 */
	private boolean isFrend(Hero unknow){
		if(this.getCamp()==unknow.getCamp()){
			return true;
		}else{
			return false;
		}
	}
	
	protected void checkCollisionPlant() {
		if (getX() < 678) {
			int x = (int) (getX() - 25) / 71;
			int y = (int) getY() / 77 - 1;
			int i = (int) (y * 9 + x);
			
			IEntity field = AndEnviroment.getInstance().getScene().getChild(MainGame.GAME_LAYER).getChild(i);
			IEntity unknow = field.getFirstChild();
			if (this.mCollide && getY() == field.getY() && 
					this.getHeroState().isAlive() && 
					field.getChildCount() == 1 && 
					unknow instanceof Hero&&
					!isFrend((Hero)unknow) && 
					unknow.getFirstChild().getChildCount() > 0) {
				final Hero enemy = (Hero) field.getFirstChild();
				if (enemy.getHeroState().isAlive()) {
					stop();
					if (enemy instanceof PlantMelon) {
						pushDamage(enemy);
					} else {
						registerUpdateHandler(new TimerHandler(Hero.this.getHeroState().getShotDelay(), false, new ITimerCallback() {
							@Override
							public void onTimePassed(TimerHandler pTimerHandler) {
								enemy.pushDamage(Hero.this);
								Hero.this.mCollide = true;					
								Log.w("Game", "collision");
							}
						}));
					}
				} else {
					//restart();
					Log.w("Game", "plant 0 life");
				}
			}
		}
	}

	public boolean ismCollide() {
		return mCollide;
	}

	public void setmCollide(boolean mCollide) {
		this.mCollide = mCollide;
	}

	public Camp getCamp() {
		return camp;
	}

	public void setCamp(Camp camp) {
		this.camp = camp;
	}
	
}

