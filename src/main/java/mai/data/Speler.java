package mai.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Speler {

    private String naam;
    private String spelerKleur;
    private String profilePictureUrl;

    private int spelerNummer, minBereik, bereik;

    @Override
    public String toString() {
        return "User{" +
                "playerName='" + naam + '\'' +
                ", playerColour='" + spelerKleur + '\'' +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                ", playerNumber=" + spelerNummer +
                ", attackDropOff=" + minBereik +
                ", range=" + bereik +
                '}';
    }
}
