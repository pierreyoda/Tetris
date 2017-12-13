package view.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
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
	private static final Color TEXT_COLOR = Color.WHITE;

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
		g.setColor(TEXT_COLOR);
		g.setFont(textFont);

		final int w = container().containerWidth(), h = container().containerHeight();

		drawCenteredText(g, textFont, w / 2, h / 4, "----- HIGH SCORES -----");
		for (int i = 0; i < highscores.size(); i++) {
			final TetrisHighScore highscore = highscores.get(i);
			if (highscore.score <= 0) break;

			final int y = h / 3 + i * h / (highscores.size() * 4);
			drawCenteredText(g, textFont,     w / 3, y, highscore.name);
			drawCenteredText(g, textFont, 2 * w / 3, y, String.valueOf(highscore.score));
		}
	}

	private void drawCenteredText(final Graphics g, final Font font,
								  final int x, final int y,
								  final String text) {
		final FontRenderContext context = new FontRenderContext(null, true, true);
		final Rectangle2D rect = font.getStringBounds(text, context);
		final int posX = x - (int)(rect.getWidth() / 2), posY = y + (int)(rect.getHeight());
		g.drawString(text, posX, posY);
	}

	@Override
	public void keyPressed(final KeyEvent e) {
		final int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE) {
			skip = true;
		}
	}

}
