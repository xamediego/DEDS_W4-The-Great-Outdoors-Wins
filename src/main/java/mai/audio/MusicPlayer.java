package mai.audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class MusicPlayer {

    private static Clip clip;

    public static void setMusic(InputStream stream) {
        try {
            if(clip != null) clip.stop();
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(stream);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public static void playMusic(){
        clip.start();
    }

    public static void loopMusic(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public static void stopMusic(){
        clip.stop();
    }
}
