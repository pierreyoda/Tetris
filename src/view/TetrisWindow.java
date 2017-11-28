package view;

import javax.swing.JFrame;

import control.TetrisController;

public class TetrisWindow extends JFrame {
	private final static String WINDOW_TITLE = "Tetris - Projet Logiciel";
	private TetrisController controller;
	private TetrisView view;
	
	public TetrisWindow(final TetrisController controller, final TetrisView view) {
		this.controller = controller;
		this.view = view;

		setTitle(WINDOW_TITLE);
		
		add(view);
		pack();
	}
}
