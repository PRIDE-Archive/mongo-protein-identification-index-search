package uk.ac.ebi.pride.proteinidentificationindex.mongo.search.indexer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.config.MongoTestConfiguration;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.model.MongoProteinIdentification;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service.MongoProteinIdentificationIndexService;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service.MongoProteinIdentificationSearchService;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service.repository.MongoProteinIdentificationRepository;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoTestConfiguration.class})
public class ProjectMongoProteinIdentificationIndexerTest {

  private static final String TEST_SUBMITTED_SEQ =
      "MSSEEVVVAVEEQEIPDVIERLMSSEEVVVAVEEQEIPDVIERLMSSEEVVVAVEEQEIPDVIERL";
  private static final String TEST_ID = "TEST_ID";
  private static final String TEST_SUBMITTED_AC = "D0NNB3";
  private static final String TEST_PROJ_ACC = "PXT000001";
  private static final String TEST_PROTEIN_ACCESSION = "D0NNB3";
  private static Logger logger =
      LoggerFactory.getLogger(ProjectMongoProteinIdentificationIndexerTest.class.getName());
  @Resource private MongoProteinIdentificationSearchService mongoProteinIdentificationSearchService;
  @Resource private MongoProteinIdentificationIndexService mongoProteinIdentificationIndexService;
  @Resource private MongoProteinIdentificationRepository mongoProteinIdentificationRepository;

  @Before
  public void setUp() {
    mongoProteinIdentificationRepository.deleteAll();
  }

  @Test
  public void testThatNoResultsAreReturned() {
    MongoProteinIdentification result = mongoProteinIdentificationSearchService.findById(TEST_ID);
    assertEquals(result.getId(), (new MongoProteinIdentification()).getId());
  }

  @Test
  public void testAddProtein() {
    addD0NNb3();
    MongoProteinIdentification result = mongoProteinIdentificationSearchService.findById(TEST_ID);
    assertNotNull(result);
    long totalCount =
        mongoProteinIdentificationSearchService.countByProjectAccession(TEST_PROJ_ACC);
    assertEquals(1, totalCount);
  }

  private void addD0NNb3() {
    MongoProteinIdentification mongoProteinIdentification = new MongoProteinIdentification();
    mongoProteinIdentification.setId(TEST_ID);
    mongoProteinIdentification.setAccession(TEST_PROTEIN_ACCESSION);
    mongoProteinIdentification.setSubmittedAccession(TEST_SUBMITTED_AC);
    mongoProteinIdentification.setSubmittedSequence(TEST_SUBMITTED_SEQ);
    mongoProteinIdentification.setProjectAccession(TEST_PROJ_ACC);
    mongoProteinIdentificationIndexService.save(mongoProteinIdentification);
  }
}
