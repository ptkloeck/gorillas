package de.ptkapps.gorillas.mocking;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.I18NBundle;

import de.ptkapps.gorillas.assets.Assets;
import de.ptkapps.gorillas.gameobjects.Gorilla;
import de.ptkapps.gorillas.gameobjects.Shot;
import de.ptkapps.gorillas.gameobjects.Shot.Direction;
import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.options.Options;
import de.ptkapps.gorillas.screens.gui.GameGUI;
import de.ptkapps.gorillas.utils.ShotUtils;
import de.ptkapps.gorillas.utils.Utils;

/**
 * static class with methods to generate a failure message and sounds for a
 * finished throw
 */
public final class Mocker {

	private GameGUI gameGUI;

	public Mocker(GameGUI gameGUI) {

		this.gameGUI = gameGUI;
	}

	/**
	 * generates a failure message and sounds in case a shot leaves the screen
	 * without having hit anything
	 * 
	 * @param shot
	 *            the shot to generate failure message for
	 */
	public void shotLeftScreen(Shot shot) {

		float hitBottomX = ShotUtils.predictScreenBottomX(shot);

		// calculate distance to next vertical screen border
		float screenBorderDist;
		if (shot.getDirection() == Direction.RIGHT_TO_LEFT) {
			screenBorderDist = Math.abs(hitBottomX);
		} else {
			screenBorderDist = hitBottomX - Gorillas.worldHeight;
		}

		String message = "";

		I18NBundle messagesBundle = Options.getInstance()
				.getCurrentMessagesBundle();

		Vector2 shotPosition = shot.getPosition();

		if (shotPosition.x < 0
				&& shot.getDirection() == Direction.LEFT_TO_RIGHT
				|| shotPosition.x > Gorillas.worldWidth
				&& shot.getDirection() == Direction.RIGHT_TO_LEFT) {

			if (Options.getInstance().isSoundOn()) {
				Assets.wrongWay.play(Gorillas.VOLUME);
			}

			message = messagesBundle.get("notThatWay");
			gameGUI.setMessage(message);
			return;
		}

		// In the following: message generation code from the QBasic version
		// beginning in line 2228 ported to java.
		if (shotPosition.y > 0) {

			if (screenBorderDist >= 1 && screenBorderDist <= 155) {

				message = (String) Utils.randomChooseFrom(new String[] {
						messagesBundle.get("slightlyTooFar1"),
						messagesBundle.get("slightlyTooFar2") });

			} else if (screenBorderDist >= 156 && screenBorderDist <= 640) {

				message = (String) Utils.randomChooseFrom(new String[] {
						messagesBundle.get("glasses"),
						messagesBundle.get("hmm") });

			} else if (screenBorderDist >= 641 && screenBorderDist <= 1500) {

				message = messagesBundle.get("milesOff");

			} else if (screenBorderDist > 1500) {

				message = (String) Utils.randomChooseFrom(new String[] {
						messagesBundle.get("playingAtWhat"),
						messagesBundle.get("temper") });
			}
		} else {

			if (screenBorderDist >= 1 && screenBorderDist <= 155) {

				message = messagesBundle.get("chance");

			} else if (screenBorderDist >= 156 && screenBorderDist <= 640) {

				message = (String) Utils
						.randomChooseFrom(new String[] {
								messagesBundle.get("nope"),
								messagesBundle.get("hello") });

			} else if (screenBorderDist >= 641 && screenBorderDist <= 1500) {

				message = (String) Utils.randomChooseFrom(new String[] {
						messagesBundle.get("goEasy"),
						messagesBundle.get("joking") });

			} else if (screenBorderDist > 1500) {

				message = messagesBundle.get("inOrbit");
			}
		}

		gameGUI.setMessage(message);
	}

	/**
	 * generates a failure message in case the city was hit by the shot
	 * 
	 * @param shot
	 *            the shot, which collided with the city
	 * @param aim
	 *            the Gorilla which was aimed for
	 */
	public void cityHit(Shot shot, Gorilla aim) {

		String message = "";

		I18NBundle messagesBundle = Options.getInstance()
				.getCurrentMessagesBundle();

		// if the shot collided with the city more than 150 m away from the aim
		// set a failure message
		if (Math.abs(aim.getPosition().x - shot.getPosition().x) > 150) {

			if (Options.getInstance().isSoundOn()) {
				Assets.tooWeak.play(Gorillas.VOLUME);
			}

			message = (String) Utils.randomChooseFrom(new String[] {
					messagesBundle.get("tooWeak"),
					messagesBundle.get("feeble"),
					messagesBundle.get("canBetter") });
		}

		gameGUI.setMessage(message);
	}

	/**
	 * generates a failure message in case a gorilla was hit
	 * 
	 * @param winner
	 *            the Gorilla who has won the round
	 * 
	 * @param thrower
	 *            the Gorilla who threw the banana
	 */
	public void gorillaHit(Gorilla winner, Gorilla thrower) {

		String message = "";

		I18NBundle messagesBundle = Options.getInstance()
				.getCurrentMessagesBundle();

		if (!thrower.equals(winner)) {

			message = messagesBundle.get("selfHit");

		} else if (winner.numShotsFired == 1) {

			message = messagesBundle.get("inOneThrow");
		}

		gameGUI.setMessage(message);
	}
}
