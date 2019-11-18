package com.iot.desktop.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class MainController {
    @FXML private Text actiontarget;

    @FXML protected void handleSubmitButtonAction(ActionEvent event) throws Exception {
        actiontarget.setText("Sign in button pressed");
        /*FXMLLoader loader = new FXMLLoader(MainController.class.getClassLoader().getResource("sample/ui/rootView.xml"));
        Stage stage = (Stage) actiontarget.getScene().getWindow();
        stage.getScene().setRoot(loader.load());*/
    }
}
