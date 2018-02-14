import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.rmi.*;
import java.util.Scanner;

public class Client {
    private static ClientPingServerThread clientPingServerThread;
    private static ClientArticleReceiverThread clientReceiver;
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void main(String args[]) {
        //take port number as command line argument
        //System.out.println(args.length);
        if (args.length != 1) {
            System.out.println("Error.\nUsage java Client port_no");
            System.exit(1);
        }
        int port = -1;
        try {
            //check if port is integer
            port = Integer.parseInt(args[0]);
        } catch (Exception ex) {
            System.out.println("Invalid Port Number.\nUsage java Client port_no");
            System.exit(1);
        }
        try {
            DatagramSocket socket = new DatagramSocket(null);
            String IP = InetAddress.getLocalHost().getHostAddress();
            InetSocketAddress address = new InetSocketAddress(IP, port);
            socket.bind(address);
            System.out.println("Client IP:" + IP);
            //hardcoded server address
            ClientConfigManager clientConfigManager = ClientConfigManager.create();
            String serverEndPoint = "rmi://" + clientConfigManager.getValue(ClientConfigManager.RMI_REGISTRY_ADDRESS)
                    + ":" + clientConfigManager.getValue(ClientConfigManager.RMI_PORT_NUMBER) + "/" +
                    clientConfigManager.getValue(ClientConfigManager.RMI_BINDING_NAME);
            IServerImplementation stub = (IServerImplementation) Naming.lookup(serverEndPoint);

            String article;

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                try {
                    System.out.println("Choose an Option? \n1. JOIN\n" +
                            "2. LEAVE\n3. Subscribe\n4. UnSubscribe\n5. Publish\n");
                    int readValue = Integer.parseInt(reader.readLine());
                    switch (readValue) {
                        case 1:
                            //start ping thread
                            clientPingServerThread = new ClientPingServerThread(stub, IP, port);
                            //start the receiver thread to receive the incoming messages and run infinitely
                            clientReceiver = new ClientArticleReceiverThread(socket);
                            stub.join(IP, port);
                            System.out.println(ANSI_GREEN + "You have succesfully joined the server! " + ANSI_RESET);
                            break;
                        case 2:
                            //garbage collect the object, which will make Java Runtime to stop the thread. Again on rejoin
                            //create a new thread
                            clientPingServerThread = null;
                            clientReceiver = null;
                            stub.leave(IP, port);
                            System.out.println(ANSI_GREEN + "You have succesfully left the server! " + ANSI_RESET);
                            break;
                        case 3:
                            if(clientReceiver == null) {
                                System.out.println(ANSI_RED  + "Please JOIN the server first" + ANSI_RESET);
                                continue;
                            }
                            System.out.println("Enter the topic to subscribe ");
                            article = reader.readLine();
                            stub.subscribe(IP, port, article);
                            System.out.println(ANSI_GREEN + "You have succesfully subscribed " + ANSI_RESET);
                            break;
                        case 4:
                            if(clientReceiver == null) {
                                System.out.println(ANSI_RED  + "Please JOIN the server first" + ANSI_RESET);
                                continue;
                            }
                            System.out.println("Enter the topic to unsubscribe");
                            article = reader.readLine();
                            stub.unsubscribe(IP, port, article);
                            System.out.println(ANSI_GREEN + "You have succesfully unsubscribed " + ANSI_RESET);
                            break;
                        case 5:
                            if(clientReceiver == null) {
                                System.out.println(ANSI_RED + "Please JOIN the server first" + ANSI_RESET);
                                continue;
                            }
                            System.out.println("Enter the article to publish");
                            article = reader.readLine();
                            stub.publish(article, IP, port);
                            System.out.println(ANSI_GREEN + "You have succesfully published " + ANSI_RESET);
                            break;
                        default:
                            System.out.println(ANSI_RED  + "Invalid Input" + ANSI_RESET);
                            break;
                    }
                } catch (Exception ex) {
                    System.out.println(ANSI_RED + ex.getMessage() + ANSI_RESET);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
