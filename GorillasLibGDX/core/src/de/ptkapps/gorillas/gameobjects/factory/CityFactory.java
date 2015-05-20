package de.ptkapps.gorillas.gameobjects.factory;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

import de.ptkapps.gorillas.assets.Assets;
import de.ptkapps.gorillas.gameobjects.City;

public class CityFactory {

	public static City createCity(Vector2 position, Pixmap original, 
			ArrayList<Vector2> buildingCoordinates) {

		City city = new City(position, original, Assets.bananaCityDestruction, City.bananaDestrPatternID, buildingCoordinates);

		city.registerDestructionPattern(Assets.gorillaCityDestruction, City.gorillaDestrPatternID);

		return city;
	}

	public static City createCity(Vector2 position, Pixmap original,
			ArrayList<Vector2> buildingCoordinates,
			Vector2 leftGorillaCoordinate, Vector2 rightGorillaCoordinate) {

		City city = new City(position, original, Assets.bananaCityDestruction, City.bananaDestrPatternID, buildingCoordinates,
				leftGorillaCoordinate, rightGorillaCoordinate);

		city.registerDestructionPattern(Assets.gorillaCityDestruction, City.gorillaDestrPatternID);

		return city;
	}
}
