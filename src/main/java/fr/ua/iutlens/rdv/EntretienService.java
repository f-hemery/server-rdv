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
import spark.Request;
import spark.template.freemarker.FreeMarkerEngine;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static spark.Spark.*;

//import static spark.Spark.port;

/**
 * Created by hemery on 03/04/2017.
 */
public class EntretienService {
    private static Logger logger = LogManager.getLogger(EntretienService.class);

    private static File uploadDir = new File("upload");

    public static String dataToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, data);
            return sw.toString();
        } catch (IOException e) {
            logger.fatal("[SERVEUR] Current Date : {}, error {}", getCurrentDate(), e.getMessage());
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
        uploadDir.mkdir();
        staticFileLocation("/public");
        logger.debug("[SERVEUR] Current Date : {}", getCurrentDate());
        LesDonnees.openLesDonnees();
        port(getHerokuAssignedPort());
        enableCORS("*", "*", "*");
//        port(Integer.valueOf(System.getenv("PORT")));

        get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Hello World!");
            return new ModelAndView(attributes, "index.ftl");
        }, new FreeMarkerEngine());

        get("/candidats", (request, response) -> {
            logger.debug("[SERVEUR] Requête \"/candidats\" Current Date : {}", getCurrentDate());
            List<Candidat> candidats = LesDonnees.getAllCandidats();
            response.status(200);
            return dataToJson(candidats);
        });

        get("/candidat/:id", (request, response) -> {
            logger.debug("[SERVEUR] Requête \"/candidats\" Current Date : {}", getCurrentDate());
            Candidat candidat = LesDonnees.getCandidatById(Long.parseLong(request.params("id")));
            response.status(200);
            return dataToJson(candidat);
        });

        get("/candidatsPage/:idFormation/:offset/:limit", (request, response) -> {
            logger.debug("[SERVEUR] Requête \"/candidatsPage\" Current Date : {}", getCurrentDate());
            int offset = 0;
            int limit = 0;
            long idFormation = 0;
            try {
                offset = Integer.parseInt(request.params(":offset"));
                limit = Integer.parseInt(request.params(":limit"));
                idFormation = Long.parseLong(request.params(":idFormation"));
            } catch (NumberFormatException e) {
            }
            List<Candidat> candidats = LesDonnees.getCandidatsPageToFormation(idFormation, offset, limit);
            response.status(200);
            return dataToJson(candidats);
        });

        get("/candidats/:idFormation", (request, response) -> {
            logger.debug("[SERVEUR] Requête \"/candidats/{}\" Current Date : {}", request.params(":idFormation"), getCurrentDate());
            List<Candidat> candidats = LesDonnees.getCandidatsToFormation(Long.parseLong(request.params(":idFormation")));
            response.status(200);
            return dataToJson(candidats);
        });

        get("/nbCandidatsFormation/:id", (request, response) -> {
            logger.debug("[SERVEUR] Requête \"/nbCandidatsFormation\" Current Date : {}", getCurrentDate());
            long nbCandidats = LesDonnees.getNbCandidatsToFormation(Long.parseLong(request.params(":id")));
            response.status(200);
            return dataToJson(nbCandidats);
        });

        get("/candidatsWithoutRdv/:idFormation", (request, response) -> {
            logger.debug("[SERVEUR] Requête \"/candidatsWithoutRdv/{}\" Current Date : {}", request.params(":idFormation"), getCurrentDate());
            List<Candidat> candidats = LesDonnees.getCandidatsToFormationWithoutRdv(Long.parseLong(request.params(":idFormation")));
            response.status(200);
            return dataToJson(candidats);
        });

        get("/candidatsCreneau/:id", (request, response) -> {
            logger.debug("[SERVEUR] Requête \"/candidatsCreneau\" Current Date : {}", getCurrentDate());
            List<Candidat> candidats = LesDonnees.getCandidatsOfCreneauId(Long.parseLong(request.params(":id")));
            response.status(200);
//            logger.debug("[SERVEUR] Fin requête \"/candidatsCreneau\" Current Date : {}", getCurrentDate());
            return dataToJson(candidats);
        });

        get("/nbCandidatsCreneau/:id", (request, response) -> {
            logger.debug("[SERVEUR] Requête \"/nbCandidatsCreneau\" Current Date : {}", getCurrentDate());
            long nbCandidats = LesDonnees.getNbCandidatsOfCreneauId(Long.parseLong(request.params(":id")));
            response.status(200);
            return dataToJson(nbCandidats);
        });

        get("/formations", (request, response) -> {
            logger.debug("[SERVEUR] Requête \"/formations\" Current Date : {}", getCurrentDate());
            List<Formation> formations = LesDonnees.getAllFormations();
            response.status(200);
            return dataToJson(formations);
        });

        get("/creneaux", (request, response) -> {
            logger.debug("[SERVEUR] Requête \"/creneaux\" Current Date : {}", getCurrentDate());
            List<Creneau> creneaux = LesDonnees.getAllCreneaux();
            response.status(200);
//            logger.debug("[SERVEUR] Fin requête \"/creneaux\" Current Date : {}", getCurrentDate());
            return dataToJson(creneaux);
        });

        get("/creneaux/:from/:to", (request, response) -> {
            logger.debug("[SERVEUR] Requête \"/creneaux/from/to\" Current Date : {}", getCurrentDate());
            List<Creneau> creneaux = LesDonnees.getCreneauxFromTo(request.params(":from"), request.params(":to"));
            response.status(200);
            return dataToJson(creneaux);
        });

        post("/candidatToCreneau/:idCreneau/:idCandidat", (request, response) -> {
            long id = 0;
            id = LesDonnees.createCandidatToCreneau(Long.parseLong(request.params(":idCreneau")), Long.parseLong(request.params(":idCandidat")));
            logger.debug("[CREATE] add candidat to creneau {}", id);
            response.status(200);
            response.type("application/json");
            return id;
        });

        delete("/candidatToCreneau/:idCreneau/:idCandidat", (request, response) -> {
            logger.debug("[SERVEUR] Requête \"/candidatToCreneau\" Current Date : {}", getCurrentDate());
            long id = 0;
            logger.debug("candidatToCreneau ici0");
            long idCandidat = Long.parseLong(request.params(":idCandidat"));
            long idCreneau = Long.parseLong(request.params(":idCreneau"));
            logger.debug("candidatToCreneau ici");
            id = LesDonnees.deleteCandidatToCreneau(idCreneau, idCandidat);
            logger.debug("[DELETE] delete candidat {} in creneau {} result {}", idCandidat, idCreneau, id);
            response.status(200);
//            response.header("Access-Control-Allow-Origin", "*");
//            response.type("application/json");
//            logger.debug("[SERVEUR] Fin Requête \"/candidatToCreneau\" Current Date : {}", getCurrentDate());
            return "{\"result\":\"Ok\"}";
        });

        post("/creneau", (request, response) -> {
            logger.debug("[SERVEUR] Requête POST \"/creneau\" Current Date : {}", getCurrentDate());
            try {
                ObjectMapper mapper = new ObjectMapper();
                NewCreneauPayload creation = mapper.readValue(request.body(), NewCreneauPayload.class);
                if (!creation.isValid()) {
                    logger.debug("[SERVEUR] Requête POST \"/creneau\" invalide : {}", getCurrentDate());
                    response.status(HTTP_BAD_REQUEST);
                    return "";
                }
                List<Creneau> creneaux = LesDonnees.createCreneau(creation.getCodeFormation(),
                        creation.getLieu(), creation.getDateCreneau(), creation.getNbCreneaux(), creation.getDureeCreneau(),
                        creation.getNbPlaces(), creation.getIntervalle(), creation.isVisible());
                response.status(200);
                List<Long> ids = new ArrayList<>();
                for (Creneau creneau : creneaux) {
                    ids.add(creneau.getId());
                    logger.debug("[SERVEUR] Requête POST \"/creneau\" Current creneau : {}", creneau);
                }
                return dataToJson(ids);
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

        get("/creneau/:id", (request, response) -> {
            logger.debug("[SERVEUR] Requête \"/candidats\" Current Date : {}", getCurrentDate());
            Creneau creneau = LesDonnees.getCreneauById(Long.parseLong(request.params("id")));
            response.status(200);
            return dataToJson(creneau);
        });

        post("/populateCandidats", (request, response) -> {
            Path tempFile = Files.createTempFile(uploadDir.toPath(), "", "");
            String location = "/tmp";  // the directory location where files will be stored
            long maxFileSize = 100000000;  // the maximum size allowed for uploaded files
            long maxRequestSize = 100000000;  // the maximum size allowed for multipart/form-data requests
            int fileSizeThreshold = 1024;  // the size threshold after which files will be written to disk

            StringBuffer sb = new StringBuffer();
            sb.append(String.format("Method: %s\n", request.requestMethod()));
            sb.append(String.format("Path: %s\n", request.pathInfo()));
            for (String s : request.params().keySet()) {
                sb.append(String.format("%s = %s\n", s, request.params(s)));
            }
            for (String s : request.headers()) {
                sb.append(String.format("%s %s\n", s, request.headers(s)));
            }
            response.status(200);
//            response.type("application/json");

            logger.info("post populateCandidats before:\n {}", sb.toString());
            MultipartConfigElement multipartConfigElement = null;
            String fName = "";
            Collection<Part> parts;
            try {
                multipartConfigElement = new MultipartConfigElement(location, maxFileSize, maxRequestSize, fileSizeThreshold);
                request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
                //-/
                sb = new StringBuffer();
                parts = request.raw().getParts();
                for (Part part : parts) {
                    sb.append("Name:");
                    sb.append(part.getName());
                    sb.append("\n");
                    sb.append("Size: ");
                    sb.append(part.getSize());
                    sb.append("\n");
                    sb.append("Filename:");
                    sb.append(part.getSubmittedFileName());
                    sb.append("\n");
                }

                fName = request.raw().getPart("candidats_file").getSubmittedFileName();
                sb.append("Title: " + request.raw().getParameter("title") + "\n");
                sb.append("File: " + fName + "\n");
            } catch (Exception e) {
                logger.error("post populateCandidats error : {} {} ", e.getMessage(), sb.toString());
            }
            logger.info("post populateCandidats: after \n{}", sb.toString());
            Part uploadedFile = request.raw().getPart("candidats_file");
            Path out = Paths.get(tempFile.toString());
            try (final InputStream in = uploadedFile.getInputStream()) {
                Files.copy(in, out, StandardCopyOption.REPLACE_EXISTING);
                PopulateEtudiants populateEtudiants = new PopulateEtudiants();
                populateEtudiants.listeCandidats(out.toString());
                uploadedFile.delete();
            } catch (Exception e) {
                logger.error("post populateCandidats error : {}", e.getMessage());
                return "{\"result\": \"Error\"}";
            }
            // cleanup
            multipartConfigElement = null;
            parts = null;
            uploadedFile = null;

            return "{\"result\": \"OK\"}";
        });
    }

    private static void logInfo(Request req, Path tempFile) throws IOException, ServletException {
        System.out.println("Uploaded file '" + getFileName(req.raw().getPart("uploaded_file")) + "' saved as '" + tempFile.toAbsolutePath() + "'");
    }

    private static String getFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    private static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }

}
