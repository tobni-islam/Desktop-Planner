package com.example.tp;

import java.io.Serializable;
import java.time.*;
public class Tache_Simple extends Tache implements Serializable{
    private boolean periodique;
    private int periodicite;

    public Tache_Simple(String nom, long duree, Periorite periorite, LocalDate deadline, Categorie categorie, Etat etat, boolean periodique, int periodicite){
        super(nom,duree,periorite,deadline, categorie,etat);
        this.periodique = periodique;
        this.periodicite = periodicite;
    }

    @Override
    public void EvaluerEtat(Etat etat) {
        this.setEtat(etat);
    }


}
