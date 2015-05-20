package de.ptkapps.gorillas.screens.gui.widgets;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.I18NBundle;

import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.options.Options;
import de.ptkapps.gorillas.screens.GameScreen;
import de.ptkapps.gorillas.screens.gui.GUIConstants;

public class DesktopHtmlPlayerWidget extends PlayerWidget {

	private TextField angleInput;
	private TextField velocityInput;

	private Label dummyLabel;

	private Label angleLabel;
	private Label velocityLabel;

	private TextButton throwButton;

	private Table table;

	public DesktopHtmlPlayerWidget(Stage stage, GameScreen gameScreen, Skin skin) {

		super(gameScreen, stage, skin);

		dummyLabel = new Label("", skin);

		angleInput = new TextField("", skin, "numberField");
		angleInput.addListener(new InputListener() {
			@Override
			public boolean keyTyped(InputEvent event, char character) {
				handleTextFieldInput(event, character, angleInput, 360);
				return true;
			}
		});

		velocityInput = new TextField("", skin, "numberField");
		velocityInput.addListener(new InputListener() {
			@Override
			public boolean keyTyped(InputEvent event, char character) {
				handleTextFieldInput(event, character, velocityInput, 200);
				return true;
			}
		});

		angleLabel = new Label("Angle:", skin);
		velocityLabel = new Label("Velocity:", skin);

		createThrowButton();

		table = new Table();
		table.add(angleLabel).left();
		table.add(angleInput).pad(2);
		table.row();
		table.add(velocityLabel).left();
		table.add(velocityInput).pad(2);
		table.row();
		table.add(dummyLabel);
		table.add(throwButton).padTop(2);

		stage.addActor(table);
	}

	private void createThrowButton() {

		throwButton = new TextButton("Shoot", skin, "numberField");
		throwButton.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {

				inputFinished();
			}
		});
	}

	@Override
	public void setPosition(Position position) {

		this.position = position;

		float angleLabelWidth = angleLabel.getPrefWidth();
		float velocityLabelWidth = velocityLabel.getPrefWidth();
		float throwButtonWidth = throwButton.getPrefWidth();

		float maxLabelWidth = Math.max(angleLabelWidth, velocityLabelWidth);

		table.getCell(dummyLabel).width(maxLabelWidth);
		table.getCell(angleLabel).width(maxLabelWidth);
		table.getCell(velocityLabel).width(maxLabelWidth);
		table.getCell(angleInput).width(throwButtonWidth);
		table.getCell(velocityInput).width(throwButtonWidth);
		table.getCell(throwButton).width(throwButtonWidth)
				.height(angleInput.getPrefHeight());

		float offsetX = GUIConstants.NAME_OFFSET_X;

		float paneWidth = Gorillas.worldWidth;
		float paneHeight = Gorillas.worldHeight;
		float tableWidth = table.getPrefWidth();
		float tableHeight = table.getPrefHeight();

		table.setSize(tableWidth, tableHeight);

		float tableX;
		float tableY = paneHeight - tableHeight - angleLabel.getPrefHeight()
				- GUIConstants.NAME_OFFSET_Y;

		if (position.equals(Position.left)) {

			tableX = offsetX;

		} else {

			tableX = paneWidth - tableWidth - offsetX;
		}

		table.setPosition(tableX, tableY);
	}

	@Override
	public void updateTextElements() {

		I18NBundle messagesBundle = Options.getInstance()
				.getCurrentMessagesBundle();

		angleLabel.setText(messagesBundle.get("angle"));
		velocityLabel.setText(messagesBundle.get("velocity"));

		throwButton.setText(messagesBundle.get("shoot"));

		if (position != null) {
			setPosition(position);
		}
	}

	@Override
	public void setVisible(boolean visible) {

		table.setVisible(visible);
		// also set input fields disabled to make sure that the
		// appropriate next textfield is found if shift is pressed
		angleInput.setDisabled(!visible);
		velocityInput.setDisabled(!visible);
	}

	@Override
	public void clear() {

		angleInput.setText("");
		angleInput.setCursorPosition(0);
		velocityInput.setText("");
		velocityInput.setCursorPosition(0);
		stage.unfocusAll();
	}

	private void handleTextFieldInput(InputEvent event, char character,
			TextField textfield, int maxValue) {

		if (event.getKeyCode() == Input.Keys.ENTER) {

			String inputText = textfield.getText();

			if (inputText.isEmpty()) {
				return;
			}
			textfield.setText(inputText.substring(0, inputText.length() - 1));
			textfield.setCursorPosition(inputText.length() - 1);
			inputFinished();

		} else {
			String inputText = textfield.getText();

			if (inputText.isEmpty()) {
				return;
			}

			char inputChar = inputText.charAt(inputText.length() - 1);
			if (!Character.isDigit(inputChar)
					|| Integer.parseInt(inputText) > maxValue) {
				textfield
						.setText(inputText.substring(0, inputText.length() - 1));
				textfield.setCursorPosition(inputText.length() - 1);
			}
		}
	}

	private void inputFinished() {

		String angleInputText = angleInput.getText();
		String velocityInputText = velocityInput.getText();

		if (!angleInputText.isEmpty() && !velocityInputText.isEmpty()) {
			gameScreen.generateShot(Integer.parseInt(angleInputText),
					Integer.parseInt(velocityInputText));
		}
	}

	@Override
	public void disable() {

		angleInput.setDisabled(true);
		velocityInput.setDisabled(true);
		throwButton.setDisabled(true);
		throwButton.setTouchable(Touchable.disabled);
	}

	@Override
	public void inputParam(int angle, int velocity) {

		angleInput.setText(Integer.toString(angle));
		velocityInput.setText(Integer.toString(velocity));
	}
}
