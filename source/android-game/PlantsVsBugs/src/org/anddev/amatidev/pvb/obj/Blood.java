package org.anddev.amatidev.pvb.obj;

import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.primitive.Rectangle;

public class Blood extends Entity{
	private int mLife;
	protected Rectangle mBlood ;
	protected Rectangle mLeftBlood ;
	protected float x,y,width,height;
	public Blood(float x,float y,float width,float height,int mLife){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.mLife = mLife;
	}
	
	public void onAttached() {
		this.mBlood = new Rectangle(x, y, width, height);
		this.mBlood.setColor(1f, 1f, 1f);
		this.mBlood.setScaleCenter(0, 0);
		attachChild(this.mBlood);
		this.mLeftBlood = new Rectangle(x+1, y+1, width-2, height-2);
		this.mLeftBlood.setColor(0f, 1f, 0f);
		this.mLeftBlood.setScaleCenter(0, 0);
		attachChild(this.mLeftBlood);
	}
	
	public void bloodChange(int remainLife){
		float remainWidth = (width-2)*remainLife/(float)mLife;
		this.mLeftBlood.setWidth(remainWidth);
	}
	
}
