package de.ptkapps.gorillas.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import de.ptkapps.gorillas.main.Gorillas;

public class LoadScreen implements Screen {

	/**
	 * small delay after the manager finishes loading, to show a
	 * "100% progress bar"
	 */
	private static final float END_DELAY_TIME = 0.02f;

	private boolean finishedLoading;

	/**
	 * total time the loading screen was active and the manager was still
	 * loading
	 */
	private float finishTime;

	/**
	 * total time the loading screen was active
	 */
	private float loadScreenTime;

	private Gorillas game;
	private AssetManager manager;

	private ShapeRenderer shapeRenderer;

	public LoadScreen(Gorillas game) {
		this.game = game;
		manager = game.manager;

		finishedLoading = false;
		loadScreenTime = 0;

		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {

		loadScreenTime += delta;

		Gdx.gl.glClearColor(0, 0, (10 * 16 + 8 * 1) / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(new Color(Color.BLACK));
		shapeRenderer.rect(Gorillas.worldWidth / 4,
				Gorillas.worldHeight / 2 - 15, Gorillas.worldWidth / 2, 30);
		shapeRenderer.setColor(new Color(Color.DARK_GRAY));
		shapeRenderer.rect(Gorillas.worldWidth / 4,
				Gorillas.worldHeight / 2 - 15, Gorillas.worldWidth / 2
						* manager.getProgress(), 30);
		shapeRenderer.end();

		if (finishedLoading) {

			if (loadScreenTime > finishTime + END_DELAY_TIME) {
				game.finishSetup();
				Gdx.app.debug("Performance", "Loading Screen was active for "
						+ loadScreenTime * 1000 + " ms.");
			}
		} else {

			if (manager.update()) {
				finishedLoading = true;
				finishTime = loadScreenTime;
			}
		}
	}

	@Override
	public void resize(int width, int height) {
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
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
	}
}
