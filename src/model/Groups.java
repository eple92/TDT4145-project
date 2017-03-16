package model;


import javafx.beans.property.SimpleStringProperty;

public class Groups {

    private SimpleStringProperty groupName;

    public Groups(String groupName){
        this.groupName = new SimpleStringProperty(groupName);
    }

    public String getInsertQuery() {
        String q = "Insert into Groups (groupName) Values ("+ getGroupName() + ")";
        return q;
    }

    public String getGroupName(){
    	return groupName.get();
    }
}
