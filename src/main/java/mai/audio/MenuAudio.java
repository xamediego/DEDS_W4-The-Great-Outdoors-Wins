package mai.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;

public class MenuAudio {
    public static File CANCEL_AUDIO;
    public static File MOVE_AUDIO;
    public static File SELECT_AUDIO;
    public static File START_AUDIO;
    public static File OK_AUDIO;

    public static File LOST;
    public static File SUMMON;


    public static void loadSounds() throws UnsupportedAudioFileException, IOException {
        CANCEL_AUDIO = new File(ButtonAudio.CANCEL.getAudio());
        MOVE_AUDIO = new File(ButtonAudio.MOVE.getAudio());
        SELECT_AUDIO = new File(ButtonAudio.SELECT.getAudio());
        START_AUDIO = new File(ButtonAudio.START.getAudio());
        OK_AUDIO = new File(ButtonAudio.OK.getAudio());

        LOST = new File(Sound.LOST.getAudio());
        SUMMON = new File(Sound.SUMMON.getAudio());
    }

}
