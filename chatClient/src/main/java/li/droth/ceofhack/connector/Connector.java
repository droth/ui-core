package li.droth.ceofhack.connector;

import li.droth.ceofhack.model.CommandInstance;

import java.io.IOException;

/**
 * <p/>
 *
 * @author danielroth
 */
public interface Connector {
    public void registerNotifier(Notifyable notifyable);

    public void setInitiator(ConnectionInitator connectionInitator);

    public boolean sendCommand(CommandInstance command) throws IOException;

    public void stop();

    public void start() throws IOException;
}
