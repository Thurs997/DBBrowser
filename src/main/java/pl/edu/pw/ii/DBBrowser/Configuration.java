package pl.edu.pw.ii.DBBrowser;

import pl.edu.pw.ii.DBBrowser.Utils.ConfigLoader;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by lucas on 07.06.14.
 */
public class Configuration {
    private static Configuration instance;
    private Properties properties;
    private Properties defaultProperties;
    private Configuration() {
        properties = new Properties();
        defaultProperties = new Properties();
        defaultProperties.setProperty("configFile", "config.ini");
        defaultProperties.setProperty("port", "8080");
        defaultProperties.setProperty("maxConnectionsInMinute", "10");
    }

    public static Configuration getInstance() {
        if(instance == null){
            synchronized (Configuration.class){
                if(instance == null)
                    instance = new Configuration();
            }
        }
        return instance;
    }

    public void load(String fileName) throws IOException {
        properties = ConfigLoader.loadFromFile(fileName);
    }

    public String getProperty(String propertyName) {
        String retString = properties.getProperty(propertyName);
        if(retString == null)
            retString = defaultProperties.getProperty(propertyName);
        return retString;
    }

    public int getPropertyAsInt(String propertyName) {
        String retString = properties.getProperty(propertyName);
        if(retString == null)
            retString = defaultProperties.getProperty(propertyName);
        return Integer.parseInt(retString);
    }
}
