package de.ptkapps.gorillas.map;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

import de.ptkapps.gorillas.assets.Assets;
import de.ptkapps.gorillas.gameobjects.City;
import de.ptkapps.gorillas.gameobjects.factory.CityFactory;
import de.ptkapps.gorillas.main.Gorillas;

public class QBasicCityGenerator {

	/**
	 * <code>defaultBuildingWidth</code> is width of gorilla plus this constant
	 */
	private static final int BUILD_GORILLA_WIDTH_ADD = 10;

	private static final int WINDOW_WIDTH = 3;

	private static final int WINDOW_HEIGHT = 6;

	private static final int WINDOW_SPACING_VERTICAL = 15;

	private static final int WINDOW_SPACING_HORIZONTAL = 10;

	/**
	 * Increment value for the next building height. Buildings are added to the
	 * city one after another from left to right. Based on a randomly selected
	 * slope type the basic height of the next building is calulated by adding
	 * or subtracting one or two times this summand to the height of the current
	 * building.
	 */
	private int heightIncrement;

	/**
	 * The basic height of a building based on the slope type is randomized by
	 * adding this summand.
	 */
	private int randHeightDifference;

	/**
	 * The basic height of the first building in case of a slope type with a
	 * small first building.
	 */
	private int bottomSlopeStart;

	/**
	 * The basic height of the first building in case of a slope type with a
	 * heigh first building.
	 */
	private int topSlopeStart;

	/**
	 * Minimum building width equals default building width plus 2. The width of
	 * a building is calculated by adding a random summand from zero to default
	 * building width (inclusive) to the minimum building width.
	 */
	private int defaultBuildingWidth;

	private int cityHeight;

	private int cityWidth;

	/**
	 * the top left corners of the builings relative to the city position
	 * (y-axis is pointing up)
	 */
	private ArrayList<Vector2> buildingCoordinates;

	/**
	 * the pixmap of the city, which can be used to create a texture and draw
	 * the city.
	 */
	private Pixmap city;

	public QBasicCityGenerator() {
		defaultBuildingWidth = Assets.gorilla.getRegionWidth()
				+ BUILD_GORILLA_WIDTH_ADD;
	}

	/**
	 * Generates a city
	 * 
	 * @param cityWidth
	 *            the width of the resulting city
	 * @param cityHeight
	 *            the height of the resulting city
	 * @param yOffsetCity
	 *            The y offset of the resulting city (y-axis pointing down).
	 *            This value is only used to assign a position to the resulting
	 *            city. The city will have a position of (0,
	 *            <code>yOffsetCity</code>).
	 * 
	 * @return the generated city
	 */
	public City generateCity(int cityWidth, int cityHeight, int yOffsetCity) {

		this.cityWidth = cityWidth;
		this.cityHeight = cityHeight;

		initBuildValues();

		generateSkyline();

		drawSkyline();

		return CityFactory.createCity(new Vector2(0, yOffsetCity), city,
				buildingCoordinates);
	}

	public City generateCity(int cityWidth, int cityHeight, int yOffsetCity,
			ArrayList<Vector2> buildingCoordinates,
			Vector2 leftGorillaCoordinate, Vector2 rightGorillaCoordinate) {

		this.cityWidth = cityWidth;
		this.cityHeight = cityHeight;

		this.buildingCoordinates = buildingCoordinates;

		drawSkyline();

		return CityFactory.createCity(new Vector2(0, yOffsetCity), city,
				buildingCoordinates, leftGorillaCoordinate,
				rightGorillaCoordinate);
	}

	/**
	 * initializes the values used for randomly creating a new city based on the
	 * given <code>cityHeight</code> and <code>cityWidth<code>
	 */
	private void initBuildValues() {

		topSlopeStart = (int) Math.round(cityHeight / 2.0);
		randHeightDifference = (int) Math.round(cityHeight / 2.0);

		int maxNumberBuildings = (int) Math.ceil(cityWidth
				/ (float) defaultBuildingWidth);

		heightIncrement = (int) Math.floor(topSlopeStart
				/ (float) maxNumberBuildings);
	}

