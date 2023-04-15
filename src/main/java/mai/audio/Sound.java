package mai.audio;

import java.io.InputStream;

public enum Sound {

    LOST{
        public InputStream getAudio(){
            return getClass().getResourceAsStream("/Sounds/S_LOST.wav");
        }
    },
    SUMMON{
        public InputStream getAudio(){
            return getClass().getResourceAsStream("/Sounds/S_SUMMON.wav");
        }
    };

    public abstract InputStream getAudio();

}
