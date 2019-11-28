package com.iot.desktop.controllers;

import com.iot.desktop.dtos.File;
import com.iot.desktop.dtos.FilePeers;
import com.iot.desktop.helpers.FileSerializer;
import com.iot.desktop.models.DownloadFileModel;
import com.iot.desktop.models.FileMetadata;
import com.iot.desktop.models.Peer;
import com.iot.desktop.services.DownloadManager;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;

@RestController
@RequestMapping("/")
public class EndpointController {

    @PostMapping
    public ResponseEntity receiveFileAndPeerDetails(@RequestBody FilePeers filePeers){

        boolean isAlready = false;
        for (int i = 0; i< FileSerializer.downloadedFiles.size(); i++){
            DownloadFileModel temp = RootController.downloadedFiles.get(i);
            if(temp.getFileName().equals(filePeers.getFileMetadata().getName())
                && temp.getProgress().equals("100.00%")){
                isAlready = true;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        new Alert(Alert.AlertType.INFORMATION,
                                "You already downloaded " + temp.getFileName() + ", check this on your default folder.", ButtonType.CANCEL).showAndWait();
                    }
                });
            }
        }
        if(!isAlready){
            new DownloadManager(filePeers.getFileMetadata(), filePeers.getPeerList()).start();
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
