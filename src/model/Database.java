package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {

	// Local variables
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/WorkoutDiary";
		
	// Database credentials
	static final String USER = "root";
	static final String PASS = "sn3gleYngel!";
	
	private final String dbName = "WorkoutDiary";

	// Create a database object
	public Database() {
		
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
			System.out.println("Opening a connetion");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
	
			System.out.println("Creating the database");
			String query = "CREATE DATABASE IF NOT EXISTS " + dbName + ";";
			pstmt = conn.prepareStatement(query);
			pstmt.executeUpdate();
			
			noReturnAction(Session.createTableQuery);
			noReturnAction(Exercise.createTableQuery);
			
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
		ArrayList<String> result = new ArrayList<String>();
		
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
		ArrayList<String> result = new ArrayList<String>();
	
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
			}
		}
		return result;
	}
}
