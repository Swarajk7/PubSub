import java.rmi.*;
public interface IServerImplementation extends Remote {

    boolean join(String IP, int PORT) throws RemoteException;

    boolean leave(String IP, int PORT) throws RemoteException;

    boolean subscribe(String IP, int PORT, String article) throws RemoteException;

    boolean unsubscribe(String IP, int PORT, String article) throws RemoteException;

    boolean publish(String article, String IP, int PORT) throws RemoteException;

    String ping(String msg) throws RemoteException;

}
