package de.ptkapps.gorillas.gameobjects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

import de.ptkapps.gorillas.assets.Assets;
import de.ptkapps.gorillas.gameobjects.destructible.ImageDestructibleGameObject;
import de.ptkapps.gorillas.utils.Utils;

public class City extends ImageDestructibleGameObject {

	public static final String bananaDestrPatternID = "BANANA_DESTRUCTION";
	public static final String gorillaDestrPatternID = "GORILLA_DESTRUCTION";

	public static final int CITY_STATE_NORMAL = 0;
	public static final int CITY_STATE_BEEING_DESTRUCTED = 1;

	/**
	 * The upper left corners of the buildings relative to the city position. In
	 * this case the y-axis is pointing up.
	 */
	private ArrayList<Vector2> buildingCoordinates;

	/**
	 * The position of the left gorilla relative to the city position. In this
	 * case the y-axis is pointing up.
	 */
	private Vector2 leftGorillaCoordinate;
	/**
	 * The position of the right gorilla relative to the city position. In this
	 * case the y-axis is pointing up.
	 */
	private Vector2 rightGorillaCoordinate;

	public Vector2 getLeftGorillaCoordinate() {
		return leftGorillaCoordinate;
	}

	public Vector2 getRightGorillaCoordinate() {
		return rightGorillaCoordinate;
	}

	public City(Vector2 position, Pixmap original, Pixmap destructionPattern,
			String destructionPatternID, ArrayList<Vector2> buildingCoordinates) {

		super(position, original, destructionPattern, destructionPatternID);

		this.buildingCoordinates = buildingCoordinates;

		randChooseGorillaCoordinates();
	}

	public City(Vector2 position, Pixmap original, Pixmap destructionPattern,
			String destructionPatternID,
			ArrayList<Vector2> buildingCoordinates,
			Vector2 leftGorillaCoordinate, Vector2 rightGorillaCoordinate) {

		super(position, original, destructionPattern, destructionPatternID);

		this.buildingCoordinates = buildingCoordinates;
		this.leftGorillaCoordinate = leftGorillaCoordinate;
		this.rightGorillaCoordinate = rightGorillaCoordinate;
	}

	private void randChooseGorillaCoordinates() {

		float gorillaWidth = Assets.gorilla.getRegionWidth();
		float gorillaHeight = Assets.gorilla.getRegionHeight();

		// list of possible coordinates for the left gorilla
		ArrayList<Vector2> leftPositions = new ArrayList<Vector2>();

		for (int i = 0; i < 3; i++) {

			float buildingStartX = buildingCoordinates.get(i).x;
			float buildingEndX = buildingCoordinates.get(i + 1).x;

			float buildingWidth = buildingEndX - buildingStartX;
			if (buildingWidth >= gorillaWidth) {
				float buildingY = buildingCoordinates.get(i).y;
				leftPositions.add(new Vector2(buildingStartX + buildingWidth
						/ 2 - gorillaWidth / 2, buildingY + gorillaHeight));
			}
		}

		// choose random coordinate out of the possible ones
		leftGorillaCoordinate = (Vector2) Utils.randomChooseFrom(leftPositions
				.toArray());

		// list of possible coordinates for the right gorilla
		ArrayList<Vector2> rightPositions = new ArrayList<Vector2>();

		for (int i = buildingCoordinates.size() - 1; i > buildingCoordinates
				.size() - 4; i--) {

			float buildingStartX = buildingCoordinates.get(i).x;

			float buildingEndX;
			if (i == buildingCoordinates.size() - 1) {
				buildingEndX = size.x;
			} else {
				buildingEndX = buildingCoordinates.get(i + 1).x;
			}

			float buildingWidth = buildingEndX - buildingStartX;
			if (buildingWidth >= gorillaWidth) {
				float buildingY = buildingCoordinates.get(i).y;
				rightPositions.add(new Vector2(buildingStartX + buildingWidth
						/ 2 - gorillaWidth / 2, buildingY + gorillaHeight));
			}
		}

		// choose random coordinate out of the possible ones
		rightGorillaCoordinate = (Vector2) Utils
				.randomChooseFrom(rightPositions.toArray());
	}

	public void startDestruction() {
		state = CITY_STATE_BEEING_DESTRUCTED;
		stateTime = 0;
	}
}