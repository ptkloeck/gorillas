package de.ptkapps.gorillas.gameobjects.destructible;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.BufferUtils;

import de.ptkapps.gorillas.assets.Assets;
import de.ptkapps.gorillas.gameobjects.destructible.pattern.IDestructionPattern;
import de.ptkapps.gorillas.gameobjects.destructible.pattern.ImageDestructionPattern;

public class DestructionComponent {

	private Pixmap result;

	private ByteBuffer transferBuffer;

	private Texture texture;

	private HashMap<String, ImageDestructionPattern> destructionPatterns;

	private ImageDestructionPattern activeDestructionPattern;

	/**
	 * @param pattern
	 *            The destruction pattern that is used when
	 *            {@link #applyDestruction(int, int)} called.
	 * @see {@link #applyDestruction(int, int)}
	 */
	public void setDestructionPattern(ImageDestructionPattern pattern) {
		this.activeDestructionPattern = pattern;
	}

	/**
	 * @return The destruction pattern that is used when
	 *         {@link #applyDestruction(int, int)} called.
	 * @see {@link #applyDestruction(int, int)}
	 */
	public IDestructionPattern getDestructionPattern() {
		return activeDestructionPattern;
	}

	public DestructionComponent(Pixmap original, Pixmap destructionPattern,
			String destructionPatternID) {

		activeDestructionPattern = new ImageDestructionPattern(
				destructionPattern);
		destructionPatterns = new HashMap<String, ImageDestructionPattern>();
		registerDestructionPattern(destructionPatternID,
				activeDestructionPattern);

		texture = new Texture(original);

		result = original;
		result.setColor(0f, 0f, 0f, 0f);
		Pixmap.setBlending(Blending.None);

		if (Gdx.app.getType().equals(ApplicationType.Desktop)) {
			// allocate as much bytes as needed to store the biggest destruction
			// image. ATTENTION: if a pattern bigger than the
			// gorillaCityDestruction pattern and the
			// bananaCityDestructionPattern is introduced the following has to
			// be changed.
			int gorillaDestructionSize = Assets.gorillaCityDestruction
					.getWidth() * Assets.gorillaCityDestruction.getHeight();
			int bananaDestructionSize = Assets.bananaCityDestruction.getWidth()
					* Assets.bananaCityDestruction.getHeight();

			transferBuffer = BufferUtils
					.newByteBuffer((gorillaDestructionSize > bananaDestructionSize ? gorillaDestructionSize
							: bananaDestructionSize) * 4);
			transferBuffer.order(ByteOrder.BIG_ENDIAN);
		}
	}

	public void registerDestructionPattern(String id,
			ImageDestructionPattern destructionPattern) {
		destructionPatterns.put(id, destructionPattern);
	}

	public void registerDestructionPattern(Pixmap destructionPattern, String id) {
		destructionPatterns.put(id, new ImageDestructionPattern(
				destructionPattern));
	}

	public void switchDestructionPatternTo(String destructionPatternID) {

		if (destructionPatterns.containsKey(destructionPatternID)) {
			activeDestructionPattern = destructionPatterns
					.get(destructionPatternID);
		}
	}

	/**
	 * Returns an integer pixel in the default RGB color model (TYPE_INT_RGBA)
	 * 
	 * @param x
	 *            The x-coordinate of the pixel from which to get the pixel.
	 * @param y
	 *            The y-coordinate of the pixel from which to get the pixel.
	 * @return An integer pixel in the default RGB color model
	 */
	public int getRGB(int x, int y) {

		if (result != null) {
			int color = result.getPixel(x, y);
			return color;

		} else {
			return 0;
		}
	}

