package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Class responsible for retrieving and persisting highscores
 * from and to the game's folder.
 */
public class TetrisScoreManager {
	final private static String SCORES_FILEPATH = "scores.txt";
	final private static String SCORE_DELIMITER = "=";
	final private static int MAX_HIGHSCORES = 10;

	/**
	 * A Tetris highscore entry.
	 */
	public class TetrisHighScore {
		public final String name;
		public final int score;

		public TetrisHighScore(final String name, final int score) {
			this.name = name;
			this.score = score;
		}
	}

	/**
	 * Array of all the highscores. The first highscore must be the highest one.
	 */
	private final ArrayList<TetrisHighScore> highscores = new ArrayList<>(MAX_HIGHSCORES);

	public TetrisScoreManager() {
		for (int i = 0; i < MAX_HIGHSCORES; i++) {
			highscores.add(new TetrisHighScore("", -1));
		}
	}

	/** Submit a new highscore. Returns true if the given score is enough for that,
	 * false otherwise.
	 */
	public boolean submit(final String name, final int score) {
		if (name.contains(SCORE_DELIMITER))
			throw new IllegalArgumentException(String.format(
				"highscore name must not contain \"%s\"", name));

		if (score < highscores.get(MAX_HIGHSCORES - 1).score) return false;

		for (int i = 0; i < MAX_HIGHSCORES; i++) {
			if (score > highscores.get(i).score) {
				highscores.add(i, new TetrisHighScore(name, score));
				highscores.remove(highscores.size() - 1);
				return true;
			}
		}
		return false;
	}

	/** Try to load the highscores from the file system.
	 * @return True if successful, false otherwise.
	 */
	public boolean load() {
		try {
			final File file = new File(SCORES_FILEPATH);
			file.createNewFile(); // create a new, empty file if it does not already exist
			final FileReader reader = new FileReader(file);
			final BufferedReader bufferedReader = new BufferedReader(reader);
			String line;
			int i = 0;
			while ((line = bufferedReader.readLine()) != null && i < MAX_HIGHSCORES) {
				 final String[] strings = line.split(SCORE_DELIMITER, 2);
				 if (strings.length < 2) {
					 System.out.format("Score manager : invalid highscore \"%s\".\n", line);
					 continue;
				 }

				 final String name = strings[0].trim();
				 final String scoreString = strings[1].trim();
				 if (name.isEmpty()) continue;

				 final int score = Integer.parseUnsignedInt(scoreString);
				 highscores.set(i++, new TetrisHighScore(name, score));
			}

			bufferedReader.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/** Try to save the highscores to the file system.
	 * @return True if successful, false otherwise.
	 */
	public boolean save() {
		try {
			final File file = new File(SCORES_FILEPATH);
			file.createNewFile(); // create a new, empty file if it does not already exist
			final FileOutputStream ouput = new FileOutputStream(file, false); // open and erase the file
			final PrintStream stream = new PrintStream(ouput);

			for (int i = 0; i < MAX_HIGHSCORES; i++) {
				final TetrisHighScore highscore = highscores.get(i);
				stream.format("%s %s %d\r\n",
							  highscore.name, SCORE_DELIMITER, highscore.score);
			}

			stream.flush();
			stream.close();
			ouput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Get the list of highscores.
	 * NB : the highscore of index 0 is the highest one.
	 * @return List of highscores.
	 */
	public ArrayList<TetrisHighScore> getHighscores() { return highscores; }

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();

		builder.append("===== HIGH SCORES =====\n");

		int count = 0;
		for (int i = 0; i < MAX_HIGHSCORES; i++) {
			final TetrisHighScore highscore = highscores.get(i);
			if (highscore.name.isEmpty() || highscore.score < 0) continue;
			builder.append(String.format("\"%s\" %s %d\n",
										 highscore.name,
										 SCORE_DELIMITER,
										 highscore.score));
			++count;
		}
		if (count == 0) builder.append("No high scores.\n");

		return builder.toString();
	}

}
