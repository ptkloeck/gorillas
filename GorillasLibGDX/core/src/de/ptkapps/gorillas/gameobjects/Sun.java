package de.ptkapps.gorillas.gameobjects;

import com.badlogic.gdx.math.Vector2;

import de.ptkapps.gorillas.assets.Assets;

public class Sun extends GameObject {

	public static final int SUN_STATE_SMILING = 0;
	public static final int SUN_STATE_ASTONISHED = 1;
	
	/**
	 * Constructor
	 * 
	 */
	public Sun() {

		super(new Vector2(0, 0), new Vector2(Assets.sunSmiling.getRegionWidth(), Assets.sunSmiling.getRegionHeight()));
		state = SUN_STATE_SMILING;
	}
	
	public void beAstonished() {
		state = SUN_STATE_ASTONISHED;
		stateTime = 0;
	}
	
	public void smile() {
		state = SUN_STATE_SMILING;
	}
}
