import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.*;

public class Server {

    public static void main(String args[]) {
        try {

            InetAddress ip;
            String hostname;
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            System.out.println("Your current IP address : " + ip);
            System.out.println("Your current Hostname : " + hostname);

            ConfigManager configManager = ConfigManager.create();
            ServerImplementation stub = new ServerImplementation();
            Naming.rebind(configManager.getValue(ConfigManager.RMI_REGISTRY_ADDRESS), stub);

            //Register server
            int port = Integer.parseInt(configManager.getValue(ConfigManager.UDP_SERVER_PORT2));
            DatagramSocket socket = UDPSocket.createSocket(port);
            ISender sender = new Sender(socket);
            //register server message [“Register;RMI;IP;Port;BindingName;Port for RMI”]
            /*String registerMessage = "Register;RMI;"
                    + "127.0.0.1" + ";"
                    + "3000" + ";"
                    + "" + ";"
                    + port;

            sender.sendMessageToClient(configManager.getValue(ConfigManager.REGISTRY_SERVER_ADDRESS),
                    Integer.parseInt(configManager.getValue(ConfigManager.REGISTRY_SERVER_PORT)), registerMessage);
            */
            String getListmessage = "GetList;RMI;" + hostname + ";" + port;
            String listofservers = sender.getList(configManager.getValue(ConfigManager.REGISTRY_SERVER_ADDRESS),
                    Integer.parseInt(configManager.getValue(ConfigManager.REGISTRY_SERVER_PORT)), getListmessage);
            System.out.println(listofservers);

            String deRegistermessage = "Deregister;RMI;127.0.0.1;" + port;
            sender.sendMessageToClient(configManager.getValue(ConfigManager.REGISTRY_SERVER_ADDRESS),
                    Integer.parseInt(configManager.getValue(ConfigManager.REGISTRY_SERVER_PORT)), deRegistermessage);

            new RegistyServerHeartBeatThread(socket);

            int numer_of_publisher_threads = 2;
            //spawn threads to publish queue
            for (int i = 0; i < numer_of_publisher_threads; i++) {
                new PublishQueueListener("PublisherThread:" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
