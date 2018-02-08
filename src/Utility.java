import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    boolean validateIP(String IP) {
        Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
        Matcher matcher = pattern.matcher(IP);
        return matcher.matches();
    }

    boolean validateArticle(String article){
        return true;
    }

    String appendIPAndPort(String IP, int port) {
        return IP + ":" + port;
    }

}
