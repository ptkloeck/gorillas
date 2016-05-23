package de.ptkapps.gorillas.gameobjects.factory;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

import de.ptkapps.gorillas.assets.Assets;
import de.ptkapps.gorillas.gameobjects.City;
import de.ptkapps.gorillas.gameobjects.Gorilla;
import de.ptkapps.gorillas.gameobjects.Shot.Direction;

public class GorillaFactory {

    public static final float SHOT_DISTORTION_Y = 10;

    /**
     * Creates a new gorilla.
     * 
     * @param position
     *            The position of the gorilla.
     * @param direction
     *            The direction of the shot.
     * @return The created gorilla.
     */
    public static Gorilla createGorilla(Vector2 position, Direction direction) {

        Pixmap original = Assets.originalGorilla;
        Pixmap destructionPattern = Assets.gorillaCityDestruction;

        float shotStartX;
        if (direction == Direction.LEFT_TO_RIGHT) {
            shotStartX = position.x;
        } else {
            shotStartX = position.x + original.getWidth() - Assets.banana.getRegionWidth();
        }
        Vector2 shotStartPosition = new Vector2(shotStartX, position.y + original.getHeight()
                + Assets.banana.getRegionWidth() + SHOT_DISTORTION_Y);
        Gorilla gorilla = new Gorilla(position, original, destructionPattern, City.gorillaDestrPatternID,
                direction, shotStartPosition);

        return gorilla;
    }
}
