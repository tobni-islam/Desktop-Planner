package com.example.tp;

import javafx.scene.control.skin.TableHeaderRow;

import java.io.Serializable;
import java.time.*;
public abstract class Tache implements Serializable {
    private String nom;
    private long duree;
    private Periorite periorete;
    private LocalDate deadline;
    private Categorie categorie;
    private Etat etat;

    public Tache(String nom, long duree, Periorite periorite, LocalDate deadline, Categorie categorie, Etat etat){
        this.nom = nom;
        this.duree = duree;
        this.periorete = periorite;
        this.deadline = deadline;
        this.categorie = categorie;
        this.etat = etat;
    }

    public void AfficherTache(){
        System.out.println("---------------------------------");
        System.out.println("Nom: "+this.nom);
        System.out.println("Duree: "+this.duree);
        System.out.println("periorite: "+this.periorete);
        System.out.println("deadline: " + this.deadline);
        System.out.println("categorie --> Name: "+this.categorie.getName()+" , Color: "+this.categorie.getCouleur());
        System.out.println("etat: "+ this.etat);
        System.out.println("----------------------------------");
    }

    public abstract void EvaluerEtat(Etat etat);

    public String getNom(){return this.nom;}
    public void setNom(String nom){this.nom = nom;}

    public long getDuree(){return this.duree;}
    public void setDuree(long duree){this.duree = duree;}

    public Periorite getPeriorite(){return this.periorete;}
    public void setPeriorete(Periorite p){this.periorete = p;}

    public LocalDate getDeadline(){return this.deadline;}
    public void setDeadline(LocalDate deadline){this.deadline = deadline;}

    public Categorie getCategorie(){return this.categorie;}
    public void setCategoeir(Categorie c){this.categorie = c;}

    public Etat getEtat(){return this.etat;}
    public void setEtat(Etat etat){this.etat = etat;}


}

