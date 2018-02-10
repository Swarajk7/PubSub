import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
/*
Class to deal with sending information to client given an IP and PORT.
Uses singleton socket at serverside.
After publish() method is invoked by any client this piece of code will be invoked.
 */
public class Sender implements ISender {
    private DatagramSocket socket;
    private boolean isSocketCreated = false;

    public Sender() throws IOException {
        this.isSocketCreated = true;
        ConfigManager configManager = ConfigManager.create();
        int port = Integer.parseInt(configManager.getValue(ConfigManager.UDP_SERVER_PORT));
        this.socket = UDPSocket.createSocket(port);
    }

    public Sender(DatagramSocket socket) {
        this.socket = socket;
    }

    @Override
    public void sendMessageToClient(String IP, int port, String msg) throws Exception {
        byte[] buf = msg.getBytes();
        try {
            InetAddress address = InetAddress.getByName(IP);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    @Override
    public String getList(String IP, int port, String msg) {
        byte[] buf = msg.getBytes();
        String ret = "";
        try {
            InetAddress address = InetAddress.getByName(IP);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);
            buf = new byte[1024];
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            ret = new String(packet.getData(), 0, packet.getLength());  //change DatagramPacket information to received
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }
}
