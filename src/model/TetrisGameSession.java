package model;

/**
 * Informations about a game session.
 */
public class TetrisGameSession {
    private final int score;
    private final int newHighScoreIndex;
    private final int linesCleared;

    public TetrisGameSession(final int score, final int newHighScoreIndex,
                             final int linesCleared) {
        this.score = score;
        this.newHighScoreIndex = newHighScoreIndex;
        this.linesCleared = linesCleared;
    }

    public int score() { return score; }
    public int newHighScoreIndex() { return newHighScoreIndex; }
    public int linesCleared() { return linesCleared; }

}
