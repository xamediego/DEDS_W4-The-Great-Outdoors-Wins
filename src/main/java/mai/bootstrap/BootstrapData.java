package mai.bootstrap;

import java.io.IOException;
import java.net.URISyntaxException;

public class BootstrapData {


    public static void loadData(){
        try {
            GenerateData.createUserWithImage();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
