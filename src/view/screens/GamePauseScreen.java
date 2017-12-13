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

	public GamePauseScreen() {
		super(UPDATE_INTERVAL, Color.BLACK);
	}

	@Override
	public boolean update() {
		return resume;
	}

	@Override
	public void render(final Graphics g, final Font textFont) {
		// TODO
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
