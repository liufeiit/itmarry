package org.loon.anddev.utils;

import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.util.HorizontalAlign;

public class AndTextScoring extends ChangeableText {
	
	private ChangeableText mLabel;
	private HorizontalAlign mHorizontalAlign;
	private int mInitialValue;
	private float mStartX;
	private float mStartY;
	
	public AndTextScoring(final float pX, final float pY, final Font pFont, final HorizontalAlign pHorizontalAlign, final int pInitialValue) {
		this(pX, pY, pFont, pHorizontalAlign, pInitialValue, null);
	}
	
	public AndTextScoring(final float pX, final float pY, final Font pFont, final HorizontalAlign pHorizontalAlign, final int pInitialValue, final String pLabel) {
		super(pX, pY, pFont, String.valueOf(pInitialValue), HorizontalAlign.RIGHT, 10);
		
		if (pLabel != null) {
			this.mLabel = new ChangeableText(0, 0, pFont, pLabel, 10);
			this.attachChild(this.mLabel);
		}
		
		this.mHorizontalAlign = pHorizontalAlign;
		this.mInitialValue = pInitialValue;
		this.mStartX = pX;
		this.mStartY = pY;
		
		reAlign();
	}
	
	private void reAlign() {
		if (this.mHorizontalAlign == HorizontalAlign.LEFT)
			setPosition(this.mStartX, this.mStartY - getHeightScaled() / 2);
		else if (this.mHorizontalAlign == HorizontalAlign.RIGHT)
			setPosition(this.mStartX - getWidthScaled(), this.mStartY - getHeightScaled() / 2);
		else
			setPosition(this.mStartX - getWidthScaled() / 2, this.mStartY - getHeightScaled() / 2);
		
		if (this.mLabel != null)
			this.mLabel.setPosition(-80, 0);
	}
	
	public int getScore() {
		return Integer.parseInt(getText());
	}
	
	public void addScore(int pScore) {
		setText(String.valueOf(getScore() + pScore));
		reAlign();
	}
	
	public void resetScore() {
		setText(String.valueOf(this.mInitialValue));
		reAlign();
	}

}
