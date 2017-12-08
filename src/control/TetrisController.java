package control;

import model.Tetrimino;
import model.TetrisBoardCell;
import model.TetrisModel;

public class TetrisController {
	private TetrisModel model;

	public TetrisController(final TetrisModel model) {
		this.model = model;
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
	public Tetrimino getControlledTetrimino() { return model.getControlledTetrimino(); }
	public TetrisBoardCell[][] getBoardCells() { return model.getBoardCells(); }

}
