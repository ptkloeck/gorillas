package de.ptkapps.gorillas.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.ptkapps.gorillas.assets.Assets;
import de.ptkapps.gorillas.gameobjects.City;
import de.ptkapps.gorillas.gameobjects.Gorilla;
import de.ptkapps.gorillas.gameobjects.Shot;
import de.ptkapps.gorillas.gameobjects.Shot.Direction;
import de.ptkapps.gorillas.gameobjects.Sun;
import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.parameterization.Parameterization;
import de.ptkapps.gorillas.screens.gui.GUIConstants;

public class WorldRenderer {

	private static final Color WINDOW_ARROW_COLOR = new Color(168, 0, 168, 0);

	private static final Color CONTROL_ARROW_COLOR = Color.WHITE;
	private static final Color OLD_CONTROL_ARROW_COLOR = Color.GRAY;

	private static final int CONTROL_ARROW_WIDTH = 4;
	private static final int CONTROL_ARROW_HEADLINE_LENGTH = 12;

	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	private Viewport viewport;

	private World world;

	public WorldRenderer(SpriteBatch batch, World world) {

		this.batch = batch;

		shapeRenderer = new ShapeRenderer();

		this.camera = new OrthographicCamera();
		camera.setToOrtho(true, Gorillas.worldWidth, Gorillas.worldHeight);
		camera.update();

		viewport = new FillViewport(Gorillas.worldWidth, Gorillas.worldHeight,
				camera);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		this.world = world;
	}

	public void resize(int width, int height) {

		viewport.update(width, height);
	}

	public void render() {

		batch.setProjectionMatrix(camera.projection);
		batch.setTransformMatrix(camera.view);

		batch.begin();

		renderGorillas();
		renderShot();
		renderCity();
		renderSun();

		batch.end();

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.identity();

		renderWind();

		shapeRenderer.end();

		camera.setToOrtho(false, Gorillas.worldWidth, Gorillas.worldHeight);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.identity();

		renderParameterization();

		shapeRenderer.end();
		camera.setToOrtho(true, Gorillas.worldWidth, Gorillas.worldHeight);
	}

	private void renderParameterization() {

		Parameterization param1 = world.paramPlayer1;
		Parameterization param2 = world.paramPlayer2;

		if (param1.isShowOld()) {

			renderOldParameterization(param1);
		}

		if (param1.isBeingAdjusted()) {

			renderNewParameterization(param1);
		}

		if (param2.isShowOld()) {

			renderOldParameterization(param2);
		}

		if (param2.isBeingAdjusted()) {

			renderNewParameterization(param2);
		}
	}

	private void renderOldParameterization(Parameterization param) {

		Vector2 arrowStartPos = param.getOldArrowStartPos();
		Vector2 arrowEndPos = param.getOldArrowEndPos();

		// if there is an old parameterization, render it
		if (arrowStartPos != null && arrowEndPos != null) {
			// fade in the arrow, to make transition more smooth
			float t = param.stateTime * 1.5f < 1 ? param.stateTime * 1.5f : 1;
			Color color = GUIConstants.BACKGROUND_COLOR.cpy().lerp(
					OLD_CONTROL_ARROW_COLOR, t);
			drawArrow(arrowStartPos, arrowEndPos, CONTROL_ARROW_WIDTH,
					CONTROL_ARROW_HEADLINE_LENGTH, color);
		}
	}

	private void renderNewParameterization(Parameterization param) {

		if (!param.minLengthReached()) {
			return;
		}

		Vector2 paramStartPos = param.getStartPos();
		Vector2 paramEndPos = param.getEndPos();

		Vector2 arrowStartPos;

		float frameHeight = Gorillas.worldHeight;

		if (param.getDirection().equals(Direction.LEFT_TO_RIGHT)) {

			Vector2 gorilla1Pos = world.gorilla1.getShotStartPosition();
			// the shapeRenderer draws with the y axis pointing up
			arrowStartPos = new Vector2(gorilla1Pos.x, frameHeight
					- gorilla1Pos.y);

		} else {

			Vector2 gorilla2Pos = world.gorilla2.getShotStartPosition();
			// the shapeRenderer draws with the y axis pointing up
			arrowStartPos = new Vector2(gorilla2Pos.x
					+ Assets.banana.getRegionWidth(), frameHeight
					- gorilla2Pos.y);
		}

		Vector2 arrowEndPos = arrowStartPos.cpy().add(
				paramStartPos.cpy().sub(paramEndPos.cpy()));

		param.rememberParameterization(arrowStartPos, arrowEndPos);

		drawArrow(arrowStartPos, arrowEndPos, CONTROL_ARROW_WIDTH,
				CONTROL_ARROW_HEADLINE_LENGTH, CONTROL_ARROW_COLOR);
	}

	/**
	 * draws an arrow, which starts in <code>startPos</code> and ends in
	 * <code>endPos</code>. Both the body of the arrow and the two headlines
	 * have a width of <code>arrowWidth</code>. The two headlines both have a
	 * length of <code>headlineLength</code>.
	 * 
	 * The <code>shapeRenderer</code>, which is used expects the y-axis to point
	 * up.
	 * 
	 * @param startPos
	 * @param endPos
	 * @param arrowWidth
	 * @param headLineLength
	 * @param color
	 */
	private void drawArrow(Vector2 startPos, Vector2 endPos, float arrowWidth,
			float headLineLength, Color color) {

		shapeRenderer.setColor(color);
		shapeRenderer.identity();

		Vector2 shotVector = endPos.cpy().sub(startPos);

		float rotationAngle = shotVector.angle() - 90;

		float length = endPos.dst(startPos);

		shapeRenderer.translate(startPos.x, startPos.y, 0);
		shapeRenderer.rotate(0, 0, 1, rotationAngle);

		shapeRenderer.rect(-arrowWidth / 2, 0, arrowWidth, length);
		shapeRenderer.rect(-arrowWidth, length + arrowWidth / 2, arrowWidth, 0,
				arrowWidth, headLineLength, 1, 1, 135);
		shapeRenderer.rect(0, length + arrowWidth / 2, 0, 0, arrowWidth,
				headLineLength, 1, 1, 225);
	}

