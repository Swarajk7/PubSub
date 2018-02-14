import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClientConfigManager {
    public static final String RMI_REGISTRY_ADDRESS = "rmiregistryaddress";
    public static final String RMI_PORT_NUMBER = "rmiportnumber";
    public static final String RMI_BINDING_NAME = "rmibindingname";
    public static final String IS_DEBUG = "isdebug";
    //fix below line for better lookup.. don't hardcode
    private static String filename = "./client_config.properties";
    private Properties prop;
    private static ClientConfigManager obj = null;

    private ClientConfigManager() {
    }

    public static ClientConfigManager create() throws IOException {
        if (obj != null) return obj;
        obj = new ClientConfigManager();
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
