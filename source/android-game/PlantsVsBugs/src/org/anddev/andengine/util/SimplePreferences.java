package org.anddev.andengine.util;

import org.anddev.andengine.util.constants.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * @author Nicolas Gramlich
 * @since 18:55:12 - 02.08.2010
 */
public class SimplePreferences implements Constants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static SharedPreferences INSTANCE;
	private static Editor EDITORINSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	public static SharedPreferences getInstance(final Context pContext) {
		if(SimplePreferences.INSTANCE == null) {
			SimplePreferences.INSTANCE = PreferenceManager.getDefaultSharedPreferences(pContext);
		}
		return SimplePreferences.INSTANCE;
	}

	public static Editor getEditorInstance(final Context pContext) {
		if(SimplePreferences.EDITORINSTANCE == null) {
			SimplePreferences.EDITORINSTANCE = SimplePreferences.getInstance(pContext).edit();
		}
		return SimplePreferences.EDITORINSTANCE;
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

	public static int incrementAccessCount(final Context pContext, final String pKey) {
		return SimplePreferences.incrementAccessCount(pContext, pKey, 1);
	}

	public static int incrementAccessCount(final Context pContext, final String pKey, final int pIncrement) {
		final SharedPreferences prefs = SimplePreferences.getInstance(pContext);
		final int accessCount = prefs.getInt(pKey, 0);

		final int newAccessCount = accessCount + pIncrement;
		prefs.edit().putInt(pKey, newAccessCount).commit();

		return newAccessCount;
	}

	public static void resetAccessCount(final Context pContext, final String pKey) {
		setValue(pContext, pKey, 0);
	}

	public static int getAccessCount(final Context pContext, final String pKey) {
		return SimplePreferences.getInstance(pContext).getInt(pKey, 0);
	}

	public static int getValue(final Context pContext, final String pKey, final int pValue) {
		return SimplePreferences.getInstance(pContext).getInt(pKey, pValue);
	}

	public static boolean getValue(final Context pContext, final String pKey, final boolean pValue) {
		return SimplePreferences.getInstance(pContext).getBoolean(pKey, pValue);
	}

	public static void setValue(final Context pContext, final String pKey, final int pValue) {
		SimplePreferences.getEditorInstance(pContext).putInt(pKey, pValue).commit();
	}

	public static void setValue(final Context pContext, final String pKey, final boolean pValue) {
		SimplePreferences.getEditorInstance(pContext).putBoolean(pKey, pValue).commit();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
