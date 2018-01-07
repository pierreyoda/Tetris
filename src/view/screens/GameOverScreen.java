package view.screens;

import control.TetrisController;
import model.TetrisGameSession;
import view.RenderingUtilities;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * This Screen displays a recap of the game session (scores, number of cleared lines).
 *
 * It also allows the player to enter a name when he achieves a new high score.
 */
public class GameOverScreen extends Screen {
    private static final int UPDATE_INTERVAL = 50;
    private static final Color BACKGROUND_COLOR = MainMenuScreen.BACKGROUND_COLOR;
    private static final Color TEXT_COLOR = Color.WHITE;

    private static final int PLAYER_NAME_MAX_LENGTH = 10;

    private final TetrisController gameController;
    private final TetrisGameSession gameSession;

    private boolean finished = false;
    private boolean hasPressedEnter = false;
    private String playerName = "";

    public GameOverScreen(final TetrisController gameController) {
        super(UPDATE_INTERVAL, BACKGROUND_COLOR);

        this.gameController = gameController;
        this.gameSession = gameController.getLastGameSession();
    }

    @Override
    public boolean update() {
        if (finished && playerName.trim().length() > 0) {
            System.out.format("Player name input : \"%s\".\n", playerName);
            gameController.submitHighScoreAndSave(playerName, gameSession.score());

            final Screen nextScreen = new HighScoresScreen(gameController.getHighscores(), false)
                .setGameSession(gameSession);
            container().pushScreen(nextScreen);
            return true;
        }
        return false;
    }

    @Override
    public void render(Graphics2D g2d, Font textFont) {
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(textFont);

        final int w = container().containerWidth(), h = container().containerHeight();

        // game recap
        final int score = gameSession.score();
        final int linesCleared = gameSession.linesCleared();

        RenderingUtilities.drawCenteredText(g2d, textFont, w / 2, (int) (h / 10),
            "GAME OVER !");
        RenderingUtilities.drawCenteredText(g2d, textFont, w / 2, (int) (1.5 * h / 10),
            String.format("Score : %d", score));
        RenderingUtilities.drawCenteredText(g2d, textFont, w / 2, (int) (2 * h / 10),
            String.format("Number of lines cleared : %d", linesCleared));

        // player name input
        if (gameSession.newHighScoreIndex() >= 0) {
            RenderingUtilities.drawCenteredText(g2d, textFont, w / 2, (int) (h / 2),
                "INPUT PLAYER NAME");
            RenderingUtilities.drawCenteredText(g2d, textFont, w / 2, (int) (h / 1.8),
                "(PRESS ENTER TWICE TO VALIDATE)");
            RenderingUtilities.drawCenteredText(g2d, textFont, w / 2, (int) (h / 1.5),
                playerName);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        final int keyCode = e.getKeyCode();

        if (gameSession.newHighScoreIndex() < 0) {
            if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE) {
                finished = true;
            }
            return;
        }

        boolean nameModified = false;
        switch (keyCode) {
        // double enter to validate
        case KeyEvent.VK_ENTER:
            if (!playerName.trim().isEmpty()) {
                if (hasPressedEnter)
                    finished = true;
                else
                    hasPressedEnter = true;
            }
            return;
        // add space
        case KeyEvent.VK_SPACE:
            playerName += ' ';
            return;
        // delete last character
        case KeyEvent.VK_BACK_SPACE:
        case KeyEvent.VK_DELETE:
            if (playerName.length() > 0) {
                playerName = playerName.substring(0, playerName.length() - 1);
                hasPressedEnter = false; // name modified : reset validation status
            }
            return;
        }

        // character input
        if (playerName.length() >= PLAYER_NAME_MAX_LENGTH) return;
        if ((65 <= keyCode && keyCode <= 90) // A-Z
            || (48 <= keyCode && keyCode <= 57)) { // 0-9
            playerName += KeyEvent.getKeyText(keyCode);
            nameModified = true;
        }
        if (96 <= keyCode && keyCode <= 105) { // Numpad 0-9
            playerName += String.valueOf(keyCode - 96);
            nameModified = true;
        }

        // name modified : reset validation status
        if (nameModified)
            hasPressedEnter = false;
    }
}
