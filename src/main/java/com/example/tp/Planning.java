package com.example.tp;

import javafx.scene.paint.Color;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.*;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
public class Planning implements Serializable {

    private ArrayList<Tache> ensemble_taches = new ArrayList<Tache>();
    private Periode periode;
    private Set<Projet> ensemble_projets;
    private ArrayList<Tache> unschuduled = new ArrayList<Tache>();

    private int dureeMinimale;

    public Periode getPeriode(){
        return this.periode;
    }




    public ArrayList<Tache> getUnschuduled(){
        return this.unschuduled;
    }

    public Planning(Periode periode){
        this.periode = periode;
    }

    public void ModifierPlanning(){};
    public void MiseAjour(){};
    public void AjouterProjet(){};


    public Boolean PlanifierTacheSimpleManual(String nom, long duree, int nb_proirite, LocalDate joure,int nb_creneau,  Categorie categorie, Boolean bloque,Boolean periodique , int periodicite){

        Periorite periorite = null ;

        if(nb_proirite == 0){
            periorite = Periorite.High;
        }else if(nb_proirite == 1) {
            periorite = Periorite.Meduim;
        } else if(nb_proirite == 2){
            periorite = Periorite.Low;
        }

        //Rechercher le jour de la date "deadline"
        Jour jour = this.periode.RechercheJour(joure);

        //On recherche l'indice de ce creneau dans la liste de tous les creneaux de jour pour l'affecter a la method "AjouterTache" de jour
        List<Creneaux> list_tout_creneaux = jour.getList_creneaux();
        Creneaux creneaux_choisi = jour.getCreneauxLibre().get(nb_creneau );
        int index_creneau = list_tout_creneaux.indexOf(creneaux_choisi);

        //Ajouter la tache dans le creneau choisi
        boolean error = false;

        Tache_Simple tache = new Tache_Simple(nom, duree, periorite, joure, categorie, Etat.notRealzed, periodique, periodicite);
        error = jour.AjouterTacheSimple(index_creneau,tache,bloque);

        if(!error){
            System.out.println("Il y'a un error quelque part");
            return false;
        }

        this.ensemble_taches.add(tache);
        int i = this.periode.getJours().indexOf(jour) + periodicite;
        if(periodique){
            while (i < this.periode.getJours().size()){
                this.periode.getJours().get(i).AjouterTacheSimple(tache,bloque);
                i = i + periodicite;
            }
        }

        return true;
    }

    public int PlanifierTacheDecomposableManual(String nom,long duree,int nb_priorite, LocalDate deadline, Categorie categorie, Boolean bloque){

        Periorite periorite = null ;

        if(nb_priorite == 0){
            periorite = Periorite.High;
        }else if(nb_priorite == 1) {
            periorite = Periorite.Meduim;
        } else if(nb_priorite == 2){
            periorite = Periorite.Low;
        }

        //Rechercher le jour de la date "deadline"
        Jour jour_deadline = this.periode.RechercheJour(deadline);

        LocalDate date;
        if(LocalDate.now().isBefore(this.periode.getJours().get(0).getDate())){
            date = this.periode.getJours().get(0).getDate();
        }else{
            date = LocalDate.now();
        }

        Jour jour = this.periode.RechercheJour(date);
        int index_jour = this.periode.getJours().indexOf(jour);



        Tache_Decomposable tache = new Tache_Decomposable(nom,duree,periorite,deadline,categorie,Etat.notRealzed);

        Boolean done = false;

        int operation =0;

        while(!done && jour.getDate().isBefore(jour_deadline.getDate().plusDays(1)) && index_jour < this.periode.getJours().size()){
            operation = 0;
            done = jour.AjouterTacheDecomposableSans(tache,bloque);
            index_jour += 1;
            if(index_jour < this.periode.getJours().size()){
                jour = this.periode.getJours().get(index_jour);
            }
        }



        //Rechercher le jour de la date "deadline"
        jour_deadline = this.periode.RechercheJour(deadline);

        if(LocalDate.now().isBefore(this.periode.getJours().get(0).getDate())){
            date = this.periode.getJours().get(0).getDate();
        }else{
            date = LocalDate.now();
        }

        jour = this.periode.RechercheJour(date);
        index_jour = this.periode.getJours().indexOf(jour);

        ArrayList<Jour> cpy_list_jours = this.periode.CopyOfListJours();

        while(!done && jour.getDate().isBefore((jour_deadline.getDate().plusDays(1))) && index_jour < this.periode.getJours().size()){
            operation =1;
            done = jour.AjouterTacheDecomposableAvec(tache, bloque);
            index_jour +=1 ;
            if(index_jour < this.periode.getJours().size()){
                jour = this.periode.getJours().get(index_jour);
            }
        }

        if(done){
            this.ensemble_taches.add(tache);
            System.out.println("la tache decomposable a ete ajoutee avec succes");
            return operation;
        }
            periode.setJours(cpy_list_jours);
            System.out.println("la tache decomposable ne peut pas etre planifier meme avec decomposition");
            return 2;
    }


