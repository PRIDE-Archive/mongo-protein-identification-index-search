package uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.model.MongoProteinIdentification;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service.repository.MongoProteinIdentificationRepository;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * A service to index a project's proteins. NOTE: protein accessions can contain chars that produce
 * problems in solr queries ([,],:). They are replaced by _ when using the repository methods
 */
@Service
@Slf4j
public class MongoProteinIdentificationIndexService {

  @Resource private MongoProteinIdentificationRepository mongoProteinIdentificationRepository;

  public MongoProteinIdentificationIndexService() {}

  public void setMongoProteinIdentificationRepository(
      MongoProteinIdentificationRepository mongoProteinIdentificationRepository) {
    this.mongoProteinIdentificationRepository = mongoProteinIdentificationRepository;
  }

  @Transactional
  public void save(MongoProteinIdentification mongoProteinIdentification) {
    mongoProteinIdentificationRepository.save(mongoProteinIdentification);
  }

  @Transactional
  public void save(Collection<MongoProteinIdentification> mongoProteinIdentifications) {
    if (CollectionUtils.isEmpty(mongoProteinIdentifications)) log.debug("No PSMs to save");
    else {
      if (log.isDebugEnabled()) {
        debugSaveProteins(mongoProteinIdentifications);
      }
      mongoProteinIdentificationRepository.saveAll(mongoProteinIdentifications);
    }
  }

  @Transactional
  public void delete(MongoProteinIdentification mongoProteinIdentification) {
    mongoProteinIdentificationRepository.delete(mongoProteinIdentification);
  }

  @Transactional
  public void deleteById(String id) {
    mongoProteinIdentificationRepository.deleteById(id);
  }

  @Transactional
  public void delete(Collection<MongoProteinIdentification> mongoProteinIdentifications) {
    mongoProteinIdentificationRepository.deleteAll(mongoProteinIdentifications);
  }

  @Transactional
  public void deleteByIds(Collection<String> ids) {
    for (String id : ids) {
      mongoProteinIdentificationRepository.deleteById(id);
    }
  }

  /**
   * Deletes all proteins in Mongo for a project.
   *
   * @param projectAccession the project's accession number to delete proteins
   */
  @Transactional
  public void deleteByProjectAccession(String projectAccession) {
    mongoProteinIdentificationRepository.deleteAll(
        mongoProteinIdentificationRepository.findByProjectAccession(projectAccession));
  }

  /**
   * Deletes all proteins in Mongo for a project's assay.
   *
   * @param assayAccession the project's assay accession number to delete proteins
   */
  @Transactional
  public void deleteByAssayccession(String assayAccession) {
    mongoProteinIdentificationRepository.deleteAll(
        mongoProteinIdentificationRepository.findByAssayAccession(assayAccession));
  }

  /**
   * Output debug information related to proteins.
   *
   * @param mongoProteinIdentifications PSMs to debug
   */
  private void debugSaveProteins(
      Collection<MongoProteinIdentification> mongoProteinIdentifications) {
    int i = 0;
    for (MongoProteinIdentification mongoProteinIdentification : mongoProteinIdentifications) {
      log.debug("Saving PSM " + i + " with ID: " + mongoProteinIdentification.getId());
      log.debug("Project accession: " + mongoProteinIdentification.getProjectAccession());
      log.debug("Assay accession: " + mongoProteinIdentification.getAssayAccession());
      log.debug("Submitted sequence: " + mongoProteinIdentification.getSubmittedSequence());
      i++;
    }
  }
}
