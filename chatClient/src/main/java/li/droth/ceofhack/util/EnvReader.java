package li.droth.ceofhack.util;

import java.io.File;
import java.util.Map;

/**
 * <p/>
 *
 * @author danielroth
 */
public class EnvReader {
    private static Map<String, String> env;
    private static String eofHome;
    private static String eofUiSocket;

    public static String getEnv(String name) {
        ensureEnv();
        return env.get(name);
    }

    public static synchronized String getEofHome() {
        ensureEnv();
        if (eofHome != null)
            return eofHome;
        eofHome = env.get("EOF_HOME");
        if (eofHome != null)
            return eofHome;
        eofHome = env.get("HOME");
        if (eofHome != null)
            return eofHome;
        eofHome = new File("test").getParent();
        return eofHome;

    }

    public static synchronized String getEofUISocket() {
        ensureEnv();
        if (eofUiSocket != null)
            return eofUiSocket;
        eofUiSocket = env.get("EOF_UI_SOCKET");
        if (eofUiSocket != null)
            return eofUiSocket;
        eofUiSocket = getEofHome() + "/.ceof/ui/socket";
        return eofUiSocket;
    }


    private static void ensureEnv() {
        if (env == null)
            env = System.getenv();

    }
}