	private void generateSkyline() {

		buildingCoordinates = new ArrayList<Vector2>();

		Random random = new Random();
		int slopeType = random.nextInt(6) + 1;

		// This height is determined for each building taking into account the
		// generated slopeType of the city. The actual building height will be
		// calculated as the basic height plus a random factor.
		int currentBasicHeight = calcInitialBasicHeight(slopeType);

		int x = 2;
		do {
			currentBasicHeight = calcNextBasicHeight(x, slopeType,
					currentBasicHeight, cityWidth);

			// Set width of building and correct the width, if the building
			// makes the city too wide
			int buildingWidth = (int) (random.nextInt(defaultBuildingWidth) + 2 + defaultBuildingWidth);
			if (x + buildingWidth > cityWidth) {
				buildingWidth = cityWidth - x - 2;
			}

			// Set height of building and correct height, if the building is too
			// small
			int buildingHeight = random.nextInt(randHeightDifference + 1) + 1
					+ currentBasicHeight;
			if (buildingHeight < heightIncrement) {
				buildingHeight = heightIncrement;
			}

			// Check to see if building is too high
			if (buildingHeight >= cityHeight) {
				buildingHeight = cityHeight;
			}

			// Set the coordinates of the building into the array
			buildingCoordinates.add(new Vector2(x, buildingHeight));

			x = x + buildingWidth + 2;

		} while (x < cityWidth - heightIncrement);
	}

	private void drawSkyline() {

		city = new Pixmap(cityWidth, cityHeight, Pixmap.Format.RGBA8888);
		Pixmap.setBlending(Pixmap.Blending.None);

		for (int i = 0; i < buildingCoordinates.size(); i++) {

			Vector2 buildingCoordinate = buildingCoordinates.get(i);

			int x = (int) buildingCoordinate.x;
			int buildingHeight = (int) buildingCoordinate.y;

			int buildingWidth;
			if (i == buildingCoordinates.size() - 1) {
				buildingWidth = cityWidth - x;
			} else {
				buildingWidth = (int) (buildingCoordinates.get(i + 1).x - x);
			}
			buildingWidth -= 2;

			drawBuilding(city, x, buildingWidth, buildingHeight);
		}

		// adjust space for score
		city.setColor(new Color(255, 255, 255, 0));
		city.fillRectangle(cityWidth / 2 - (int) Gorillas.scoreLabelWidth / 2,
				cityHeight - (int) Gorillas.normalLabelHeight - 5,
				(int) Gorillas.scoreLabelWidth,
				(int) Gorillas.normalLabelHeight);
	}

	/**
	 * determines the basic height for the next building according to the
	 * slopeType
	 * 
	 * @param x
	 *            the x position of the building
	 * @param slopeType
	 *            the slopeType of the city
	 * @param currentBasicHeight
	 *            the basic height of the building left to the building, whose
	 *            basic height should be returned
	 * @param cityWidth
	 *            the width of the city
	 * 
	 * @return the basic height for the next building of the city
	 */
	private int calcNextBasicHeight(int x, int slopeType,
			int currentBasicHeight, int cityWidth) {

		if (slopeType == 1) {
			// upward slope
			currentBasicHeight += heightIncrement;

		} else if (slopeType == 2) {
			// downward slope
			currentBasicHeight -= heightIncrement;

		} else if (slopeType >= 3 && slopeType <= 5) {
			// "V" slope - most common
			if (x > cityWidth / 2f) {
				currentBasicHeight = currentBasicHeight + 2 * heightIncrement;
			} else {
				currentBasicHeight = currentBasicHeight - 2 * heightIncrement;
			}

		} else {
			// inverted "V" slope
			if (x > cityWidth / 2f) {
				currentBasicHeight = currentBasicHeight - 2 * heightIncrement;
			} else {
				currentBasicHeight = currentBasicHeight + 2 * heightIncrement;
			}
		}

		return currentBasicHeight;
	}

