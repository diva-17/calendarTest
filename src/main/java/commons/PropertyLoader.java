package commons;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {

    public Properties loadProperty(String propertyName) {
        Properties prop = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(propertyName)) {
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;
    }

    public Properties loadQueriesProperty() {
        return loadProperty("propertyFile.properties");
    }


}