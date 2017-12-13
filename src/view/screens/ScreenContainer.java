package view.screens;

/**
 * Implemented by a Screen container.
 *
 * This allows the container to safely pass a ScreenContainer reference of itself
 * to its screens.
 */
public interface ScreenContainer {

	/**
	 * Add a new Screen to the stack, initialize it and set it as the current one.
	 */
	public void pushScreen(final Screen screen);

}
