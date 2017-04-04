package fr.ua.iutlens.rdv;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import fr.ua.iutlens.rdv.model.Candidat;
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

//import static spark.Spark.port;
import static spark.Spark.port;
import static spark.Spark.staticFileLocation;
import static spark.Spark.get;

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
            response.type("application/json");
            return dataToJson(LesDonnees.getAllCandidats());
        });

//        LesDonnees.closeLesDonnees();

    }

}
