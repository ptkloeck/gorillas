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
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.options.Options;
import de.ptkapps.gorillas.screens.GameScreen;
import de.ptkapps.gorillas.screens.GameScreen.ControlType;
import de.ptkapps.gorillas.screens.gui.GUIConstants;

public class GameAISetupScreen extends GameSetupScreen {

	private Table table;

	private Label controlPlayer1;
	private CheckBox modernPlayer1;
	private CheckBox classicPlayer1;

	private Label player1NameLabel;
	private TextField player1Input;

	private Label player1ErrorLabel;

	public GameAISetupScreen(Gorillas game) {

		super(game);

		controlPlayer1 = new Label("", skin);
		controlPlayer1.setColor(Color.BLACK);

		generateCheckBoxes();

		generateInputFieldsAndLabels();

		int padLeft = 5;

		table = new Table(skin);

		table.setBackground("menuBackground");

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

		table.add(startGameButton).pad(5, 0, 5, 0).colspan(4);

		player1ErrorLabel.setText("");

		guiStage.addActor(table);

		updateTextElements();
	}

	private void generateCheckBoxes() {

		modernPlayer1 = new CheckBox("", skin);
		modernPlayer1.setDisabled(true);
		classicPlayer1 = new CheckBox("", skin);
		classicPlayer1.setDisabled(true);

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

				return true;
			}
		});
	}

	private void generateInputFieldsAndLabels() {

		player1NameLabel = new Label("", skin);
		player1NameLabel.setColor(Color.BLACK);

		player1ErrorLabel = new Label("", skin);
		player1ErrorLabel.setColor(Color.RED);
		player1Input = new TextField("", skin);
		player1Input.setMaxLength(15);

		// a textfield should not receive an "enter character" (is this a bug?)
		// this is a hack to prevent this
		player1Input.addListener(new InputListener() {
			@Override
			public boolean keyTyped(InputEvent event, char character) {

				handlePossibleEnterPress(player1Input, event);
				return true;
			}
		});
	}

	@Override
	protected void nameInputFinished() {

		I18NBundle messagesBundle = Options.getInstance()
				.getCurrentMessagesBundle();

		String player1Name = player1Input.getText().trim();

		boolean faultyName = false;

		if (player1Name.isEmpty()) {

			player1ErrorLabel.setText(messagesBundle.get("notEmpty"));
			faultyName = true;
		}

		if (faultyName) {
			return;
		} else {

			initGame(player1Name);
		}
	}

	private void initGame(String player1Name) {

		GameScreen gameScreen = game.gameScreen;

		gameScreen.setPlayerNames(player1Name, "Computer");
		gameScreen.setAIGame();

		Options options = Options.getInstance();

		ControlType player1Control;
		if (classicPlayer1.isChecked()) {
			player1Control = ControlType.CLASSIC;
		} else {
			player1Control = ControlType.MODERN;
		}

		gameScreen.setPlayer1Control(player1Control);
		options.setPlayer1Control(player1Control);
		options.setPlayer1Name(player1Name);

		game.setScreen(gameScreen);
	}

	private void layout() {

		I18NBundle messagesBundle = Options.getInstance()
				.getCurrentMessagesBundle();

		// set each wrap to false to not receive 0 as preferred width
		player1NameLabel.setWrap(false);
		player1ErrorLabel.setWrap(false);

		float border = 10;

		float controlWidth = controlPlayer1.getPrefWidth();
		float controlHeight = controlPlayer1.getPrefHeight();

		float modernWidth = modernPlayer1.getPrefWidth();
		float classicWidth = classicPlayer1.getPrefWidth();

		float playerNameLabelWidth = player1NameLabel.getPrefWidth();
		float playerNameLabelHeight = player1NameLabel.getPrefHeight();

		float maxFirstColumnWidth = controlWidth;

		player1ErrorLabel.setText(messagesBundle.get("notEmpty"));
		float notEmptyWidth = player1ErrorLabel.getPrefWidth();
		float notEmptyHeight = player1ErrorLabel.getPrefHeight();

		float maxErrorLabelWidth = notEmptyWidth;
		float maxErrorLabelHeight = notEmptyHeight;

		float tableWidth = maxFirstColumnWidth + maxErrorLabelWidth + border;
		float tableHeight = controlHeight + playerNameLabelHeight
				+ maxErrorLabelHeight + startGameButton.getPrefHeight()
				+ border;

		float tableX = Gorillas.worldWidth / 2 - tableWidth / 2;
		float tableY = Gorillas.worldHeight * 2 / 3 - tableHeight / 2;

		// it seems one shouldn't use setBounds with position variables with decimal places (can lead to pixel errors)
		table.setBounds((int) tableX, (int) tableY, tableWidth, tableHeight);

		player1NameLabel.setWrap(true);
		player1NameLabel.setAlignment(Align.center);

		player1ErrorLabel.setWrap(true);

		table.getCell(player1NameLabel).width(playerNameLabelWidth);

		table.getCell(modernPlayer1).width(modernWidth);
		table.getCell(classicPlayer1).width(classicWidth).expandX();

		table.getCell(player1Input).width(modernWidth + classicWidth);

		table.getCell(player1ErrorLabel).width(maxErrorLabelWidth)
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
		controlPlayer1.setText(messagesBundle.get("control"));
		modernPlayer1.setText(messagesBundle.get("modern"));
		classicPlayer1.setText(messagesBundle.get("classic"));

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

		String player1Name = Options.getInstance().getPlayer1Name();
		player1Input.setText(player1Name);
		player1Input.setCursorPosition(player1Name.length());

		ControlType player1Control = Options.getInstance()
				.getPlayerOneControl();

		if (player1Control.equals(ControlType.CLASSIC)) {
			modernPlayer1.setChecked(false);
			classicPlayer1.setChecked(true);

		} else {
			modernPlayer1.setChecked(true);
			classicPlayer1.setChecked(false);
		}
	}

	@Override
	public void hide() {

		player1Input.getOnscreenKeyboard().show(false);
	}
}
