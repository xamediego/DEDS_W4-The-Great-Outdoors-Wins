package mai.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mai.enums.Difficulty;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class AI extends User{



    private Set<Difficulty> DIFFICULTIES = new HashSet<>();

    public AI(String playerName, String playerColour, String profilePictureUrl, int playerNumber, int attackDropOff, int range) {
        super(playerName, playerColour, profilePictureUrl, playerNumber, attackDropOff, range);
    }

    public Set<Difficulty> getAiTypes() {
        return DIFFICULTIES;
    }

    public void setAiTypes(Set<Difficulty> DIFFICULTIES) {
        this.DIFFICULTIES = DIFFICULTIES;
    }


}
