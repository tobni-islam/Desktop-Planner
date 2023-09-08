package com.example.tp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class FixerDureeMinimaleController {
    private Utilisateur utilisateur;

    public void setUtilisateur(Utilisateur utilisateur){
        this.utilisateur = utilisateur;
    }

    @FXML
    Button validerButton;
    @FXML
    Spinner<Integer> getDuree;

    public void initialize(Utilisateur utilisateur){
        setUtilisateur(utilisateur);
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 120);
        valueFactory.setValue(10);
        valueFactory.setAmountToStepBy(10);
        getDuree.setValueFactory(valueFactory);
    }

    @FXML
    public void Valider(){
        int duree = getDuree.getValue();
        this.utilisateur.getPlanning().setDureeMinimale(duree);

        Stage stage = (Stage) validerButton.getScene().getWindow();
        stage.close();

        try {
            Parent root= FXMLLoader.load(getClass().getResource("fixercreneaux.fxml"));
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.setTitle("Fixer Les Creneaux");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
