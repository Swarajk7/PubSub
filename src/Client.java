import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.*;
import java.util.Scanner;

public class Client {

    public static void main(String args[]) {
        try {
            DatagramSocket socket = new DatagramSocket(4546);
            InetAddress address = InetAddress.getByName("localhost");

            //start the receiver thread to receive the incoming messages and run infinitely
            new ClientReceiver(socket);

            IServerImplementation stub = (IServerImplementation) Naming.lookup("rmi://localhost:3000/khada004" );
            System.out.println(stub.ping());

            while (true) {
                System.out.println("Enter Text to publish?");
                Scanner scanner = new Scanner(System.in);
                stub.publish(scanner.next(),address.getHostAddress(),socket.getPort());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
