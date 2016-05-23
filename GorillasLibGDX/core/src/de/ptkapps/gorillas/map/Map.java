package de.ptkapps.gorillas.map;

import de.ptkapps.gorillas.gameobjects.City;

public class Map {

    public static int Y_OFFSET_CITY = 20;
    
	private int wind;
	
	private City city;
	
	public float getWind() {
		return wind;
	}

	public void setWind(int wind) {
		this.wind = wind;	
	}
	
	public City getCity() {
		return city;
	}
	
	public Map(City city, int wind) {

		this.city = city;
		this.wind = wind;
	}	
}
