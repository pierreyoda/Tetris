package view.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import model.TetrisScoreManager.TetrisHighScore;
import view.RenderingUtilities;

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
	private static final Color TEXT_COLOR = Color.WHITE;
	private static final Color TEXT_COLOR_HIGHLIGHT = Color.YELLOW;
	private static final long SCORE_HIGHLIGHT_BLINK_DELAY = 1_000_000_000; // in ns

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

	/**
	 * Index of the high score that was just set after a game over. -1 if null.
	 */
	private int newHighScoreIndex = -1;

	private long newHighScoreLastBlink = System.nanoTime();
	private boolean newHighScoreHighlighted = true;
	private Color newHighScoreCurrentColor = TEXT_COLOR_HIGHLIGHT;

	public GameOverScreen(final ArrayList<TetrisHighScore> highscores, final boolean onlyScores) {
		super(UPDATE_INTERVAL, BACKGROUND_COLOR);

		this.highscores = highscores;
		this.onlyScores = onlyScores;
	}

	/**
	 * After a game over, this method allows to define the high score that was
	 * just set.
	 * @param index Index of the high score (-1 if no new high score).
	 * @return The GameOverScreen instance.
	 */
	public GameOverScreen setNewHighScore(final int index) {
		newHighScoreIndex = index;

		return this;
	}

	@Override
	public boolean update() {
		return skip;
	}

	@Override
	public void render(final Graphics g, final Font textFont) {
		g.setColor(TEXT_COLOR);
		g.setFont(textFont);

		final int w = container().containerWidth(), h = container().containerHeight();

		// high scores table
		RenderingUtilities.drawCenteredText(g, textFont, w / 2, h / 4,
			"----- HIGH SCORES -----");
		for (int i = 0; i < highscores.size(); i++) {
			final TetrisHighScore highscore = highscores.get(i);
			if (highscore.score <= 0) break;

			final int y = h / 3 + i * h / (highscores.size() * 4);

			g.setColor(TEXT_COLOR);

			// high scores after game over : highlight the new high score with blink
			if (!onlyScores && i == newHighScoreIndex) {
				final long t = System.nanoTime();
				if (t - newHighScoreLastBlink >= SCORE_HIGHLIGHT_BLINK_DELAY) {
					newHighScoreLastBlink = t;
					newHighScoreCurrentColor = newHighScoreHighlighted ? TEXT_COLOR : TEXT_COLOR_HIGHLIGHT;
					newHighScoreHighlighted = !newHighScoreHighlighted;
				}
				g.setColor(newHighScoreCurrentColor);
			}

			// name
			RenderingUtilities.drawCenteredText(g, textFont,     w / 3, y,
				highscore.name);
			// score
			RenderingUtilities.drawCenteredText(g, textFont, 2 * w / 3, y,
				String.valueOf(highscore.score));
		}

		// instructions
		g.setColor(TEXT_COLOR);
		RenderingUtilities.drawCenteredText(g, textFont, w / 2, (int)(h / 1.2),
			"PRESS SPACE OR ENTER TO CONTINUE");
		RenderingUtilities.drawCenteredText(g, textFont, w / 2, (int)(h/1.1),
			"PRESS ESCAPE TO EXIT");
	}


	@Override
	public void keyPressed(final KeyEvent e) {
		final int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_ESCAPE:
			container().requestExit();
			break;
		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_SPACE:
			skip = true;
			break;
		}
	}

}
