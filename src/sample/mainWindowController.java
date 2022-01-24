package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class mainWindowController {
    @FXML
    private void staffLogin(){
//        open loginWindow stage
        System.out.println("opening staff login page");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("LoginWindow.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Staff login portal");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
