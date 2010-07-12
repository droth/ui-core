package li.droth.ceofhack.ui;

import li.droth.ceofhack.connector.Connector;
import li.droth.ceofhack.model.CommandInstance;
import li.droth.ceofhack.model.UI2CeofTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;

/**
 * <p/>
 *
 * @author danielroth
 */
public class ConsoleUI implements UIClass, CommandReadObserver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleUI.class);

    private UI2CeofTranslator translator;
    private boolean quit = false;
    private Connector connector;
    private Set<String> displayableCommands;

    public ConsoleUI() {
        translator = UI2CeofTranslator.getInstance();

    }

    @Override
    public void run() {
        ConsoleReader.getInstance().registerObserver(this);
        while (!quit) {

        }
        LOGGER.debug("Quiting");

    }

    @Override
    public void commandRead(String command) {
        //TODO serialize and send to socket
        if (command.startsWith("/")) {
            try {
                CommandInstance commandInstance = translator.buildCommandInstance(command);
                if (commandInstance != null) {
                    connector.sendCommand(commandInstance);
                    LOGGER.debug("Issued command {}", commandInstance);
                    if (displayCommand(commandInstance))
                        System.out.println("me>" + commandInstance.toString());
                }
            } catch (IOException e) {
                LOGGER.error("Could not send command {}, the error was {}", command, e);
            }
        } else {
            if (command.startsWith("quit")) {
                quit = true;
            }

        }
    }

    private boolean displayCommand(CommandInstance instance) {
        return displayableCommands.contains(instance.getDisplayName());
    }

    @Override
    public void notify(CommandInstance command) {
        LOGGER.debug("Received command {}", command);
        if (displayCommand(command))
            System.out.println("me<" + command.toString());
    }


    @Override
    public void init(Set<String> displayableCommands, Connector connectorParam) {
        this.displayableCommands = displayableCommands;
        this.connector = connectorParam;
    }
}
