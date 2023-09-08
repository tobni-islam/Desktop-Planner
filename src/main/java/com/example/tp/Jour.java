package com.example.tp;

import java.io.Serializable;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.List;
public class Jour implements Statistiques, Serializable {
    private LocalDate date;
    private int dureeMinimale;


    private List<Creneaux> list_creneaux = new ArrayList<Creneaux>();
    private List<Tache> list_taches = new ArrayList<Tache>();
    private Badge badge;

    public Jour(LocalDate d){
        this.date = d;
    }

    //Ajouter un creneau libre a la liste des creneaux de jour
    public int AjouterCreneauxLibre(String debut,String fin, Boolean bloque){
        Creneaux c = new Creneaux(debut, fin, true,bloque);

        if(c.getDuree() < this.dureeMinimale){
            return 1;
        }else{
            if(this.CreerConflit(c)){
                return 2;
            }
        }

        this.inserCreneau(c);
        return 0;
    };

    //Verifier s'il y a une conflit entre deux creneaux (exp: 10:00 --> 12:00 et 11:30 --> 14:00)
    private boolean CreerConflit(Creneaux creneau){
        LocalTime debut_1,debut_2, fin_1,fin_2;
        Boolean cond1 ,cond2;
        for(Creneaux c : this.list_creneaux){

            debut_1 = LocalTime.parse(c.getHeureDebut());
            debut_2 = LocalTime.parse(creneau.getHeureDebut());

            fin_1 = LocalTime.parse(c.getHeureFin());
            fin_2 = LocalTime.parse(creneau.getHeureFin());

            cond1 = debut_1.compareTo(debut_2) >=  0 && debut_1.compareTo(fin_2) <= 0;
            cond2 = debut_2.compareTo(debut_1) >= 0 && debut_2.compareTo(fin_1) <=0;
            if(cond1 || cond2){
                return  true;
            }
        }
        return false;
    }

    // Insertion d'un creneaux dans la liste de tous les creneaux pour assurer l'ordre
    private void inserCreneau(Creneaux c){
        if(this.list_creneaux.size() == 0){
            this.list_creneaux.add(c);
        }else{
            int pos = 0;
            while((LocalTime.parse(c.getHeureDebut()).compareTo(LocalTime.parse(this.list_creneaux.get(pos).getHeureDebut())) > 0) && pos < this.list_creneaux.size()){
                pos++;
                if(pos == this.list_creneaux.size()){
                    break;
                }
            }

            if(pos == this.list_creneaux.size()){
                this.list_creneaux.add(c);
            }else{
                this.list_creneaux.add(pos,c);
            }
        }
    }

    public Boolean AjouterTacheSimple(int nb_creneau,Tache_Simple tache,Boolean bloque){
        Creneaux creneau = this.list_creneaux.get(nb_creneau);

        if(creneau.getDuree() >= tache.getDuree()){
            if(tache.getDuree() % this.dureeMinimale != 0){
                tache.setDuree(tache.getDuree() - (tache.getDuree() % this.dureeMinimale) + this.dureeMinimale);
            }

            if(creneau.getDuree() != tache.getDuree()){
                DecomposerCreneau(creneau, tache.getDuree(), bloque);
            }

            creneau.AjouterTache(tache,bloque);
            creneau.setLibre(false);
            this.list_taches.add(tache);
            return true;
        }
        return false;
    }

    //Ajouter une tache simple sans specifier le creneau, si le systeme ne trouve pas une creneau libre
    // la tache ne sera pas planifier

    public Boolean AjouterTacheSimple(Tache_Simple tache, Boolean bloque){
        int i=0;
        while(i < this.list_creneaux.size()){
            if(this.list_creneaux.get(i).getDuree() >= tache.getDuree() && this.list_creneaux.get(i).getLibre()){
                if(AjouterTacheSimple(i,tache,bloque)){
                    return true;
                }
            }
            i++;
        }
        return false;
    }


    //Le systeme recherche d'une creneaux libre avant la date limite pour planifier cette tache, s'il ne trouve il va retourne false
    public Boolean AjouterTacheDecomposableSans(Tache_Decomposable tache,Boolean bloque){
        if(tache.getDuree() % this.dureeMinimale != 0){
            tache.setDuree(tache.getDuree() - (tache.getDuree() % this.dureeMinimale) + this.dureeMinimale);
        }
        for(Creneaux c: this.list_creneaux){
            if(c.getDuree() >= tache.getDuree() && c.getLibre()){
                if(c.getDuree() != tache.getDuree()){
                    DecomposerCreneau(c, tache.getDuree(), bloque);
                }
                c.AjouterTache(tache,bloque);
                c.setLibre(false);
                this.list_taches.add(tache);
                return true;
            }
        }
        return false;
    }

