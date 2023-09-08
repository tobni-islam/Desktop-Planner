package com.example.tp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.util.Optional;

public class HelloController  {

    private static final String Fichier_pseudo_txt = "src/main/resources/pseudo.txt";
    private static final String prompt_error_authentification = "Cet utilisateur n'existe pas!";

    static Mydesktop_Planner desktop_planner;

    static public void setMydesktopPlanner(Mydesktop_Planner d){
        desktop_planner = d;
    }

    static Utilisateur user;
    static public Utilisateur getUser(){
        return user;
    }

    @FXML
    private TextField input_pseudo;

    @FXML
    private Label errorlabel;

    @FXML
    private Button quitter_button;

    @FXML
    private Button ConnecterButton;


    private Stage stage ;



    @FXML
    protected void onHelloButtonClick() throws Exception{
        String pseudo = input_pseudo.getText();
        if(desktop_planner.Authentification(pseudo)){
            user = desktop_planner.getUser(pseudo);

            Calendrier calendrier;
            if(user.getCalendrier() == null){
                calendrier = new Calendrier();
                user.setCalendrier(calendrier);
            }

            user.AfficherPlannings();

            user.setPlanning(null);

            DialogueController.setUtilisateur(user);
            CrenauController.setUtilisateur(user);
            CalendrierController.setUtilisateur(user);
            ChoixPlanningContoller.setUtilisateur(user);
            CalendrierController.setMydesktopPlanner(desktop_planner);


            CategorieController.setUtilisateur(user);
            JourController.setUtilisateur(user);

            TacheSimpleController.setUtilisateur(user);
            TacheDecomposableController.setUtilisateur(user);
            TacheAutomatiqueController.setUtilisateur(user);
            TypeTacheController.setUtilisateur(user);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("calendrier.fxml"));
        Parent root = loader.load();
        stage = (Stage)quitter_button.getScene().getWindow();
        Scene scene = new Scene(root);

        CalendrierController calendrierController = loader.getController();

        DialogueController.setCalendrier(calendrierController);
        ChoixPlanningContoller.setCalendrier(calendrierController);


        stage.setOnCloseRequest(windowEvent -> {
            windowEvent.consume();
            calendrierController.stop_programme();
        });
        stage.setScene(scene);
        stage.show();
    }else{
            errorlabel.setText(prompt_error_authentification);
        }
    }


    @FXML
    private void openDialog(ActionEvent event)  throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("inscrire.fxml"));
        Parent root= loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Ajouter un utilisateur");
        stage.setResizable(false);
        InscrireController inscrireController = loader.getController();
        scene.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER){
                try{
                    inscrireController.ValiderNouveauPseudo();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

    }


    @FXML
    private  void stop_programme(){
        String Fichier_desktopplanner_path = "src/main/resources/desktop_planner.ser";
        File file = new File(Fichier_desktopplanner_path);

        try {
            //Serialization
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(desktop_planner);
            out.close();
            fileOut.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        // Get a reference to the stage
        Stage stage = (Stage) quitter_button.getScene().getWindow();
        // Close the dialog
        stage.close();
    }

}