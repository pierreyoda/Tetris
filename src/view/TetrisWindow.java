package view;

import java.awt.Dimension;

import javax.swing.JFrame;

import control.TetrisController;
import model.TetrisModel;

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
