import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
Singleton class for handling server side config read and write
 */
public class ConfigManager {
    public static final String RMI_REGISTRY_ADDRESS = "rmiregistryaddress";
    public static final String UDP_SERVER_PORT = "port";
    //fix below line for better lookup.. don't hardcode
    private static String filename = "C:\\Users\\sk111\\OneDrive\\Documents\\private\\PubSub_V2\\server_config.properties";
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
}