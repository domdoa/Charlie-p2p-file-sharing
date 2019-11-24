package com.iot.desktop.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.iot.desktop.models.DownloadFileModel;

import java.net.URL;
import java.util.*;

public class RootController implements Initializable {

    @FXML private TableColumn<DownloadFileModel,String> downloadFile;
    @FXML private TableColumn<DownloadFileModel,String> downloadSize;
    @FXML private TableColumn<DownloadFileModel,String> downloadProgress;
    @FXML private TableColumn<DownloadFileModel,String> downloadSpeed;
    @FXML public TableView<DownloadFileModel> downloadTable;
    ObservableList<DownloadFileModel> observableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        downloadFile.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        downloadSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        downloadProgress.setCellValueFactory(new PropertyValueFactory<>("progress"));
        downloadSpeed.setCellValueFactory(new PropertyValueFactory<>("speed"));
        observableList = FXCollections.observableList(createFiles());
        downloadTable.setItems(observableList);
    }

    private List<DownloadFileModel> createFiles() {
        List<DownloadFileModel> res = new ArrayList<>();
        Random rng = new Random();
        for (int i = 0; i < 15; i++){
            res.add(new DownloadFileModel((i+1)+".file",
                        rng.nextInt(900)+50+"MB",
                    rng.nextInt(100)+"%",
                    rng.nextInt(100)+"%"));
        }
        return res;
    }
}
