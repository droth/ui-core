package li.droth.ceofhack.connector;

import li.droth.ceofhack.model.CommandInstance;

/**
 * <p/>
 *
 * @author danielroth
 */
@SuppressWarnings({"UnusedDeclaration"})
public class LoopbackConnector implements Connector {
    private Notifyable notifyable;

    public LoopbackConnector() {

    }

    @Override
    public void registerNotifier(Notifyable notifyable) {
        this.notifyable = notifyable;
    }

    @Override
    public void setInitiator(ConnectionInitator connectionInitator) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean sendCommand(CommandInstance command) {
        notifyable.notify(command);
        return true;
    }

    @Override
    public void stop() {
    }

    @Override
    public void start() {
    }
}
