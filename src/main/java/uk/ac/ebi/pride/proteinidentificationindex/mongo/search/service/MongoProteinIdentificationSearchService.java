package uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.model.MongoProteinIdentification;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service.repository.MongoProteinIdentificationRepository;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * Service to search proteins in Mongo. NOTE: protein accessions can contain chars that produce
 * problems in solr queries ([,],:). They are replaced by _ when using the repository methods
 */
@SuppressWarnings("unused")
@Service
@Slf4j
public class MongoProteinIdentificationSearchService {

  @Resource private MongoProteinIdentificationRepository mongoProteinIdentificationRepository;

  /** Default constructor. */
  public MongoProteinIdentificationSearchService() {}

  /**
   * Constructor, sets the mongo protein id repository.
   *
   * @param mongoProteinIdentificationRepository the mongo protein id repository
   */
  public MongoProteinIdentificationSearchService(
      MongoProteinIdentificationRepository mongoProteinIdentificationRepository) {
    this.mongoProteinIdentificationRepository = mongoProteinIdentificationRepository;
  }

  /**
   * Sets the mongo protein id repository.
   *
   * @param mongoProteinIdentificationRepository the mongo * protein id repository
   */
  public void setMongoProteinIdentificationRepository(
      MongoProteinIdentificationRepository mongoProteinIdentificationRepository) {
    this.mongoProteinIdentificationRepository = mongoProteinIdentificationRepository;
  }

  /**
   * Finds a protein by id.
   *
   * @param id the id to search for
   * @return the protein, otherwise an empty protein is returned.
   */
  public MongoProteinIdentification findById(String id) {
    return mongoProteinIdentificationRepository
        .findById(id)
        .orElse(new MongoProteinIdentification());
  }

  /**
   * Finds a protein by IDs.
   *
   * @param ids the IDs to search for
   * @return the proteins, otherwise a list with an empty protein is returned.
   */
  public List<MongoProteinIdentification> findByIdIn(Collection<String> ids) {
    return mongoProteinIdentificationRepository.findByIdIn(ids);
  }

  /**
   * Finds a protein by IDs, sorted to a field.
   *
   * @param ids the IDs to search for
   * @param sort the field to sort results upon
   * @return the protein, otherwise an empty protein is returned.
   */
  public List<MongoProteinIdentification> findByIdIn(Collection<String> ids, Sort sort) {
    return mongoProteinIdentificationRepository.findByIdIn(ids, sort);
  }

  /**
   * Finds a list of proteins by project accession.
   *
   * @param projectAccession the project accession to query for
   * @return a list of proteins for a project accession
   */
  public List<MongoProteinIdentification> findByProjectAccession(String projectAccession) {
    return mongoProteinIdentificationRepository.findByProjectAccession(projectAccession);
  }

  /**
   * Finds a page of proteins by project accession.
   *
   * @param projectAccession the project accession to query for
   * @param pageable the page to query for
   * @return a page of proteins for a project accession
   */
  public Page<MongoProteinIdentification> findByProjectAccession(
      String projectAccession, Pageable pageable) {
    return mongoProteinIdentificationRepository.findByProjectAccession(projectAccession, pageable);
  }

  /**
   * Counts the total number of proteins for a project accession.
   *
   * @param projectAccession the project accession
   * @return the total number of proteins for a project accession
   */
  public long countByProjectAccession(String projectAccession) {
    return mongoProteinIdentificationRepository.countByProjectAccession(projectAccession);
  }

  /**
   * Finds a list of proteins by assay accession.
   *
   * @param assayAccession the assay accession to query for
   * @return a list of proteins for an assay accession
   */
  public List<MongoProteinIdentification> findByAssayAccession(String assayAccession) {
    return mongoProteinIdentificationRepository.findByAssayAccession(assayAccession);
  }

  /**
   * Finds a page of proteins by assay accession.
   *
   * @param assayAccession the assay accession to query for
   * @return a page of proteins for an assay accession
   */
  public Page<MongoProteinIdentification> findByAssayAccession(
      String assayAccession, Pageable pageable) {
    return mongoProteinIdentificationRepository.findByAssayAccession(assayAccession, pageable);
  }

  /**
   * Counts the total number of proteins for the assay accession.
   *
   * @param assayAccession the assay accession
   * @return the total number of proteins for an assay accession
   */
  public long countByAssayAccession(String assayAccession) {
    return mongoProteinIdentificationRepository.countByAssayAccession(assayAccession);
  }
}
