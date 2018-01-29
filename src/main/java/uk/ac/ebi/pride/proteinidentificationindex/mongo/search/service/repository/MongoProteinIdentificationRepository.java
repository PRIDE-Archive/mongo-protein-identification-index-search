package uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service.repository;


import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.model.MongoProteinIdentification;

import java.util.Collection;
import java.util.List;

@Repository
public interface MongoProteinIdentificationRepository extends MongoRepository<MongoProteinIdentification, String> {

  MongoProteinIdentification findById(String id);
  List<MongoProteinIdentification> findByIdIn(Collection<String> id);
  List<MongoProteinIdentification> findByIdIn(Collection<String> id, Sort sort);

  List<MongoProteinIdentification> findByProjectAccession(String projectAccession);
  long countByProjectAccession(String projectAccession);
}
