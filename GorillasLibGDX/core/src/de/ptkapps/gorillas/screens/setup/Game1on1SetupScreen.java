package de.ptkapps.gorillas.screens.setup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.I18NBundle;

import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.options.Options;
import de.ptkapps.gorillas.screens.GameScreen;
import de.ptkapps.gorillas.screens.GameScreen.ControlType;
import de.ptkapps.gorillas.screens.gui.GUIConstants;

public class Game1on1SetupScreen extends GameSetupScreen {

	private Table table;

	private Label player1Label;

	private Label controlPlayer1;
	private CheckBox modernPlayer1;
	private CheckBox classicPlayer1;

	private Label player1NameLabel;
	private TextField player1Input;

	private Label player1ErrorLabel;

	private Label player2Label;

	private Label controlPlayer2;
	private CheckBox modernPlayer2;
	private CheckBox classicPlayer2;

	private Label player2NameLabel;
	private TextField player2Input;

	private Label player2ErrorLabel;

	public Game1on1SetupScreen(Gorillas game) {

		super(game);

		player1Label = new Label("", skin);
		player1Label.setColor(GUIConstants.NAME_COLOR);

		player2Label = new Label("", skin);
		player2Label.setColor(GUIConstants.NAME_COLOR);

		controlPlayer1 = new Label("", skin);
		controlPlayer1.setColor(Color.BLACK);
		controlPlayer2 = new Label("", skin);
		controlPlayer2.setColor(Color.BLACK);

		generateCheckBoxes();

		generateInputFieldsAndLabels();

		int padLeft = 5;

		table = new Table(skin);

		table.setBackground("menuBackground");

		table.add(player1Label).left().padLeft(padLeft);
		table.add().colspan(3);

		table.row();

		table.add(controlPlayer1).left().padLeft(padLeft);
		table.add(modernPlayer1).left();
		table.add(classicPlayer1).left();
		table.add();

		table.row();

		table.add(player1NameLabel).left().padLeft(padLeft);
		table.add(player1Input).left().colspan(2);
		table.add();

		table.row();

		table.add();
		table.add(player1ErrorLabel).left().colspan(3);

		table.row();

		table.add(player2Label).left().padLeft(padLeft);
		table.add().colspan(3);

		table.row();

		table.add(controlPlayer2).left().padLeft(padLeft);
		table.add(modernPlayer2).left();
		table.add(classicPlayer2).left();
		table.add();

		table.row();

		table.add(player2NameLabel).left().padLeft(padLeft);
		table.add(player2Input).left().colspan(2);
		table.add();

		table.row();

		table.add();
		table.add(player2ErrorLabel).left().colspan(3);

		table.row();

		table.add(startGameButton).pad(5, 0, 5, 0).colspan(4);

		player1ErrorLabel.setText("");
		player2ErrorLabel.setText("");

		guiStage.addActor(table);

		updateTextElements();
	}

