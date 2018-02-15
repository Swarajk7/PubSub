import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.*;

public class Server {

    public static void main(String args[]) {
        try {
            String ip;
            ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Your current IP address : " + ip);

            ConfigManager configManager = ConfigManager.create();
            Utility utility = new Utility();
            String rmi_end_point = utility.createRMIConnectionString(ip,
                    configManager.getIntegerValue(ConfigManager.RMI_PORT_NUMBER),
                    configManager.getValue(ConfigManager.RMI_BINDING_NAME));

            ServerImplementation stub = new ServerImplementation();
            Naming.rebind(rmi_end_point, stub);

            System.out.println("Your current RMI address : " + rmi_end_point);

            //Register server. Start a socket with different port to recieve and send packets from registry server
            int port = Integer.parseInt(configManager.getValue(ConfigManager.UDP_SERVER_PORT2));
            DatagramSocket socket = UDPSocket.createSocket(port);

            //if socket timeout is defined in config, then set socket timeout
            if(configManager.getIntegerValue(ConfigManager.SOCKET_TIMEOUT) !=-1) {
                socket.setSoTimeout(configManager.getIntegerValue(ConfigManager.SOCKET_TIMEOUT));
            }

            ISender sender = new Sender(socket);

            int numer_of_publisher_threads = configManager.getIntegerValue(ConfigManager.NUMBER_OF_PUBLISH_THREADS);
            //spawn threads to publish queue
            for (int i = 0; i < numer_of_publisher_threads; i++) {
                //pass name of the thread and assign a queue number to poll
                new PublishQueueListener("PublisherThread:" + i, i);
            }

            //register server message [“Register;RMI;IP;Port;BindingName;Port for RMI”]
            String registerMessage = utility.createUDPMessageForRegister(ip,configManager.getIntegerValue(ConfigManager.RMI_PORT_NUMBER),
                    configManager.getValue(ConfigManager.RMI_BINDING_NAME),port);
            //System.out.println(registerMessage);
            sender.sendMessageToClient(configManager.getValue(ConfigManager.REGISTRY_SERVER_ADDRESS),
                    Integer.parseInt(configManager.getValue(ConfigManager.REGISTRY_SERVER_PORT)), registerMessage);

            //getList and print for now, twist as per need as time comes
            String getListmessage = utility.createUDPMessageForGetList(ip,configManager.getIntegerValue(ConfigManager.RMI_PORT_NUMBER));
            System.out.println("GetListMessage: " + getListmessage);
            String listofservers = sender.getList(configManager.getValue(ConfigManager.REGISTRY_SERVER_ADDRESS),
                    Integer.parseInt(configManager.getValue(ConfigManager.REGISTRY_SERVER_PORT)), getListmessage);
            System.out.println("List of Servers:" + listofservers);

            //create a new thread to start listening to incoming messages
            new RegistyServerHeartBeatThread(socket);

            /*
            String ip_port = ip + ";" + 3267;
            if (!listofservers.contains(ip_port)) {
                System.out.println("Server is not registered successfully. Please check port and ip.");
                System.exit(1);
            }
            */
            //code for deregistration
            /*
            String deRegistermessage = utility.createUDPMessageForDeRegister(ip,configManager.getIntegerValue(ConfigManager.RMI_PORT_NUMBER));
            sender.sendMessageToClient(configManager.getValue(ConfigManager.REGISTRY_SERVER_ADDRESS),
                    Integer.parseInt(configManager.getValue(ConfigManager.REGISTRY_SERVER_PORT)), deRegistermessage);
            System.out.println(deRegistermessage);

            listofservers = sender.getList(configManager.getValue(ConfigManager.REGISTRY_SERVER_ADDRESS),
                            Integer.parseInt(configManager.getValue(ConfigManager.REGISTRY_SERVER_PORT)), getListmessage);
            System.out.println(listofservers);
            */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
