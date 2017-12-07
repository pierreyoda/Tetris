package model;

/**
 * The TetrisBoard class modelizes all the static tetrimino blocks present in the game.
 * 
 * This excludes the blocks of the currently played tetrimino, which is dynamic.
 */
public class TetrisBoard {
	private static final char DEBUG_PRESENT = 'x'; // character representing a visible cell
	private static final char DEBUG_ABSENT = '.'; // character representing a null cell

	public static int WIDTH = 10;
	public static int HEIGHT = 22;

	/**
	 * The internal representation of the board.
	 * NB : first dimension is Y (vertical), second dimension is X (horizontal).
	 */
	private TetrisBoardCell[][] cells = new TetrisBoardCell[HEIGHT][WIDTH];
	
	public TetrisBoard() {
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				cells[y][x] = new TetrisBoardCell(false, new TetriminoColor(0, 0, 0));
			}
		}
	}

	/**
	 * Add a new, 'freezed' tetrimino to the board.
	 */
	public void addTetrimino(final Tetrimino tetrimino) {
		for (int j = 0; j < 4 && (tetrimino.getY() + j) < HEIGHT; j++) {
			for (int i = 0; i < 4 && (tetrimino.getX() + i) < WIDTH; i++) {
				if (!tetrimino.getBlock(i, j)) continue;
				final TetrisBoardCell cell = cells[tetrimino.getY() + j][tetrimino.getX() + i];
				cell.present = true;
				cell.color = tetrimino.getColor();
			}
		}
	}
	
	/**
	 * Check for complete lines, clear them and return the number of cleared lines.
	 * 
	 * A complete line is cleared from the board by erasing it and dropping down the
	 * other lines above it by 1 tile.
	 */
	public int checkForCompleteLines() {
		int numberOfLinesCleared = 0;

		for (int y = 0; y < HEIGHT; y++) {
			boolean complete = true;
			for (int x = 0; x < WIDTH; x++) {
				if (!cells[y][x].present) {
					complete = false;
					break;
				}
			}
			if (complete) {
				clearLine(y);
				++numberOfLinesCleared;
			}
		}

		return numberOfLinesCleared;
	}
	
	private void clearLine(final int lineY) {
		// TODO
	}
	
	/**
	 * Returns a string representation of the board. Useful for debugging.
	 */
	@Override
	public String toString() {
		// we use a StringBuilder since it's faster than string concatenation in loops
		final StringBuilder builder = new StringBuilder((WIDTH + 1) * HEIGHT);

		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				builder.append(cells[y][x].present ? DEBUG_PRESENT : DEBUG_ABSENT);
			}
			builder.append('\n'); // line break
		}

		return builder.toString();
	}
	
	public TetrisBoardCell[][] getCells() { return cells; }

}
