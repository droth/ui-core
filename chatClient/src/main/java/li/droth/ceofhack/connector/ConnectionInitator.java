package li.droth.ceofhack.connector;

import java.io.IOException;

/**
 * <p/>
 *
 * @author danielroth
 */
public interface ConnectionInitator {
    public boolean init(Connector connector) throws IOException;
}
