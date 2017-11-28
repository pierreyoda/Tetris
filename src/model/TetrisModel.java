package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

public class TetrisModel implements ActionListener {
	public static int BOARD_WIDTH = 10;
	public static int BOARD_HEIGHT = 22;
	public static int PIECE_SIZE = 35;
	private static float GAME_SPEED = 1.f;

	private float gameTimer = 0.f;
	private Timer timer;
	private Random random = new Random();

	private int score = 0;
	private Tetrimino currentTetrimino;
	private ArrayList<Tetrimino> tetriminos = new ArrayList();

	public TetrisModel() {
		generateNewTetrimino();

		timer = new Timer((int) (GAME_SPEED * 1000.f), this);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		update();
	}

	public void pause() { timer.stop(); }
	public void resume() { timer.restart(); }

	public void update() {
		for (Tetrimino tetrimino : tetriminos) {
			tetrimino.move(0, +1, BOARD_WIDTH, BOARD_HEIGHT);
			tetrimino.rotate(true);
		}
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

	public ArrayList<Tetrimino> getTetriminos() { return tetriminos; }

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

}
