package de.ptkapps.gorillas.screens.help;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.I18NBundle;

import de.ptkapps.gorillas.assets.Assets;
import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.options.Options;
import de.ptkapps.gorillas.world.World;
import de.ptkapps.gorillas.world.WorldRenderer;

public class HelpScreen1 extends HelpScreen {

	public HelpScreen1(Gorillas game) {
		super(game);

		updateTextElements();	
	}

	private void initCity() {
		
		float paneWidth = Gorillas.worldWidth;
		float paneHeight = Gorillas.worldHeight;

		float fifthHeight = paneHeight / 5;
		float fifthWidth = paneWidth / 5;
		
		float scale = 2 / 3f;
		
		ArrayList<Vector2> buildingCoordinates = new ArrayList<Vector2>();
		buildingCoordinates.add(new Vector2(2, fifthHeight));
		buildingCoordinates.add(new Vector2(2 + fifthWidth,
		    fifthHeight * scale));
		buildingCoordinates.add(new Vector2(2 + fifthWidth * 2,
		    fifthHeight * 2 / 3));
		buildingCoordinates.add(new Vector2(2 + fifthWidth * 3,
		    fifthHeight * scale));
		buildingCoordinates.add(new Vector2(2 + fifthWidth * 4,
		    fifthHeight));

		Vector2 gorillaLeftCoordinate = new Vector2(2 + fifthWidth / 2
				- Assets.gorilla.getRegionWidth() / 2, fifthHeight 
				+ Assets.gorilla.getRegionHeight());

		Vector2 gorillaRightCoordinate = new Vector2(2 + fifthWidth * 4
				+ fifthWidth / 2 - Assets.gorilla.getRegionWidth() / 2,
				fifthHeight+ Assets.gorilla.getRegionHeight());

		world.generateLevel(buildingCoordinates, gorillaLeftCoordinate,
				gorillaRightCoordinate, 5);
		
		gameGUI.initGame();
		
		gameGUI.hidePlayer1Widget();
	}

	@Override
	protected void next() {
		game.helpScreen2.setWorldAndWorldRenderer(world, worldRenderer);
		game.setScreen(game.helpScreen2);
	}

	@Override
	protected void previous() {
		game.setScreen(game.mainMenuScreen);
	}

	@Override
	public void updateTextElements() {

		I18NBundle messagesBundle = Options.getInstance()
				.getCurrentMessagesBundle();

		helpLabel.setText(messagesBundle.get("help1"));

		super.updateTextElements();
	}
	
	@Override
	public void show() {
		
		super.show();
	
		if (world == null) {
			world = new World();
			worldRenderer = new WorldRenderer(game.batch, world);
			initCity();
		}	
	}
	
	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, (10 * 16 + 8 * 1) / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		worldRenderer.render();
		super.render(delta);
	}
}
