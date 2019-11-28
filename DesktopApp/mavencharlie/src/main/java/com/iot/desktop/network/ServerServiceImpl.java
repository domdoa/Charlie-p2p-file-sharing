package com.iot.desktop.network;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iot.desktop.controllers.Constants;
import com.iot.desktop.dtos.File;
import com.iot.desktop.helpers.FileSerializer;
import com.iot.desktop.models.Group;
import com.iot.desktop.models.Peer;
import com.iot.desktop.network.interfaces.ServerServiceInterface;
import okhttp3.*;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerServiceImpl {

    private static List<String> BASE_URL;
    private ServerServiceInterface serverService;

    public ServerServiceImpl() {
        List<String> urls = new ArrayList<>();
        for (Map.Entry<String, String> entry : FileSerializer.metaDatas.entrySet()) {
            if (entry.getKey().contains("BASE_URL"))
                urls.add(entry.getValue());
        }
        BASE_URL = new ArrayList<>(urls);

        serverService = new Retrofit.Builder()
                .baseUrl(BASE_URL.get(0))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ServerServiceInterface.class);
    }

    public List<Peer> getAvailableSeeders(String filename, String md5Signature) {
        HttpURLConnection con = null;
        List<Peer> availablePeers = null;
        try {
            URL url = new URL(BASE_URL.get(0) + "peers/getAvailableSeeders");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(ParameterStringBuilder.getParamsString(
                    new HashMap<String, String>() {{
                        put("filename", filename);
                        put("signature", md5Signature);
                    }}
            ));
            out.flush();
            out.close();
            con.setConnectTimeout(10000);
            int status = con.getResponseCode();

            BufferedReader in = null;
            if (status == 200) {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                // TODO: Create a list of peers from the response, it is just a string...
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
            } else {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return availablePeers;
    }

    public void notifyActualPeerIsOnline(String ipAddress, int port) throws Exception {
        HttpURLConnection con = null;
        try {
            URL url = new URL(BASE_URL.get(0) + "/peers/notifyActualPeerIsOnline");
            con = buildHttpConnectionForNotify(url);
            if (con != null) {
                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                out.writeBytes(ParameterStringBuilder.getParamsString(
                        new HashMap<String, String>() {{
                            put("ipAddress", ipAddress);
                            put("port", "" + port);
                        }}));
                out.flush();
                out.close();
                int responseCode = con.getResponseCode();

                if (responseCode == 200) {
                    return;
                } else {
                    // TODO: Implement retry logic
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    public void notifyActualPeerIsOffline() throws Exception {
        HttpURLConnection con = null;
        try {
            URL url = new URL(BASE_URL.get(0) + "/peers/notifyActualPeerIsOffline");
            con = buildHttpConnectionForNotify(url);
            if (con != null) {
                con.getResponseCode();   // If success is required, then check for the response is 200
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    private HttpURLConnection buildHttpConnectionForNotify(URL url) throws IOException {
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(10000);
            con.setDoInput(true);
            con.setDoOutput(true);
            return con;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addFileToPeer(String emailAddress, File file) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Request request = new Request.Builder()
                .url(Constants.serverURL + Constants.addFileToUserEndpoint + "?email=" + emailAddress)
                .addHeader("Authorization", Constants.JWTToken)
                .addHeader("Content-Type", "application/json")
                .post(okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mapper.writeValueAsString(file)))
                .build();

        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        Response response = call.execute();
       if(response.code()!=200){
           System.err.println("Failed to add the file to the peer");
       }
    }

    public void updateFileOfPeer(File file, String fileName, String emailAddress) { }

    public void removeFileFromPeer(File file, String emailAddress) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Request request = new Request.Builder()
                .url(Constants.serverURL + Constants.removeFileFromUserEndpoint + "?email=" + emailAddress)
                .addHeader("Authorization", Constants.JWTToken)
                .addHeader("Content-Type", "application/json")
                .delete(okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mapper.writeValueAsString(file)))
                .build();

        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        Response response = call.execute();
        if(response.code()!=200){
            System.err.println("Failed to remove the file from the peer");
        }
    }
}
