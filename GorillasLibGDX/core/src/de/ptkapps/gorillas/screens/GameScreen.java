package de.ptkapps.gorillas.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.ptkapps.gorillas.ai.AI;
import de.ptkapps.gorillas.assets.Assets;
import de.ptkapps.gorillas.gameobjects.Gorilla;
import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.mocking.Mocker;
import de.ptkapps.gorillas.options.Options;
import de.ptkapps.gorillas.parameterization.Parameterization;
import de.ptkapps.gorillas.screens.gui.GameGUI;
import de.ptkapps.gorillas.world.World;
import de.ptkapps.gorillas.world.WorldRenderer;

public class GameScreen extends GorillasScreen {

    // private FPSLogger fpsLogger;

    private World world;
    private WorldRenderer worldRenderer;

    private GameGUI gameGUI;

    private AI ai;

    private Mocker mocker;

    // --- some timing variables and constants ---

    /**
     * the time passed since the start of the current level
     */
    private float levelPlayTime;

    private static final float AI_LEVEL_THINK_TIME = 1.0f;

    /**
     * the time passed since the banana left the screen or the time passed since the shot hit the
     * city
     */
    private float timeShotTerminated;

    private static final float MESSAGE_SHOW_TIME = 2.4f;

    // --- the game types (match against computer, or 1 on 1 match) ---

    private static final int GAME_TYPE_AI = 0;
    private static final int GAME_TYPE_1_ON_1 = 1;

    private int gameType;

    // --- the game states ---

    private static final int GAME_PARAMETER_INPUT = 0;
    private static final int GAME_BANANA_FLYING = 1;
    private static final int GAME_CITY_DESTRUCTION = 2;
    private static final int GAME_GORILLA_1_HIT = 3;
    private static final int GAME_GORILLA_2_HIT = 4;
    private static final int GAME_LEFT_SCREEN = 5;
    private static final int GAME_PAUSED = 6;

    private int state;

    /**
     * the state in which pause was pressed has to be remembered to be able to resume the game
     */
    private int stateBeforePaused;

    // --- Whose turn is it? (player 1 always on the left, player 2 on the
    // right) ---

    public static final int TURN_PLAYER_1 = 1;
    public static final int TURN_PLAYER_2 = 2;

    private int whoseTurn;

    // --- Control types ---

    public enum ControlType {
        CLASSIC, MODERN
    }

    private ControlType controlPlayer1;
    private ControlType controlPlayer2;

    public ControlType getControlPlayer1() {
        return controlPlayer1;
    }

    public GameScreen(Gorillas game) {

        super(game);

        addModernControlListener();

        // fpsLogger = new FPSLogger();

        world = new World(game.guiWidth, game.guiHeight);
        worldRenderer = new WorldRenderer(game.batch, world);

        ai = new AI(world, Options.getInstance().getDifficulty());

        gameGUI = new GameGUI(this, guiStage, game.skin);

        mocker = new Mocker(gameGUI);

        updateTextElements();
    }

    public void setPlayerNames(String namePlayer1, String namePlayer2) {

        world.namePlayer1 = namePlayer1;
        world.namePlayer2 = namePlayer2;
        gameGUI.setPlayerNames(namePlayer1, namePlayer2);
    }

    public void setAIGame() {
        gameType = GAME_TYPE_AI;
        controlPlayer2 = ControlType.CLASSIC;
    }

    public void set1on1Game() {
        gameType = GAME_TYPE_1_ON_1;
    }

    public void setPlayer1Control(ControlType control) {
        controlPlayer1 = control;
    }

    public void setPlayer2Control(ControlType control) {
        controlPlayer2 = control;
    }

