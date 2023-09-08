package com.example.tp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.CacheRequest;
import java.net.URL;
import java.util.ResourceBundle;

public class CategorieController implements Initializable {

    static private Utilisateur utilisateur;

    public static Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public static void setUtilisateur(Utilisateur utilisateur) {
        CategorieController.utilisateur = utilisateur;
    }

    @FXML
    TextField textField;
    @FXML
    ColorPicker color ;

    @FXML
    Label label;
    @FXML
    Button retourbutton;
    public void NoButtonAction(ActionEvent e) {
        Stage stage = (Stage) retourbutton.getScene().getWindow();
        // Close the dialog
        stage.close();
    }

    @FXML
    public void ajoutercategorie (){
       Categorie c = new Categorie(textField.getText(),color.getValue());
        utilisateur.getPlanning().getPeriode().getCategories().add(c) ;
        label.setText("Categorie ajoute avec succes");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        label.setText("");
    }
}
