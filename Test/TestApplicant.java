import org.junit.Assert;
import org.junit.Test;
import org.mockito.cglib.core.Local;
import sample.Applicant;

import java.sql.SQLException;
import java.time.LocalDate;

public class TestApplicant {

    private final Applicant testApplicant = new Applicant(1, "Adam", "Caruana",
            "UK", "test", "new", LocalDate.parse("2004-03-01"));

    @Test
    public void testGetYearsOld(){
        Assert.assertEquals(testApplicant.getYearsOld(), 17);
    }
    @Test
    public void testGetForename(){
        Assert.assertEquals(testApplicant.getForename(), "Adam");
    }
    @Test
    public void testGetSurname(){
        Assert.assertEquals(testApplicant.getSurname(), "Caruana");
    }
    @Test
    public void testGetFullName(){
        Assert.assertEquals(testApplicant.getFullName(), "Adam Caruana");
    }
    @Test
    public void testGetCountry(){
        Assert.assertEquals(testApplicant.getCountry(), "UK");
    }
    @Test
    public void testGetAbout(){
        Assert.assertEquals(testApplicant.getAbout(), "test");
    }
    @Test
    public void testGetStatus(){
        Assert.assertEquals(testApplicant.getStatus(), "new");
    }
    @Test
    public void testGetDOB(){
        LocalDate expectedDOB = LocalDate.parse("2004-03-01");
        Assert.assertEquals(testApplicant.getDOB(), expectedDOB);
    }
    @Test
    public void testUpdateStatusInvalid() throws Exception {
        // first make sure we have a new applicant
        Applicant applicantToUpdate = new Applicant(1, "Adam", "Caruana",
                "UK", "test", "new", LocalDate.parse("2004-03-01"));
        Assert.assertEquals(applicantToUpdate.getStatus(), "new");

        // run an update on the same applicant
        try{
            applicantToUpdate.updateStatus("invalid string");
        }
        catch (Exception e){
            // pass test if we get the expected exception
            if (e.getMessage().equals("entered status is not valid.")){
                Assert.assertFalse(false);
            }
            else {
                throw e;
            }
        }
    }

    @Test
    public void testUpdateStatusValid() throws Exception {
        // first make sure we have a new applicant
        Applicant applicantToUpdate = new Applicant(1, "Adam", "Caruana",
                "UK", "test", "new", LocalDate.parse("2004-03-01"));
        Assert.assertEquals(applicantToUpdate.getStatus(), "new");

        // run an update on the same applicant
        applicantToUpdate.updateStatus("accepted");
        Assert.assertEquals(applicantToUpdate.getStatus(), "accepted");
    }

}