	private void renderSun() {

		Sun sun = world.sun;
		TextureRegion sunKeyFrame = Assets.sunSmiling;

		if (sun.state == Sun.SUN_STATE_ASTONISHED) {
			sunKeyFrame = Assets.sunAstonished;

		}

		batch.draw(sunKeyFrame, sun.getPosition().x, sun.getPosition().y);
	}

	private void renderWind() {

		float wind = world.currentMap.getWind();

		if (wind != 0) {

			shapeRenderer.setColor(WINDOW_ARROW_COLOR);
			float frameWidth = Gorillas.worldWidth;
			float frameHeight = Gorillas.worldHeight;

			float windLine = wind * 3f * frameWidth / 320f;

			float startX = frameWidth / 2;
			float startY = frameHeight - 11;
			float arrowWidth = Math.abs(windLine);
			float arrowHeight = 2;
			float headLineWidth = 12;

			float halfArrowHeight = arrowHeight / 2f;

			if (windLine < 0) {
				// at last translate the arrow back into the middle
				shapeRenderer.translate(startX + arrowWidth / 2f
						+ halfArrowHeight, startY + arrowHeight, 0);
				// then mirror the arrow, because wind blows from right to left
				shapeRenderer.rotate(0, 0, 1, -180);
				// first translate to (0,0)
				shapeRenderer.translate(-startX, -startY, 0);
			} else {
				// the arrow is later drawn beginning from the middle of the
				// frame. Thus, translate everything which is drawn from now on
				// by half of the arrow's width to the left
				shapeRenderer.translate(-1
						* (arrowWidth / 2f + halfArrowHeight), 0, 0);
			}

			// draw arrow body
			shapeRenderer.rect(startX, startY, arrowWidth, arrowHeight);

			// draw lower head line
			shapeRenderer.rect(startX + arrowWidth + halfArrowHeight, startY
					+ halfArrowHeight, 0, 0, headLineWidth, arrowHeight, 1, 1,
					135);
			// draw upper head line
			shapeRenderer.rect(startX + arrowWidth + halfArrowHeight, startY
					- halfArrowHeight, 0, arrowHeight, headLineWidth,
					arrowHeight, 1, 1, 225);
		}
	}

	private void renderGorillas() {

		Gorilla gorilla1 = world.gorilla1;
		Gorilla gorilla2 = world.gorilla2;

		renderGorilla(gorilla1);
		renderGorilla(gorilla2);
	}

	private void renderGorilla(Gorilla gorilla) {

		TextureRegion gorillaKeyFrame;

		if (gorilla.state == Gorilla.GORILLA_STATE_THROWING) {

			if (gorilla.getDirection() == Direction.LEFT_TO_RIGHT) {
				gorillaKeyFrame = Assets.leftThrowingGorilla.getKeyFrame(
						gorilla.stateTime, false);
			} else {
				gorillaKeyFrame = Assets.rightThrowingGorilla.getKeyFrame(
						gorilla.stateTime, false);
			}

			batch.draw(gorillaKeyFrame, (int) gorilla.getPosition().x,
					(int) gorilla.getPosition().y);

		} else if (gorilla.state == Gorilla.GORILLA_STATE_CHEERING) {

			gorillaKeyFrame = Assets.cheeringGorilla.getKeyFrame(
					gorilla.stateTime, false);

			batch.draw(gorillaKeyFrame, (int) gorilla.getPosition().x,
					(int) gorilla.getPosition().y);

		} else if (gorilla.state == Gorilla.GORILLA_STATE_DESTRUCTED) {

			batch.draw(Assets.gorillaDestructed, (int) gorilla.getPosition().x,
					(int) gorilla.getPosition().y);

		} else {

			// state is normal
			Texture gorillaTexture = gorilla.getTexture();

			batch.draw(gorillaTexture, (int) gorilla.getPosition().x,
					(int) gorilla.getPosition().y, gorillaTexture.getWidth(),
					gorillaTexture.getHeight(), 0, 0,
					gorillaTexture.getWidth(), gorillaTexture.getHeight(),
					false, true);
		}
	}

	private void renderShot() {

		Shot shot = world.currentShot;
		if (world.currentShot != null) {
			batch.draw(Assets.banana, world.currentShot.getPosition().x,
					world.currentShot.getPosition().y, shot.getWidth() / 2,
					shot.getHeight() / 2, shot.getWidth(), shot.getHeight(), 1,
					1, shot.getRotation() * 360);
		}
	}

	private void renderCity() {

		City city = world.city;

		batch.draw(city.getTexture(), city.getPosition().x,
				city.getPosition().y, city.getTexture().getWidth(), city
						.getTexture().getHeight(), 0, 0, city.getTexture()
						.getWidth(), city.getTexture().getHeight(), false, true);

		if (city.state == City.CITY_STATE_BEEING_DESTRUCTED) {

			Vector2 hitPosition = city.getLastHitPosition();
			int explosionWidth = Assets.cityDestruction1.getRegionWidth();
			int explosionHeight = Assets.cityDestruction1.getRegionHeight();
			batch.draw(Assets.cityDestruction.getKeyFrame(city.stateTime),
					hitPosition.x - explosionWidth / 2, hitPosition.y
							- explosionHeight / 2);
		}
	}

	public void dispose() {
		shapeRenderer.dispose();		
	}
}
