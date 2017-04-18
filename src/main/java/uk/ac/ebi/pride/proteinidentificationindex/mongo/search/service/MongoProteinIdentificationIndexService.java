package uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.model.MongoProteinIdentification;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service.repository.MongoProteinIdentificationRepository;

import javax.annotation.Resource;
import java.util.Collection;

/*
 * NOTE: protein accessions can contain chars that produce problems in solr queries ([,],:). They are replaced by _ when
 * using the repository methods
 */
@Service
public class MongoProteinIdentificationIndexService {

  @Resource
  private MongoProteinIdentificationRepository mongoProteinIdentificationRepository;

  private static Logger logger = LoggerFactory.getLogger(MongoProteinIdentificationIndexService.class.getName());

  public MongoProteinIdentificationIndexService() {
  }

  public void setMongoProteinIdentificationRepository(MongoProteinIdentificationRepository mongoProteinIdentificationRepository) {
    this.mongoProteinIdentificationRepository = mongoProteinIdentificationRepository;
  }

  @Transactional
  public void save(MongoProteinIdentification mongoProteinIdentification) {
    mongoProteinIdentificationRepository.save(mongoProteinIdentification);
  }

  @Transactional
  public Iterable<MongoProteinIdentification> save(Collection<MongoProteinIdentification> mongoProteinIdentifications) {
    return mongoProteinIdentificationRepository.save(mongoProteinIdentifications);
  }

  @Transactional
  public void delete(MongoProteinIdentification mongoProteinIdentification){
    mongoProteinIdentificationRepository.delete(mongoProteinIdentification);
  }

  public void delete(Iterable<MongoProteinIdentification> psms){
    mongoProteinIdentificationRepository.delete(psms);
  }
}
