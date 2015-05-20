package de.ptkapps.gorillas.screens.gui.widgets;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class NumberInput extends Widget {

	public static final int PAD = 2;

	public interface NumberFieldInputListener {

		public void numberEntered(int number);

		public void cePressed();

		public void enterPressed();
	}

	private class NumberInputListener extends InputListener {

		protected int number;

		public NumberInputListener(int number) {
			this.number = number;
		}
	}

	private NumberFieldInputListener numberFieldInputListener;

	private Table table;

	public void setNumberFieldInputListener(
			NumberFieldInputListener numberFieldInputListener) {
		this.numberFieldInputListener = numberFieldInputListener;
	}

	public NumberInput(Stage stage, Skin skin, float buttonWidth,
			float buttonHeight) {

		TextButton[] numberButtons = generateNumberButtons(skin);

		TextButton ceButton = new TextButton("CE", skin, "numberField");
		ceButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				numberFieldInputListener.cePressed();
				return true;
			}
		});

		TextButton enterButton = new TextButton("Enter", skin, "numberField");
		enterButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				numberFieldInputListener.enterPressed();
				return true;
			}
		});

		table = new Table(skin);
		
		table.setBackground("textfield2");
		
		table.add(numberButtons[7]).height(buttonHeight).width(buttonWidth)
				.pad(PAD / 2f).padTop(PAD);
		table.add(numberButtons[8]).height(buttonHeight).width(buttonWidth)
				.pad(PAD / 2f);
		table.add(numberButtons[9]).height(buttonHeight).width(buttonWidth)
				.pad(PAD / 2f);
		table.row();
		table.add(numberButtons[4]).height(buttonHeight).width(buttonWidth)
				.pad(PAD / 2f);
		table.add(numberButtons[5]).height(buttonHeight).width(buttonWidth)
				.pad(PAD / 2f);	
		table.add(numberButtons[6]).height(buttonHeight).width(buttonWidth)
				.pad(PAD / 2f);
		table.row();
		table.add(numberButtons[1]).height(buttonHeight).width(buttonWidth)
				.pad(PAD / 2f);
		table.add(numberButtons[2]).height(buttonHeight).width(buttonWidth)
				.pad(PAD / 2f);
		table.add(numberButtons[3]).height(buttonHeight).width(buttonWidth)
				.pad(PAD / 2f);
		table.row();		
		table.add(ceButton).height(buttonHeight).pad(PAD / 2f)
				.fill();
		table.add(numberButtons[0]).height(buttonHeight).width(buttonWidth)
		.pad(PAD / 2f);		
		table.add(enterButton).height(buttonHeight).pad(PAD / 2f)
				.fill();

		table.setVisible(false);
		stage.addActor(table);
	}

	private TextButton[] generateNumberButtons(Skin skin) {

		TextButton[] numberButtons = new TextButton[10];

		for (int i = 0; i < 10; i++) {

			TextButton numberButton = new TextButton(Integer.toString(i), skin,
					"numberField");
			numberButton.addListener(new NumberInputListener(i) {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					numberFieldInputListener.numberEntered(number);
					return true;
				}
			});

			numberButtons[i] = numberButton;
		}

		return numberButtons;
	}

	@Override
	public void setPosition(float x, float y) {
		table.setPosition(x, y);
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
