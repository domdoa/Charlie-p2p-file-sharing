import helpers.FileSerializer;
import helpers.FileSystemWatcher;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.FileMetadata;
import models.Peer;
import network.FileServer;
import org.apache.commons.io.FileUtils;
import services.DownloadManager;

import java.io.File;
import java.net.InetAddress;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileSharingMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        primaryStage.setTitle("P2PBit");
        primaryStage.setScene(new Scene(root, 600, 550));
        primaryStage.show();

        // TODO: Deserialize metadatas from the file
        HashMap<String,String> metadatas = new FileSerializer().readFromFile();

        // TODO: Start the local fileserver to serve those peers who want to download
        FileServer fileServer = new FileServer();
        new Thread(fileServer).start();

        new Thread(new FileSystemWatcher(Paths.get("C:/Users/totha/TEST"), true)).start();

        // Create a peer which can "download" test.txt from the fileserver
        FileMetadata file = new FileMetadata();
        file.setFileName("photo");
        file.setExtension("jpg");
        file.setSize(1583860);
        List<Peer> peers = new ArrayList<>();
        peers.add(new Peer(InetAddress.getLocalHost().getHostAddress(),fileServer.getServerSocket().getLocalPort()));
        System.out.println("Peer address: " + peers.get(0).getIpAddress());
        System.out.println("Peer port: " + peers.get(0).getPort());
        new DownloadManager(file,peers).start();

        List<File> filelist = (List<File>) FileUtils.listFiles(new File(FileSerializer.metaDatas.get("defaultDir")), null, true);

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
