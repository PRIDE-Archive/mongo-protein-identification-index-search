package uk.ac.ebi.pride.proteinidentificationindex.mongo.search.indexer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.pride.jmztab.model.MZTabFile;
import uk.ac.ebi.pride.proteincatalogindex.search.model.ProteinIdentified;
import uk.ac.ebi.pride.proteincatalogindex.search.service.ProteinCatalogSearchService;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.model.MongoProteinIdentification;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.util.MongoProteinIdentificationMzTabBuilder;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service.MongoProteinIdentificationIndexService;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service.MongoProteinIdentificationSearchService;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class MongoProjectProteinIdentificationsIndexer {

  private static Logger logger = LoggerFactory.getLogger(MongoProjectProteinIdentificationsIndexer.class.getName());

  @Resource
  private MongoProteinIdentificationSearchService mongoProteinIdentificationSearchService;
  @Resource
  private MongoProteinIdentificationIndexService mongoProteinIdentificationIndexService;
  @Resource
  private ProteinCatalogSearchService proteinCatalogSearchService;


  public MongoProjectProteinIdentificationsIndexer(MongoProteinIdentificationSearchService mongoProteinIdentificationSearchService,
                                                   MongoProteinIdentificationIndexService mongoProteinIdentificationIndexService,
                                                   ProteinCatalogSearchService proteinCatalogSearchService) {
    this.mongoProteinIdentificationSearchService = mongoProteinIdentificationSearchService;
    this.mongoProteinIdentificationIndexService = mongoProteinIdentificationIndexService;
    this.proteinCatalogSearchService = proteinCatalogSearchService;
  }

  public void indexAllProteinIdentificationsForProjectAndAssay(String projectAccession, String assayAccession, MZTabFile mzTabFile){
    try {
      if (mzTabFile != null) {
        List<MongoProteinIdentification> proteinsFromFile = MongoProteinIdentificationMzTabBuilder.readProteinIdentificationsFromMzTabFile(projectAccession, assayAccession, mzTabFile);
        logger.debug("Found " + proteinsFromFile.size() + " Protein Identifications "
            + " for PROJECT:" + projectAccession
            + " and ASSAY:" + assayAccession);
        if (proteinsFromFile.size()>0) {
          addCatalogInfoToProteinIdentifications(proteinsFromFile);  // add synonyms, details, etc
          mongoProteinIdentificationIndexService.save(proteinsFromFile);
          logger.debug("COMMITTED " + proteinsFromFile.size() +
              " Protein Identifications from PROJECT:" + projectAccession +
              " ASSAY:" + assayAccession);
        }
      } else {
        logger.error("An empty mzTab file has been passed to the indexing method - no indexing took place");
      }
    } catch (Exception e) {
      logger.error("Cannot index Protein Identifications from PROJECT:" + projectAccession + " and ASSAY:" + assayAccession );
      logger.error("Reason: ");
      e.printStackTrace();
    }
  }

  /**
   * Deletes all protein identifications for a given project accession
   *
   * @param projectAccession The accession that identifies the PRIDE Archive project
   */
  public void deleteAllProteinIdentificationsForProject(String projectAccession) {
    List<MongoProteinIdentification> mongoProteinIdentifications = this.mongoProteinIdentificationSearchService.findByProjectAccession(projectAccession);
    this.mongoProteinIdentificationIndexService.delete(mongoProteinIdentifications);
  }

  /**
   * Use the protein Catalog to retrieve synonym information, protein details, etc.
   *
   * @param mongoProteinIdentifications The list to be enriched with information from the Catalog
   */
  private void addCatalogInfoToProteinIdentifications(List<MongoProteinIdentification> mongoProteinIdentifications) {
    mongoProteinIdentifications.forEach(mongoProteinIdentification -> {
      findOtherMappings(mongoProteinIdentification);
    });
  }

  private void findOtherMappings(MongoProteinIdentification mongoProteinIdentification) {
    mongoProteinIdentification.setOtherMappings(new TreeSet<>());
    mongoProteinIdentification.setDescription(new LinkedList<>());
    List<ProteinIdentified> proteinsFromCatalog = proteinCatalogSearchService.findByAccession(mongoProteinIdentification.getAccession());
    if (proteinsFromCatalog != null && proteinsFromCatalog.size() > 0) {
      logger.debug("Protein " + mongoProteinIdentification.getAccession() + " already in the Catalog - getting details...");
      proteinsFromCatalog.forEach(proteins->
          updateProteinIdentification(mongoProteinIdentification, proteins));
    } else { // if none, there were errors
      logger.error("Protein " + mongoProteinIdentification.getId() + " not in the catalog - It should be saved by now...");
    }
  }

  private void updateProteinIdentification(MongoProteinIdentification mongoProteinIdentification, ProteinIdentified proteinFromCatalog) {
    if (proteinFromCatalog.getUniprotMapping()!=null) {
      mongoProteinIdentification.setUniprotMapping(proteinFromCatalog.getUniprotMapping());
    }
    if (proteinFromCatalog.getEnsemblMapping()!=null) {
      mongoProteinIdentification.setEnsemblMapping(proteinFromCatalog.getEnsemblMapping());
    }
    if (proteinFromCatalog.getOtherMappings() != null) {
      mongoProteinIdentification.getOtherMappings().addAll(proteinFromCatalog.getOtherMappings());
    }
    if (proteinFromCatalog.getDescription() != null) {
      mongoProteinIdentification.getDescription().addAll(proteinFromCatalog.getDescription());
    }
    mongoProteinIdentification.setInferredSequence(proteinFromCatalog.getInferredSequence());
  }
}
