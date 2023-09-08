package com.example.tp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.util.ArrayList;

public class DecompositionDialogueController {

    private Tache_Decomposable tache;
    private Utilisateur utilisateur;

    private ArrayList<Jour> cpy_list_jours;

    public void setTache(Tache_Decomposable tache){
        this.tache = tache;
    }
    public void setUtilisateur(Utilisateur u){
        this.utilisateur = u;
    }
    public void setCpy_list_jours(ArrayList<Jour> cpy_list_jours){
        this.cpy_list_jours = cpy_list_jours;
    }

    @FXML
    Button DecompositionButton;

    public void initialize(Tache_Decomposable tache,Utilisateur utilisateur,ArrayList<Jour> cpy_list_jours){
        setTache(tache);
        setUtilisateur(utilisateur);
        setCpy_list_jours(cpy_list_jours);
    }

    @FXML
    public void OnDecompositionClick(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("voir_decomposition.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            DecompositionTacheController decompositionTacheController = loader.getController();
            decompositionTacheController.initialize(this.tache,this.utilisateur,this.cpy_list_jours);
            stage.setOnCloseRequest(windowEvent -> {
                windowEvent.consume();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("CONFIRMATION");
                alert.setHeaderText("Vous êtes sur le point de valider la decomposition");
                alert.setContentText("Voulez-vous valider la decomposition et la tache va etre planifier");
                if(alert.showAndWait().get() == ButtonType.OK){
                    decompositionTacheController.Valider();
                }
            });
            stage.setTitle("Decomposition de tache "+this.tache.getNom());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
        Stage stage = (Stage) DecompositionButton.getScene().getWindow();
        stage.close();
    }
}
