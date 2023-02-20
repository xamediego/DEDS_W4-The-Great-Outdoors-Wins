package mai;

import mai.bootstrap.BootstrapData;

public class Main {
    public static void main(String[] args) {
        BootstrapData.loadData();

        JFXApplication.main(args);
    }
}
