package com.example.tp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.application.Platform;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class CrenauController {

    @FXML
    Spinner<Integer> heure_debut;

    @FXML
    Spinner<Integer> min_debut;
    @FXML
    Spinner<Integer> heure_fin;
    @FXML
    Spinner<Integer> min_fin;

    @FXML
    Label succeslabel;
    @FXML
    Label datelabel;

    @FXML
    CheckBox checkBox;

   static Utilisateur utilisateur;
   static  LocalDate date_debut ;
   static LocalDate date_fin;

    LocalDate date_tmp = firstdate ;


    static LocalDate firstdate;

    static public void setFirstdate(LocalDate d){
        firstdate = d;
    }


    static public void setUtilisateur(Utilisateur u){
        utilisateur = u;
    }
    static  public void setDate(LocalDate d , LocalDate f){
        date_debut= d ;
        date_fin = f;
    }


    @FXML
    public  void AjouterCreneau(){
        LocalTime debut = LocalTime.of(heure_debut.getValue(),min_debut.getValue());
        LocalTime fin = LocalTime.of(heure_fin.getValue(),min_fin.getValue());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String debut_s = debut.format(formatter);
        String fin_s = fin.format(formatter);

        // Code operation = 0 si l'operation a ete fait avec succes, 1 si durre de creneau < duree_min, 2 s'il y a une
        // conflit avec un autre creneaux
        int operation = utilisateur.getPlanning().getPeriode().RechercheJour(date_debut).AjouterCreneauxLibre(debut_s,fin_s,false) ;

        date_tmp = date_debut;
        if (checkBox.isSelected()){
            date_tmp = date_tmp.plusDays(1);
            while(date_tmp.isBefore(date_fin.plusDays(1))) {
                utilisateur.getPlanning().getPeriode().RechercheJour(date_tmp).AjouterCreneauxLibre(debut_s,fin_s,false);
                date_tmp = date_tmp.plusDays(1);
            }
        }

        if(operation == 0){
            initialize();
        }else{
            if(operation == 1){

                initialize();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Vous ne pouvez pas prendre cette mesure en raison du error / manque de données");
                alert.setContentText("Impossible d'ajouter creneau! la duree minimal est " + utilisateur.getPlanning().getDureeMinimale());
                alert.showAndWait();

            }else{

                initialize();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Vous ne pouvez pas prendre cette mesure en raison du error / manque de données");
                alert.setContentText("Impossible d'ajouter creneau! il y a des conflit avec les autres creneaux");
                alert.showAndWait();
            }
        }
    }
    public void Joursuivant() throws Exception{
        succeslabel.setText("");
        if(date_debut.plusDays(1).isBefore(date_fin.plusDays(1))){
            setFirstdate(date_debut);
            setDate(date_debut.plusDays(1),date_fin);
            Stage stage = (Stage) succeslabel.getScene().getWindow();
            // Close the dialog
            stage.close();
            Parent root= FXMLLoader.load(getClass().getResource("fixercreneaux.fxml"));
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.setTitle("Fixer Les Creneaux");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();}
        else {

            Stage stage = (Stage) succeslabel.getScene().getWindow();
            // Close the dialog
            stage.close();
            Parent root= FXMLLoader.load(getClass().getResource("categorie.fxml"));
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.setTitle("Fixer Les Creneaux");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }
    }

    public void NoButtonAction(ActionEvent e) {
        Stage stage = (Stage) succeslabel.getScene().getWindow();
        // Close the dialog
        stage.close();
    }




    public void initialize() {
       datelabel.setText(date_debut.toString());
       succeslabel.setText("");

        SpinnerValueFactory<Integer> valueFactory_HD = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
        SpinnerValueFactory<Integer> valueFactory_HF = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
        SpinnerValueFactory<Integer> valueFactory_MD = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59);
        SpinnerValueFactory<Integer> valueFactory_MF = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59);
        valueFactory_MD.setValue(0);
        valueFactory_MF.setValue(0);
        valueFactory_HD.setValue(0);
        valueFactory_HF.setValue(0);
        heure_debut.setValueFactory(valueFactory_HD);
        min_debut.setValueFactory(valueFactory_MD);
        heure_fin.setValueFactory(valueFactory_HF);
        min_fin.setValueFactory(valueFactory_MF);

        checkBox.setSelected(false);

    }
}
