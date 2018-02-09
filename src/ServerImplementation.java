import java.rmi.*;
import java.rmi.server.*;


public class ServerImplementation extends UnicastRemoteObject implements IServerImplementation {
    private Utility utility;
    private DataRepository data;
    private final boolean debug_mode = true;
    
    protected ServerImplementation() throws RemoteException {
        super();
        utility = new Utility();
        data = DataRepository.create();
    }

    @Override
    public boolean join(String IP, int PORT) throws RemoteException {

        if(debug_mode) System.out.println("JOIN:"+IP + ":" + PORT);
        //add clientDetails to client Queue
        data.addToServer(IP,PORT);
        return true;
    }

    @Override
    public boolean leave(String IP, int PORT) throws RemoteException{
        if(debug_mode) System.out.println("JOIN:"+IP + ":" + PORT);
        data.removeFromServer(IP,PORT);
        return true;
    }

    @Override
    public boolean subscribe(String IP, int PORT, String article) throws RemoteException{
        if(debug_mode) System.out.println("Subscribe:"+IP + ":" + PORT + " -> " + article);
        data.subscribe(IP,PORT,article);
        return true;
    }

    @Override
    public boolean unsubscribe(String IP, int PORT, String article) throws RemoteException {
        if(debug_mode) System.out.println("Unsubscribe:"+IP + ":" + PORT + " -> " + article);
        data.unSubscribe(IP,PORT,article);
        return true;
    }

    @Override
    public boolean publish(String article, String IP, int PORT) throws RemoteException {
        if(debug_mode) System.out.println("Publish:"+IP + ":" + PORT + " -> " + article);
        data.publish(article,IP,PORT);
//        System.out.println(IP + ":" + PORT);
//        try {
//            ConfigManager configManager = ConfigManager.create();
//            ISender sender = new Sender(UDPSocket.createSocket(Integer.parseInt(configManager.getValue(ConfigManager.UDP_SERVER_PORT))));
//            sender.sendMessageToClient(IP, PORT, "Hello" + article);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return false;
//        }
        return true;
    }

    @Override
    public String ping(String msg) throws RemoteException {
        return msg;
    }
}
