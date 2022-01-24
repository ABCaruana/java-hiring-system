import org.ini4j.Ini;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.HashMap;

public class Database {
    public static void main(String[] args) {
        connectToDatabase();
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
        finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            }
            catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
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
        // create a database if it doesnt exist
//        create database if not exists hiring_system;
//
//        EXAMPLE OF
//        CREATE TABLE `hiring_system`.applicants(
//                applicantID INT AUTO_INCREMENT PRIMARY KEY,
//                forename VARCHAR(50),
//                surname VARCHAR(50),
//                date_of_birth DATE,
//                country VARCHAR(50),
//                about TEXT,
//                application_status VARCHAR(20)
//        );
//
//        insert into `hiring_system`.applicants values(null, 'John', 'Smith', '2004-03-01', 'United Kingdom', 'seriously cool', 'ACCEPTED');
//
//        insert into `hiring_system`.applicants values(null, 'Ann', 'Smith', '2004-03-01', 'United States', 'seriously uncool', 'DECLINED')
        try {
            Statement statement = conn.createStatement();
            int myResult = statement.executeUpdate("CREATE DATABASE IF NOT EXISTS hiring_system");

            // set the hiring system as our active database.
            conn.setCatalog("hiring_system");

            String createTable = "CREATE TABLE IF NOT EXISTS applicants"
                + "(applicantID INT AUTO_INCREMENT PRIMARY KEY,"
                + "forename VARCHAR(50),"
                + "surname VARCHAR(50),"
                + "date_of_birth DATE,"
                + "country VARCHAR(50),"
                + "about TEXT,"
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
