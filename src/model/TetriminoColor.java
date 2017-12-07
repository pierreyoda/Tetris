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
		this.red = red;
		this.green = green;
		this.blue = blue;
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
	
	@Override
	public String toString() {
		return String.format("RGB (%d, %d, %d)", red, green, blue);
	}
	
}
