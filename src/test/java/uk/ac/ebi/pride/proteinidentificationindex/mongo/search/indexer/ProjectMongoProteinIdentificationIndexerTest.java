package uk.ac.ebi.pride.proteinidentificationindex.mongo.search.indexer;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.config.MongoTestConfiguration;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.model.MongoProteinIdentification;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service.MongoProteinIdentificationIndexService;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service.MongoProteinIdentificationSearchService;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.service.repository.MongoProteinIdentificationRepository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoTestConfiguration.class})
@Slf4j
public class ProjectMongoProteinIdentificationIndexerTest {

  private static final String TEST_SUBMITTED_SEQ =
      "MSSEEVVVAVEEQEIPDVIERLMSSEEVVVAVEEQEIPDVIERLMSSEEVVVAVEEQEIPDVIERL";
  private static final String TEST_ID = "TEST_ID";
  private static final String TEST_SUBMITTED_AC = "D0NNB3";
  private static final String TEST_PROJ_ACC = "PXT000001";
  private static final String TEST_PROTEIN_ACCESSION = "D0NNB3";

  @Resource private MongoProteinIdentificationSearchService mongoProteinIdentificationSearchService;
  @Resource private MongoProteinIdentificationIndexService mongoProteinIdentificationIndexService;
  @Resource private MongoProteinIdentificationRepository mongoProteinIdentificationRepository;

  @Before
  public void setUp() {
    mongoProteinIdentificationRepository.deleteAll();
    mongoProteinIdentificationIndexService.setMongoProteinIdentificationRepository(
        mongoProteinIdentificationRepository);
  }

  @Test
  public void testThatNoResultsAreReturned() {
    MongoProteinIdentification result = mongoProteinIdentificationSearchService.findById(TEST_ID);
    assertEquals(result.getId(), (new MongoProteinIdentification()).getId());
  }

  @Test
  public void testAddProtein() {
    List<MongoProteinIdentification> proteins = new ArrayList<>();
    mongoProteinIdentificationIndexService.save(proteins);
    mongoProteinIdentificationIndexService.save(createTestProtein());
    proteins.add(createTestProtein());
    mongoProteinIdentificationIndexService.save(proteins);
    MongoProteinIdentification result = mongoProteinIdentificationSearchService.findById(TEST_ID);
    assertNotNull(result);
    long totalCount =
        mongoProteinIdentificationSearchService.countByProjectAccession(TEST_PROJ_ACC);
    assertEquals(1, totalCount);
  }

  private MongoProteinIdentification createTestProtein() {
    MongoProteinIdentification mongoProteinIdentification = new MongoProteinIdentification();
    mongoProteinIdentification.setId(TEST_ID);
    mongoProteinIdentification.setAccession(TEST_PROTEIN_ACCESSION);
    mongoProteinIdentification.setSubmittedAccession(TEST_SUBMITTED_AC);
    mongoProteinIdentification.setSubmittedSequence(TEST_SUBMITTED_SEQ);
    mongoProteinIdentification.setProjectAccession(TEST_PROJ_ACC);
    return mongoProteinIdentification;
  }

  @After
  public void deleteProteins() {
    MongoProteinIdentification protein = createTestProtein();
    mongoProteinIdentificationIndexService.delete(protein);
    mongoProteinIdentificationIndexService.save(protein);
    List<MongoProteinIdentification> proteins = new ArrayList<>();
    mongoProteinIdentificationIndexService.delete(proteins);
    proteins.add(protein);
    mongoProteinIdentificationIndexService.delete(proteins);
    mongoProteinIdentificationIndexService.save(protein);
    mongoProteinIdentificationIndexService.deleteById(protein.getId());
    mongoProteinIdentificationIndexService.save(protein);
    mongoProteinIdentificationIndexService.deleteByIds(
        proteins.stream().map(MongoProteinIdentification::getId).collect(Collectors.toList()));
    mongoProteinIdentificationIndexService.save(protein);
    mongoProteinIdentificationIndexService.deleteByProjectAccession(protein.getProjectAccession());
    mongoProteinIdentificationIndexService.save(protein);
    mongoProteinIdentificationIndexService.deleteByAssayAccession(protein.getAssayAccession());
  }
}
