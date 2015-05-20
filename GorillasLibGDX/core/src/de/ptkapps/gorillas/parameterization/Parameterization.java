package de.ptkapps.gorillas.parameterization;

import com.badlogic.gdx.math.Vector2;

import de.ptkapps.gorillas.gameobjects.Shot.Direction;
import de.ptkapps.gorillas.main.Gorillas;

/**
 * 
 * class, which helps handling the modern control type
 * 
 * One instance of this class is used for each player.
 * 
 * @author Peter
 * 
 */
public class Parameterization {

	public static final int MINIMUM_LENGTH = 10;

	private float maximumLength;

	/**
	 * whether to show the chosen parameterization of the last throw
	 */
	private boolean showOld;

	/**
	 * the remembered old arrow start and end positions. These are not the
	 * screen coordinates, where the touch or click occurred or were released,
	 * but the actual start and end position of the rendered arrow. Thus, the
	 * conversion does not have to be made twice.
	 */
	private Vector2 oldArrowStartPos;
	private Vector2 oldArrowEndPos;

	private Vector2 lastArrowStartPos;
	private Vector2 lastArrowEndPos;

	/**
	 * indicates whether a shot is being adjusted in the moment
	 */
	private boolean isBeingAdjusted;

	/**
	 * the direction of the shot, which is parameterized
	 */
	private Direction direction;

	/**
	 * the screen coordinate, where a touch or click occurred, which started the
	 * parameterization
	 */
	private Vector2 startPos;

	/**
	 * the screen coordinate, where a touch or click was released, which
	 * finished the parameterization
	 */
	private Vector2 endPos;

	public float stateTime;

	public boolean isShowOld() {
		return showOld;
	}

	public void setShowOld(boolean showOld) {
		this.showOld = showOld;
		stateTime = 0;
	}

	public Vector2 getOldArrowStartPos() {
		return oldArrowStartPos;
	}

	public Vector2 getOldArrowEndPos() {
		return oldArrowEndPos;
	}

	public void setBeingAdjusted(boolean isBeingAdjusted) {
		this.isBeingAdjusted = isBeingAdjusted;
	}

	public boolean isBeingAdjusted() {
		return isBeingAdjusted;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setStartPos(Vector2 startPos) {
		this.startPos = startPos;
		isBeingAdjusted = true;
	}

	public Vector2 getStartPos() {
		return startPos;
	}

	public void setEndPos(Vector2 endPos) {

		if (!(endPos.dst(startPos) > maximumLength)) {
			this.endPos = endPos;

		} else {
			Vector2 direction = endPos.cpy().sub(startPos);
			float directionLength = endPos.dst(startPos);
			this.endPos = startPos.cpy().mulAdd(direction,
					maximumLength / directionLength);
		}
	}

	public Vector2 getEndPos() {
		return endPos;
	}

	public Parameterization(Direction direction) {

		isBeingAdjusted = false;
		this.direction = direction;
		stateTime = 0;

		maximumLength = Gorillas.worldWidth / 2;
		if (maximumLength > 400) {
			maximumLength = 400;
		}
	}

	public void saveToOldParameterization() {
		oldArrowStartPos = lastArrowStartPos;
		oldArrowEndPos = lastArrowEndPos;
	}

	public void reset() {
		oldArrowStartPos = null;
		oldArrowEndPos = null;
		isBeingAdjusted = false;
		showOld = false;
	}

	public void rememberParameterization(Vector2 arrowStartPos,
			Vector2 arrowEndPos) {
		lastArrowStartPos = arrowStartPos;
		lastArrowEndPos = arrowEndPos;
	}

	public boolean minLengthReached() {
		return endPos.dst(startPos) >= MINIMUM_LENGTH;
	}

	public int getVelocity() {

		float length = endPos.dst(startPos);

		int velocity = 0;

		if (length > maximumLength) {
			velocity = 200;
		} else {
			velocity = (int) (200 * length / maximumLength);
		}

		return velocity;
	}

	public int getAngle() {

		float angle = 0;
		Vector2 shotVector = startPos.cpy().sub(endPos);

		if (direction.equals(Direction.LEFT_TO_RIGHT)) {

			angle = shotVector.angle();

		} else {

			angle = shotVector.angle();
			if (shotVector.y > 0) {
				angle = 180 - angle;
			} else {
				angle = 180 + (360 - angle);
			}
		}

		return (int) angle;
	}

	public void update(float delta) {
		stateTime += delta;
	}
}
