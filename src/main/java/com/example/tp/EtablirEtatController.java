package com.example.tp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class EtablirEtatController {

    private Tache tache;
    private LocalDate date;
    private JourController jourController;

    @FXML
    ComboBox<Etat> getEtat;
    @FXML
    Button confirmerButton;


    public void initialize(JourController parent, Tache tache, LocalDate date){
        ObservableList<Etat> etats = FXCollections.observableArrayList(Etat.notRealzed,Etat.progress,Etat.completed,Etat.delayed);
        getEtat.setItems(etats);
        getEtat.setValue(tache.getEtat());
        setTache(tache);
        setJourController(parent);
        setDate(date);
    }
    @FXML
    public void Confirmer(){
        tache.setEtat(getEtat.getValue());
        this.jourController.initialize(date);
        Stage stage = (Stage) confirmerButton.getScene().getWindow();
        stage.close();
    }

    public void setTache(Tache t){
        this.tache = t;
    }
    public void setJourController(JourController j){
        this.jourController = j;
    }
    public void setDate(LocalDate date){
        this.date = date;
    }

}
