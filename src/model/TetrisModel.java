package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

import view.TetrisView;

public class TetrisModel implements ActionListener {
	public static int BOARD_WIDTH = 10;
	public static int BOARD_HEIGHT = 22;
	public static int PIECE_SIZE = 35;
	private static float GAME_SPEED = 1.f;

	private Timer timer;
	private Random random = new Random();

	private int score = 0;

	private Tetrimino currentTetrimino;
	private final TetrisBoard board = new TetrisBoard();
	
	private TetrisView view = null; // needed to refresh the view after each update

	public TetrisModel() {
		// game initialization
		generateNewTetrimino();

		// set up a timer to call the actionPerformed method at fixed intervals
		timer = new Timer((int) (GAME_SPEED * 1000.f), this);
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
		if (view == null) throw new IllegalStateException("TetrisModel.setView must be called first.");

		update();

		view.repaint();
	}

	public void pause() { timer.stop(); }
	public void resume() { timer.restart(); }

	public void update() {
		// current tetrimino fall
		moveCurrentTetrimino(0, +1);
		
		// check for lines cleared
		// TODO

		// board debug rendering
		System.out.println(board.toString() + "\n\n\n");
	}
	
	private void moveCurrentTetrimino(final int deltaX, final int deltaY) {
		boolean collision = false;
		final Tetrimino t = currentTetrimino;

		final int posX = t.getX() + deltaX, posY = t.getY() + deltaY;
		
		// check collision with the game corners
		for (int i = 0; i < 4 && !collision; i++) {
			for (int j = 0; j < 4; j++) {
				if (!t.getBlock(i, j)) continue;
				
				final int x = posX + i, y = posY + j;
				if (x < 0 || x >= BOARD_WIDTH || y < 0) return; // movement forbidden
				if (y >= BOARD_HEIGHT) {
					collision = true;
					break;
				}
			}
		}
		
		// has a collision with another block or the bottom corner happened ?
		if (collision) {
			board.addTetrimino(t);
			generateNewTetrimino();
			System.out.println(String.format("collision with bottom (x = %d) !", t.getX()));
		}
		
		// at this point we can safely move the tetrimino
		t.move(deltaX, deltaY);
	}

	private void generateNewTetrimino() {
		final TetriminoColor color = TetriminoColor.getRandomThemeColor();
		final TetriminoType type = TetriminoType.values()[random.nextInt(TetriminoType.values().length)];
		System.out.println(String.format("New tetrimino type = \"%s\"", type));
		
		currentTetrimino = new Tetrimino(color, type,
			random.nextInt(BOARD_WIDTH),
			0, Tetrimino.getBlocksFromType(type));
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
