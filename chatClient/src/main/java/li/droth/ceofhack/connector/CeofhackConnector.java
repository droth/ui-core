package li.droth.ceofhack.connector;

import com.google.code.juds.UnixDomainSocketClient;
import li.droth.ceofhack.model.CommandInstance;
import li.droth.ceofhack.model.DataTypeInstance;
import li.droth.ceofhack.model.UI2CeofTranslator;
import li.droth.ceofhack.util.EnvReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p/>
 *
 * @author danielroth
 */
public class CeofhackConnector implements Connector, Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(CeofhackConnector.class);

    private Notifyable notifyable;
    private InputStream inputStream;
    private OutputStream outputStream;
    private boolean started = false;
    private Thread connector;
    private ConnectionInitator initator;

    public CeofhackConnector() {
        String socketName = EnvReader.getEofUISocket();
        if (socketName == null)
            throw new IllegalArgumentException("unixsocket is not specified");
        UnixDomainSocketClient socket;
        try {
            socket = new UnixDomainSocketClient(socketName, 1);
        } catch (IOException e) {
            throw new IllegalArgumentException("unixsocket is not allowed", e);
        }
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();


    }

    @Override
    public void registerNotifier(Notifyable notifyable) {
        this.notifyable = notifyable;
    }

    @Override
    public void setInitiator(ConnectionInitator connectionInitator) {
        initator = connectionInitator;
    }

    @Override
    public boolean sendCommand(CommandInstance command) throws IOException {
        StringBuilder cmd = new StringBuilder();
        cmd.append(command.getSocketName());
        for (DataTypeInstance dataTypeInstance : command.getOrderedContents()) {
            cmd.append(dataTypeInstance.getContent());
        }

        try {
            LOGGER.debug("Writing:" + cmd.toString());
            outputStream.write(cmd.toString().getBytes("UTF-8"));
        } catch (Throwable t) {
            LOGGER.debug("Writing:" + cmd.toString());
            outputStream.write(cmd.toString().getBytes("UTF-8"));
        }

        return true;
    }

    @Override
    public void stop() {
        started = false;
        if (connector != null)
            connector.interrupt();
    }


    @Override
    public void start() throws IOException {
        if (started)
            return;
        connector = new Thread(this, "ceofhackconnector");
        connector.setDaemon(false);
        started = true;
        connector.start();

        initator.init(this);
    }

    @Override
    public void run() {
        StringBuilder commandString = new StringBuilder();
        while (started) {
            try {
                byte[] bytes = new byte[1024];
                inputStream.read(bytes);
                String cmd = new String(bytes, "UTF-8");
                LOGGER.debug("Read '" + cmd + "' from socket");
                commandString.append(cmd);
                CommandInstance commandInstance = UI2CeofTranslator.getInstance().buildCommandInstanceFromCeof(cmd);
                if (commandInstance == null) {
                    commandInstance = UI2CeofTranslator.getInstance().buildCommandInstanceFromCeof(commandString.toString());
                }

                if (commandInstance != null) {
                    if (notifyable != null)
                        notifyable.notify(commandInstance);
                    commandString = new StringBuilder();
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    //ignore  vm does not want us to sleep
                }
            } catch (IOException e) {
                LOGGER.error("Unexpected error reading from socket ", e);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ignored) {
                }

            }
        }
    }
}
