package mai.enums;

public enum Sound {

    LOST{
        public String getAudio(){
            return "src/main/resources/Sounds/Lost.wav";
        }
    },
    WIN{
        public String getAudio(){
            return "src/main/resources/Sounds/Win.wav";
        }
    },
    DRAW{
        public String getAudio(){
            return "src/main/resources/Sounds/Draw.wav";
        }
    };

    public abstract String getAudio();

}
