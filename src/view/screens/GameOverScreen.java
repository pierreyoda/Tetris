package view.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import model.TetrisScoreManager.TetrisHighScore;

/**
 * The game over screen displayed at the end of a Tetris game shows a recap of the
 * game session and displays the high scores.
 *
 * Alternatively, this Screen can also only display the high scores.
 *
 */
public class GameOverScreen extends Screen {
	private static final int UPDATE_INTERVAL = 50; // we want to be responsive to a skip command
	private static final Color BACKGROUND_COLOR = Color.BLACK;

	/**
	 * If true, exit the current screen.
	 */
	private boolean skip = false;

	/**
	 * If true, display only the high scores (no game over recap).
	 */
	private boolean onlyScores;

	/**
	 * Array of all the highscores. The first highscore must be the highest one.
	 */
	private final ArrayList<TetrisHighScore> highscores;

	public GameOverScreen(final ArrayList<TetrisHighScore> highscores, final boolean onlyScores) {
		super(UPDATE_INTERVAL, BACKGROUND_COLOR);

		this.highscores = highscores;
		this.onlyScores = onlyScores;
	}

	@Override
	public void init(final ScreenContainer container) {
		super.init(container);

		System.out.format("GameOverScreen.init : top highscore = %d\n", highscores.get(0).score);
	}

	@Override
	public boolean update() {
		return skip;
	}

	@Override
	public void render(final Graphics g, final Font textFont) {
		// TODO
	}


	@Override
	public void keyPressed(final KeyEvent e) {
		final int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE) {
			skip = true;
		}
	}

}
