import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

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
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4546);
            socket.send(packet);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}