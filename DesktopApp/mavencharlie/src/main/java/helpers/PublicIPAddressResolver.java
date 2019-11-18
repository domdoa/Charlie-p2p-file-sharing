package helpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class PublicIPAddressResolver {

    public static String GetPublicIPAddress(){
        String systemIpAddress = "";
        try
        {
            URL url_name = new URL("http://bot.whatismyipaddress.com");
            BufferedReader sc = new BufferedReader(new InputStreamReader(url_name.openStream()));
            // reads system IPAddress
            systemIpAddress = sc.readLine().trim();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return systemIpAddress;
    }
}
