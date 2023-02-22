package mai.audio;

import mai.enums.ButtonAudio;

import java.io.File;

public class MenuAudio {
    public final static File CANCEL_AUDIO = new File(ButtonAudio.CANCEL.getAudio());
    public final static File MOVE_AUDIO = new File(ButtonAudio.MOVE.getAudio());
    public final static File SELECT_AUDIO = new File(ButtonAudio.SELECT.getAudio());
    public final static File START_AUDIO = new File(ButtonAudio.START.getAudio());
    public final static File OK_AUDIO = new File(ButtonAudio.OK.getAudio());
}
