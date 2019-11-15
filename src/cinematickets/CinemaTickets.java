/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinematickets;

import java.sql.ResultSet;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author joj0s
 */
public class CinemaTickets extends Application {
    
    static User user = null;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent loginRoot = FXMLLoader.load(getClass().getResource("loginScene.fxml"));
        
        Scene loginScene = new Scene(loginRoot);
        loginScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        
        stage.setScene(loginScene);
        stage.setResizable(false);
        stage.setTitle("CinemaTickets - Login");
        stage.show();
    }


    public static void main(String[] args) {
        //ΕΚΚΙΝΗΣΗ ΤΟΥ SQL STATEMENT 
        try{
            DBUtils.initStatement();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        //ΕΚΚΙΝΗΣΗ ΤΗΣ JAVAFX
        launch(args);
       
    }
    
}
