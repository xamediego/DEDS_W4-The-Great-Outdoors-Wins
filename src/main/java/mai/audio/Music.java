package mai.audio;

import java.io.InputStream;

public enum Music {

    CONFIG{
        public InputStream getAudio(){
            return getClass().getResourceAsStream("/Sounds/M_CONFIG.wav");
        }
    };

    public abstract InputStream getAudio();
}
