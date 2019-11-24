package com.iot.desktop.controllers;

import com.iot.desktop.FileSharingMain;
import com.iot.desktop.models.FileMetadata;
import com.iot.desktop.models.Peer;
import com.iot.desktop.models.UploadFileModel;
import com.iot.desktop.services.DownloadManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.iot.desktop.models.DownloadFileModel;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;

public class RootController implements Initializable {

    @FXML public TableView<UploadFileModel> uploadTable;
    @FXML public TableColumn<UploadFileModel, String> uploadFile;
    @FXML public TableColumn<UploadFileModel, String> uploadSize;
    @FXML public TableColumn<UploadFileModel, Date> uploadDate;
    public static ObservableList<UploadFileModel> uploadedFiles;

    @FXML private TableColumn<DownloadFileModel,String> downloadFile;
    @FXML private TableColumn<DownloadFileModel,String> downloadSize;
    @FXML private TableColumn<DownloadFileModel,String> downloadProgress;
    @FXML private TableColumn<DownloadFileModel,String> downloadSpeed;
    @FXML public TableView<DownloadFileModel> downloadTable;
    public static ObservableList<DownloadFileModel> downloadedFiles;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        uploadFile.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        uploadSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        uploadDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        uploadedFiles = FXCollections.observableList(new ArrayList<>());
        uploadTable.setItems(uploadedFiles);

        downloadFile.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        downloadSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        downloadProgress.setCellValueFactory(new PropertyValueFactory<>("progress"));
        downloadSpeed.setCellValueFactory(new PropertyValueFactory<>("speed"));
        downloadedFiles = FXCollections.observableList(new ArrayList<>());
        downloadTable.setItems(downloadedFiles);

        // Create a peer which can "download" test.txt from the fileserver
        FileMetadata file = new FileMetadata();
        file.setFileName("dummy");
        file.setExtension("pdf");
        file.setSize(11939277);
        List<Peer> peers = new ArrayList<>();
        try {
            peers.add(new Peer(InetAddress.getLocalHost().getHostAddress(), FileSharingMain.serverSocketPort));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("Peer address: " + peers.get(0).getIpAddress());
        System.out.println("Peer port: " + peers.get(0).getPort());
        new DownloadManager(file,peers).start();
    }


    private List<DownloadFileModel> createFiles() {
        List<DownloadFileModel> res = new ArrayList<>();
        Random rng = new Random();
        for (int i = 0; i < 15; i++){
            res.add(new DownloadFileModel((i+1)+".file",
                        rng.nextInt(900)+50,
                    rng.nextInt(100)+"%",
                    rng.nextInt(100)+"%"));
        }
        return res;
    }
}
