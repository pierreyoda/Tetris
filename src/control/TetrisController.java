package control;

import model.TetrisModel;

public class TetrisController {
	private TetrisModel model;
		
	public TetrisController(final TetrisModel model) {
		this.model = model;
	}

	public void keyUp() {
		System.out.println("up");
	}

	public void keyDown() {
	}

	public void keyLeft() {
	}

	public void keyRight() {
	}

}
