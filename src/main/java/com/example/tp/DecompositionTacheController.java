package com.example.tp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class DecompositionTacheController {

    private Tache_Decomposable tache;

    private Tache_Simple currentSousTache;
    private Utilisateur utilisateur;

    private ArrayList<Jour> cpy_list_jours;

    public void setTache(Tache_Decomposable tache){
        this.tache = tache;
    }

    public void setCurrentSousTache(Tache_Simple st){
        this.currentSousTache = st;
    }

    public void setUtilisateur(Utilisateur utilisateur){
        this.utilisateur = utilisateur;
    }

    public void setCpy_list_jours(ArrayList<Jour> cpy_list_jours){
        this.cpy_list_jours = cpy_list_jours;
    }

    public class SousTache{
        private String nom;
        private LocalDate date;
        private String heure_debut;
        private String heure_fin;

        public SousTache(String nom,LocalDate date, String heure_debut,String heure_fin){
            this.nom = nom;
            this.date = date;
            this.heure_debut = heure_debut;
            this.heure_fin = heure_fin;
        }

        public String getNom(){
            return this.nom;
        }

        public LocalDate getDate(){
            return this.date;
        }

        public String getHeure_debut(){
            return this.heure_debut;
        }

        public String getHeure_fin(){
            return this.heure_fin;
        }
    }
    @FXML
    TableView<SousTache> TableSousTaches;
    @FXML
    TableColumn<SousTache,String> NomColumn;
    @FXML
    TableColumn<SousTache, LocalDate> DateColumn;
    @FXML
    TableColumn<SousTache, String> DebutColumn;
    @FXML
    TableColumn<SousTache, String> FinColumn;
    @FXML
    Button ModifierButton;
    @FXML
    Button ValiderButton;
    @FXML
    Button RefuserButton;

    public void initialize(Tache_Decomposable tache,Utilisateur utilisateur,ArrayList<Jour> cpy_list_jours) {
        setTache(tache);
        setUtilisateur(utilisateur);
        setCpy_list_jours(cpy_list_jours);
        ObservableList<SousTache> listTachesView = FXCollections.observableArrayList();
        NomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        DebutColumn.setCellValueFactory(new PropertyValueFactory<>("heure_debut"));
        FinColumn.setCellValueFactory(new PropertyValueFactory<>("heure_fin"));

        //ArrayList<SousTache> listSousTache = new ArrayList<SousTache>();
        listTachesView.clear();

        Jour jour = utilisateur.getPlanning().getPeriode().RechercheJour(LocalDate.now());
        while (jour.getDate().isBefore(tache.getDeadline().plusDays(1))) {
            for (Creneaux c : jour.getList_creneaux()) {
                if (!c.getLibre()) {
                    if (tache.getSous_tache().contains(c.getTache())) {
                        SousTache sousTache = new SousTache(c.getTache().getNom(), jour.getDate(), c.getHeureDebut(), c.getHeureFin());
                        listTachesView.add(sousTache);
                    }
                }
            }
            jour = utilisateur.getPlanning().getPeriode().RechercheJour(jour.getDate().plusDays(1));
            if(jour == null){
                break;
            }
        }

        TableSousTaches.getItems().clear();
        TableSousTaches.getItems().addAll(listTachesView);
        TableSousTaches.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DecompositionTacheController.SousTache>() {
            @Override
            public void changed(ObservableValue<? extends DecompositionTacheController.SousTache> observableValue, DecompositionTacheController.SousTache sousTache, DecompositionTacheController.SousTache t1) {
                DecompositionTacheController.SousTache current = TableSousTaches.getSelectionModel().getSelectedItem();
                int index = TableSousTaches.getSelectionModel().getSelectedIndex();
                if(index >= 0){
                    setCurrentSousTache(tache.getSous_tache().get(index));
                }else{
                    setCurrentSousTache(null);
                }
            }
        });

    }


    @FXML
    public void ModifierNom(){
        if(this.currentSousTache != null){
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("modifierNomSousTache.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                ModifierNomSousTacheController modifierNomSousTacheController = loader.getController();
                modifierNomSousTacheController.initialize(this,this.tache,this.currentSousTache,this.utilisateur,this.cpy_list_jours);
                stage.setResizable(false);
                stage.setTitle("Modifier Nom de Sous-Tache");
                stage.setOnCloseRequest(windowEvent -> {
                    windowEvent.consume();
                    modifierNomSousTacheController.Annuler();
                });
                stage.show();

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void Valider(){
        Stage stage = (Stage) ValiderButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void Refuser(){
        utilisateur.getPlanning().getEnsemble_taches().remove(utilisateur.getPlanning().getEnsemble_taches().get(utilisateur.getPlanning().getEnsemble_taches().size() -1));
        utilisateur.getPlanning().getPeriode().setJours(cpy_list_jours);
        Valider();
    }
}
