package model;


import javafx.beans.property.SimpleStringProperty;

// Unused class. Related functionality not yet implemented
public class GroupsInGroups {

    private SimpleStringProperty parentGroup;
    private SimpleStringProperty childGroup;

    public GroupsInGroups(String parentGroup, String childGroup){
        this.parentGroup = new SimpleStringProperty(parentGroup);
        this.childGroup = new SimpleStringProperty(childGroup);
    }

    public String getParentGroup(){
    	return parentGroup.get();
    }

    public String getChildGroup(){
    	return childGroup.get();
    }
}
