package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.DownloadFileModel;

import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class RootController implements Initializable {

    @FXML private TableColumn<DownloadFileModel,String> downloadFile;
    @FXML private TableColumn<DownloadFileModel,String> downloadSize;
    @FXML private TableColumn<DownloadFileModel,String> downloadProgress;
    @FXML private TableColumn<DownloadFileModel,String> downloadSpeed;
    @FXML private TableView<DownloadFileModel> downloadTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        downloadFile.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        downloadSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        downloadProgress.setCellValueFactory(new PropertyValueFactory<>("progress"));
        downloadSpeed.setCellValueFactory(new PropertyValueFactory<>("speed"));
        downloadTable.getItems().setAll(createFiles());
    }

    private List<DownloadFileModel> createFiles(){
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
