package de.ptkapps.gorillas.gameobjects.destructible.pattern;

import com.badlogic.gdx.math.Vector2;

public interface IDestructionPattern {

	/**
	 * @return The width of the bounding box around the destruction.
	 */
	public int getWidth();
	
	/**
	 * @return The height of the bounding box around the destruction.
	 */
	public int getHeight();

	/**
	 * @return The center of the destruction. Note: The center of the
	 *         destruction is not necessarily the same as the center of the
	 *         bounding box around the pattern.
	 */
	public Vector2 getCenter();

	/**
	 * Determines if a pixel should be erased during the destruction.
	 * 
	 * @param x
	 *            The x-coordinate of the pixel relative to the upper left
	 *            corner of the bounding box around the pattern.
	 * @param y
	 *            The y-coordinate of the pixel relative to the upper left
	 *            corner of the bounding box around the pattern.
	 *            
	 * @return true, if the pixel should be erased, otherwise false
	 */
	public boolean shouldErase(int x, int y);
}
