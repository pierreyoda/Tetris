package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import control.TetrisController;

public class TetrisWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	private final static String WINDOW_TITLE = "Tetris - Projet Logiciel";
	private TetrisController controller;
	private TetrisView view;
	
	public TetrisWindow(final TetrisController controller, final TetrisView view) {
		this.controller = controller;
		this.view = view;

		setTitle(WINDOW_TITLE);
		
		// force application stop when closing the window
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		add(view);
		pack();
	}

	public void startGame() {
		setVisible(true);
	}
}
