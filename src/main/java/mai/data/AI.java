package mai.data;

import mai.enums.DIFFICULTY;

import java.util.Set;

public class AI extends User{

    private Set<DIFFICULTY> DIFFICULTIES;

    public AI(String playerName, String playerColour, String profilePictureUrl) {
        super(playerName, playerColour, profilePictureUrl);
    }

    public Set<DIFFICULTY> getAiTypes() {
        return DIFFICULTIES;
    }

    public void setAiTypes(Set<DIFFICULTY> DIFFICULTIES) {
        this.DIFFICULTIES = DIFFICULTIES;
    }
}
