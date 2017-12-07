package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
	private final ArrayList<Tetrimino> tetriminos = new ArrayList<Tetrimino>();
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
		currentTetrimino.move(0,  +1, BOARD_WIDTH, BOARD_HEIGHT);
		
		// board update
		board.update(tetriminos);
		System.out.println(board.toString() + "\n\n\n");
	}

	private void generateNewTetrimino() {
		final TetriminoColor color = TetriminoColor.getRandomThemeColor();
		final TetriminoType type = TetriminoType.values()[random.nextInt(TetriminoType.values().length)];
		System.out.println(type);
		
		final Tetrimino tetrimino = new Tetrimino(color, type,
				random.nextInt(BOARD_WIDTH),
				0, Tetrimino.getBlocksFromType(type));
		tetriminos.add(tetrimino);
		currentTetrimino = tetrimino;
	}

	public int getScore() { return score; }

	public void rotate() {
		currentTetrimino.rotate(true);
	}
	
	public void speedUpFall() {
		currentTetrimino.move(0, +1, BOARD_WIDTH, BOARD_HEIGHT);
	}
	
	public void left() {
		currentTetrimino.move(-1, 0, BOARD_WIDTH, BOARD_HEIGHT);
	}
	
	public void right() {
		currentTetrimino.move(+1, 0, BOARD_WIDTH, BOARD_HEIGHT);
	}

	public TetrisBoardCell[][] getBoardCells() { return board.getCells(); }

}