	/**
	 * determines the basic height for the first building according to the
	 * slopeType
	 * 
	 * @param slopeType
	 *            the slopeType of the city
	 * @return the basic height for the first building of the city
	 */
	private int calcInitialBasicHeight(int slopeType) {

		if (slopeType == 1) {
			// upward slope
			Gdx.app.debug("SlopeTye", "Generating upward slope.");
			return bottomSlopeStart;

		} else if (slopeType == 2) {
			// downward slope
			Gdx.app.debug("SlopeTye", "Generating downward slope.");
			return topSlopeStart;

		} else if (slopeType >= 3 && slopeType <= 5) {
			Gdx.app.debug("SlopeTye", "Generating V slope.");
			// "V" slope - most common
			return topSlopeStart;

		} else {
			// inverted "V" slope
			Gdx.app.debug("SlopeTye", "Generating inverted V slope.");
			return bottomSlopeStart;
		}
	}

	/**
	 * draws a single building a the specified x position on the graphic with
	 * the width specified in buildingWidth and the height specified in
	 * buildingHeigt
	 * 
	 * @param pixmap
	 *            the pixmap to draw on
	 * @param x
	 *            the x position of the left buildings corner
	 * @param buildingWidth
	 *            the width of the building to draw
	 * @param buildingHeight
	 *            the height of the building to draw
	 */
	private void drawBuilding(Pixmap pixmap, int x, int buildingWidth,
			int buildingHeight) {

		int argb = chooseBuildingColor();

		// Draw the building
		pixmap.setColor(new Color(argb));
		pixmap.fillRectangle(x, cityHeight - buildingHeight, buildingWidth,
				buildingHeight);

		// Draw the windows
		int c = x + 3;
		do {
			for (int i = buildingHeight - 3; i >= 7; i -= WINDOW_SPACING_VERTICAL) {

				// draw single window
				argb = chooseWindowColor();
				pixmap.setColor(new Color(argb));
				pixmap.fillRectangle(c, cityHeight - i, WINDOW_WIDTH,
						WINDOW_HEIGHT);
			}
			c += WINDOW_SPACING_HORIZONTAL;

		} while (c < x + buildingWidth - 3);

	}

	/**
	 * randomly chooses a building color out of the three QBasic colors light
	 * grey, dark red and turquoise
	 * 
	 * @return the window color encoded as Color.ARGB value
	 */
	private int chooseBuildingColor() {

		int argb = 0;

		Random random = new Random();
		int qBasicColor = random.nextInt(3) + 1;

		switch (qBasicColor) {
		case 1:
			argb = 0xA80000FF;
			break;
		case 2:
			argb = 0xA8A8A8FF;
			break;
		case 3:
			argb = 0x54FCFCFF;
			break;
		}
		return argb;
	}

	/**
	 * randomly chooses a window color out of the two QBasic colors dark grey
	 * and yellow
	 * 
	 * @return the window color encoded as Color.ARGB value
	 */
	private int chooseWindowColor() {

		int argb = 0;

		Random random = new Random();
		int ran = random.nextInt(2);

		if (ran == 0) {
			argb = 0xFCFC54FF;
		} else {
			argb = 0x545454FF;
		}
		return argb;
	}

	// @formatter:off
		/**
			
		QBasic color table:
			
		Attribute Hex value Red Green Blue 
		COLOR 0 = &HFF000050 00 00 50 
		COLOR 1 = &HFF0000A8 00 00 A8 
		COLOR 2 = &HFF00A800 00 A8 00 
		COLOR 3 = &HFF00A8A8 00 A8 A8 
		COLOR 4 = &HFFA80000 A8 00 00 
		COLOR 5 = &HFFA800A8 A8 00 A8 
		COLOR 6 = &HFFA85400 A8 54 00 
		COLOR 7 = &HFFA8A8A8 A8 A8 A8 
		COLOR 8 = &HFF545454 54 54 54 
		COLOR 9 = &HFF5454FC 54 54 FC 
		COLOR 10 = &HFF54FC54 54 FC 54 
		COLOR 11 = &HFF5454FC 54 FC FC 
		COLOR 12 = &HFFFC5454 FC 54 54 
		COLOR 13 = &HFFFC54FC FC 54 FC 
		COLOR 14 = &HFFFCFC54 FC FC 54 
		COLOR 15 = &HFFFCFCFC FC FC FC
		*/
	// @formatter:on
}
