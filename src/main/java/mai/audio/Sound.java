package mai.audio;

public enum Sound {

    LOST{
        public String getAudio(){
            return "src/main/resources/Sounds/S_LOST.wav";
        }
    },
    SUMMON{
        public String getAudio(){
            return "src/main/resources/Sounds/S_SUMMON.wav";
        }
    };

    public abstract String getAudio();

}
