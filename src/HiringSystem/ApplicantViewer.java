package HiringSystem;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;

public class ApplicantViewer {
    @FXML
    GridPane applicantGrid;


    public void addImage(int applicantID, int row, int col){
        HBox imageBox = new HBox();
        imageBox.setAlignment(Pos.CENTER);
        Image image = new Image(String.format("File:pictures/%s.png", applicantID));
        ImageView applicantImage = new ImageView(image);
        applicantImage.setFitWidth(100);
        applicantImage.setFitHeight(100);
        imageBox.getChildren().add(applicantImage);
        applicantGrid.add(imageBox, col, row);
    }

    public void addDetails(Applicant applicant, int row, int col){
        VBox detailBox = new VBox();
        detailBox.setAlignment(Pos.CENTER_LEFT);
        applicantGrid.add(detailBox, col, row);
        Label namelabel = new Label(String.format("Name: %s", applicant.getFullName()));
        Label genderLabel = new Label(String.format("Gender: %s", applicant.getGender()));
        Label dobLabel = new Label(String.format("date of birth: %s (%s)", applicant.getDOB(), applicant.getYearsOld()));
        Label countryLabel = new Label(String.format("Country: %s", applicant.getCountry()));
        detailBox.getChildren().addAll(namelabel, genderLabel, dobLabel, countryLabel);
    }

    public void addButton(Applicant applicant, int row, int col){
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        Button viewApplicantButton = new Button("View");

        // open a window with more detailed information about a given applicant
        viewApplicantButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                System.out.println(applicant.getApplicantID());
            }
        });

        buttonBox.getChildren().add(viewApplicantButton);
        applicantGrid.add(buttonBox, col, row);
    }

    public void addRow(Applicant applicant){
        int applicantID = applicant.getApplicantID();

        // add a new row to the gridpane with applicants' details
        System.out.println("adding row " + applicantGrid.getRowCount());
        RowConstraints row = new RowConstraints(120);
        applicantGrid.getRowConstraints().add(row);

        // the row number to add information to
        int applicantRow = applicantGrid.getRowCount() - 1;

        // add image
        addImage(applicantID, applicantRow, 0);

        // add details
        addDetails(applicant, applicantRow, 1);

        // add button to open up detailed view of applicant
        addButton(applicant, applicantRow, 2);

    }

    @FXML
    void initialize() throws SQLException {
        // get data of all applicants
        ArrayList<Applicant> fullQuery = Database.getAllApplicants();

        // start adding our rows
        for (Applicant applicant : fullQuery){
            System.out.println(applicant.getForename());
            addRow(applicant);
        }
    }
}
