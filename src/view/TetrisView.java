package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import control.TetrisController;
import model.Tetrimino;
import model.TetriminoColor;
import model.TetrisBoard;
import model.TetrisBoardCell;
import model.TetrisModel;

/**
 * The Tetris game view.
 * Its purpose is to display the state of a 'TetrisModel' instance.
 *
 * In addition, it also displays a HUD (Head Up Display) offering information
 * to the player : score, current level, next tetrimino...
 */
public class TetrisView extends JPanel implements KeyListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Color of the game view's background.
	 */
	private static final Color BACKGROUND_COLOR = Color.BLACK;

	/**
	 * Color of the border around the game board.
	 */
	private static final Color BORDER_COLOR = Color.GREEN;

	/**
	 * Text font used to render the HUD.
	 */
	private static final Font TEXT_FONT = new Font(Font.SERIF, Font.BOLD, 16);

	/**
	 * If true, debug mode will be activated (for test purposes).
	 */
	private boolean debugMode = false;

	private TetrisController controller;

	public TetrisView(final TetrisController controller) {
		this.controller = controller;

		setPreferredSize(new Dimension((TetrisBoard.WIDTH + 2) * TetrisModel.PIECE_SIZE,
							           (TetrisBoard.HEIGHT + 2) * TetrisModel.PIECE_SIZE));

		setFocusable(true);
		requestFocusInWindow();

		// listen for key events
		addKeyListener(this);
	}

	/**
	 * Called by Swing to render the view's graphics.
	 */
	@Override
	public void paint(Graphics g) {
		// background
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, (TetrisBoard.WIDTH + 2) * TetrisModel.PIECE_SIZE,
				   (TetrisBoard.HEIGHT + 2) * TetrisModel.PIECE_SIZE);

		renderGame(g);
		renderHud(g);
	}

	/**
	 * Convert the game's internal color representation to the Swing equivalent.
	 */
	public Color colorToSwing(final TetriminoColor color) {
		return new Color(color.red(), color.green(), color.blue());
	}

	/**
	 * Render the game's current state.
	 * @param g Does the actual drawing of primitives.
	 */
	private void renderGame(Graphics g) {
		final int size = TetrisModel.PIECE_SIZE;

		// render the game border
		g.setColor(BORDER_COLOR);
		for (int x = 0; x < TetrisBoard.WIDTH + 2; x++) {
			g.fillRect(x * size, 0, size, size);                               // top
			g.fillRect(x * size, (TetrisBoard.HEIGHT + 1) * size, size, size); // bottom
		}
		for (int y = 1; y < TetrisBoard.HEIGHT + 1; y++) {
			g.fillRect(0, y * size, size, size);                               // left
			g.fillRect((TetrisBoard.WIDTH + 1) * size, y * size, size, size);  // right
		}

		// move the origin to the actual gameplay area
		g.translate(size, size);

		// render the currently controlled tetrimino
		final Tetrimino t = controller.getControlledTetrimino();
		g.setColor(colorToSwing(t.getColor()));
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (!t.getBlock(i, j)) continue;
				g.fillRect((t.getX() + i) * size, (t.getY() + j) * size, size, size);
			}
		}

		// render the rest of the blocks
		final TetrisBoardCell[][] cells = controller.getBoardCells();
		for (int y = 0; y < TetrisBoard.HEIGHT; y++) {
			for (int x = 0; x < TetrisBoard.WIDTH; x++) {
				final TetrisBoardCell cell = cells[y][x];
				if (!cell.present) continue;

				if (debugMode) {
					g.setColor(Color.RED);
				} else {
					g.setColor(colorToSwing(cell.color));
				}
				g.fillRect(x * size, y * size, size, size);
			}
		}
	}

	/**
	 * Render the game's Head-Up Display.
	 * @param g Does the actual drawing of primitives.
	 */
	private void renderHud(Graphics g) {
		g.setFont(TEXT_FONT);
		g.setColor(Color.WHITE);
		g.drawString("Score : " + controller.getScore(), 0, 0);
	}

	/**
	 * Called whenever a key is pressed.
	 */
	@Override
	public void keyPressed(final KeyEvent e) {
		final int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_F1:
			debugMode = !debugMode;
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_KP_UP:
			controller.keyUp();
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_KP_DOWN:
			controller.keyDown();
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_KP_LEFT:
			controller.keyLeft();
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_KP_RIGHT:
			controller.keyRight();
			break;
		}
	}

	@Override
	public void keyReleased(final KeyEvent e) { }

	@Override
	public void keyTyped(final KeyEvent e) { }

}
