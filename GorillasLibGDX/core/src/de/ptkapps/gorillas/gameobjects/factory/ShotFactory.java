package de.ptkapps.gorillas.gameobjects.factory;

import com.badlogic.gdx.math.Vector2;

import de.ptkapps.gorillas.assets.Assets;
import de.ptkapps.gorillas.gameobjects.Shot;
import de.ptkapps.gorillas.gameobjects.Shot.Direction;

public class ShotFactory {

	public static Shot createShot(Vector2 startPosition, Direction direction,
			float angle, float speed, float wind) {

		float width = Assets.banana.getRegionWidth();
		float height = Assets.banana.getRegionHeight();
		return new Shot(startPosition, new Vector2(width, height), angle,
				speed, 1.0f, direction, wind);
	}
}
