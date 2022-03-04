package HiringSystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class SingleApplicantViewer {
    private static Applicant singleApplicant;

    @FXML
    Label genderLabel;
    @FXML
    Label nameLabel;
    @FXML
    Label dobLabel;
    @FXML
    Label countryLabel;
    @FXML
    TextField genderBox;
    @FXML
    TextField nameBox;
    @FXML
    TextField dobBox;
    @FXML
    TextField countryBox;
    @FXML
    TextArea aboutBox;
    @FXML
    ImageView imageBox;
    @FXML
    Button acceptButton;
    @FXML
    Button declineButton;
    @FXML
    Button closeButton;

    public static void showApplicant(Applicant applicant) throws IOException {
        System.out.println("opening window for applicant " + applicant.getApplicantID());
        singleApplicant = applicant;
        openInitialWindow();
    }

    private static void openInitialWindow() throws IOException {
        Parent root = FXMLLoader.load(SingleApplicantViewer.class.getResource("SingleApplicantViewer.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Applicant viewer");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    private void setDetails(){
        genderBox.setText(singleApplicant.getGender());
        nameBox.setText(singleApplicant.getFullName());
        aboutBox.setText(singleApplicant.getAbout());
        countryBox.setText(singleApplicant.getCountry());
        dobBox.setText(singleApplicant.getDOB().toString());
        imageBox.setImage(singleApplicant.getApplicantImage());
    }

    @FXML
    private void closeForm(){
        // get a handle to the stage
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void acceptApplicant() throws Exception {
        singleApplicant.updateStatus("accepted");
        Alert a = new Alert(Alert.AlertType.INFORMATION, "Applicant has been accepted");
        a.showAndWait();
        closeForm();
    }

    @FXML
    private void declineApplicant() throws Exception {
        singleApplicant.updateStatus("declined");
        Alert a = new Alert(Alert.AlertType.INFORMATION, "Applicant has been declined");
        a.showAndWait();
        closeForm();
    }

    @FXML
    void initialize(){
        // pass all of our applicant object details into the window
        setDetails();
        if (singleApplicant.getStatus().equals("accepted")) {
            acceptButton.setDisable(true);
        }
        else if (singleApplicant.getStatus().equals("declined")) {
            declineButton.setDisable(true);
        }
    }
}
