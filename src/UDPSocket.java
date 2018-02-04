import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPSocket {
    private DatagramSocket socket;
    private static UDPSocket udpSocket = null;
    private UDPSocket(int port) throws SocketException {
        this.socket = new DatagramSocket(port);
    }
    public static DatagramSocket createSocket(int port) throws SocketException {
        //if already created it will ignore the port. Need to redesign
        if(udpSocket == null) {
            udpSocket = new UDPSocket(port);
        }
        return udpSocket.socket;
    }
}
