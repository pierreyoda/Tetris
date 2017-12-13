package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;

import javax.swing.JPanel;
import javax.swing.Timer;

import model.TetriminoColor;
import model.TetrisBoard;
import model.TetrisModel;
import view.screens.Screen;
import view.screens.ScreenContainer;

/**
 * The Tetris game view.
 * This is a container for Screen instances.
 */
public class TetrisView extends JPanel implements KeyListener, ActionListener, ScreenContainer {
	private static final long serialVersionUID = 1L;

	/**
	 * Text font used to render the HUD.
	 */
	private static final Font TEXT_FONT = new Font(Font.SERIF, Font.BOLD, 16);

	private Stack<Screen> screens = new Stack<>();
	private Screen currentScreen;
	private Screen screenToAdd = null;

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
		timer = new Timer(1000, this);
		setNewScreen(screen);
	}

	/**
	 * Add a Screen to the stack, initialize it and set it as the current one.
	 *
	 * The actual operation will be done on the next update tick, to allow the
	 * current Screen to properly terminate if needed.
	 */
	@Override
	public void pushScreen(final Screen screen) {
		if (screen == null)
			throw new IllegalArgumentException("TetrisView.pushScreen : null Screen.");

		screenToAdd = screen;
	}

	/**
	 * Initialize the given Screen and set it as the current one.
	 */
	private void setNewScreen(final Screen screen) {
		timer.stop();

		currentScreen = screen;
		screens.add(currentScreen);

		currentScreen.init(this);

		final int delay = currentScreen.updateRate();
		timer.setInitialDelay(delay);
		timer.setDelay(delay); // call this.actionPerformed at fixed intervals
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (screenToAdd != null) {
			setNewScreen(screenToAdd);
			screenToAdd = null;
		}

		if (currentScreen == null) return;

		// current screen update
		if (currentScreen.update()) { // terminate the current Screen ?
			if (screens.size() == 1) {
				if (screenToAdd != null) { // this.pushScreen may have been called by the current Screen
					screens.pop();
					setNewScreen(screenToAdd);
					screenToAdd = null;
				} else {
					timer.stop();
					System.exit(0);
				}

				return;
			}

			screens.pop();
			currentScreen = screens.peek();
		}

		repaint(); // refresh the view
	}

	/**
	 * Called by Swing to render the view's graphics.
	 */
	@Override
	public void paint(Graphics g) {
		if (currentScreen == null) return;

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

	/**
	 * Called whenever a key is pressed.
	 */
	@Override
	public void keyPressed(final KeyEvent e) {
		if (currentScreen == null) return;

		currentScreen.keyPressed(e);
	}

	@Override
	public void keyReleased(final KeyEvent e) { }

	@Override
	public void keyTyped(final KeyEvent e) { }

}
