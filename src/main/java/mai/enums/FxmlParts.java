package mai.enums;

public enum FxmlParts {

    MENU{
        public String getPath(){
            return "/FxmlFiles/Game/GameMenu.fxml";
        }
    },
    GAME{
        public String getPath(){
            return "/FxmlFiles/Game/Game.fxml";
        }
    },
    GAMEOVER {
        public String getPath(){
            return "/FxmlFiles/Game/GameOver.fxml";
        }
    },
    CONFIG{
        public String getPath(){
            return "/FxmlFiles/Game/GameConfig.fxml";
        }
    },

}
