package model;

import java.util.Random;

/**
 * Defines the color of a tetrimino.
 */
public class TetriminoColor {

	private static Random random = new Random();

	private int red;
	private int green;
	private int blue;

	public TetriminoColor(final int red, final int green, final int blue) {
		set(red, green, blue);
	}

	public TetriminoColor(final TetriminoColor other) {
		this.red = other.red;
		this.green = other.green;
		this.blue = other.blue;
	}

	/**
	 * Get a completely random color.
	 */
	public static TetriminoColor getRandomColor() {
		return new TetriminoColor(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}

	/**
	 * Get the color corresponding to a tetrimino type.
	 */
	public static TetriminoColor getColorFromType(final TetriminoType type) {
		final TetriminoColor[] palette = {
			new TetriminoColor(20, 255, 255), // stick
			new TetriminoColor(240, 240, 20), // box
			new TetriminoColor(130, 30, 130), // stairs
			new TetriminoColor(40, 255, 40), // right snake
			new TetriminoColor(0, 67, 0), // left snake
			new TetriminoColor(40, 50, 255), // left L
			new TetriminoColor(255, 160, 20), // right L
		};
		return palette[type.ordinal()];
	}

	public int red() { return red; }
	public int green() { return green; }
	public int blue() { return blue; }

	public final void setRed(final int red) {
		if (!isColorScalarValid(red))
			throw new IllegalArgumentException("A color value must be between 0 and 255.");
		this.red = red;
	}

	public final void setGreen(final int green) {
		if (!isColorScalarValid(green))
			throw new IllegalArgumentException("A color value must be between 0 and 255.");
		this.green = green;
	}

	public final void setBlue(final int blue) {
		if (!isColorScalarValid(blue))
			throw new IllegalArgumentException("A color value must be between 0 and 255.");
		this.blue = blue;
	}

	public final void set(final int red, final int green, final int blue) {
		if (!isColorScalarValid(red) || !isColorScalarValid(green) || !isColorScalarValid(blue))
			throw new IllegalArgumentException("A color value must be between 0 and 255.");
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	@Override
	public String toString() {
		return String.format("RGB (%d, %d, %d)", red, green, blue);
	}

	private static boolean isColorScalarValid(final int value) {
		return 0 <= value && value <= 255;
	}
}
