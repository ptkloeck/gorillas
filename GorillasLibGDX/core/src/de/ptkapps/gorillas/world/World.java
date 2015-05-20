package de.ptkapps.gorillas.world;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import de.ptkapps.gorillas.assets.Assets;
import de.ptkapps.gorillas.gameobjects.City;
import de.ptkapps.gorillas.gameobjects.Gorilla;
import de.ptkapps.gorillas.gameobjects.Shot;
import de.ptkapps.gorillas.gameobjects.Shot.Direction;
import de.ptkapps.gorillas.gameobjects.Sun;
import de.ptkapps.gorillas.gameobjects.destructible.ImageDestructibleGameObject;
import de.ptkapps.gorillas.gameobjects.factory.GorillaFactory;
import de.ptkapps.gorillas.gameobjects.factory.ShotFactory;
import de.ptkapps.gorillas.gameobjects.factory.SunFactory;
import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.map.Map;
import de.ptkapps.gorillas.map.MapGenerator;
import de.ptkapps.gorillas.parameterization.Parameterization;
import de.ptkapps.gorillas.screens.GameScreen;
import de.ptkapps.gorillas.screens.gui.GUIConstants;

public class World {

	public Shot currentShot;

	public Gorilla gorilla1;
	public Gorilla gorilla2;

	public City city;

	public Sun sun;

	public Map currentMap;

	public int scorePlayer1;
	public int scorePlayer2;

	public String namePlayer1;
	public String namePlayer2;

	public Parameterization paramPlayer1;
	public Parameterization paramPlayer2;

	public World() {
		paramPlayer1 = new Parameterization(Direction.LEFT_TO_RIGHT);
		paramPlayer2 = new Parameterization(Direction.RIGHT_TO_LEFT);
	}

	public void initGame() {

		scorePlayer1 = 0;
		scorePlayer2 = 0;
		generateLevel();
	}

	public void generateLevel(ArrayList<Vector2> buildingCoordinates,
			Vector2 leftGorillaCoordinate, Vector2 rightGorillaCoordinate,
			int wind) {

		MapGenerator mapGenerator = new MapGenerator();
		currentMap = mapGenerator.generateMap(buildingCoordinates,
				leftGorillaCoordinate, rightGorillaCoordinate, wind);

		setupLevel();
	}

	public void generateLevel() {

		// float paneWidth = Gorillas.worldWidth;
		// float paneHeight = Gorillas.worldHeight;
		//
		// int yOffsetCity = (int) Gorillas.worldHeight / 3;
		// int cityHeight = (int) Gorillas.worldHeight - yOffsetCity - 20;
		//
		// cityHeight = 1;
		//
		// ArrayList<Vector2> buildingCoordinates = new ArrayList<Vector2>();
		// buildingCoordinates.add(new Vector2(2, cityHeight));
		//
		// Vector2 gorillaLeftCoordinate = new Vector2(2 + paneWidth / 5 / 2
		// - Assets.gorilla.getRegionWidth() / 2, cityHeight
		// + Assets.gorilla.getRegionHeight());
		//
		// Vector2 gorillaRightCoordinate = new Vector2(2 + paneWidth / 5 * 4
		// + paneWidth / 5 / 2 - Assets.gorilla.getRegionWidth() / 2,
		// cityHeight + Assets.gorilla.getRegionHeight());

		MapGenerator mapGenerator = new MapGenerator();
		currentMap = mapGenerator.generateMap();

		// currentMap = mapGenerator.generateMap(buildingCoordinates,
		// gorillaLeftCoordinate,
		// gorillaRightCoordinate, 0);

		setupLevel();
	}

	private void setupLevel() {

		paramPlayer1.reset();
		paramPlayer2.reset();

		if (city != null) {
			// the pixmap of the old city - if there was one - has to be
			// disposed
			city.dispose();
		}

		city = currentMap.getCity();

		Vector2 cityLeftGorillaPos = city.getLeftGorillaCoordinate();
		float gorillaLeftPosY = city.getPosition().y
				+ (city.getHeight() - cityLeftGorillaPos.y);
		Vector2 gorillaLeftPosition = new Vector2(cityLeftGorillaPos.x,
				gorillaLeftPosY);
		gorilla1 = GorillaFactory.createGorilla(gorillaLeftPosition,
				Direction.LEFT_TO_RIGHT);

		Vector2 cityRightGorillaPos = city.getRightGorillaCoordinate();
		float gorillaRightPosY = city.getPosition().y
				+ (city.getHeight() - cityRightGorillaPos.y);
		Vector2 gorillaRightPosition = new Vector2(cityRightGorillaPos.x,
				gorillaRightPosY);
		gorilla2 = GorillaFactory.createGorilla(gorillaRightPosition,
				Direction.RIGHT_TO_LEFT);

		gorilla1.numShotsFired = 0;
		gorilla2.numShotsFired = 0;

		sun = SunFactory.createSun(Gorillas.worldWidth / 2
				- Assets.sunAstonished.getRegionWidth() / 2,
				Gorillas.normalLabelHeight + GUIConstants.NAME_OFFSET_Y * 2);
	}

