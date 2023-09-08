package com.example.tp;

import java.io.Serializable;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Periode implements Statistiques, Serializable {
    private LocalDate date_debut;

    private ArrayList<Categorie> categories= new ArrayList<>();
    private LocalDate date_fin;
    private ArrayList<Jour> jours = new ArrayList<Jour>();
    public ArrayList<Categorie> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Categorie> categories) {
        this.categories = categories;
    }

    public Periode(LocalDate debut,LocalDate fin){
        this.date_debut = debut;
        this.date_fin = fin;
        setListJour(debut, fin);
    }

    //Fixer tous les jours de la perdiode selon la date debut et date fin
    private void setListJour(LocalDate debut, LocalDate fin){
        LocalDate temp_date = debut;
        Jour temp_jour = new Jour(debut);
        while(temp_date.compareTo(fin) != 0){
            this.jours.add(temp_jour);
            temp_date = temp_date.plusDays(1);
            temp_jour = new Jour(temp_date);
        }
        this.jours.add(temp_jour);
    }


    public Jour RechercheJour(LocalDate date){
        for(Jour j: this.jours){
            if(date.compareTo(j.getDate()) == 0){
                return j;
            }
        }
        return null;
    }

    public ArrayList<Jour> CopyOfListJours() {
        ArrayList<Jour> copyListJours = new ArrayList<Jour>();

        for (Jour jour : this.jours) {
            Jour copyJour = jour.CopyOfJour();
            copyListJours.add(copyJour);
        }

        return copyListJours;
    }

    public LocalDate getDate_debut(){
        return this.date_debut;
    }

    public LocalDate getDate_fin(){
        return this.date_fin;
    }

    public ArrayList<Jour> getJours(){
        return this.jours;
    }

    public void setJours(ArrayList<Jour> j){
        this.jours = j;
    }
}

