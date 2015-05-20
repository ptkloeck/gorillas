package de.ptkapps.gorillas.utils;

import java.util.Random;

public class Utils {

	/**
	 * return a random object in the given items array
	 * 
	 * @param items
	 *            the array to choose from
	 * @return a random item in the given array
	 */
	public static Object randomChooseFrom(Object[] items) {

		Random random = new Random();
		int indexOfChosen = random.nextInt(items.length);

		return items[indexOfChosen];
	}
}
