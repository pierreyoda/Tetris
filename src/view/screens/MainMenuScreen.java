package view.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import control.TetrisController;

/**
 * <pre>
 * The main menu of the game, i.e. the first Screen that is presented to the
 * player.
 *
 * Several buttons are displayed, each with its own purpose :
 * - Start Game : start a new Tetris game
 * - High Scores : view the current high scores
 * - Exit : exit the application
 * </pre>
 */
public class MainMenuScreen extends Screen {
	private static final int UPDATE_INTERVAL = 50; // we want to be responsive to a skip command

	private static final Color BACKGROUND_COLOR = Color.BLACK;
	private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
	private static final Color BUTTON_SELECTED_TEXT_COLOR = Color.YELLOW;

	private static final String[] BUTTONS_TEXT = {
		"Start Game",
		"High Scores",
		"Exit",
	};
	private static final int BUTTONS_COUNT = BUTTONS_TEXT.length;
	private int selectionIndex = 0; // the index of the currently selected button

	private boolean exit = false;

	private TetrisController gameController;

	public MainMenuScreen(final TetrisController controller) {
		super(UPDATE_INTERVAL, BACKGROUND_COLOR);

		this.gameController = controller;
	}

	@Override
	public boolean update() {
		return exit;
	}

	@Override
	public void render(Graphics g, Font textFont) {
		g.setFont(textFont);
		for (int i = 0; i < BUTTONS_COUNT; i++) {
			drawButton(g, textFont, i);
		}
	}

	private void drawButton(final Graphics g, final Font font, final int index) {
		final int w = container().containerWidth(), h = container().containerHeight();
		final String text = BUTTONS_TEXT[index];

		final FontRenderContext context = new FontRenderContext(null, true, true);
		final Rectangle2D rect = font.getStringBounds(text, context);
	    final int posX = x - (int)(rect.getWidth() / 2), posY = y + (int)(rect.getHeight());

		g.setColor(index == selectionIndex ? BUTTON_SELECTED_TEXT_COLOR : BUTTON_TEXT_COLOR);
		g.drawString(text, posX, posY);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		final int keyCode = e.getKeyCode();
		switch (keyCode) {
		// fast exit
		case KeyEvent.VK_ESCAPE:
			exit = true;
			break;
		// button action
		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_SPACE:
			activateCurrentButton();
		// previous button
		case KeyEvent.VK_UP:
		case KeyEvent.VK_KP_UP:
			selectionIndex = Math.floorMod(selectionIndex - 1, BUTTONS_COUNT);
			break;
		// next button
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_KP_DOWN:
			selectionIndex = (selectionIndex + 1) % BUTTONS_COUNT;
			break;
		}
	}

	private void activateCurrentButton() {
		switch (selectionIndex) {
		case 0: // start game
			container().pushScreen(new GameScreen(gameController));
			break;
		case 1: // high scores
			container().pushScreen(new GameOverScreen(gameController.getHighscores(), true));
			break;
		case 2: // exit
			exit = true;
			break;
		default:
			throw new IllegalStateException("MainMenuScreen : illegal button index");
		}
	}

}
