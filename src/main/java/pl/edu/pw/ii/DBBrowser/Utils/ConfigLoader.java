package pl.edu.pw.ii.DBBrowser.Utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Created by lucas on 07.06.14.
 */
public class ConfigLoader {

    public static Properties loadFromFile(String fileName) throws IOException {
        //TODO uncomment before deploy
        InputStream input;
        input = ConfigLoader.class.getClassLoader().getResourceAsStream(fileName);
        if(input == null)
            return new Properties();
        Properties prop = loadFromStream(input);
        if(input != null)
            input.close();
        return prop;
    }
    public static Properties loadFromString(String configText) throws IOException {
        InputStream input = new ByteArrayInputStream(configText.getBytes(StandardCharsets.UTF_8));
        Properties prop =  loadFromStream(input);
        input.close();
        return prop;
    }

    public static Properties loadFromStream(InputStream inputStream) throws IOException {
        Properties prop = new Properties();
        prop.load(inputStream);
        return prop;
    }
}
