package de.ptkapps.gorillas.screens;

import java.util.Locale;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import de.ptkapps.gorillas.ai.AI.Difficulty;
import de.ptkapps.gorillas.assets.Assets;
import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.options.Options;
import de.ptkapps.gorillas.screens.gui.GUIConstants;
import de.ptkapps.gorillas.screens.gui.widgets.ValueAdjuster;

public class OptionsScreen extends GorillasScreen {

	private Skin skin;

	private Button soundButton;

	private Button englishButton;
	private Button germanButton;

	private TextButton mainMenuButton;
	private Label gravityLabel;
	private ValueAdjuster gravityAdjuster;
	private Label roundsLabel;
	private ValueAdjuster roundsAdjuster;
	private Label aiDifficulty;
	private Label moderateLabel;
	private CheckBox moderate;
	private Label difficultLabel;
	private CheckBox difficult;
	private Label deathlyLabel;
	private CheckBox deathly;
	private Table table;

	public OptionsScreen(Gorillas game) {

		super(game);

		skin = game.skin;

		ImageButtonStyle soundButtonStyle = new ImageButtonStyle();
		soundButton = new ImageButton(soundButtonStyle);
		soundButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {

				boolean soundOn = Options.getInstance().isSoundOn();
				Options.getInstance().setSoundOn(!soundOn);
				if (soundOn) {
					Assets.mainTheme.stop();
				}

				ButtonStyle soundButtonStyle = soundButton.getStyle();
				soundButtonStyle.up = new TextureRegionDrawable(
						soundOn ? Assets.soundOff : Assets.soundOn);
				soundButton.setStyle(soundButtonStyle);
			}
		});
		stage.addActor(soundButton);
		
		mainMenuButton = new TextButton("", skin);
		mainMenuButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				backToMainMenu();
			}
		});
		if (!(Gdx.app.getType() == ApplicationType.Android)) {
//			stage.addActor(mainMenuButton);
		}

		ButtonStyle britishButtonStyle = new ButtonStyle();
		britishButtonStyle.up = new TextureRegionDrawable(Assets.britishFlag);
		englishButton = new Button(britishButtonStyle);
		englishButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {

				Options.getInstance().setCurrentLocale(new Locale("en", "US"));
				updateGameTextElements();
			}
		});
		stage.addActor(englishButton);

		ButtonStyle germanButtonStyle = new ButtonStyle();
		germanButtonStyle.up = new TextureRegionDrawable(Assets.germanFlag);
		germanButton = new Button(germanButtonStyle);
		germanButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {

				Options.getInstance().setCurrentLocale(new Locale("de", "DE"));

				updateGameTextElements();
			}
		});
		stage.addActor(germanButton);

		gravityLabel = new Label("", skin);
		gravityLabel.setColor(Color.BLACK);
		int currentGravity = Options.getInstance().getGravity();
		gravityAdjuster = new ValueAdjuster(skin, currentGravity, 1, 20);

		roundsLabel = new Label("", skin);
		roundsLabel.setColor(Color.BLACK);

		int currentRounds = Options.getInstance().getRounds();
		roundsAdjuster = new ValueAdjuster(skin, currentRounds, 1, 10);

		aiDifficulty = new Label("", skin);
		aiDifficulty.setColor(Color.BLACK);

		moderateLabel = new Label("", skin);
		moderateLabel.setColor(Color.BLACK);
		difficultLabel = new Label("", skin);
		difficultLabel.setColor(Color.BLACK);
		deathlyLabel = new Label("", skin);
		deathlyLabel.setColor(Color.BLACK);

		generateCheckBoxes();

		table = new Table(skin);

		table.setBackground("menuBackground");

		table.add(roundsLabel).width(Gorillas.worldWidth / 2f)
				.colspan(6);
		table.row();
		table.add(roundsAdjuster).padBottom(Gorillas.normalLabelHeight).colspan(6);
		table.row();
		table.add(gravityLabel).colspan(6);
		table.row();
		table.add(gravityAdjuster).padBottom(Gorillas.normalLabelHeight).colspan(6);
		table.row();
		table.add(aiDifficulty).colspan(6).padLeft(2).padRight(2);
		table.row();
		table.add(moderateLabel).padTop(5).padLeft(2);
		table.add(moderate).padTop(5);
		table.add(difficultLabel).padTop(5);
		table.add(difficult).padTop(5);
		table.add(deathlyLabel).padTop(5);
		table.add(deathly).padTop(5).padRight(2);

		stage.addActor(table);

		updateTextElements();
	}

	private void generateCheckBoxes() {

		moderate = new CheckBox("", skin);
		moderate.setDisabled(true);
		difficult = new CheckBox("", skin);
		difficult.setDisabled(true);
		deathly = new CheckBox("", skin);
		deathly.setDisabled(true);

		// do checking and unchecking manually, only one checkbox should be
		// checked at a time. Trying to implement this feature with
		// ChangeListeners or individual InputListeners failed, cause they
		// always
		// trigger each other.
		stage.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				float moderateWidth = moderate.getWidth();
				float moderateHeight = moderate.getHeight();

				float difficultWidth = difficult.getWidth();
				float difficultHeight = difficult.getHeight();

				float deathlyWidth = deathly.getWidth();
				float deathlyHeight = deathly.getHeight();

				float moderateX = moderate.getX() + table.getX();
				float moderateY = moderate.getY() + table.getY();

				float difficultX = difficult.getX() + table.getX();
				float difficultY = difficult.getY() + table.getY();

				float deathlyX = deathly.getX() + table.getX();
				float deathlyY = deathly.getY() + table.getY();

				float moderateEndX = moderateX + moderateWidth;
				float moderateEndY = moderateY + moderateHeight;

				float difficultEndX = difficultX + difficultWidth;
				float difficultEndY = difficultY + difficultHeight;

				float deathlyEndX = deathlyX + deathlyWidth;
				float deathlyEndY = deathlyY + deathlyHeight;

				if (x >= moderateX && x <= moderateEndX && y >= moderateY
						&& y <= moderateEndY) {

					difficult.setChecked(false);
					moderate.setChecked(true);
					deathly.setChecked(false);
				}

				if (x >= difficultX && x <= difficultEndX && y >= difficultY
						&& y <= difficultEndY) {

					difficult.setChecked(true);
					moderate.setChecked(false);
					deathly.setChecked(false);
				}

				if (x >= deathlyX && x <= deathlyEndX && y >= deathlyY
						&& y <= deathlyEndY) {

					difficult.setChecked(false);
					moderate.setChecked(false);
					deathly.setChecked(true);
				}

				return true;
			}
		});
	}

	private void updateGameTextElements() {

		game.updateTextElements();
	}

	private void backToMainMenu() {

		game.setScreen(game.mainMenuScreen);
	}

	private void layout() {

		int gap = 10;
		int paneWidth = (int) Gorillas.worldWidth;
		int paneHeight = (int) Gorillas.worldHeight;

		roundsLabel.setWrap(true);
		roundsLabel.setAlignment(Align.center);

		mainMenuButton.setWidth(mainMenuButton.getPrefWidth()
				+ GUIConstants.DOS_BUTTON_CORRECT_X);
		mainMenuButton.setHeight(mainMenuButton.getPrefHeight());
		mainMenuButton.setPosition(paneWidth - mainMenuButton.getWidth() - gap,
				gap);

		germanButton.pack();
		germanButton.setPosition(paneWidth - germanButton.getWidth() - gap,
				paneHeight - germanButton.getHeight() - gap);
		englishButton.pack();
		englishButton.setPosition(paneWidth - germanButton.getWidth() - gap
				- gap - englishButton.getWidth(),
				paneHeight - englishButton.getHeight() - gap);

		soundButton.setSize(soundButton.getPrefWidth(),
				soundButton.getPrefHeight());
		soundButton
				.setPosition(gap, paneHeight - soundButton.getHeight() - gap);

		table.pack();
		table.setPosition((int) (paneWidth / 2 - table.getPrefWidth() / 2),
				(int) (paneHeight / 2 - table.getPrefHeight() / 2));
	}

	@Override
	public void updateTextElements() {

		I18NBundle messagesBundle = Options.getInstance()
				.getCurrentMessagesBundle();

		mainMenuButton.setText(messagesBundle.get("backToMainMenu"));
		gravityLabel.setText(messagesBundle.get("chooseGravity"));
		roundsLabel.setText(messagesBundle.get("chooseRounds"));
		aiDifficulty.setText(messagesBundle.get("aiDifficulty"));
		moderateLabel.setText(messagesBundle.get("moderate"));
		difficultLabel.setText(messagesBundle.get("difficult"));
		deathlyLabel.setText(messagesBundle.get("deathly"));

		layout();
	}

	@Override
	protected void handleBackKey() {

		game.setScreen(game.mainMenuScreen);
	}

	@Override
	public void show() {

		super.show();

		Difficulty difficulty = Options.getInstance().getDifficulty();
		if (difficulty.equals(Difficulty.MODERATE)) {
			moderate.setChecked(true);
			difficult.setChecked(false);
			deathly.setChecked(false);
		} else if (difficulty.equals(Difficulty.DIFFICULT)) {
			moderate.setChecked(false);
			difficult.setChecked(true);
			deathly.setChecked(false);
		} else {
			moderate.setChecked(false);
			difficult.setChecked(false);
			deathly.setChecked(true);
		}

		ButtonStyle soundButtonStyle = soundButton.getStyle();
		soundButtonStyle.up = new TextureRegionDrawable(Options.getInstance()
				.isSoundOn() ? Assets.soundOn : Assets.soundOff);
		soundButton.setStyle(soundButtonStyle);

		layout();
	}

	@Override
	public void hide() {

		Options.getInstance().setGravity(gravityAdjuster.getValue());
		Options.getInstance().setRounds(roundsAdjuster.getValue());

		Difficulty difficulty;
		if (moderate.isChecked()) {
			difficulty = Difficulty.MODERATE;
		} else if (difficult.isChecked()) {
			difficulty = Difficulty.DIFFICULT;
		} else {
			difficulty = Difficulty.DEATHLY;
		}
		Options.getInstance().setDifficulty(difficulty);
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, (10 * 16 + 8 * 1) / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render(delta);
	}
}
