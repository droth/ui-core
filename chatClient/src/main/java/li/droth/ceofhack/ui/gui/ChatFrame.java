package li.droth.ceofhack.ui.gui;

import li.droth.ceofhack.connector.Connector;
import li.droth.ceofhack.connector.Notifyable;
import li.droth.ceofhack.model.CommandInstance;
import li.droth.ceofhack.model.UI2CeofTranslator;
import li.droth.ceofhack.ui.UIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: droth
 * Date: Jul 10, 2010
 * Time: 11:40:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class ChatFrame extends JFrame implements UIClass {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatFrame.class);
    private UI2CeofTranslator translator;
    private Connector connector;
    private Set<String> displayableCommands;
    private CommandInputField inputField;
    private FormatTextArea chatHistory;

    public ChatFrame() {
        super("Chatframe");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        translator = UI2CeofTranslator.getInstance();
        setLayout(new BorderLayout());
        chatHistory = new FormatTextArea();
        chatHistory.setEditable(false);
        chatHistory.setSize(800, 550);

        inputField = new CommandInputField(chatHistory, connector);
        getContentPane().add(inputField, BorderLayout.SOUTH);
        getContentPane().add(chatHistory, BorderLayout.CENTER);
        setSize(800, 600);
        setVisible(true);
    }

    @Override
    public void notify(CommandInstance command) {
        chatHistory.setChatText("to me>\t" + command.toString());
    }

    @Override
    public void run() {
        while(true);
    }

    @Override
    public void init(Set<String> displayableCommands, Connector connectorParam) {
        this.displayableCommands = displayableCommands;
        this.connector = connectorParam;
    }
}
