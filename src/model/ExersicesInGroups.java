package model;


import javafx.beans.property.SimpleStringProperty;

public class ExersicesInGroups {

    private SimpleStringProperty parentGroup;
    private SimpleStringProperty exersice;

    public ExersicesInGroups(String parentGroup, String exersice){
        this.parentGroup = new SimpleStringProperty(parentGroup);
        this.exersice = new SimpleStringProperty(exersice);
    }

    public String getInsertQuery(){
        String q = "Insert into ExersicesInGroups (parentGroup, childGroup) " +
                "Values('"+getParentGroup()+"', '"+getExersice()+"')";
        return q;
    }

    public String getParentGroup(){return parentGroup.get();}

    public String getExersice(){return exersice.get();}
}
