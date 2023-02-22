package mai.enums;

public enum ButtonType {

    SHORTRANGE {
        public String getType() {
            return "copySelect";
        }
    },
    LONGRANGE {
        public String getType() {
            return "farSelect";
        }
    },
    SELECT {
        public String getType() {
            return "spaceSelect";
        }
    };

    public abstract String getType();
}
