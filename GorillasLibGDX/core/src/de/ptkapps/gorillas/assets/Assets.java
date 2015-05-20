package de.ptkapps.gorillas.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.ptkapps.gorillas.main.Gorillas.ScreenSize;

public class Assets {

	public static final String DESTRUCTION_PATH = "destruction.png";
	public static final String GORILLA_DESTRUCTION_PATH = "gorilla_destruction.png";
	public static final String GORILLA_PATH = "gorilla.png";

	public static final String GORILLA = "gorilla";
	public static final String GORILLA_LEFT_UP = "gorilla_left_arm_up";
	public static final String GORILLA_RIGHT_UP = "gorilla_right_arm_up";
	public static final String GORILLA_DESTRUCTED = "gorilla_destructed";

	public static final String EXPLOSION_1 = "explosion_1";
	public static final String EXPLOSION_2 = "explosion_2";

	public static final String BANANA = "banana";

	public static final String SUN_SMILING = "sun_smiling";
	public static final String SUN_ASTONISHED = "sun_astonished";

	public static final String UNION_JACK = "union_jack";
	public static final String GERMAN = "german_flag";

	public static final String SOUND_ON = "sound_on";
	public static final String SOUND_OFF = "sound_off";

	public static Pixmap bananaCityDestruction;
	public static Pixmap gorillaCityDestruction;

	public static Pixmap originalGorilla;

	public static TextureRegion gorilla;
	public static TextureRegion gorillaLeftArmUp;
	public static TextureRegion gorillaRightArmUp;
	public static TextureRegion gorillaDestructed;

	public static Animation cheeringGorilla;
	public static Animation leftThrowingGorilla;
	public static Animation rightThrowingGorilla;
	public static TextureRegion cityDestruction1;
	public static TextureRegion cityDestruction2;
	public static Animation cityDestruction;
	public static TextureRegion banana;
	public static TextureRegion sunSmiling;
	public static TextureRegion sunAstonished;

	public static TextureRegion britishFlag;
	public static TextureRegion germanFlag;

	public static TextureRegion soundOn;
	public static TextureRegion soundOff;

	public static Sound bounce;
	public static Sound cityHit;
	public static Sound gameStart;
	public static Sound gorillaExplode;
	public static Sound inOneThrow;
	public static Sound mainTheme;
	public static Sound throwStart;
	public static Sound tooWeak;
	public static Sound velocityRanOut;
	public static Sound victoryDance;
	public static Sound wrongWay;

	public static String pathPrefix;

	public static void load(AssetManager manager, ScreenSize screenSize) {

		pathPrefix = "pngs/";
		switch (screenSize) {
		case SMALL:
			pathPrefix = pathPrefix.concat("ldpi/");
			break;
		case NORMAL:
			pathPrefix = pathPrefix.concat("mdpi/");
			break;
		case LARGE:
			pathPrefix = pathPrefix.concat("hdpi/");
			break;
		case XLARGE:
			pathPrefix = pathPrefix.concat("xhdpi/");
			break;
		default:
			pathPrefix = pathPrefix.concat("xxhdpi/");
			break;
		}

		manager.load(pathPrefix + DESTRUCTION_PATH, Pixmap.class);
		manager.load(pathPrefix + GORILLA_DESTRUCTION_PATH, Pixmap.class);
		manager.load(pathPrefix + GORILLA_PATH, Pixmap.class);

		manager.load(pathPrefix + "items.pack", TextureAtlas.class);

		manager.load("sounds/bounce.wav", Sound.class);
		manager.load("sounds/cityHit.wav", Sound.class);
		manager.load("sounds/gameStart.wav", Sound.class);
		manager.load("sounds/gorillaExplode.wav", Sound.class);
		manager.load("sounds/inOneThrow.wav", Sound.class);
		manager.load("sounds/mainTheme.wav", Sound.class);
		manager.load("sounds/throwStart.wav", Sound.class);
		manager.load("sounds/tooWeak.wav", Sound.class);
		manager.load("sounds/velocityRanOut.wav", Sound.class);
		manager.load("sounds/victoryDance.wav", Sound.class);
		manager.load("sounds/wrongWay.wav", Sound.class);
	}

