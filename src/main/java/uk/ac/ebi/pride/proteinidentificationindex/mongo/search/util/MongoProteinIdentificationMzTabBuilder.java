package uk.ac.ebi.pride.proteinidentificationindex.mongo.search.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import uk.ac.ebi.pride.indexutils.helpers.ModificationHelper;
import uk.ac.ebi.pride.jmztab.model.MZTabFile;
import uk.ac.ebi.pride.jmztab.model.Modification;
import uk.ac.ebi.pride.jmztab.model.Protein;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.model.MongoProteinIdentification;
import uk.ac.ebi.pride.tools.utils.AccessionResolver;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class MongoProteinIdentificationMzTabBuilder {

  public static String OPTIONAL_SEQUENCE_COLUMN = "protein_sequence";
  private static Logger logger =
      LoggerFactory.getLogger(MongoProteinIdentificationMzTabBuilder.class.getName());

  public static List<MongoProteinIdentification> readProteinIdentificationsFromMzTabFile(
      String projectAccession, String assayAccession, MZTabFile tabFile) {
    List<MongoProteinIdentification> result = new LinkedList<>();
    String sequence;
    if (tabFile != null) {
      Collection<Protein> mzTabProteins = tabFile.getProteins();
      for (Protein mzTabProtein : mzTabProteins) {
        MongoProteinIdentification mongoProteinIdentification = new MongoProteinIdentification();
        mongoProteinIdentification.setId(
            MongoProteinIdentificationIdBuilder.getId(
                mzTabProtein.getAccession(), projectAccession, assayAccession));
        mongoProteinIdentification.setOtherMappings(new TreeSet<>());
        mongoProteinIdentification.setSubmittedAccession(mzTabProtein.getAccession());
        mongoProteinIdentification.setAssayAccession(assayAccession);
        mongoProteinIdentification.setProjectAccession(projectAccession);
        mongoProteinIdentification.setAmbiguityGroupSubmittedAccessions(new LinkedList<>());
        if ((sequence = mzTabProtein.getOptionColumnValue(OPTIONAL_SEQUENCE_COLUMN)) != null) {
          if (!sequence.isEmpty()) {
            mongoProteinIdentification.setSubmittedSequence(sequence);
            logger.debug("Retrieved submitted sequence");
          }
        }
        if (mzTabProtein.getAmbiguityMembers() != null
            && mzTabProtein.getAmbiguityMembers().size() > 0
            && !mzTabProtein.getAmbiguityMembers().get(0).equals("null")) {
          mongoProteinIdentification
              .getAmbiguityGroupSubmittedAccessions()
              .addAll(mzTabProtein.getAmbiguityMembers());
        }
        mongoProteinIdentification.setModifications(new LinkedList<>());
        if (!CollectionUtils.isEmpty(mzTabProtein.getModifications())) {
          for (Modification mod : mzTabProtein.getModifications())
            mongoProteinIdentification.addModification(
                ModificationHelper.convertToModificationProvider(mod));
        }
        try {
          String correctedAccession =
              getCorrectedAccession(mzTabProtein.getAccession(), mzTabProtein.getDatabase());
          mongoProteinIdentification.setAccession(correctedAccession);
        } catch (Exception e) {
          logger.error(
              "Cannot correct protein accession "
                  + mzTabProtein.getAccession()
                  + " with DB "
                  + mzTabProtein.getDatabase());
          logger.error("Original accession will be used");
          logger.error("Cause:" + e.getCause());
          mongoProteinIdentification.setAccession(mzTabProtein.getAccession());
        }
        result.add(mongoProteinIdentification);
      }
      logger.debug(
          "Found "
              + result.size()
              + " protein identifications for Assay "
              + assayAccession
              + " in file.");
    } else {
      logger.error("Passed null mzTab file to protein identifications reader");
    }
    return result;
  }

  private static String getCorrectedAccession(String accession, String database) {
    try {
      AccessionResolver accessionResolver =
          new AccessionResolver(accession, null, database); // we don't have versions
      String fixedAccession = accessionResolver.getAccession();

      if (fixedAccession == null || "".equals(fixedAccession)) {
        logger.debug(
            "No proper fix found for accession "
                + accession
                + ". Obtained: <"
                + fixedAccession
                + ">. Original accession will be used.");
        return accession;
      } else {
        logger.debug("Original accession " + accession + " fixed to " + fixedAccession);
        return fixedAccession;
      }
    } catch (Exception e) {
      logger.error(
          "There were problems getting corrected accession for "
              + accession
              + ". Original accession will be used.");
      return accession;
    }
  }
}
