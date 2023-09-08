package com.example.tp;

import javafx.beans.binding.ListBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.time.*;
public class Calendrier implements Serializable {
    private List<Planning> plannings = new ArrayList<Planning>();

    private LocalDate aujourdhui = LocalDate.now();


    //Afficher le calendrier
    public void AfficherCalendrier(){};

    //Ajouter une perdiode (Ajouter une exception si date debut < date aujourdhui)  (Exception possible)
    public void AjouterPlanning(Planning planning){
        plannings.add(planning);
    };

    public LocalDate getAujourdhui(){
        return this.aujourdhui;
    }

    public List<Planning> getPlannings(){
        return this.plannings;
    }
}

