package uk.ac.ebi.pride.proteinidentificationindex.mongo.search.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
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

@Slf4j
public class MongoProteinIdentificationMzTabBuilder {

  public static String OPTIONAL_SEQUENCE_COLUMN = "protein_sequence";

  public static List<MongoProteinIdentification> readProteinIdentificationsFromMzTabFile(
      String projectAccession, String assayAccession, MZTabFile tabFile) {
    List<MongoProteinIdentification> result = new LinkedList<>();
    if (tabFile != null) {
      Collection<Protein> mzTabProteins = tabFile.getProteins();
      for (Protein mzTabProtein : mzTabProteins) {
        MongoProteinIdentification mongoProteinIdentification = new MongoProteinIdentification();
        mongoProteinIdentification.setId(
            MongoProteinIdentificationIdBuilder.createId(
                mzTabProtein.getAccession(), projectAccession, assayAccession));
        mongoProteinIdentification.setOtherMappings(new TreeSet<>());
        mongoProteinIdentification.setSubmittedAccession(mzTabProtein.getAccession());
        mongoProteinIdentification.setAssayAccession(assayAccession);
        mongoProteinIdentification.setProjectAccession(projectAccession);
        mongoProteinIdentification.setAmbiguityGroupSubmittedAccessions(new LinkedList<>());
        String sequence = mzTabProtein.getOptionColumnValue(OPTIONAL_SEQUENCE_COLUMN);
        if (!StringUtils.isEmpty(sequence)) {
          mongoProteinIdentification.setSubmittedSequence(sequence);
          log.debug("Retrieved submitted sequence");
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
          log.error(
              "Cannot correct protein accession "
                  + mzTabProtein.getAccession()
                  + " with DB "
                  + mzTabProtein.getDatabase());
          log.error("Original accession will be used");
          log.error("Cause:" + e.getCause());
          mongoProteinIdentification.setAccession(mzTabProtein.getAccession());
        }
        result.add(mongoProteinIdentification);
      }
      log.debug(
          "Found "
              + result.size()
              + " protein identifications for Assay "
              + assayAccession
              + " in file.");
    } else {
      log.error("Passed null mzTab file to protein identifications reader");
    }
    return result;
  }

  private static String getCorrectedAccession(String accession, String database) {
    String result;
    try {
      AccessionResolver accessionResolver =
          new AccessionResolver(accession, null, database); // we don't have versions
      String fixedAccession = accessionResolver.getAccession();
      if (StringUtils.isEmpty(fixedAccession)) {
        log.debug(
            "No proper fix found for accession "
                + accession
                + ". Obtained: <"
                + fixedAccession
                + ">. Original accession will be used.");
        return accession;
      } else {
        log.debug("Original accession " + accession + " fixed to " + fixedAccession);
        result = fixedAccession;
      }
    } catch (Exception e) {
      log.error(
          "There were problems getting corrected accession for "
              + accession
              + ". Original accession will be used.");
      result = accession;
    }
    return result;
  }

  // todo javadoc
  // todo coverage
}
