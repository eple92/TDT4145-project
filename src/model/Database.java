package model;

import main.MainApp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Properties;

public class Database {

	// Local variables
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String HOST_URL = "jdbc:mysql://localhost/";
	private String DB_URL = "jdbc:mysql://localhost/";

	// Database credentials
	private String USER = "user";
	private String PASS = "password";

	private final String dbName = "workoutdiary";

	// Create a database object
	public Database() {

        Properties prop = new Properties();
        InputStream input = null;
        System.out.println(System.getProperty("user.dir"));

        try {

            input = new FileInputStream("resources/config.properties");

            prop.load(input);

            DB_URL = prop.getProperty("dburl");
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

		try {

			try {
				// Register JDBC driver in program 
				Class.forName(JDBC_DRIVER).newInstance();
			} catch (ClassNotFoundException e) {
				System.out.println("ERROR: can't find MySQL JDBC Driver");
				e.printStackTrace();
				return;
			}
		
			// Open a connection
			System.out.println("Opening a connection");
			conn = DriverManager.getConnection(HOST_URL, USER, PASS);

            System.out.println("Creating the database");
            String dropQuery = "DROP DATABASE IF EXISTS " + dbName + ";";
            pstmt = conn.prepareStatement(dropQuery);
            pstmt.executeUpdate();
            String createQuery = "CREATE DATABASE IF NOT EXISTS " + dbName + ";";
            pstmt = conn.prepareStatement(createQuery);
            pstmt.executeUpdate();

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            createTables();

            populateDatabase();
		
		} catch(SQLException se){
			//Handle errors for DriverManager.getConnection() (JDBC)
			se.printStackTrace();
		} catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			try{
				if(this.pstmt != null)
					this.pstmt.close();
			} catch(SQLException se2){
				// Nothing to do
			}
			try {
				if(conn != null) {
		            conn.close();
				}
			} catch(SQLException se){
		         se.printStackTrace();
			}
		}
	}
	
	// For update, drop, create and delete
	public void noReturnAction(String query) {
		try {
			// Open a connection
		    conn = DriverManager.getConnection(DB_URL, USER, PASS);
		    
		    pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		    
		    pstmt.executeUpdate(query);
			
		    if (rs != null) {
		    	rs.close();
		    }			
	   } catch(SQLException se){
	      //Handle errors for JDBC
	      se.printStackTrace();
	   } catch(Exception e){
	      //Handle errors for Class.forName
	      e.printStackTrace();
	   } finally{
	      //finally block used to close resources
	      try {
	         if(pstmt!=null)
	            conn.close();
	      }catch(SQLException se){
	      }// do nothing
	      try {
	         if(conn!=null)
	            conn.close();
	      } catch(SQLException se){
	         se.printStackTrace();
	      }
	   }
	}
	
	// For select
	public ArrayList<String> select(String query, String type) {
		System.out.println("Query in select: " + query);
		ArrayList<String> result = new ArrayList<>();
		
		try {
			// Open a connection
		    conn = DriverManager.getConnection(DB_URL, USER, PASS);
		    
		    // Create a statement
	    	pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	    
			rs = pstmt.executeQuery(query);
			result = rsToList(rs, type);
			
		    if (rs != null) {
		    	rs.close();
		    }			
	   } catch(SQLException se){
	      //Handle errors for JDBC
	      se.printStackTrace();
	   } catch(Exception e){
	      //Handle errors for Class.forName
	      e.printStackTrace();
	   } finally{
	      //finally block used to close resources
	      try {
	         if(pstmt!=null)
	            conn.close();
	      }catch(SQLException se){
	      }// do nothing
	      try {
	         if(conn!=null)
	            conn.close();
	      } catch(SQLException se){
	         se.printStackTrace();
	      }
	   }
		return result;
	}
	
