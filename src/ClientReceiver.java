import java.io.IOException;
import java.net.*;

public class ClientReceiver implements Runnable{
    private DatagramSocket socket;
    private byte[] buf = new byte[256];
    public ClientReceiver(DatagramSocket socket) {
        this.socket = socket;
        new Thread(this, "ClientReceiver").start();
    }
    @Override
    public void run() {
        System.out.println("Client Receiver Running");
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length); //create a new packet to receive information
                socket.receive(packet); //receive packet
                String received = new String(packet.getData(), 0, packet.getLength());  //change DatagramPacket information to received
                System.out.println("Received String: " + received);
                Thread.sleep(1000);
            } catch (IOException ex) {
                ex.printStackTrace();
                //System.out.println("Nothing!!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
