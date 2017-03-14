package model;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GroupsInGroups {

    private SimpleStringProperty parentGroup;
    private SimpleStringProperty childGroup;

    public GroupsInGroups(String parentGroup, String childGroup){
        this.parentGroup = new SimpleStringProperty(parentGroup);
        this.childGroup = new SimpleStringProperty(childGroup);
    }

    public String getInsertQuery(){
       String q = "Insert into GroupsInGroups (parentGroup, childGroup) " +
               "Values('"+getParentGroup()+"', '"+getChildGroup()+"')";
       return q;
    }

    public String getParentGroup(){return parentGroup.get();}

    public String getChildGroup(){return childGroup.get();}
}
