import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.*;

public class DataRepository {
    private HashMap<String, HashSet<String>> typeToClientMap, organizationToClientMap, originatorToClientMap;
    private HashMap<String, ClientDetails> clientMap;
    private int MAX_COUNT = 10;
    private static DataRepository repository;
    private Utility utility;

    private DataRepository() {
        typeToClientMap = new HashMap<>();
        organizationToClientMap = new HashMap<>();
        originatorToClientMap = new HashMap<>();
        clientMap = new HashMap<>();
        utility = new Utility();

    }

    public static DataRepository create() {
        if (repository == null) repository = new DataRepository();
        return repository;
    }

    public void addToServer(String IP, int PORT) throws RemoteException {
        if (!utility.validateIP(IP)) throw new RemoteException("Invalid IP Address");
        String ip_port = utility.appendIPAndPort(IP, PORT);
        //check is same ip and port, already a client is joined and then throw exception
        if (clientMap.size() < MAX_COUNT && !clientMap.containsKey(ip_port)) {
            ClientDetails clientDetails = new ClientDetails(IP, PORT);
            clientMap.put(ip_port, clientDetails);
        }
    }

    public void removeFromServer(String IP, int PORT) throws RemoteException {
        if (!utility.validateIP(IP)) throw new RemoteException("Invalid IP Address");
        String ip_port = utility.appendIPAndPort(IP, PORT);
        //check is same ip and port, already a client is joined and then throw exception
        if (clientMap.containsKey(ip_port)) {

            for (String type : clientMap.get(ip_port).subscribedType) {
                typeToClientMap.get(type).remove(ip_port);
            }

            for (String type : clientMap.get(ip_port).subscribedOriginator) {
                originatorToClientMap.get(type).remove(ip_port);
            }

            for (String type : clientMap.get(ip_port).subscribedOrganization) {
                organizationToClientMap.get(type).remove(ip_port);
            }

            clientMap.remove(ip_port);
        }
    }

    public void subscribe(String IP, int PORT, String article) throws RemoteException {
        if (!utility.validateIP(IP)) throw new RemoteException("Invalid IP Address");
        String ip_port = utility.appendIPAndPort(IP, PORT);
        //check is same ip and port, already a client is joined and then throw exception

        if (!clientMap.containsKey(ip_port)) throw new RemoteException("Haven't joined server yet!!!");

        if (!utility.validateArticle(article, false)) throw new RemoteException("Invalid Article");

        //trim the string to remove padding spaces
        String[] words = article.trim().split(";",-1);

        if (!"".equals(words[0])) {
            if (typeToClientMap.containsKey(words[0])) {
                typeToClientMap.get(words[0]).add(ip_port);
            } else {
                HashSet<String> typeSet = new HashSet<String>();
                typeSet.add(ip_port);
                typeToClientMap.put(words[0], typeSet);
            }

            clientMap.get(ip_port).subscribedType.add(words[0]);
        }

        if (!"".equals(words[1])) {
            if (originatorToClientMap.containsKey(words[1])) {
                originatorToClientMap.get(words[1]).add(ip_port);
            } else {
                HashSet<String> originatorSet = new HashSet<String>();
                originatorSet.add(ip_port);
                originatorToClientMap.put(words[1], originatorSet);
            }
            clientMap.get(ip_port).subscribedOriginator.add(words[1]);
        }

        if (!"".equals(words[2])) {
            if (organizationToClientMap.containsKey(words[2])) {
                organizationToClientMap.get(words[2]).add(ip_port);
            } else {
                HashSet<String> orgSet = new HashSet<String>();
                orgSet.add(ip_port);
                organizationToClientMap.put(words[2], orgSet);
            }
            clientMap.get(ip_port).subscribedOrganization.add(words[2]);
        }

    }

    public void unSubscribe(String IP, int PORT, String article) throws RemoteException {
        if (!utility.validateIP(IP)) throw new RemoteException("Invalid IP Address");
        String ip_port = utility.appendIPAndPort(IP, PORT);
        //check is same ip and port, already a client is joined and then throw exception

        if (!clientMap.containsKey(ip_port)) throw new RemoteException("Haven't joined server yet!!!");

        if (!utility.validateArticle(article, false)) throw new RemoteException("Invalid Article");

        //trim the string to remove padding spaces
        String[] words = article.trim().split(";");

        if (!"".equals(words[0])) {
            if (typeToClientMap.containsKey(words[0])) {
                typeToClientMap.get(words[0]).remove(ip_port);
            }

            clientMap.get(ip_port).subscribedType.remove(words[0]);
        }

        if (!"".equals(words[1])) {
            if (originatorToClientMap.containsKey(words[1])) {
                originatorToClientMap.get(words[1]).remove(ip_port);
            }

            clientMap.get(ip_port).subscribedOriginator.remove(words[1]);
        }

        if (!"".equals(words[2])) {
            if (organizationToClientMap.containsKey(words[2])) {
                organizationToClientMap.get(words[2]).remove(ip_port);
            }

            clientMap.get(ip_port).subscribedOrganization.remove(words[2]);
        }

    }

}
