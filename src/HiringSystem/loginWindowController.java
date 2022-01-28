package HiringSystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.ini4j.Ini;

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
            // correct password
            Parent root = FXMLLoader.load(getClass().getResource("ApplicantViewer.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Applicant viewer");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        }
        else {
            // incorrect password
            Alert a = new Alert(Alert.AlertType.ERROR,"Username or password is invalid.");
            a.setTitle("Error - invalid password");
            a.showAndWait();
            return false;
        }
        return false;
    }

}
