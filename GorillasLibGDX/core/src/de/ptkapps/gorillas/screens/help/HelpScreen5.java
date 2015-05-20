package de.ptkapps.gorillas.screens.help;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.I18NBundle;

import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.options.Options;

public class HelpScreen5 extends HelpScreen {

	public HelpScreen5(Gorillas game) {
		super(game);

		updateTextElements();

		gameGUI.initGame();
		gameGUI.hidePlayer1Widget();
	}

	@Override
	protected void next() {

		game.setScreen(game.mainMenuScreen);
	}

	@Override
	protected void previous() {

		game.setScreen(game.helpScreen4);
	}

	@Override
	public void updateTextElements() {

		I18NBundle messagesBundle = Options.getInstance()
				.getCurrentMessagesBundle();

		helpLabel.setText(messagesBundle.get("help5"));

		super.updateTextElements();
	}

	@Override
	public void show() {

		super.show();
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, (10 * 16 + 8 * 1) / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		worldRenderer.render();
		super.render(delta);
	}

}
