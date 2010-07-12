package li.droth.ceofhack.ui.gui;

import li.droth.ceofhack.connector.Connector;
import li.droth.ceofhack.model.CommandInstance;
import li.droth.ceofhack.model.UI2CeofTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: droth
 * Date: Jul 10, 2010
 * Time: 1:30:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommandInputField extends JTextField{
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandInputField.class);
    private static final int MAX_HISTORY_ENTRIES = 10;

    private FormatTextArea formatTextArea;
    private Connector connector;
    private List<String> commands;
    private int i = 0;


    public CommandInputField(FormatTextArea formatTextArea, Connector connector) {
        super();
        this.formatTextArea = formatTextArea;
        this.connector = connector;
        commands = new ArrayList<String>();
        init();
    }

    private void init() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    String text = getText();
                    addCommand(text);
                    formatTextArea.setChatText("from me>\t" + text );
                    CommandInstance commandInstance = UI2CeofTranslator.getInstance().buildCommandInstance(text);
                    setText("");
                    if (commandInstance != null) {
                        try {
                            connector.sendCommand(commandInstance);
                        } catch (IOException e) {
                            LOGGER.error("Error while sending command:", e);
                        }
                    }
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                    setText(getCommand(true));
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                    setText(getCommand(false));
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    private void addCommand(String text) {
        i = -1;
        commands.add(0, text);
        while (commands.size() > MAX_HISTORY_ENTRIES) {
            commands.remove(commands.size()-1);
        }

    }

    private String getCommand(boolean up) {
        String command;
        if (up) {
            i++;
            if (i > commands.size()) {
                i = commands.size();
                return "";
            }
        } else {
            i--;
            if (i < -1) {
                i = -1;
                return "";
            }
        }
        if (i < 0 || i >= commands.size())
            return "";
        command =  commands.get(i);
        return command;
    }

}
