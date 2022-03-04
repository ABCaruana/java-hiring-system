package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.ini4j.Ini;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class loginWindowController {
    @FXML
    TextField staffUsernameInput;
    @FXML
    PasswordField staffPasswordInput;

    @FXML
    private boolean checkCredentials() throws IOException {
        String inputtedUsername = staffUsernameInput.getText();
        String inputtedPassword = staffPasswordInput.getText();

        Ini passwordConfig = new Ini(new FileReader("config.ini"));
        Ini.Section connectionSection = passwordConfig.get("staff");
        String actualUsername = connectionSection.get("username");
        String actualPassword = connectionSection.get("password");

        System.out.println("comparing user input (" + inputtedUsername + ") to actual credential (" + actualUsername + ")");
        System.out.println("comparing user input (" + inputtedPassword + ") to actual credential (" + actualPassword + ")");

        if (inputtedUsername.equals(actualUsername) && inputtedPassword.equals(actualPassword)){
            return true;
        }
        else {
            Alert a = new Alert(Alert.AlertType.ERROR,"Username or password is invalid.");
            a.setTitle("Error - invalid password");
//            DialogPane dialogPane = a.getDialogPane();
//            dialogPane.getStylesheets().add(
//                    getClass().getResource("css/customDialogs.css").toExternalForm());
//            dialogPane.getStyleClass().add("customDialog");
            a.showAndWait();
            return false;
        }
    }

}
