package org.anddev.amatidev.pvb.hero;

import org.anddev.amatidev.pvb.obj.Camp;
import org.anddev.amatidev.pvb.singleton.GameData;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.MoveYModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.sprite.Sprite;

public class BugCaterpillar extends Hero {
	
	public BugCaterpillar() {
		super(GameData.getInstance().mBugCaterpillar2);
		this.setCamp(Camp.JINWEI);
		getFirstChild().getFirstChild().attachChild(new Sprite(-10, 0, GameData.getInstance().mBugCaterpillar2));
		getFirstChild().getFirstChild().attachChild(new Sprite(-20, 0, GameData.getInstance().mBugCaterpillar2));
		getFirstChild().getFirstChild().attachChild(new Sprite(0, 0, GameData.getInstance().mBugCaterpillar));
		
		move(-3, 2, getFirstChild().getFirstChild().getChild(0));
		move(2, -3, getFirstChild().getFirstChild().getChild(1));
	}
	
	protected IShape getBody() {
		return ((IShape) getFirstChild().getFirstChild().getChild(2));
	}

	protected void bodyDetachSelf() {
		getFirstChild().getFirstChild().detachSelf();
	}

	protected void colorDamage() {
		getFirstChild().getFirstChild().setColor(3f, 3f, 3f);
		getFirstChild().getFirstChild().getChild(0).setColor(3f, 3f, 3f);
		getFirstChild().getFirstChild().getChild(1).setColor(3f, 3f, 3f);
		getBody().setColor(3f, 3f, 3f);
		registerUpdateHandler(new TimerHandler(0.1f, false, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				BugCaterpillar.this.getFirstChild().getChild(0).setColor(1f, 1f, 1f);
				BugCaterpillar.this.getFirstChild().getFirstChild().getChild(0).setColor(1f, 1f, 1f);
				BugCaterpillar.this.getFirstChild().getFirstChild().getChild(1).setColor(1f, 1f, 1f);
				BugCaterpillar.this.getBody().setColor(1f, 1f, 1f);
			}
		}));
	}
	
	private void move(final int pStartY, final int pEndY, final IEntity pElem) {
		pElem.registerEntityModifier(
				new LoopEntityModifier(
						null, 
						-1, 
						null,
						new SequenceEntityModifier(
								new MoveYModifier(2f, pStartY, pEndY),
								new MoveYModifier(2f, pEndY, pStartY)
						)
				));
	}
	
}
