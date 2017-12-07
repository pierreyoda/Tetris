package model;

/**
 * An individual cell in the TetrisBoard model.
 *
 * Note : we could also store a flag of presence in TetriminoColor.
 */
public class TetrisBoardCell {
	public boolean present;
	public TetriminoColor color;

	public TetrisBoardCell(final boolean present, final TetriminoColor color) {
		this.present = present;
		this.color = color;
	}
}
