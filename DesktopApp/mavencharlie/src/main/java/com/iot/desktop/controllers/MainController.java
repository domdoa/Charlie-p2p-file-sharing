package com.iot.desktop.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iot.desktop.FileSharingMain;
import com.iot.desktop.dtos.File;
import com.iot.desktop.helpers.FileSerializer;
import com.iot.desktop.models.FileMetadata;
import com.iot.desktop.models.ForeignPC;
import com.iot.desktop.models.Group;
import com.iot.desktop.models.Peer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import okhttp3.*;
import org.apache.tomcat.util.bcel.Const;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainController {
    public String JWTToken;
    private static String contentType = "application/json";
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Text actionTarget;

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) throws Exception {

        String username = usernameField.getText();
        Constants.emailAddress = username;
        String password = passwordField.getText();
        String body = "{\"email\"" + ":" + "\"" + username + "\"," + "\"password\": " + "\"" + password + "\"" + "}";

        Request request = new Request.Builder()
                .url(Constants.serverURL + Constants.loginEndpoint)
                .addHeader("Content-Type", contentType)
                .post(okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body))
                .build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        Response response = call.execute();
        Gson gson = new Gson();
        ResponseBody responseBody = response.body();

        if (responseBody != null) {
            JWTToken = responseBody.string();
            if(!JWTToken.equals("")){
                String bearer = JWTToken.split(":")[1];
                JWTToken = bearer.substring(1,bearer.length()-2);
                //add all the super peers here
                //Constants.allSuperPeers.addAll(Objects.requireNonNull(notifyPeerIsOnline(username, JWTToken)));
                notifyPeerIsOnline(username,JWTToken);
                Constants.JWTToken = JWTToken;
                Constants.userGroups = getGroupOfTheUser(username);
            }

        }
        actionTarget.setText("Sign in button pressed");
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/rootView.fxml"));
        Scene scene = new Scene(root, 600, 550);
        stage.setScene(scene);
        stage.show();


        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                notifyPeerIsOffline(Constants.emailAddress, JWTToken);
                Platform.exit();
                System.exit(0);
            }
        });
    }

    private void notifyPeerIsOnline(String userEmail,String authorization) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        List<File> peerFiles = new ArrayList<>(FileSerializer.downloadedFiles);
        peerFiles.addAll(FileSerializer.uploadedFiles);
        Peer peer = new Peer(Constants.localAddress, FileSharingMain.serverSocketPort,Constants.peerSpringPort);
        peer.setFileList(peerFiles);
        peer.setEmail(userEmail);

        Request request = new Request.Builder()
                .url(Constants.serverURL + Constants.notifyPeerIsOnlineEndpoint)
                .addHeader("Authorization",authorization)
                .addHeader("Content-Type", contentType)
                .post(okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mapper.writeValueAsString(peer)))
                .build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        Response response = call.execute();
        ResponseBody responseBody =response.body();
        Gson gson = new Gson();
//        assert responseBody != null;
//        List foreignPCS = gson.fromJson(responseBody.string(),List.class);
        int responseCode = response.code();
        if(responseCode!=200){
            System.err.println("Notify Peer Is Online went wrong");
        }

    }

    public List<Group> getGroupOfTheUser(String emailAddress) throws IOException {
        Request request = new Request.Builder()
                .url(Constants.serverURL + Constants.getGroupsForUserEndpoint+"?email="+emailAddress)
                .addHeader("Authorization",JWTToken)
                .addHeader("Content-Type", contentType)
                .build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        Response response = call.execute();
        ResponseBody responseBody = response.body();
        Gson gson = new Gson();
        List<Group> listOfGroups = gson.fromJson(responseBody.string(),new TypeToken<List<Group>>(){}.getType());

        return listOfGroups;
    }

    public static void notifyPeerIsOffline(String userEmail,String authorization) {
        ObjectMapper mapper = new ObjectMapper();

        List<File> peerFiles = new ArrayList<>(FileSerializer.downloadedFiles);
        peerFiles.addAll(FileSerializer.uploadedFiles);
        Peer peer = new Peer(Constants.localAddress, FileSharingMain.serverSocketPort);
        peer.setFileList(peerFiles);
        peer.setEmail(userEmail);

        try{
            Request request = new Request.Builder()
                    .url(Constants.serverURL + Constants.notifyPeerIsOfflineEndpoint)
                    .addHeader("Authorization",authorization)
                    .addHeader("Content-Type", contentType)
                    .delete(okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mapper.writeValueAsString(peer)))
                    .build();
            OkHttpClient client = new OkHttpClient();
            Call call = client.newCall(request);
            Response response = call.execute();
            int responseCode = response.code();
            if(responseCode!=200){
                System.err.println("Notify Peer Is Online went wrong");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}