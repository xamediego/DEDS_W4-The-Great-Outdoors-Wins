package mai.data;

import mai.enums.AIType;

import java.util.Set;

public class AI extends User{

    private Set<AIType> aiTypes;

    public AI(String playerName, String playerColour, String profilePictureUrl) {
        super(playerName, playerColour, profilePictureUrl);
    }

    public Set<AIType> getAiTypes() {
        return aiTypes;
    }

    public void setAiTypes(Set<AIType> aiTypes) {
        this.aiTypes = aiTypes;
    }
}
