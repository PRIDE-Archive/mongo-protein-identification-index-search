package uk.ac.ebi.pride.proteinidentificationindex.mongo.search.indexer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.model.MongoProteinIdentification;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service.MongoProteinIdentificationIndexService;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service.MongoProteinIdentificationSearchService;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service.repository.MongoProteinIdentificationRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-mongo-test-context.xml")
public class ProjectMongoProteinIdentificationIndexerTest {

  private static Logger logger = LoggerFactory.getLogger(ProjectMongoProteinIdentificationIndexerTest.class.getName());

  private static final String TEST_SUBMITTED_SEQ = "MSSEEVVVAVEEQEIPDVIERLMSSEEVVVAVEEQEIPDVIERLMSSEEVVVAVEEQEIPDVIERL";
  private static final String TEST_ID = "TEST_ID";
  private static final String TEST_SUBMITTED_AC = "D0NNB3";

  private static final String TEST_PROTEIN_ACCESSION = "D0NNB3";

  @Resource
  private MongoProteinIdentificationRepository mongoProteinIdentificationRepository;

  @Resource
  MongoProteinIdentificationSearchService mongoProteinIdentificationSearchService;

  @Resource
  MongoProteinIdentificationIndexService mongoProteinIdentificationIndexService;

  @Before
  public void setUp() throws Exception {
    mongoProteinIdentificationRepository.deleteAll();
  }

  @Test
  public void testThatNoResultsAreReturned() {
    MongoProteinIdentification result = mongoProteinIdentificationSearchService.findById(TEST_ID);
    assertNull(result);
  }

  @Test
  public void testAddProtein() {
    addD0NNb3();
    MongoProteinIdentification result = mongoProteinIdentificationSearchService.findById(TEST_ID);
    assertNotNull(result);
  }

  private void addD0NNb3() {
    MongoProteinIdentification mongoProteinIdentification = new MongoProteinIdentification();
    mongoProteinIdentification.setId(TEST_ID);
    mongoProteinIdentification.setAccession(TEST_PROTEIN_ACCESSION);
    mongoProteinIdentification.setSubmittedAccession(TEST_SUBMITTED_AC);
    mongoProteinIdentification.setSubmittedSequence(TEST_SUBMITTED_SEQ);
    mongoProteinIdentificationIndexService.save(mongoProteinIdentification);
  }
}
