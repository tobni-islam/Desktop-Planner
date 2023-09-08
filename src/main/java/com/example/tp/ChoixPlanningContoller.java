package com.example.tp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChoixPlanningContoller implements Initializable {

    public static Utilisateur utilisateur;
    public static void setUtilisateur(Utilisateur u){
        utilisateur = u;
    }

    static CalendrierController calendrierController;
    static public void setCalendrier(CalendrierController c){
        calendrierController = c;
    }


    @FXML
    ComboBox ListPlanning;
    @FXML
    Button ValiderButton;
    @FXML
    Button retourbutton;
    @FXML
    Label labelError;

    public void initialize(URL url, ResourceBundle resourceBundle){
        List<String> plannings = new ArrayList<String>();
        for(Planning p: utilisateur.getListePlanning()){
            plannings.add(p.getPeriode().getDate_debut() + " --->  "+ p.getPeriode().getDate_fin());
        }

        ObservableList<String> pls = FXCollections.observableArrayList(plannings);
        ListPlanning.setItems(pls);

    }


    @FXML
    private void ValiderPlanning(){
        int indice_planning = ListPlanning.getSelectionModel().getSelectedIndex();
        if(indice_planning >=0){
            utilisateur.setPlanning(utilisateur.getListePlanning().get(indice_planning));
            calendrierController.forwardOneMonth();
            calendrierController.backOneMonth();
            NoButtonAction();
        }else{
            labelError.setText("Non planning est fixe!");
        }
    }

    @FXML
    public void NoButtonAction() {
        Stage stage = (Stage) retourbutton.getScene().getWindow();
        // Close the dialog
        stage.close();
    }
}
