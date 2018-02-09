import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.*;
import java.util.Scanner;

public class Client {

    private static final boolean debugmode = true;

    public static void main(String args[]) {
        //take port number as command line argument
        System.out.println(args.length);
        if(args.length != 1) {
            System.out.println("Error.\nUsage java Client port_no");
            System.exit(1);
        }
        int port = -1;
        try {
            //check if port is integer
            port = Integer.parseInt(args[0]);
        }
        catch (Exception ex) {
            System.out.println("Invalid Port Number.\nUsage java Client port_no");
            System.exit(1);
        }
        try {
            DatagramSocket socket = new DatagramSocket(port);
            InetAddress address = InetAddress.getByName("localhost");
            String IP = address.getHostAddress();

            //start the receiver thread to receive the incoming messages and run infinitely
            new ClientReceiver(socket);

            //hardcoded server address
            IServerImplementation stub = (IServerImplementation) Naming.lookup("rmi://localhost:3000/khada004");

            boolean breakfromloop = false;
            String article;

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (!breakfromloop) {
                try {
                    System.out.println("Choose an Option? \n1. JOIN\n" +
                            "2. LEAVE\n3. Subscribe\n4. UnSubscribe\n5. Publish\n6. Sleep");
                    int readValue = Integer.parseInt(reader.readLine());
                    switch (readValue) {
                        case 1:
                            stub.join(IP, port);
                            break;
                        case 2:
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
                    if(debugmode) break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
