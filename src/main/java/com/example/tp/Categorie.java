package com.example.tp;

import javafx.scene.paint.Color;

import java.io.Serializable;

public class Categorie implements Serializable {
    private String name;
    private transient Color couleur ;

    public Categorie(String name, Color couleur) {
        this.name = name;
        this.couleur = couleur;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getCouleur() {
        return couleur;
    }

    public String getName() {
        return name;
    }

    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }



}
