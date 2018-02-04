import java.rmi.*;
public interface IServer extends Remote{

public boolean join(String IP,int PORT)throws RemoteException;

public boolean leave(String IP,int PORT)throws RemoteException;

public boolean subscribe(String IP,int PORT, String article)throws RemoteException;

public boolean unsubscribe(String IP,int PORT, String article)throws RemoteException;

public boolean publish(String article,String IP,int PORT)throws RemoteException;

public boolean ping()throws RemoteException;


}
