package Server;

import java.io.*;
import java.util.Properties;

public class Configurations {

    private static Configurations instance=null;
    private static Properties prop;

    public static Configurations getInstance()
    {
        if (instance == null)
        {
            instance = new Configurations();
        }
        return instance;
    }

    private Configurations() {
       prop = new Properties();
    }

    public String getProperty(String name){
        try {
            InputStream input = new FileInputStream("resources/config.properties");
            prop.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop.getProperty(name);
    }

    public void setProperty(String key, String value) {
        try (OutputStream output = new FileOutputStream("resources/config.properties")) {

            prop.setProperty(key, value);
            prop.store(output,"");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
