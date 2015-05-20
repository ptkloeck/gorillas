package de.ptkapps.gorillas.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.utils.ScreenshotFactory;

public abstract class GorillasScreen implements Screen {

	protected Gorillas game;

	protected Stage stage;

	protected OrthographicCamera camera;
	protected Viewport viewport;

	public GorillasScreen(Gorillas game) {

		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gorillas.worldWidth, Gorillas.worldHeight);
		camera.position.set(Gorillas.worldWidth / 2, Gorillas.worldHeight / 2,
				0);
		camera.update();

		viewport = new FillViewport(Gorillas.worldWidth, Gorillas.worldHeight,
				camera);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		stage = new Stage(viewport);
	}

	public abstract void updateTextElements();

	protected abstract void handleBackKey();

	@Override
	public void resize(int width, int height) {

		viewport.update(width, height);
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render(float delta) {
		
		if ((Gdx.input.isKeyJustPressed(Keys.BACK) || Gdx.input
				.isKeyJustPressed(Keys.ESCAPE))) {
			handleBackKey();
		}

		stage.act();
		stage.draw();
		
		if (Gdx.input.isKeyJustPressed(Keys.S)) {
			ScreenshotFactory.saveScreenshot();
		}
	}

	@Override
	public void show() {

		Gdx.input.setInputProcessor(stage);
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
		stage.dispose();
	}
}
