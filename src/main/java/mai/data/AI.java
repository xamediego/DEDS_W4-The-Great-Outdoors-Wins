package mai.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import mai.enums.AIType;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Data
public class AI extends Speler {



    private Set<AIType> Types = new HashSet<>();

    public AI(String naam, String kleur, String profilePictureUrl, int spelerNummer, int minBereik, int bereik) {
        super(naam, kleur, profilePictureUrl, spelerNummer, minBereik, bereik);
    }

    public Set<AIType> getAiTypes() {
        return Types;
    }

    public void setAiTypes(Set<AIType> DIFFICULTIES) {
        this.Types = DIFFICULTIES;
    }


}
