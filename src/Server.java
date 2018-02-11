import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.*;

public class Server {

    public static void main(String args[]) {
        try {

            String ip;
            String hostname;
            ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Your current IP address : " + ip);

            ConfigManager configManager = ConfigManager.create();
            String rmi_end_point = "rmi://"+ip+":3267/khada004";
            ServerImplementation stub = new ServerImplementation();
            System.out.println("Your current RMI address : " + rmi_end_point);
            Naming.rebind(rmi_end_point, stub);

            //Register server
            int port = Integer.parseInt(configManager.getValue(ConfigManager.UDP_SERVER_PORT2));
            DatagramSocket socket = UDPSocket.createSocket(port);
            ISender sender = new Sender(socket);
            System.out.println(socket.getLocalAddress().getHostAddress());
            //register server message [“Register;RMI;IP;Port;BindingName;Port for RMI”]
            String registerMessage = "Register;RMI;"
                    + ip + ";"
                    + "3267" + ";"
                    + "" + ";"
                    + port;
            System.out.println(registerMessage);
            sender.sendMessageToClient(configManager.getValue(ConfigManager.REGISTRY_SERVER_ADDRESS),
                    Integer.parseInt(configManager.getValue(ConfigManager.REGISTRY_SERVER_PORT)), registerMessage);
            String getListmessage = "GetList;RMI;" + ip + ";" + 3267;
            System.out.println(getListmessage);
            Thread.sleep(1000);
            String listofservers = sender.getList(configManager.getValue(ConfigManager.REGISTRY_SERVER_ADDRESS),
                    Integer.parseInt(configManager.getValue(ConfigManager.REGISTRY_SERVER_PORT)), getListmessage);
            System.out.println(listofservers);

            new RegistyServerHeartBeatThread(socket);

            /*String deRegistermessage = "Deregister;RMI;"+ip+";" + 3000;
            sender.sendMessageToClient(configManager.getValue(ConfigManager.REGISTRY_SERVER_ADDRESS),
                    Integer.parseInt(configManager.getValue(ConfigManager.REGISTRY_SERVER_PORT)), deRegistermessage);
            System.out.println(deRegistermessage);

            listofservers = sender.getList(configManager.getValue(ConfigManager.REGISTRY_SERVER_ADDRESS),
                            Integer.parseInt(configManager.getValue(ConfigManager.REGISTRY_SERVER_PORT)), getListmessage);
            System.out.println(listofservers);
            */
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
