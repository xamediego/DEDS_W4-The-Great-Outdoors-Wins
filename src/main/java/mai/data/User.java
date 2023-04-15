package mai.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(playerName, user.playerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName);
    }
}
