import javafx.util.Pair;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class DataRepository {
    private HashMap<String, Set<String>> typeToClientMap, organizationToClientMap, originatorToClientMap;
    private HashMap<String, ClientDetails> clientMap;
    private int MAX_COUNT = 10;
    private static DataRepository repository;
    private Utility utility;
    private Queue<Pair<String,String>> publishQueue;

    private DataRepository() {
        typeToClientMap = new HashMap<>();
        organizationToClientMap = new HashMap<>();
        originatorToClientMap = new HashMap<>();
        clientMap = new HashMap<>();
        utility = new Utility();
        publishQueue = new ConcurrentLinkedQueue<>();
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
                // Assuming that maximum number of clients for this system will be 100
                Set<String> typeSet = (Set) ConcurrentHashMap.newKeySet(100);
                typeSet.add(ip_port);
                typeToClientMap.put(words[0], typeSet);
            }

            clientMap.get(ip_port).subscribedType.add(words[0]);
        }

        if (!"".equals(words[1])) {
            if (originatorToClientMap.containsKey(words[1])) {
                originatorToClientMap.get(words[1]).add(ip_port);
            } else {
                // Assuming that maximum number of clients for this system will be 100
                Set<String> originatorSet = (Set) ConcurrentHashMap.newKeySet(100);
                originatorSet.add(ip_port);
                originatorToClientMap.put(words[1], originatorSet);
            }
            clientMap.get(ip_port).subscribedOriginator.add(words[1]);
        }

        if (!"".equals(words[2])) {
            if (organizationToClientMap.containsKey(words[2])) {
                organizationToClientMap.get(words[2]).add(ip_port);
            } else {
                // Assuming that maximum number of clients for this system will be 100
                Set<String> orgSet = (Set) ConcurrentHashMap.newKeySet(100);
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
        String[] words = article.trim().split(";",-1);

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

    public Pair<String, String> getHeadItemFromPublishQueue() {
        //ensures no other thread is getting this item and Queue is concurreny safe
        return publishQueue.poll();
    }

    public ClientDetails validateAndGetClientForPublish(String key, String[] tokens) {
        return clientMap.get(key);
    }
    public void publish(String article, String IP, int PORT) throws  RemoteException {
        if (!utility.validateIP(IP)) throw new RemoteException("Invalid IP Address");
        String ip_port = utility.appendIPAndPort(IP, PORT);

        if (!utility.validateArticle(article, true)) throw new RemoteException("Invalid Article");

        //trim the string to remove padding spaces
        String[] words = article.trim().split(";", -1);
        HashSet<String> publishSet = new HashSet<String>();

        if (!"".equals(words[0])) {
            if (typeToClientMap.containsKey(words[0])) {
                publishSet.addAll(typeToClientMap.get(words[0]));
            }
        }

        if (!"".equals(words[1])) {
            if (originatorToClientMap.containsKey(words[1])) {
                publishSet.addAll(originatorToClientMap.get(words[1]));
            }
        }

        if (!"".equals(words[2])) {
            if (organizationToClientMap.containsKey(words[2])) {
                publishSet.addAll(organizationToClientMap.get(words[2]));
            }
        }

        for (String type : publishSet) {
            Pair tmpPair = new Pair(type, article);
            publishQueue.add(tmpPair);
        }

    }

}
