package com.example.tp;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class TypeTacheController implements Initializable {

    static Utilisateur utilisateur;
    static Boolean Automatique;

    public static void setAutomatique(Boolean value){
        Automatique = value;
    }
    public static void setUtilisateur(Utilisateur u){utilisateur = u;}


    @FXML
    Button ButtonDecomposable;

    @FXML
    Button ButtonSimple;
    @FXML
    Button terminerButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BooleanProperty ShowBooleanProperty = new SimpleBooleanProperty(Automatique);
        terminerButton.visibleProperty().bind(ShowBooleanProperty);
    }


    @FXML
    public void PlanifierSimple() throws Exception{

        Stage stage = (Stage) ButtonSimple.getScene().getWindow();
        // Close the dialog
        stage.close();

        String source;

        if(Automatique){
            source = "tache_automatique.fxml";
            TacheAutomatiqueController.setSimple(true);
        }else{
            source = "tacheSimple.fxml";
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource(source));
        Parent root= loader.load();
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setTitle("Planifier Une Tache Simple");
        stage.setScene(scene);
        Stage finalStage = stage;
        stage.setOnCloseRequest(windowEvent -> {
            windowEvent.consume();
            Annuler(finalStage);

        });
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    public void PlanifierDecomposable() throws Exception{

        Stage stage = (Stage) ButtonSimple.getScene().getWindow();
        // Close the dialog
        stage.close();

        String source;

        if(Automatique){
            source = "tache_automatique.fxml";
            TacheAutomatiqueController.setSimple(false);
        }else{
            source = "tacheDecomposable.fxml";
        }

        Parent root= FXMLLoader.load(getClass().getResource(source));
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setTitle("Planifier Une Tache Decomposable");
        stage.setScene(scene);
        Stage finalStage = stage;
        stage.setOnCloseRequest(windowEvent -> {
            windowEvent.consume();
            Annuler(finalStage);

        });
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }


    @FXML
    public void Terminer(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setHeaderText("Vous etes sur le point de confirmer l'operation");
        String text;
        if(TacheAutomatiqueController.ens_taches == null || TacheAutomatiqueController.ens_taches.size() == 0){
            text = "Vous n'avez saisi aucune tâche à planifier! Voulez-vous terminer?";
        }else{
            text = "Voulez-vous confirmer la currente liste des taches a planifier";
        }
        alert.setContentText(text);
        if(alert.showAndWait().get() == ButtonType.OK){
            if(TacheAutomatiqueController.ens_taches != null){
                System.out.println("La liste des taches a planifier");
                for(Map.Entry<Tache,Boolean> entry: TacheAutomatiqueController.ens_taches.entrySet()){
                    Tache tache = entry.getKey();
                    System.out.println("Nom: "+ tache.getNom()+" |Duree: "+tache.getDuree()+ " | bloque: "+ TacheAutomatiqueController.ens_taches.get(tache));
                }

                utilisateur.getPlanning().PlanificationAutomatique(TacheAutomatiqueController.ens_taches);
                TacheAutomatiqueController.ens_taches.clear();
            }
            System.out.println("-------------------------------------------------");

            Stage stage = (Stage) terminerButton.getScene().getWindow();
            stage.close();
        }
    }


    public void Annuler(Stage stage){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setHeaderText("Vous etes sur le point d'annuler l'operation");

        if(Automatique){
            alert.setContentText("Voulez-vous annuler la planification automatique des taches");
        }else{
            alert.setContentText("Voulez-vous annuler la planification de cette tache");
        }

        if(alert.showAndWait().get() == ButtonType.OK){
            if(TacheAutomatiqueController.ens_taches != null){
                TacheAutomatiqueController.ens_taches.clear();
            }
            stage.close();
        }
    }

}
