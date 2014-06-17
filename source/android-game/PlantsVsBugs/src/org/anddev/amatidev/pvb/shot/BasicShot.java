package org.anddev.amatidev.pvb.shot;

import org.anddev.amatidev.pvb.hero.Hero;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class BasicShot extends Sprite{
	private Hero owner;
	
	public BasicShot(final float pX, final float pY, final TextureRegion pTextureRegion) {
		super(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion);
	}

	public Hero getOwner() {
		return owner;
	}

	public void setOwner(Hero owner) {
		this.owner = owner;
	}
	
	
}
