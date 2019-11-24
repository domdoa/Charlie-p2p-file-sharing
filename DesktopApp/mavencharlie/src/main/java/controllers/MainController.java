package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainController {
    @FXML private Text actiontarget;

    @FXML protected void handleSubmitButtonAction(ActionEvent event) throws Exception {
        actiontarget.setText("Sign in button pressed");
        Node node=(Node) event.getSource();
        Stage stage=(Stage) node.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/rootView.fxml"));
        Scene scene = new Scene(root, 600,550);
        stage.setScene(scene);
        stage.show();
    }
}
