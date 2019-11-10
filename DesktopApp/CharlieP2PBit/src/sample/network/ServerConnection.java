package sample.network;

import sample.helpers.FileSerializer;
import sample.models.Peer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerConnection {

    private static List<String> BASE_URL;

    public ServerConnection(){
        List<String> urls = new ArrayList<>();
        for (Map.Entry<String , String> entry : FileSerializer.metaDatas.entrySet()) {
            if(entry.getKey().contains("BASE_URL"))
                urls.add(entry.getValue());
        }
        BASE_URL = new ArrayList<>(urls);
    }

    public List<Peer> getAvailableSeeders(String filename, String md5Signature){
        HttpURLConnection con = null;
        List<Peer> availablePeers = null;
        try{
            URL url = new URL(BASE_URL.get(0) + "peers/getAvailableSeeders");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(ParameterStringBuilder.getParamsString(
                    new HashMap<String, String>() {{put("filename", filename); put("signature", md5Signature);}}
            ));
            out.flush();
            out.close();
            con.setConnectTimeout(10000);
            int status = con.getResponseCode();

            BufferedReader in = null;
            if (status == 200){
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                // TODO: Create a list of peers from the response, it is just a string...
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
            }
            else {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (con != null){
                con.disconnect();
            }
        }
        return availablePeers;
    }


    public void notifyActualPeerIsOnline(int port) throws Exception {
        HttpURLConnection con = null;
        try{
            URL url = new URL(BASE_URL.get(0) + "/peers/notifyActualPeerIsOnline");
            con = buildHttpConnectionForNotify(url);
            if(con != null){
                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                out.writeBytes(ParameterStringBuilder.getParamsString(new HashMap<String, String>() {{put("port", ""+port);}}));
                out.flush();
                out.close();
                int responseCode = con.getResponseCode();

                if (responseCode == 200){
                    return;
                }
                else {
                    // TODO: Implement retry logic
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (con != null){
                con.disconnect();
            }
        }
    }

    public void notifyActualPeerIsOffline() throws Exception {
        HttpURLConnection con = null;
        try{
            URL url = new URL(BASE_URL.get(0)+ "/peers/notifyActualPeerIsOffline");
            con = buildHttpConnectionForNotify(url);
            if(con != null){
                con.getResponseCode();   // If success is required, then check for the response is 200
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (con != null){
                con.disconnect();
            }
        }
    }

    private HttpURLConnection buildHttpConnectionForNotify(URL url) throws IOException {
        try{
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(10000);
            con.setDoInput(true);
            con.setDoOutput(true);
            return con;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
