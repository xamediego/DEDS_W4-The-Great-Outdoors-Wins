package mai.audio;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AudioPlayer {
    public static void playAudioFile(InputStream stream){
            try {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(stream);

                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();

            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                System.out.println("Something went wrong while reading this audio file");
                System.out.println(e);
            }
    }
}
