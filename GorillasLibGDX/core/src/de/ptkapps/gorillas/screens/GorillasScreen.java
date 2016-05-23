package de.ptkapps.gorillas.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.utils.ScreenshotFactory;

public abstract class GorillasScreen implements Screen {

    protected Gorillas game;

    protected Stage guiStage;

    protected OrthographicCamera guiCamera;

    public GorillasScreen(Gorillas game) {

        this.game = game;

        guiCamera = new OrthographicCamera();

        Viewport guiViewport = new ExtendViewport(game.guiWidth, game.guiHeight, guiCamera);
        guiStage = new Stage(guiViewport);
    }

    public abstract void updateTextElements();

    protected abstract void handleBackKey();

    @Override
    public void resize(int width, int height) {
        guiStage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {

        if ((Gdx.input.isKeyJustPressed(Keys.BACK) || Gdx.input.isKeyJustPressed(Keys.ESCAPE))) {
            handleBackKey();
        }

        guiStage.getViewport().apply();

        guiStage.act();
        guiStage.draw();

        if (Gdx.input.isKeyJustPressed(Keys.S)) {
            ScreenshotFactory.saveScreenshot();
        }
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(guiStage);
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
        guiStage.dispose();
    }
}
