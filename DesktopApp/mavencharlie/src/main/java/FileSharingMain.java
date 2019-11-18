import helpers.PublicIPAddressResolver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import helpers.FileSerializer;
import models.FileMetadata;
import models.Peer;
import network.FileServer;
import network.PeerSocket;
import network.ServerConnection;
import services.DownloadManager;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FileSharingMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/rootView.fxml"));
        primaryStage.setTitle("P2PBit");
        primaryStage.setScene(new Scene(root, 600, 550));
        primaryStage.show();

        // TODO: Deserialize metadatas from the file
        HashMap<String,String> metadatas = new FileSerializer().readFromFile();

        // TODO: Start the local fileserver to serve those peers who want to download
        FileServer fileServer = new FileServer();
        /*Thread t =*/  new Thread(fileServer).start();
        //t.start();

        // Create a peer which can "download" test.txt from the fileserver
        FileMetadata file = new FileMetadata();
        file.setFileName("test");
        file.setExtension("txt");
        file.setSize(3495);
        List<Peer> peers = new ArrayList<>();
        peers.add(new Peer(InetAddress.getLocalHost().getHostAddress(),fileServer.getServerSocket().getLocalPort()));
        System.out.println("Peer address: " + peers.get(0).getIpAddress());
        System.out.println("Peer port: " + peers.get(0).getPort());
        new DownloadManager(file,peers).start();

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
