package org.anddev.amatidev.pvb.singleton;

import java.util.LinkedList;

import org.anddev.amatidev.pvb.card.Card;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.HorizontalAlign;
import org.loon.anddev.utils.AndResourceLoader;
import org.loon.anddev.utils.AndSound;
import org.loon.anddev.utils.AndTextScoring;

import android.graphics.Color;

public class GameData {
	
    private static GameData mInstance = null;
    
    // generici
    public LinkedList<Card> mCards;
    
    public AndTextScoring mMySeed;
    public AndTextScoring mMyScore;
    public AndTextScoring mMyLevel;
    
    public TextureRegion mDialog;
    public TextureRegion mMainTitle;
    public TextureRegion mMainBackground;
    public TextureRegion mBackground;
    
    public TextureRegion mArrow;
    public TextureRegion mTable;
    public TextureRegion mSeed;
    public TextureRegion mSeed2;
    public TextureRegion mShot;
    public TextureRegion mShotShadow;
    public TextureRegion mPlantShadow;
    public TextureRegion mCardSelected;
    
    // cards
    public TextureRegion mCard;
    public TextureRegion mCardTomato;
	public TextureRegion mCardPotato;
	public TextureRegion mCardOrange;
	public TextureRegion mCardMelon;
	public TextureRegion mCardBag;
	
	// plants
	public TextureRegion mPlantTomato;
	public TextureRegion mPlantPotato;
	public TextureRegion mPlantOrange;
	public TextureRegion mPlantMelon;
	public TextureRegion mPlantBag;
	
	public TextureRegion mBugRip;
	
	// bugs
	public TextureRegion mBugBeetle;
	public TextureRegion mBugLadybug;
	public TextureRegion mBugCaterpillar;
	public TextureRegion mBugCaterpillar2;
	public TextureRegion mBugSnail;

	public TextureRegion mHelm1;
	
	// fonts
	public Font mFontSeed;
	public Font mFontCard;
	public Font mFontScore;
	public Font mFontEvent;
	public Font mFontTutorial;
	public Font mFontMainMenu;
	public Font mFontGameMenu;
	public Font mFontDemage;
	public Font mFontPushDemage;
	

	public TiledTextureRegion mBugLeg;
	
	public AndSound mSoundPlanting;
	public AndSound mSoundPop;
	public AndSound mSoundCard;
	public AndSound mSoundSeed;
	public AndSound mSoundMenu;
	
	private GameData() {
		
	}
	
	public static synchronized GameData getInstance() {
		if (mInstance == null) 
			mInstance = new GameData();
		return mInstance;
	}
	
	public void initData() {
		this.mSoundPlanting = AndResourceLoader.getSound("planting");
		this.mSoundPop = AndResourceLoader.getSound("pop");
		this.mSoundCard = AndResourceLoader.getSound("card");
		this.mSoundSeed = AndResourceLoader.getSound("seed");
		this.mSoundMenu = AndResourceLoader.getSound("ok");
		
		this.mFontSeed = AndResourceLoader.getFont("akaDylan Plain", 20, 2, Color.WHITE, Color.BLACK);
		this.mFontCard = AndResourceLoader.getFont("akaDylan Plain", 14, 1, Color.WHITE, Color.BLACK);
		this.mFontScore = AndResourceLoader.getFont("akaDylan Plain", 22, 2, Color.WHITE, Color.BLACK);
		
		this.mDialog = AndResourceLoader.getTexture(512, 256, "dialog");
		this.mFontEvent = AndResourceLoader.getFont(512, 512, "akaDylan Plain", 38, 3, Color.WHITE, Color.BLACK);
		this.mFontTutorial = AndResourceLoader.getFont(512, 512, "akaDylan Plain", 56, 3, Color.WHITE, Color.BLACK);
		this.mFontMainMenu = AndResourceLoader.getFont(512, 512, "akaDylan Plain", 40, 2, Color.WHITE, Color.BLACK);
		this.mFontGameMenu = AndResourceLoader.getFont(512, 512, "akaDylan Plain", 48, 3, Color.WHITE, Color.BLACK);
		this.mFontDemage = AndResourceLoader.getFont(512, 512, "akaDylan Plain", 20, 2, Color.RED, Color.BLACK);
		this.mFontPushDemage = AndResourceLoader.getFont(512, 512, "akaDylan Plain", 20, 2, Color.WHITE, Color.BLACK);

		
//		this.mBackground = AndResourceLoader.getTexture(1024, 512, "back");
		this.mBackground = AndResourceLoader.getTexture(1024, 512, "dotaback");
		this.mMainBackground = AndResourceLoader.getTexture(1024, 512, "main2");
		this.mMainTitle = AndResourceLoader.getTexture(1024, 256, "title");
		
		this.mTable  = AndResourceLoader.getTexture(1024, 128, "table");
		this.mSeed = AndResourceLoader.getTexture(64, 64, "seed");
		this.mSeed2 = AndResourceLoader.getTexture(64, 64, "seed2");
		this.mShot = AndResourceLoader.getTexture(64, 64, "shot");
		this.mShotShadow = AndResourceLoader.getTexture(64, 64, "shadow2");
		
		this.mCardSelected = AndResourceLoader.getTexture(64, 128, "select");
		
		this.mCard = AndResourceLoader.getTexture(64, 128, "card");
		this.mArrow = AndResourceLoader.getTexture(64, 128, "arrow");
		
		this.mCardTomato = AndResourceLoader.getTexture(64, 64, "card_tomato");
		this.mCardPotato = AndResourceLoader.getTexture(64, 64, "card_potato");
		this.mCardOrange = AndResourceLoader.getTexture(64, 64, "card_orange");
		this.mCardMelon = AndResourceLoader.getTexture(64, 64, "card_melon");
		this.mCardBag = AndResourceLoader.getTexture(64, 64, "card_bag");
		
		this.mPlantShadow = AndResourceLoader.getTexture(64, 16, "shadow");
		
		this.mPlantTomato = AndResourceLoader.getTexture(64, 128, "tomato");
		this.mPlantPotato = AndResourceLoader.getTexture(64, 128, "potato");
		this.mPlantOrange = AndResourceLoader.getTexture(64, 128, "orange");
		this.mPlantMelon = AndResourceLoader.getTexture(64, 128, "melon");
		this.mPlantBag = AndResourceLoader.getTexture(64, 128, "bag");
		
		this.mBugRip = AndResourceLoader.getTexture(64, 128, "rip");
		
		this.mBugBeetle = AndResourceLoader.getTexture(64, 128, "beetle");
		this.mBugLadybug = AndResourceLoader.getTexture(64, 128, "ladybug");
		this.mBugCaterpillar = AndResourceLoader.getTexture(64, 128, "caterpillar");
		this.mBugCaterpillar2 = AndResourceLoader.getTexture(64, 128, "caterpillar2");
		this.mBugSnail = AndResourceLoader.getTexture(64, 128, "snail");
		
		this.mHelm1 = AndResourceLoader.getTexture(64, 128, "helm1");
		
		this.mMySeed = new AndTextScoring(48, 67, GameData.getInstance().mFontSeed, HorizontalAlign.CENTER, 6);
		this.mMyScore = new AndTextScoring(703, 30, GameData.getInstance().mFontScore, HorizontalAlign.RIGHT, 0, "Pt.");
		this.mMyLevel = new AndTextScoring(703, 70, GameData.getInstance().mFontScore, HorizontalAlign.RIGHT, 0, "Lv.");
		
		this.mBugLeg = AndResourceLoader.getTexture(64, 64, "leg", 1, 2);
		
		this.mCards = new LinkedList<Card>();
	}
	
}
