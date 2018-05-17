package uk.ac.ebi.pride.proteinidentificationindex.mongo.search.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.config.MongoTestConfiguration;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.util.MongoProteinIdentificationIdBuilder;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoTestConfiguration.class})
public class MongoMongoProteinIdentificationIdBuilderTest {

  private static final String PROTEIN_ACCESSION = "protein-accession-1_2_3_4";
  private static final String PROJECT_ACCESSION = "project-accession-1_2_3_4";
  private static final String ASSAY_ACCESSION = "assay-accession-1_2_3_4";
  private static final String ID =
      PROTEIN_ACCESSION
          + MongoProteinIdentificationIdBuilder.SEPARATOR
          + PROJECT_ACCESSION
          + MongoProteinIdentificationIdBuilder.SEPARATOR
          + ASSAY_ACCESSION;

  @Test
  public void testGetId() throws Exception {
    String newID =
        MongoProteinIdentificationIdBuilder.getId(
            PROTEIN_ACCESSION, PROJECT_ACCESSION, ASSAY_ACCESSION);
    assertEquals(ID, newID);
  }

  @Test
  public void testGetProteinAccession() throws Exception {
    String proteinAccession = MongoProteinIdentificationIdBuilder.getProteinAccession(ID);
    assertEquals(PROTEIN_ACCESSION, proteinAccession);
  }

  @Test
  public void testGetProjectAccession() throws Exception {
    String projectAccession = MongoProteinIdentificationIdBuilder.getProjectAccession(ID);
    assertEquals(PROJECT_ACCESSION, projectAccession);
  }

  @Test
  public void testGetAssayAccession() throws Exception {
    String assayAccession = MongoProteinIdentificationIdBuilder.getAssayAccession(ID);
    assertEquals(ASSAY_ACCESSION, assayAccession);
  }
}
