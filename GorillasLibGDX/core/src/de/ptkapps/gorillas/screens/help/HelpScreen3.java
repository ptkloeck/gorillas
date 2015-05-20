package de.ptkapps.gorillas.screens.help;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.I18NBundle;

import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.options.Options;

public class HelpScreen3 extends HelpScreen {

	public HelpScreen3(Gorillas game) {
		super(game);

		updateTextElements();

		gameGUI.initGame();
		gameGUI.hidePlayer1Widget();
	}

	@Override
	protected void next() {
		game.helpScreen4.setWorldAndWorldRenderer(world, worldRenderer);
		game.setScreen(game.helpScreen4);
	}

	@Override
	protected void previous() {
		game.setScreen(game.helpScreen2);
	}

	@Override
	public void updateTextElements() {

		I18NBundle messagesBundle = Options.getInstance()
				.getCurrentMessagesBundle();

		helpLabel.setText(messagesBundle.get("help3"));

		super.updateTextElements();
	}

	@Override
	public void show() {

		world.paramPlayer1.setBeingAdjusted(false);

		gameGUI.showPlayer1Widget();		
		gameGUI.inputParamPlayer1(45, 80);
		gameGUI.disablePlayer1Widget();
		
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
