import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.*;

public class Client {

    public static void main(String args[]) {
        try {
            DatagramSocket socket = new DatagramSocket(4546);
            InetAddress address = InetAddress.getByName("localhost");

            //start the receiver thread to receive the incoming messages and run infinitely
            new ClientReceiver(socket);

            IServer stub = (IServer) Naming.lookup("rmi://localhost:3000/khada004" );
            System.out.println(stub.ping());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
