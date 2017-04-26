package fr.ua.iutlens.rdv;


import fr.ua.iutlens.rdv.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hemery on 02/04/2017.
 */
public class LesDonnees {
    private static final String PERSISTENCE_UNIT_NAME = "Entretiens";
    private static EntityManagerFactory factory;
    private static EntityManager em;
    private static Logger logger = LogManager.getLogger(LesDonnees.class);


    private static Date getCurrentDate() {
        return new Date();
    }

    public static Formation getFormation(String code) {
        TypedQuery<Formation> typedQuery = em.createNamedQuery("searchFormationByCode", Formation.class);
        Formation formation = null;
        try {
            formation = typedQuery.setParameter("le_code", code).getSingleResult();
        } catch (NoResultException e) {
            logger.debug("[FIND] Formation not found  : {}", code);
        }
        return formation;
    }

    public static List<Formation> getAllFormations() {
        TypedQuery<Formation> typedQuery = em.createQuery("select f from Formation f", Formation.class);
        List<Formation> list = typedQuery.getResultList();
        System.out.println(String.format("Nombre de formations %d ", list.size()));
        logger.debug("[SELECT] Nombre de formations  : {}", list.size());
        return list;
    }

    public static List<Candidat> getAllCandidats() {
        TypedQuery<Candidat> typedQuery = em.createQuery("select c from Candidat c", Candidat.class);
        List<Candidat> list = typedQuery.getResultList();
        System.out.println(String.format("Nombre de candidats %d ", list.size()));
        logger.debug("[SELECT] Nombre de candidats  : {}", list.size());
        return list;
    }

    private static List<Creneau> executeQueryOnCreneaux(TypedQuery<Creneau> typedQuery) {
        List<Creneau> list = typedQuery.getResultList();
        System.out.println(String.format("Nombre de creneaux %d ", list.size()));
        logger.debug("[SELECT] Nombre de creneaux  : {}", list.size());
        for (Creneau creneau : list) {
            creneau.setNbPlacesDispo(creneau.getNbPlaces() - (int) getNbCandidatsOfCreneauId(creneau.getId()));
        }
        return list;
    }

    public static List<Creneau> getAllCreneaux() {
        TypedQuery<Creneau> typedQuery = em.createQuery("select c from Creneau c", Creneau.class);
        return executeQueryOnCreneaux(typedQuery);
    }

    public static List<Creneau> getCreneauxFromTo(String from, String to) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        TypedQuery<Creneau> typedQuery = em.createNamedQuery("searchCreneauxFromTo", Creneau.class);
        Date currentDate = null;
        try {
            currentDate = sdf.parse(from);
            typedQuery.setParameter("from", currentDate);
            currentDate = sdf.parse(to);
            typedQuery.setParameter("to", currentDate);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return executeQueryOnCreneaux(typedQuery);
    }

    public static Candidat getCandidatByName(String nom, String prenom, Date ddn) {
        TypedQuery<Candidat> typedQuery = em.createNamedQuery("searchCandidatByName", Candidat.class);
        Candidat candidat = null;
        try {
            candidat = typedQuery.setParameter("nom", nom).setParameter("prenom", prenom).setParameter("ddn", ddn).getSingleResult();
        } catch (NoResultException e) {
            logger.warn("getCandidatByName not found {} {} : {}", prenom, nom, e.getMessage());
        }
        return candidat;
    }

    public static Candidat getCandidatByIne(String ine) {
        TypedQuery<Candidat> typedQuery = em.createNamedQuery("searchCandidatByIne", Candidat.class);
        Candidat candidat = null;
        try {
            candidat = typedQuery.setParameter("ine", ine).getSingleResult();
        } catch (NoResultException e) {
            logger.warn(e.getMessage());
        }
        return candidat;
    }

    public static Candidat getCandidatById(long id) {
        TypedQuery<Candidat> typedQuery = em.createNamedQuery("searchCandidatById", Candidat.class);
        Candidat candidat = null;
        try {
            candidat = typedQuery.setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            logger.warn(e.getMessage());
        }
        return candidat;
    }

    public static Formation createFormation(Formation formation) {
        em.getTransaction().begin();
        em.persist(formation);
        em.getTransaction().commit();
        Formation f = getFormation(formation.getCodeFormation());
        return f;
    }

    public static boolean createCandidatFormation(CandidatFormation cf) {

        try {
            em.getTransaction().begin();
            em.persist(cf);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.warn("create CandidatFormation error {}", e.getMessage());
            em.getTransaction().rollback();
            return false;
        }
        return true;
    }

    public static boolean deleteCandidatFormation(CandidatFormation cf) {
        try {
            em.getTransaction().begin();
            cf.getCandidat().getCandidatFormations().remove(cf);
            cf.getFormation().getCandidatFormations().remove(cf);
            em.remove(cf);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.warn("delete CandidatFormation error {}", e.getMessage());
            return false;
        }
        return true;
    }

