package fr.ua.iutlens.rdv;


import fr.ua.iutlens.rdv.model.Candidat;
import fr.ua.iutlens.rdv.model.Formation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by hemery on 02/04/2017.
 */
public class LesDonnees {
    private static final String PERSISTENCE_UNIT_NAME = "Entretiens";
    private static EntityManagerFactory factory;
    private static EntityManager em;

    private static Date getCurrentDate() {
        return new Date();
    }

    public static Formation getFormation(String code) {
        Logger logger = LogManager.getLogger(LesDonnees.class);
        TypedQuery<Formation> typedQuery = em.createNamedQuery("searchFormationByCode", Formation.class);
        Formation formation = null;
        try {
            formation = typedQuery.setParameter("le_code", code).getSingleResult();
        } catch (NoResultException e) {
            logger.debug("[FIND] Formation not found  : {}", code);
        }
        logger.debug("[FIND] Formation is  : {}", formation);
        return formation;
    }

    public static List<Candidat> getAllCandidats() {
//        Query query = em.createNamedQuery( "searchCandidatByNoDossier" );
        TypedQuery<Candidat> typedQuery = em.createQuery("select c from Candidat c", Candidat.class);

        List<Candidat> list = typedQuery.getResultList();
        System.out.println(String.format("Nombre de candidats %d ", list.size()));
        Logger logger = LogManager.getLogger(LesDonnees.class);
        logger.debug("[SELECT] Nombre de candidats  : {}", list.size());
        return list;
    }

    public static Candidat getCandidat(String noDossier) {
//        Query query = em.createNamedQuery( "searchCandidatByNoDossier" );
        TypedQuery<Candidat> typedQuery = em.createNamedQuery("searchCandidatByNoDossier", Candidat.class);
        Candidat candidat = null;
        try {
            candidat = typedQuery.setParameter("nodossier", noDossier).getSingleResult();
        } catch (NoResultException e) {
        }
        return candidat;
    }

    public static Formation createFormation(Formation formation) {
        em.getTransaction().begin();
        em.persist(formation);
        em.getTransaction().commit();
        Formation f = getFormation(formation.getCodeFormation());
        System.out.println("created : " + f);
        return f;
    }

    public static Candidat createCandidat(Candidat candidat) {
        em.getTransaction().begin();
        em.persist(candidat);
        em.getTransaction().commit();
        return getCandidat(candidat.getNoDossier());
    }

    public static void openLesDonnees() {
        Logger logger = LogManager.getLogger(LesDonnees.class);
        logger.debug("[OPEN] Current Date : {}", getCurrentDate());
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();
    }

    public static void closeLesDonnees() {
        Logger logger = LogManager.getLogger(LesDonnees.class);
        logger.debug("[CLOSE] Current Date : {}", getCurrentDate());

        em.close();
        factory.close();
    }

}
