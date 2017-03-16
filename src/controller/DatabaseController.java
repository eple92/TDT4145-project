package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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

            try{
                insertIndoorSession(new Indoor(formatter.parse("12.12.15 12:00:00"), formatter.parse("12.12.15 13:00:00"),8, 9, "Note", "", 0));
                insertIndoorSession(new Indoor(formatter.parse("11.12.15 12:00:00"),formatter.parse("121.12.15 13:00:00"),8, 9, "Note", "", 0));
                insertIndoorSession(new Indoor(formatter.parse("8.2.15 12:00:00"), formatter.parse("8.2.15 13:00:00"),8, 9, "Note", "", 0));
                insertIndoorSession(new Indoor(formatter.parse("4.8.15 12:00:00"), formatter.parse("4.8.15 12:00:00"),8, 9, "Note", "", 0));

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

		private void insertSession(Session session, Connection conn) {
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
        }

        public void insertIndoorSession(Indoor session){
            db.connectToDB();
            Connection conn = db.getConnection();
            insertSession(session, conn);
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

    public void insertOutdoorSession(Outdoor session){
        db.connectToDB();
        Connection conn = db.getConnection();
        insertSession(session, conn);
        String q = "INSERT INTO outdooression(outdoorStartDateAndTime, temperature, weather) VALUES (?,?,?);";
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
            pstmt.setInt(4,results.getExerciseSet());
            pstmt.setInt(4,results.getDistance());
            pstmt.setInt(4,results.getDuration());
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
}
