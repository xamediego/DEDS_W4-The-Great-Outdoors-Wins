package mai.enums;

public enum FXMLPart {

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
    GAMECONFIG{
        public String getPath(){
            return "/FxmlFiles/Game/GameConfig.fxml";
        }
    },
    TITLEBAR{
        public String getPath(){
            return "/FxmlFiles/Game/TitleBar.fxml";
        }
    };

    public abstract String getPath();

}
