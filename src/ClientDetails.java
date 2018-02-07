import java.util.HashSet;

public class ClientDetails {
    String IP;
    int port;
    HashSet<String> subscribedOrganization;
    HashSet<String> subscribedOriginator;
    HashSet<String> subscribedType;

    public ClientDetails(String IP, int port) {
        this.IP = IP;
        this.port = port;
        subscribedOrganization = new HashSet<>();
        subscribedOriginator = new HashSet<>();
        subscribedType = new HashSet<>();
    }
}