	/**
	 * 
	 * @param centerX
	 * @param centerY
	 */
	public void applyDestruction(int centerX, int centerY) {

		int startX = centerX
				- Math.round(activeDestructionPattern.getCenter().x);
		int startY = centerY
				- Math.round(activeDestructionPattern.getCenter().y);

		// if startX or startY are negative they have to be set to 0, but negX
		// and negY have to be set as offset for the application of the image
		// destruction pattern
		int negX = 0;
		int negY = 0;

		// check and correct startX
		if (startX < 0) {
			negX = Math.abs(startX);
			startX = 0;
		} else if (startX >= texture.getWidth()) {
			return; // pattern out of frame
		}

		// check and correct startY
		if (startY < 0) {
			negY = Math.abs(startY);
			startY = 0;
		} else if (startY >= texture.getHeight()) {
			return; // pattern out of frame
		}

		int endX = centerX + activeDestructionPattern.getWidth()
				- Math.round(activeDestructionPattern.getCenter().x);
		int endY = centerY + activeDestructionPattern.getHeight()
				- Math.round(activeDestructionPattern.getCenter().y);

		// check and correct endX
		if (endX < 0) {
			return; // pattern out of frame
		} else if (endX >= texture.getWidth()) {
			endX = texture.getWidth() - 1;
		}

		// check and correct endY
		if (endY < 0) {
			return; // pattern out of frame
		} else if (endY >= texture.getHeight()) {
			endY = texture.getHeight();
		}

		// long start = System.currentTimeMillis();
		// run the destruction parallelized
		DestructionRunner.runDestruction(startY, endY, startX, endX, negX,
				negY, result, activeDestructionPattern);
		// System.out.println("Destruction runner took: "
		// + (System.currentTimeMillis() - start));

		updateResult(startX, endX, startY, endY);
	}

	/**
	 * updates the texture with the pixels in the pixmap from startY to endY
	 * (exclusive) and startX to endX (exclusive)
	 * 
	 * @param startX
	 *            the starting x index
	 * @param endX
	 *            the ending x index - 1
	 * @param startY
	 *            the starting y index
	 * @param endY
	 *            the ending y index - 1
	 */
	private void updateResult(int startX, int endX, int startY, int endY) {

		texture.bind();

		if (Gdx.app.getType().equals(ApplicationType.Desktop)) {

			// experiments showed that on desktop it is much faster to write to
			// a transfer buffer and then use glTexSubImage2D with this buffer,
			// which holds only the part of the image which was changed during
			// the destruction. (glTexSubImage2D on whole image -> about 45 ms,
			// glTexSubImage2D with the transfer buffer on a small part on
			// the image-> about 5 ms; filling the transfer buffer about 1 ms)

			// long start = System.currentTimeMillis();
			for (int y = startY; y < endY; y++) {
				for (int x = startX; x < endX; x++) {
					transferBuffer.putInt(result.getPixel(x, y));
				}
			}

			transferBuffer.position(0);
			// System.out.println("Filling transfer buffer took: "
			// + (System.currentTimeMillis() - start));

			// long start2 = System.currentTimeMillis();
			Gdx.gl.glTexSubImage2D(GL20.GL_TEXTURE_2D, 0, startX, startY, endX
					- startX, endY - startY, GL20.GL_RGBA,
					GL20.GL_UNSIGNED_BYTE, transferBuffer);
			// System.out.println("glTexSubImage2d took: "
			// + (System.currentTimeMillis() - start3));

		} else {

			// on android it is much faster to just use glTexSubImage on the
			// whole image (about 8 ms), because the for loops would take up to
			// 50 ms

			// long start3 = System.currentTimeMillis();
			Gdx.gl.glTexSubImage2D(GL20.GL_TEXTURE_2D, 0, 0, 0,
					result.getWidth(), result.getHeight(), GL20.GL_RGBA,
					GL20.GL_UNSIGNED_BYTE, result.getPixels());
			// System.out.println("glTexSubImage2d took: "
			// + (System.currentTimeMillis() - start3));
		}

		// the DestructionRunner still takes lots of time on android
	}

	public Texture getTexture() {
		return texture;
	}

	public void dispose() {
		result.dispose();
	}

	public void reload() {
		texture = new Texture(result);
	}
}
