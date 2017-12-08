package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

import view.TetrisView;

public class TetrisModel implements ActionListener {
	public static int PIECE_SIZE = 35;
	private static int GAME_TICK = 750; // update interval, in ms

	private Timer timer;
	private Random random = new Random();

	private int score = 0;
	private TetrisScoreManager scoreManager = new TetrisScoreManager();

	private Tetrimino currentTetrimino;
	private final TetrisBoard board = new TetrisBoard();

	private TetrisView view = null; // needed to refresh the view after each update

	public TetrisModel() {
	}

	public void initGame() {
		if (view == null) throw new IllegalStateException("TetrisModel.initGame : TetrisModel.setView must be called first.");

		// score manager initialization
		if (!scoreManager.load()) {
			System.out.println("Score manager : failed to load the highscores.");
			System.exit(1);
		}
		System.out.println("Score manager : loaded highscores.");
		/*scoreManager.submit("B", 1500);
		scoreManager.submit("A", 1700);
		scoreManager.submit("C", 300);
		for (int i = 0; i < 10; i++) {
			scoreManager.submit("CCC", random.nextInt(1800));
		}*/
		System.out.println(scoreManager.toString() + "\n\n");
		scoreManager.save();
		System.out.println("Score manager : saved highscores.");

		// spawn a new tetrimino for the player to control
		generateNewTetrimino();

		// set up a timer to call the actionPerformed method at fixed intervals
		timer = new Timer(GAME_TICK, this);
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

	@Override
	public void actionPerformed(ActionEvent event) {
		update();

		view.repaint();
	}

	public void pause() { timer.stop(); }
	public void resume() { timer.restart(); }

	public void update() {
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

	private void generateNewTetrimino() {
		final TetriminoType type = TetriminoType.values()[random.nextInt(TetriminoType.values().length)];
		generateNewTetrimino(type);
	}

	private void generateNewTetrimino(final TetriminoType type) {
		final TetriminoColor color = TetriminoColor.getRandomThemeColor();
		currentTetrimino = new Tetrimino(color, type,
			random.nextInt(TetrisBoard.WIDTH),
			0, Tetrimino.getBlocksFromType(type));

		System.out.println(String.format("New tetrimino type = \"%s\"", type));
	}

	private int computeScore(final int linesCleared) {
		if (linesCleared <= 0) return 0;
		if (linesCleared == 1) return 100;
		if (linesCleared == 2) return 300;
		if (linesCleared == 3) return 700;
		return 1000;
	}

	public int getScore() { return score; }

	public void rotate() {
		// TODO : add collision check (with border and blocks)
		currentTetrimino.rotate(true);
	}

	public void speedUpFall() {
		moveCurrentTetrimino(0, +1);
	}

	public void left() {
		moveCurrentTetrimino(-1, 0);
	}

	public void right() {
		moveCurrentTetrimino(+1, 0);
	}

	public Tetrimino getCurrentTetrimino() { return currentTetrimino; }
	public TetrisBoardCell[][] getBoardCells() { return board.getCells(); }

}
