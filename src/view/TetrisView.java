package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import model.TetriminoColor;
import model.TetrisBoard;
import model.TetrisModel;
import view.screens.Screen;

/**
 * The Tetris game view.
 * This is a container for Screen instances.
 */
public class TetrisView extends JPanel implements KeyListener, ActionListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Text font used to render the HUD.
	 */
	private static final Font TEXT_FONT = new Font(Font.SERIF, Font.BOLD, 16);

	/*
	 * The currently active screen.
	 */
	private Screen currentScreen;

	private Timer timer;

	public TetrisView(final Screen screen) {
		// sanity check
		if (screen == null)
			throw new IllegalArgumentException("TetrisView : null Screen, aborting.");

		setPreferredSize(new Dimension((TetrisBoard.WIDTH + 2) * TetrisModel.PIECE_SIZE,
							           (TetrisBoard.HEIGHT + 2) * TetrisModel.PIECE_SIZE));

		setFocusable(true);
		requestFocusInWindow();

		// listen for key events
		addKeyListener(this);

		// screen management
		currentScreen = screen;
		timer = new Timer(currentScreen.updateRate(), this); // call this.actionPerformed at fixed intervals
		screen.init();
		timer.start();
	}

	/**
	 * Called by Swing to render the view's graphics.
	 */
	@Override
	public void paint(Graphics g) {
		// clear the panel
		g.setColor(currentScreen.backgroundColor());
		g.fillRect(0, 0, getWidth(), getHeight());
		// render the current screen
		currentScreen.render(g, TEXT_FONT);
	}

	/**
	 * Convert the game's internal color representation to the Swing equivalent.
	 */
	public static Color colorToSwing(final TetriminoColor color) {
		return new Color(color.red(), color.green(), color.blue());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (currentScreen.update()) {
			System.exit(0);
			return;
		}

		repaint(); // refresh the view
	}

	/**
	 * Called whenever a key is pressed.
	 */
	@Override
	public void keyPressed(final KeyEvent e) {
		currentScreen.keyPressed(e);
	}

	@Override
	public void keyReleased(final KeyEvent e) { }

	@Override
	public void keyTyped(final KeyEvent e) { }

}
