package grading.staff;

import java.net.ServerSocket;

/**
 * Utility to finding a free port
 * @author Antonio
 *
 */
public class FreePortUtility {

    private static Object lock = new Object();

    /**
     * Return a lock to be used to synchronize acquiring of free ports
     * @return
     */
    public static Object getLock() {
        return lock;
    }

    /**
     * Continuously check until we find a free server port.
     * MUST ACQUIRE AND SYNCHRONIZE ON LOCK FIRST TO GAUARANTEE SAFETY
     * @return
     */
    public static int getFreePort() {

        while (true) {
            try {
                final ServerSocket server = new ServerSocket(0);
                try {
                    return server.getLocalPort();
                } finally {
                    server.close();
                }
            } catch (Exception ignore) {
            }
        }

    }

}
