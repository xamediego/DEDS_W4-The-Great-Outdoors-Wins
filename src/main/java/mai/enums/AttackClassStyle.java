package mai.enums;

public enum AttackClassStyle {

    SHORT{
        public String getStyle(){
            return "copySelect";
        }
    },
    LONG{
        public String getStyle(){
            return "farSelect";
        }
    }
}
