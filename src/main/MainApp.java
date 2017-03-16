package main;

import controller.ControllerManager;

public class MainApp {
    
    public static void main(String[] args) {
    	System.out.println("Welcome to your exercise diary!");
    	ControllerManager controllerManager = new ControllerManager();
    	controllerManager.getInputController().getAction();
    }
}
