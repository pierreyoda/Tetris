package model;

/**
 * The TetrisBoard class modelizes all the static tetrimino blocks present in the game.
 *
 * This excludes the blocks of the currently played tetrimino, which is dynamic.
 */
public class TetrisBoard {
	private static final char DEBUG_PRESENT = 'x'; // character representing a visible cell
	private static final char DEBUG_ABSENT = '.';  // character representing a null cell

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

	/**
	 * Clear a completed line and drop down the lines above it.
	 * @param lineY Vertical position of the line to clear (0 : top).
	 */
	private void clearLine(final int lineY) {
		// clear the line
		for (int x = 0; x < WIDTH; x++) {
			final TetrisBoardCell cell = cells[lineY][x];
			cell.present = false;
			cell.color.set(0, 0, 0);
		}

		// move the upper lines' blocks downwards
		for (int y = lineY - 1; y >= 0; y--) {
			for (int x = 0; x < WIDTH; x++) {
				final TetrisBoardCell cell = cells[y][x];
				if (!cell.present) continue;

				// save block info & clear
				final TetriminoColor color = new TetriminoColor(cell.color); // copy
				cell.present = false;

				// move downwards
				final TetrisBoardCell newCell = cells[y + 1][x];
				newCell.present = true;
				newCell.color = color;
			}
		}
	}

	/**
	 * Completely clear the board from any existing cell.
	 */
	public void clear() {
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				final TetrisBoardCell cell = cells[y][x];
				cell.present = false;
				cell.color.set(0, 0, 0);
			}
		}
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

	/**
	 * <pre>
	 * Get the two-dimensional array describing the board's cells.
	 * NB : the array's first dimension is the Y axis (vertical), and its second
	 * one is the X axis (horizontal).
	 * The coordinate system works as follows :
	 *
	 * +---------> X
	 * |
	 * |
	 * |
	 * |
	 * v
	 *
	 * Y
	 *
	 * </pre>
	 *
	 * @return Cells of the board.
	 */
	public TetrisBoardCell[][] getCells() { return cells; }

}
