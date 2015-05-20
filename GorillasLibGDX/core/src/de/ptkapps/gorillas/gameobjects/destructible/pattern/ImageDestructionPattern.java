package de.ptkapps.gorillas.gameobjects.destructible.pattern;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

public class ImageDestructionPattern implements IDestructionPattern {

  /**
   * The image, represented as pixmap, which this pattern is based on.
   */
  private Pixmap pattern;

  /**
   * matrix element is true, if the pixel should be erased, otherwise false
   */
  private boolean[][] eraseMatrix;

  /**
   * The x-coordinate of the center of the pattern.
   */
  private int centerX;

  /**
   * The y-coordinate of the center of the pattern.
   */
  private int centerY;

  /**
   * Constructor
   * 
   * @param pattern
   *          The pixmap which defines the pattern by it's alphamap.
   */
  public ImageDestructionPattern(Pixmap pattern) {

    this.pattern = pattern;

    if (this.pattern != null) {

      setupEraseMatrix();

      centerX = this.pattern.getWidth() / 2;
      centerY = this.pattern.getHeight() / 2;
    }
  }

  private void setupEraseMatrix() {

    eraseMatrix = new boolean[pattern.getWidth()][pattern.getHeight()];

    for (int x = 0; x < pattern.getWidth(); x++) {

      for (int y = 0; y < pattern.getHeight(); y++) {

        if ((pattern.getPixel(x, y) & 0x000000FF) == 0) {

          eraseMatrix[x][y] = true;

        } else {

          eraseMatrix[x][y] = false;
        }
      }
    }
  }

  @Override
  public int getWidth() {
    if (pattern != null) {
      return pattern.getWidth();
    }
    return 0;
  }

  @Override
  public int getHeight() {
    if (pattern != null) {
      return pattern.getHeight();
    }
    return 0;
  }

  @Override
  public Vector2 getCenter() {
    return new Vector2(centerX, centerY);
  }

  @Override
  public boolean shouldErase(int x, int y) {
    return eraseMatrix[x][y];
  }
}
