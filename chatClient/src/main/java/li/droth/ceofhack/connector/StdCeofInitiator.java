package li.droth.ceofhack.connector;

import li.droth.ceofhack.model.UI2CeofTranslator;

import java.io.IOException;

/**
 * <p/>
 *
 * @author danielroth
 */
public class StdCeofInitiator implements ConnectionInitator {
    @Override
    public boolean init(Connector connector) throws IOException {
        connector.sendCommand(UI2CeofTranslator.getInstance().buildCommandInstance("/register 123456"));
        return true;
    }
}
