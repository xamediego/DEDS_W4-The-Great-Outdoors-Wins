package mai.enums;

public enum ButtonAudio {

    CANCEL{
        public String getAudio(){
            return "src/main/resources/Menu_sounds/C_CANCEL.wav";
        }
    },
    MOVE{
        public String getAudio(){return "src/main/resources/Menu_sounds/C_MOVE.wav";}
    },
    SELECT{
        public String getAudio(){
            return "src/main/resources/Menu_sounds/C_SELECT.wav";
        }
    },
    START{
        public String getAudio(){
            return "src/main/resources/Menu_sounds/C_START.wav";
        }
    },
    OK{
        public String getAudio(){
            return "src/main/resources/Menu_sounds/C_OK.wav";
        }
    };

    public abstract String getAudio();

}
