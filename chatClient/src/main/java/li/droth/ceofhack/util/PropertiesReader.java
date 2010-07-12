package li.droth.ceofhack.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * <p/>
 *
 * @author danielroth
 */
public class PropertiesReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesReader.class);

    public static Properties readProperties(Class defaultRoot, String fileName) {
        InputStream in = null;

        Properties properties = new Properties();
        File propFile;
        if (fileName.startsWith("/"))
            propFile = new File(fileName.substring(1));
        else
            propFile = new File(fileName);

        if (propFile.exists() && !propFile.isDirectory()) {
            try {
                in = new FileInputStream(propFile);
            } catch (FileNotFoundException e) {
                LOGGER.error("Unexpected error opening property file", e);
            }
        } else {
            in = defaultRoot.getResourceAsStream(fileName);
        }

        if (in != null) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
            try {
                properties.load(bufferedInputStream);
                bufferedInputStream.close();
            } catch (IOException e) {
                LOGGER.error("Error initializing default properties", e);
            }
        }

        return properties;
    }


}
