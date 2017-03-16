package model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class Database {

	// Local variables
	private Connection conn;
	private PreparedStatement pstmt;
	
	// JDBC driver name and database URL
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private String HOST_URL = "jdbc:mysql://localhost/";

    private String suppressSSLWarnings = "?useSSL=false";

	// Database credentials
	private String USER = "user";
	private String PASS = "password";

	private String dbName = "workoutdiary";

	// Create a database object
	public Database() {
        readConfig();
		try {
			// Register JDBC driver in program 
			Class.forName(JDBC_DRIVER).newInstance();
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR: can't find MySQL JDBC Driver");
			e.printStackTrace();
		} catch (InstantiationException | IllegalAccessException ie){
		    ie.printStackTrace();
        }
    }

	public Connection getConnection(){
	    return conn;
    }

    public void closeConnection(){
        try {
            if(conn != null) {
                conn.close();
            }
        } catch(SQLException se){
            se.printStackTrace();
        }
    }

    private void connectToHost(){
	    try{
            conn = DriverManager.getConnection(HOST_URL + suppressSSLWarnings, USER, PASS);
        } catch (SQLException se){
	        System.out.println("Couldn't connect to db. Check your settings.");
	        //se.printStackTrace();
        }
    }

    public  void connectToDB(){
        try{
            conn = DriverManager.getConnection(HOST_URL + dbName + suppressSSLWarnings, USER, PASS);
        } catch (SQLException se){
            System.out.println("Couldn't connect to db. Check your settings.");
            //se.printStackTrace();
        }
    }

    public void recreateDB(){
        connectToHost();
        if (conn != null){
            try{
                System.out.println("Creating the database");
                String dropQuery = "DROP DATABASE IF EXISTS " + dbName + ";";
                pstmt = conn.prepareStatement(dropQuery);
                pstmt.executeUpdate();
                String createQuery = "CREATE DATABASE IF NOT EXISTS " + dbName + ";";
                pstmt = conn.prepareStatement(createQuery);
                pstmt.executeUpdate();
                connectToDB();
                createTables();
                addExercisesToDb();
            } catch (SQLException se){
                se.printStackTrace();
            } finally {
                try{
                    if(this.pstmt != null)
                        this.pstmt.close();
                } catch(SQLException se){
                    se.printStackTrace();
                }
                closeConnection();
            }
        }
    }

    private void readConfig() {
        Properties prop = new Properties();
        InputStream input = null;
        try {

            input = new FileInputStream("resources/config.properties");

            prop.load(input);
            dbName = prop.getProperty("dbname");
            HOST_URL = prop.getProperty("hosturl");
            USER = prop.getProperty("dbuser");
            PASS = prop.getProperty("dbpass");


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

	private void createTables(){
		try{

            String query = "CREATE TABLE IF NOT EXISTS Session (" +
                    "    startDateAndTime DATETIME NOT NULL PRIMARY KEY," +
                    "    endDateAndTime DATETIME NOT NULL UNIQUE," +
                    "    inOrOut VARCHAR(3)," +
                    "    personalShape INT(1)," +
                    "    prestation INT(1)," +
                    "    note VARCHAR(255)" +
                    ");";

            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();

            query = "CREATE TABLE IF NOT EXISTS OutdoorSession (" +
                    "    outdoorStartDateAndTime DATETIME NOT NULL PRIMARY KEY,\n" +
                    "    weather VARCHAR(20)," +
                    "    temperature INT(2)," +
                    "    FOREIGN KEY (outdoorStartDateAndTime)" +
                    "    REFERENCES Session (startDateAndTime)" +
                    "        ON DELETE CASCADE" +
                    "        ON UPDATE CASCADE" +
                    ");";

            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();

            query = "CREATE TABLE IF NOT EXISTS IndoorSession (" +
                    "    indoorStartDateAndTime DATETIME NOT NULL PRIMARY KEY," +
                    "    aircondition VARCHAR(20)," +
                    "    viewers INT(3)," +
                    "    FOREIGN KEY (indoorStartDateAndTime)" +
                    "        REFERENCES Session (startDateAndTime)" +
                    "        ON DELETE CASCADE" +
                    "        ON UPDATE CASCADE" +
                    ");";

            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();

            query = "CREATE TABLE IF NOT EXISTS Exercise (" +
                    "    exerciseName VARCHAR(20) NOT NULL PRIMARY KEY," +
                    "    description TEXT" +
                    ");";

            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();

            query = "CREATE TABLE IF NOT EXISTS Alternatives (" +
                    "    exercise VARCHAR(20) NOT NULL," +
                    "    alternative VARCHAR(20) NOT NULL," +
                    "    FOREIGN KEY (exercise)" +
                    "        REFERENCES Exercise (exerciseName)" +
                    "        ON DELETE CASCADE" +
                    "        ON UPDATE CASCADE," +
                    "    FOREIGN KEY (alternative)" +
                    "        REFERENCES Exercise (exerciseName)" +
                    "        ON DELETE CASCADE" +
                    "        ON UPDATE CASCADE," +
                    "    PRIMARY KEY (exercise , alternative)" +
                    ");";

            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();

            query = "CREATE TABLE IF NOT EXISTS SessionExercises (" +
                    "    exerciseName VARCHAR(20) NOT NULL," +
                    "    sessionStartDateAndTime DATETIME NOT NULL," +
                    "    FOREIGN KEY (exerciseName)" +
                    "        REFERENCES Exercise (exerciseName)" +
                    "        ON DELETE CASCADE" +
                    "        ON UPDATE CASCADE," +
                    "    FOREIGN KEY (sessionStartDateAndTime)" +
                    "        REFERENCES Session (startDateAndTime)" +
                    "        ON DELETE RESTRICT" +
                    "        ON UPDATE CASCADE," +
                    "    PRIMARY KEY (exerciseName , sessionStartDateAndTime)" +
                    ");";

            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();

            query = "CREATE TABLE IF NOT EXISTS SessionDrafts (" +
                    "    draftName VARCHAR(20) NOT NULL PRIMARY KEY" +
                    ");";

            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();

            query = "CREATE TABLE IF NOT EXISTS ExercisesInDraft (" +
                    "    draftName VARCHAR(20) NOT NULL," +
                    "    exerciseName VARCHAR(20) NOT NULL," +
                    "    FOREIGN KEY (draftName)" +
                    "        REFERENCES SessionDrafts (draftName)" +
                    "        ON DELETE CASCADE" +
                    "        ON UPDATE CASCADE," +
                    "    FOREIGN KEY (exerciseName)" +
                    "        REFERENCES Exercise (exerciseName)" +
                    "        ON DELETE CASCADE" +
                    "        ON UPDATE CASCADE," +
                    "    PRIMARY KEY (draftName, exerciseName)" +
                    ");";

            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();

            query = "CREATE TABLE IF NOT EXISTS Results (" +
                    "    resultID INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "    exerciseName VARCHAR(20) NOT NULL," +
                    "    sessionStartDateAndTime DATETIME NOT NULL," +
                    "    weight INT(3)," +
                    "    rep INT(2)," +
                    "    exerciseSet INT(2)," +
                    "    distance INT(3)," +
                    "    duration INT(3)," +
                    "    FOREIGN KEY (exerciseName)" +
                    "        REFERENCES Exercise (exerciseName)" +
                    "        ON DELETE NO ACTION" +
                    "        ON UPDATE CASCADE," +
                    "    FOREIGN KEY (sessionStartDateAndTime)" +
                    "        REFERENCES Session (startDateAndTime)" +
                    "        ON DELETE RESTRICT" +
                    "        ON UPDATE CASCADE" +
                    ");";


            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();

            query = "CREATE TABLE IF NOT EXISTS Goals (" +
                    "    startDate DATETIME NOT NULL," +
                    "    endDate DATETIME NOT NULL," +
                    "    exerciseName VARCHAR(20) NOT NULL," +
                    "    weight INT(3)," +
                    "    rep INT(2)," +
                    "    exerciseSet INT(2)," +
                    "    distance INT(3)," +
                    "    duration INT(3)," +
                    "    FOREIGN KEY (exerciseName)" +
                    "        REFERENCES Exercise (exerciseName)" +
                    "        ON DELETE CASCADE" +
                    "        ON UPDATE CASCADE," +
                    "\tPRIMARY KEY (startDate, endDate, exerciseName)" +
                    ");";

            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();

            query = "CREATE TABLE IF NOT EXISTS Groups (" +
                    "\tgroupName VARCHAR(20) PRIMARY KEY" +
                    ");";

            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();

            query = "CREATE TABLE IF NOT EXISTS GroupsInGroups (" +
                    "    parentGroup VARCHAR(20) NOT NULL," +
                    "    childGroup VARCHAR(20) NOT NULL," +
                    "    FOREIGN KEY (parentGroup)" +
                    "        REFERENCES Groups (groupName)" +
                    "        ON DELETE CASCADE" +
                    "        ON UPDATE CASCADE," +
                    "    FOREIGN KEY (childGroup)" +
                    "        REFERENCES Groups (groupName)" +
                    "        ON DELETE CASCADE" +
                    "        ON UPDATE CASCADE," +
                    "    PRIMARY KEY (parentGroup , childGroup)" +
                    ");";

            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();

            query = "CREATE TABLE IF NOT EXISTS ExercisesInGroups (" +
                    "    parentGroup VARCHAR(20) NOT NULL," +
                    "    exercise VARCHAR(20) NOT NULL," +
                    "    FOREIGN KEY (parentGroup)" +
                    "        REFERENCES Groups (groupName)" +
                    "        ON DELETE CASCADE" +
                    "        ON UPDATE CASCADE," +
                    "    FOREIGN KEY (exercise)" +
                    "        REFERENCES Exercise (exerciseName)" +
                    "        ON DELETE CASCADE" +
                    "        ON UPDATE CASCADE," +
                    "    PRIMARY KEY (parentGroup , exercise)" +
                    ");";

            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();

            query = "CREATE TABLE IF NOT EXISTS PulsAndGPS (" +
                    "    dateAndTime DATETIME NOT NULL PRIMARY KEY," +
                    "    puls INT(3)," +
                    "    longitude INT(3)," +
                    "    latitude INT(3)," +
                    "    metersAboveOcean INT(4)," +
                    "    results_fk INT UNSIGNED NOT NULL," +
                    "    FOREIGN KEY (results_fk)" +
                    "        REFERENCES Results (resultID)" +
                    "        ON DELETE CASCADE" +
                    "        ON UPDATE CASCADE" +
                    ");";

            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
		} catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}
	}

	private void addExercisesToDb(){

	    try {
	        pstmt = conn.prepareStatement("INSERT INTO Exercise(exerciseName, description) VALUES(?,?);");
	        pstmt.setString(1,"Running");
	        pstmt.setString(2, "Run");
	        pstmt.executeUpdate();
            pstmt.setString(1,"Rowing");
            pstmt.setString(2, "Move oars in half circles, to propel boat forward.");
            pstmt.executeUpdate();
            pstmt.setString(1,"Jogging");
            pstmt.setString(2, "Jog");
            pstmt.executeUpdate();
            pstmt.setString(1,"Jump Rope");
            pstmt.setString(2, "Swing rope around yourself and jump over it when it reaches feet.");
            pstmt.executeUpdate();
            pstmt.setString(1,"Squat");
            pstmt.setString(2, "Squatting");
            pstmt.executeUpdate();
            pstmt.setString(1,"Benchpress");
            pstmt.setString(2, "Press");
            pstmt.executeUpdate();
            pstmt.setString(1,"Arm Circles");
            pstmt.setString(2, "Description");
            pstmt.executeUpdate();
            pstmt.setString(1,"Ab roller");
            pstmt.setString(2, "Belly");
            pstmt.executeUpdate();

        } catch (SQLException se) {
	        se.printStackTrace();
        }
	}
}