    public void PlanificationAutomatique(Map<Tache,Boolean> ens_taches){
        //Declaration
        ArrayList<Tache> High = new ArrayList<Tache>();
        ArrayList<Tache> Meduim = new ArrayList<Tache>();
        ArrayList<Tache> Low = new ArrayList<Tache>();

        for(Map.Entry<Tache,Boolean> entry : ens_taches.entrySet()){
            Tache t = entry.getKey();
            if(t.getPeriorite() == Periorite.High){
                High.add(t);
            }else{
                if(t.getPeriorite() == Periorite.Meduim){
                    Meduim.add(t);
                }else{
                    Low.add(t);
                }
            }
        }

        Collections.sort(High, Comparator.comparing(Tache::getDeadline));
        Collections.sort(Meduim, Comparator.comparing(Tache::getDeadline));
        Collections.sort(Low, Comparator.comparing(Tache::getDeadline));

        //High list
        System.out.println("Les taches de type HIGH:");
        for(Tache t: High){
            PlanifierTache(t,ens_taches.get(t));
            t.AfficherTache();
        }

        System.out.println("Les taches de type MEDUIM:");
        for(Tache t: Meduim){
            PlanifierTache(t,ens_taches.get(t));
            t.AfficherTache();
        }

        System.out.println("Les taches de type Low:");
        for(Tache t: Low){
            PlanifierTache(t,ens_taches.get(t));
            t.AfficherTache();
        }
        

        System.out.println("----------------------------------------------");

        System.out.println("Les taches non-planifies sont:");
        for (Tache t: this.unschuduled){
            System.out.println("Nom: "+t.getNom()+" |"+"Bloque: "+ens_taches.get(t));
        }

    }

    private void PlanifierTache(Tache t,Boolean bloque){
        Boolean done = false;

         if(t instanceof Tache_Simple){
             System.out.println("Planifier tache simple :"+ t.getNom());

             LocalDate date;
             if(LocalDate.now().isBefore(this.periode.getJours().get(0).getDate())){
                 date = this.periode.getJours().get(0).getDate();
             }else{
                 date = LocalDate.now();
             }

             Jour jour = this.periode.RechercheJour(date);
             int index_jour = this.periode.getJours().indexOf(jour);

             while(!done && jour.getDate().isBefore(t.getDeadline().plusDays(1)) && index_jour < this.periode.getJours().size()){
                 done = jour.AjouterTacheSimple((Tache_Simple) t,bloque);
                 if(done){
                     System.out.println("la tache "+t.getNom()+" a ete ajoutee avec success");
                 }
                 index_jour += 1;
                 if(index_jour < this.periode.getJours().size()){
                     jour = this.periode.getJours().get(index_jour);
                 }
             }

         }else{

             int nb_proirite;
             if(t.getPeriorite() == Periorite.High){
                 nb_proirite = 0;
             }else{
                 if(t.getPeriorite() == Periorite.Meduim){
                     nb_proirite = 1;
                 }else{
                     nb_proirite = 2;
                 }
             }

             int operation = PlanifierTacheDecomposableManual(t.getNom(),t.getDuree(),nb_proirite,t.getDeadline(),t.getCategorie(),bloque);
             if(operation != 2){
                 done = true;
                 System.out.println("la tache "+ t.getNom()+" a ete ajoutee avec success");
             }
         }

         if(!done){
             this.unschuduled.add(t);
         }
    }


    public void SupprimerTache(Tache tache, LocalDate date){

        Boolean decomposable = false;
        for(Tache t: this.ensemble_taches){
            if(t instanceof Tache_Decomposable){
                if(((Tache_Decomposable) t).getSous_tache().contains(tache)){
                    ((Tache_Decomposable)t).SupprimerTache(tache);
                    decomposable = true;
                    break;
                }
            }
        }

        this.ensemble_taches.remove(tache);
        Jour jour = this.periode.RechercheJour(date);
        jour.SupprimerTache(tache);
    }


    public void Statistique(){};

    public void AfficherPlanning(){
        System.out.println("#########################################################");
        for(Jour j: this.periode.getJours()){
            System.out.println("------- "+ j.getDate()+" -------");
            for(Creneaux c: j.getList_creneaux()){
                if(c.getLibre()){
                    System.out.println("[ "+ c.getHeureDebut()+ " --> "+ c.getHeureFin()+" ] ---> Libre");
                }else{
                    System.out.println("[ "+ c.getHeureDebut()+ " --> "+ c.getHeureFin()+ " ] ---> "+c.getTache().getNom());
                }
            }

        }
        System.out.println("#########################################################");
    }

    public ArrayList<Tache> getEnsemble_taches(){
        return this.ensemble_taches;
    }

    public void setDureeMinimale(int duree){
        this.dureeMinimale = duree;
        for(Jour j: this.periode.getJours()){
            j.setDureeMinimale(duree);
        }
    }

    public int getDureeMinimale(){
        return this.dureeMinimale;
    }
}

