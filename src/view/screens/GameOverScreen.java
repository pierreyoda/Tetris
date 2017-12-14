package view.screens;

import model.TetrisGameSession;
import model.TetrisScoreManager.TetrisHighScore;
import view.RenderingUtilities;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * The game over screen displayed at the end of a Tetris game shows a recap of the
 * game session and displays the high scores.
 *
 * Alternatively, this Screen can also only display the high scores.
 *
 */
public class GameOverScreen extends Screen {
	private static final int UPDATE_INTERVAL = 50; // we want to be responsive to a skip command
	private static final Color BACKGROUND_COLOR = MainMenuScreen.BACKGROUND_COLOR;
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
	 * Informations about the game that was just played.
	 */
	private TetrisGameSession gameSession = null;

	private long newHighScoreLastBlink = System.nanoTime();
	private boolean newHighScoreHighlighted = true;
	private Color newHighScoreCurrentColor = TEXT_COLOR_HIGHLIGHT;

	public GameOverScreen(final ArrayList<TetrisHighScore> highscores, final boolean onlyScores) {
		super(UPDATE_INTERVAL, BACKGROUND_COLOR);

		this.highscores = highscores;
		this.onlyScores = onlyScores;
	}

	/**
	 * After a game over, this method allows to define the game session that
	 * was just played.
	 * @param gameSession Tetris game session informations.
	 * @return The GameOverScreen instance.
	 */
	public GameOverScreen setGameSession(final TetrisGameSession gameSession) {
		this.gameSession = gameSession;

		return this;
	}

	@Override
	public boolean update() {
		return skip;
	}

	@Override
	public void render(final Graphics2D g2d, final Font textFont) {
		g2d.setColor(TEXT_COLOR);
		g2d.setFont(textFont);

		final int w = container().containerWidth(), h = container().containerHeight();
		final int newHighScoreIndex = onlyScores ? -1 : gameSession.newHighScoreIndex();

		// game recap
		if (!onlyScores) {
			final int score = gameSession.score();
			final int linesCleared = gameSession.linesCleared();

			RenderingUtilities.drawCenteredText(g2d, textFont, w / 2, (int) (h / 10),
				"GAME OVER !");
			RenderingUtilities.drawCenteredText(g2d, textFont, w / 2, (int) (1.5 * h / 10),
				String.format("Score : %d", score));
			RenderingUtilities.drawCenteredText(g2d, textFont, w / 2, (int) (2 * h / 10),
				String.format("Number of lines cleared : %d", linesCleared));
		}

		// high scores table
		RenderingUtilities.drawCenteredText(g2d, textFont, w / 2, h / 4,
			"----- HIGH SCORES -----");
		if (!onlyScores && newHighScoreIndex >= 0) {
			g2d.setColor(TEXT_COLOR_HIGHLIGHT);
			RenderingUtilities.drawCenteredText(g2d, textFont, w / 2, (int)(h / 3.5),
				"NEW HIGH SCORE !");
		}
		for (int i = 0; i < highscores.size(); i++) {
			final TetrisHighScore highscore = highscores.get(i);
			if (highscore.score <= 0) break;

			final int y = h / 3 + i * h / (highscores.size() * 4);

			g2d.setColor(TEXT_COLOR);

			// high scores after game over : highlight the new high score with blink
			if (!onlyScores && i == newHighScoreIndex) {
				final long t = System.nanoTime();
				if (t - newHighScoreLastBlink >= SCORE_HIGHLIGHT_BLINK_DELAY) {
					newHighScoreLastBlink = t;
					newHighScoreCurrentColor = newHighScoreHighlighted ? TEXT_COLOR : TEXT_COLOR_HIGHLIGHT;
					newHighScoreHighlighted = !newHighScoreHighlighted;
				}
				g2d.setColor(newHighScoreCurrentColor);
			}

			// name
			RenderingUtilities.drawCenteredText(g2d, textFont,     w / 3, y,
				highscore.name);
			// score
			RenderingUtilities.drawCenteredText(g2d, textFont, 2 * w / 3, y,
				String.valueOf(highscore.score));
		}

		// instructions
		g2d.setColor(TEXT_COLOR);
		RenderingUtilities.drawCenteredText(g2d, textFont, w / 2, (int)(h / 1.2),
			"PRESS SPACE OR ENTER TO CONTINUE");
		RenderingUtilities.drawCenteredText(g2d, textFont, w / 2, (int)(h/1.1),
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
