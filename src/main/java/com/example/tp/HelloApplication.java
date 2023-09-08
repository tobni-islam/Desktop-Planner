package com.example.tp;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.io.*;


public class HelloApplication extends Application {

    public Mydesktop_Planner desktop_planner = null;

     public void start(Stage primaryStage) throws Exception {

         String Fichier_desktopplanner_path = "src/main/resources/desktop_planner.ser";
         File file = new File(Fichier_desktopplanner_path);

         try{
             if(file.exists()){

                 //Deserialization
                 FileInputStream fileIn = new FileInputStream(file);
                 ObjectInputStream in = new ObjectInputStream(fileIn);

                 desktop_planner = (Mydesktop_Planner)in.readObject();

                 if(desktop_planner == null){
                     System.out.println("Desktop is null");
                 }else{
                     System.out.println("Desktop is not null");
                 }

                 in.close();
                 fileIn.close();

             }else{
                 boolean created = file.createNewFile();
                 if(created){

                     desktop_planner = new Mydesktop_Planner();

                     //Serialization
                     FileOutputStream fileOut = new FileOutputStream(file);
                     ObjectOutputStream out = new ObjectOutputStream(fileOut);

                     out.writeObject(desktop_planner);
                     out.close();
                     fileOut.close();

                 }else{
                     System.out.println("Failed to create the file");
                 }
             }
         }catch(IOException | ClassNotFoundException e){
             e.printStackTrace();
         }

         //Test list of users in desktop planner
         System.out.println("---------------------------------");
         System.out.println("La list de tous les utilisateurs dans MyDesktopPlanner: ");
         desktop_planner.AfficherUsers();
         System.out.println("---------------------------------");


         InscrireController.setMydesktopPlanner(desktop_planner);
         HelloController.setMydesktopPlanner(desktop_planner);

         FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));


         Parent root = loader.load();
         Scene scene = new Scene(root);

         HelloController helloController = loader.getController();
         scene.setOnKeyPressed(keyEvent -> {
             if(keyEvent.getCode() == KeyCode.ENTER){
                 try{
                     helloController.onHelloButtonClick();
                 }catch (Exception e){
                     e.printStackTrace();
                 }
             }
         });

         primaryStage.setResizable(false);
         primaryStage.setOnCloseRequest(windowEvent -> {
             windowEvent.consume();
             stop_programme(primaryStage);
         });
         primaryStage.setScene(scene);
         primaryStage.show();

     }

    public void stop_programme(Stage stage) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Déconnecter");
        alert.setHeaderText("Vous êtes sur le point de vous déconnecter");
        alert.setContentText("Voulez-vous enregistrer avant de quitter");

        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("Le planning de l'utilisateur ne sera pas affiche car la deconetion est faite onCloseRequest");

            String Fichier_desktopplanner_path = "src/main/resources/desktop_planner.ser";
            File file = new File(Fichier_desktopplanner_path);

            try {
                //Serialization
                FileOutputStream fileOut = new FileOutputStream(file);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);

                out.writeObject(desktop_planner);
                out.close();
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Close the dialog
            stage.close();
        }
    }

    public static void main(String[] args) {
         Application.launch(args);
    }

}
