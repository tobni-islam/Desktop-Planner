package com.example.tp;

import java.io.Serializable;
import java.time.*;
import java.time.format.DateTimeFormatter;
public class Creneaux implements Serializable {
    private static final int duree_min = 30; //La duree minimal de creneau

    private long duree;
    private String heure_debut;
    private String heure_fin;
    private boolean libre;
    private boolean bloque;
    private Tache tache;

    public Creneaux(String debut,String fin,boolean libre,boolean bloque){
        this.duree = CalculerDuree(debut, fin);
        this.heure_debut = debut;
        this.heure_fin = fin;
        this.libre = libre;
        this.bloque = bloque;
    }

    //Calculer la duree de temps en minutes entre deux temps. Exp: CalculerDuree("13:10","14:20") --> 70
    private long CalculerDuree(String debut,String fin){
        LocalTime t_debut = LocalTime.parse(debut);
        LocalTime t_fin = LocalTime.parse(fin);
        Duration t_duree = Duration.between(t_debut, t_fin);
        long duree = t_duree.toMinutes(); //Throw Exception si la duree < duree_min
        return duree;
    }


    public void AjouterTache(Tache t, boolean bloque){
        this.tache = t;
        this.bloque = bloque;
    };

    /*
        Il y a deux possibilites,
        -> si la tache prend une partie de creneau (exp: creneau 3h et l'ajout d'une tache de 2h)
        dans ce cas on fait la decomposition de creneau par la creation d'un nouveau creneau contenant la tache
        et ce creneau reste libre.
        -> sinon, donc la tache ajoute prend tous le creneau (exp: creneau 3h et l'ajout d'une tache de 3h) alors
        la tache sera affecte a le champ 'tache' directement.
        -> Il y a des Exceptions dans les cas:
        - affectation d'une tache de duree plus grand a la duree de creneau
        - la decomposition de creneau produise un creneau de duree inferieure a la duree minimal (duree_min)
    */

    public void AfficherCreneau(){
        System.out.println("Heure debut: "+this.heure_debut);
        System.out.println("Heure fin: "+this.heure_fin);
        System.out.println("La duree: "+this.duree);
    }


    public Creneaux CopyOfCreneau(){
        Creneaux c = new Creneaux(this.heure_debut,this.heure_fin,this.getLibre(),this.getBloque());
        c.setTache(this.tache);
        return c;
    }
    //Setters + Getters
    public long getDuree(){return this.duree;}
    public void setDuree(){
        this.duree = CalculerDuree(this.heure_debut, this.heure_fin);
    }

    public String getHeureDebut(){return this.heure_debut;}
    public void setHeureDebut(String heure_debut){this.heure_debut = heure_debut;}

    public String getHeureFin(){return this.heure_fin;}
    public void setHeureFin(String heure_fin){this.heure_fin = heure_fin;}

    public boolean getLibre(){return this.libre;}
    public void setLibre(boolean libre){this.libre = libre;}

    public boolean getBloque(){return this.bloque;}
    public void setBloque(boolean bloque){this.bloque = bloque;}

    public Tache getTache(){return this.tache;}
    public void setTache(Tache t){this.tache = t;}
}