	private void generateCheckBoxes() {

		modernPlayer1 = new CheckBox("", skin);
		modernPlayer1.setDisabled(true);
		classicPlayer1 = new CheckBox("", skin);
		classicPlayer1.setDisabled(true);

		modernPlayer2 = new CheckBox("", skin);
		modernPlayer2.setDisabled(true);
		classicPlayer2 = new CheckBox("", skin);
		classicPlayer2.setDisabled(true);

		// do checking and unchecking manually, only one checkbox should be
		// checked at a time. Trying to implement this feature with
		// ChangeListeners or individual InputListeners failed, cause they
		// always trigger each other.
		guiStage.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				float modernPlayer1Width = modernPlayer1.getWidth();
				float modernPlayer1Height = modernPlayer1.getHeight();

				float classicPlayer1Width = classicPlayer1.getWidth();
				float classicPlayer1Height = classicPlayer1.getHeight();

				float modernPlayer1X = modernPlayer1.getX() + table.getX();
				float modernPlayer1Y = modernPlayer1.getY() + table.getY();

				float classicPlayer1X = classicPlayer1.getX() + table.getX();
				float classicPlayer1Y = classicPlayer1.getY() + table.getY();

				float modernPlayer1EndX = modernPlayer1X + modernPlayer1Width;
				float modernPlayer1EndY = modernPlayer1Y + modernPlayer1Height;

				float classicPlayer1EndX = classicPlayer1X
						+ classicPlayer1Width;
				float classicPlayer1EndY = classicPlayer1Y
						+ classicPlayer1Height;

				if (x >= modernPlayer1X && x <= modernPlayer1EndX
						&& y >= modernPlayer1Y && y <= modernPlayer1EndY) {

					classicPlayer1.setChecked(false);
					modernPlayer1.setChecked(true);
				}

				if (x >= classicPlayer1X && x <= classicPlayer1EndX
						&& y >= classicPlayer1Y && y <= classicPlayer1EndY) {

					classicPlayer1.setChecked(true);
					modernPlayer1.setChecked(false);
				}

				float modernPlayer2Width = modernPlayer2.getWidth();
				float modernPlayer2Height = modernPlayer2.getHeight();

				float classicPlayer2Width = classicPlayer2.getWidth();
				float classicPlayer2Height = classicPlayer2.getHeight();

				float modernPlayer2X = modernPlayer2.getX() + table.getX();
				float modernPlayer2Y = modernPlayer2.getY() + table.getY();

				float classicPlayer2X = classicPlayer2.getX() + table.getX();
				float classicPlayer2Y = classicPlayer2.getY() + table.getY();

				float modernPlayer2EndX = modernPlayer2X + modernPlayer2Width;
				float modernPlayer2EndY = modernPlayer2Y + modernPlayer2Height;

				float classicPlayer2EndX = classicPlayer2X
						+ classicPlayer2Width;
				float classicPlayer2EndY = classicPlayer2Y
						+ classicPlayer2Height;

				if (x >= modernPlayer2X && x <= modernPlayer2EndX
						&& y >= modernPlayer2Y && y <= modernPlayer2EndY) {

					classicPlayer2.setChecked(false);
					modernPlayer2.setChecked(true);
				}

				if (x >= classicPlayer2X && x <= classicPlayer2EndX
						&& y >= classicPlayer2Y && y <= classicPlayer2EndY) {

					classicPlayer2.setChecked(true);
					modernPlayer2.setChecked(false);
				}

				return true;
			}
		});

	}

	private void generateInputFieldsAndLabels() {

		player1NameLabel = new Label("Please insert name for player 1:", skin);
		player1NameLabel.setColor(Color.BLACK);
		player1ErrorLabel = new Label("A player's name can't be empty.", skin);
		player1ErrorLabel.setColor(Color.RED);
		player1Input = new TextField("", skin);
		player1Input.setMaxLength(15);

		player2NameLabel = new Label("Please insert name for player 2:", skin);
		player2NameLabel.setColor(Color.BLACK);
		player2ErrorLabel = new Label("A player's name can't be empty.", skin);
		player2ErrorLabel.setColor(Color.RED);
		player2Input = new TextField("", skin);
		player2Input.setMaxLength(15);

		// a textfield should not receive an "enter character" in the desktop
		// application (is this a bug?)
		// this is a hack to prevent this
		player1Input.addListener(new InputListener() {
			@Override
			public boolean keyTyped(InputEvent event, char character) {

				handlePossibleEnterPress(player1Input, event);
				return true;
			}
		});
		player2Input.addListener(new InputListener() {
			@Override
			public boolean keyTyped(InputEvent event, char character) {

				handlePossibleEnterPress(player2Input, event);
				return true;
			}
		});
	}

	@Override
	protected void nameInputFinished() {

		I18NBundle messagesBundle = Options.getInstance()
				.getCurrentMessagesBundle();

		String player1Name = player1Input.getText().trim();
		String player2Name = player2Input.getText().trim();

		boolean faultyName = false;

		if (player1Name.isEmpty()) {

			player1ErrorLabel.setText(messagesBundle.get("notEmpty"));
			if (!player2Name.isEmpty()) {
				player2ErrorLabel.setText("");
			}
			faultyName = true;
		}

		if (player2Name.isEmpty()) {

			if (!player1Name.isEmpty()) {
				player1ErrorLabel.setText("");
			}
			player2ErrorLabel.setText(messagesBundle.get("notEmpty"));
			faultyName = true;
		}

		if (!faultyName && player1Name.equals(player2Name)) {

			player1ErrorLabel.setText(messagesBundle.get("notEqual"));
			player2ErrorLabel.setText(messagesBundle.get("notEqual"));
			faultyName = true;
		}

		if (faultyName) {
			return;
		} else {
			initGame(player1Name, player2Name);
		}
	}

	private void initGame(String player1Name, String player2Name) {

		GameScreen gameScreen = game.gameScreen;

		gameScreen.setPlayerNames(player1Name, player2Name);
		gameScreen.set1on1Game();

		Options options = Options.getInstance();
		ControlType player1Control;
		ControlType player2Control;

		if (classicPlayer1.isChecked()) {
			player1Control = ControlType.CLASSIC;
		} else {
			player1Control = ControlType.MODERN;
		}
		if (classicPlayer2.isChecked()) {
			player2Control = ControlType.CLASSIC;
		} else {
			player2Control = ControlType.MODERN;
		}

		gameScreen.setPlayer1Control(player1Control);
		gameScreen.setPlayer2Control(player2Control);

		options.setPlayer1Control(player1Control);
		options.setPlayer2Control(player2Control);

		options.setPlayer1Name(player1Name);
		options.setPlayer2Name(player2Name);

		game.setScreen(gameScreen);
	}

	private void layout() {

		I18NBundle messagesBundle = Options.getInstance()
				.getCurrentMessagesBundle();

		// set each wrap to false to not receive 0 as preferred width
		player1ErrorLabel.setWrap(false);
		player2ErrorLabel.setWrap(false);

		float border = 10;

		float playerLabelWidth = player1Label.getPrefWidth();
		float playerLabelHeight = player1Label.getPrefHeight();

		float controlWidth = controlPlayer1.getPrefWidth();
		float controlHeight = controlPlayer1.getPrefHeight();

		float modernWidth = modernPlayer1.getPrefWidth();
		float classicWidth = classicPlayer1.getPrefWidth();

		float playerNameLabelWidth = player1NameLabel.getPrefWidth();
		float playerNameLabelHeight = player1NameLabel.getPrefHeight();

		float maxFirstColumnWidth = Math.max(playerLabelWidth, controlWidth);

		player1ErrorLabel.setText(messagesBundle.get("notEmpty"));
		float notEmptyWidth = player1ErrorLabel.getPrefWidth();
		float notEmptyHeight = player1ErrorLabel.getPrefHeight();

		player1ErrorLabel.setText(messagesBundle.get("notEqual"));
		float notEqualWidth = player1ErrorLabel.getPrefWidth();
		float notEqualHeight = player1ErrorLabel.getPrefHeight();

		float maxErrorLabelHeight = Math.max(notEmptyHeight, notEqualHeight);

		float maxErrorLabelWidth = Math.max(notEmptyWidth, notEqualWidth);
		if (maxErrorLabelWidth > Gorillas.worldWidth - maxFirstColumnWidth
				- border * 2) {
			maxErrorLabelWidth = Gorillas.worldWidth - maxFirstColumnWidth
					- border * 2;
			maxErrorLabelHeight *= 2;
		}

		float tableWidth = maxFirstColumnWidth + maxErrorLabelWidth + border;
		float tableHeight = 2 * playerLabelHeight + 2 * controlHeight + 2
				* playerNameLabelHeight + 2 * maxErrorLabelHeight
				+ startGameButton.getPrefHeight() + border;

		float tableX = Gorillas.worldWidth / 2 - tableWidth / 2;
		float tableY = Gorillas.worldHeight - tableHeight - border;

		// it seems one shouldn't use setBounds with position variables with decimal places (can lead to pixel errors)
		table.setBounds((int) tableX, (int) tableY, tableWidth, tableHeight);

		player1ErrorLabel.setWrap(true);
		player2ErrorLabel.setWrap(true);

		table.getCell(player1NameLabel).width(playerNameLabelWidth);
		table.getCell(player2NameLabel).width(playerNameLabelWidth);

		table.getCell(modernPlayer1).width(modernWidth);
		table.getCell(classicPlayer1).width(classicWidth).expandX();

		table.getCell(player1Input).width(modernWidth + classicWidth);

		table.getCell(player1ErrorLabel).width(maxErrorLabelWidth)
				.height(maxErrorLabelHeight);

		table.getCell(modernPlayer2).width(modernWidth);
		table.getCell(classicPlayer2).width(classicWidth);

		table.getCell(player2Input).width(modernWidth + classicWidth);

		table.getCell(player2ErrorLabel).width(maxErrorLabelWidth)
				.height(maxErrorLabelHeight);

		table.getCell(startGameButton).width(
				startGameButton.getPrefWidth()
						+ GUIConstants.DOS_BUTTON_CORRECT_X);
	}

	@Override
	public void updateTextElements() {

		super.updateTextElements();

		I18NBundle messagesBundle = Options.getInstance()
				.getCurrentMessagesBundle();

		player1NameLabel.setText(messagesBundle.get("playerOneInsert"));
		player2NameLabel.setText(messagesBundle.get("playerTwoInsert"));

		player1Label.setText(messagesBundle.get("player1"));
		player2Label.setText(messagesBundle.get("player2"));

		controlPlayer1.setText(messagesBundle.get("control"));
		modernPlayer1.setText(messagesBundle.get("modern"));
		classicPlayer1.setText(messagesBundle.get("classic"));

		controlPlayer2.setText(messagesBundle.get("control"));
		modernPlayer2.setText(messagesBundle.get("modern"));
		classicPlayer2.setText(messagesBundle.get("classic"));

		layout();
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, (10 * 16 + 8 * 1) / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render(delta);
	}

	@Override
	public void show() {

		super.show();

		player1ErrorLabel.setText("");
		player2ErrorLabel.setText("");

		String player1Name = Options.getInstance().getPlayer1Name();
		player1Input.setText(player1Name);
		player1Input.setCursorPosition(player1Name.length());

		String player2Name = Options.getInstance().getPlayer2Name();
		player2Input.setText(player2Name);
		player2Input.setCursorPosition(player2Name.length());

		ControlType player1Control = Options.getInstance()
				.getPlayerOneControl();
		ControlType player2Control = Options.getInstance()
				.getPlayerTwoControl();

		if (player1Control.equals(ControlType.CLASSIC)) {
			classicPlayer1.setChecked(true);
			modernPlayer1.setChecked(false);
		} else {
			classicPlayer1.setChecked(false);
			modernPlayer1.setChecked(true);
		}
		if (player2Control.equals(ControlType.CLASSIC)) {
			classicPlayer2.setChecked(true);
			modernPlayer2.setChecked(false);
		} else {
			classicPlayer2.setChecked(false);
			modernPlayer2.setChecked(true);
		}
	}

	@Override
	public void hide() {

		player1Input.getOnscreenKeyboard().show(false);
		player2Input.getOnscreenKeyboard().show(false);
	}
}
