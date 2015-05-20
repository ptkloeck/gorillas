package de.ptkapps.gorillas.screens.help;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.I18NBundle;

import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.options.Options;
import de.ptkapps.gorillas.screens.GorillasScreen;
import de.ptkapps.gorillas.screens.gui.GUIConstants;
import de.ptkapps.gorillas.screens.gui.GameGUI;
import de.ptkapps.gorillas.world.World;
import de.ptkapps.gorillas.world.WorldRenderer;

public abstract class HelpScreen extends GorillasScreen {

	protected TextButton previousButton;
	protected TextButton nextButton;

	protected Table table;
	protected Label helpLabel;

	protected World world;
	protected WorldRenderer worldRenderer;

	protected GameGUI gameGUI;

	public void setWorldAndWorldRenderer(World world, WorldRenderer worldRenderer) {
		this.world = world;
		this.worldRenderer = worldRenderer;
	}

	public HelpScreen(Gorillas game) {
		super(game);

		previousButton = new TextButton("", game.skin);
		previousButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				previous();
			}
		});
		stage.addActor(previousButton);

		nextButton = new TextButton("", game.skin);
		nextButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				next();
			}
		});
		stage.addActor(nextButton);

		table = new Table(game.skin);
		table.setBackground("menuBackground");

		helpLabel = new Label("", game.skin);
		helpLabel.setColor(Color.BLACK);
		helpLabel.setWrap(true);
		helpLabel.setAlignment(Align.center);

		table.add(helpLabel);

		stage.addActor(table);

		gameGUI = new GameGUI(null, stage, game.skin);
	}

	@Override
	public void updateTextElements() {

		gameGUI.updateTextElements();

		I18NBundle messagesBundle = Options.getInstance()
				.getCurrentMessagesBundle();

		previousButton.setText(messagesBundle.get("previous"));
		nextButton.setText(messagesBundle.get("next"));

		layout();
	}

	private void layout() {

		int gap = 10;
		int paneWidth = (int) Gorillas.worldWidth;
		int paneHeight = (int) Gorillas.worldHeight;

		previousButton.setWidth(previousButton.getPrefWidth()
				+ GUIConstants.DOS_BUTTON_CORRECT_X);
		previousButton.setHeight(previousButton.getPrefHeight());
		previousButton.setPosition(gap,
				paneHeight * 2 / 3.5f - previousButton.getPrefHeight() / 2);

		nextButton.setWidth(nextButton.getPrefWidth()
				+ GUIConstants.DOS_BUTTON_CORRECT_X);
		nextButton.setHeight(nextButton.getPrefHeight());
		nextButton.setPosition(paneWidth - nextButton.getWidth() - gap,
				paneHeight * 2 / 3.5f - nextButton.getPrefHeight() / 2);

		// set each wrap to false to not receive 0 as preferred width
		helpLabel.setWrap(false);

		float helpLabelWidth = helpLabel.getPrefWidth();

		if (helpLabelWidth > Gorillas.worldWidth / 2.3f) {
			helpLabelWidth = Gorillas.worldWidth / 2.3f;
		}

		helpLabel.setWrap(true);

		table.getCell(helpLabel).width(helpLabelWidth).fill();

		table.pack();

		float tableWidth = table.getPrefWidth();
		float tableHeight = table.getPrefHeight();

		table.setPosition((int) (Gorillas.worldWidth / 2 - tableWidth / 2),
				(int) (Gorillas.worldHeight * 2 / 3.5f  - tableHeight / 2));
	}

	@Override
	protected void handleBackKey() {
		game.setScreen(game.mainMenuScreen);
	}

	protected abstract void next();

	protected abstract void previous();

	@Override
	public void dispose() {

		if (world != null) {
			world.dispose();
			worldRenderer.dispose();
		}
		super.dispose();
	}
	
	@Override
	public void resume() {

		if (world != null) {
			world.resume();
		}
		super.resume();
	}
}
