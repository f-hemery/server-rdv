package fr.ua.iutlens.rdv;

import fr.ua.iutlens.rdv.model.Candidat;
import fr.ua.iutlens.rdv.model.CandidatFormation;
import fr.ua.iutlens.rdv.model.Formation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by hemery on 03/04/2017.
 */
public class PopulateEtudiants {

    private static Calendar cal = Calendar.getInstance();
    private static Logger logger = LogManager.getLogger(PopulateEtudiants.class);


    /**
     * @param filename
     * @return
     * @throws IOException
     */


    private static Workbook readFile(String filename) throws IOException, InvalidFormatException {

        InputStream inp = new FileInputStream(filename);
        //InputStream inp = new FileInputStream("workbook.xlsx");

        return WorkbookFactory.create(inp);

    }

    private String uneValeur(Cell cell) {
        return cell == null ? "" : cell.getStringCellValue();
    }


    private Date getDate(String dateStr) {
        StringTokenizer st = new StringTokenizer(dateStr, "/");
        if (st.countTokens() != 3) {
            return null;
        }
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(st.nextToken()));
        cal.set(Calendar.MONTH, Integer.parseInt(st.nextToken()) - 1);
        cal.set(Calendar.YEAR, Integer.parseInt(st.nextToken().trim()));
        return new Date(cal.getTimeInMillis());
    }

    public void listeCandidats(String filename) {
        try {
            Workbook wb = readFile(filename);
            Sheet sheet = wb.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();
            System.out.println("Feuille \"" + wb.getSheetName(0) + "\" a " + rows + " ligne(s).");
            for (int r = 1; r < rows; r++) {
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }
                System.out.println("Ligne : " + r);
                int numCell = 0;
                Candidat candidat = new Candidat();
                candidat.setNoDossier(uneValeur(row.getCell(numCell++)));
                candidat.setIne(uneValeur(row.getCell(numCell++)));
                candidat.setCivilite((row.getCell(numCell++).getStringCellValue().trim().equalsIgnoreCase("M.") ? "Monsieur" : "Madame"));
                candidat.setNom(uneValeur(row.getCell(numCell++)));
                candidat.setPrenom(uneValeur(row.getCell(numCell++)));
                candidat.setDdn(getDate(uneValeur(row.getCell(numCell++))));
                candidat.setNationalite(uneValeur(row.getCell(numCell++)));
                candidat.setLangue(uneValeur(row.getCell(numCell++)));
                candidat.setTelephone(uneValeur(row.getCell(numCell++)));
                candidat.setTelephonePortable(uneValeur(row.getCell(numCell++)));
                candidat.setMail(uneValeur(row.getCell(numCell++)));
                candidat.setAdresse(uneValeur(row.getCell(numCell++)));
                candidat.setAdresse1(uneValeur(row.getCell(numCell++)));
                candidat.setAdresse2(uneValeur(row.getCell(numCell++)));
                candidat.setAdresse3(uneValeur(row.getCell(numCell++)));
                candidat.setCodePostal(uneValeur(row.getCell(numCell++)));
                candidat.setCommune(uneValeur(row.getCell(numCell++)));
                candidat.setCommuneEtr(uneValeur(row.getCell(numCell++)));
                candidat.setPays(uneValeur(row.getCell(numCell++)));
                candidat.setDernierEtab(uneValeur(row.getCell(numCell++)));
                candidat.setDernierDip(uneValeur(row.getCell(numCell++)));
                String code = uneValeur(row.getCell(numCell++));
                String libelle = uneValeur(row.getCell(numCell++));
                candidat.setDateVoeux(getDate(uneValeur(row.getCell(numCell++))));
                candidat.setDateTrans(getDate(uneValeur(row.getCell(numCell++))));
                candidat.setStatutDossier(uneValeur(row.getCell(numCell++)));
                candidat.setDateModifStatutDossier(getDate(uneValeur(row.getCell(numCell++))));
                candidat.setDateReceptionDossier(getDate(uneValeur(row.getCell(numCell++))));
                candidat.setDateCompletDossier(getDate(uneValeur(row.getCell(numCell++))));
                candidat.setDateIncompletDossier(getDate(uneValeur(row.getCell(numCell++))));
                Formation formation = LesDonnees.getFormation(code);
                if (formation == null) {
                    formation = new Formation();
                    formation.setCodeFormation(code);
                    formation.setLibelleFormation(libelle);
                    formation = LesDonnees.createFormation(formation);
                    logger.debug("listeCandidats create formation : {}", String.format("%3d %s %50s",formation.getId(), formation.getCodeFormation(), formation.getLibelleFormation()));
                }
                Candidat c = LesDonnees.getCandidatByName(candidat.getNom(),candidat.getPrenom(),candidat.getDdn());
                CandidatFormation cf;
                if (c == null) {
                    LesDonnees.createCandidat(candidat);
                    logger.debug("listeCandidats create candidat {}",candidat.getNom());
                    cf = new CandidatFormation(candidat, formation);
                }
                else {
                    cf = new CandidatFormation(c, formation);
                }
                LesDonnees.createCandidatFormation(cf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PopulateEtudiants populateEtudiants = new PopulateEtudiants();
        LesDonnees.openLesDonnees();
        populateEtudiants.listeCandidats("liste.xlsx");
        LesDonnees.closeLesDonnees();
    }

}
