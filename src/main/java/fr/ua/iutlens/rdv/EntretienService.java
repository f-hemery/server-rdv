package fr.ua.iutlens.rdv;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import fr.ua.iutlens.rdv.model.Candidat;
import fr.ua.iutlens.rdv.model.Creneau;
import fr.ua.iutlens.rdv.model.Formation;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static spark.Spark.*;

//import static spark.Spark.port;

/**
 * Created by hemery on 03/04/2017.
 */
public class EntretienService {


    public static String dataToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, data);
            return sw.toString();
        } catch (IOException e) {
            throw new RuntimeException("IOException from a StringWriter?");
        }
    }

    private static Date getCurrentDate() {
        return new Date();
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

    @Data
    static class NewCreneauPayload {
        private long id;
        private String lieu;
        private String codeFormation;
        private Date dateCreneau;
        private int nbCreneaux;
        private int dureeCreneau;
        private int nbPlaces;
        private int intervalle;
        private boolean visible;

        public boolean isValid() {
            return !codeFormation.isEmpty() && !lieu.isEmpty() && dateCreneau != null;
        }
    }

    public static void main(String[] args) {
        Logger logger = LogManager.getLogger(EntretienService.class);
        logger.debug("[SERVEUR] Current Date : {}", getCurrentDate());
        LesDonnees.openLesDonnees();
        port(getHerokuAssignedPort());

//        port(Integer.valueOf(System.getenv("PORT")));
        staticFileLocation("/public");

        get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Hello World!");
            return new ModelAndView(attributes, "index.ftl");
        }, new FreeMarkerEngine());


        get("/candidats", (request, response) -> {
            logger.debug("[SERVEUR] Requête \"/candidats\" Current Date : {}", getCurrentDate());
//            LesDonnees.openLesDonnees();
            List<Candidat> candidats = LesDonnees.getAllCandidats();
//            LesDonnees.closeLesDonnees();
            response.status(200);
            response.header("Access-Control-Allow-Origin", "*");
            response.type("application/json");
            return dataToJson(LesDonnees.getAllCandidats());
        });

//        LesDonnees.closeLesDonnees();

        get("/formations", (request, response) -> {
            logger.debug("[SERVEUR] Requête \"/formations\" Current Date : {}", getCurrentDate());
//            LesDonnees.openLesDonnees();
            List<Formation> formations = LesDonnees.getAllFormations();
//            LesDonnees.closeLesDonnees();
            response.status(200);
            response.header("Access-Control-Allow-Origin", "*");
            response.type("application/json");
            return dataToJson(formations);
        });

        get("/creneaux", (request, response) -> {
            logger.debug("[SERVEUR] Requête \"/creneaux\" Current Date : {}", getCurrentDate());
//            LesDonnees.openLesDonnees();
            List<Creneau> creneaux = LesDonnees.getAllCreneaux();
//            LesDonnees.closeLesDonnees();
            response.status(200);
            response.header("Access-Control-Allow-Origin", "*");
            response.type("application/json");
            return dataToJson(creneaux);
        });

        post("/creneau", (request, response) -> {
            logger.debug("[SERVEUR] Requête POST \"/creneau\" Current Date : {}", getCurrentDate());
            try {
                ObjectMapper mapper = new ObjectMapper();
                logger.debug("[SERVEUR] Requête POST \"/creneau\" ici : {}", request.body());
                NewCreneauPayload creation = mapper.readValue(request.body(), NewCreneauPayload.class);
                logger.debug("[SERVEUR] Requête POST \"/creneau\" ici2 : {}", getCurrentDate());
                if (!creation.isValid()) {
                    logger.debug("[SERVEUR] Requête POST \"/creneau\" invalide : {}", getCurrentDate());
                    response.status(HTTP_BAD_REQUEST);
                    return "";
                }
                Creneau creneau = LesDonnees.createCreneau(creation.getCodeFormation(),
                        creation.getLieu(), creation.getDateCreneau(), creation.getNbCreneaux(),creation.getDureeCreneau(),
                        creation.getNbPlaces(), creation.getIntervalle(), creation.isVisible());
                logger.debug("[SERVEUR] Requête POST \"/creneau\" Current creneau : {}", creneau);
                response.status(200);
                response.header("Access-Control-Allow-Origin", "*");
                response.type("application/json");
                return creneau.getId();
            } catch (JsonParseException jpe) {
                logger.debug("[SERVEUR] Requête POST \"/creneau\" jsonerror : {}", jpe.getMessage());
                logger.error(jpe.getMessage());
                response.status(HTTP_BAD_REQUEST);
                return "";
            } catch (IOException ioe) {
                logger.debug("[SERVEUR] Requête POST \"/creneau\" ioerror : {}", ioe.getMessage());
                response.status(HTTP_BAD_REQUEST);
                return "";
            }
        });

    }

}
