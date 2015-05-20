package de.ptkapps.gorillas.screens.gui;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.I18NBundle;

import de.ptkapps.gorillas.assets.Assets;
import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.options.Options;
import de.ptkapps.gorillas.screens.GameScreen;
import de.ptkapps.gorillas.screens.GameScreen.ControlType;
import de.ptkapps.gorillas.screens.gui.widgets.AndroidPlayerWidget;
import de.ptkapps.gorillas.screens.gui.widgets.DesktopHtmlPlayerWidget;
import de.ptkapps.gorillas.screens.gui.widgets.PlayerWidget;
import de.ptkapps.gorillas.screens.gui.widgets.PlayerWidget.Position;

public class GameGUI {

	private GameScreen gameScreen;

	private Stage stage;

	private Skin skin;

	private PlayerWidget player1Widget;
	private PlayerWidget player2Widget;

	private Label player1NameLabel;
	private Label player2NameLabel;

	private Label player1TurnLabel;
	private Label player2TurnLabel;

	private Label messageLabel;

	private Label score;

	private Dialog quitDialog;
	private Dialog winDialog;

	public GameGUI(GameScreen gameScreen, Stage stage, Skin skin) {

		this.gameScreen = gameScreen;
		this.stage = stage;
		this.skin = skin;

		ApplicationType appType = Gdx.app.getType();
		appType = ApplicationType.Android;
		if (appType == ApplicationType.Desktop) {
			player1Widget = new DesktopHtmlPlayerWidget(stage, gameScreen, skin);
			player2Widget = new DesktopHtmlPlayerWidget(stage, gameScreen, skin);
		} else if (appType == ApplicationType.Android) {
			player1Widget = new AndroidPlayerWidget(stage, gameScreen, skin);
			player2Widget = new AndroidPlayerWidget(stage, gameScreen, skin);
		}

		player1NameLabel = new Label("", skin);
		player1NameLabel.setColor(GUIConstants.NAME_COLOR);

		player2NameLabel = new Label("", skin);
		player2NameLabel.setColor(GUIConstants.NAME_COLOR);

		stage.addActor(player1NameLabel);
		stage.addActor(player2NameLabel);

		player1TurnLabel = new Label("", skin);
		player2TurnLabel = new Label("", skin);

		stage.addActor(player1TurnLabel);
		stage.addActor(player2TurnLabel);

		messageLabel = new Label("", skin);

		stage.addActor(messageLabel);

		score = new Label("", skin);

		stage.addActor(score);
	}

	private void quitMatch() {
		gameScreen.quitMatch();
	}

	private void endPause() {
		gameScreen.endPause();
	}

	public void setPlayerNames(String player1Name, String player2Name) {

		player1NameLabel.setText(player1Name);
		player2NameLabel.setText(player2Name);

		I18NBundle messagesBundle = Options.getInstance()
				.getCurrentMessagesBundle();

		player1TurnLabel.setText(player1Name + messagesBundle.get("turn"));
		player2TurnLabel.setText(player2Name + messagesBundle.get("turn"));
	}

	public void initGame() {

		float frameWidth = Gorillas.worldWidth;
		float frameHeight = Gorillas.worldHeight;

		// adjust name label positions
		player1NameLabel
				.setPosition(GUIConstants.NAME_OFFSET_X, frameHeight
						- player1NameLabel.getPrefHeight()
						+ GUIConstants.NAME_OFFSET_Y);
		player2NameLabel.setPosition(
				frameWidth - player2NameLabel.getPrefWidth()
						- GUIConstants.NAME_OFFSET_X, frameHeight
						- player2NameLabel.getPrefHeight()
						+ GUIConstants.NAME_OFFSET_Y);

		player1Widget.setPosition(Position.left);
		player2Widget.setPosition(Position.right);

		if (gameScreen != null) {
			if (gameScreen.getControlPlayer1().equals(ControlType.CLASSIC)) {
				player1Widget.setVisible(true);
			} else {
				player1Widget.setVisible(false);
			}
		}
		player2Widget.setVisible(false);

		player1Widget.clear();
		player2Widget.clear();

		player1TurnLabel.setPosition(
				frameWidth / 4f - player1TurnLabel.getPrefWidth() / 2f, 2 / 3f
						* frameHeight + Assets.gorilla.getRegionHeight() + 5);
		player2TurnLabel.setPosition(
				frameWidth * 3 / 4f - player2TurnLabel.getPrefWidth() / 2f, 2
						/ 3f * frameHeight + Assets.gorilla.getRegionHeight()
						+ 5);

		player1TurnLabel.setColor(1f, 1f, 1f, 0f);
		player2TurnLabel.setColor(1f, 1f, 1f, 0f);

		score.setText("0 > Score < 0");
		score.pack();

		score.setPosition(frameWidth / 2 - score.getPrefWidth() / 2, 20 + 5);

		messageLabel.setPosition(frameWidth / 2, 2 / 3f * frameHeight
				+ Assets.gorilla.getRegionHeight() + 5);

		resetMessage();
	}

