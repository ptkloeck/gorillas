package de.ptkapps.gorillas.ai;

import java.util.Random;

import de.ptkapps.gorillas.utils.ShotUtils;
import de.ptkapps.gorillas.world.World;

public class AI {

	public enum Difficulty {
		MODERATE, DIFFICULT, DEATHLY
	}

	private Difficulty difficulty;

	private World world;

	private Random random;

	private int levelThrowCount;

	private int perfectAngle;
	private int perfectVelocity;

	private int angle;
	private int velocity;

	public int getAngle() {
		return angle;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public AI(World world, Difficulty difficulty) {

		this.difficulty = difficulty;
		this.world = world;
		random = new Random();
	}

	public void determineShot() {

		if (levelThrowCount == 0) {
			calculatePerfectShot();
		}

		levelThrowCount++;

		switch (difficulty) {

		case MODERATE:
			determineShotModerate();
			break;

		case DIFFICULT:
			determineShotDifficult();
			break;

		case DEATHLY:
			determineShotDeathly();
			break;
		}
	}

	private void determineShotModerate() {

		switch (levelThrowCount) {

		case 1:

			angle = 60;

			if (random.nextInt(2) == 0) {
				// first estimation too short
				velocity = perfectVelocity - (random.nextInt(5) + 10);

			} else {
				// first estimation too far
				velocity = perfectVelocity + (random.nextInt(5) + 10);
			}
			break;

		case 2:

			angle = 60;

			if (random.nextInt(2) == 0) {
				// second estimation too short
				velocity = perfectVelocity - (random.nextInt(5) + 5);

			} else {
				// second estimation too far
				velocity = perfectVelocity + (random.nextInt(5) + 5);
			}
			break;

		case 3:

			angle = 60;

			if (random.nextInt(2) == 0) {
				// third estimation too short
				velocity = perfectVelocity - (random.nextInt(5) + 5);

			} else {
				// third estimation too far
				velocity = perfectVelocity + (random.nextInt(5) + 5);
			}
			break;

		case 4:

			angle = perfectAngle;
			velocity = perfectVelocity;
			break;

		default:

			angle = perfectAngle;
			velocity = perfectVelocity;
			break;
		}
	}

	private void determineShotDifficult() {

		switch (levelThrowCount) {

		case 1:

			angle = 60;

			if (random.nextInt(2) == 0) {
				// first estimation too short
				velocity = perfectVelocity - (random.nextInt(5) + 10);

			} else {
				// first estimation too far
				velocity = perfectVelocity + (random.nextInt(5) + 10);
			}
			break;

		case 2:

			if (random.nextInt(2) == 0) {
				// second estimation slightly wrong
				angle = 60;
				velocity = perfectVelocity - 5;
			} else {
				// second estimation perfect
				angle = perfectAngle;
				velocity = perfectVelocity;
			}

			break;

		case 3:

			angle = perfectAngle;
			velocity = perfectVelocity;
			break;

		default:

			angle = perfectAngle;
			velocity = perfectVelocity;
			break;
		}
	}

	private void determineShotDeathly() {

		angle = perfectAngle;
		velocity = perfectVelocity;
	}

	private void calculatePerfectShot() {

		float x0 = world.gorilla2.getShotStartPosition().x;
		float y0 = world.gorilla2.getShotStartPosition().y;

		float x = world.gorilla1.getPosition().x;
		float y = world.gorilla1.getPosition().y;

		float wind = world.currentMap.getWind();

		perfectAngle = 60;
		perfectVelocity = (int) ShotUtils.determineNeededVelocity(x0, y0, x, y,
				wind, perfectAngle);

		do {
			perfectAngle -= 5;
			perfectVelocity = (int) ShotUtils.determineNeededVelocity(x0, y0, x, y,
					wind, perfectAngle);
		} while (perfectVelocity > 200 & perfectAngle >= 0);
	}

	public void nextLevel() {

		levelThrowCount = 0;
	}
}
