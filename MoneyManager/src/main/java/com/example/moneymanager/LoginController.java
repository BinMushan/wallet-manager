package com.example.moneymanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    protected void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Simple validation (no database)
        if (username.equals("Mushan") && password.equals("12345678")) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        HelloApplication.class.getResource("hello-view.fxml")
                );
                Scene scene = new Scene(loader.load());
                scene.getStylesheets().add(
                        HelloApplication.class.getResource("style.css").toExternalForm()
                );

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Wallet Management System");
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            messageLabel.setText("Invalid username or password");
        }
    }
}
