package fr.ua.iutlens.rdv.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by hemery on 02/04/2017.
 */
@NamedQueries( {
        @NamedQuery(
                name = "getCandidatIdInCreneauId",
                query = "select c from ChoixCandCren c where c.creneau.id = :idCreneau and c.candidat.id = :idCandidat"),
        @NamedQuery(
                name = "getMaxOfCreneauId",
                query = "select max(c.ordrePassage) from ChoixCandCren c where c.creneau.id = :id")
})
@Entity
public class ChoixCandCren {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne()
    private Candidat candidat;
    @ManyToOne()
    private Creneau creneau;
//    private int numCreneau;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateReservation;
    private int ordrePassage;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Candidat getCandidat() {
        return candidat;
    }

    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }

    public Creneau getCreneau() {
        return creneau;
    }

    public void setCreneau(Creneau creneau) {
        this.creneau = creneau;
    }

/*    public int getNumCreneau() {
        return numCreneau;
    }

    public void setNumCreneau(int numCreneau) {
        this.numCreneau = numCreneau;
    }*/

    public Date getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }

    public int getOrdrePassage() {
        return ordrePassage;
    }

    public void setOrdrePassage(int ordrePassage) {
        this.ordrePassage = ordrePassage;
    }
}
