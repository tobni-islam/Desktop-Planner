package com.example.tp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.time.*;
public class Tache_Decomposable extends Tache implements Serializable {
    private List<Tache_Simple> sous_tache = new ArrayList<Tache_Simple>();

    public Tache_Decomposable(String nom, long duree, Periorite periorite, LocalDate deadline, Categorie categorie, Etat etat){
        super(nom,duree,periorite, deadline,categorie, etat);
    }


    public void EvaluerEtat(){};
    public void DecomposerTache(long duree){
        Tache_Simple tache_s1 = null;
        Tache_Simple tache_s2 = null;
        long nouv_duree;

        if(this.sous_tache.size() == 0){
            nouv_duree = this.getDuree() - duree;

            tache_s1 = new Tache_Simple(GenererNom(),duree,this.getPeriorite(),this.getDeadline(),this.getCategorie(),this.getEtat(),false,0);
            this.sous_tache.add(tache_s1);

            tache_s2 = new Tache_Simple(GenererNom(),nouv_duree,this.getPeriorite(),this.getDeadline(),this.getCategorie(),this.getEtat(),false,0);
            this.sous_tache.add(tache_s2);
        }else{
            tache_s1 = this.sous_tache.get(this.sous_tache.size()-1);
            nouv_duree = tache_s1.getDuree() - duree;
            tache_s1.setDuree(duree);

            tache_s2 = new Tache_Simple(GenererNom(),nouv_duree,this.getPeriorite(),this.getDeadline(),this.getCategorie(),this.getEtat(),false,0);
            this.sous_tache.add(tache_s2);
        }

    };
    public String GenererNom(){
        int nb = this.sous_tache.size() +1;
        String nb_s = Integer.toString(nb);

        String res = this.getNom() + nb_s;
        return res;
    }

    public void SupprimerTache(Tache tache){
        if(this.sous_tache.contains(tache)){
            this.sous_tache.remove(tache);
            this.setDuree(CalculerDuree());
            this.DeterminerEtat();
        }
    }

    private long CalculerDuree(){
        long res= 0;
        for(Tache_Simple t:this.sous_tache){
            res += t.getDuree();
        }
        return res;
    }

    public void DeterminerEtat(){
        if(this.sous_tache.size() != 0) {
            int notrealized = 0;
            int inprogress = 0;
            int completed = 0;
            int delayed = 0;
            for (Tache_Simple t : this.sous_tache) {
                switch (t.getEtat()) {
                    case notRealzed -> notrealized++;
                    case completed -> completed++;
                    case progress -> inprogress++;
                    case delayed -> delayed++;
                }
            }

            if (inprogress == 0 && completed == 0 && notrealized != 0) {
                setEtat(Etat.notRealzed);
            } else {
                if (completed == this.sous_tache.size()) {
                    setEtat(Etat.completed);
                } else {
                    if (delayed == this.sous_tache.size()) {
                        setEtat(Etat.delayed);
                    } else {
                        setEtat(Etat.progress);
                    }
                }
            }
        }
    }

    public void AfficherSousTache(){
        for(Tache_Simple t: this.sous_tache){
            t.AfficherTache();
        }
    }

    @Override
    public void EvaluerEtat(Etat etat) {
        this.setEtat(etat);
    }

    public List<Tache_Simple> getSous_tache(){
        return this.sous_tache;
    }
}

