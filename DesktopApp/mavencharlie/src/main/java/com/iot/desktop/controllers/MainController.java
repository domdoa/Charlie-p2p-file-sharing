package com.iot.desktop.controllers;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import okhttp3.*;

public class MainController {
    public String JWTToken;
    private String serverURL = "http://localhost:8080/login";
    private String contentType = "application/json";
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Text actionTarget;

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) throws Exception {

        String username = usernameField.getText();
        String password = passwordField.getText();
        String body = "{\"email\"" + ":" + "\"" + username + "\"," + "\"password\": " + "\"" + password + "\"" + "}";

        Request request = new Request.Builder()
                .url(serverURL)
                .addHeader("Content-Type", contentType)
                .post(okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body))
                .build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        Response response = call.execute();
        Gson gson = new Gson();
        ResponseBody responseBody = response.body();

        JWTToken = responseBody.string();



        actionTarget.setText("Sign in button pressed");
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/rootView.fxml"));
        Scene scene = new Scene(root, 600, 550);
        stage.setScene(scene);
        stage.show();
    }
}