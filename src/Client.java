import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.rmi.*;
import java.util.Scanner;

public class Client {

    private static final boolean debugmode = true;
    private static ClientPingServerThread clientPingServerThread;
    private static ClientArticleReceiverThread clientReceiver;

    public static void main(String args[]) {
        //take port number as command line argument
        System.out.println(args.length);
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
            InetSocketAddress address = new InetSocketAddress(IP,port);
            socket.bind(address);
            System.out.println("Client IP:" + IP);
            //hardcoded server address
            IServerImplementation stub = (IServerImplementation) Naming.lookup("rmi://10.0.0.210:3267/khada004");

            String article;

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                try {
                    System.out.println("Choose an Option? \n1. JOIN\n" +
                            "2. LEAVE\n3. Subscribe\n4. UnSubscribe\n5. Publish\n6. Sleep");
                    int readValue = Integer.parseInt(reader.readLine());
                    switch (readValue) {
                        case 1:
                            //start ping thread
                            clientPingServerThread = new ClientPingServerThread(stub, IP, port);
                            //start the receiver thread to receive the incoming messages and run infinitely
                            clientReceiver = new ClientArticleReceiverThread(socket);
                            stub.join(IP, port);
                            break;
                        case 2:
                            //garbage collect the object, which will make Java Runtime to stop the thread. Again on rejoin
                            //create a new thread
                            clientPingServerThread = null;
                            clientReceiver = null;
                            stub.leave(IP, port);
                            break;
                        case 3:
                            System.out.println("Enter Subscribe Article?");
                            article = reader.readLine();
                            stub.subscribe(IP, port, article);
                            break;
                        case 4:
                            System.out.println("Enter UnSubscribe Article?");
                            article = reader.readLine();
                            stub.unsubscribe(IP, port, article);
                            break;
                        case 5:
                            System.out.println("Enter Publish Article?");
                            article = reader.readLine();
                            stub.publish(article, IP, port);
                            break;
                        case 6:
                            int sleeptime = Integer.parseInt(reader.readLine());
                            Thread.sleep(sleeptime);
                            break;
                        default:
                            System.out.println("Invalid Input");
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    //if(debugmode) break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
