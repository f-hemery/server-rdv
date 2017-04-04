package fr.ua.iutlens.rdv.model;

import javax.persistence.*;

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
    private int id;
    @Column(name = "code_formation")
    private String codeFormation;
    @Column(name = "libelle_formation")
    private String libelleFormation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public String toString() {
        return "Formation{" +
                "id=" + id +
                ", codeFormation='" + codeFormation + '\'' +
                ", libelleFormation='" + libelleFormation + '\'' +
                '}';
    }
}