    private void addModernControlListener() {

        guiStage.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return handleTouchDown(x, y);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

                handleTouchDragged(x, y);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                handleTouchUp();
            }
        });
    }

    private boolean handleTouchDown(float x, float y) {

        if (state == GAME_PARAMETER_INPUT) {

            boolean player1Turn = (whoseTurn == TURN_PLAYER_1);
            boolean player1Modern = controlPlayer1.equals(ControlType.MODERN);

            boolean player2Turn = (whoseTurn == TURN_PLAYER_2);
            boolean player2Modern = controlPlayer2.equals(ControlType.MODERN);

            if ((player1Turn && player1Modern) | (player2Turn && player2Modern)) {

                if (player1Turn) {
                    gameGUI.hidePlayer1Turn();
                } else {
                    gameGUI.hidePlayer2Turn();
                }

                Parameterization param = whoseTurn == TURN_PLAYER_1 ? world.paramPlayer1 : world.paramPlayer2;

                param.setStartPos(new Vector2(x, y));
                param.setEndPos(new Vector2(x, y));

                return true;
            }
        }
        return false;
    }

    private void handleTouchDragged(float x, float y) {

        if (state == GAME_PARAMETER_INPUT) {

            boolean player1Turn = (whoseTurn == TURN_PLAYER_1);
            boolean player1Modern = controlPlayer1.equals(ControlType.MODERN);

            boolean player2Turn = (whoseTurn == TURN_PLAYER_2);
            boolean player2Modern = controlPlayer2.equals(ControlType.MODERN);

            if ((player1Turn && player1Modern) | (player2Turn && player2Modern)) {

                Parameterization param = whoseTurn == TURN_PLAYER_1 ? world.paramPlayer1 : world.paramPlayer2;
                param.setEndPos(new Vector2(x, y));
            }
        }
    }

    private void handleTouchUp() {

        Parameterization param = whoseTurn == TURN_PLAYER_1 ? world.paramPlayer1 : world.paramPlayer2;
        if (param.minLengthReached()) {
            param.setBeingAdjusted(false);
            param.setShowOld(false);
            generateShot(param.getAngle(), param.getVelocity());
            param.saveToOldParameterization();
        }
    }

    private void update(float delta) {

        if (state != GAME_PAUSED && state != GAME_BANANA_FLYING) {
            world.update(delta);
        }

        levelPlayTime += delta;

        switch (state) {
        case GAME_PARAMETER_INPUT:
            updateParameterInput(delta);
            break;

        case GAME_BANANA_FLYING:
            updateBananaFlying(delta);
            break;

        case GAME_CITY_DESTRUCTION:
            updateCityDestruction(delta);
            break;

        case GAME_GORILLA_1_HIT:
            updateGorilla1Hit();
            break;

        case GAME_GORILLA_2_HIT:
            updateGorilla2Hit();
            break;

        case GAME_LEFT_SCREEN:
            updateLeftScreen(delta);
            break;

        case GAME_PAUSED:
            updateGamePaused();
            break;
        }
    }

    private void updateParameterInput(float delta) {

        boolean player1Turn = (whoseTurn == TURN_PLAYER_1);
        boolean player1Modern = controlPlayer1.equals(ControlType.MODERN);

        boolean player2Turn = (whoseTurn == TURN_PLAYER_2);
        boolean player2Modern = controlPlayer2.equals(ControlType.MODERN);

        if (player1Turn && player1Modern) {

            // updateParameterInput will be called repeatedly as long as the
            // shot is not fired. This if statement makes sure the statements it
            // contains are only called once and at the beginning of the
            // parameterInputState.
            if (!world.paramPlayer1.isShowOld()) {

                // show the old parameterization
                world.paramPlayer1.setShowOld(true);

                // if 0 shots have been fired before, there will be no old
                // parameterization to show. Thus, indicate whose turn it is
                // with an extra label.
                if (world.gorilla1.numShotsFired == 0) {
                    gameGUI.showPlayer1Turn();
                }
            }

        } else if (player2Turn && player2Modern) {

            // same for player 2 -> Additionally do not show the turn label if
            // it is the computer's turn.
            if (!world.paramPlayer2.isShowOld()) {

                world.paramPlayer2.setShowOld(true);

                if (world.gorilla2.numShotsFired == 0 && !(gameType == GAME_TYPE_AI)) {
                    gameGUI.showPlayer2Turn();
                }
            }
        }

        // if it is the computers turn and after a short pause, let the ai
        // calculate the shot parameters and do the shot
        if (gameType == GAME_TYPE_AI && player2Turn && levelPlayTime >= AI_LEVEL_THINK_TIME) {

            ai.determineShot();

            generateShot(ai.getAngle(), ai.getVelocity());
        }
    }

    /**
     * update method for the banana flying gamestate
     * 
     * @param delta
     *            the delta time of the libgdx update
     */
    private void updateBananaFlying(float delta) {

        // make small time steps, which approximately cover every pixel position
        // of the banana
        for (float time = 0.0026f; time < delta; time += 0.0026f) {

            // if the step update returned true, the shot finished (hit city
            // or gorilla or left screen). Stop stepping then.
            if (stepBananaFlying(0.0026f)) {
                break;
            }
        }
    }

    /**
     * @param delta
     *            the time step size
     * @return true if the shot terminated (hit city, a gorilla, or left the screen), otherwise
     *         false
     */
    private boolean stepBananaFlying(float delta) {

        world.update(delta);

        world.checkThrowingCompleted(whoseTurn);

        world.checkSunHit();

        if (world.checkCityCollision()) {

            if (Options.getInstance().isSoundOn()) {
                Assets.cityHit.play(Gorillas.VOLUME);
            }
            Gorilla aim = whoseTurn == TURN_PLAYER_1 ? world.gorilla2 : world.gorilla1;
            if (!(whoseTurn == TURN_PLAYER_2 && gameType == GAME_TYPE_AI)) {
                mocker.cityHit(world.currentShot, aim);
            }

            world.currentShot = null;
            world.city.startDestruction();
            world.sun.smile();
            state = GAME_CITY_DESTRUCTION;
            return true;

        } else if (world.checkGorillaHit(world.gorilla1)) {

            if (Options.getInstance().isSoundOn()) {
                Assets.gorillaExplode.play(Gorillas.VOLUME);
                Assets.victoryDance.play(Gorillas.VOLUME);
            }

            Gorilla winner = world.gorilla2;
            Gorilla thrower = whoseTurn == TURN_PLAYER_1 ? world.gorilla1 : world.gorilla2;
            if (!(whoseTurn == TURN_PLAYER_2 && gameType == GAME_TYPE_AI)) {
                mocker.gorillaHit(winner, thrower);
            }

            world.sun.smile();
            state = GAME_GORILLA_1_HIT;
            return true;

        } else if (world.checkGorillaHit(world.gorilla2)) {

            if (Options.getInstance().isSoundOn()) {
                Assets.gorillaExplode.play(Gorillas.VOLUME);
                Assets.victoryDance.play(Gorillas.VOLUME);
            }

            Gorilla winner = world.gorilla1;
            Gorilla thrower = whoseTurn == TURN_PLAYER_1 ? world.gorilla1 : world.gorilla2;
            if (!(whoseTurn == TURN_PLAYER_2 && gameType == GAME_TYPE_AI)) {
                mocker.gorillaHit(winner, thrower);
            }

            world.sun.smile();
            state = GAME_GORILLA_2_HIT;
            return true;

        } else if (world.checkLeaveScreen()) {

            if (!(whoseTurn == TURN_PLAYER_2 && gameType == GAME_TYPE_AI)) {
                mocker.shotLeftScreen(world.currentShot);
            }
            world.currentShot = null;
            world.sun.smile();
            state = GAME_LEFT_SCREEN;
            return true;
        }

        return false;
    }

    private void updateCityDestruction(float delta) {

        timeShotTerminated += delta;

        if (world.cityDestructionCompleted() && timeShotTerminated >= MESSAGE_SHOW_TIME) {
            timeShotTerminated = 0;
            switchTurns();
            state = GAME_PARAMETER_INPUT;
            gameGUI.resetMessage();
        }
    }

    private void updateGorilla1Hit() {

        if (world.checkCheeringCompleted(world.gorilla2)) {
            world.scorePlayer2++;
            if (world.scorePlayer2 == Options.getInstance().getRounds()) {

                gameGUI.showWinDialog(world.namePlayer2);
                gameGUI.updateScore(world.scorePlayer1, world.scorePlayer2);

                state = GAME_PAUSED;
            } else {
                generateLevel();
            }
        }
    }

    private void updateGorilla2Hit() {

        if (world.checkCheeringCompleted(world.gorilla1)) {

            world.scorePlayer1++;

            if (world.scorePlayer1 == Options.getInstance().getRounds()) {

                gameGUI.showWinDialog(world.namePlayer1);
                gameGUI.updateScore(world.scorePlayer1, world.scorePlayer2);

                state = GAME_PAUSED;
            } else {
                generateLevel();
            }
        }
    }

    private void updateLeftScreen(float delta) {

        timeShotTerminated += delta;

        if (timeShotTerminated >= MESSAGE_SHOW_TIME) {
            state = GAME_PARAMETER_INPUT;
            timeShotTerminated = 0;
            gameGUI.resetMessage();
            switchTurns();
        }
    }

    private void updateGamePaused() {
    }

    private void generateLevel() {

        gameGUI.initLevel();
        gameGUI.updateScore(world.scorePlayer1, world.scorePlayer2);
        gameGUI.resetMessage();

        world.generateLevel();

        ai.nextLevel();

        state = GAME_PARAMETER_INPUT;

        switchTurns();

        levelPlayTime = 0;
    }

    private void switchTurns() {

        if (whoseTurn == TURN_PLAYER_1) {
            whoseTurn = TURN_PLAYER_2;
            gameGUI.hidePlayer1Widget();

            // if match against computer -> computer does not need widget
            // if player2 controls with touch or click -> no widget
            if (gameType == GAME_TYPE_1_ON_1 && controlPlayer2.equals(ControlType.CLASSIC)) {
                gameGUI.showPlayer2Widget();
            }
        } else {
            whoseTurn = TURN_PLAYER_1;

            if (controlPlayer1.equals(ControlType.CLASSIC)) {
                gameGUI.showPlayer1Widget();
            }
            gameGUI.hidePlayer2Widget();
        }
    }

    public void generateShot(int angle, int velocity) {

        if (state == GAME_PARAMETER_INPUT) {

            if (Options.getInstance().isSoundOn()) {
                Assets.throwStart.play(Gorillas.VOLUME);
            }

            Gorilla shooter;
            if (whoseTurn == TURN_PLAYER_1) {
                shooter = world.gorilla1;
                gameGUI.hidePlayer1Widget();
            } else {
                shooter = world.gorilla2;
                gameGUI.hidePlayer2Widget();
            }

            if (!(whoseTurn == TURN_PLAYER_2 && gameType == GAME_TYPE_AI)) {
                // scale down velocity for small screens
                if (Gorillas.worldWidth / 800 < 1) {
                    velocity = (int) (velocity * Gorillas.worldWidth / 800);
                }
            }

            world.generateShot(shooter, angle, velocity);
            state = GAME_BANANA_FLYING;
        }
    }

    public void quitMatch() {
        game.setScreen(game.mainMenuScreen);
    }

    public void endPause() {
        state = stateBeforePaused;
    }

    @Override
    public void updateTextElements() {
        gameGUI.updateTextElements();
    }

    @Override
    protected void handleBackKey() {

        if ((Gdx.input.isKeyJustPressed(Keys.BACK) || Gdx.input.isKeyJustPressed(Keys.ESCAPE))
                && state != GAME_PAUSED) {

            stateBeforePaused = state;
            state = GAME_PAUSED;

            gameGUI.showQuitDialog();
        }
    }

    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width, height);

        super.resize(width, height);
    }

    @Override
    public void render(float delta) {

        // fpsLogger.log();

        update(delta);

        if (delta > 0.02f) {
            Gdx.app.debug("Performance", "The frame took longer than 20 ms -> " + delta);
        }

        Gdx.gl.glClearColor(0, 0, (10 * 16 + 8 * 1) / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        worldRenderer.render();

        super.render(delta);
    }

    @Override
    public void show() {

        Assets.mainTheme.stop();

        super.show();

        if (Options.getInstance().isSoundOn()) {
            Assets.gameStart.play(Gorillas.VOLUME);
        }

        world.initGame();

        state = GAME_PARAMETER_INPUT;
        whoseTurn = TURN_PLAYER_1;

        gameGUI.initGame();

        ai.nextLevel();
        ai.setDifficulty(Options.getInstance().getDifficulty());
    }

    @Override
    public void hide() {
        world.paramPlayer1.reset();
        world.paramPlayer2.reset();
        world.currentShot = null;
    }

    @Override
    public void resume() {
        world.resume();
        super.resume();
    }

    @Override
    public void dispose() {
        world.dispose();
        worldRenderer.dispose();
        super.dispose();
    }
}
