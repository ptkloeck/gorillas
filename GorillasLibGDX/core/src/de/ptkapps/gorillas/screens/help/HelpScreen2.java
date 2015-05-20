package de.ptkapps.gorillas.screens.help;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.I18NBundle;

import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.options.Options;

public class HelpScreen2 extends HelpScreen {

	public HelpScreen2(Gorillas game) {
		super(game);

		updateTextElements();
		
		gameGUI.initGame();
		gameGUI.hidePlayer1Widget();		
	}

	@Override
	protected void next() {
		game.helpScreen3.setWorldAndWorldRenderer(world, worldRenderer);
		game.setScreen(game.helpScreen3);
	}

	@Override
	protected void previous() {		
		world.paramPlayer1.setBeingAdjusted(false);
		game.setScreen(game.helpScreen1);
	}

	@Override
	public void updateTextElements() {

		I18NBundle messagesBundle = Options.getInstance()
				.getCurrentMessagesBundle();

		helpLabel.setText(messagesBundle.get("help2"));

		super.updateTextElements();
	}

	@Override
	public void show() {

		world.paramPlayer1.setStartPos(new Vector2(0, 0));
		world.paramPlayer1.setEndPos(new Vector2(- Gorillas.worldWidth / 8, - Gorillas.worldHeight / 16));
		world.paramPlayer1.setBeingAdjusted(true);
		
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
