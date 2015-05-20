package de.ptkapps.gorillas.gameobjects.factory;

import com.badlogic.gdx.math.Vector2;

import de.ptkapps.gorillas.gameobjects.Sun;

public class SunFactory {

	public static Sun createSun(float x, float y) {

		Sun sun = new Sun();
		sun.setPosition(new Vector2(x, y));
		
		return sun;
	}
}
