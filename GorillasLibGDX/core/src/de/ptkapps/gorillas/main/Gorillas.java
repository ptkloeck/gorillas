package de.ptkapps.gorillas.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.ptkapps.gorillas.ActionResolver;
import de.ptkapps.gorillas.assets.Assets;
import de.ptkapps.gorillas.options.Options;
import de.ptkapps.gorillas.screens.GameScreen;
import de.ptkapps.gorillas.screens.LoadScreen;
import de.ptkapps.gorillas.screens.MainMenuScreen;
import de.ptkapps.gorillas.screens.OptionsScreen;
import de.ptkapps.gorillas.screens.help.HelpScreen1;
import de.ptkapps.gorillas.screens.help.HelpScreen2;
import de.ptkapps.gorillas.screens.help.HelpScreen3;
import de.ptkapps.gorillas.screens.help.HelpScreen4;
import de.ptkapps.gorillas.screens.help.HelpScreen5;
import de.ptkapps.gorillas.screens.setup.Game1on1SetupScreen;
import de.ptkapps.gorillas.screens.setup.GameAISetupScreen;

public class Gorillas extends Game {

    public static final float VOLUME = 0.7f;

    public static final int worldWidth = 640;
    public static final int worldHeight = 320;

    public int guiWidth;
    public int guiHeight;

    public static float normalLabelHeight;
    public static float scoreLabelWidth;

    public SpriteBatch batch;
    public Skin skin;

    public LoadScreen loadScreen;
    public MainMenuScreen mainMenuScreen;
    public Game1on1SetupScreen game1on1SetupScreen;
    public GameAISetupScreen gameAISetupScreen;
    public GameScreen gameScreen;
    public OptionsScreen optionsScreen;
    public HelpScreen1 helpScreen1;
    public HelpScreen2 helpScreen2;
    public HelpScreen3 helpScreen3;
    public HelpScreen4 helpScreen4;
    public HelpScreen5 helpScreen5;

    public AssetManager manager;

    public enum ScreenSize {
        SMALL, NORMAL, LARGE, XLARGE, XXLARGE
    }

    private ScreenSize screenSize;

    public ActionResolver actionResolver;

    public Gorillas(ActionResolver actionResolver) {
        this.actionResolver = actionResolver;
    }

    @Override
    public void create() {

        // Gdx.app.setLogLevel(Application.LOG_DEBUG);

        batch = new SpriteBatch();

        manager = new AssetManager();

        determineGuiSize();

        manager.load("gui/gui-" + "large" + ".json", Skin.class);

        Assets.load(manager, screenSize);

        loadScreen = new LoadScreen(this);
        this.setScreen(loadScreen);
    }

    private void determineGuiSize() {
        
        guiWidth = worldWidth;
        guiHeight = Math.round(guiWidth * Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth());
    }
    
    @Override
    public void resume() {
        // only the current screen will receive a resume call by the libgdx
        // framework. The city texture of the world of HelpScreen1 is unmanaged
        // and gets lost -> it will be recreated in the resume method of
        // HelpScreen1 (the world of HelpScreen1 is shared by all the other
        // helpscreens).
        if (!getScreen().equals(helpScreen1)) {
            if (helpScreen1 != null) {
                helpScreen1.resume();
            }
        }
        super.resume();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        
        determineGuiSize();
        super.resize(width, height);
    }
    
    @Override
    public void dispose() {
        batch.dispose();
        manager.dispose();
        if (mainMenuScreen != null) {
            mainMenuScreen.dispose();
        }
        if (game1on1SetupScreen != null) {
            game1on1SetupScreen.dispose();
        }
        if (gameAISetupScreen != null) {
            gameAISetupScreen.dispose();
        }
        if (gameScreen != null) {
            gameScreen.dispose();
        }
        if (optionsScreen != null) {
            optionsScreen.dispose();
        }
        if (helpScreen1 != null) {
            helpScreen1.dispose();
        }
    }

    public void finishSetup() {

        long start = System.currentTimeMillis();

        skin = manager.get("gui/gui-" + "large" + ".json", Skin.class);

        Label scoreDummyLabel = new Label("10 > score < 10", skin);
        normalLabelHeight = scoreDummyLabel.getPrefHeight();
        scoreLabelWidth = scoreDummyLabel.getPrefWidth();

        Assets.finishSetup(manager);

        createScreens();

        Gdx.app.debug("Performance", "Finish setup took " + (System.currentTimeMillis() - start) + " ms.");

        if (Options.getInstance().isSoundOn()) {
            Assets.mainTheme.play(Gorillas.VOLUME);
        }

        this.setScreen(mainMenuScreen);
    }

    private void createScreens() {

        game1on1SetupScreen = new Game1on1SetupScreen(this);
        gameAISetupScreen = new GameAISetupScreen(this);
        mainMenuScreen = new MainMenuScreen(this);
        gameScreen = new GameScreen(this);
        optionsScreen = new OptionsScreen(this);
        helpScreen1 = new HelpScreen1(this);
        helpScreen2 = new HelpScreen2(this);
        helpScreen3 = new HelpScreen3(this);
        helpScreen4 = new HelpScreen4(this);
        helpScreen5 = new HelpScreen5(this);
    }

    public void updateTextElements() {

        mainMenuScreen.updateTextElements();
        game1on1SetupScreen.updateTextElements();
        gameAISetupScreen.updateTextElements();
        gameScreen.updateTextElements();
        optionsScreen.updateTextElements();
        helpScreen1.updateTextElements();
        helpScreen2.updateTextElements();
        helpScreen3.updateTextElements();
        helpScreen4.updateTextElements();
        helpScreen5.updateTextElements();
    }
}
