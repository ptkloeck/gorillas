package de.ptkapps.gorillas.screens.gui.widgets;

import static de.ptkapps.gorillas.main.Gorillas.normalLabelHeight;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.OnscreenKeyboard;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.I18NBundle;

import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.options.Options;
import de.ptkapps.gorillas.screens.GameScreen;
import de.ptkapps.gorillas.screens.gui.GUIConstants;
import de.ptkapps.gorillas.screens.gui.widgets.NumberInput.NumberFieldInputListener;

public class AndroidPlayerWidget extends PlayerWidget {

	private NumberInput angleNumberInput;
	private NumberInput velocityNumberInput;

	private Label angleLabel;
	private Label velocityLabel;

	private Label dummyLabel;

	private TextField angleInputField;
	private TextField velocityInputField;

	private TextButton throwButton;

	private Table table;

	public AndroidPlayerWidget(Stage stage, GameScreen gameScreen, Skin skin) {

		super(gameScreen, stage, skin);

		dummyLabel = new Label("", skin);

		createThrowButton();
		createAngleNumberInput(stage);
		createVelocityNumberInput(stage);
		createInputLabels();

		table = new Table();
		table.add(angleLabel).left();
		table.add(angleInputField).pad(2);
		table.row();
		table.add(velocityLabel).left();
		table.add(velocityInputField);
		table.row();
		table.add(dummyLabel);
		table.add(throwButton).padTop(2);

		stage.addActor(table);
	}

