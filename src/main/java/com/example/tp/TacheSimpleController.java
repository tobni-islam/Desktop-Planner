package com.example.tp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class TacheSimpleController implements Initializable {

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
    ComboBox getcreneau ;
    @FXML
    RadioButton decomposable;
    @FXML
    RadioButton periodique;
    @FXML
    RadioButton Simple ;
    @FXML
    CheckBox blocked;
    @FXML
    Spinner<Integer> periodicite = new Spinner<Integer>();
    @FXML
    Button retourbutton;
    @FXML
    Button ajouter ;

    @FXML
    Label jourError;
    @FXML
    Label creneauError;
    @FXML
    Label nomError;
    @FXML
    Label dureeError;
    @FXML
    Label perioriteError;
    @FXML
    Label categorieError;



    @FXML
    public void fillcreneau(){

        String chosen = getjour.getValue();
        String pattern = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate date = LocalDate.parse(chosen, formatter);
        List<String> crenaux = new ArrayList<String>();

        for ( Creneaux c : utilisateur.getPlanning().getPeriode().RechercheJour(date).getCreneauxLibre())
        {
            crenaux.add(c.getHeureDebut()+" ---> "+c.getHeureFin());
        }
        ObservableList<String> crs = FXCollections.observableArrayList(crenaux);
        getcreneau.setItems(crs);

    }

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

    if(getcreneau.getValue() == null || getcreneau.getValue() == ""){
        creneauError.setText("Fixez un creneau");
        verifie = false;
    }else{
        creneauError.setText("");
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
            verifie = false;
        }
    }

    if(getpriorite.getValue() == null){
        perioriteError.setText("Fixez une priorite");
        verifie = false;
    }else{
        perioriteError.setText("");
    }

    if(getcategorie.getValue() == null){
        categorieError.setText("Fixez une categorie");
        verifie = false;
    }else{
        categorieError.setText("");
    }

    if(verifie){
        Boolean done = utilisateur.getPlanning().PlanifierTacheSimpleManual(getnom.getText(),Long.parseLong(getduree.getText()),getpriorite.getSelectionModel().getSelectedIndex(),date,getcreneau.getSelectionModel().getSelectedIndex(),getcategorie.getValue(),blocked.isSelected(),periodique.isSelected(),periodicite.getValue());

        if(!done){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ATTENTION");
            alert.setHeaderText("Impossible de planifier cette tache simple!");
            alert.setContentText("La duree de tache est superieure a la duree de creneau choisi");
            alert.showAndWait();
        }else{
            Stage stage = (Stage) retourbutton.getScene().getWindow();
            stage.close();
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



    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> items = FXCollections.observableArrayList("High", "Medium", "Low");
        getpriorite.setItems(items);
        LocalDate date = utilisateur.getPlanning().getPeriode().getDate_debut();
        LocalDate date_fin = utilisateur.getPlanning().getPeriode().getDate_fin();
        while (date.isBefore(date_fin.plusDays(1))) {
            joures.add(date.toString());
            date = date.plusDays(1);
        }
        ObservableList<String> jours = FXCollections.observableArrayList(joures);
        getjour.setItems(jours);
        ArrayList<Categorie> categories = new ArrayList<>();
        for (Categorie c : utilisateur.getPlanning().getPeriode().getCategories()) {
            categories.add(c);
        }
        ObservableList<Categorie> cats = FXCollections.observableArrayList(categories);
        getcategorie.setItems(cats);
        getcategorie.setCellFactory(param -> new CategoryListCell());
        getcategorie.setButtonCell(new CategoryListCell());

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);
        valueFactory.setValue(1);
        periodicite.setValueFactory(valueFactory);
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
}
