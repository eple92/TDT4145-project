package model;


import javafx.beans.property.SimpleStringProperty;


// Unused class. Related functionality not yet implemented
public class ExersicesInGroups {

    private SimpleStringProperty parentGroup;
    private SimpleStringProperty exercise;

    public ExersicesInGroups(String parentGroup, String exercise){
        this.parentGroup = new SimpleStringProperty(parentGroup);
        this.exercise = new SimpleStringProperty(exercise);
    }

    public String getParentGroup(){
    	return parentGroup.get();
    }

    public String getExersice(){
    	return exercise.get();
    }
}
