package model;

import java.util.Random; /**
 * <pre>
 * Enumerates the different possible types of tetrimino :
 * - STICK xxxx
 * - BOX xx
 *       xx
 * - STAIRS  x
 *          xxx
 * - RIGHT SNAKE  xx
 *               xx
 * - LEFT SNAKE      xx
 *                    xx
 * - LEFT L       x
 *                xxx
 * - RIGHT L   x
 *           xxx
 *  </pre>
 */
public enum TetriminoType {
	STICK,
	BOX,
	STAIRS,
	RIGHT_SNAKE,
	LEFT_SNAKE,
	LEFT_L,
	RIGHT_L,;

	/**
	 * Get a random TetriminoType.
	 */
    public static TetriminoType getRandomType(Random random) {
    	return TetriminoType.values()[random.nextInt(TetriminoType.values().length)];
    }
}
