package model;

import java.util.ArrayList;
import java.util.Random;

import model.TetrisScoreManager.TetrisHighScore;

public class TetrisModel {
	/**
	 * The size of a single tetrimino block, in pixels.
	 */
	public static int PIECE_SIZE = 35;

	/**
	 * Amount of time between each game state update, in milliseconds.
	 *
	 * Must be strictly positive.
	 */
	public static int GAME_UPDATE_INTERVAL = 750;

	private Random random = new Random();

	private int score = 0;
	private TetrisScoreManager scoreManager = new TetrisScoreManager();
	private boolean gameOver = false;

	private Tetrimino currentTetrimino;
	private final TetrisBoard board = new TetrisBoard();

	public TetrisModel() {
		// score manager initialization
		if (!scoreManager.load()) {
			System.out.println("Score manager : failed to load the highscores.");
			System.exit(1);
		}
		System.out.println("Score manager : loaded highscores.");
	}

	/**
	 * Initialize the game and start its execution.
	 */
	public void startGame() {
		// sanity check
		if (GAME_UPDATE_INTERVAL <= 0)
			throw new IllegalStateException("TetrisModel.initGame : GAME_UPDATE_INTERVAL must be > 0.");

		// clean up (for game restarts)
		score = 0;
		gameOver = false;
		board.clear();
		currentTetrimino = null;

		// spawn a new tetrimino for the player to control
		generateNewTetrimino();

		// quickstart for test games
		if (true) {
			for (int y = TetrisBoard.HEIGHT / 2; y < TetrisBoard.HEIGHT; y++) {
				for (int x = 0; x + 1 < TetrisBoard.WIDTH && x < y / 3; x++) {
					board.getCells()[y][x].present = true;
					board.getCells()[y][x].color = TetriminoColor.getColorFromType(TetriminoType.STICK);
				}
			}
		}
	}

	private void gameOver() {
		System.out.format("Game Over ! Score = %d\n", score);
		scoreManager.submit("PLAYER", score); // TODO : player name input...
		scoreManager.save();
		gameOver = true;
	}

	/**
	 * Update the game's state by one tick.
	 *
	 * @eturn True if game over, false otherwise.
	 */
	public boolean updateGame() {
		if (gameOver) return true;

		// current tetrimino fall
		moveCurrentTetrimino(0, +1, true);
		if (gameOver) return true; // game over ?

		// lines clearing & scoring
		final int linesCleared = board.checkForCompleteLines();
		if (linesCleared > 0) {
			score += computeScore(linesCleared);
			System.out.format("=> [%d] lines cleared !\n", linesCleared);
		}

		// debug info
		//System.out.println(board.toString());
		//System.out.format("score = %d\n", score);
		//System.out.println("\n\n\n");

		return false;
	}

	/**
	 * Try to move the current tetrimino by the given offset vector.
	 *
	 * @param deltaX Offset along the X axis (horizontal).
	 * @param deltaY Offset along the Y axis (vertical).
	 * @param performMove True if the command is a real one, and not just a collision test.
	 * If false, no movement will be performed.
	 *
	 * @return True if the movement is allowed (no collisions), false otherwise.
	 */
	private boolean moveCurrentTetrimino(final int deltaX, final int deltaY, boolean performMove) {
		boolean collision = false;
		final Tetrimino t = currentTetrimino;
		if (t == null) return false;

		final int posX = t.getX() + deltaX, posY = t.getY() + deltaY;

		// check movement & collision for each individual block of the current tetrimino
		for (int i = 0; i < 4 && !collision; i++) {
			for (int j = 0; j < 4; j++) {
				if (!t.getBlock(i, j)) continue;
				final int x = posX + i, y = posY + j;

				// borders movement check
				if (x < 0 || x >= TetrisBoard.WIDTH || y < 0) return false; // movement forbidden

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
					return false;
				}
			}
		}

		// has a collision with another block or the bottom corner happened ?
		if (collision) {
			board.addTetrimino(t);
			generateNewTetrimino();
			System.out.format("collision (x = %d, y = %d) !\n", t.getX(), t.getY());

			// check for gameover
			if (t.getY() < 0) {
				System.out.format("gameover (x = %d, y = %d, arg1) !\n", t.getX(), t.getY());
				gameOver();
			}

			return false;
		}

		// at this point we can safely move the tetrimino
		if (performMove) {
			t.move(deltaX, deltaY);
		}

		return true;
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
		final TetriminoColor color = TetriminoColor.getColorFromType(type);
		currentTetrimino = new Tetrimino(color, type, 0, 0,
			Tetrimino.getBlocksFromType(type));

		// try to find a free spot to spawn the tetrimino
		boolean spawned = false;
		int spawnPosition = 0;
		if (moveCurrentTetrimino(TetrisBoard.WIDTH / 2, 0, false)) { // spawn in the middle by default
			spawned = true;
			spawnPosition = TetrisBoard.WIDTH / 2;
		}
		if (!spawned) {
			for (int x = 0; x < TetrisBoard.WIDTH; x++) {
				if (moveCurrentTetrimino(x, 0, false)) {
					spawned = true;
					spawnPosition = x;
					break;
				}
			}
		}

		// did we succeed ?
		if (!spawned) {
			gameOver();
			return;
		}
		currentTetrimino.move(spawnPosition, 0);
		System.out.format("New tetrimino spawned (type = \"%s\", positionX = %d).\n",
				          type, spawnPosition);
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
		return 1000 * (linesCleared - 3);
	}

	/**
	 * Get the player's current score.
	 * @return Current score.
	 */
	public int getScore() { return score; }

	/**
	 * Get the current highscores (loaded from and saved to the file system).
	 */
	public ArrayList<TetrisHighScore> getHighscores() { return scoreManager.getHighscores(); }

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
		moveCurrentTetrimino(0, +1, true);
	}

	/**
	 * Move the player's tetrimino to the left if possible.
	 */
	public void left() {
		moveCurrentTetrimino(-1, 0, true);
	}

	/**
	 * Move the player's tetrimino to the right if possible.
	 */
	public void right() {
		moveCurrentTetrimino(+1, 0, true);
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
