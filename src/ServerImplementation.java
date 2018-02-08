import java.rmi.*;
import java.rmi.server.*;


public class ServerImplementation extends UnicastRemoteObject implements IServerImplementation {
    private Utility utility;
    private DataRepository data;
    
    protected ServerImplementation() throws RemoteException {
        super();
        utility = new Utility();
        data = DataRepository.create();
    }

    @Override
    public boolean join(String IP, int PORT) throws RemoteException {

        data.addToServer(IP,PORT);
        //add clientDetails to client Queue

        return true;
    }

    @Override
    public boolean leave(String IP, int PORT) throws RemoteException{
        data.removeFromServer(IP,PORT);
        return true;
    }

    @Override
    public boolean subscribe(String IP, int PORT, String article) throws RemoteException{
        data.subscribe(IP,PORT,article);
        return true;
    }

    @Override
    public boolean unsubscribe(String IP, int PORT, String article) throws RemoteException {
        return false;
    }

    @Override
    public boolean publish(String article, String IP, int PORT) throws RemoteException {
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
    public boolean ping() throws RemoteException {
        return false;
    }
}
