package de.ptkapps.gorillas.utils;

import com.badlogic.gdx.math.Vector2;

import de.ptkapps.gorillas.gameobjects.Shot;
import de.ptkapps.gorillas.gameobjects.Shot.Direction;
import de.ptkapps.gorillas.main.Gorillas;
import de.ptkapps.gorillas.options.Options;

public class ShotUtils {

    // wind scaling factor to ensure a realistic wind strength
    private static final float WIND_SCALING_FACTOR = 1 / 5f;

    /**
     * Predicts how far (see the description of the return value) a shot will fly until it hits the
     * screen bottom.<br>
     * <br>
     * At the time the shot reaches the screen bottom the y value of the shot equals the frame
     * height. Thus, following equation has to be solved to determine the time the shot needs to
     * reach the screen bottom (see
     * {@link de.tu_darmstadt.gdi1.gorillas.model.actions.ParabolicFlightAction#updatePosition(Shot shot, int delta)
     * updatePosition} for the right part of the equation).<br>
     * <br>
     * frameHeight = startY - initialYVelocity * t + 1/2 * g * t^2<br>
     * <br>
     * Activation towards 0 generates:<br>
     * <br>
     * 0 = t^2 - initialYVelocity / g - 2 / g * (frameHeight - startY)<br>
     * <br>
     * This equation can be solved with the pq-formula.<br>
     * <br>
     * 
     * @param shot
     *            the shot, whose throw wide should be predicted
     * 
     * @return the x value of the shot at the time the shot reaches the y level of the screen bottom
     */
    public static float predictScreenBottomX(Shot shot) {

        float frameHeight = Gorillas.worldHeight;

        float startY = shot.getStartPosition().y;
        float InitialYVelocity = shot.getSpeedY();
        float g = Options.getInstance().getGravity();

        // use pq-formula to determine t
        float pHalf = InitialYVelocity / g;
        float q = -2f / g * (frameHeight - startY);

        float t = pHalf + (float) Math.sqrt(((float) Math.pow(pHalf, 2)) - q);

        // determine displacement
        float windValue = shot.getWind() * WIND_SCALING_FACTOR;
        float wind = 0.5f * windValue * (float) Math.pow(t, 2);
        float dx;
        if (shot.getDirection() == Direction.LEFT_TO_RIGHT) {
            dx = shot.getSpeedX() * t + wind;
        } else {
            dx = -shot.getSpeedX() * t + wind;
        }

        // determine x position at the time the shot reaches the y level of the
        // screen bottom
        float hitBottomX = shot.getStartPosition().x + dx;

        return hitBottomX;
    }

    /**
     * This method determines the next position of a <em>Shot</em> object, based on the time passed,
     * the initial launching angle, launching speed and the starting position.<br>
     * <br>
     * The displacement is determined by using the following fomulas: <br>
     * x = x_0 + (v_x * t) + wind<br>
     * y = y_0 + (v_y * t) - (1/2 * g * t^2) <br>
     * wind = 1/2 * (windValue / 5) * t^2 <br>
     * <br>
     * Note that the upper formulas have to be slightly adjusted depending on whether the shot flies
     * from right to left or from left to right (upper formulas hold for the case left to right;
     * wind and v_x * t have to be subtracted in the case right to left).
     * 
     * @param shot
     *            The <em>Shot</em> entity.
     * @param delta
     *            The amount of time passed, used to determine how far the object has to move, in
     *            milliseconds. This value will be scaled by factor 1/100 to provide a more
     *            realistic flying curve.
     */
    public static Vector2 determineNextPosition(Shot shot, float delta) {
        float g = Options.getInstance().getGravity();

        // update the current time
        float t = shot.getTimeElapsed() + delta * 8;
        shot.setTimeElapsed(t);

        // determine displacement
        float windValue = shot.getWind() * WIND_SCALING_FACTOR;
        float wind = 0.5f * windValue * (float) Math.pow(t, 2);
        float dx;
        if (shot.getDirection() == Direction.LEFT_TO_RIGHT) {
            dx = shot.getSpeedX() * t + wind;
        } else {
            dx = -shot.getSpeedX() * t + wind;
        }
        float dy = shot.getSpeedY() * t - (0.5f * g * (float) Math.pow(t, 2));

        // calculate and set the new position
        Vector2 pos = new Vector2();
        pos.x = shot.getStartPosition().x + dx;
        pos.y = shot.getStartPosition().y + dy;
        return pos;
    }

    /**
     * calculates the velocity to hit target (x, y) given the start position (x0, y0) the current
     * wind value and the starting angle. Beware: This function only delivers the correct result for
     * a shot from right to left! <br>
     * <br>
     * Starting with the shot position formulas for the case from right to left... <br>
     * (1) x = x_0 - (cos(apha) * v * t) - wind<br>
     * (2) y = y_0 - (sin(alpha) * v * t) + (1/2 * g * t^2) <br>
     * wind = 1/2 * (windValue / 5) * t^2 <br>
     * <br>
     * ... the following formulas for v can be obtained from (1) and (2) by each activating towards
     * v: <br>
     * (3) v = y - y_0 - 1/2 * g * t^2 / (-sin(alpha) * t)<br>
     * (4) v = x - x_0 + 1/2 * wind / 5 * t^2 / (cos(alpha) * t) <br>
     * <br>
     * This leads to:<br>
     * y - y_0 - 1/2 * g * t^2 / (-sin(alpha) * t) = x - x_0 + 1/2 * wind / 5 * t^2 / (cos(alpha) *
     * t).<br>
     * <br>
     * Multiplying with (-sin(alpha) * t) and then with (cos(alpha) *t) leads to:<br>
     * (y - y_0) * cos(alpha) + 1/2 * g * cos(alpha) * t^2 = -(x - x_0) * sin(alpha) + 1/2 *
     * sin(alpha) * wind / 5 * t^2<br>
     * <br>
     * This can be rewritten to:<br>
     * (1/2 * g * cos(alpha) + 1/2 * sin(alpha) * wind/5) * t^2 = -(x * x_0) * sin(alpha) + (y -
     * y_0) * cos(alpha)<br>
     * <br>
     * This formula can be activated towards t, and then t can be used to calculate v using either
     * (3) or (4).<br>
     * <br>
     * 
     * @param x0
     *            the x value of the start position of the shot
     * @param y0
     *            the y value of the start position of the shot
     * @param x
     *            the x value of the position, that should be hit
     * @param y
     *            the y value of the position, that should be hit
     * @param wind
     *            the current wind value of the map
     * @param angle
     *            the starting angle of the shot
     * 
     * @return the velocity needed to hit the target
     */
    public static float determineNeededVelocity(float x0, float y0, float x, float y, float wind, float angle) {

        float g = Options.getInstance().getGravity();

        float alpha = (float) Math.toRadians(angle);

        double z0 = (x - x0) * Math.sin(alpha);
        double z1 = (y - y0) * Math.cos(alpha);

        double z2 = 0.5f * g * Math.cos(alpha);
        double z3 = 0.5f * Math.sin(alpha) * wind / 5f;

        double t = (-z0 + z1) / (z2 - z3);
        t = Math.sqrt(Math.abs(t));

        double w0 = y - y0 - 0.5f * g * Math.pow(t, 2);
        double w1 = Math.sin(alpha) * t;

        return (float) (w0 / -w1);
    }
}