	public void generateShot(Gorilla shooter, int angle, int velocity) {

		Vector2 shotStartPosition = shooter.getShotStartPosition();
		Direction shotDirection = shooter.getDirection();
		shooter.startThrow();
		shooter.numShotsFired++;

		currentShot = ShotFactory.createShot(shotStartPosition, shotDirection,
				angle, velocity, currentMap.getWind());
	}

	public void update(float delta) {

		if (currentShot != null) {
			currentShot.update(delta);
		}
		gorilla1.update(delta);
		gorilla2.update(delta);
		city.update(delta);
		sun.update(delta);
		paramPlayer1.update(delta);
		paramPlayer2.update(delta);
	}

	public boolean checkCityCollision() {

		if (checkBoundingBoxShotCollision(city)) {

			city.impactAt(currentShot.getPosition());

			return true;
		} else {
			return false;
		}
	}

	public boolean checkGorillaHit(Gorilla gorilla) {

		if (checkBoundingBoxShotCollision(gorilla)) {

			gorilla.state = Gorilla.GORILLA_STATE_DESTRUCTED;

			city.switchDestructionPatternTo(City.gorillaDestrPatternID);
			float cityImpactPositionX = gorilla.getPosition().x
					+ gorilla.getSize().x / 2;
			float cityImpactPositionY = gorilla.getPosition().y
					+ gorilla.getSize().y / 2;
			city.impactAt(new Vector2(cityImpactPositionX, cityImpactPositionY));
			city.switchDestructionPatternTo(City.bananaDestrPatternID);

			if (gorilla.equals(gorilla1)) {
				gorilla2.cheer();
			} else {
				gorilla1.cheer();
			}

			currentShot = null;
			return true;
		}
		return false;
	}

	private boolean checkBoundingBoxShotCollision(
			ImageDestructibleGameObject gameObject) {

		Vector2 shotPosition = currentShot.getPosition();

		int bananaWidth = Assets.banana.getRegionWidth();
		int bananaHeight = Assets.banana.getRegionHeight();

		// do a bounding box test
		boolean collides = gameObject.collides(shotPosition.x, shotPosition.y);
		collides |= gameObject.collides(shotPosition.x + bananaWidth,
				shotPosition.y);
		collides |= gameObject.collides(shotPosition.x, shotPosition.y
				+ bananaHeight);
		collides |= gameObject.collides(shotPosition.x + bananaWidth,
				shotPosition.y + bananaHeight);

		return collides;
	}

	public boolean checkLeaveScreen() {

		Vector2 shotPosition = currentShot.getPosition();
		if (((shotPosition.x < -currentShot.getWidth() | shotPosition.x > Gorillas.worldWidth) & !currentShot
				.isLandInScreen()) | shotPosition.y > Gorillas.worldHeight) {
			return true;
		}
		return false;
	}

	public boolean checkCheeringCompleted(Gorilla gorilla) {

		if (Assets.cheeringGorilla.isAnimationFinished(gorilla.stateTime)) {
			gorilla.state = Gorilla.GORILLA_STATE_NORMAL;
			return true;
		} else
			return false;
	}

	public boolean checkThrowingCompleted(int whoseTurn) {

		Gorilla gorilla = whoseTurn == GameScreen.TURN_PLAYER_1 ? gorilla1
				: gorilla2;

		if (Assets.leftThrowingGorilla.isAnimationFinished(gorilla.stateTime)) {
			gorilla.state = Gorilla.GORILLA_STATE_NORMAL;
			return true;
		} else {
			return false;
		}
	}

	public boolean cityDestructionCompleted() {

		if (Assets.cityDestruction.isAnimationFinished(city.stateTime)) {
			city.state = City.CITY_STATE_NORMAL;
			return true;
		} else {
			return false;
		}
	}

	public void checkSunHit() {

		Vector2 shotPosition = currentShot.getPosition();
		float shotX = shotPosition.x;
		float shotY = shotPosition.y;

		Vector2 sunPosition = sun.getPosition();
		float sunX = sunPosition.x;
		float sunY = sunPosition.y;
		Vector2 sunSize = sun.getSize();
		float sunWidth = sunSize.x;
		float sunHeight = sunSize.y;

		if (shotX >= sunX && shotX <= sunX + sunWidth && shotY >= sunY
				&& shotY <= sunY + sunHeight) {
			sun.beAstonished();
		}
	}

	public void dispose() {
		if (!(city == null)) {
			city.dispose();
		}
	}

	public void resume() {
		gorilla1.reload();
		gorilla2.reload();
		city.reload();
	}
}
