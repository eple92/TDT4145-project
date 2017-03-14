package controller;

import java.io.BufferedReader;
import java.io.IOException;

public class QualityController {

    private BufferedReader br;

    public void getSession() throws IOException {
        System.out.println("vil du se på beste økt for ute- eller innetrening? (in/out)");
        String inout = null;
        while(inout == null ){
            String input = br.readLine();

            if (input == "in"){
                // TODO
                inout = input;
            }
            else if(input == "out"){
                // TODO
                inout = input;
            }
            else{
                System.out.println("Det er ikke en godkjent treningstype (in/out)");

            }
        }
    }
}
