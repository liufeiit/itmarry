package org.anddev.andengine.engine.camera.hud;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.scene.CameraScene;
import org.anddev.andengine.entity.scene.Scene;

/**
 * While you can add a {@link HUD} to {@link Scene}, you should not do so.
 * {@link HUD}s are meant to be added to {@link Camera}s via {@link Camera#setHUD(HUD)}.
 * 
 * @author Nicolas Gramlich
 * @since 14:13:13 - 01.04.2010
 */
public class HUD extends CameraScene {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public HUD() {
		super();

		this.setBackgroundEnabled(false);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
