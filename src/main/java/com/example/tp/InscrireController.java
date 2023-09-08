package com.example.tp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InscrireController {

    static Mydesktop_Planner desktop_planner;

    static public void setMydesktopPlanner(Mydesktop_Planner d){
        desktop_planner = d;
    }

    @FXML
    Button retourButton;

    @FXML
    Label label;

    @FXML
    Button validerbutton;

    @FXML
    TextField new_pseudo;

    @FXML
    Label errorlabel;

    @FXML
    public void ValiderNouveauPseudo(){
        String nouv_pseudo = new_pseudo.getText();
        if(desktop_planner.Authentification(nouv_pseudo)){
            errorlabel.setText("Cet utilisateur existe deja!");
        }else{
            String Verif_pseudo = new_pseudo.getText().trim();
            if(Verif_pseudo.isEmpty()){
                errorlabel.setText("Donnez un pseudo!");
            }else{
                Utilisateur user = new Utilisateur(nouv_pseudo);
                desktop_planner.AjouterUser(user);

                NoButtonAction();
            }
        }
    }

    @FXML
    private void NoButtonAction() {
        // Get a reference to the stage
        Stage stage = (Stage) retourButton.getScene().getWindow();
        // Close the dialog
        stage.close();
    }

}
