package view.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class GamePauseScreen extends Screen {
	private static final int UPDATE_INTERVAL = 50;

	/**
	 * If true, exit the current screen and resume the game.
	 */
	private boolean resume = false;

	/**
	 * True if the menu was already drawn once.
	 */
	private boolean firstRender = true;

	public GamePauseScreen() {
		super(UPDATE_INTERVAL, Color.BLACK);

		clearScreen = false; // transparent menu over the paused game
	}

	@Override
	public void onResume() {
		firstRender = true;
	}

	@Override
	public boolean update() {
		return resume;
	}

	@Override
	public void render(final Graphics g, final Font textFont) {
		if (!firstRender) return;

		// TODO

		firstRender = false;
	}


	@Override
	public void keyPressed(final KeyEvent e) {
		final int keyCode = e.getKeyCode();
		switch (keyCode) {
		// exit
		case KeyEvent.VK_ESCAPE:
			container().requestExit();
			break;
		// resume
		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_SPACE:
			resume = true;
			break;
		}
	}
}
