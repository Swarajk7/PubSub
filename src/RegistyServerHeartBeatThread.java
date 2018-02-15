import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class RegistyServerHeartBeatThread implements Runnable {
    private ISender sender;
    private DatagramSocket socket;
    private String registryServerAddress;
    private int registryServerPort;
    private boolean debug_mode;

    public RegistyServerHeartBeatThread() throws IOException {
        ConfigManager configManager = ConfigManager.create();
        this.socket = UDPSocket.createSocket(configManager.getIntegerValue(ConfigManager.RMI_PORT_NUMBER));
        this.sender = new Sender(socket);
        this.registryServerAddress = configManager.getValue(ConfigManager.REGISTRY_SERVER_ADDRESS);
        this.registryServerPort = Integer.parseInt(configManager.getValue(ConfigManager.REGISTRY_SERVER_PORT));
        debug_mode = Boolean.parseBoolean(configManager.getValue(ConfigManager.IS_DEBUG));
        new Thread(this, "RegistyServerHeartBeatThread").start();
    }

    @Override
    public void run() {
        //[“Register;RMI;IP;Port;BindingName;Port for RMI”]
        byte[] buf = new byte[1024];
        System.out.println("Heartbeat Thread Started");
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                int portno = packet.getPort();
                String received = new String(packet.getData(), 0, packet.getLength());  //change DatagramPacket information to received
                //System.out.println("Running: " + received);
                //if it fails what to do think?
                //send same string message to server
                sender.sendMessageToClient(this.registryServerAddress, portno, received);
            } catch (IOException e) {
                if (debug_mode) System.out.println(e.getMessage());
            } catch (Exception e) {
                if (debug_mode) System.out.println(e.getMessage());
            }
        }
    }
}
