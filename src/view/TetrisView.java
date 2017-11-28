package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import control.TetrisController;
import model.Tetrimino;
import model.TetrisModel;

public class TetrisView extends JPanel implements KeyListener {
	
	private static final Color BACKGROUND_COLOR = Color.BLACK;
	private static final Font TEXT_FONT = new Font(Font.SERIF, Font.BOLD, 16);
	
	private TetrisModel model;
	private TetrisController controller;

	public TetrisView(final TetrisModel model, final TetrisController controller) {
		this.model = model;
		this.controller = controller;
		
		setSize(TetrisModel.BOARD_WIDTH * TetrisModel.PIECE_SIZE, TetrisModel.BOARD_HEIGHT * TetrisModel.PIECE_SIZE);
		setFocusable(true);
		addKeyListener(this);
		requestFocusInWindow();
	}
	
	public void paint(Graphics g) {
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, TetrisModel.BOARD_WIDTH * TetrisModel.PIECE_SIZE,
				   TetrisModel.BOARD_HEIGHT * TetrisModel.PIECE_SIZE);
		
		displayTetriminos(g);
		displayHud(g);
	}
	
	@Override
	public void keyPressed(final KeyEvent e) {
		final int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_UP:
		case KeyEvent.VK_KP_UP:
			controller.keyUp();
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_KP_DOWN:
			controller.keyDown();
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_KP_LEFT:
			controller.keyLeft();
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_KP_RIGHT:
			controller.keyRight();
		}
	}
	
	@Override
	public void keyReleased(final KeyEvent e) { }
	
	@Override
	public void keyTyped(final KeyEvent e) { }
	
	private void displayTetriminos(Graphics g) {
		final int size = TetrisModel.PIECE_SIZE;

		for (final Tetrimino tetrimino : model.getTetriminos()) {
			final int posX = tetrimino.getX(), posY = tetrimino.getY();
			g.setColor(tetrimino.getColor().toSwing());
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (tetrimino.getBlock(i, j)) {
						g.fillRect((posX + i) * size, (posY + j) * size, size, size);
					}
				}
			}
		}
	}
	
	private void displayHud(Graphics g) {
		g.setFont(TEXT_FONT);
		g.setColor(Color.WHITE);
		g.drawString("Score : " + model.getScore(), 0, 0);
	}

}
