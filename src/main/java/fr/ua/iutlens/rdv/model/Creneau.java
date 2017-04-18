package fr.ua.iutlens.rdv.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by hemery on 02/04/2017.
 */
@Entity
public class Creneau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String lieu;
    @ManyToOne
    @JoinColumn(nullable = false, name = "formation_id")
    private Formation Formation;
    private Date dateCreneau;
    private int nbCreneaux;
    private int dureeCreneau;
    private int nbPlaces;
    private int intervalle;
    private boolean visible;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public fr.ua.iutlens.rdv.model.Formation getFormation() {
        return Formation;
    }

    public void setFormation(fr.ua.iutlens.rdv.model.Formation formation) {
        Formation = formation;
    }

    public Date getDateCreneau() {
        return dateCreneau;
    }

    public void setDateCreneau(Date dateCreneau) {
        this.dateCreneau = dateCreneau;
    }

    public int getNbCreneaux() {
        return nbCreneaux;
    }

    public void setNbCreneaux(int nbCreneaux) {
        this.nbCreneaux = nbCreneaux;
    }

    public int getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public int getIntervalle() {
        return intervalle;
    }

    public void setIntervalle(int intervalle) {
        this.intervalle = intervalle;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getDureeCreneau() {
        return dureeCreneau;
    }

    public void setDureeCreneau(int dureeCreneau) {
        this.dureeCreneau = dureeCreneau;
    }

    @Override
    public String toString() {
        return "Creneau{" +
                "id=" + id +
                ", lieu='" + lieu + '\'' +
                ", Formation=" + Formation +
                ", dateCreneau=" + dateCreneau +
                ", nbCreneaux=" + nbCreneaux +
                ", dureeCreneau=" + dureeCreneau +
                ", nbPlaces=" + nbPlaces +
                ", intervalle=" + intervalle +
                ", visible=" + visible +
                '}';
    }
}
