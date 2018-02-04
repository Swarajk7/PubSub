import java.rmi.*;
import java.rmi.server.*;


public class ServerImplementation extends UnicastRemoteObject implements IServer {

    protected ServerImplementation() throws RemoteException {
        super();
    }

    @Override
    public boolean join(String IP, int PORT) {
        return false;
    }

    @Override
    public boolean leave(String IP, int PORT) {
        return false;
    }

    @Override
    public boolean subscribe(String IP, int PORT, String article) {
        return false;
    }

    @Override
    public boolean unsubscribe(String IP, int PORT, String article)  {
        return false;
    }

    @Override
    public boolean publish(String article, String IP, int PORT)  {
        System.out.println(IP);
        try {
            ConfigManager configManager = ConfigManager.create();
            ISender sender = new Sender(UDPSocket.createSocket(Integer.parseInt(configManager.getValue(ConfigManager.UDP_SERVER_PORT))));
            sender.sendMessageToClient(IP, PORT, "Hello" + article);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean ping()  {
        return false;
    }
}
