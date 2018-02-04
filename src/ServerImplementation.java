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
    public boolean subscribe(String IP, int PORT, String article)  {
        return false;
    }

    @Override
    public boolean unsubscribe(String IP, int PORT, String article)  {
        return false;
    }

    @Override
    public boolean publish(String article, String IP, int PORT)  {
        return false;
    }

    @Override
    public boolean ping()  {
        return false;
    }
}
