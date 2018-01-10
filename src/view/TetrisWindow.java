package view;

import javax.swing.*;

/**
 * The game's window.
 */
public class TetrisWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	private final static String WINDOW_TITLE = "Tetris - Projet Logiciel";

	public TetrisWindow(final TetrisView view) {
		setTitle(WINDOW_TITLE);

		// force application stop when closing the window
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		add(view);
		pack();
	}

	/**
	 * Display the game's window.
	 */
	public void displayGame() {
		setVisible(true);
	}

}
