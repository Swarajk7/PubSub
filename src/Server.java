import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.*;

public class Server {
    public static void main(String args[]){
        try {

            ConfigManager configManager = ConfigManager.create();
            ServerImplementation stub = new ServerImplementation();
            Naming.rebind(configManager.getValue(ConfigManager.RMI_REGISTRY_ADDRESS), stub);

            ISender sender = new Sender();

            Thread.sleep(3000);
            for (int i = 0; i < 10; i++) {
                sender.sendMessageToClient("127.0.0.1", 4545, "Hello" + i);
                Thread.sleep(1000);
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
