package fr.ua.iutlens.rdv.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hemery on 02/04/2017.
 */
@NamedQueries(
        @NamedQuery(
                name = "searchFormationByCode",
                query = "select f from Formation f where f.codeFormation = :le_code")
)
@Entity
@Table(name = "formation")
public class Formation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "code_formation")
    private String codeFormation;
    @Column(name = "libelle_formation")
    private String libelleFormation;

    @JsonIgnore
    @OneToMany(mappedBy = "formation")
    private Set<CandidatFormation> candidatFormations =
            new HashSet<CandidatFormation>();

    @Transient
    private int nbCandidats;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodeFormation() {
        return codeFormation;
    }

    public void setCodeFormation(String code) {
        this.codeFormation = code;
    }

    public String getLibelleFormation() {
        return libelleFormation;
    }

    public void setLibelleFormation(String libelleFormation) {
        this.libelleFormation = libelleFormation;
    }

    public Set<CandidatFormation> getCandidatFormations() {
        return candidatFormations;
    }

    public void setCandidatFormations(Set<CandidatFormation> candidatFormations) {
        this.candidatFormations = candidatFormations;
    }

    public int getNbCandidats() {
        nbCandidats = candidatFormations.size();
        return nbCandidats;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "id=" + id +
                ", codeFormation='" + codeFormation + '\'' +
                ", libelleFormation='" + libelleFormation + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Formation))
            return false;
        Formation formation = (Formation)obj;
        return formation.getCodeFormation().equals(getCodeFormation()) && formation.getLibelleFormation().equals(getLibelleFormation());
    }
}