	public void initLevel() {
		player1Widget.clear();
		player2Widget.clear();
	}

	public void hidePlayer1Widget() {
		player1Widget.setVisible(false);
	}

	public void hidePlayer2Widget() {
		player2Widget.setVisible(false);
	}

	public void showPlayer1Widget() {
		player1Widget.setVisible(true);
	}

	public void showPlayer2Widget() {
		player2Widget.setVisible(true);
	}

	public void setMessage(String message) {

		messageLabel.setText(message);

		float frameWidth = Gorillas.worldWidth;
		float frameHeight = Gorillas.worldHeight;

		float messageWidth = messageLabel.getPrefWidth();

		messageLabel.setPosition(frameWidth / 2 - messageWidth / 2, 2 / 3f * frameHeight
				+ Assets.gorilla.getRegionHeight() + 5);
	}

	public void updateScore(int scorePlayer1, int scorePlayer2) {
		score.setText(scorePlayer1 + " > Score < " + scorePlayer2);
	}

	public void resetMessage() {
		messageLabel.setText("");
	}

	public void showQuitDialog() {

		quitDialog = new Dialog("", skin) {

			@Override
			protected void result(Object object) {
				if (object.equals(true)) {
					this.setVisible(false);
					quitMatch();
				} else {
					endPause();
				}
			}
		};

		I18NBundle messagesBundle = Options.getInstance()
				.getCurrentMessagesBundle();

		Label quitLabel = new Label(" " + messagesBundle.get("quitGame") + " ",
				skin);

		TextButton noButton = new TextButton(messagesBundle.get("cancel"), skin);

		TextButton yesButton = new TextButton(messagesBundle.get("ok"), skin);

		quitDialog.text(quitLabel);
		quitDialog.button(yesButton, true);
		quitDialog.button(noButton, false).padBottom(5);

		quitDialog
				.getButtonTable()
				.getCell(yesButton)
				.width(yesButton.getPrefWidth()
						+ GUIConstants.DOS_BUTTON_CORRECT_X);
		quitDialog
				.getButtonTable()
				.getCell(noButton)
				.width(noButton.getPrefWidth()
						+ GUIConstants.DOS_BUTTON_CORRECT_X);

		quitDialog.setVisible(true);
		quitDialog.show(stage);
	}

	public void showWinDialog(String winnerName) {

		I18NBundle messagesBundle = Options.getInstance()
				.getCurrentMessagesBundle();

		winDialog = new Dialog("", skin) {

			@Override
			protected void result(Object object) {
				this.setVisible(false);
				quitMatch();
			}
		};

		TextButton okButton = new TextButton("Ok", skin);

		winDialog.button(okButton).padBottom(5);

		winDialog
				.getButtonTable()
				.getCell(okButton)
				.width(okButton.getPrefWidth()
						+ GUIConstants.DOS_BUTTON_CORRECT_X);

		Label winLabel = new Label(" " + winnerName + " "
				+ messagesBundle.get("hasWon") + " ", skin);
		winLabel.setName("winMessage");

		winDialog.text(winLabel);
		winDialog.setVisible(true);
		winDialog.show(stage);
	}

	public void showPlayer1Turn() {
		player1TurnLabel.addAction(Actions.fadeIn(1f));
	}

	public void hidePlayer1Turn() {
		player1TurnLabel.addAction(Actions.fadeOut(1f));
	}

	public void showPlayer2Turn() {
		player2TurnLabel.addAction(Actions.fadeIn(1f));
	}

	public void hidePlayer2Turn() {
		player2TurnLabel.addAction(Actions.fadeOut(1f));
	}

	public void updateTextElements() {
		player1Widget.updateTextElements();
		player2Widget.updateTextElements();
	}

	public void disablePlayer1Widget() {
		player1Widget.disable();
	}

	public void inputParamPlayer1(int angle, int velocity) {
		player1Widget.inputParam(angle, velocity);
	}
}
