package li.droth.ceofhack.ui;

import li.droth.ceofhack.connector.Connector;
import li.droth.ceofhack.connector.Notifyable;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: droth
 * Date: Jul 10, 2010
 * Time: 2:39:07 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UIClass extends Notifyable, Runnable{
    public void init(Set<String> displayableCommands, Connector connectorParam);
}
