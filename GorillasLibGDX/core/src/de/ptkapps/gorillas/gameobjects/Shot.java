package de.ptkapps.gorillas.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.utils.ShotUtils;

/**
 * This class represents a single shot entity.
 * 
 * @author Peter Kloeckner, Sebastian Fach
 * @version 1.0
 */
public class Shot extends GameObject {

	protected Vector2 startPosition;
	protected float angle;
	protected float speed;
	protected float speedX;
	protected float speedY;
	protected float rotationSpeed;
	protected float timeElapsed;
	protected Direction direction;
	protected float wind;
	protected float rotation;
	
	private boolean landInScreen;
	
	public boolean isLandInScreen() {
		return landInScreen;
	}
	
	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	/**
	 * The direction of a shot.
	 */
	public enum Direction {
		LEFT_TO_RIGHT, RIGHT_TO_LEFT
	};

	/**
	 * @param startPosition
	 *            The start position of this shot as <em>Vector2f(x, y)</em>.
	 */
	public void setStartPosition(Vector2 startPosition) {
		this.startPosition = startPosition;
	}

	/**
	 * @return The start position of this shot as <em>Vector2f(x, y)</em>.
	 */
	public Vector2 getStartPosition() {
		return startPosition;
	}

	/**
	 * @param speed
	 *            The launching speed, in meters per second.
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
		updateSpeedComponents();
	}

	/**
	 * @return The launching speed, in meters per second.
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * @return The x-component of the speed, in meters per second.
	 */
	public float getSpeedX() {
		return speedX;
	}

	/**
	 * @return The y-component of the speed, in meters per second.
	 */
	public float getSpeedY() {
		return speedY;
	}

	/**
	 * @param angle
	 *            The launching angle, in degrees.
	 */
	public void setAngle(float angle) {
		this.angle = angle;
		updateSpeedComponents();
	}

	/**
	 * @return The launching angle, in degrees.
	 */
	public float getAngle() {
		return angle;
	}

	/**
	 * @param rotationSpeed
	 *            The rotation speed, in degrees per millisecond.
	 */
	public void setRotationSpeed(float rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

	/**
	 * @return The rotation speed, in degrees per millisecond.
	 */
	public float getRotationSpeed() {
		return rotationSpeed;
	}

	/**
	 * @param direction
	 *            The direction of the shot.
	 * @see {@link Shot.Direction}
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	/**
	 * @return The direction of the shot.
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * @param wind
	 *            The wind value of the current map.
	 */
	public void setWind(float wind) {
		this.wind = wind;
	}

	/**
	 * @return The wind value of the current map.
	 */
	public float getWind() {
		return wind;
	}

	/**
	 * @param timeElapsed
	 *            The elapsed time which is used to calculate the parabolic arc.
	 */
	public void setTimeElapsed(float timeElapsed) {
		this.timeElapsed = timeElapsed;
	}

	/**
	 * @return The elapsed time which is used to calculate the parabolic arc.
	 */
	public float getTimeElapsed() {
		return timeElapsed;
	}

	/**
	 * Reset the elapsed time which is used to calculate the parabolic arc.
	 */
	public void resetTimeElapsed() {
		timeElapsed = 0;
	}

	/**
	 * Calculates and updates the speed components.
	 */
	protected void updateSpeedComponents() {
		float a = (float) Math.toRadians(angle);
		speedX = (float) (Math.cos(a) * speed);
		speedY = (float) (Math.sin(a) * speed);
	}

	/**
	 * Constructor
	 * 
	 * @param startPosition
	 *            The start position of this shot as <em>Vector2(x, y)</em>.
	 * @param angle
	 *            The launching angle, in degrees.
	 * @param speed
	 *            The launching speed, in meters per second.
	 * @param rotationSpeed
	 *            The rotation speed, in degrees per millisecond.
	 * @param direction
	 *            The direction of the shot.
	 * @param wind
	 *            The wind value of the current map
	 * @see {@link Shot.Direction}
	 */
	public Shot(Vector2 startPosition, Vector2 size, float angle, float speed,
			float rotationSpeed, Direction direction, float wind) {

		super(startPosition, size);
		this.startPosition = startPosition;
		this.angle = angle;
		this.speed = speed;
		this.rotationSpeed = rotationSpeed;
		this.direction = direction;
		this.wind = wind;
		updateSpeedComponents();
		
		float hitBottomX = ShotUtils.predictScreenBottomX(this);
		Gdx.app.debug("Shot destination", "Shot hits bottom at x=" + hitBottomX);
		landInScreen = hitBottomX > 0 & hitBottomX < Gorillas.worldWidth;
	}
	
	@Override
	public void update(float delta) {
		
		float dAngle = delta * this.getRotationSpeed();
		if (this.getDirection() == Direction.RIGHT_TO_LEFT) {
			dAngle = -dAngle;
		}
		float newAngle = (this.getRotation() + dAngle) % 360f;
		this.setRotation(newAngle);

		this.setPosition(ShotUtils.determineNextPosition(
				this, delta));
	}
}
