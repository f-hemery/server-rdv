package fr.ua.iutlens.rdv.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hemery on 02/04/2017.
 */
@NamedQueries({
        @NamedQuery(
                name = "searchCandidatById",
                query = "select c from Candidat c where c.id = :id"),
        @NamedQuery(
                name = "searchCandidatByName",
                query = "select c from Candidat c where c.nom = :nom and c.prenom = :prenom and c.ddn = :ddn"),
        @NamedQuery(
                name = "searchAllCandidats",
                query = "select c from Candidat c"),
        @NamedQuery(
                name = "searchCandidatByNoDossier",
                query = "select c from Candidat c where c.noDossier = :nodossier"),
        @NamedQuery(
                name = "searchCandidatByIne",
                query = "select c from Candidat c where c.ine = :ine")
})
@Entity
public class Candidat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "num_dossier")
    private String noDossier;
    private String ine;
    private String civilite;
    private String nom;
    private String prenom;
    @Temporal(TemporalType.DATE)
    private Date ddn;
    private String nationalite;
    private String langue;
    private String telephone;
    private String telephonePortable;
    private String mail;
    private String adresse;
    private String adresse1;
    private String adresse2;
    private String adresse3;
    private String codePostal;
    private String commune;
    private String communeEtr;
    private String pays;
    private String dernierEtab;
    private String dernierDip;
    @JsonIgnore
    @OneToMany(mappedBy = "candidat", fetch = FetchType.EAGER)
    private Set<CandidatFormation> candidatFormations =
            new HashSet<CandidatFormation>();
    @Temporal(TemporalType.DATE)
    private Date dateVoeux;
    @Temporal(TemporalType.DATE)
    private Date dateTrans;
    private String statutDossier;
    @Temporal(TemporalType.DATE)
    private Date dateModifStatutDossier;
    @Temporal(TemporalType.DATE)
    private Date dateReceptionDossier;
    @Temporal(TemporalType.DATE)
    private Date dateCompletDossier;
    @Temporal(TemporalType.DATE)
    private Date dateIncompletDossier;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNoDossier() {
        return noDossier;
    }

    public void setNoDossier(String noDossier) {
        this.noDossier = noDossier;
    }

    public String getIne() {
        return ine;
    }

    public void setIne(String ine) {
        this.ine = ine;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Date getDdn() {
        return ddn;
    }

    public void setDdn(Date ddn) {
        this.ddn = ddn;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTelephonePortable() {
        return telephonePortable;
    }

    public void setTelephonePortable(String telephonePortable) {
        this.telephonePortable = telephonePortable;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getAdresse1() {
        return adresse1;
    }

    public void setAdresse1(String adresse1) {
        this.adresse1 = adresse1;
    }

    public String getAdresse2() {
        return adresse2;
    }

    public void setAdresse2(String adresse2) {
        this.adresse2 = adresse2;
    }

    public String getAdresse3() {
        return adresse3;
    }

    public void setAdresse3(String adresse3) {
        this.adresse3 = adresse3;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getCommuneEtr() {
        return communeEtr;
    }

    public void setCommuneEtr(String communeEtr) {
        this.communeEtr = communeEtr;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getDernierEtab() {
        return dernierEtab;
    }

    public void setDernierEtab(String dernierEtab) {
        this.dernierEtab = dernierEtab;
    }

    public String getDernierDip() {
        return dernierDip;
    }

    public void setDernierDip(String dernierDip) {
        this.dernierDip = dernierDip;
    }

    public Set<CandidatFormation> getCandidatFormations() {
        return candidatFormations;
    }

    public void setCandidatFormations(Set<CandidatFormation> candidatFormations) {
        this.candidatFormations = candidatFormations;
    }
/*
    public Set<Formation> getFormations() {
        return formations;
    }

    public void setFormations(Set<Formation> formations) {
        this.formations = formations;
    }
*/

    public Date getDateVoeux() {
        return dateVoeux;
    }

    public void setDateVoeux(Date dateVoeux) {
        this.dateVoeux = dateVoeux;
    }

    public Date getDateTrans() {
        return dateTrans;
    }

    public void setDateTrans(Date dateTrans) {
        this.dateTrans = dateTrans;
    }

    public String getStatutDossier() {
        return statutDossier;
    }

    public void setStatutDossier(String statutDossier) {
        this.statutDossier = statutDossier;
    }

    public Date getDateModifStatutDossier() {
        return dateModifStatutDossier;
    }

    public void setDateModifStatutDossier(Date dateModifStatutDossier) {
        this.dateModifStatutDossier = dateModifStatutDossier;
    }

    public Date getDateReceptionDossier() {
        return dateReceptionDossier;
    }

    public void setDateReceptionDossier(Date dateReceptionDossier) {
        this.dateReceptionDossier = dateReceptionDossier;
    }

    public Date getDateCompletDossier() {
        return dateCompletDossier;
    }

    public void setDateCompletDossier(Date dateCompletDossier) {
        this.dateCompletDossier = dateCompletDossier;
    }

    public Date getDateIncompletDossier() {
        return dateIncompletDossier;
    }

    public void setDateIncompletDossier(Date dateIncompletDossier) {
        this.dateIncompletDossier = dateIncompletDossier;
    }

    @Override
    public String toString() {
        return "Candidat{" +
                "id=" + id +
                ", noDossier='" + noDossier + '\'' +
                ", ine='" + ine + '\'' +
                ", civilite='" + civilite + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", ddn=" + ddn +
                ", nationalite='" + nationalite + '\'' +
                ", langue='" + langue + '\'' +
                ", telephone='" + telephone + '\'' +
                ", telephonePortable='" + telephonePortable + '\'' +
                ", mail='" + mail + '\'' +
                ", adresse='" + adresse + '\'' +
                ", adresse1='" + adresse1 + '\'' +
                ", adresse2='" + adresse2 + '\'' +
                ", adresse3='" + adresse3 + '\'' +
                ", codePostal='" + codePostal + '\'' +
                ", commune='" + commune + '\'' +
                ", communeEtr='" + communeEtr + '\'' +
                ", pays='" + pays + '\'' +
                ", dernierEtab='" + dernierEtab + '\'' +
                ", dernierDip='" + dernierDip + '\'' +
                ", dateVoeux=" + dateVoeux +
                ", dateTrans=" + dateTrans +
                ", statutDossier='" + statutDossier + '\'' +
                ", dateModifStatutDossier=" + dateModifStatutDossier +
                ", dateReceptionDossier=" + dateReceptionDossier +
                ", dateCompletDossier=" + dateCompletDossier +
                ", dateIncompletDossier=" + dateIncompletDossier +
                '}';
    }
}
