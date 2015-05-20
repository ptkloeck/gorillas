package de.ptkapps.gorillas.options;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.I18NBundle;

import de.ptkapps.gorillas.ai.AI.Difficulty;
import de.ptkapps.gorillas.screens.GameScreen.ControlType;

public class Options {

	private static final Locale GERMAN_LOCALE = new Locale("de", "DE");
	private static final Locale ENGLISH_LOCALE = new Locale("en", "US");

	private static final String FILE_NAME = "options.op";

	private static Options options = new Options();

	private I18NBundle germanMessagesBundle;
	private I18NBundle englishMessagesBundle;

	private I18NBundle currentMessagesBundle;

	private Locale currentLocale;

	private String player1Name;
	private String player2Name;

	private ControlType player1Control;
	private ControlType player2Control;

	private Difficulty difficulty;

	private int gravityValue;

	private int rounds;

	private boolean soundOn;

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
		save();
	}

	public String getPlayer1Name() {
		return player1Name;
	}

	public void setPlayer1Name(String player1Name) {
		this.player1Name = player1Name;
		save();
	}

	public String getPlayer2Name() {
		return player2Name;
	}

	public void setPlayer2Name(String player2Name) {
		this.player2Name = player2Name;
		save();
	}

	public ControlType getPlayerOneControl() {
		return player1Control;
	}

	public void setPlayer1Control(ControlType player1Control) {
		this.player1Control = player1Control;
	}

	public ControlType getPlayerTwoControl() {
		return player2Control;
	}

	public void setPlayer2Control(ControlType player2Control) {
		this.player2Control = player2Control;
	}

	public Locale getCurrentLocale() {
		return currentLocale;
	}

	public void setCurrentLocale(Locale currentLocale) {
		this.currentLocale = currentLocale;
		if (currentLocale.equals(GERMAN_LOCALE)) {
			currentMessagesBundle = germanMessagesBundle;
		} else {
			// fallback is english
			currentMessagesBundle = englishMessagesBundle;
		}
		save();
	}

	public I18NBundle getCurrentMessagesBundle() {
		return currentMessagesBundle;
	}

	public int getGravity() {
		return gravityValue;
	}

	public void setGravity(int gravityValue) {
		this.gravityValue = gravityValue;
		save();
	}

	public int getRounds() {
		return rounds;
	}

	public void setRounds(int rounds) {
		this.rounds = rounds;
		save();
	}

	public boolean isSoundOn() {
		return soundOn;
	}

	public void setSoundOn(boolean soundOn) {
		this.soundOn = soundOn;
		save();
	}

	private Options() {
		this.load();
	}

	private void setDefaultValues() {

		player1Name = "Player1";
		player2Name = "Player2";

		player1Control = ControlType.MODERN;
		player2Control = ControlType.MODERN;

		currentLocale = ENGLISH_LOCALE;
		// check the locale of the OS
		if (Locale.getDefault().equals(GERMAN_LOCALE)) {
			currentLocale = GERMAN_LOCALE;
		}

		difficulty = Difficulty.DIFFICULT;

		gravityValue = 10;
		rounds = 5;
		soundOn = true;
	}

	/**
	 * Saves the Options
	 */
	public void save() {

		FileHandle fileHandle = Gdx.files.local(FILE_NAME);

		try {

			OutputStream outputStream = fileHandle.write(false);
			ObjectOutputStream oos = new ObjectOutputStream(outputStream);

			oos.writeObject(player1Name);
			oos.writeObject(player2Name);
			oos.writeObject(player1Control);
			oos.writeObject(player2Control);
			oos.writeObject(currentLocale);
			oos.writeInt(gravityValue);
			oos.writeInt(rounds);
			oos.writeBoolean(soundOn);
			oos.writeObject(difficulty);

			oos.close();

		} catch (IOException | GdxRuntimeException e) {
			Gdx.app.error(
					"SettingsSaveError",
					"Current change of the settings could not be saved. The change will be lost after the application is closed.",
					e);
		}
	}

	/**
	 * Loads the options
	 */
	public void load() {

		FileHandle baseFileHandle = Gdx.files
				.internal("text/MessagesBundle_de_DE");
		germanMessagesBundle = I18NBundle.createBundle(baseFileHandle,
				GERMAN_LOCALE, "ISO-8859-1");

		baseFileHandle = Gdx.files.internal("text/MessagesBundle_en_US");
		englishMessagesBundle = I18NBundle.createBundle(baseFileHandle,
				ENGLISH_LOCALE, "ISO-8859-1");

		FileHandle fileHandle = Gdx.files.local(FILE_NAME);

		try {
			InputStream inputStream = fileHandle.read();

			ObjectInputStream ois = new ObjectInputStream(inputStream);

			player1Name = (String) ois.readObject();
			player2Name = (String) ois.readObject();
			player1Control = (ControlType) ois.readObject();
			player2Control = (ControlType) ois.readObject();
			currentLocale = (Locale) ois.readObject();
			gravityValue = ois.readInt();
			rounds = ois.readInt();
			soundOn = ois.readBoolean();
			difficulty = (Difficulty) ois.readObject();

			ois.close();

		} catch (IOException | ClassNotFoundException | ClassCastException
				| GdxRuntimeException e) {
			setDefaultValues();
			Gdx.app.error(
					"SettingsLoadError",
					"The settings could not be loaded. Hence, the standard values will be used. (At first startup of the app ever this error is normal, since no settings were saved any time before.",
					e);
		}

		if (currentLocale.equals(GERMAN_LOCALE)) {
			currentMessagesBundle = germanMessagesBundle;
		} else {
			currentMessagesBundle = englishMessagesBundle;
		}
	}

	public static Options getInstance() {
		return options;
	}
}