	private ArrayList<String> rsToList(ResultSet rs, String type) throws SQLException {
		ArrayList<String> result = new ArrayList<>();
	
		if (rs == null) {
			System.out.println("ERROR: RS is null");
			return null;
		}
		rs.last();
		rs.beforeFirst();
		while (rs.next()) {
			if (type == "session") {
				String session = rs.getDate("startDateAndTime") + " to " + rs.getDate("endDateAndTime") + 
						". Personalshape: " + rs.getInt("personalShape") + ". Prestation: " + rs.getInt("prestation") +
						". Note: " + rs.getString("note");
				result.add(session);
			} else if (type == "exercise") {
				// TODO: missing
				String exercise = rs.getString("exerciseName") + " Description: " + rs.getString("description");
				result.add(exercise);
			} else if (type == "inResults") {
				String inResult = rs.getDate("sessionStartDateAndTime") + ": " + rs.getString("exerciseName") + " - " + rs.getInt("weight") + "kg";
				result.add(inResult);
			} else if (type == "outResults") {
				String outResult = rs.getDate("sessionStartDateAndTime") + ": " + rs.getString("exerciseName") + " - " + rs.getInt("distance") + "km";
				result.add(outResult);
			}else if (type == "results") {
				String results = rs.getDate("sessionStartDateAndTime") + ": " + rs.getString("exerciseName");
				result.add(results);
			}
		}
		return result;
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
	
	@SuppressWarnings("deprecation")
	public void populateDatabase() {
		noReturnAction(new Exercise("Ab Roller", "1. Hold the Ab Roller with both hands and kneel" +
                " on the floor.\n" +
				"2. Now place the ab roller on the floor in front of you so that you are on all your hands and knees" +
                " (as in a kneeling push up position). This will be your starting position.\n" +
				"3. Slowly roll the ab roller straight forward, stretching your body into a straight position. Tip:" +
                " Go down as far as you can without touching the floor with your body. Breathe in during this portion" +
                " of the movement.\n" +
				"4. After a pause at the stretched position, start pulling yourself back to the starting position as" +
                " you breathe out. Tip: Go slowly and keep your abs tight at all times.\n" +
				"Caution: This exercise is not advised for people with lower back problems or hernias.\n" +
				"\n" +
				"Variations: If you are advanced you can perform the exercise moving the ab roller to the sides in a" +
                " diagonal fashion as opposed to straight forward. This version places more emphasis on the obliques.").getInstertQuery());
        noReturnAction(new Exercise("Arm Circles", "Stand up and extend your arms straight out by" +
                " the sides. The arms should be parallel to the floor and perpendicular (90-degree angle) to your" +
                " torso. This will be your starting position.\n" +
                "Slowly start to make circles of about 1 foot in diameter with each outstretched arm. Breathe" +
                " normally as you perform the movement.\n" +
                "Continue the circular motion of the outstretched arms for about ten seconds. Then reverse the" +
                " movement, going the opposite direction.\n" +
                "Tip: Ten second movements equal one set and each circle equals one repetition.\n" +
                "\n" +
                "Variations: As you get stronger you can use some light resistance.").getInstertQuery());
        noReturnAction(new Exercise("Running", "Run").getInstertQuery());
        noReturnAction(new Exercise("Rowing", "Move oars in half circles, to propel boat forward.").getInstertQuery());
        noReturnAction(new Exercise("Jogging", "Jog").getInstertQuery());
        noReturnAction(new Exercise("Jump Rope", "Swing rope around yourself and jump over it when it reaches feet.").getInstertQuery());
	
        noReturnAction(new Exercise("Squat", "Squatting").getInstertQuery());
        noReturnAction(new Exercise("Benchpress", "Press").getInstertQuery());
        noReturnAction(new Exercise("Arm Circles", "Description").getInstertQuery());
        noReturnAction(new Exercise("Ab roller", "Belly").getInstertQuery());
        
        
        noReturnAction(new Session(new Date(12, 12, 2015), new Date(12, 12, 2015), 8, 9, "Note").getInsertQuery());
        noReturnAction(new Session(new Date(11, 12, 2015), new Date(11, 12, 2015), 8, 9, "Note").getInsertQuery());
        noReturnAction(new Session(new Date(8, 2, 2015), new Date(8, 2, 2015), 4, 5, "Note").getInsertQuery());
        noReturnAction(new Session(new Date(4, 8, 2015), new Date(4, 8, 2015), 4, 5, "Note").getInsertQuery());

        noReturnAction(new Results("Arm Circles", new Date(12, 12, 2015), 80, 4, 4, 0, 0).getInsertQuery());
        noReturnAction(new Results("Squat", new Date(12, 12, 2015), 70, 4, 4, 0, 0).getInsertQuery());
        noReturnAction(new Results("Ab roller", new Date(12, 12, 2015), 10, 6, 4, 0, 0).getInsertQuery());
        noReturnAction(new Results("Squat", new Date(12, 12, 2015), 90, 3, 2, 0, 0).getInsertQuery());
        noReturnAction(new Results("Benchpress", new Date(12, 12, 2015), 30, 20, 4, 0, 0).getInsertQuery());
        
        noReturnAction(new Results("Running", new Date(12, 12, 2015), 0, 0, 0, 12, 55).getInsertQuery());
        noReturnAction(new Results("Running", new Date(11, 12, 2015), 0, 0, 0, 20, 60).getInsertQuery());
        noReturnAction(new Results("Jogging", new Date(8, 2, 2015), 0, 0, 0, 3, 50).getInsertQuery());
        noReturnAction(new Results("Jogging", new Date(4, 8, 2015), 0, 0, 0, 4, 20).getInsertQuery());
	}

	private void addExercisesToDb(){

		Exercise abRoller = new Exercise("Ab Roller", "1. Hold the Ab Roller with both hands and kneel" +
				" on the floor.\n" +
				"2. Now place the ab roller on the floor in front of you so that you are on all your hands and knees" +
				" (as in a kneeling push up position). This will be your starting position.\n" +
				"3. Slowly roll the ab roller straight forward, stretching your body into a straight position. Tip:" +
				" Go down as far as you can without touching the floor with your body. Breathe in during this portion" +
				" of the movement.\n" +
				"4. After a pause at the stretched position, start pulling yourself back to the starting position as" +
				" you breathe out. Tip: Go slowly and keep your abs tight at all times.\n" +
				"Caution: This exercise is not advised for people with lower back problems or hernias.\n" +
				"\n" +
				"Variations: If you are advanced you can perform the exercise moving the ab roller to the sides in a" +
				" diagonal fashion as opposed to straight forward. This version places more emphasis on the obliques.");

		if (select(abRoller.getSelectQuery(), "exercise") == null){
			noReturnAction(abRoller.getInstertQuery());
		}

		Exercise armCircles = new Exercise("Arm Circles", "Stand up and extend your arms straight out by" +
				" the sides. The arms should be parallel to the floor and perpendicular (90-degree angle) to your" +
				" torso. This will be your starting position.\n" +
				"Slowly start to make circles of about 1 foot in diameter with each outstretched arm. Breathe" +
				" normally as you perform the movement.\n" +
				"Continue the circular motion of the outstretched arms for about ten seconds. Then reverse the" +
				" movement, going the opposite direction.\n" +
				"Tip: Ten second movements equal one set and each circle equals one repetition.\n" +
				"\n" +
				"Variations: As you get stronger you can use some light resistance.");
		if (select(armCircles.getSelectQuery(), "exercise") == null){
			noReturnAction(armCircles.getInstertQuery());
		}
		Exercise running = new Exercise("Running", "Run");
		if (select(running.getSelectQuery(), "exercise") == null){
			noReturnAction(running.getInstertQuery());
		}
		Exercise rowing = new Exercise("Rowing", "Move oars in half circles, to propel boat forward.");
		if (select(rowing.getSelectQuery(), "exercise") == null){
			noReturnAction(rowing.getInstertQuery());
		}
		Exercise jogging = new Exercise("Jogging", "Jog");
		if (select(jogging.getSelectQuery(), "exercise") == null){
			noReturnAction(jogging.getInstertQuery());
		}
		Exercise jumpRope = new Exercise("Jump Rope", "Swing rope around yourself and jump over it when it reaches feet.");
		if (select(jumpRope.getSelectQuery(), "exercise") == null){
			noReturnAction(jumpRope.getInstertQuery());
		}
	}
}
