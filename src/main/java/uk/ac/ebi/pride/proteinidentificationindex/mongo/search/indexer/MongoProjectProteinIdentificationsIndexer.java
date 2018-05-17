package uk.ac.ebi.pride.proteinidentificationindex.mongo.search.indexer;

import lombok.extern.slf4j.Slf4j;
import uk.ac.ebi.pride.jmztab.model.MZTabFile;
import uk.ac.ebi.pride.proteincatalogindex.search.model.ProteinIdentified;
import uk.ac.ebi.pride.proteincatalogindex.search.service.ProteinCatalogSearchService;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.model.MongoProteinIdentification;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service.MongoProteinIdentificationIndexService;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service.MongoProteinIdentificationSearchService;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.util.MongoProteinIdentificationMzTabBuilder;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

/** Indexes a project's proteins in Mongo. */
@Slf4j
public class MongoProjectProteinIdentificationsIndexer {

  @Resource private MongoProteinIdentificationSearchService mongoProteinIdentificationSearchService;
  @Resource private MongoProteinIdentificationIndexService mongoProteinIdentificationIndexService;
  @Resource private ProteinCatalogSearchService proteinCatalogSearchService;

  /**
   * Constructor, sets the search, index, and catalog services.
   *
   * @param mongoProteinIdentificationSearchService the protein ID search service
   * @param mongoProteinIdentificationIndexService the protein ID index service
   * @param proteinCatalogSearchService the protein catalog search sergice
   */
  public MongoProjectProteinIdentificationsIndexer(
      MongoProteinIdentificationSearchService mongoProteinIdentificationSearchService,
      MongoProteinIdentificationIndexService mongoProteinIdentificationIndexService,
      ProteinCatalogSearchService proteinCatalogSearchService) {
    this.mongoProteinIdentificationSearchService = mongoProteinIdentificationSearchService;
    this.mongoProteinIdentificationIndexService = mongoProteinIdentificationIndexService;
    this.proteinCatalogSearchService = proteinCatalogSearchService;
  }

  /**
   * Indexes all the protein IDs for the Project and Assay
   *
   * @param projectAccession the project accession
   * @param assayAccession the assay accession
   * @param mzTabFile the mzTab file
   */
  public void indexAllProteinIdentificationsForProjectAndAssay(
      String projectAccession, String assayAccession, MZTabFile mzTabFile) {
    try {
      if (mzTabFile != null) {
        List<MongoProteinIdentification> proteinsFromFile =
            MongoProteinIdentificationMzTabBuilder.readProteinIdentificationsFromMzTabFile(
                projectAccession, assayAccession, mzTabFile);
        log.debug(
            "Found "
                + proteinsFromFile.size()
                + " Protein Identifications "
                + " for PROJECT:"
                + projectAccession
                + " and ASSAY:"
                + assayAccession);
        if (proteinsFromFile.size() > 0) {
          addCatalogInfoToProteinIdentifications(proteinsFromFile); // add synonyms, details, etc
          mongoProteinIdentificationIndexService.save(proteinsFromFile);
          log.debug(
              "COMMITTED "
                  + proteinsFromFile.size()
                  + " Protein Identifications from PROJECT:"
                  + projectAccession
                  + " ASSAY:"
                  + assayAccession);
        }
      } else {
        log.error(
            "An empty mzTab file has been passed to the indexing method - no indexing took place");
      }
    } catch (Exception e) {
      log.error(
          "Cannot index Protein Identifications from PROJECT:"
              + projectAccession
              + " and ASSAY:"
              + assayAccession);
      log.error("Reason: ");
      e.printStackTrace();
    }
  }

  /**
   * Deletes all protein identifications for a given project accession
   *
   * @param projectAccession The accession that identifies the PRIDE Archive project
   */
  public void deleteAllProteinIdentificationsForProject(String projectAccession) {
    List<MongoProteinIdentification> mongoProteinIdentifications =
        this.mongoProteinIdentificationSearchService.findByProjectAccession(projectAccession);
    this.mongoProteinIdentificationIndexService.delete(mongoProteinIdentifications);
  }

  /**
   * Use the protein Catalog to retrieve synonym information, protein details, etc.
   *
   * @param mongoProteinIdentifications The list to be enriched with information from the Catalog
   */
  private void addCatalogInfoToProteinIdentifications(
      List<MongoProteinIdentification> mongoProteinIdentifications) {
    for (MongoProteinIdentification mongoProteinIdentification : mongoProteinIdentifications) {
      findOtherMappings(mongoProteinIdentification);
    }
  }

  private void findOtherMappings(MongoProteinIdentification mongoProteinIdentification) {
    mongoProteinIdentification.setOtherMappings(new TreeSet<String>());
    mongoProteinIdentification.setDescription(new LinkedList<String>());
    List<ProteinIdentified> proteinsFromCatalog =
        proteinCatalogSearchService.findByAccession(mongoProteinIdentification.getAccession());
    if (proteinsFromCatalog != null && proteinsFromCatalog.size() > 0) {
      log.debug(
          "Protein "
              + mongoProteinIdentification.getAccession()
              + " already in the Catalog - getting details...");
      for (ProteinIdentified proteinIdentified : proteinsFromCatalog) {
        updateProteinIdentification(mongoProteinIdentification, proteinIdentified);
      }
    } else {
      log.error(
          "Protein "
              + mongoProteinIdentification.getId()
              + " not in the catalog - It should be saved by now...");
    }
  }

  private void updateProteinIdentification(
      MongoProteinIdentification mongoProteinIdentification, ProteinIdentified proteinFromCatalog) {
    if (proteinFromCatalog.getUniprotMapping() != null) {
      mongoProteinIdentification.setUniprotMapping(proteinFromCatalog.getUniprotMapping());
    }
    if (proteinFromCatalog.getEnsemblMapping() != null) {
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
