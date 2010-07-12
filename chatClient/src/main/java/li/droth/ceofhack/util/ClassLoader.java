package li.droth.ceofhack.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: droth
 * Date: Jul 10, 2010
 * Time: 2:56:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClassLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassLoader.class);

    public static Object loadClass(String defaultPackage, String className) {
        Object loadedClass = loadClass(className);
        if (loadedClass == null) {
            loadedClass = loadClass(defaultPackage + "." + className);
        }
        return loadedClass;
    }

    public static Object loadClass(String name) {
        try {
            Object loadedClass = Class.forName(name).newInstance();
            LOGGER.debug("Loaded class {}", name);
            return loadedClass;
        } catch (InstantiationException e) {
            LOGGER.error("Could not create instance for class {}", name);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            LOGGER.error("Could not access class {}", name);
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Could not find class {}", name);
        }
        return null;
    }
}
