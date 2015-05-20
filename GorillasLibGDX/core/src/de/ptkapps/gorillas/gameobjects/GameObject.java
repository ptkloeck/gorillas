package de.ptkapps.gorillas.gameobjects;

import com.badlogic.gdx.math.Vector2;

public class GameObject {

	/**
	 * The position of the game object. The y-axis points down.
	 */
	protected Vector2 position;
	
	protected Vector2 size;
	
	public float stateTime;

	public int state;
	
	public Vector2 getSize() {
		return size;
	}
	
	public float getWidth() {
		return size.x;
	}
	
	public float getHeight() {
		return size.y;
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	
	public GameObject(Vector2 position, Vector2 size) {
		this.position = position;
		this.size = size;
	}
	
	public void update(float deltaTime) {
		stateTime += deltaTime;
	}
}
