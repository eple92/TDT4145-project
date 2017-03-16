package controller;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Date;

import model.*;

public class DatabaseController {

		private Database db;
		private ControllerManager manager;
		private PreparedStatement pstmt;
		
		public DatabaseController(ControllerManager manager) {
			this.manager = manager;
		}

		public void makeNewConnection(){
            db = new Database();
        }

        public void createDatabase(){
		    db.recreateDB();
        }

        public void populateDatabase() {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
            List<String> exercises = Arrays.asList("Arm Circles", "Squat", "Ab roller", "Benchpress", "Running", "Jogging");

            try{
                insertIndoorSession(new Indoor(formatter.parse("12.12.15 12:00:00"), formatter.parse("12.12.15 13:00:00"), 8, 9, "Note", "", 0),exercises);
                insertIndoorSession(new Indoor(formatter.parse("11.12.15 12:00:00"),formatter.parse("121.12.15 13:00:00"),8, 9, "Note", "", 0),exercises);
                insertIndoorSession(new Indoor(formatter.parse("8.2.15 12:00:00"), formatter.parse("8.2.15 13:00:00"),8, 9, "Note", "", 0), exercises);
                insertIndoorSession(new Indoor(formatter.parse("4.8.15 12:00:00"), formatter.parse("4.8.15 12:00:00"),8, 9, "Note", "", 0), exercises);

                insertResults(new Results("Arm Circles", formatter.parse("12.12.15 12:00:00"), 80, 4, 4, 0, 0));
                insertResults(new Results("Squat", formatter.parse("12.12.15 12:00:00"), 70, 4, 4, 0, 0));
                insertResults(new Results("Ab roller", formatter.parse("12.12.15 12:00:00"), 10, 6, 4, 0, 0));
                insertResults(new Results("Squat", formatter.parse("12.12.15 12:00:00"), 90, 3, 2, 0, 0));
                insertResults(new Results("Benchpress", formatter.parse("12.12.15 12:00:00"), 30, 20, 4, 0, 0));

                insertResults(new Results("Running", formatter.parse("12.12.15 12:00:00"), 0, 0, 0, 12, 55));
                insertResults(new Results("Running", formatter.parse("11.12.15 12:00:00"), 0, 0, 0, 20, 60));
                insertResults(new Results("Jogging", formatter.parse("8.2.15 12:00:00"), 0, 0, 0, 3, 50));
                insertResults(new Results("Jogging", formatter.parse("4.8.15 12:00:00"), 0, 0, 0, 4, 20));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
		
		public void insertAction(String query) {
			db.noReturnAction(query);
			System.out.println("Inserted");
		}
				
		public void showTable(String q, String type) {
			ArrayList<String> result = db.select(q, type);
			for (int i = 0; i < result.size(); i++) {
				System.out.println(result.get(i) + "\n");
			}
		}

		public ArrayList<String> selectAction(String query, String type){
			return db.select(query, type);
		}

		private void insertSession(Session session, List<String> exercises) {
            db.connectToDB();
            Connection conn = db.getConnection();
		    String q = "INSERT INTO Session(startDateAndTime, endDateAndTime, inOrOut, personalShape, prestation, note) VALUES (?,?,?,?,?,?);";
		    try {
                pstmt = conn.prepareStatement(q);
                pstmt.setTimestamp(1, new Timestamp(session.getStartDate().getTime()));
                pstmt.setTimestamp(2, new Timestamp(session.getEndDate().getTime()));
                pstmt.setString(3, session.getInOrOut());
                pstmt.setInt(4, session.getPersonalShape());
                pstmt.setInt(5, session.getPrestation());
                pstmt.setString(6, session.getNote());
                pstmt.executeUpdate();
            } catch (SQLException se) {
		        se.printStackTrace();
            } finally {
                try{
                    if(this.pstmt != null)
                        this.pstmt.close();
                } catch(SQLException se){
                    se.printStackTrace();
                }
            }
            db.closeConnection();
		    insertSessionExercises(session, exercises);
        }

    private void insertSessionExercises(Session session, List<String> exercises) {
        db.connectToDB();
        Connection conn = db.getConnection();
        String q = "INSERT INTO SessionExercises(exerciseName, sessionStartDateAndTime) VALUES (?,?);";
        try {
            pstmt = conn.prepareStatement(q);
            pstmt.setTimestamp(2, new Timestamp(session.getStartDate().getTime()));
            for (String exercise: exercises) {
                pstmt.setString(1,exercise);
                pstmt.executeUpdate();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try{
                if(this.pstmt != null)
                    this.pstmt.close();
            } catch(SQLException se){
                se.printStackTrace();
            }
        }
        db.closeConnection();
    }

        public void insertIndoorSession(Indoor session, List<String> exercises){
            insertSession(session, exercises);
            db.connectToDB();
            Connection conn = db.getConnection();
            String q = "INSERT INTO indoorsession(indoorStartDateAndTime, aircondition, viewers) VALUES (?,?,?);";
            try {
                pstmt = conn.prepareStatement(q);
                pstmt.setTimestamp(1, new Timestamp(session.getStartDate().getTime()));
                pstmt.setString(2, session.getAircondition());
                pstmt.setInt(3,session.getViewers());
                pstmt.executeUpdate();
            } catch (SQLException se) {
                se.printStackTrace();
            } finally {
                try{
                    if(this.pstmt != null)
                        this.pstmt.close();
                } catch(SQLException se){
                    se.printStackTrace();
                }
            }
            db.closeConnection();

        }

    public void insertOutdoorSession(Outdoor session, List<String> exercises){
        insertSession(session, exercises);
        db.connectToDB();
        Connection conn = db.getConnection();
        String q = "INSERT INTO outdoorsession(outdoorStartDateAndTime, temperature, weather) VALUES (?,?,?);";
        try {
            pstmt = conn.prepareStatement(q);
            pstmt.setTimestamp(1, new Timestamp(session.getStartDate().getTime()));
            pstmt.setInt(2, session.getTemperature());
            pstmt.setString(3,session.getWeather());
            pstmt.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try{
                if(this.pstmt != null)
                    this.pstmt.close();
            } catch(SQLException se){
                se.printStackTrace();
            }
        }
        db.closeConnection();
    }



    public void insertResults(Results results){
        db.connectToDB();
        Connection conn = db.getConnection();
        String q = "INSERT INTO Results (exerciseName, sessionStartDateAndTime, weight, rep, exerciseSet, distance, duration) VALUES (?,?,?,?,?,?,?);";
        try {
            pstmt = conn.prepareStatement(q);
            pstmt.setString(1,results.getExerciseNameString());
            pstmt.setTimestamp(2, new Timestamp(results.getSessionStartDateAndTime().getTime()));
            pstmt.setInt(3, results.getWeight());
            pstmt.setInt(4,results.getRep());
            pstmt.setInt(5,results.getExerciseSet());
            pstmt.setInt(6,results.getDistance());
            pstmt.setInt(7,results.getDuration());
            pstmt.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try{
                if(this.pstmt != null)
                    this.pstmt.close();
            } catch(SQLException se){
                se.printStackTrace();
            }
        }
        db.closeConnection();
    }

    public ArrayList<Exercise> selectExercise(String exerciseName) {
        db.connectToDB();
        Connection conn = db.getConnection();
        ResultSet rs;
        ArrayList<Exercise> result = new ArrayList<>();
        String q = "SELECT * FROM Exercise WHERE exerciseName = ?;";
        try {
            pstmt = conn.prepareStatement(q);
            pstmt.setString(1,exerciseName);
            rs = pstmt.executeQuery();
            if (rs == null) {
                System.out.println("ERROR: RS is null");
                return null;
            } else {
                while(rs.next()) {
                    result.add(new Exercise(rs.getString("exerciseName"),rs.getString("description")));
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try{
                if(this.pstmt != null)
                    this.pstmt.close();
            } catch(SQLException se){
                se.printStackTrace();
            }
        }
        db.closeConnection();
        return result;
    }

    public List<String> selectAllExerciseNamesFromSession(Date startDateAndTime){
        db.connectToDB();
        Connection conn = db.getConnection();
        ResultSet rs;
        List<String > result = new ArrayList<>();
        String q = "SELECT exerciseName FROM SessionExercises WHERE sessionStartDateAndTime = ?";
        try {
            pstmt = conn.prepareStatement(q);
            pstmt.setTimestamp(1, new Timestamp(startDateAndTime.getTime()));
            rs = pstmt.executeQuery();
            if (rs == null) {
                System.out.println("ERROR: RS is null");
                return null;
            } else {
                while(rs.next()) {
                    result.add(rs.getString("exerciseName"));
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try{
                if(this.pstmt != null)
                    this.pstmt.close();
            } catch(SQLException se){
                se.printStackTrace();
            }
        }
        db.closeConnection();
        return result;
    }

    public ArrayList<Session> selectAllSessions() {
        db.connectToDB();
        Connection conn = db.getConnection();
        ResultSet rs;
        ArrayList<Session> result = new ArrayList<>();
        String q = "SELECT * FROM session left join indoorsession on startDateAndTime=indoorStartDateAndTime left join outdoorsession on startDateAndTime = outdoorStartDateAndTime;";
        try {
            pstmt = conn.prepareStatement(q);
            rs = pstmt.executeQuery();
            if (rs == null) {
                System.out.println("ERROR: RS is null");
                return null;
            } else {
                while(rs.next()) {
                    if (rs.getString("inOrOut").equals("in")) {
                        result.add(new Indoor(rs.getTimestamp("startDateAndTime"), rs.getTimestamp("endDateAndTime"), rs.getInt("personalShape"), rs.getInt("prestation"), rs.getString("note"), rs.getString("aircondition"), rs.getInt("viewers")));
                    } else {
                        result.add(new Outdoor(rs.getTimestamp("startDateAndTime"), rs.getTimestamp("endDateAndTime"), rs.getInt("personalShape"), rs.getInt("prestation"), rs.getString("note"), rs.getInt("temperature"), rs.getString("weather")));
                    }
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try{
                if(this.pstmt != null)
                    this.pstmt.close();
            } catch(SQLException se){
                se.printStackTrace();
            }
        }
        db.closeConnection();
        return result;
    }

    public ArrayList<Session> selectSession(Date startDateAndTime) {
        db.connectToDB();
        Connection conn = db.getConnection();
        ResultSet rs;
        ArrayList<Session> result = new ArrayList<>();
        String q = "SELECT * FROM session left join indoorsession on startDateAndTime=indoorStartDateAndTime left join outdoorsession on startDateAndTime = outdoorStartDateAndTime WHERE startDateAndTime = ?;";
        try {
            pstmt = conn.prepareStatement(q);
            pstmt.setTimestamp(1, new Timestamp(startDateAndTime.getTime()));
            rs = pstmt.executeQuery();
            if (rs == null) {
                System.out.println("ERROR: RS is null");
                return null;
            } else {
                while(rs.next()) {
                    if (rs.getString("inOrOut").equals("in")) {
                        result.add(new Indoor(rs.getTimestamp("startDateAndTime"), rs.getTimestamp("endDateAndTime"), rs.getInt("personalShape"), rs.getInt("prestation"), rs.getString("note"), rs.getString("aircondition"), rs.getInt("viewers")));
                    } else {
                        result.add(new Outdoor(rs.getTimestamp("startDateAndTime"), rs.getTimestamp("endDateAndTime"), rs.getInt("personalShape"), rs.getInt("prestation"), rs.getString("note"), rs.getInt("temperature"), rs.getString("weather")));
                    }
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try{
                if(this.pstmt != null)
                    this.pstmt.close();
            } catch(SQLException se){
                se.printStackTrace();
            }
        }
        db.closeConnection();
        return result;
    }
}
