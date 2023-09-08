package com.example.tp;

import java.io.Serializable;
import java.util.*;

public class Projet implements Serializable {
    private String nom;
    private String description;
    private List<Tache> list_taches = new ArrayList<Tache>();

    public Projet(String nom,String description,List<Tache> list_taches){
        this.nom = nom;
        this.description = description;
        this.list_taches = list_taches;
    }

    public void SetEtat(){};
    public void AjouterTache(Tache tache){
        this.list_taches.add(tache);
    };
}
