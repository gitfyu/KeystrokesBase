package pw.cinque.keystrokesmod.util;

/**
 * Utility class that allows storing a two dimensional position. The X and Y coordinates are
 * relative to the nearest side of the screen (left or right for X, top or bottom for Y) to ensure
 * the correct position is maintained when the screen is resized.
 * <p>
 * If the screen gets resized, the new dimensions should be set using {@link
 * Position#updateScreenSize}
 * <b>before</b> calling the {@link Position#getX} or {@link Position#getY} methods again.
 */
public class Position {

    private int x;
    private int y;
    private int screenWidth;
    private int screenHeight;
    private boolean rightAligned;
    private boolean bottomAligned;

    /**
     * This method should be called when the screen is resized, to ensure {@link Position#getX} or
     * {@link Position#getY} still return the correct coordinates.
     *
     * @param width  The new screen width
     * @param height The new screen height
     */
    public void updateScreenSize(int width, int height) {
        screenWidth = width;
        screenHeight = height;

        // run alignment checks again
        setX(getX());
        setY(getY());
    }

    /**
     * Translates the position by the given offsets.
     *
     * @param dx The offset across the X axis
     * @param dy The offset across the Y axis
     */
    public void translate(int dx, int dy) {
        setX(getX() + dx);
        setY(getY() + dy);
    }

    /**
     * Gets the absolute X coordinate of this position.
     *
     * @return The X coordinate
     */
    public int getX() {
        return rightAligned ? screenWidth - x : x;
    }

    /**
     * Sets the absolute X coordinate of this position
     *
     * @param x The X coordinate
     */
    public void setX(int x) {
        // if the X coordinate is closer to the right side of the screen, store the position
        // relative to the right side instead
        if (x > screenWidth / 2) {
            this.x = screenWidth - x;
            rightAligned = true;
        } else {
            this.x = x;
            rightAligned = false;
        }
    }

    /**
     * Gets the absolute Y coordinate of this position.
     *
     * @return The Y coordinate
     */
    public int getY() {
        return bottomAligned ? screenHeight - y : y;
    }

    /**
     * Sets the absolute X coordinate of this position
     *
     * @param y The Y coordinate
     */
    public void setY(int y) {
        // if the Y coordinate is closer to the bottom of the screen, store the position relative
        // to the bottom instead
        if (y > screenHeight / 2) {
            this.y = screenHeight - y;
            bottomAligned = true;
        } else {
            this.y = y;
            bottomAligned = false;
        }
    }

}
