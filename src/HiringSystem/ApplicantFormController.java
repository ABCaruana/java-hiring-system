package HiringSystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ApplicantFormController {
    @FXML
    ComboBox<Integer> dayDOB;
    @FXML
    ComboBox<Integer> monthDOB;
    @FXML
    ComboBox<Integer> yearDOB;
    @FXML
    ComboBox<String> countryDropdown;
    @FXML
    TextField forenameBox;
    @FXML
    TextField surnameBox;
    @FXML
    TextArea aboutTextarea;
    @FXML
    Button submitButton;
    @FXML
    Button cancelButton;
    @FXML
    ToggleGroup genderToggleGroup;
    @FXML
    Button imageUploadButton;
    @FXML
    ImageView imageBox;

    @FXML
    private void closeForm(){
        // get a handle to the stage
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void submitForm() throws SQLException {
        RadioButton selectedRadioButton = (RadioButton) genderToggleGroup.getSelectedToggle(); // gender toggle group
        String selectedGender = selectedRadioButton.getText(); // gender
        String forenameBoxText = toProperCase(sanitiseWord(forenameBox.getText()));  // forename
        String surnameBoxText = toProperCase(sanitiseWord(surnameBox.getText()));  // surname
        Integer dayDOBValue = dayDOB.getValue();  // day
        Integer monthDOBValue = monthDOB.getValue();  // month
        Integer yearDOBValue = yearDOB.getValue();  // year
        String countryText = countryDropdown.getValue(); // country
        String aboutText = aboutTextarea.getText();  // about

        // create a list of problematic fields to present to the user at the end, if applicable
        ArrayList<String> errorFields = checkErrors(forenameBoxText, surnameBoxText,
                dayDOBValue, monthDOBValue, yearDOBValue, countryText, aboutText);

        if (errorFields.size() >= 1) {
            // create a list of errors
            StringBuilder finalErrorMessage = new StringBuilder("There are problems with some information in your application. Please fix the following and try again:\n\n");
            for (String error : errorFields) {
                finalErrorMessage.append("- ").append(error).append("\n");
            }
            Alert a = new Alert(Alert.AlertType.INFORMATION, finalErrorMessage.toString());
            a.setTitle("Problems with application");
            a.showAndWait();
            return;
        }

        // now that validation is done we can parse the DOB
        LocalDate dateOfBirth = LocalDate.of(yearDOBValue, monthDOBValue, dayDOBValue);

        System.out.println("adding user to database");
        try {
            // add user to database and get back ID, to add to image name
            String result = Database.addApplicantToDB(forenameBoxText, surnameBoxText, countryText, aboutText, "new", dateOfBirth, selectedGender);

            Image profilePicture = imageBox.getImage();
            ImageIO.write(SwingFXUtils.fromFXImage(profilePicture, null), "png", new File("pictures/"+result+".png"));
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Thank you for submitting your information, " + forenameBoxText);
            a.setTitle("Complete");
            a.showAndWait();
            closeForm();
        }
        catch (Exception ex){
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Please upload an image");
            a.setTitle("Error uploading profile picture");
            a.showAndWait();
        }
    }

    @FXML
    public void uploadImage() throws FileNotFoundException {
        Stage stage = (Stage) imageUploadButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a profile picture");

        // prevent users from uploading anything but images
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        // convert the opened file into an image object
        File file = fileChooser.showOpenDialog(stage);
        Image image = new Image(new FileInputStream(file));
        imageBox.setImage(image);
    }

    @FXML
    void initialize(){
        // prepare all of our comboBox objects when the scene is called
        addDOBDays();
        addDOBMonths();
        addDOBYears();
        addCountries();
    }

    private void addDOBDays(){
        ObservableList<Integer> availableChoices = FXCollections.observableArrayList(
                1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31);
        dayDOB.setItems(availableChoices);
    }
    private void addDOBMonths(){
        ObservableList<Integer> availableChoices = FXCollections.observableArrayList(
                1,2,3,4,5,6,7,8,9,10,11,12);
        monthDOB.setItems(availableChoices);
    }
    private void addDOBYears(){
        ObservableList<Integer> availableChoices = FXCollections.observableArrayList();
        // list all years from current year minus 16 to current year minus 100
        for (int i = Calendar.getInstance().get(Calendar.YEAR) - 16; i >= Calendar.getInstance().get(Calendar.YEAR) - 100 ; i--)
            availableChoices.add(i);
        yearDOB.setItems(availableChoices);
    }
    private void addCountries(){
        // use system locale to get list of countries
        Locale[] allLocales = Locale.getAvailableLocales();
        ObservableList<String> availableChoices = FXCollections.observableArrayList();

        for (Locale locale : Locale.getAvailableLocales())
        {
            String countryToAdd = locale.getDisplayCountry();
            // ignore item if empty or a duplicate value
            if (!countryToAdd.equals("") && !availableChoices.contains(countryToAdd)){
                availableChoices.add(countryToAdd);
            }
        }

        //make list alphabetical for easier finding
        FXCollections.sort(availableChoices);
        countryDropdown.setItems(availableChoices);
    }
    private int getWordCount(String text){
        // split the text up by each space, return length of the list of words generated
        return text.split(" ").length;
    }
    private LocalDate validateDOB(int day, int month, int year){
        // checks that the date of birth is legitimate (not 31st feb, 31st november etc)
        LocalDate dateOfBirth;
        try {
            dateOfBirth = LocalDate.of(year, month, day);
        }
        catch (DateTimeException ex) {
            return null;
        }
        return dateOfBirth;
    }
    private ArrayList<String> checkErrors(String forenameBoxText, String surnameBoxText,
                                          Integer dayDOBValue, Integer monthDOBValue, Integer yearDOBValue,
                                          String countryText, String aboutText){
        ArrayList<String> errorFields = new ArrayList<>();

        if (forenameBoxText.equals("")) {
            errorFields.add("Forename");
        }
        if (surnameBoxText.equals("")) {
            errorFields.add("Surname");
        }
        if (countryText == null) {
            errorFields.add("Country of residence");
        }
        if (dayDOBValue == null || monthDOBValue == null || yearDOBValue == null) {
            // if not all fields are entered
            errorFields.add("Date of birth");
        }
        else if (validateDOB(dayDOBValue, monthDOBValue, yearDOBValue) == null){
            // if all fields are filled but the date is not possible
            errorFields.add("Date of birth");
        }
        if (aboutText.equals("")) {
            errorFields.add("About yourself");
        }
        else if (getWordCount(aboutText) > 1500){
            errorFields.add("About yourself (too many words, " + getWordCount(aboutText) + " > 1500)");
        }
        return errorFields;
    }
    private String toProperCase(String text){
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
    private String sanitiseWord(String text){
        // remove everything but upper and lowercase letters from string
        return text.replaceAll("[^a-zA-Z]","");
    }
}
