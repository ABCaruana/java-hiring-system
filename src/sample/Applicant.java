package sample;

import com.mysql.cj.util.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoUnit.DAYS;

public class Applicant {
    private int applicantID;
    private String forename;
    private String surname;
    private String country;
    private String about;
    private String status;
    private LocalDate dateOfBirth;

    public Applicant(int applicantID, String forename, String surname, String country,
                     String about, String status, LocalDate dateOfBirth){
        this.applicantID = applicantID;
        this.forename = forename;
        this.surname = surname;
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
    public LocalDate getDOB(){
        return dateOfBirth;
    }

}
