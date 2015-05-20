package de.ptkapps.gorillas.screens.setup;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.I18NBundle;

import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.options.Options;
import de.ptkapps.gorillas.screens.GorillasScreen;
import de.ptkapps.gorillas.screens.gui.GUIConstants;

public abstract class GameSetupScreen extends GorillasScreen {

	protected Skin skin;

	protected TextButton mainMenuButton;
	protected TextButton startGameButton;

	public GameSetupScreen(Gorillas game) {

		super(game);

		skin = game.skin;

		mainMenuButton = new TextButton("", skin);
		mainMenuButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				backToMainMenu();
			}
		});
		if (!(Gdx.app.getType() == ApplicationType.Android)) {
			stage.addActor(mainMenuButton);
		}

		generateStartGameButton();
	}

	private void generateStartGameButton() {

		startGameButton = new TextButton("", skin);
		startGameButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {

				nameInputFinished();
			}
		});
		startGameButton.getLabel().setAlignment(Align.left);
	}

	protected void handlePossibleEnterPress(TextField textfield,
			InputEvent event) {

		if (Gdx.app.getType() != ApplicationType.Desktop) {
			return;
		}

		if (event.getKeyCode() == Input.Keys.ENTER) {

			String inputText = textfield.getText();

			if (inputText.isEmpty()) {
				return;
			}

			textfield.setText(inputText.substring(0, inputText.length() - 1));
			textfield.setCursorPosition(inputText.length() - 1);
		}
		return;
	}

	protected void backToMainMenu() {
		game.setScreen(game.mainMenuScreen);
	}

	protected abstract void nameInputFinished();

	@Override
	public void updateTextElements() {

		I18NBundle messagesBundle = Options.getInstance()
				.getCurrentMessagesBundle();

		mainMenuButton.setText(messagesBundle.get("backToMainMenu"));
		startGameButton.setText(messagesBundle.get("startGame"));

		layout();
	}

	private void layout() {

		int gap = 10;
		int paneWidth = (int) Gorillas.worldWidth;

		startGameButton.setWidth(startGameButton.getPrefWidth()
				+ GUIConstants.DOS_BUTTON_CORRECT_X);

		mainMenuButton.setWidth(mainMenuButton.getPrefWidth()
				+ GUIConstants.DOS_BUTTON_CORRECT_X);
		mainMenuButton.setHeight(mainMenuButton.getPrefHeight());
		mainMenuButton.setPosition(paneWidth - mainMenuButton.getWidth() - gap,
				gap);
	}

	@Override
	public void handleBackKey() {
		game.setScreen(game.mainMenuScreen);
	}

	@Override
	public void show() {
		// the next two lines fix a bug on HTC Wildfire S with Android 2.3.5
		// In this bug, the keyboard wouldn't show up the first time it was
		// requested by a
		// click on a Textfield. (The following solution seems to be more than
		// crazy...)
		Gdx.input.setOnscreenKeyboardVisible(true);
		Gdx.input.setOnscreenKeyboardVisible(false);

		super.show();
	}

	@Override
	public void resume() {
		// the next two lines fix a bug on HTC Wildfire S with Android 2.3.5
		// In this bug, the keyboard wouldn't show up the first time it was
		// requested by a
		// click on a Textfield. (The following solution seems to be more than
		// crazy...)
		Gdx.input.setOnscreenKeyboardVisible(true);
		Gdx.input.setOnscreenKeyboardVisible(false);

		super.resume();
	}
}
