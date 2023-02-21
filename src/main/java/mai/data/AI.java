package mai.data;

import mai.enums.Difficulty;

import java.util.Set;

public class AI extends User{

    private Set<Difficulty> DIFFICULTIES;

    public AI(String playerName, String playerColour, String profilePictureUrl) {
        super(playerName, playerColour, profilePictureUrl);
    }

    public Set<Difficulty> getAiTypes() {
        return DIFFICULTIES;
    }

    public void setAiTypes(Set<Difficulty> DIFFICULTIES) {
        this.DIFFICULTIES = DIFFICULTIES;
    }
}
