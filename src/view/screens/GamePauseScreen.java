package view.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import view.RenderingUtilities;

public class GamePauseScreen extends Screen {
	private static final int UPDATE_INTERVAL = 50;

	private static final Color TEXT_COLOR = Color.WHITE;

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

		g.setColor(TEXT_COLOR);
		g.setFont(textFont);

		final int w = container().containerWidth(), h = container().containerHeight();

		RenderingUtilities.drawCenteredText(g, textFont, w / 2, h / 2,
			"PAUSED");
		RenderingUtilities.drawCenteredText(g, textFont, w / 2, (int)(h / 1.7),
			"SPACE OR ENTER TO RESUME");
		RenderingUtilities.drawCenteredText(g, textFont, w / 2, (int)(h / 1.3),
			"ESCAPE TO QUIT");

		firstRender = false;
	}

	@Override
	public void keyPressed(final KeyEvent e) {
		final int keyCode = e.getKeyCode();
		switch (keyCode) {
		// exit to main menu
		case KeyEvent.VK_ESCAPE:
			container().deletePreviousScreen();
			resume = true;
			break;
		// resume the game
		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_SPACE:
			resume = true;
			break;
		}
	}
}
