import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.*;
import java.util.HashMap;

public class Server {
    private HashMap<String, ClientDetails> clientMap;

    public static void main(String args[]) {
        try {

            ConfigManager configManager = ConfigManager.create();
            ServerImplementation stub = new ServerImplementation();
            Naming.rebind(configManager.getValue(ConfigManager.RMI_REGISTRY_ADDRESS), stub);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
