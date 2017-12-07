package model;

/**
 * A tetrimino is a geometric shape composed of four squares, able to move and
 * rotate and with its own color.
 */
public class Tetrimino {
	/**
	 * The color of the blocks composing the tetrimino.
	 */
	private TetriminoColor color;

	/**
	 * The array defining the geometry of the tetrimino. True means that a block is
	 * present.
	 * First dimension is the rotation index (0-3), second dimension is X (horizontal axis) and
	 * third dimension is Y (vertical axis).
	 */
	private boolean[][][] blocks = new boolean[4][4][4];

	/**
	 * The X and Y positions on the board. X is the horizontal axis and Y the
	 * vertical ones (descending).
	 */
	private int positionX, positionY;
	
	/**
	 * The type of the tetrimino.
	 */
	private TetriminoType type;
	
	/**
	 * The current rotation state.
	 */
	private int rotation = 0;

	public Tetrimino(final TetriminoColor color, final TetriminoType type,
					 final int positionX, final int positionY,
					 final boolean[][][] blocks) {
		this.color = color;
		this.type = type;
		this.positionX = positionX;
		this.positionY = positionY;
		this.blocks = blocks;
	}

	public static boolean[][][] getBlocksFromType(final TetriminoType type) {
		switch (type) {
		case STICK:
			return new boolean[][][] {
				{
					{ true, true, true, true }, { false, false, false, false },
				    { false, false, false, false }, { false, false, false, false },
				},
				{
					{ false, false, true, false }, { false, false, true, false },
				    { false, false, true, false }, { false, false, true, false },
				},
				{
					{ false, false, false, false }, { false, false, false, false },
					{ true, true, true, true }, { false, false, false, false },
				},
				{
					{ false, true, false, false }, { false, true, false, false },
					{ false, true, false, false }, { false, true, false, false },
				},
			};
		case BOX:
			return new boolean[][][] {
				{
					{ true, true, false, false }, { true, true, false, false },
					{ false, false, false, false }, { false, false, false, false },
				},
				{
					{ true, true, false, false }, { true, true, false, false },
					{ false, false, false, false }, { false, false, false, false },
				},
				{
					{ true, true, false, false }, { true, true, false, false },
					{ false, false, false, false }, { false, false, false, false },
				},
				{
					{ true, true, false, false }, { true, true, false, false },
					{ false, false, false, false }, { false, false, false, false },
				},
			};
		case STAIRS:
			return new boolean[][][] {
				{
					{ false, true, false, false }, { true, true, true, false },
					{ false, false, false, false }, { false, false, false, false },
				},
				{
					{ false, true, false, false }, { false, true, true, false },
					{ false, true, false, false }, { false, false, false, false },
				},
				{
					{ false, false, false, false }, { true, true, true, false },
					{ false, true, false, false }, { false, false, false, false },
				},
				{
					{ false, true, false, false }, { true, true, false, false },
					{ false, true, false, false }, { false, false, false, false },
				},
			};
		case RIGHT_SNAKE:
			return new boolean[][][] {
				{
					{ false, false, true, true }, { false, true, true, false },
					{ false, false, false, false }, { false, false, false, false },
				},
				{
					{ false, false, true, false }, { false, false, true, true },
					{ false, false, false, true }, { false, false, false, false },
				},
				{
					{ false, false, false, false }, { false, false, true, true },
					{ false, true, true, false }, { false, false, false, false }
				},
				{
					{ false, true, false, false }, { false, true, true, false },
					{ false, false, true, false }, { false, false, false, false },
				},
			};
		case LEFT_SNAKE:
			return new boolean[][][] {
				{
					{ true, true, false, false }, { false, true, true, false },
					{ false, false, false, false }, { false, false, false, false },
				},
				{
					{ false, false, true, false }, { false, true, true, false },
					{ false, true, false, false }, { false, false, false, false },
				},
				{
					{ false, false, false, false }, { true, true, false, false },
					{ false, true, true, false }, { false, false, false, false }
				},
				{
					{ false, true, false, false }, { true, true, false, false },
					{ true, false, false, false }, { false, false, false, false },
				},
			};
		case LEFT_L:
			return new boolean[][][] {
				{
					{ true, false, false, false }, { true, true, true, false },
					{ false, false, false, false }, { false, false, false, false }
				},
				{
					{ false, true, true, false }, { false, true, false, false },
					{ false, true, false, false }, { false, false, false, false }
				},
				{
					{ false, false, false, false }, { true, true, true, false },
					{ false, false, true, false }, { false, false, false, false },
				},
				{
					{ false, true, false, false }, { false, true, false, false },
					{ true, true, false, false }, { false, false, false, false },
				},
			};
		case RIGHT_L:
			return new boolean[][][] {
				{
					{ false, false, true, false }, { true, true, true, false },
					{ false, false, false, false }, { false, false, false, false }
				},
				{
					{ false, true, false, false }, { false, true, false, false },
					{ false, true, true, false }, { false, false, false, false },
				},
				{
					{ false, false, false, false }, { true, true, true, false },
					{ true, false, false, false }, { false, false, false, false },
				},
				{
					{ true, true, false, false }, { false, true, false, false },
					{ false, true, false, false }, { false, false, false, false },
				},
			};
		}
		return null;
	}

	public void rotate(final boolean right) {
		rotation = (right ? (rotation + 1) : (rotation - 1)) % 4;
	}

	public void move(final int deltaX, final int deltaY) {
		positionX = positionX + deltaX;
		positionY = positionY + deltaY;
	}

	public int getX() { return positionX; }
	public int getY() { return positionY; }

	public boolean getBlock(final int x, final int y) {
		if (x < 0 || x >= 4 || y < 0 || y >= 4)
			return false;
		return blocks[rotation][x][y];
	}

	public TetriminoColor getColor() { return color; }
	public TetriminoType getType() { return type; }

}
