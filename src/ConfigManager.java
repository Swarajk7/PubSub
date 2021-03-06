import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Properties;

/*
Singleton class for handling server side config read and write.
ConfigurationManger is also readonly and hence won't cause any conflicts or concurrency issue.
 */
public class ConfigManager {
    public static final String RMI_REGISTRY_ADDRESS = "rmiregistryaddress";
    public static final String UDP_SERVER_PORT = "port";
    public static final String REGISTRY_SERVER_ADDRESS = "registryserveraddress";
    public static final String REGISTRY_SERVER_PORT = "registryserverport";
    public static final String UDP_SERVER_PORT2 = "updserverport2";
    public static final String RMI_PORT_NUMBER="rmiportnumber";
    public static final String RMI_BINDING_NAME="rmibindingname";
    public static final String SOCKET_TIMEOUT = "sockettimeout";
    public static final String NUMBER_OF_PUBLISH_THREADS = "numerofpublisherthreads";
    public static final String IS_DEBUG = "isdebug";
    public static final String MAX_CLIENT_COUNT = "maxclientcount";
    //fix below line for better lookup.. don't hardcode
    private static String filename = "./server_config.properties";
    private Properties prop;
    private static ConfigManager obj = null;

    private ConfigManager() {
    }

    public static ConfigManager create() throws IOException {
        if (obj != null) return obj;
        obj = new ConfigManager();
        obj.prop = new Properties();
        InputStream input = new FileInputStream(filename);
        obj.prop.load(input);
        return obj;
    }

    public String getValue(String key) {
        return this.prop.getProperty(key);
    }

    public int getIntegerValue(String key) {
        return Integer.parseInt(this.getValue(key));
    }
}
