package model;

import java.awt.Color;
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
	
	public static TetriminoColor getRandomColor() {
		return new TetriminoColor(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}
	
	public static TetriminoColor getRandomThemeColor() {
		final TetriminoColor[] themeColors = {
			new TetriminoColor(0, 255, 255),
			new TetriminoColor(0, 0, 255),
			new TetriminoColor(0, 170, 170),
			new TetriminoColor(100, 65, 0),
			new TetriminoColor(0, 67, 0),
			new TetriminoColor(67, 33, 0),
			new TetriminoColor(67, 67, 0),
		};
		return themeColors[random.nextInt(themeColors.length)];
	}
	
	public Color toSwing() { return new Color(red, green, blue); }
	
	public int getRed() { return red; }
	public int getGreen() { return green; }
	public int getBlue() { return blue; }
	
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
