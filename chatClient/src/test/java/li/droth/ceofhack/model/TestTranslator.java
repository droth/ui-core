package li.droth.ceofhack.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * <p/>
 *
 * @author danielroth
 */
public class TestTranslator {

    @Test
    public void buildCommandInstances() {
        String command = "1100MyID123456";
        CommandInstance commandInstance = UI2CeofTranslator.getInstance().buildCommandInstance(command);
        assertEquals("The command should be equal", "/ack MyID123456", commandInstance.toString());

        command = "/ack MyID123456";
        commandInstance = UI2CeofTranslator.getInstance().buildCommandInstance(command);
        assertEquals("The command should be equal", "/ack MyID123456", commandInstance.toString());

        command = "1100MyID123457111";
        commandInstance = UI2CeofTranslator.getInstance().buildCommandInstance(command);
        assertEquals("The command should be equal", "/ack MyID123457", commandInstance.toString());
        String test = "/peerAdd Nico tcp:129.132.102.115:4242 57A6A244041B5DD9D75CAE8657A36865F176280E";
        test = "/peerSend Nico hallo";
    }
}
