package li.droth.ceofhack;

import li.droth.ceofhack.connector.ConnectionInitator;
import li.droth.ceofhack.connector.Connector;
import li.droth.ceofhack.connector.ConnectorFactory;
import li.droth.ceofhack.ui.ConsoleUI;
import li.droth.ceofhack.ui.UIClass;
import li.droth.ceofhack.ui.gui.ChatFrame;
import li.droth.ceofhack.util.ClassLoader;
import li.droth.ceofhack.util.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * <p/>
 *
 * @author danielroth
 */
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static boolean cliMode;
    private static Properties properties;
    private static Connector connector;
    private static Set<String> displayableCommands;

    static {
        //default init
        properties = PropertiesReader.readProperties(Main.class, "/app.properties");
    }

    public static void main(String[] args) {
        System.out.println("Starting chatclient");
        try {
            analyzeArgs(args);
            if (properties.get("command.filter") != null) {
                displayableCommands = new HashSet<String>();
                Collections.addAll(displayableCommands, properties.get("command.filter").toString().split(" "));
            }
            initConnector();
            Object uiClassName = properties.get("uiClass");

            String defaultUiPkg = Main.class.getPackage().getName() + ".ui";
            UIClass uiClass = null;
            if (cliMode) {
                if (uiClassName != null) {
                    uiClass = (UIClass) ClassLoader.loadClass(defaultUiPkg, uiClassName.toString());
                }
                if (uiClass == null) {
                    uiClass = new ConsoleUI();
                }
            } else {
                if (uiClassName != null) {
                    uiClass = (UIClass) ClassLoader.loadClass(defaultUiPkg + ".gui", uiClassName.toString());

                }
                if (uiClass == null) {
                    uiClass = new ChatFrame();
                }
            }
            Thread ui;
            uiClass.init(displayableCommands, connector);
            connector.registerNotifier(uiClass);
            connector.start();
            ui = new Thread(uiClass, "UI");
            ui.setDaemon(false);
            ui.start();

            ui.join();
        } catch (Throwable t) {
            LOGGER.error("Unexpected Error: {}", t);
        }
        System.exit(0);
    }

    private static void initConnector() throws IllegalDefinedConnector {
        Object connectorType = properties.get("connector.name");
        if (connectorType == null)
            throw new IllegalDefinedConnector("Connector type is not defined");
        connector = ConnectorFactory.createConnector(connectorType.toString());
        Object init = properties.get("connector.init");
        try {
            ConnectionInitator connectionInitator = (ConnectionInitator)
                    ClassLoader.loadClass(connector.getClass().getPackage().getName(), init.toString());
            connector.setInitiator(connectionInitator);
        } catch (Exception e) {
            throw new IllegalDefinedConnector(e);
        }
    }

    private static void initDefault() {

    }

    private static boolean analyzeArgs(String[] args) throws IOException {
        initDefault();
        if (args != null) {
            FileInputStream fInStream = null;
            try {
                for (int i = 0; i < args.length; i++) {
                    String arg = args[i];
                    switch (CLI_CMD.getCMD(arg)) {
                        case PROPERTY_FILE:
                            if (i++ < args.length) {
                                String fileName = args[i];
                                properties = PropertiesReader.readProperties(Main.class, fileName);
                            }
                            break;
                        default:
                            usage();
                            return false;
                    }
                }
            } finally {
                if (fInStream != null) {
                    fInStream.close();
                }
            }
        }
        return initProperties(properties);

    }

    private static boolean initProperties(Properties properties) {
        cliMode = Boolean.valueOf(properties.getProperty("cliMode"));
        return true;
    }

    private static void usage() {
        System.out.println("Usage of Chatclient:");
        System.out.println("\t-f <property-file-path>");
    }

    private static enum CLI_CMD {
        PROPERTY_FILE("-f");

        private final String name;

        private CLI_CMD(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public static CLI_CMD getCMD(String cmd) {
            if (cmd.equalsIgnoreCase("-f"))
                return PROPERTY_FILE;
            else
                return null;
        }

    }

    private static class IllegalDefinedConnector extends Exception {
        public IllegalDefinedConnector(String s) {
            super(s);
        }

        public IllegalDefinedConnector(Exception e) {
            super(e);
        }
    }
}
