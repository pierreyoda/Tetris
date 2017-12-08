package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

import view.TetrisView;

public class TetrisModel implements ActionListener {
	/**
	 * The size of a single tetrimino block, in pixels.
	 */
	public static int PIECE_SIZE = 35;

	/**
	 * Amount of time between each game state update, in milliseconds.
	 *
	 * Must be strictly positive.
	 */
	private static int GAME_UPDATE_INTERVAL = 750;

	private Timer timer;
	private Random random = new Random();

	private int score = 0;
	private TetrisScoreManager scoreManager = new TetrisScoreManager();

	private Tetrimino currentTetrimino;
	private final TetrisBoard board = new TetrisBoard();

	private TetrisView view = null; // needed to refresh the view after each update

	public TetrisModel() {
	}

	/**
	 * Initialize the game and start its execution.
	 */
	public void startGame() {
		if (view == null) throw new IllegalStateException("TetrisModel.initGame : TetrisModel.setView must be called first.");

		// score manager initialization
		if (!scoreManager.load()) {
			System.out.println("Score manager : failed to load the highscores.");
			System.exit(1);
		}
		System.out.println("Score manager : loaded highscores.");
		System.out.println(scoreManager.toString() + "\n\n");
		scoreManager.save();
		System.out.println("Score manager : saved highscores.");

		// spawn a new tetrimino for the player to control
		generateNewTetrimino();

		// set up a timer to call the actionPerformed method at fixed intervals
		timer = new Timer(GAME_UPDATE_INTERVAL, this);
		timer.start();
	}

	/**
	 * Set the TetrisView instance responsible for displaying the game.
	 *
	 * Must be called before any update happens.
	 */
	public void setView(final TetrisView view) {
		this.view = view;
	}

	/**
	 * Called every 'GAME_UPDATE_INTERVAL' milliseconds.
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		updateGame();

		view.repaint();
	}

	/**
	 * Pause the game.
	 */
	public void pause() { timer.stop(); }

	/**
	 * Unpause the game.
	 */
	public void unpause() { timer.restart(); }

	/**
	 * Update the game's state by one tick.
	 */
	public void updateGame() {
		// current tetrimino fall
		moveCurrentTetrimino(0, +1);

		// lines clearing & scoring
		final int linesCleared = board.checkForCompleteLines();
		if (linesCleared > 0) {
			score += computeScore(linesCleared);
			System.out.format("=> [%d] lines cleared !\n", linesCleared);
		}

		// debug info
		System.out.println(board.toString());
		System.out.format("score = %d\n", score);
		System.out.println("\n\n\n");
	}

	/**
	 * Try to move the current tetrimino by the given offset vector.
	 *
	 * @param deltaX Offset along the X axis (horizontal).
	 * @param deltaY Offset along the Y axis (vertical).
	 */
	private void moveCurrentTetrimino(final int deltaX, final int deltaY) {
		boolean collision = false;
		final Tetrimino t = currentTetrimino;

		final int posX = t.getX() + deltaX, posY = t.getY() + deltaY;

		// check movement & collision for each individual block of the current tetrimino
		for (int i = 0; i < 4 && !collision; i++) {
			for (int j = 0; j < 4; j++) {
				if (!t.getBlock(i, j)) continue;
				final int x = posX + i, y = posY + j;

				// borders movement check
				if (x < 0 || x >= TetrisBoard.WIDTH || y < 0) return; // movement forbidden

				// bottom border collision
				if (y >= TetrisBoard.HEIGHT) {
					collision = true;
					break;
				}

				// board collision check
				if (board.getCells()[y][x].present) {
					// collision...
					if (deltaY > 0) {
						collision = true;
						break;
					}
					// ... or movement forbidden ?
					return;
				}
			}
		}

		// has a collision with another block or the bottom corner happened ?
		if (collision) {
			board.addTetrimino(t);
			generateNewTetrimino();
			System.out.format("collision (x = %d, y = %d) !\n", t.getX(), t.getY());
			return;
		}

		// at this point we can safely move the tetrimino
		t.move(deltaX, deltaY);
	}

	/**
	 * Generate a new, random tetrimino and assign it as the currently controlled one.
	 */
	private void generateNewTetrimino() {
		final TetriminoType type = TetriminoType.values()[random.nextInt(TetriminoType.values().length)];
		generateNewTetrimino(type);
	}

	/**
	 * Generate a new, random tetrimino with the given type and assign it as the
	 * currently controlled one.
	 *
	 * @param type Type of the tetrimino.
	 */
	private void generateNewTetrimino(final TetriminoType type) {
		final TetriminoColor color = TetriminoColor.getRandomThemeColor();
		currentTetrimino = new Tetrimino(color, type,
			random.nextInt(TetrisBoard.WIDTH),
			0, Tetrimino.getBlocksFromType(type));

		System.out.println(String.format("New tetrimino type = \"%s\"", type));
	}

	/**
	 * Compute the score gained from clearing lines.
	 * @param linesCleared Number of lines cleared by the player.
	 * @return Score gained.
	 */
	private int computeScore(final int linesCleared) {
		if (linesCleared <= 0) return 0;
		if (linesCleared == 1) return 100;
		if (linesCleared == 2) return 300;
		if (linesCleared == 3) return 700;
		return 1000;
	}

	/**
	 * Get the player's current score.
	 * @return Current score.
	 */
	public int getScore() { return score; }

	/**
	 * Rotate the player's tetrimino.
	 */
	public void rotate() {
		// TODO : add collision check (with border and blocks)
		currentTetrimino.rotate(true);
	}

	/**
	 * Accelerate the fall of the player's tetrimino.
	 */
	public void speedUpFall() {
		moveCurrentTetrimino(0, +1);
	}

	/**
	 * Move the player's tetrimino to the left if possible.
	 */
	public void left() {
		moveCurrentTetrimino(-1, 0);
	}

	/**
	 * Move the player's tetrimino to the right if possible.
	 */
	public void right() {
		moveCurrentTetrimino(+1, 0);
	}

	/**
	 * Get the tetrimino currently controlled by the player.
	 * @return
	 */
	public Tetrimino getControlledTetrimino() { return currentTetrimino; }

	/**
	 * <pre>
	 * Get the two-dimensional array describing the board's cells.
	 * NB : the array's first dimension is the Y axis (vertical), and its second
	 * one is the X axis (horizontal).
	 * The coordinate system works as follows :
	 *
	 * +---------> X
	 * |
	 * |
	 * |
	 * |
	 * v
	 *
	 * Y
	 *
	 * </pre>
	 *
	 * @return Cells of the board.
	 */
	public TetrisBoardCell[][] getBoardCells() { return board.getCells(); }

}
