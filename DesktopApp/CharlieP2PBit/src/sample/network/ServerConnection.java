package sample.network;

import sample.helpers.FileHandler;
import sample.models.Peer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServerConnection {

    private static String[] BASE_URL = new String[5];

    public ServerConnection(){
        List<String> urls = new ArrayList<>();
        for (Map.Entry<String , String> entry : FileHandler.metaDatas.entrySet()) {
            if(entry.getKey().contains("BASE_URL"))
                urls.add(entry.getValue());
        }
        BASE_URL = (String[]) urls.toArray();
    }

    public List<Peer> getAvailableSeeders(String filename, String md5Signature){
        return null;
    }


    public void notifyActualPeerIsOnline() throws Exception {
        try{
            URL url = new URL(BASE_URL[0] + "/api/peers/notifyActualPeerIsOnline");
            notifyHttpRequestBuilder(url);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void notifyActualPeerIsOffline() throws Exception {
        try{
            URL url = new URL(BASE_URL[0]+ "/api/peers/notifyActualPeerIsOffline");
            notifyHttpRequestBuilder(url);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void notifyHttpRequestBuilder(URL url) throws IOException {
        try{
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setConnectTimeout(10000);
            int status = con.getResponseCode();

            if(status == 200)
                return;
            else{
                // TODO: Implement retry logic
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
