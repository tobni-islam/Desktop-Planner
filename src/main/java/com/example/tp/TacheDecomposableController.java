package com.example.tp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TacheDecomposableController implements Initializable {

    private Planning planning = DialogueController.getPlanning();
    static private Utilisateur utilisateur ;
    List<String> joures = new ArrayList<String>();


    static public void setUtilisateur(Utilisateur u ){
        utilisateur = u ;
    }


    @FXML
    TextField getnom ;
    @FXML
    TextField getduree;
    @FXML
    ComboBox<String> getpriorite = new ComboBox<String>() ;
    @FXML
    ComboBox<String> getjour= new ComboBox<>();

    @FXML
    ComboBox<Categorie> getcategorie ;

    @FXML
    CheckBox blocked;

    @FXML
    Button ajouter ;

    @FXML
    Button retourbutton;

    @FXML
    Label jourError;
    @FXML
    Label nomError;
    @FXML
    Label dureeError;
    @FXML
    Label prioriteError;
    @FXML
    Label categorieError;


    @FXML
    public void AjouterTache(){
        Boolean verifie = true;

        LocalDate date = null;
        if(getjour.getValue() == "" || getjour.getValue() == null){
            jourError.setText("Fixez un jour");
            verifie = false;
        }else{
            String chosen = getjour.getValue();
            String pattern = "yyyy-MM-dd";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            date = LocalDate.parse(chosen, formatter);
            jourError.setText("");
        }

        String verif_nom = getnom.getText().trim();
        if(verif_nom.isEmpty()){
            nomError.setText("Donnez le nom");
            verifie = false;
        }else{
            if(getnom.getLength() >= 20){
                nomError.setText("Nom tres long, 20 max");
                verifie = false;
            }else{
                nomError.setText("");
            }
        }

        String verif_duree = getduree.getText().trim();
        if(verif_duree.isEmpty()){
            dureeError.setText("Fixez une duree");
            verifie = false;
        }else{
            try {
                long num = Long.parseLong(getduree.getText());
                dureeError.setText("");
            }catch (Exception e){
                dureeError.setText("La duree en minutes!");
            }
        }

        if(getpriorite.getValue() == null){
            prioriteError.setText("Fixez une priorite");
            verifie = false;
        }else{
            prioriteError.setText("");
        }

        if(getcategorie.getValue() == null){
            categorieError.setText("Fixez une categorie");
            verifie = false;
        }else{
            categorieError.setText("");
        }

        if(verifie){
            ArrayList<Jour> cpy_list_jours = utilisateur.getPlanning().getPeriode().CopyOfListJours();
            int op = utilisateur.getPlanning().PlanifierTacheDecomposableManual(getnom.getText(),Long.parseLong(getduree.getText()),getpriorite.getSelectionModel().getSelectedIndex(),date,getcategorie.getValue(),blocked.isSelected());
            System.out.println("le planning apres la planification de tache decomposable");
            utilisateur.getPlanning().AfficherPlanning();
            if(op == 1){
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("decomposition_dialogue.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    DecompositionDialogueController decompositionDialogueController = loader.getController();
                    decompositionDialogueController.initialize((Tache_Decomposable) utilisateur.getPlanning().getEnsemble_taches().get(utilisateur.getPlanning().getEnsemble_taches().size()-1),utilisateur,cpy_list_jours);
                    stage.setOnCloseRequest(windowEvent -> {
                        windowEvent.consume();
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("CONFIRMATION");
                        alert.setHeaderText("Vous êtes sur le point de refuser la decomposition");
                        alert.setContentText("Voulez-vous refuser la decomposition et la tache ne sera pas planifier");
                        if(alert.showAndWait().get() == ButtonType.OK){
                            utilisateur.getPlanning().getPeriode().setJours(cpy_list_jours);
                            utilisateur.getPlanning().getEnsemble_taches().remove(utilisateur.getPlanning().getEnsemble_taches().get(utilisateur.getPlanning().getEnsemble_taches().size() -1));
                        }
                    });
                    stage.setTitle("Decomposition de tache");
                    stage.setResizable(false);
                    stage.show();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            Stage stage = (Stage) retourbutton.getScene().getWindow();
            // Close the dialog
            stage.close();
        }
    }


    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> items = FXCollections.observableArrayList( "High","Medium","Low");
        getpriorite.setItems(items);
        LocalDate date = utilisateur.getPlanning().getPeriode().getDate_debut();
        LocalDate date_fin = utilisateur.getPlanning().getPeriode().getDate_fin();
        while (date.isBefore(date_fin.plusDays(1))){
            joures.add(date.toString());
            date = date.plusDays(1);
        }
        ObservableList<String> jours =  FXCollections.observableArrayList(joures);
        getjour.setItems(jours);
        ArrayList<Categorie> categories = new ArrayList<>();
        for(Categorie c : utilisateur.getPlanning().getPeriode().getCategories()){
            categories.add(c);
        }
        ObservableList<Categorie> cats =  FXCollections.observableArrayList(categories);
        getcategorie.setItems(cats);
        getcategorie.setCellFactory(param -> new CategoryListCell());
        getcategorie.setButtonCell(new CategoryListCell());


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
    public void NoButtonAction() throws Exception{
        Stage stage = (Stage) retourbutton.getScene().getWindow();
        // Close the dialog
        stage.close();

        Parent root= FXMLLoader.load(getClass().getResource("ChoixTypeTache.fxml"));
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setTitle("Choisir le type du tache");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }


}
