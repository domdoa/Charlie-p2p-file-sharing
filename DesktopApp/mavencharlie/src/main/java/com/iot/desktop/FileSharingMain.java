package com.iot.desktop;

import com.iot.desktop.controllers.MyStompSessionHandler;
import com.iot.desktop.helpers.FileSystemWatcher;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.iot.desktop.helpers.FileSerializer;
import com.iot.desktop.models.FileMetadata;
import com.iot.desktop.models.Peer;
import com.iot.desktop.network.FileServer;
import com.iot.desktop.services.DownloadManager;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.InetAddress;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class FileSharingMain extends Application {

    private static String URL = "ws://localhost:8080/desktop";
    public static int serverSocketPort;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FileServer fileServer = new FileServer(); // start thread here so it has time to load
        Thread t =  new Thread(fileServer);
        t.start();

        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        primaryStage.setTitle("P2PBit");
        primaryStage.setScene(new Scene(root, 600, 550));
        primaryStage.show();

        // Deserialize metadatas from the file
        HashMap<String,String> metadatas = new FileSerializer().readFromFile();

        // Observe the default directory for changes
        new Thread(new FileSystemWatcher(Paths.get(FileSerializer.metaDatas.get("defaultDir")), true)).start();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                // code goes here.
                WebSocketClient client = new StandardWebSocketClient();
                WebSocketStompClient stompClient = new WebSocketStompClient(client);
                //Convert to string
                stompClient.setMessageConverter(new StringMessageConverter());
                //Used for converting models
                //stompClient.setMessageConverter(new MappingJackson2MessageConverter());

                StompSessionHandler sessionHandler = new MyStompSessionHandler();
                stompClient.connect(URL, sessionHandler);;
                new Scanner(System.in).nextLine(); // Don't close immediately.
            }
        });
        t1.start();

        serverSocketPort = fileServer.getServerSocket().getLocalPort();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        // TODO: Serialize actualmetadatas to file with custom FileHandler class
        new FileSerializer().writeToFile();
        // TODO: Notify the backend that this peer is not available anymore
        //new ServerConnection().notifyActualPeerIsOffline();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
