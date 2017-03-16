package model;


import javafx.beans.property.SimpleStringProperty;

// Unused class. Related functionality not yet implemented
public class Groups {

    private SimpleStringProperty groupName;

    public Groups(String groupName){
        this.groupName = new SimpleStringProperty(groupName);
    }

    public String getGroupName(){
    	return groupName.get();
    }
}
