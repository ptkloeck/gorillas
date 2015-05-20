package de.ptkapps.gorillas.screens.help;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.I18NBundle;

import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.options.Options;

public class HelpScreen4 extends HelpScreen {

	public HelpScreen4(Gorillas game) {
		super(game);

		updateTextElements();

		gameGUI.initGame();
		gameGUI.hidePlayer1Widget();
	}

	@Override
	protected void next() {

		game.helpScreen5.setWorldAndWorldRenderer(world, worldRenderer);
		game.setScreen(game.helpScreen5);
	}

	@Override
	protected void previous() {

		game.setScreen(game.helpScreen3);
	}

	@Override
	public void updateTextElements() {

		I18NBundle messagesBundle = Options.getInstance()
				.getCurrentMessagesBundle();

		helpLabel.setText(messagesBundle.get("help4"));

		super.updateTextElements();
	}

	@Override
	public void show() {

		super.show();
		
		gameGUI.showPlayer1Widget();
		gameGUI.disablePlayer1Widget();
		gameGUI.inputParamPlayer1(45, 80);
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, (10 * 16 + 8 * 1) / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		worldRenderer.render();
		super.render(delta);
	}

}
