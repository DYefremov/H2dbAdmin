package by.post.ui;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * Very simple implementation of verification that the application is already running.
 *
 * @author Dmitriy V.Yefremov
 */
public class AppRunChecker {
    // TODO To think about possible better method to implements this function in Windows and Linux
    private static final int PORT = 11112;
    private static ServerSocket socket;

    public boolean isRunning() {

        try {
            socket = new ServerSocket(PORT, 10, InetAddress.getLocalHost());
        } catch (IOException e) {
            return true;
        }

        return  socket == null && socket.isClosed();
    }
}
