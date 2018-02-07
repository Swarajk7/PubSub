import java.rmi.*;
import java.rmi.server.*;


public class ServerImplementation extends UnicastRemoteObject implements IServerImplementation {
    private Utility utility;
    protected ServerImplementation() throws RemoteException {
        super();
        utility = new Utility();
    }

    @Override
    public boolean join(String IP, int PORT) throws Exception {
        if(!utility.validateIP(IP)) throw new Exception("Invalid IP Address");
        String ip_port = utility.appendIPAndPort(IP,PORT);
        //check is same ip and port, already a client is joined and then throw exception
        ClientDetails clientDetails = new ClientDetails(IP, PORT);
        //add clientDetails to client Queue
        return true;
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