    public Boolean AjouterTacheDecomposableAvec(Tache_Decomposable tache,Boolean bloque){
        if(tache.getDuree() % this.dureeMinimale != 0){
            tache.setDuree(tache.getDuree() - (tache.getDuree() % this.dureeMinimale) + this.dureeMinimale);
        }

        int i= 0;

        Tache_Simple tache_reste = null;
        if(tache.getSous_tache().size() != 0){
            tache_reste = tache.getSous_tache().get(tache.getSous_tache().size()-1);
        }

        Boolean done = false;

        while(i < this.list_creneaux.size() && !done){
            if(this.list_creneaux.get(i).getLibre()){

                if(tache.getSous_tache().size() == 0){
                    tache.DecomposerTache(this.list_creneaux.get(i).getDuree());
                    AjouterTacheSimple(i,tache.getSous_tache().get(0),bloque);
                    tache_reste = tache.getSous_tache().get(1);
                }else{

                    if(this.list_creneaux.get(i).getDuree() >= tache_reste.getDuree()){
                        AjouterTacheSimple(i,tache_reste,bloque);
                        done = true;
                    }else{
                        tache.DecomposerTache(this.list_creneaux.get(i).getDuree());
                        AjouterTacheSimple(i,tache.getSous_tache().get(tache.getSous_tache().size() -2),bloque);
                        tache_reste = tache.getSous_tache().get(tache.getSous_tache().size()-1);
                    }
                }
            }
            i++;
        }

        return done;
    }

    private long CalculerDureeLibre(){
        long res = 0;
        for(Creneaux c: this.list_creneaux){
            if(c.getLibre()){
                res += c.getDuree();
            }
        }
        return res;
    }



    public void DecomposerCreneau(Creneaux creneaux,long duree,Boolean bloque){
        //Calculer l'heure fin + la transformer au type String
        LocalTime debut = LocalTime.parse(creneaux.getHeureDebut());
        LocalTime nouv_debut = debut.plusMinutes(duree);
        DateTimeFormatter formatteur = DateTimeFormatter.ofPattern("HH:mm");
        String heure_nouv_debut = formatteur.format(nouv_debut);

        //Creer un nouveau creneau de heure_debut et prend la duree 'duree'
        Creneaux new_creneau = new Creneaux(heure_nouv_debut, creneaux.getHeureFin(), true, false);

        //le creneau current sera de heure_fin de nouveau creneau jusqu'a la fin
        creneaux.setHeureFin(heure_nouv_debut);
        creneaux.setDuree();

        this.list_creneaux.add(this.list_creneaux.indexOf(creneaux)+1,new_creneau);
    };

    //retourner tous les creneaux libres de jour sans les afficher
    public List<Creneaux> getCreneauxLibre(){
        List<Creneaux> list_creneaux = new ArrayList<Creneaux>();
        for(Creneaux c: this.list_creneaux){
            if(c.getLibre()){
                list_creneaux.add(c);
            }
        }
        return list_creneaux;
    }


    public void SupprimerTache(Tache tache){
        this.list_taches.remove(tache);
        for(Creneaux c: this.list_creneaux){
            if(!c.getLibre()){
                if(c.getTache().equals(tache)){
                    c.setTache(null);
                    c.setLibre(true);
                    c.setBloque(false);
                }
            }
        }
        ReglerCreneaux();
    }

    private void ReglerCreneaux(){
        if(this.list_creneaux.size() >= 2){
            Creneaux tmp_c;
            int i =0;
            while( i< this.list_creneaux.size()-1){
                if(this.list_creneaux.get(i).getLibre() && this.list_creneaux.get(i+1).getLibre() && this.list_creneaux.get(i).getHeureFin().equals(this.list_creneaux.get(i+1).getHeureDebut())){
                    this.getList_creneaux().get(i).setHeureFin(this.list_creneaux.get(i+1).getHeureFin());
                    this.list_creneaux.get(i).setDuree();
                    tmp_c = this.list_creneaux.get(i+1);
                    this.list_creneaux.remove(tmp_c);
                    i = 0;
                }else{
                    i++;
                }
            }
        }
    }


    public void Feliciter(){};

    public Jour CopyOfJour() {
        ArrayList<Creneaux> copyListCreneaux = new ArrayList<Creneaux>();

        for (Creneaux creneau : this.list_creneaux) {
            Creneaux copyCreneau = creneau.CopyOfCreneau();
            copyListCreneaux.add(copyCreneau);
        }

        Jour copyJour = new Jour(this.date);
        copyJour.setList_taches(this.list_taches);
        copyJour.setBadge(this.badge);
        copyJour.setListCreneaux(copyListCreneaux);

        return copyJour;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Creneaux> getList_creneaux() {
        return list_creneaux;
    }

    public void setListCreneaux(ArrayList<Creneaux> list_creneaux){
        this.list_creneaux = list_creneaux;
    }

    public List<Tache> getList_taches(){
        return this.list_taches;
    }

    public void setBadge(Badge b){
        this.badge = b;
    }

    public void setList_taches(List<Tache> t){
        this.list_taches = t;
    }

    public void setDureeMinimale(int duree){
        this.dureeMinimale = duree;
    }

}

