import java.io.IOException;
import java.net.*;
/*
This is a thread which will keep running and it will listen to a socket at a given port.
We need to implement singleton socket at client also. Then we won't need to pass around socket object.
Singleton will work fine because socket is readonly.
 */
public class ClientArticleReceiverThread implements Runnable{
    private DatagramSocket socket;
    private byte[] buf = new byte[256];
    public ClientArticleReceiverThread(DatagramSocket socket) {
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
