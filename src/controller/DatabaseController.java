package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import model.Database;
import model.Indoor;
import model.Outdoor;
import model.Session;

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
            db.populateDatabase();
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
            System.out.println(conn.toString());
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
        System.out.println(conn.toString());
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
}
