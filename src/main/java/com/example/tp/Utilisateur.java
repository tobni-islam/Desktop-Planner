package com.example.tp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utilisateur implements Serializable {
    private String pseudo;
    private int nb_badges;
    private Calendrier calendrier;

    private Planning planning;
    private List<Planning> plannings = new ArrayList<Planning>();
    private List<Tache> taches;
    private int nb_min_tache;
    private List<Planning> historique;

    //Crier un nouveau utilisateur avec son pseudo
    public Utilisateur(String pseudo){
        this.pseudo = pseudo;
    }

    public void AjouterPlanning(Planning p){
        this.plannings.add(p);
    }

    public void ProposerPlanning(){};
    public void AfficherStatistique(){};

    public void AfficherPlannings(){
        System.out.println("-----------------------------------------");
        System.out.println("La list des plannings de "+  this.pseudo+" : ");
        int i =1;
        for(Planning p:this.getListePlanning()){
            System.out.println(i+". Planning de "+ p.getPeriode().getDate_debut()+" ---> "+p.getPeriode().getDate_fin());
        }
        System.out.println("-----------------------------------------");
    }

    @Override
    public boolean equals(Object o){
        Utilisateur user = (Utilisateur) o;
        return Objects.equals(this.pseudo,user.pseudo);
    }

    @Override
    public  int hashCode(){
        return Objects.hash(this.pseudo);
    }

    public String getPseudo(){
        return this.pseudo;
    }

    public void setPseudo(String p){
        this.pseudo = p;
    }

    public Calendrier getCalendrier(){
        return this.calendrier;
    }

    public void setCalendrier(Calendrier calendrier){
        this.calendrier = calendrier;
    }

    public List<Planning> getListePlanning(){
        return this.plannings;
    }


    public void setPlanning(Planning p){
        this.planning = p;
    }

    public Planning getPlanning(){
        return this.planning;
    }


}

