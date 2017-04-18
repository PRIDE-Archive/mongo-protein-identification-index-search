package uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service;

import org.springframework.stereotype.Service;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.model.MongoProteinIdentification;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service.repository.MongoProteinIdentificationRepository;

import javax.annotation.Resource;
import java.util.*;

/*  NOTE: protein accessions can contain chars that produce problems in solr queries ([,],:). They are replaced by _ when
 *          using the repository methods
 */
@SuppressWarnings("unused")
@Service
public class MongoProteinIdentificationSearchService {

  @Resource
  private MongoProteinIdentificationRepository mongoProteinIdentificationRepository;

  public MongoProteinIdentificationSearchService() {
  }

  public MongoProteinIdentificationSearchService(MongoProteinIdentificationRepository mongoProteinIdentificationRepository) {
    this.mongoProteinIdentificationRepository = mongoProteinIdentificationRepository;
  }

  public void setMongoProteinIdentificationRepository(MongoProteinIdentificationRepository mongoProteinIdentificationRepository) {
    this.mongoProteinIdentificationRepository = mongoProteinIdentificationRepository;
  }

  public List<MongoProteinIdentification> findById(String id) {
    return mongoProteinIdentificationRepository.findById(id);
  }

  public List<MongoProteinIdentification> findById(Collection<String> ids) {
    return mongoProteinIdentificationRepository.findByIdIn(ids);
  }
  public List<MongoProteinIdentification> findByProjectAccession(String projectAccession) {
    return mongoProteinIdentificationRepository.findByProjectAccession(projectAccession);
  }
}
