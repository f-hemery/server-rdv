package fr.ua.iutlens.rdv.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by hemery on 21/04/2017.
 */
@Entity
@Table(name = "candidat_formation")
public class CandidatFormation {
    @Embeddable
    public static class Id implements Serializable {
        @Column(name = "candidat_id")
        private Long candidatId;
        @Column(name = "formation_id")
        private Long formationId;
        public Id() {}
        public Id(Long candidatId, Long formationId) {
            this.candidatId = candidatId;
            this.formationId = formationId;
        }
        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id)o;
                return this.candidatId.equals(that.candidatId) &&
                        this.formationId.equals(that.formationId);
            } else {
                return false;
            }
        }
        public int hashCode() {
            return candidatId.hashCode() + formationId.hashCode();
        }
    }

    @EmbeddedId
    private Id id = new Id();

    private boolean rdv;
    private String decision;
    private double note;

    @ManyToOne
    @JoinColumn(name="formation_id",
            insertable = false,
            updatable = false)
    private Formation formation;
    @ManyToOne
    @JoinColumn(name="candidat_id",
            insertable = false,
            updatable = false)
    private Candidat candidat;

    public CandidatFormation() {}
    public CandidatFormation(Candidat candidat,
                           Formation formation) {
        this.candidat = candidat;
        this.formation = formation;
        this.decision = "En attente";
        this.rdv = false;
// Set identifier values
        this.id.candidatId = candidat.getId();
        this.id.formationId = formation.getId();
// Guarantee referential integrity
        candidat.getCandidatFormations().add(this);
        formation.getCandidatFormations().add(this);
    }

    public Id getId() {
        return id;
    }

    public boolean isRdv() {
        return rdv;
    }

    public void setRdv(boolean rdv) {
        this.rdv = rdv;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public Formation getFormation() {
        return formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    public Candidat getCandidat() {
        return candidat;
    }

    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }
}
