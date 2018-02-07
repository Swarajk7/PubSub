import java.util.HashMap;

public class DataRepository {
    private HashMap<String, String> typeToClientMap, organizationToClientMap, originatorToClientMap;
    private HashMap<String, ClientDetails> clientMap;

    private static DataRepository repository;

    private DataRepository() {
        typeToClientMap = new HashMap<>();
        organizationToClientMap = new HashMap<>();
        organizationToClientMap = new HashMap<>();
        clientMap = new HashMap<>();
    }

    public static DataRepository create() {
        if (repository == null) repository = new DataRepository();
        return repository;
    }
}
