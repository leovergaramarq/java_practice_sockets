package justachat;

import java.util.regex.Pattern;

public class Utils {
    public static boolean validIp(String ip) {
        // https://stackoverflow.com/questions/5667371/validate-ipv4-address-in-java
        return ip.equals("") || ip.equals(LOCALHOST) || Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.) {3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$"
        ).matcher(ip).find();
    }
    
    public static boolean validPort(int port) {
        return port > -1 && port < 10000;
    }
    
    public static int randomPort() {
        return (int)(Math.random() * 10000);
    }
    
    public static final String LOCALHOST = "localhost";
}
