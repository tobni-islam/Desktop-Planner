package com.example.tp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TacheAutomatiqueController {

    private static Utilisateur utilisateur;
    private static Boolean simple;
    public static Map<Tache,Boolean> ens_taches;

    public static void setUtilisateur(Utilisateur u){
        utilisateur = u;
    }

    public static void setSimple(Boolean s){
        simple = s;
    }

    @FXML
    TextField getNom ;
    @FXML
    TextField getDuree;
    @FXML
    ComboBox<String> getProirite = new ComboBox<String>() ;
    @FXML
    ComboBox<String> getDeadline= new ComboBox<>();

    @FXML
    ComboBox<Categorie> getCategorie ;

    @FXML
    CheckBox getBloque;

    @FXML
    Button AjouterButton ;

    @FXML
    Button RetourButton;
    @FXML
    Button TerminerButton;

    @FXML
    Label DeadlineError;
    @FXML
    Label NomError;
    @FXML
    Label DureeError;
    @FXML
    Label ProiriteError;
    @FXML
    Label CategorieError;


    public void initialize() {
        List<String> joures = new ArrayList<String>();
        ObservableList<String> items = FXCollections.observableArrayList( "High","Medium","Low");
        getProirite.setItems(items);
        LocalDate date = utilisateur.getPlanning().getPeriode().getDate_debut();
        LocalDate date_fin = utilisateur.getPlanning().getPeriode().getDate_fin();
        while (date.isBefore(date_fin.plusDays(1))){
            joures.add(date.toString());
            date = date.plusDays(1);
        }
        ObservableList<String> jours =  FXCollections.observableArrayList(joures);
        getDeadline.setItems(jours);
        ArrayList<Categorie> categories = new ArrayList<>();
        for(Categorie c : utilisateur.getPlanning().getPeriode().getCategories()){
            categories.add(c);
        }
        ObservableList<Categorie> cats =  FXCollections.observableArrayList(categories);
        getCategorie.setItems(cats);
        getCategorie.setCellFactory(param -> new CategoryListCell());
        getCategorie.setButtonCell(new CategoryListCell());


    }


    static class CategoryListCell extends javafx.scene.control.ListCell<Categorie> {
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
    public void Ajouter(){
        Boolean verifie = true;

        LocalDate date = null;
        if(getDeadline.getValue() == "" || getDeadline.getValue() == null){
            DeadlineError.setText("Fixez un jour");
            verifie = false;
        }else{
            String chosen = getDeadline.getValue();
            String pattern = "yyyy-MM-dd";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            date = LocalDate.parse(chosen, formatter);
            DeadlineError.setText("");
        }

        String verif_nom = getNom.getText().trim();
        if(verif_nom.isEmpty()){
            NomError.setText("Donnez le nom");
            verifie = false;
        }else{
            if(getNom.getLength() >= 20){
                NomError.setText("Nom tres long, 20 max");
                verifie = false;
            }else{
                NomError.setText("");
            }
        }

        String verif_duree = getDuree.getText().trim();
        if(verif_duree.isEmpty()){
            DureeError.setText("Fixez une duree");
            verifie = false;
        }else{
            try {
                long num = Long.parseLong(getDuree.getText());
                DureeError.setText("");
            }catch (Exception e){
                DureeError.setText("La duree en minutes!");
                verifie = false;
            }
        }

        Periorite periorite = null;
        if(getProirite.getValue() == null){
            ProiriteError.setText("Fixez une priorite");
            verifie = false;
        }else{
            ProiriteError.setText("");
            if(getProirite.getSelectionModel().getSelectedIndex() == 0){
                periorite = Periorite.High;
            }else{
                if(getProirite.getSelectionModel().getSelectedIndex() == 1){
                    periorite = Periorite.Meduim;
                }else {
                    periorite = Periorite.Low;
                }
            }
        }

        if(getCategorie.getValue() == null){
            CategorieError.setText("Fixez une categorie");
            verifie = false;
        }else{
            CategorieError.setText("");
        }


        if(verifie){
            Tache tache;
            if(this.simple){
                tache = new Tache_Simple(getNom.getText(),Long.parseLong(getDuree.getText()),periorite,date,getCategorie.getValue(),Etat.notRealzed,false,0);
            }else{
                tache = new Tache_Decomposable(getNom.getText(),Long.parseLong(getDuree.getText()),periorite,date,getCategorie.getValue(),Etat.notRealzed);
            }

            if(ens_taches == null) {
                ens_taches = new HashMap<Tache,Boolean>();
            }

            ens_taches.put(tache,getBloque.isSelected());

            Stage stage = (Stage) RetourButton.getScene().getWindow();
            stage.close();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ChoixTypeTache.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage = new Stage();
                stage.setResizable(false);
                stage.setScene(scene);
                TypeTacheController.setAutomatique(true);
                TypeTacheController typeTacheController = loader.getController();
                Stage finalStage = stage;
                stage.setOnCloseRequest(windowEvent -> {
                    windowEvent.consume();
                    typeTacheController.Annuler(finalStage);
                });
                stage.setTitle("Choisir le type du tache");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    @FXML
    public void Retour(){
        try {
            Stage stage = (Stage) RetourButton.getScene().getWindow();
            // Close the dialog
            stage.close();

            Parent root= FXMLLoader.load(getClass().getResource("ChoixTypeTache.fxml"));
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.setTitle("Choisir le type du tache");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
