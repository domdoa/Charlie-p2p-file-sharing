package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.helpers.FileSerializer;
import sample.network.FileServer;
import sample.network.PeerSocket;
import sample.network.ServerConnection;

import java.util.HashMap;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("ui/rootView.fxml"));
        primaryStage.setTitle("P2PBit");
        primaryStage.setScene(new Scene(root, 600, 550));
        primaryStage.show();

        // TODO: Deserialize metadatas from the file
        HashMap<String,String> metadatas = new FileSerializer().readFromFile();

        // TODO: Start the local fileserver to serve those peers who want to download
        FileServer fileServer = new FileServer();
        new Thread(fileServer).start();

        PeerSocket socket = new PeerSocket();

    }


    @Override
    public void stop() throws Exception {
        super.stop();
        // TODO: Serialize actualmetadatas to file with custom FileHandler class
        new FileSerializer().writeToFile();
        // TODO: Notify the backend that this peer is not available anymore
        new ServerConnection().notifyActualPeerIsOffline();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
