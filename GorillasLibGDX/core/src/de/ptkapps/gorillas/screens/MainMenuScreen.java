package de.ptkapps.gorillas.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.I18NBundle;

import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.options.Options;
import de.ptkapps.gorillas.screens.gui.GUIConstants;

public class MainMenuScreen extends GorillasScreen {

    private TextButton start1on1GameButton;
    private TextButton startAIGameButton;
    private TextButton achievementsButton;
    private TextButton settingsButton;
    private TextButton helpButton;
    private TextButton quitGameButton;
    private Table table;

    private long enterTime;

    public MainMenuScreen(Gorillas game) {

        super(game);

        Skin skin = game.skin;

        start1on1GameButton = new TextButton("", skin);
        start1on1GameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                start1on1Game();
            }
        });

        startAIGameButton = new TextButton("", skin);
        startAIGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startAIGame();
            }
        });

        achievementsButton = new TextButton("", skin);
        achievementsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showAchievements();
            }
        });

        settingsButton = new TextButton("", skin);
        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toOptionsScreen();
            }
        });

        helpButton = new TextButton("", skin);
        helpButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toHelpScreen();
            }
        });

        quitGameButton = new TextButton("", skin);
        quitGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        if (!(Gdx.app.getType() == ApplicationType.Android)) {
            guiStage.addActor(quitGameButton);
        }

        table = new Table(skin);
        table.setBackground("menuBackground");

        table.add(startAIGameButton).pad(5);
        table.row();
        table.add(start1on1GameButton).pad(5);
        table.row();
        table.add(achievementsButton).pad(5);
        table.row();
        table.add(settingsButton).pad(5);
        table.row();
        table.add(helpButton).pad(5);

        guiStage.addActor(table);

        updateTextElements();
    }

    private void start1on1Game() {
        game.setScreen(game.game1on1SetupScreen);
    }

    private void startAIGame() {
        game.setScreen(game.gameAISetupScreen);
    }

    private void showAchievements() {
        if (game.actionResolver.getSignedInGPGS()) {
            game.actionResolver.getAchievementsGPGS();
        } else {
            game.actionResolver.loginGPGS();
        }
    }

    private void toOptionsScreen() {
        game.setScreen(game.optionsScreen);
    }

    private void toHelpScreen() {
        game.setScreen(game.helpScreen1);
    }

    @Override
    public void updateTextElements() {

        I18NBundle messagesBundle = Options.getInstance().getCurrentMessagesBundle();

        start1on1GameButton.setText(messagesBundle.get("startGame1on1"));
        startAIGameButton.setText(messagesBundle.get("startAIGame"));
        achievementsButton.setText(messagesBundle.get("achievements"));
        settingsButton.setText(messagesBundle.get("options"));
        quitGameButton.setText(messagesBundle.get("quitApp"));
        helpButton.setText(messagesBundle.get("help"));

        layout();
    }

    @Override
    public void show() {

        Gdx.input.setCatchBackKey(true);

        super.show();

        enterTime = System.currentTimeMillis();

        resize((int) Gorillas.worldWidth, (int) Gorillas.worldHeight);
    }

    private void layout() {

        int gap = 10;

        float start1on1ButtonWidth = start1on1GameButton.getPrefWidth();
        float startAIButtonWidth = startAIGameButton.getPrefWidth();
        float achievementsButtonWidth = achievementsButton.getPrefWidth();
        float settingsButtonWidth = settingsButton.getPrefWidth();
        float helpButtonWidth = helpButton.getPrefWidth();

        float maxWidth = Math.max(Math.max(Math.max(startAIButtonWidth, start1on1ButtonWidth),
                Math.max(settingsButtonWidth, helpButtonWidth)), achievementsButtonWidth);

        table.getCell(start1on1GameButton).width(maxWidth + GUIConstants.DOS_BUTTON_CORRECT_X);
        table.getCell(startAIGameButton).width(maxWidth + GUIConstants.DOS_BUTTON_CORRECT_X);
        table.getCell(achievementsButton).width(maxWidth + GUIConstants.DOS_BUTTON_CORRECT_X);
        table.getCell(settingsButton).width(maxWidth + GUIConstants.DOS_BUTTON_CORRECT_X);
        table.getCell(helpButton).width(maxWidth + GUIConstants.DOS_BUTTON_CORRECT_X);

        table.pack();

        float tableWidth = table.getPrefWidth();
        float tableHeight = table.getPrefHeight();

        table.setPosition((int) (Gorillas.worldWidth / 2 - tableWidth / 2), (int) (Gorillas.worldHeight / 2 - tableHeight / 2));

        quitGameButton.setWidth(quitGameButton.getPrefWidth() + GUIConstants.DOS_BUTTON_CORRECT_X);
        quitGameButton.setHeight(quitGameButton.getPrefHeight());
        quitGameButton.setPosition(Gorillas.worldWidth - quitGameButton.getWidth() - gap, gap);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, (10 * 16 + 8 * 1) / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render(delta);
    }

    @Override
    protected void handleBackKey() {

        // hack to prevent, that a press on the back key or a hit on escape
        // terminates the app in spite those events were handled in another
        // screen before
        long currentTime = System.currentTimeMillis();
        if (currentTime >= enterTime + 500) {
            Gdx.app.exit();
        }
    }
}
