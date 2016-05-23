package de.ptkapps.gorillas.gameobjects.destructible;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import de.ptkapps.gorillas.gameobjects.GameObject;

public class ImageDestructibleGameObject extends GameObject {

    private DestructionComponent destructionComponent;

    private Vector2 lastHitPosition;

    public Vector2 getLastHitPosition() {
        return lastHitPosition;
    }

    public void setLastHitPosition(Vector2 lastHitPosition) {
        this.lastHitPosition = lastHitPosition;
    }

    public Texture getTexture() {
        return destructionComponent.getTexture();
    }

    public ImageDestructibleGameObject(Vector2 position, Pixmap original, Pixmap destructionPattern,
            String destructionPatternID) {

        super(position, new Vector2(original.getWidth(), original.getHeight()));

        destructionComponent = new DestructionComponent(original, destructionPattern, destructionPatternID);
    }

    public void registerDestructionPattern(Pixmap destructionPattern, String id) {
        destructionComponent.registerDestructionPattern(destructionPattern, id);
    }

    public void switchDestructionPatternTo(String destructionPatternPath) {
        destructionComponent.switchDestructionPatternTo(destructionPatternPath);
    }

    public boolean collides(float x, float y) {

        // calculate the relative coordinates (relative to the upper left corner
        // of the buffer used in the renderer)
        int relX = Math.round(x - getPosition().x);
        int relY = Math.round(getSize().y - (y - this.getPosition().y));

        if (relX < 0 || relY < 0 || relX >= this.getSize().x || relY >= this.getSize().y) {
            return false; // not in buffer area
        }

        // return true, if a pixel is hit that is not fully transparent
        return (destructionComponent.getRGB(relX, relY) & 0x000000FF) != 0;
    }

    /**
     * the gameobject hereby receives destruction according to the active destruction pattern of the
     * destruction component with the center at the given position
     * 
     * @param position
     */
    public void impactAt(Vector2 position) {

        if (position == null) {
            return;
        }

        int relX = Math.round(position.x - getPosition().x);
        int relY = Math.round(getSize().y - (position.y - this.getPosition().y));

        // correct possible slightly negative values, which stem from the
        // bounding box test (e. g. if the banana hits the city image from the
        // top)
        relX = relX < 0 ? 0 : relX;
        relY = relY < 0 ? 0 : relY;

        // remember impact position (global position)
        setLastHitPosition(position.cpy());

        long start = System.currentTimeMillis();
        destructionComponent.applyDestruction(relX, relY);
        Gdx.app.debug("Performance", "Impact took " + (System.currentTimeMillis() - start));
    }

    public void dispose() {
        destructionComponent.dispose();
    }

    public void reload() {
        destructionComponent.reload();
    }
}
