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
		setScreen(screen, true);
	}

	/**
	 * Add a Screen to the stack, initialize it and set it as the current one.
	 *
	 * The actual operation will be done after the current Screen's update
	 * function returned, to allow this Screen to properly terminate if needed.
	 */
	@Override
	public void pushScreen(final Screen screen) {
		if (screen == null)
			throw new IllegalArgumentException("TetrisView.pushScreen : null Screen.");

		screenToAdd = screen;
	}

	@Override
	public void requestExit() {
		timer.stop();
		System.exit(0);
	}

	/**
	 * Set the given Screen as the current one.
	 *
	 * @param screen The screen.
	 * @param newScreen If true, initialize the Screen first.
	 */
	private void setScreen(final Screen screen, final boolean newScreen) {
		timer.stop();

		currentScreen = screen;

		if (newScreen) {
			screens.push(currentScreen);

			currentScreen.init(this);
			if (!currentScreen.hasContainer())
				throw new IllegalStateException(
					"TetrisView.setNewScreen : the given Screen has not set its container properly in its init method.");
		}

		final int delay = currentScreen.updateRate();
		timer.setInitialDelay(delay);
		timer.setDelay(delay); // call this.actionPerformed at fixed intervals
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (currentScreen == null) return;

		// current screen update
		if (currentScreen.update()) { // terminate the current Screen ?
			screens.pop();

			if (screens.empty() && screenToAdd == null) { // last Screen : exit
				timer.stop();
				System.exit(0);
				return;
			}

			setScreen(screens.peek(), false); // resume the previous Screen's execution
		}

		// Screen push ?
		if (screenToAdd != null) {
			setScreen(screenToAdd, true);
			screenToAdd = null;
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
