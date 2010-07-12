package li.droth.ceofhack.connector;

import li.droth.ceofhack.model.CommandInstance;

/**
 * <p/>
 *
 * @author danielroth
 */
public interface Notifyable {
    public void notify(CommandInstance command);
        
}
