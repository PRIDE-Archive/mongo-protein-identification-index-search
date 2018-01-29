package uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
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

  @Autowired
  private MongoOperations mongoOperations;

  public MongoProteinIdentificationSearchService() {
  }

  public MongoProteinIdentificationSearchService(MongoProteinIdentificationRepository mongoProteinIdentificationRepository) {
    this.mongoProteinIdentificationRepository = mongoProteinIdentificationRepository;
  }

  public void setMongoProteinIdentificationRepository(MongoProteinIdentificationRepository mongoProteinIdentificationRepository) {
    this.mongoProteinIdentificationRepository = mongoProteinIdentificationRepository;
  }

  public MongoProteinIdentification findById(String id) {
    return mongoProteinIdentificationRepository.findById(id);
  }

  public List<MongoProteinIdentification> findByIdIn(Collection<String> ids) {
    return mongoProteinIdentificationRepository.findByIdIn(ids);
  }

  public List<MongoProteinIdentification> findByIdIn(Collection<String> ids, Sort sort) {
    return mongoProteinIdentificationRepository.findByIdIn(ids, sort);
  }

  public List<MongoProteinIdentification> findByProjectAccession(String projectAccession) {
    return mongoProteinIdentificationRepository.findByProjectAccession(projectAccession);
  }

  public long countByProjectAccession(String projectAccession) {
    return mongoProteinIdentificationRepository.countByProjectAccession(projectAccession);
  }
}
