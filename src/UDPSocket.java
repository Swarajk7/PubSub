import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Dictionary;
import java.util.HashMap;

public class UDPSocket {
    private DatagramSocket socket;

    private static HashMap<Integer, DatagramSocket> udpSockets;

    private UDPSocket(int port) throws SocketException {
        this.socket = new DatagramSocket(port);
    }
    public static DatagramSocket createSocket(int port) throws SocketException {
        //if already created it will ignore the port. Need to redesign

        if (udpSockets.get(port) == null) {
            udpSockets.put(port, new DatagramSocket(port));
        }

        return udpSockets.get(port);
    }
}
