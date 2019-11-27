package com.iot.desktop.controllers;

import com.iot.desktop.FileSharingMain;
import com.iot.desktop.models.DownloadFileModel;
import com.iot.desktop.models.FileMetadata;
import com.iot.desktop.models.Peer;
import com.iot.desktop.models.UploadFileModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;

public class RootController implements Initializable {

    @FXML
    public TableView<UploadFileModel> uploadTable;
    @FXML
    public TableColumn<UploadFileModel, String> uploadFile;
    @FXML
    public TableColumn<UploadFileModel, String> uploadSize;
    @FXML
    public TableColumn<UploadFileModel, Date> uploadDate;
    public static ObservableList<UploadFileModel> uploadedFiles;

    @FXML
    public Label groupNameLabel;
    @FXML
    public TextField groupTextField;

    @FXML
    public TextField inviteStringField;
    @FXML
    public Label inviteStringLabel;

    @FXML
    private TableColumn<DownloadFileModel, String> downloadFile;
    @FXML
    private TableColumn<DownloadFileModel, String> downloadSize;
    @FXML
    private TableColumn<DownloadFileModel, String> downloadProgress;
    @FXML
    private TableColumn<DownloadFileModel, String> downloadSpeed;
    @FXML
    public TableView<DownloadFileModel> downloadTable;
    public static ObservableList<DownloadFileModel> downloadedFiles;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Constants.userGroups.size() > 1) {
            Constants.groupNameOfTheUser = Constants.userGroups.get(1).getName();
            groupTextField.setText(Constants.groupNameOfTheUser);
            inviteStringField.setText(Constants.userGroups.get(1).getInviteString());
            createGroupFolder(Constants.groupNameOfTheUser);
        } else {
            groupNameLabel.setVisible(false);
            groupTextField.setVisible(false);
            inviteStringField.setVisible(false);
            inviteStringLabel.setVisible(false);
        }
        createGroupFolder("public");
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
        //new DownloadManager(file,peers).start();
    }


    private List<DownloadFileModel> createFiles() {
        List<DownloadFileModel> res = new ArrayList<>();
        Random rng = new Random();
        for (int i = 0; i < 15; i++) {
            res.add(new DownloadFileModel((i + 1) + ".file",
                    rng.nextInt(900) + 50,
                    rng.nextInt(100) + "%",
                    rng.nextInt(100) + "%"));
        }
        return res;
    }

    private void createGroupFolder(String groupName) {
        File file = new File(Constants.currentDirectory + Constants.charlieP2PFolder + "/" + groupName);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }

    }

}
