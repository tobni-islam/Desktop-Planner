package com.example.tp;
import com.example.tp.CalendarActivity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

import static com.example.tp.CrenauController.setDate;

public class CalendrierController implements Initializable {

    ZonedDateTime dateFocus;
    ZonedDateTime today;

    LocalDate day;

    private static LocalDate date;

    public static LocalDate getDate() {
        return date;
    }

    public static void setDate(LocalDate date) {
        CalendrierController.date = date;
    }


    @FXML
    private Text year;

    static Utilisateur utilisateur;

    static void setUtilisateur(Utilisateur u) {
        utilisateur = u;
    }

    static Mydesktop_Planner desktop_planner;

    static public void setMydesktopPlanner(Mydesktop_Planner d) {
        desktop_planner = d;
    }

    @FXML
    private Text month;
    @FXML
    private Button ajoutertache;
    @FXML
    private Button proposer;
    @FXML
    private Button statistiques;
    @FXML
    private Button replanifier;
    @FXML
    private Button etat;
    @FXML
    private Button historique;

    @FXML
    private FlowPane calendar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
    }

    @FXML
    void backOneMonth() {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth() {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    public void drawCalendar() {
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));
        boolean stop = false;
        int n = 0;
        if (utilisateur.getPlanning() != null) {
            n = utilisateur.getPlanning().getPeriode().getJours().size();
        }
        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 0.5;


        //List of activities for a given month

        int monthMaxDate = dateFocus.getMonth().maxLength();
        //Check for leap year
        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 8) - strokeWidth;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 5) - strokeWidth;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j + 1) + (7 * i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        date.setFont(Font.font("Arial", 16));
                        stackPane.getChildren().add(date);

                        if (utilisateur.getPlanning() != null) {
                            if ((currentDate == (utilisateur.getPlanning().getPeriode().getDate_debut().getDayOfMonth())) && (dateFocus.getMonth() == (utilisateur.getPlanning().getPeriode().getDate_debut().getMonth()))) {
                                stop = true;
                            }
                        }

                        if (n == 0) {
                            stop = false;
                        }
                        if (n != 0 && stop == true) {
                            n--;
                            rectangle.setFill(Color.valueOf("FFA500"));
                            date.setOnMouseClicked(mouseEvent -> {
                                //On ... click print all activities for given date
                                LocalDate time = LocalDate.of(dateFocus.getYear(), dateFocus.getMonth().getValue(), currentDate);
                                // System.out.println(time);
                                retourner(time);
                            });
                        }

                    }

                    if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate) {
                        rectangle.setFill(Color.valueOf("FFA500"));
                    }

                    /*
                    if(!(utilisateur.getCalendrier().getPeriodes().isEmpty())){
                        System.out.println(dateFocus.getDayOfMonth());

                        if(dateFocus.toLocalDate().equals(utilisateur.getCalendrier().getPeriodes().get(0).getDate_debut())){
                        System.out.println("Kaderrrr");
                            System.out.println(dateFocus.getDayOfMonth());
                        rectangle.setFill(Color.valueOf("FFA500"));
                    }
                    }

                }*/

                }
                calendar.getChildren().add(stackPane);
            }
        }
    }

    private void retourner(LocalDate date) {

        setDate(date);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("jour.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle(date.toString());
            JourController jourController = loader.getController();
            jourController.initialize(date);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            System.out.println("kayn prblm");
            e.printStackTrace();
        }

    }

    @FXML
    private Button dialogbutton;

    @FXML
    private void openDialog(ActionEvent event) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("dialogue.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Fixer Un Planning");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

    }

    @FXML
    private void ChoisirPlanning() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("choix_planning.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Choisir un planning");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }


    @FXML
    private void openTache(ActionEvent event) throws Exception {

        if (utilisateur.getPlanning() != null) {
            TypeTacheController.setAutomatique(false);
            Parent root = FXMLLoader.load(getClass().getResource("ChoixTypeTache.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Choisir le type du tache");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Vous ne pouvez pas prendre cette mesure en raison du error / manque de données");
            alert.setContentText("Vous ne pouvez pas planifier une tache manuellment sans fixer une planning ou creer un nouveau");
            alert.showAndWait();
        }
    }

    @FXML
    private void openAutomatique(ActionEvent event) throws Exception {

        if (utilisateur.getPlanning() != null) {
            TypeTacheController.setAutomatique(true);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChoixTypeTache.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Choisir le type du tache");
            stage.setScene(scene);
            TypeTacheController typeTacheController = loader.getController();
            stage.setOnCloseRequest(windowEvent -> {
                windowEvent.consume();
                typeTacheController.Annuler(stage);
            });
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Vous ne pouvez pas prendre cette mesure en raison du error / manque de données");
            alert.setContentText("Vous ne pouvez pas planifier des taches automatiquement sans fixer une planning ou creer un nouveau");
            alert.showAndWait();
        }
    }

    @FXML
    Button quitter_button;


    public void stop_programme() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quitter");
        alert.setHeaderText("Vous êtes sur le point de vous déconnecter");
        alert.setContentText("Voulez-vous enregistrer avant de quitter");

        if (alert.showAndWait().get() == ButtonType.OK) {
            if(utilisateur.getPlanning() == null){
                System.out.println("il n'y a pas une planning fixe pour l'afficher");
            }else{
                utilisateur.getPlanning().AfficherPlanning();
            }

            String Fichier_desktopplanner_path = "src/main/resources/desktop_planner.ser";
            File file = new File(Fichier_desktopplanner_path);

            try {
                //Serialization
                FileOutputStream fileOut = new FileOutputStream(file);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);

                out.writeObject((Mydesktop_Planner) desktop_planner);
                out.close();
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Get a reference to the stage
            Stage stage = (Stage) quitter_button.getScene().getWindow();
            // Close the dialog
            stage.close();
        }
    }

    public void stop_programme(Stage stage) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Déconnecter");
        alert.setHeaderText("Vous êtes sur le point de vous déconnecter");
        alert.setContentText("Voulez-vous enregistrer avant de quitter");

        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("Le planning de l'utilisateur ne sera pas affiche car la deconetion est faite onCloseRequest");

            String Fichier_desktopplanner_path = "src/main/resources/desktop_planner.ser";
            File file = new File(Fichier_desktopplanner_path);

            try {
                //Serialization
                FileOutputStream fileOut = new FileOutputStream(file);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);

                out.writeObject(desktop_planner);
                out.close();
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Close the dialog
            stage.close();
        }
    }

    @FXML
    public void Deconnecter(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setHeaderText("Vous êtes sur le point de vous déconnecter");
        alert.setContentText("Voulez-vous deconnecter de votre compte");
        if(alert.showAndWait().get() == ButtonType.OK){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage)quitter_button.getScene().getWindow();
                HelloController helloController = loader.getController();
                scene.setOnKeyPressed(keyEvent -> {
                    if(keyEvent.getCode() == KeyCode.ENTER){
                        try{
                            helloController.onHelloButtonClick();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                stage.setResizable(false);
                stage.setOnCloseRequest(windowEvent -> {
                    windowEvent.consume();
                    stop_programme(stage);
                });
                stage.setScene(scene);
                stage.show();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}