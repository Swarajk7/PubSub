import java.rmi.RemoteException;
import java.util.HashMap;

public class DataRepository {
    private HashMap<String, String> typeToClientMap, organizationToClientMap, originatorToClientMap;
    private HashMap<String, ClientDetails> clientMap;
    private int MAX_COUNT = 10;
    private static DataRepository repository;
    private Utility utility;

    private DataRepository() {
        typeToClientMap = new HashMap<>();
        organizationToClientMap = new HashMap<>();
        organizationToClientMap = new HashMap<>();
        clientMap = new HashMap<>();
        utility = new Utility();

    }

    public static DataRepository create() {

        if (repository == null) repository = new DataRepository();
        return repository;
    }

    public void addToServer(String IP, int PORT)throws RemoteException {
        if(!utility.validateIP(IP)) throw new RemoteException("Invalid IP Address");
        String ip_port = utility.appendIPAndPort(IP,PORT);
        //check is same ip and port, already a client is joined and then throw exception
        if (clientMap.size() < MAX_COUNT && !clientMap.containsKey(ip_port)){
            ClientDetails clientDetails = new ClientDetails(IP, PORT);
            clientMap.put(ip_port,clientDetails);
        }

    }

    public void removeFromServer(String IP, int PORT) throws RemoteException{
        if(!utility.validateIP(IP)) throw new RemoteException("Invalid IP Address");
        String ip_port = utility.appendIPAndPort(IP,PORT);
        //check is same ip and port, already a client is joined and then throw exception
        if (clientMap.containsKey(ip_port)){
            clientMap.remove(ip_port);
        }
        //Have to update typeToClientMap, organizationToClientMap, originatorToClientMap

    }

    public void subscribe(String IP, int PORT, String article) throws RemoteException{
        if(!utility.validateIP(IP)) throw new RemoteException("Invalid IP Address");
        String ip_port = utility.appendIPAndPort(IP,PORT);
        //check is same ip and port, already a client is joined and then throw exception

        if (!clientMap.containsKey(ip_port)) throw new RemoteException("Haven't joined server yet!!!");

        if(!utility.validateArticle(article, false)) throw new RemoteException("Invalid Article");

        //trim the string to remove padding spaces
        String[] words = article.trim().split(";");



    }

}
