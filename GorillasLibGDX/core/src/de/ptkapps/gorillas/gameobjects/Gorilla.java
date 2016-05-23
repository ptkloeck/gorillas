package de.ptkapps.gorillas.gameobjects;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

import de.ptkapps.gorillas.gameobjects.Shot.Direction;
import de.ptkapps.gorillas.gameobjects.destructible.ImageDestructibleGameObject;

public class Gorilla extends ImageDestructibleGameObject {

    public static final int GORILLA_STATE_NORMAL = 0;
    public static final int GORILLA_STATE_THROWING = 1;
    public static final int GORILLA_STATE_CHEERING = 2;
    public static final int GORILLA_STATE_DESTRUCTED = 3;

    /**
     * The direction of the shot.
     */
    protected Direction direction;

    /**
     * The position as <em>Vector2f(x, y)</em> where the shot starts.
     */
    protected Vector2 shotStartPosition;

    protected Vector2 position;

    /**
     * the number of shots the gorilla has fired so far in the current level
     */
    public int numShotsFired;

    /**
     * @param direction
     *            The direction of the shot.
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * @return The direction of the shot.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * @param startPosition
     *            The position as <em>Vector2(x, y)</em> where the shot starts.
     */
    public void setShotStartPosition(Vector2 startPosition) {
        this.shotStartPosition = startPosition;
    }

    /**
     * @return The position as <em>Vector2f(x, y)</em> where the shot starts.
     */
    public Vector2 getShotStartPosition() {
        return shotStartPosition;
    }

    /**
     * Constructor
     * 
     * @param position
     *            the position of the gorilla
     * @param original
     *            the original image represented as pixmap
     * @param destructionPattern
     *            the destructionPattern represented as pixmap
     * @param destructionPatternID
     *            the ID for the given <code>destructionPattern</code>
     * @param direction
     *            The direction of the shot.
     * @param shotStartPosition
     *            The relative position as <em>Vector2(x, y)</em> where the shot starts. Relative
     *            means here releative to the position of the gorilla entity.
     * 
     * @see {@link Shot.Direction}
     */
    public Gorilla(Vector2 position, Pixmap original, Pixmap destructionPattern, String destructionPatternID,
            Direction direction, Vector2 shotStartPosition) {

        super(position, original, destructionPattern, destructionPatternID);
        this.direction = direction;
        this.shotStartPosition = shotStartPosition;
        numShotsFired = 0;
        this.state = GORILLA_STATE_NORMAL;
    }

    public void cheer() {
        state = GORILLA_STATE_CHEERING;
        stateTime = 0;
    }

    public void startThrow() {
        state = GORILLA_STATE_THROWING;
        stateTime = 0;
    }
}
