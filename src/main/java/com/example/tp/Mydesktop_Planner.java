package com.example.tp;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
public class Mydesktop_Planner implements Serializable{
    static int duree_minimal = 30;
    private HashSet<Utilisateur> Users = new HashSet<Utilisateur>();


    //Ajouter un utilisateur a la liste
    public void AjouterUser(Utilisateur User){
        this.Users.add(User);
    };

    //Verifier si l'utilisateur existe dans la liste ou non (Autentification)
    public boolean Authentification(String pseudo){
        Utilisateur verifier_utilisateur = new Utilisateur(pseudo);

        return this.Users.contains(verifier_utilisateur);
    }

    public Utilisateur getUser(String pseudo){
        for(Utilisateur u : this.Users){
            if(pseudo.equals(u.getPseudo())){
                return u;
            }
        }
        return null;
    }


    //Afficher la liste de tous les utilisateurs (avec leur pseudo)
    public void AfficherUsers(){
        for(Utilisateur u: Users){
            System.out.println(u.getPseudo());
        }
    }
}

