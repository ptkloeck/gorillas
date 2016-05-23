package de.ptkapps.gorillas.map;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import de.ptkapps.gorillas.gameobjects.City;
import de.ptkapps.gorillas.main.Gorillas;

public class MapGenerator {

    public Map generateMap(int cityWidth, int cityHeight, int yOffsetCity) {

        QBasicCityGenerator cityGenerator = new QBasicCityGenerator();

        City city = cityGenerator.generateCity(cityWidth, cityHeight, yOffsetCity);

        Map map = new Map(city, chooseWindSpeed());

        return map;
    }

    public Map generateMap() {

        int yOffsetCity = Map.Y_OFFSET_CITY;

        int cityWidth = Gorillas.worldWidth;
        int cityHeight = Math.round(Gorillas.worldHeight * 2 / 3f) - yOffsetCity;

        return generateMap(cityWidth, cityHeight, yOffsetCity);
    }

    public Map generateMap(ArrayList<Vector2> buildingCoordinates, Vector2 leftGorillaCoordinate,
            Vector2 rightGorillaCoordinate, int wind) {

        int yOffsetCity = Map.Y_OFFSET_CITY;

        int cityWidth = Gorillas.worldWidth;
        int cityHeight = Math.round(Gorillas.worldHeight * 2 / 3f) - yOffsetCity;

        QBasicCityGenerator cityGenerator = new QBasicCityGenerator();

        City city = cityGenerator.generateCity(cityWidth, cityHeight, yOffsetCity, buildingCoordinates,
                leftGorillaCoordinate, rightGorillaCoordinate);

        Map map = new Map(city, wind);

        return map;
    }

    private int chooseWindSpeed() {

        Random random = new Random();
        int wind = random.nextInt(11) - 5;

        if (random.nextInt(4) == 1) {
            if (wind > 0) {
                wind += random.nextInt(11);
            } else {
                wind -= random.nextInt(11);
            }
        }

        return wind;
    }
}
