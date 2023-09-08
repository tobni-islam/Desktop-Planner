package com.example.tp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class JourController {

    static private Utilisateur utilisateur ;
    private Tache currentTache;
    private LocalDate date;

    public void setDate(LocalDate date){
        this.date = date;
    }
    public static Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public static void setUtilisateur(Utilisateur u) {
        utilisateur = u;
    }
    public void setCurrentTache(Tache t){
        this.currentTache = t;
    }

    @FXML
    Label tacheTitre;
    @FXML
    Button modifierButton;
    @FXML
    Button etablirButton;
    @FXML
    Button supprimerButton;
    @FXML
    TableView<TacheView> listTaches;
    @FXML
    TableColumn<TacheView,String> nomColumn;
    @FXML
    TableColumn<TacheView,String> heuredebutColumn;
    @FXML
    TableColumn<TacheView,String> heurefinColumn;
    @FXML
    TableColumn<TacheView,String> categorieColumn;
    @FXML
    TableColumn<TacheView,Periorite> proiriteColumn;
    @FXML
    TableColumn<TacheView,Etat> etatColumn;



    public class TacheView{
        private String nom;
        private String heureDebut;
        private String heureFin;
        private Periorite proirite;
        private String categorie;
        private Etat etat;

        public TacheView(String n, String hD,String hF,Periorite p,String c,Etat e){
            this.nom = n;
            this.heureDebut = hD;
            this.heureFin = hF;
            this.proirite = p;
            this.categorie = c;
            this.etat = e;
        }

        public String getNom(){
            return this.nom;
        }
        public String getHeureDebut(){
            return this.heureDebut;
        }

        public String getHeureFin(){
            return this.heureFin;
        }

        public Periorite getProirite(){
            return this.proirite;
        }
        public String getCategorie(){
            return this.categorie;
        }

        public Etat getEtat(){
            return this.etat;
        }

    }


    public void initialize(LocalDate date) {
        setDate(date);
        setCurrentTache(null);
        ObservableList<TacheView> listTachesView = FXCollections.observableArrayList();
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        heuredebutColumn.setCellValueFactory(new PropertyValueFactory<>("heureDebut"));
        heurefinColumn.setCellValueFactory(new PropertyValueFactory<>("heureFin"));
        proiriteColumn.setCellValueFactory(new PropertyValueFactory<>("proirite"));
        categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        etatColumn.setCellValueFactory(new PropertyValueFactory<>("etat"));

        Jour jour = utilisateur.getPlanning().getPeriode().RechercheJour(date);
        ArrayList<Tache> listTache = new ArrayList<Tache>();
        listTachesView.clear();

        for(Creneaux c: jour.getList_creneaux()){
            if(!c.getLibre()){
                listTachesView.add(new TacheView(c.getTache().getNom(),c.getHeureDebut(),c.getHeureFin(),c.getTache().getPeriorite(),c.getTache().getCategorie().getName(),c.getTache().getEtat()));
                listTache.add(c.getTache());
            }
        }
        listTaches.getItems().clear();
        listTaches.getItems().addAll(listTachesView);
        listTaches.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TacheView>() {
            @Override
            public void changed(ObservableValue<? extends TacheView> observableValue, TacheView tacheView, TacheView t1) {
                TacheView current = listTaches.getSelectionModel().getSelectedItem();
                int index = listTaches.getSelectionModel().getSelectedIndex();
                if(index >= 0){
                    setCurrentTache(listTache.get(index));
                    tacheTitre.setText(current.getNom());
                }else{
                    tacheTitre.setText("");
                }
            }
        });
    }

    @FXML
    public void ModifierTache(){
        if(this.currentTache != null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("modifierTache.fxml"));
                Parent root = loader.load();
                ModifierTacheController modifierTacheController = loader.getController();
                modifierTacheController.initialize(this.date,this.currentTache,utilisateur.getPlanning().getPeriode().getCategories());
                modifierTacheController.setJourController(this);
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Modification de Tache");
                stage.setScene(scene);
                stage.setOnCloseRequest(windowEvent -> {
                    windowEvent.consume();
                    modifierTacheController.Annuler();
                });
                stage.show();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void EtablirEtatTache(){
        if(this.currentTache != null){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("etablirEtat.fxml"));
                Parent root = loader.load();
                EtablirEtatController etablirEtatController = loader.getController();
                etablirEtatController.initialize(this,this.currentTache,date);
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle(this.currentTache.getNom());
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.show();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void SupprimerTache(){
        if(this.currentTache != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setContentText("Voulez-vous annuler cette tache?");
            if(alert.showAndWait().get() == ButtonType.OK){
                utilisateur.getPlanning().SupprimerTache(this.currentTache,this.date);
                initialize(this.date);
            }
        }
    }
}
