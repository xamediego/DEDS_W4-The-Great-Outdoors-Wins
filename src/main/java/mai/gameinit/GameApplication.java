package mai.gameinit;

import mai.data.AI;
import mai.data.User;
import mai.service.AIService;
import mai.service.UserService;

import java.util.List;

public class GameApplication {


    public GameApplication(User user, List<AI> aiUsers) {
        UserService.user = user;
        AIService.aiList = aiUsers;
    }

}
