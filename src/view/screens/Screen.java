package view.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 * A Screen instance is a self-contained state of the application that is
 * updated, drawn and passed events to from a TetrisView container.
 */
public abstract class Screen {
	/**
	 * Interval between each 'update' call, in milliseconds.
	 */
	private int updateRate;

	/*
	 *The background color that should be used.
	 */
	private Color backgroundColor;

	protected Screen(final int updateRate, final Color backgroundColor) {
		this.updateRate = updateRate;
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Initialize the Screen's state.
	 */
	public abstract void init();

	/**
	 * Update the Screen's state.
	 *
	 * @return True if the Screen should stop running, false otherwise.
	 */
	public abstract boolean update();

	/**
	 * Render the Screen's state.
	 *
	 * @param g Does the actual drawing of primitives.
	 * @param textFont Font to be used for drawing text.
	 */
	public abstract void render(final Graphics g, final Font textFont);

	/**
	 * Called whenever a key is pressed.
	 */
	public abstract void keyPressed(final KeyEvent e);

	public int updateRate() { return updateRate; }
	public Color backgroundColor() { return backgroundColor; }

}
