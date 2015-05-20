package de.ptkapps.gorillas.screens.gui.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import de.ptkapps.gorillas.screens.gui.GUIConstants;

public class ValueAdjuster extends WidgetGroup {

	private Label numberLabel;
	private TextButton minusButton;
	private TextButton plusButton;
	private Table table;

	private int min;
	private int max;

	private int value;

	/**
	 * time passed since the value was last changed
	 */
	private float changeTime;

	private float totalPressTime;

	public int getValue() {
		return value;
	}
	
	public ValueAdjuster(Skin skin, int defaultValue, int min, int max) {

		this.value = defaultValue;
		this.min = min;
		this.max = max;

		changeTime = 0;

		minusButton = new TextButton(" - ", skin);
		minusButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				minusPressed();
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				changeTime = 0;
				totalPressTime = 0;
			}
		});

		numberLabel = new Label(Integer.toString(defaultValue), skin);
		numberLabel.setColor(Color.BLACK);
		numberLabel.setAlignment(Align.center);

		plusButton = new TextButton(" + ", skin);
		plusButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				plusPressed();
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				changeTime = 0;
				totalPressTime = 0;
			}
		});

		numberLabel.setText("10");
		float numberLabelWidth = numberLabel.getPrefWidth();
		numberLabel.setText(Integer.toString(defaultValue));
		
		table = new Table(skin);
		table.add(minusButton).width(minusButton.getPrefWidth() + 7);
		table.add(numberLabel).width(numberLabelWidth);
		table.add(plusButton).width(plusButton.getPrefWidth() + 7);

		float tableWidth = minusButton.getPrefWidth() + GUIConstants.DOS_BUTTON_CORRECT_X
				+ plusButton.getPrefWidth() + GUIConstants.DOS_BUTTON_CORRECT_X + numberLabelWidth;

		table.setBounds(0, 0, tableWidth, numberLabelWidth);
		addActor(table);
	}

	private void minusPressed() {

		if (value > min) {
			value--;
			numberLabel.setText(Integer.toString(value));
		}
	}

	private void plusPressed() {

		if (value < max) {
			value++;
			numberLabel.setText(Integer.toString(value));
		}
	}

	@Override
	public void act(float delta) {

		if (minusButton.isPressed() || plusButton.isPressed()) {

			totalPressTime += delta;

			if (totalPressTime > 0.5) {

				float changeInterval;
				if (totalPressTime > 1.5) {
					changeInterval = 0.05f;
				} else {
					changeInterval = 0.2f;
				}

				if (changeTime > changeInterval) {

					if (minusButton.isPressed()) {
						minusPressed();
					} else {
						plusPressed();
					}
					changeTime = 0;

				} else {
					changeTime += delta;
				}
			}
		}
	}

	@Override
	public void setPosition(float x, float y) {

		float sizeX = table.getPrefWidth();
		float sizeY = table.getPrefHeight();

		table.setPosition(x + sizeX / 2, y + sizeY / 2);
	}

	@Override
	public void setVisible(boolean visible) {
		table.setVisible(visible);
	}

	@Override
	public float getPrefHeight() {
		return table.getPrefHeight();
	}

	@Override
	public float getPrefWidth() {
		return table.getPrefWidth();
	}

	@Override
	public void setSize(float width, float height) {
		table.setSize(width, height);
	}
}
