package mai.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;

public class MenuAudio {
//    public static AudioInputStream CANCEL_AUDIO;
//    public static AudioInputStream MOVE_AUDIO;
//    public static AudioInputStream SELECT_AUDIO;
//    public static AudioInputStream START_AUDIO;
//    public static AudioInputStream OK_AUDIO;
//
//    public static AudioInputStream LOST;
//    public static AudioInputStream SUMMON;

    public static File CANCEL_AUDIO;
    public static File MOVE_AUDIO;
    public static File SELECT_AUDIO;
    public static File START_AUDIO;
    public static File OK_AUDIO;

    public static File LOST;
    public static File SUMMON;


    public static void loadSounds() throws UnsupportedAudioFileException, IOException {
//        CANCEL_AUDIO = AudioSystem.getAudioInputStream(new File(ButtonAudio.CANCEL.getAudio()));
//        MOVE_AUDIO = AudioSystem.getAudioInputStream(new File(ButtonAudio.MOVE.getAudio()));
//        SELECT_AUDIO = AudioSystem.getAudioInputStream(new File(ButtonAudio.MOVE.getAudio()));
//        START_AUDIO = AudioSystem.getAudioInputStream(new File(ButtonAudio.MOVE.getAudio()));
//        OK_AUDIO = AudioSystem.getAudioInputStream(new File(ButtonAudio.MOVE.getAudio()));
//
//        LOST = AudioSystem.getAudioInputStream(new File(Sound.LOST.getAudio()));
//        SUMMON = AudioSystem.getAudioInputStream(new File(Sound.SUMMON.getAudio()));

        CANCEL_AUDIO = new File(ButtonAudio.CANCEL.getAudio());
        MOVE_AUDIO = new File(ButtonAudio.MOVE.getAudio());
        SELECT_AUDIO = new File(ButtonAudio.MOVE.getAudio());
        START_AUDIO = new File(ButtonAudio.MOVE.getAudio());
        OK_AUDIO = new File(ButtonAudio.MOVE.getAudio());

        LOST = new File(Sound.LOST.getAudio());
        SUMMON = new File(Sound.SUMMON.getAudio());
    }

}
