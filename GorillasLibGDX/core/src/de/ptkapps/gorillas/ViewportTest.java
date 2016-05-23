package de.ptkapps.gorillas;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.ptkapps.gorillas.assets.Assets;
import de.ptkapps.gorillas.gameobjects.City;
import de.ptkapps.gorillas.main.Gorillas.ScreenSize;
import de.ptkapps.gorillas.map.MapGenerator;

public class ViewportTest extends ApplicationAdapter {

    private static final int VIRTUAL_WIDTH = 640;
    private static final int VIRTUAL_HEIGHT = 320;

    private OrthographicCamera camera;
    private Viewport viewport;

    private SpriteBatch batch;

    private City city;

    private AssetManager manager;

    @Override
    public void create() {

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

        batch = new SpriteBatch();

        manager = new AssetManager();

        Assets.load(manager, ScreenSize.SMALL);
        manager.finishLoading();
        Assets.finishSetup(manager);

        MapGenerator mapGenerator = new MapGenerator();
        city = mapGenerator.generateMap(VIRTUAL_WIDTH, Math.round(VIRTUAL_HEIGHT * 2 / 3f), 10).getCity();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {

        Gdx.gl.glClearColor(0, 0, (10 * 16 + 8 * 1) / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);

        batch.begin();
        batch.draw(city.getTexture(), city.getPosition().x, city.getPosition().y);
        batch.end();
    }
}
