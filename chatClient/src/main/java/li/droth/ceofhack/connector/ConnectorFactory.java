package li.droth.ceofhack.connector;

import li.droth.ceofhack.util.ClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p/>
 *
 * @author danielroth
 */
public class ConnectorFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectorFactory.class);

    public static Connector createConnector(String type) {
        try {
            Object obj = ClassLoader.loadClass("li.droth.ceofhack.connector", type);
            if (obj != null) {
                return (Connector) obj;
            }
        } catch (ClassCastException e) {
            LOGGER.error("Class {} can not be casted to Connector", type);
        }
        return null;
    }
}