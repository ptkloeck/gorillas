package de.ptkapps.gorillas.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.ptkapps.gorillas.gameobjects.City;
import de.ptkapps.gorillas.gameobjects.Gorilla;
import de.ptkapps.gorillas.gameobjects.Shot.Direction;
import de.ptkapps.gorillas.gameobjects.factory.GorillaFactory;
import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.map.Map;
import de.ptkapps.gorillas.map.MapGenerator;

public class TestScreen implements Screen {

	private SpriteBatch batch;
	private OrthographicCamera camera;

	private Viewport viewport;

	private ShapeRenderer shapeRenderer;

	static final int WORLD_WIDTH = 800;
	static final int WORLD_HEIGHT = 480;

	private Stage stage;

	private Label score;

	private City city;
	private Gorilla gorilla;

	public TestScreen(Gorillas game) {

		batch = game.batch;

		camera = new OrthographicCamera();
		camera.setToOrtho(true, WORLD_WIDTH, WORLD_HEIGHT);
		camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
		camera.update();

		viewport = new FillViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

		shapeRenderer = new ShapeRenderer();

		stage = new Stage(viewport);

		score = new Label("", game.skin);
		score.setText("0 > Score < 0");
		score.setPosition(WORLD_WIDTH / 2 - score.getPrefWidth() / 2, 47);
		stage.addActor(score);
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, (10 * 16 + 8 * 1) / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.projection);
		batch.setTransformMatrix(camera.view);
		batch.begin();
		batch.draw(city.getTexture(), city.getPosition().x,
				city.getPosition().y, city.getTexture().getWidth(), city
						.getTexture().getHeight(), 0, 0, city.getTexture()
						.getWidth(), city.getTexture().getHeight(), false, true);
		Texture gorillaTexture = gorilla.getTexture();
		batch.draw(gorillaTexture, gorilla.getPosition().x,
				gorilla.getPosition().y, gorillaTexture.getWidth(),
				gorillaTexture.getHeight(), 0, 0, gorillaTexture.getWidth(),
				gorillaTexture.getHeight(), false, true);
		batch.end();

		shapeRenderer.setProjectionMatrix(camera.combined);
		// shapeRenderer.setTransformMatrix(camera.view);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.rect(20, 20, 20, 20, 30, 5, 1, 1, -45);
		shapeRenderer.end();

		camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
		stage.act();
		stage.draw();
		camera.setToOrtho(true, WORLD_WIDTH, WORLD_HEIGHT);
	}

	@Override
	public void resize(int width, int height) {

		// float aspectY = height / (float) WORLD_HEIGHT;
		//
		// System.out.println(aspectY);
		//
		// city.setPosition(new Vector2(0, WORLD_HEIGHT - city.getHeight() *
		// aspectY));

		viewport.update(width, height);
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {

		MapGenerator mg = new MapGenerator();
		Map map = mg.generateMap(WORLD_WIDTH, WORLD_HEIGHT * 2 / 3 - 20,
				WORLD_HEIGHT / 3);

		city = map.getCity();

		Vector2 cityLeftGorillaPos = city.getLeftGorillaCoordinate();
		float gorillaLeftPosY = city.getPosition().y
				+ (city.getHeight() - cityLeftGorillaPos.y);
		Vector2 gorillaLeftPosition = new Vector2(cityLeftGorillaPos.x,
				gorillaLeftPosY);
		gorilla = GorillaFactory.createGorilla(gorillaLeftPosition,
				Direction.LEFT_TO_RIGHT);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
