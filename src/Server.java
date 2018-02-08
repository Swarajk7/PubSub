import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.*;
import java.util.HashMap;

public class Server {

    public static void main(String args[]) {
        try {

            ConfigManager configManager = ConfigManager.create();
            ServerImplementation stub = new ServerImplementation();
            Naming.rebind(configManager.getValue(ConfigManager.RMI_REGISTRY_ADDRESS), stub);

            int numer_of_publisher_threads = 1;
            //spawn threads to publish queue
            for (int i = 0; i < numer_of_publisher_threads; i++) {
                new PublishQueueListener("PublisherThread:" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
