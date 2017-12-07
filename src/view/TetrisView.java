package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import control.TetrisController;
import model.Tetrimino;
import model.TetrisBoardCell;
import model.TetrisModel;

public class TetrisView extends JPanel implements KeyListener {
	private static final long serialVersionUID = 1L;

	private static final Color BACKGROUND_COLOR = Color.BLACK;
	private static final Font TEXT_FONT = new Font(Font.SERIF, Font.BOLD, 16);
	
	private TetrisModel model;
	private TetrisController controller;

	public TetrisView(final TetrisModel model, final TetrisController controller) {
		this.model = model;
		this.controller = controller;
		
		setSize(TetrisModel.BOARD_WIDTH * TetrisModel.PIECE_SIZE, TetrisModel.BOARD_HEIGHT * TetrisModel.PIECE_SIZE);
		setPreferredSize(new Dimension(TetrisModel.BOARD_WIDTH * TetrisModel.PIECE_SIZE, TetrisModel.BOARD_HEIGHT * TetrisModel.PIECE_SIZE));

		setFocusable(true);
		requestFocusInWindow();

		addKeyListener(this);
	}
	
	public void paint(Graphics g) {
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, TetrisModel.BOARD_WIDTH * TetrisModel.PIECE_SIZE,
				   TetrisModel.BOARD_HEIGHT * TetrisModel.PIECE_SIZE);
		
		renderGame(g);
		renderHud(g);
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
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_KP_LEFT:
			controller.keyLeft();
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_KP_RIGHT:
			controller.keyRight();
			break;
		}
	}
	
	@Override
	public void keyReleased(final KeyEvent e) { }
	
	@Override
	public void keyTyped(final KeyEvent e) { }
	
	private void renderGame(Graphics g) {
		final int size = TetrisModel.PIECE_SIZE;
		
		// render the current tetrimino
		final Tetrimino t = model.getCurrentTetrimino();
		g.setColor(t.getColor().toSwing());
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (!t.getBlock(i, j)) continue;
				g.fillRect((t.getX() + i) * size, (t.getY() + j) * size, size, size);
			}
		}

		// render the rest of the blocks
		final TetrisBoardCell[][] cells = model.getBoardCells();
		for (int y = 0; y < TetrisModel.BOARD_HEIGHT; y++) {
			for (int x = 0; x < TetrisModel.BOARD_WIDTH; x++) {
				final TetrisBoardCell cell = cells[y][x];
				if (!cell.present) continue;
				
				g.setColor(cell.color.toSwing());
				g.fillRect(x * size, y * size, size, size);
			}
		}
	}
	
	private void renderHud(Graphics g) {
		g.setFont(TEXT_FONT);
		g.setColor(Color.WHITE);
		g.drawString("Score : " + model.getScore(), 0, 0);
	}

}
