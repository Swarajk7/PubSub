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

    public RegistyServerHeartBeatThread(DatagramSocket socket) throws IOException {
        this.sender = new Sender(socket);
        this.socket = socket;
        ConfigManager configManager = ConfigManager.create();
        this.registryServerAddress = configManager.getValue(ConfigManager.REGISTRY_SERVER_ADDRESS);
        this.registryServerPort = Integer.parseInt(configManager.getValue(ConfigManager.REGISTRY_SERVER_PORT));
        new Thread(this, "RegistyServerHeartBeatThread").start();
    }

    @Override
    public void run() {
        //[“Register;RMI;IP;Port;BindingName;Port for RMI”]
        byte[] buf = new byte[1024];
        System.out.println("HeartbeatThreadRunning");
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());  //change DatagramPacket information to received
                System.out.println("Running: "+received);
                //if it fails what to do think?
                //send same string message to server
                sender.sendMessageToClient(this.registryServerAddress, this.registryServerPort, received);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
