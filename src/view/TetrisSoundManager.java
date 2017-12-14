package view;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Plays the classic Tetris theme music as well as sound effects when needed.
 */
public class TetrisSoundManager {
    private final Clip musicClip;

    public TetrisSoundManager() {
        try {
            final AudioInputStream audioIn = AudioSystem.getAudioInputStream(
                new File("data/music.wav"));
            musicClip = AudioSystem.getClip();
            musicClip.open(audioIn);
        } catch (LineUnavailableException e){
            e.printStackTrace();
            throw new RuntimeException("TetrisSoundManager: line unavailable.");
        } catch (UnsupportedAudioFileException e){
            e.printStackTrace();
            throw new RuntimeException("TetrisSoundManager: unsupported audio file.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("TetrisSoundManager: Input/Output Error.");
        }
    }

    public void startMusic() {
        musicClip.setFramePosition(0); // rewind
        musicClip.loop(Clip.LOOP_CONTINUOUSLY); // infinite loop
        musicClip.start();
    }

    public void stopMusic() { musicClip.stop(); }
}
