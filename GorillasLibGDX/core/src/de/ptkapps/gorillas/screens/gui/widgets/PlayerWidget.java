package de.ptkapps.gorillas.screens.gui.widgets;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.ptkapps.gorillas.screens.GameScreen;

public abstract class PlayerWidget {

	public enum Position {
		left, right
	}

	protected Position position;

	protected Skin skin;

	protected GameScreen gameScreen;

	protected Stage stage;

	public PlayerWidget(GameScreen gameScreen, Stage stage, Skin skin) {

		this.gameScreen = gameScreen;
		this.stage = stage;
		this.skin = skin;
	}

	public abstract void setPosition(Position position);

	public abstract void setVisible(boolean visible);

	public abstract void clear();

	public abstract void updateTextElements();
	
	public abstract void disable();

	public abstract void inputParam(int angle, int velocity);
}
