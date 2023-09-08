package com.example.tp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class DialogueController implements Initializable {
    private Periode periode;

    static Utilisateur utilisateur;
    @FXML
    private Button validerButton;

    @FXML
    private Button retourButton;

    static private Planning planning;

    @FXML
    private DatePicker datepicker1;

    @FXML
    private DatePicker datepicker2;

    static public void setUtilisateur(Utilisateur u){
       utilisateur = u;
    }

    static public Planning getPlanning(){
        return planning;
    }

    static CalendrierController calendrierController;
    static public void setCalendrier(CalendrierController c){
        calendrierController = c;
    }

    @FXML
    private Label label = new Label();

    @FXML
    private void NoButtonAction(ActionEvent event) {
        // Get a reference to the stage
        Stage stage = (Stage) retourButton.getScene().getWindow();
        // Close the dialog
        stage.close();
    }

    @FXML
    private void Valider(ActionEvent event) throws Exception {
        LocalDate date_debut = datepicker1.getValue();
        LocalDate date_fin =datepicker2.getValue();

        if (date_debut.isBefore(LocalDate.now())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Vous ne pouvez pas prendre cette mesure en raison du error / manque de données");
            alert.setContentText("Vous ne pouvez pas spécifier une date de début antérieure à la date actuelle");
            alert.showAndWait();
        }
        else{

            Periode periode = new Periode(datepicker1.getValue(),datepicker2.getValue());

          //Create new planning and set it as  the default planning of the user
          planning = new Planning(periode);
          utilisateur.AjouterPlanning(planning);
          utilisateur.getCalendrier().AjouterPlanning(planning);
          utilisateur.setPlanning(planning);

            calendrierController.forwardOneMonth();
            calendrierController.backOneMonth();

            CrenauController.setDate(date_debut,date_fin);
            CrenauController.setFirstdate(date_debut);

            Stage stage = (Stage) validerButton.getScene().getWindow();
            // Close the dialog
            stage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("duree_minimale.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            FixerDureeMinimaleController fixerDureeMinimaleController = loader.getController();
            fixerDureeMinimaleController.initialize(utilisateur);
            stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);

            stage.setTitle("Fixer la duree minimale d'un creneau");
            stage.show();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        datepicker1.setValue(LocalDate.now());
        datepicker2.setValue(LocalDate.now());
        label.setText("");
    }

    @FXML
    private void NoButtonAction() {
        // Get a reference to the stage
        Stage stage = (Stage) retourButton.getScene().getWindow();
        // Close the dialog
        stage.close();
    }

}
