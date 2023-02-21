package mai.service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {
    public static void playAudioFile(File file){

            try {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();

            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                System.out.println("Something went wrong while reading this audio file");
                System.out.println(e);
            }


    }
}
