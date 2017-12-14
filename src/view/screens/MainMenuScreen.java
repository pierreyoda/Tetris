package view.screens;

import control.TetrisController;
import view.RenderingUtilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

	private static final Font BANNER_FONT = new Font(Font.SERIF, Font.BOLD, 100);
	private static final Color BANNER_COLOR = Color.BLACK;

	public static final Color BACKGROUND_COLOR = new Color(60, 70, 80);
	private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
	private static final Color BUTTON_SELECTED_TEXT_COLOR = Color.YELLOW;

	private BufferedImage selectionCursorImage;

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
	public void init(final ScreenContainer container) {
		super.init(container);

		try {
			selectionCursorImage = ImageIO.read(new File("data/selection.png"));
		} catch (IOException e) {
			throw new IllegalStateException("Cannot read cursor selection image from data/ folder.");
		}
	}

	@Override
	public boolean update() {
		return exit;
	}

	@Override
	public void render(Graphics2D g2d, Font textFont) {
		final int w = container().containerWidth(), h = container().containerHeight();

		// banner title
		g2d.setFont(BANNER_FONT);
		g2d.setColor(BANNER_COLOR);
		RenderingUtilities.drawCenteredText(g2d, BANNER_FONT, w / 2, h / 20,
			"TETRIS");

		// menu buttons
		g2d.setFont(textFont);
		for (int i = 0; i < BUTTONS_COUNT; i++) {
			final String text = BUTTONS_TEXT[i];
			final int x = w / 2, y = (int)(h / 2.5) + i * h / (BUTTONS_COUNT * 3);
			if (i == selectionIndex) {
				g2d.setColor(BUTTON_SELECTED_TEXT_COLOR);
				g2d.drawImage(selectionCursorImage,
							(int)(w / 3), y + selectionCursorImage.getHeight(),
							null);
			} else {
				g2d.setColor(BUTTON_TEXT_COLOR);
			}
			RenderingUtilities.drawCenteredText(g2d, textFont, x, y, text);
		}
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
			container().pushScreen(new HighScoresScreen(gameController.getHighscores(), true));
			break;
		case 2: // exit
			exit = true;
			break;
		default:
			throw new IllegalStateException("MainMenuScreen : illegal button index");
		}
	}

}
