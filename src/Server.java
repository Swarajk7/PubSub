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

            String rmi_end_point = "rmi://" + ip + ":3267/khada004";
            ServerImplementation stub = new ServerImplementation();
            Naming.rebind(rmi_end_point, stub);

            System.out.println("Your current RMI address : " + rmi_end_point);

            //Register server. Start a socket with different port to recieve and send packets from registry server
            int port = Integer.parseInt(configManager.getValue(ConfigManager.UDP_SERVER_PORT2));
            DatagramSocket socket = UDPSocket.createSocket(port);
            socket.setSoTimeout(3000);
            ISender sender = new Sender(socket);

            //create a new thread to start listening to incoming messages
            new RegistyServerHeartBeatThread(socket);

            int numer_of_publisher_threads = 2;
            //spawn threads to publish queue
            for (int i = 0; i < numer_of_publisher_threads; i++) {
                new PublishQueueListener("PublisherThread:" + i);
            }

            //register server message [“Register;RMI;IP;Port;BindingName;Port for RMI”]
            String registerMessage = "Register;RMI;"
                    + ip + ";"
                    + "3267" + ";"
                    + "" + ";"
                    + port;
            //System.out.println(registerMessage);
            sender.sendMessageToClient(configManager.getValue(ConfigManager.REGISTRY_SERVER_ADDRESS),
                    Integer.parseInt(configManager.getValue(ConfigManager.REGISTRY_SERVER_PORT)), registerMessage);

            //getList and print for now, twist as per need as time comes
            String getListmessage = "GetList;RMI;" + ip + ";" + 3267;
            System.out.println(getListmessage);
            Thread.sleep(1000);
            String listofservers = sender.getList(configManager.getValue(ConfigManager.REGISTRY_SERVER_ADDRESS),
                    Integer.parseInt(configManager.getValue(ConfigManager.REGISTRY_SERVER_PORT)), getListmessage);
            System.out.println(listofservers);


            //code for deregistration
            /*String deRegistermessage = "Deregister;RMI;"+ip+";" + 3000;
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
