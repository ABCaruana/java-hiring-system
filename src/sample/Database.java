package sample;

import org.ini4j.Ini;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;

public class Database {
    private static Connection conn;

    public static void main(String[] args) throws SQLException {
        conn = connectToDatabase();
//        addApplicantToDB("adam", "caruana", "UK", "test", "new", LocalDate.parse("2004-03-01"));
//        updateStatus(3, "accepted");
        System.out.println(getApplicantByID(3).getYearsOld());
    }

    public static void updateStatus(int applicantID, String status) throws Exception {
        String[] acceptableStatuses = new String[] {"accepted", "declined", "new"};
        for (String acceptableStatus : acceptableStatuses) {
            if (acceptableStatus.equalsIgnoreCase(status)) {
                String updateQuery = String.format("update applicants set application_status='%s' where applicantID=%s", status.toLowerCase(), applicantID);
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(updateQuery);
                return;
            }
        }
        throw new Exception("entered status is not valid.");
    }
    public static Applicant getApplicantByID(int applicantID) throws SQLException {
        Statement getApplicantQuery = conn.createStatement();
        ResultSet queryOutput = getApplicantQuery.executeQuery("SELECT * FROM applicants WHERE applicantID = " + applicantID);

        queryOutput.next();

        String forename = queryOutput.getString("forename");
        String surname = queryOutput.getString("surname");
        String country = queryOutput.getString("country");
        String about = queryOutput.getString("about");
        String status = queryOutput.getString("application_status");

        // parse our date of birth to a LocalDate
        String dateOfBirth = queryOutput.getString("date_of_birth");
        LocalDate parsedDOB = LocalDate.parse(dateOfBirth);

        return new Applicant(applicantID, forename, surname, country, about, status, parsedDOB);
    }
    public static void addApplicantToDB(String forename, String surname, String country,
                                 String about, String status, LocalDate dateOfBirth) throws SQLException {
        Statement stmt = conn.createStatement();
        String insertStmt = String.format("INSERT INTO `applicants` VALUES (NULL, '%s', '%s', '%s', '%s', '%s', '%s')",
                forename, surname, dateOfBirth, country, about, "new");
        System.out.println(insertStmt);
        stmt.executeUpdate(insertStmt);
    }
    public static Connection connectToDatabase(){
        Connection conn = null;

        try {
            HashMap<String, String> connectionDetails = getConnectionDetails();
            String host = connectionDetails.get("host");
            String port = connectionDetails.get("port");
            String username = connectionDetails.get("username");
            String password = connectionDetails.get("password");

            String url = String.format("jdbc:mysql://%s:%s", host, port);
            System.out.println("Going to try to connect to " + url);
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("successfully connected to database!");
            initialiseDB(conn);
            return conn;
        }
        catch (SQLException | IOException e) {
            throw new Error("Problem connecting to the database: ", e);
        }
    }
    public static HashMap<String, String> getConnectionDetails() throws IOException {
        try {
            Ini ini = new Ini(new FileReader("config.ini"));
            Ini.Section connectionSection = ini.get("database");
            String host = connectionSection.get("host");
            String port = connectionSection.get("port");
            String username = connectionSection.get("username");
            String password = connectionSection.get("password");

            return new HashMap<>() {{
                put("host", host);
                put("port", port);
                put("username", username);
                put("password", password);
            }};
        }
        catch (IOException e){
            System.out.println("unable to get connection details");
            e.printStackTrace();
            return null;
        }
    }
    public static void initialiseDB(Connection conn){
        try {
            Statement statement = conn.createStatement();
            int myResult = statement.executeUpdate("CREATE DATABASE IF NOT EXISTS hiring_system");

            // set the hiring system as our active database.
            conn.setCatalog("hiring_system");

            String createTable = "CREATE TABLE IF NOT EXISTS applicants"
                + "(applicantID INT AUTO_INCREMENT PRIMARY KEY,"
                + "forename VARCHAR(50) NOT NULL,"
                + "surname VARCHAR(50) NOT NULL,"
                + "date_of_birth DATE NOT NULL,"
                + "country VARCHAR(50) NOT NULL,"
                + "about TEXT NOT NULL,"
                + "application_status VARCHAR(20))";

            Statement stmt = conn.createStatement();
            stmt.execute(createTable);
            System.out.println("database initialised.");
        }
        catch (SQLException e){
            System.out.println("unable to initialise database: " + e);
        }
    }
}
