package org.loon.anddev.utils;

import java.io.IOException;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public abstract class AndResourceLoader {
	
	public static Font getFont(final String pName, final int pSize, final int pWidth, final int pFillColor, final int pBorderColor) {
		return AndResourceLoader.getFont(256, 256, pName, pSize, pWidth, pFillColor, pBorderColor);
	}
	
	public static Font getFont(final int pTextureWidth, final int pTextureHeight, final String pName, final int pSize, final int pBorderSize, final int pFillColor, final int pBorderColor) {
		Texture tex = new Texture(pTextureWidth, pTextureHeight, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		Font font = FontFactory.createStrokeFromAsset(tex, AndEnviroment.getInstance().getContext(), "font/" + pName + ".ttf", pSize, true, pFillColor, pBorderSize, pBorderColor, false);
		AndEnviroment.getInstance().getEngine().getTextureManager().loadTexture(tex);
		AndEnviroment.getInstance().getEngine().getFontManager().loadFont(font);
		return font;
	}
	
	public static Font getFont(final String pName, final int pSize, final int pFillColor) {
		return AndResourceLoader.getFont(256, 256, pName, pSize, pFillColor);
	}
	
	public static Font getFont(final int pTextureWidth, final int pTextureHeight, final String pName, final int pSize, final int pFillColor) {
		Texture tex = new Texture(pTextureWidth, pTextureHeight, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		Font font = FontFactory.createFromAsset(tex, AndEnviroment.getInstance().getContext(), "font/" + pName + ".ttf", pSize, true, pFillColor);
		AndEnviroment.getInstance().getEngine().getTextureManager().loadTexture(tex);
		AndEnviroment.getInstance().getEngine().getFontManager().loadFont(font);
		return font;
	}
	
	public static TextureRegion getTexture(final int pTextureWidth, final int pTextureHeight, final String pName) {
		Texture tex = new Texture(pTextureWidth, pTextureHeight, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegion texReg = TextureRegionFactory.createFromAsset(tex, AndEnviroment.getInstance().getContext(), "gfx/" + pName + ".png", 0, 0);
		AndEnviroment.getInstance().getEngine().getTextureManager().loadTexture(tex);
		return texReg;
	}
	
	public static TiledTextureRegion getTexture(final int pTextureWidth, final int pTextureHeight, final String pName, final int pColumns, final int pRows) {
		Texture tex = new Texture(pTextureWidth, pTextureHeight, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TiledTextureRegion texReg = TextureRegionFactory.createTiledFromAsset(tex, AndEnviroment.getInstance().getContext(), "gfx/" + pName + ".png", 0, 0, pColumns, pRows);
		AndEnviroment.getInstance().getEngine().getTextureManager().loadTexture(tex);
		return texReg;
	}
	
	public static AndSound getSound(final String pName) {
		AndSound sound = new AndSound();
		try {
			Sound s = SoundFactory.createSoundFromAsset(AndEnviroment.getInstance().getEngine().getSoundManager(), AndEnviroment.getInstance().getContext(), "mfx/" + pName + ".wav");
			sound.setSound(s);
		} catch (final IOException e) {
			
		}
		return sound;
	}
	
}
