package mai.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String playerName;
    private String playerColour;
    private String profilePictureUrl;

    private int playerNumber, attackDropOff, range;

    @Override
    public String toString() {
        return "User{" +
                "playerName='" + playerName + '\'' +
                ", playerColour='" + playerColour + '\'' +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                ", playerNumber=" + playerNumber +
                ", attackDropOff=" + attackDropOff +
                ", range=" + range +
                '}';
    }
}
