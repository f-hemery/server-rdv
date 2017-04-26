package fr.ua.iutlens.rdv.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by hemery on 02/04/2017.
 */
@NamedQueries( {
        @NamedQuery(
                name = "searchCreneauxFromTo",
                query = "select c from Creneau c where c.dateCreneau  between :from and :to order by c.dateCreneau"),
        @NamedQuery(
                name = "searchCreneauById",
                query = "select c from Creneau c where c.id = :id")
})
@Entity
public class Creneau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String lieu;
    @ManyToOne
    @JoinColumn(nullable = false, name = "formation_id")
    private Formation formation;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreneau;
//    private int nbCreneaux;
    private int dureeCreneau;
    private int nbPlaces;
    @Transient
    private int nbPlacesDispo;
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
        return formation;
    }

    public void setFormation(fr.ua.iutlens.rdv.model.Formation formation) {
        this.formation = formation;
    }

    public Date getDateCreneau() {
        return dateCreneau;
    }

    public void setDateCreneau(Date dateCreneau) {
        this.dateCreneau = dateCreneau;
    }

    public int getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
        this.setNbPlacesDispo(nbPlaces);
    }

    public int getNbPlacesDispo() {
        return nbPlacesDispo;
    }

    public void setNbPlacesDispo(int nbPlacesDispo) {
        this.nbPlacesDispo = nbPlacesDispo;
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
                ", formation=" + formation +
                ", dateCreneau=" + dateCreneau +
                ", dureeCreneau=" + dureeCreneau +
                ", nbPlaces=" + nbPlaces +
                ", nbPlacesDispo=" + nbPlacesDispo +
                ", visible=" + visible +
                '}';
    }
}
