package uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.model.MongoProteinIdentification;

import java.util.Collection;
import java.util.List;

/** The Mongo Protein ID repository. */
@Repository
public interface MongoProteinIdentificationRepository
    extends MongoRepository<MongoProteinIdentification, String> {

  /**
   * Finds a sorted list of proteins in a collection of IDs.
   *
   * @param ids a collection of IDs to search for *
   * @param pageable the page
   * @return a sorted list of proteins corresponding to the provided IDs.
   */
  List<MongoProteinIdentification> findByIdIn(Collection<String> ids, Pageable pageable);

  /**
   * Finds a page of proteins for a project accession.
   *
   * @param projectAccession the project accession to search for
   * @return a page of proteins corresponding to the project accession
   */
  Page<MongoProteinIdentification> findByProjectAccession(
      String projectAccession, Pageable pageable);

  /**
   * Counts the total number of proteins for a project accession.
   *
   * @param projectAccession the project accession to search for
   * @return a total number of proteins for the project accession
   */
  long countByProjectAccession(String projectAccession);

  /**
   * Finds a page of proteins for a assay accession.
   *
   * @param assayAccession the assay accession to search for
   * @return a page of proteins corresponding to the assay accession
   */
  Page<MongoProteinIdentification> findByAssayAccession(String assayAccession, Pageable pageable);

  /**
   * Counts the total number of proteins for a assay accession.
   *
   * @param assayAccession the assay accession to search for
   * @return a total number of proteins for the assay accession
   */
  long countByAssayAccession(String assayAccession);
}
