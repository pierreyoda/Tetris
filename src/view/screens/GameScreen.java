package view.screens;

import control.TetrisController;
import model.Tetrimino;
import model.TetrisBoard;
import model.TetrisBoardCell;
import model.TetrisModel;
import view.TetrisView;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * The GameScreen Screen contains the actual Tetris game.
 *
 * It updates and renders a TetrisModel instance through a given TetrisController.
 *
 * In addition, it also displays a HUD (Head Up Display) offering important information
 * to the player : score, current level, next tetrimino...
 */
public class GameScreen extends Screen {

	/**
	 * Color of the game view's background.
	 */
	private static final Color BACKGROUND_COLOR = Color.BLACK;

	/**
	 * Color of the border around the game board.
	 */
	private static final Color BORDER_COLOR = Color.GREEN;

	/**
	 * The game's controller.
	 */
	private TetrisController gameController;

	/**
	 * If true, debug mode will be activated (for test purposes).
	 */
	private boolean debugMode = false;

	public GameScreen(final TetrisController controller) {
		super(TetrisModel.GAME_UPDATE_INTERVAL, BACKGROUND_COLOR);

		gameController = controller;
	}

	@Override
	public void init(final ScreenContainer container) {
		super.init(container);

		gameController.startGame();
	}

	@Override
	public boolean update() {
		if (gameController.updateGame()) { // game over ?
			final Screen nextScreen = new GameOverScreen(gameController);
			container().pushScreen(nextScreen); // request a game over recap Screen
			return true; // terminate the GameScreen
		}

		return false;
	}

	@Override
	public void render(final Graphics2D g2d, final Font textFont) {
		renderGame(g2d);
		renderHud(g2d, textFont);
	}

	/**
	 * Render the game's current state.
	 * @param g2d Does the actual drawing of primitives.
	 */
	private void renderGame(Graphics2D g2d) {
		final int size = TetrisModel.PIECE_SIZE;

		// render the game border
		g2d.setColor(BORDER_COLOR);
		for (int x = 0; x < TetrisBoard.WIDTH + 2; x++) {
			g2d.fillRect(x * size, 0, size, size);                               // top
			g2d.fillRect(x * size, (TetrisBoard.HEIGHT + 1) * size, size, size); // bottom
		}
		for (int y = 1; y < TetrisBoard.HEIGHT + 1; y++) {
			g2d.fillRect(0, y * size, size, size);                               // left
			g2d.fillRect((TetrisBoard.WIDTH + 1) * size, y * size, size, size);  // right
		}

		// move the origin to the actual gameplay area
		g2d.translate(size, size);

		// render the currently controlled tetrimino
		final Tetrimino t = gameController.getControlledTetrimino();
		g2d.setColor(TetrisView.colorToSwing(t.getColor()));
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (!t.getBlock(i, j)) continue;
				g2d.fillRect((t.getX() + i) * size, (t.getY() + j) * size, size, size);
			}
		}

		// render the rest of the blocks
		final TetrisBoardCell[][] cells = gameController.getBoardCells();
		for (int y = 0; y < TetrisBoard.HEIGHT; y++) {
			for (int x = 0; x < TetrisBoard.WIDTH; x++) {
				final TetrisBoardCell cell = cells[y][x];
				if (!cell.present) continue;

				if (debugMode) {
					g2d.setColor(Color.RED);
				} else {
					g2d.setColor(TetrisView.colorToSwing(cell.color));
				}
				g2d.fillRect(x * size, y * size, size, size);
			}
		}
	}

	/**
	 * Render the game's Head-Up Display.
	 *
	 * @param g Does the actual drawing of primitives.
	 * @param font Font to be used for drawing text.
	 */
	private void renderHud(final Graphics g, final Font font) {
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("Score : " + gameController.getScore(), 0, 0);
	}

	@Override
	public void keyPressed(final KeyEvent e) {
		final int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_ESCAPE:
			container().pushScreen(new GamePauseScreen());
			break;
		case KeyEvent.VK_F1:
			debugMode = !debugMode;
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_KP_UP:
			gameController.keyUp();
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_KP_DOWN:
			gameController.keyDown();
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_KP_LEFT:
			gameController.keyLeft();
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_KP_RIGHT:
			gameController.keyRight();
			break;
		}
	}

}
