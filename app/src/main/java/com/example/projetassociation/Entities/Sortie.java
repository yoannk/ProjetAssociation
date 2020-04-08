package com.example.projetassociation.Entities;

public class Sortie {
    private int idAssociation;
    private int idSortie;
    private String nom;
    private double prix;
    private String photo;
    private String description;
    private String date;
    private boolean statut;
    private int nbinscrits;
    private int capaciteMaximum;

    public int getIdAssociation() {
        return idAssociation;
    }

    public void setIdAssociation(int idAssociation) {
        this.idAssociation = idAssociation;
    }

    public int getIdSortie() {
        return idSortie;
    }

    public void setIdSortie(int idSortie) {
        this.idSortie = idSortie;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isStatut() {
        return statut;
    }

    public void setStatut(boolean statut) {
        this.statut = statut;
    }

    public int getNbinscrits() {
        return nbinscrits;
    }

    public void setNbinscrits(int nbinscrits) {
        this.nbinscrits = nbinscrits;
    }

    public int getCapaciteMaximum() {
        return capaciteMaximum;
    }

    public void setCapaciteMaximum(int capaciteMaximum) {
        this.capaciteMaximum = capaciteMaximum;
    }
}
