package uk.ac.ebi.pride.proteinidentificationindex.mongo.search.util;

/** Builds the protein identification field. */
public class MongoProteinIdentificationIdBuilder {

  /** The separator between important parts of the protein ID. */
  public static final String SEPARATOR = "_%_%_";

  /**
   * Creates an ID for a protein.
   *
   * @param proteinAccession the protein accession number
   * @param projectAccession the project assession number
   * @param assayAccession the assay accession number
   * @return the ID for the protein
   */
  public static String createId(
      String proteinAccession, String projectAccession, String assayAccession) {
    return proteinAccession + SEPARATOR + projectAccession + SEPARATOR + assayAccession;
  }

  /**
   * Gets the protein accession part from the ID.
   *
   * @param id the protein ID
   * @return the protein accession part from the ID.
   */
  public static String getProteinAccession(String id) {
    String[] tokens = id.split(SEPARATOR);
    return tokens[0];
  }

  /**
   * Gets the project accession part from the ID.
   *
   * @param id the protein ID
   * @return the project accession part from the ID.
   */
  public static String getProjectAccession(String id) {
    String[] tokens = id.split(SEPARATOR);
    return 0 < tokens.length ? tokens[1] : "";
  }

  /**
   * Gets the assay accession part from the ID.
   *
   * @param id the protein ID
   * @return the assay accession part from the ID.
   */
  public static String getAssayAccession(String id) {
    String[] tokens = id.split(SEPARATOR);
    return 1 < tokens.length ? tokens[2] : "";
  }
}
