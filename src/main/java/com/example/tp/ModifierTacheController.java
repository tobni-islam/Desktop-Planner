package com.example.tp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;

public class ModifierTacheController {

    private Boolean modification = false;
    private Tache tache;
    private LocalDate date;
    private String nomPrimaire = null;
    private Periorite perioritePrimaire = null;
    private Categorie categoriePrimaire = null;
    private JourController jourController;

    public void setJourController(JourController j){
        this.jourController =j;
    }



    @FXML
    TextField nomTacheModifier;
    @FXML
    ComboBox<Periorite> proiriteModifier;
    @FXML
    ComboBox<Categorie> categorieModifier;
    @FXML
    Button annulerButton;
    @FXML
    Button appliquerButton;
    @FXML
    Label nomError;

    public void initialize(LocalDate date,Tache tache, ArrayList<Categorie> categories){

        //fill
        ObservableList<Categorie> cats = FXCollections.observableArrayList(categories);
        categorieModifier.setItems(cats);
        categorieModifier.setValue(tache.getCategorie());
        categorieModifier.setCellFactory(param -> new CategoryListCell());
        categorieModifier.setButtonCell(new CategoryListCell());

        ObservableList<Periorite> periorites = FXCollections.observableArrayList(Periorite.High,Periorite.Meduim,Periorite.Low);
        proiriteModifier.setItems(periorites);
        proiriteModifier.setValue(tache.getPeriorite());

        nomTacheModifier.setText(tache.getNom());

        setTache(tache);
        setNomPrimaire(tache.getNom());
        setPerioritePrimaire(tache.getPeriorite());
        setCategoriePrimaire(tache.getCategorie());

        setDate(date);
    }

    public Tache getTache() {
        return tache;
    }

    public void setTache(Tache tache) {
        this.tache = tache;
    }


    public class CategoryListCell extends javafx.scene.control.ListCell<Categorie> {
        @Override
        protected void updateItem(Categorie item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                // Créer un rectangle avec la couleur de la catégorie
                Rectangle colorRect = new Rectangle(20, 20, item.getCouleur());

                // Afficher le nom de la catégorie et le rectangle de couleur
                setText(item.getName());
                setGraphic(colorRect);
            }
        }
    }

    @FXML
    public void Appliquer(){
        setModification(!(nomTacheModifier.getText().equals(this.tache.getNom()) && proiriteModifier.getValue().equals(this.tache.getPeriorite()) && categorieModifier.getValue().equals(this.tache.getCategorie())));
        if(!this.modification){
            Annuler();
        }else{
            if(VerifNom() == 0){
                Alert alert  = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("CONFIRMATION");
                alert.setContentText("voulez-vous enregistrer les modifications avant de quitter");
                if(alert.showAndWait().get() == ButtonType.OK){
                    tache.setNom(nomTacheModifier.getText());
                    tache.setPeriorete(proiriteModifier.getValue());
                    tache.setCategoeir(categorieModifier.getValue());
                    this.jourController.initialize(this.date);
                    Stage stage = (Stage) annulerButton.getScene().getWindow();
                    stage.close();

                }
            }
        }
    }

    @FXML
    public void Annuler(){
        setModification(!(nomTacheModifier.getText().equals(this.tache.getNom()) && proiriteModifier.getValue().equals(this.tache.getPeriorite()) && categorieModifier.getValue().equals(this.tache.getCategorie())));
        if(this.modification){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setContentText("voulez-vous quitter sans enregistrer les modifications?");
            if(alert.showAndWait().get() == ButtonType.OK){
                tache.setNom(this.nomPrimaire);
                tache.setPeriorete(this.perioritePrimaire);
                tache.setCategoeir(this.categoriePrimaire);
                this.jourController.initialize(this.date);
            }
        }

        Stage stage = (Stage) annulerButton.getScene().getWindow();
        stage.close();
    }

    public int VerifNom(){
        int op;
        String verif_nom = nomTacheModifier.getText().trim();
        if(verif_nom.isEmpty()){
            nomError.setText("Donnez le nom");
            op = 1;
        }else{
            if(nomTacheModifier.getLength() >= 20){
                nomError.setText("Nom tres long, 20 max");
                op = 2;
            }else{
                nomError.setText("");
                op = 0;
            }
        }
        return op;
    }

    public Boolean getModification() {
        return modification;
    }

    public void setModification(Boolean modification) {
        this.modification = modification;
    }

    public String getNomPrimaire() {
        return nomPrimaire;
    }

    public void setNomPrimaire(String nomPrimaire) {
        this.nomPrimaire = nomPrimaire;
    }

    public Periorite getPerioritePrimaire() {
        return perioritePrimaire;
    }

    public void setPerioritePrimaire(Periorite perioritePrimaire) {
        this.perioritePrimaire = perioritePrimaire;
    }

    public Categorie getCategoriePrimaire() {
        return categoriePrimaire;
    }

    public void setCategoriePrimaire(Categorie categoriePrimaire) {
        this.categoriePrimaire = categoriePrimaire;
    }

    public void setDate(LocalDate date){
        this.date = date;
    }
}
