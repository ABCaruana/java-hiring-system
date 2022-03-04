package HiringSystem;

import javafx.scene.image.Image;

import java.time.Clock;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class Applicant {
    private final int applicantID;
    private final String forename;
    private final String surname;
    private final String gender;
    private final String country;
    private final String about;
    private String status;
    private final LocalDate dateOfBirth;

    public Applicant(int applicantID, String forename, String surname, String gender, String country,
                     String about, String status, LocalDate dateOfBirth){
        this.applicantID = applicantID;
        this.forename = forename;
        this.surname = surname;
        this.gender = gender;
        this.country = country;
        this.about = about;
        this.status = status;
        this.dateOfBirth = dateOfBirth;
    }

    public int getYearsOld(){
        Clock clock = Clock.systemUTC();
        LocalDate currentDate = LocalDate.now(clock);
        long timeDifference = DAYS.between(dateOfBirth, currentDate);
        return (int) (Math.floor(timeDifference) / 365);
    }

    public void updateStatus(String status) throws Exception {
        try{
            Database.updateStatus(applicantID, status);
            this.status = status;
        }
        catch (Exception e){
            System.out.printf("Unable to update status for applicant %s%n", applicantID);
            e.printStackTrace();
            throw e;
        }
    }

    public Image getApplicantImage(){
        String imagePath = "pictures/" + getApplicantID() + ".png";
        return new Image("file:" + imagePath);
    }

    public int getApplicantID(){
        return applicantID;
    }
    public String getForename(){
        return forename;
    }
    public String getSurname(){
        return surname;
    }
    public String getFullName(){
        return forename + " " + surname;
    }
    public String getCountry(){
        return country;
    }
    public String getAbout(){
        return about;
    }
    public String getStatus(){
        return status;
    }
    public String getGender(){
        return gender;
    }
    public LocalDate getDOB(){
        return dateOfBirth;
    }

}
