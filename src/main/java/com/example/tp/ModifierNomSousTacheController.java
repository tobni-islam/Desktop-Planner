package com.example.tp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ModifierNomSousTacheController {
    private Tache_Decomposable tache;
    private Tache_Simple sousTache;
    private String nomPrimaire;
    private DecompositionTacheController decompositionTacheController;
    private Utilisateur utilisateur;
    private ArrayList<Jour> cpy_list_jours;

    public void setSousTache(Tache_Simple tache){
        this.sousTache = tache;
    }

    public Tache_Simple getSousTache(){
        return this.sousTache;
    }


    public void setNomPrimaire(String nom){
        this.nomPrimaire = nom;
    }

    public void setDecompositionTacheController(DecompositionTacheController d){
        this.decompositionTacheController = d;
    }

    public void setUtilisateur(Utilisateur utilisateur){
        this.utilisateur = utilisateur;
    }
    public void setTache(Tache_Decomposable tache){
        this.tache = tache;
    }
    public void setCpy_list_jours(ArrayList<Jour> cpy_list_jours){
        this.cpy_list_jours = cpy_list_jours;
    }

    @FXML
    TextField getNom;
    @FXML
    Label errorLabel;
    @FXML
    Button validerButton;

    public void initialize(DecompositionTacheController parent,Tache_Decomposable parent_tache, Tache_Simple tache, Utilisateur utilisateur, ArrayList<Jour> cpy_list_jours){
        setCpy_list_jours(cpy_list_jours);
        getNom.setText(tache.getNom());
        setSousTache(tache);
        setTache(parent_tache);
        setNomPrimaire(tache.getNom());
        setDecompositionTacheController(parent);
        setUtilisateur(utilisateur);
    }

    @FXML
    public void Valider(){
        if(getNom.getText().equals(this.sousTache.getNom())){
            Annuler();
        }else{
            if(VerifNom() == 0){
                Alert alert  = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("CONFIRMATION");
                alert.setContentText("voulez-vous enregistrer les modifications avant de quitter");
                if(alert.showAndWait().get() == ButtonType.OK){
                    this.sousTache.setNom(getNom.getText());
                    this.decompositionTacheController.initialize(this.tache,this.utilisateur,this.cpy_list_jours);
                    Stage stage = (Stage) validerButton.getScene().getWindow();
                    stage.close();
                }
            }
        }
    }

    public void Annuler(){
        if(!getNom.getText().equals(this.tache.getNom()) ){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setContentText("voulez-vous quitter sans enregistrer les modifications?");
            if(alert.showAndWait().get() == ButtonType.OK){
                tache.setNom(this.nomPrimaire);
                this.decompositionTacheController.initialize(this.tache,this.utilisateur,this.cpy_list_jours);
            }
        }

        Stage stage = (Stage) validerButton.getScene().getWindow();
        stage.close();
    }

    public int VerifNom(){
        int op;
        String verif_nom = getNom.getText().trim();
        if(verif_nom.isEmpty()){
            errorLabel.setText("Donnez le nom");
            op = 1;
        }else{
            if(getNom.getLength() >= 20){
                errorLabel.setText("Nom tres long, 20 max");
                op = 2;
            }else{
                errorLabel.setText("");
                op = 0;
            }
        }
        return op;
    }
}
