package controller;

import java.util.ArrayList;

import model.Database;

public class DatabaseController {

		private Database db;
		private ControllerManager manager;
		
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
}