	private void createInputLabels() {

		angleLabel = new Label("Angle:", skin);
		velocityLabel = new Label("Velocity:", skin);

		angleInputField = new TextField("", skin, "numberField");
		angleInputField.setOnscreenKeyboard(new OnscreenKeyboard() {
			@Override
			public void show(boolean visible) {
				angleNumberInput.setVisible(true);
				velocityNumberInput.setVisible(false);
			}
		});
		angleInputField.addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor,
					boolean focused) {
				if (focused) {
					angleInputField.setSelection(0, angleInputField.getText()
							.length());
				}
				super.keyboardFocusChanged(event, actor, focused);
			}
		});

		velocityInputField = new TextField("", skin, "numberField");
		velocityInputField.setOnscreenKeyboard(new OnscreenKeyboard() {
			@Override
			public void show(boolean visible) {
				angleNumberInput.setVisible(false);
				velocityNumberInput.setVisible(true);
			}
		});
		velocityInputField.addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor,
					boolean focused) {
				if (focused) {
					velocityInputField.setSelection(0, velocityInputField
							.getText().length());
				}
				super.keyboardFocusChanged(event, actor, focused);
			}
		});
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

	private void createVelocityNumberInput(Stage stage) {

		velocityNumberInput = new NumberInput(stage, skin,
				throwButton.getWidth(), normalLabelHeight * 4 / 3f);
		velocityNumberInput
				.setNumberFieldInputListener(new NumberFieldInputListener() {

					@Override
					public void numberEntered(int number) {
						insertNumber(velocityInputField, number, 200);
					}

					@Override
					public void cePressed() {
						handleDelete(velocityInputField);

					}

					@Override
					public void enterPressed() {
						handleEnterVelocity();					
					}
				});
	}

	private void createAngleNumberInput(Stage stage) {

		angleNumberInput = new NumberInput(stage, skin, throwButton.getWidth(),
				normalLabelHeight * 4 / 3f);
		angleNumberInput
				.setNumberFieldInputListener(new NumberFieldInputListener() {

					@Override
					public void numberEntered(int number) {
						insertNumber(angleInputField, number, 360);
					}

					@Override
					public void cePressed() {
						handleDelete(angleInputField);

					}

					@Override
					public void enterPressed() {
						handleEnterAngle();
					}
				});
	}

	private void insertNumber(TextField inputField, int number, int maxValue) {

		int cursorPos = inputField.getCursorPosition();
		int nextCursorPos;
		int value;
		if (inputField.getSelection().equals("")) {
			String currentValue = inputField.getText();
			String preCursor = currentValue.substring(0, cursorPos);
			String postCursor = currentValue.substring(cursorPos);
			value = Integer.parseInt(preCursor + Integer.toString(number)
					+ postCursor);
			nextCursorPos = cursorPos + 1;
		} else {
			// If the cursor is on the right end of the selection,
			// selectionStart denotes the index left of the cursor, the
			// selection starts with.
			// If the cursor is on the left end of the selection, selectionStart
			// denotes the index after the rightmost selected character.
			int selectionStart = inputField.getSelectionStart();
			String currentValue = inputField.getText();
			String preSelection;
			String postSelection;
			if (selectionStart > cursorPos) {
				preSelection = currentValue.substring(0, cursorPos);
				postSelection = currentValue.substring(selectionStart);
				nextCursorPos = cursorPos + 1;
			} else {
				preSelection = currentValue.substring(0, selectionStart);
				postSelection = currentValue.substring(cursorPos);
				nextCursorPos = selectionStart + 1;
			}
			value = Integer.parseInt(preSelection + Integer.toString(number)
					+ postSelection);
		}
		if (value <= maxValue) {
			String newValue = Integer.toString(value);
			inputField.setText(newValue);
			inputField.setCursorPosition(nextCursorPos);
		}
	}

	private void handleDelete(TextField inputField) {

		int cursorPos = inputField.getCursorPosition();
		if (inputField.getSelection().equals("")) {
			String currentValue = inputField.getText().toString();
			String preCursorValue = currentValue.substring(0, cursorPos);
			String postCursorValue = currentValue.substring(cursorPos);
			if (preCursorValue.isEmpty()) {
				return;
			}
			inputField.setText(preCursorValue.substring(0,
					preCursorValue.length() - 1)
					+ postCursorValue);
			inputField.setCursorPosition(cursorPos - 1);
		} else {
			int nextCursorPos;
			int selectionStart = inputField.getSelectionStart();
			String currentValue = inputField.getText();
			String preSelection;
			String postSelection;
			if (selectionStart > cursorPos) {
				preSelection = currentValue.substring(0, cursorPos);
				postSelection = currentValue.substring(selectionStart);
				nextCursorPos = cursorPos;
			} else {
				preSelection = currentValue.substring(0, selectionStart);
				postSelection = currentValue.substring(cursorPos);
				nextCursorPos = selectionStart;
			}
			inputField.setText(preSelection + postSelection);
			inputField.setCursorPosition(nextCursorPos);
		}
	}

	private void handleEnterVelocity() {	
		velocityNumberInput.setVisible(false);
		velocityInputField.clearSelection();
		stage.unfocus(velocityInputField);
	}
	
	private void handleEnterAngle() {	
		angleNumberInput.setVisible(false);
		angleInputField.clearSelection();
		stage.unfocus(angleInputField);
	}
	
	private void inputFinished() {

		String angleInputText = angleInputField.getText().toString();
		String velocityInputText = velocityInputField.getText().toString();

		if (!angleInputText.isEmpty() && !velocityInputText.isEmpty()) {
			gameScreen.generateShot(Integer.parseInt(angleInputText),
					Integer.parseInt(velocityInputText));
			this.setVisible(false);
		}
	}

	@Override
	public void setPosition(Position position) {

		this.position = position;

		float angleLabelWidth = angleLabel.getPrefWidth();
		float velocityLabelWidth = velocityLabel.getPrefWidth();
		float throwButtonWidth = throwButton.getPrefWidth();

		float maxLabelWidth = Math.max(angleLabelWidth, velocityLabelWidth);

		table.getCell(dummyLabel).width(maxLabelWidth)
				.height(normalLabelHeight * 4 / 3f);
		table.getCell(angleLabel).width(maxLabelWidth)
				.height(normalLabelHeight * 4 / 3f);
		table.getCell(velocityLabel).width(maxLabelWidth)
				.height(normalLabelHeight * 4 / 3f);
		table.getCell(angleInputField).width(throwButtonWidth)
				.height(normalLabelHeight * 4 / 3f);
		table.getCell(velocityInputField).width(throwButtonWidth)
				.height(normalLabelHeight * 4 / 3f);
		table.getCell(throwButton).width(throwButtonWidth)
				.height(normalLabelHeight * 4 / 3f);

		float offsetX = 5;

		float angleNumInWidth = angleNumberInput.getPrefWidth();
		float angleNumInHeight = angleNumberInput.getPrefHeight();
		float velNumInWidth = velocityNumberInput.getPrefWidth();
		float velNumInHeight = velocityNumberInput.getPrefHeight();
		float tableWidth = table.getPrefWidth();
		float tableHeight = table.getPrefHeight();

		table.setSize(tableWidth, tableHeight);
		angleNumberInput.setSize(angleNumInWidth, angleNumInHeight);
		velocityNumberInput.setSize(velNumInWidth, velNumInHeight);

		float paneWidth = Gorillas.worldWidth;
		float paneHeight = Gorillas.worldHeight;

		float tableX;
		float tableY = paneHeight - tableHeight - angleLabel.getPrefHeight()
				- GUIConstants.NAME_OFFSET_Y;
		float angleNumInX;
		float angleNumInY = paneHeight - angleNumInHeight - GUIConstants.NAME_OFFSET_X;
		float velNumInX;
		float velNumInY = paneHeight - velNumInHeight - GUIConstants.NAME_OFFSET_X;

		if (position.equals(Position.left)) {
			tableX = offsetX;			
		} else {
			tableX = paneWidth - tableWidth - offsetX;
		}

		angleNumInX = (int) (paneWidth / 2f - angleNumInWidth / 2f);
		velNumInX = (int) (paneWidth / 2f - velNumInWidth / 2f);
		
		table.setPosition(tableX, tableY);
		angleNumberInput.setPosition(angleNumInX, angleNumInY);
		velocityNumberInput.setPosition(velNumInX, velNumInY);
	}

	@Override
	public void setVisible(boolean visible) {

		table.setVisible(visible);

		angleNumberInput.setVisible(false);
		velocityNumberInput.setVisible(false);
		stage.unfocus(angleInputField);
		stage.unfocus(velocityInputField);
	}

	@Override
	public void clear() {

		angleInputField.setText("");
		velocityInputField.setText("");
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
	public void disable() {

		angleInputField.setDisabled(true);
		angleInputField.setTouchable(Touchable.disabled);
		velocityInputField.setDisabled(true);
		velocityInputField.setTouchable(Touchable.disabled);
		throwButton.setDisabled(true);
		throwButton.setTouchable(Touchable.disabled);
	}

	@Override
	public void inputParam(int angle, int velocity) {

		angleInputField.setText(Integer.toString(angle));
		velocityInputField.setText(Integer.toString(velocity));
	}
}
