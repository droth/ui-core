package li.droth.ceofhack.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 *
 * @author danielroth
 */
public class ConsoleReader implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleReader.class);
    private List<CommandReadObserver> commandReadObservers;
    private boolean stop = true;
    private Thread th;
    private static ConsoleReader instance;


    private ConsoleReader() {
        super();
        commandReadObservers = new ArrayList<CommandReadObserver>();
    }

    public static ConsoleReader getInstance() {
        if (instance == null) {
            instance = new ConsoleReader();
        }
        return instance;
    }



    public synchronized boolean registerObserver(CommandReadObserver commandReadObserver) {
        if (stop) {
            start();
        }
        return commandReadObservers.add(commandReadObserver);
    }

    public synchronized boolean deRegisterObserver(CommandReadObserver commandReadObserver) {
        if (!stop && commandReadObservers.size() == 1)
            stop();
        return commandReadObservers.remove(commandReadObserver);
    }

    private synchronized void stop() {
        stop = true;
        th.interrupt();
        th = null;
    }

    private synchronized void start() {
        stop = false;
        if (th == null) {
            th = new Thread(this, "ConsoleReader");

            th.setDaemon(false);
            th.start();
        }
    }

    private synchronized void fireCommandRead(String command) {
        if (stop)
            return;
        command = command.trim();
        for (CommandReadObserver commandReadObserver:commandReadObservers){
            commandReadObserver.commandRead(command);
        }
    }


    @Override
    public void run() {
        InputStream in = System.in;
        StringBuilder builder = new StringBuilder();
        while (!stop) {
            try {
                int available = in.available();
                if (available > 0) {
                    //TODO implement usual commandline behavior eg arrow up/down
                    byte[] bytes = new byte[available];
                    int readBytes = in.read(bytes);
                    LOGGER.debug("Read {} bytes of data {}", readBytes, new String(bytes));
                    String read = new String(bytes);
                    int newLinePosition;
                    if ((newLinePosition = read.indexOf("\n")) != -1) {
                        builder.append(read.substring(0, newLinePosition));
                        read = read.substring(newLinePosition);
                        fireCommandRead(builder.toString());
                        builder = new StringBuilder();
                    }
                    builder.append(read);
                }
            } catch (IOException e) {
                LOGGER.error("Error reading Conole", e);
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
                //ignore as we shall not sleep anymore
            }


        }
    }
}
