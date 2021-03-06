package uk.ac.ebi.pride.proteinidentificationindex.mongo.search.util;

public class MongoProteinIdentificationIdBuilder {

    public static final String SEPARATOR = "_%_%_";

    public static String getId(String proteinAccession, String projectAccession, String assayAccession) {
        return proteinAccession + SEPARATOR + projectAccession + SEPARATOR + assayAccession;
    }

    public static String getProteinAccession(String id) {
        String [] tokens = id.split(SEPARATOR);
        return tokens[0];
    }

    public static String getProjectAccession(String id) {
        String [] tokens = id.split(SEPARATOR);
        return tokens[1];
    }

    public static String getAssayAccession(String id) {
        String [] tokens = id.split(SEPARATOR);
        return tokens[2];
    }


}
