package org.anddev.amatidev.pvb.hero;

import org.anddev.amatidev.pvb.MainGame;
import org.anddev.amatidev.pvb.obj.Camp;
import org.anddev.amatidev.pvb.singleton.GameData;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.AlphaModifier;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.PathModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.anddev.andengine.entity.modifier.PathModifier.Path;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.util.modifier.IModifier;
import org.loon.anddev.utils.AndVibration;
import org.loon.anddev.utils.AndEnviroment;

public class PlantMelon extends Hero {
	
	public PlantMelon() {
		super(null);
		this.setCamp(Camp.TIANZAI);
	}
	
	public void onAttached() {
		final Sprite plant = new Sprite(getParent().getX() + 2, getParent().getY() - 300, GameData.getInstance().mPlantMelon);
		AndEnviroment.getInstance().getScene().getChild(MainGame.PRESHOT_GAME_LAYER).attachChild(plant);
		
		Path path = new Path(2).to(getParent().getX() + 2, getParent().getY() - 300).to(getParent().getX() + 2,  getParent().getY() - 13);
		plant.registerEntityModifier(new PathModifier(1.5f, path, new IEntityModifierListener() {
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				AndEnviroment.getInstance().getEngine().runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						PlantMelon.this.getFirstChild().attachChild(new Entity());
						AndVibration.duration(400);
						plant.registerEntityModifier(
								new LoopEntityModifier(
										new IEntityModifierListener() {
											@Override
											public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
//												PlantMelon.this.mLife = 0;
											}

											@Override
											public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
												plant.detachSelf();
												PlantMelon.this.detachSelf();
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
				});
			}

			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			
			}
		}));
	}
	
}