    public static Candidat createCandidat(Candidat candidat) {
        em.getTransaction().begin();
        em.persist(candidat);
        em.getTransaction().commit();
        return candidat;
    }

    public static Candidat updateCandidat(Candidat candidat) {
        em.getTransaction().begin();
        em.merge(candidat);
        em.getTransaction().commit();
        logger.debug("updateCandidat {}", candidat);
        return candidat;
    }

    public static List<Creneau> createCreneau(String codeFormation, String lieu, Date dateHeure, int nbCreneaux, int dureeCreneau, int nbPlaces, int intervalle, boolean visible) {
        Logger logger = LogManager.getLogger(LesDonnees.class);
        List<Creneau> creneaux = generateCreneaux(codeFormation, lieu, dateHeure, nbCreneaux, dureeCreneau, nbPlaces, intervalle, visible);
        try {
            em.getTransaction().begin();
            for (Creneau creneau : creneaux) {
                em.persist(creneau);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.error("createCreneau error: {}",e.getMessage());
        }
        return creneaux;
    }

    public static Creneau getCreneauById(long id) {
        TypedQuery<Creneau> typedQuery = em.createNamedQuery("searchCreneauById", Creneau.class);
        Creneau creneau = null;
        try {
            creneau = typedQuery.setParameter("id", id).getSingleResult();
            creneau.setNbPlacesDispo(creneau.getNbPlaces() - (int) getNbCandidatsOfCreneauId(creneau.getId()));
        } catch (NoResultException e) {
            logger.warn("getCreneauById error: {}",e.getMessage());
        }
        return creneau;
    }

    public static long createCandidatToCreneau(long idCreneau, long idCandidat) {
        ChoixCandCren choix = new ChoixCandCren();
        choix.setCandidat(getCandidatById(idCandidat));
        choix.setCreneau(getCreneauById(idCreneau));
        choix.setOrdrePassage(getMaxOrdrePassage(idCreneau));
        choix.setDateReservation(new Date());
        try {
            em.getTransaction().begin();
            em.persist(choix);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.warn("createCandidatToCreneau error: {}", e.getMessage());
        }
        return choix.getId();
    }

    public static long deleteCandidatToCreneau(long idCreneau, long idCandidat) {
        ChoixCandCren choix = new ChoixCandCren();
        TypedQuery<ChoixCandCren> typedQuery = em.createNamedQuery("getCandidatIdInCreneauId", ChoixCandCren.class);
        try {
            typedQuery.setParameter("idCreneau", idCreneau). setParameter("idCandidat", idCandidat);
            choix = typedQuery.getSingleResult();
            em.getTransaction().begin();
            em.remove(choix);
            em.flush();
            em.getTransaction().commit();
        }catch(Exception e) {
            em.getTransaction().rollback();
            logger.warn("deleteCandidatToCreneau unable to delete choix candidat {} in creneau {} error {} ",idCandidat,idCreneau, e.getMessage());
            return 0L;
        }
        return choix.getId();
    }

    private static int getMaxOrdrePassage(long idCreneau) {
        int max = 0;
        TypedQuery<Integer> typedQuery = em.createNamedQuery("getMaxOfCreneauId", Integer.class);
        typedQuery.setParameter("id", idCreneau);
        try {
            max = typedQuery.getSingleResult().intValue();
        } catch (Exception e) {
            logger.debug("[FIND] getMaxOrdrePassage request error  : {}", e.getMessage());
        }
        return max + 1;
    }

    private static List<Creneau> generateCreneaux(String codeFormation, String lieu, Date dateHeure, int nbCreneaux, int dureeCreneau, int nbPlaces, int intervalle, boolean visible) {
        List<Creneau> creneaux = new ArrayList<>();
        Formation formation = getFormation(codeFormation);
        for (int i = 0; i < nbCreneaux; i++) {
            Creneau creneau = new Creneau();
            creneau.setFormation(formation);
            creneau.setLieu(lieu);
            creneau.setDureeCreneau(dureeCreneau);
            creneau.setNbPlaces(nbPlaces);
            creneau.setDateCreneau(startOfCreneau(dateHeure, dureeCreneau * i, intervalle * i));
            creneau.setVisible(visible);
            creneaux.add(creneau);
        }
        return creneaux;
    }

    private static Date startOfCreneau(Date debut, int duree, int intervalle) {
        Instant instant = debut.toInstant();
        Date d = new Date(instant.plus(duree, ChronoUnit.MINUTES).plus(intervalle, ChronoUnit.MINUTES).toEpochMilli());
        Logger logger = LogManager.getLogger(LesDonnees.class);
        return d;
    }

    public static long getNbCandidatsOfCreneauId(long id) {
        long nbCandidats = 0;
        TypedQuery<Long> typedQuery = em.createQuery("select count(ccc) from ChoixCandCren ccc  where ccc.creneau.id = :id", Long.class);
        typedQuery.setParameter("id", id);
        try {
            nbCandidats = typedQuery.getSingleResult();
        } catch (Exception e) {
            logger.error("[FIND] CandidatOfCreneauId request error  : {}", e.getMessage());
        }
        logger.debug("[FIND] getNbCandidatsOfCreneauId {}, nbCandidats  : {}",id, nbCandidats);
        return nbCandidats;
    }

    public static List<Candidat> getCandidatsOfCreneauId(long id) {
        List<Candidat> candidats = new ArrayList<>();
        List<ChoixCandCren> liste = new ArrayList<>();
        try {
            TypedQuery<ChoixCandCren> typedQuery = em.createQuery("select ccc from ChoixCandCren ccc  where ccc.creneau.id = :id", ChoixCandCren.class);
            typedQuery.setParameter("id", id);
            liste = typedQuery.getResultList();
        } catch (Exception e) {
            logger.error("[FIND] CandidatOfCreneauId request error  : {}", e.getMessage());
        }
        for (ChoixCandCren ccc : liste) {
            candidats.add(ccc.getCandidat());
        }
        logger.debug("[FIND] CandidatOfCreneauId nb Candidats  : {}", candidats.size());

        return candidats;
    }

    public static List<Candidat> getCandidatsPageToFormation(long idFormation, int offset, int limit) {
        List<Candidat> candidats = new ArrayList<>();
        TypedQuery<Candidat> typedQuery = em.createQuery("select cf.candidat from Formation f, CandidatFormation cf" +
                " where f.id = :idFormation and f.id = cf.formation.id", Candidat.class);
        typedQuery.setParameter("idFormation", idFormation);
        typedQuery.setFirstResult(offset * limit);
        if (limit != 0)
            typedQuery.setMaxResults(limit);
        try {
            candidats = typedQuery.getResultList();
        } catch (Exception e) {
            logger.error("[FIND] getCandidatsToFormation request error  : {}", e.getMessage());
        }
        logger.debug("[FIND] getCandidatsToFormation nb Candidats  : {}", candidats.size());
        return candidats;
    }

    public static long getNbCandidatsToFormation(long idFormation) {
        long nbCandidats = 0;
        TypedQuery<Long> typedQuery = em.createQuery("select count(cf.candidat) from Formation f, CandidatFormation cf" +
                " where f.id = :idFormation and f.id = cf.formation.id", Long.class);
        typedQuery.setParameter("idFormation", idFormation);
        try {
            nbCandidats = typedQuery.getSingleResult();
        } catch (Exception e) {
            logger.error("[FIND] getCandidatsToFormation request error  : {}", e.getMessage());
        }
        logger.debug("[FIND] getNbCandidatsToFormation nb Candidats  : {}", nbCandidats);
        return nbCandidats;
    }

    public static List<Candidat> getCandidatsToFormation(long idFormation) {
        List<Candidat> candidats = new ArrayList<>();
        TypedQuery<Candidat> typedQuery = em.createQuery("select c from CandidatFormation cf, Candidat c" +
                " where cf.formation.id = :idFormation", Candidat.class);
        typedQuery.setParameter("idFormation", idFormation);
        try {
            candidats = typedQuery.getResultList();
        } catch (Exception e) {
            logger.error("[FIND] getCandidatsToFormation request error  : {}", e.getMessage());
        }
        logger.debug("[FIND] getCandidatsToFormation nb Candidats  : {}", candidats.size());
        return candidats;
    }

    public static List<Candidat> getCandidatsToFormationWithoutRdv(long idFormation) {
        List<Candidat> candidats = new ArrayList<>();
        List<ChoixCandCren> liste = new ArrayList<>();
        // select c.* from Candidat c join candidat_formation cf on c.id = cf.id_candidat
        // where cf.id_formation = idFormation and c.id not in (select cc.id_candidat
        // from ChoixCandCren cc where cc.id_formation = idFormation)
        TypedQuery<Candidat> typedQuery = em.createQuery("select c from Candidat c, Formation f, CandidatFormation" +
                "  cf  where f.id = :idFormation and cf.formation.id = :idFormation and cf.candidat.id = c.id and c.id not in " +
                "(select cc.candidat.id from ChoixCandCren cc where cc.creneau.formation.id = :idFormation)", Candidat.class);
        typedQuery.setParameter("idFormation", idFormation);
        try {
            candidats = typedQuery.getResultList();
        } catch (Exception e) {
            logger.error("[FIND] getCandidatsWithoutRdv request error  : {}", e.getMessage());
        }

        logger.debug("[FIND] getCandidatsWithoutRdv nb Candidats  : {}", candidats.size());
        return candidats;
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
