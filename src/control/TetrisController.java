package control;

import java.util.ArrayList;

import model.Tetrimino;
import model.TetrisBoardCell;
import model.TetrisGameSession;
import model.TetrisModel;
import model.TetrisScoreManager.TetrisHighScore;

public class TetrisController {
	private TetrisModel model;

	public TetrisController(final TetrisModel model) {
		this.model = model;
	}

	/*
	 * Initialize and start the game.
	 */
	public void startGame() {
		model.startGame();
	}

	/**
	 * Update the game's state by one tick.
	 *
	 * @return True if the game is lost, false otherwise.
	 */
	public boolean updateGame() {
		return model.updateGame();
	}

	/**
	 * Called when the up key is pressed. Rotates the tetrimino.
	 */
	public void keyUp() {
		model.rotate();
	}

	/**
	 * Called when the down key is pressed. Accelerates the tetrimino's fall.
	 */
	public void keyDown() {
		model.speedUpFall();
	}

	/**
	 * Called when the left key is pressed. Moves the tetrimino to the left.
	 */
	public void keyLeft() {
		model.left();
	}

	/**
	 * Called when the right key is pressed. Moves the tetrimino to the right.
	 */
	public void keyRight() {
		model.right();
	}

	public int getScore() { return model.getScore(); }
	public ArrayList<TetrisHighScore> getHighscores() { return model.getHighscores(); }
	public TetrisGameSession getLastGameSession() { return model.getLastGameSession(); }
	public Tetrimino getControlledTetrimino() { return model.getControlledTetrimino(); }
	public TetrisBoardCell[][] getBoardCells() { return model.getBoardCells(); }

}