	/**
	 * creates TextureRegions and Animations from the Textures
	 * loaded by the given AssetManager and makes references to the Sounds and Pixmaps. ATTENTION: the
	 * manager has to have finished loading the assets.
	 * 
	 * @param manager
	 *            the AssetManager, which has finished loading the assets of the
	 *            game
	 */
	public static void finishSetup(AssetManager manager) {

		bananaCityDestruction = manager.get(pathPrefix + DESTRUCTION_PATH,
				Pixmap.class);
		gorillaCityDestruction = manager.get(pathPrefix
				+ GORILLA_DESTRUCTION_PATH, Pixmap.class);
		originalGorilla = manager.get(pathPrefix + GORILLA_PATH, Pixmap.class);

		gorilla = manager.get(pathPrefix + "items.pack",
				TextureAtlas.class).findRegion(GORILLA);
		gorilla.flip(false, true);
		gorillaLeftArmUp = manager.get(
				pathPrefix + "items.pack", TextureAtlas.class).findRegion(
				GORILLA_LEFT_UP);
		gorillaLeftArmUp.flip(false, true);
		gorillaRightArmUp = manager.get(
				pathPrefix + "items.pack", TextureAtlas.class).findRegion(
				GORILLA_RIGHT_UP);
		gorillaRightArmUp.flip(false, true);

		gorillaDestructed = manager.get(
				pathPrefix + "items.pack", TextureAtlas.class).findRegion(
				GORILLA_DESTRUCTED);
		gorillaDestructed.flip(false, true);

		cheeringGorilla = new Animation(0.2f, gorillaLeftArmUp,
				gorillaRightArmUp, gorillaLeftArmUp, gorillaRightArmUp,
				gorillaLeftArmUp, gorillaRightArmUp, gorillaLeftArmUp,
				gorillaRightArmUp, gorillaLeftArmUp, gorillaRightArmUp,
				gorillaLeftArmUp, gorillaRightArmUp);

		leftThrowingGorilla = new Animation(0.3f, gorillaLeftArmUp, gorilla);
		rightThrowingGorilla = new Animation(0.3f, gorillaRightArmUp, gorilla);

		cityDestruction1 = manager.get(
				pathPrefix + "items.pack", TextureAtlas.class).findRegion(
				EXPLOSION_1);
		cityDestruction2 = manager.get(
				pathPrefix + "items.pack", TextureAtlas.class).findRegion(
				EXPLOSION_2);
		cityDestruction = new Animation(0.2f, cityDestruction1,
				cityDestruction2);

		banana = new TextureRegion(manager.get(pathPrefix + "items.pack",
				TextureAtlas.class).findRegion(BANANA));

		sunSmiling = manager.get(pathPrefix + "items.pack",
				TextureAtlas.class).findRegion(SUN_SMILING);
		sunSmiling.flip(false, true);
		sunAstonished = manager.get(
				pathPrefix + "items.pack", TextureAtlas.class).findRegion(
				SUN_ASTONISHED);
		sunAstonished.flip(false, true);

		britishFlag = manager.get(pathPrefix + "items.pack",
				TextureAtlas.class).findRegion(UNION_JACK);
		germanFlag = manager.get(pathPrefix + "items.pack",
				TextureAtlas.class).findRegion(GERMAN);

		soundOn = manager.get(pathPrefix + "items.pack",
				TextureAtlas.class).findRegion(SOUND_ON);
		soundOff = manager.get(pathPrefix + "items.pack",
				TextureAtlas.class).findRegion(SOUND_OFF);

		bounce = manager.get("sounds/bounce.wav", Sound.class);
		cityHit = manager.get("sounds/cityHit.wav", Sound.class);
		gameStart = manager.get("sounds/gameStart.wav", Sound.class);
		gorillaExplode = manager.get("sounds/gorillaExplode.wav", Sound.class);
		inOneThrow = manager.get("sounds/inOneThrow.wav", Sound.class);
		mainTheme = manager.get("sounds/mainTheme.wav", Sound.class);
		throwStart = manager.get("sounds/throwStart.wav", Sound.class);
		tooWeak = manager.get("sounds/tooWeak.wav", Sound.class);
		velocityRanOut = manager.get("sounds/velocityRanOut.wav", Sound.class);
		victoryDance = manager.get("sounds/victoryDance.wav", Sound.class);
		wrongWay = manager.get("sounds/wrongWay.wav", Sound.class);
	}
}
