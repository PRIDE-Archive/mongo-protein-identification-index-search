package uk.ac.ebi.pride.proteinidentificationindex.mongo.search.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;
import uk.ac.ebi.pride.jmztab.model.MZTabFile;
import uk.ac.ebi.pride.jmztab.utils.MZTabFileParser;
import uk.ac.ebi.pride.proteincatalogindex.search.util.ErrorLogOutputStream;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.config.MongoTestConfiguration;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.util.MongoProteinDetailUtils;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.util.MongoProteinIdentificationIdBuilder;
import uk.ac.ebi.pride.proteinidentificationindex.mongo.search.util.MongoProteinIdentificationMzTabBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoTestConfiguration.class})
@Slf4j
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
  public void testCreateId() {
    String newID =
        MongoProteinIdentificationIdBuilder.createId(
            PROTEIN_ACCESSION, PROJECT_ACCESSION, ASSAY_ACCESSION);
    assertEquals(ID, newID);
  }

  @Test
  public void testGetProteinAccession() {
    String proteinAccession = MongoProteinIdentificationIdBuilder.getProteinAccession(ID);
    assertEquals(PROTEIN_ACCESSION, proteinAccession);
  }

  @Test
  public void testGetProjectAccession() {
    String projectAccession = MongoProteinIdentificationIdBuilder.getProjectAccession(ID);
    assertEquals(PROJECT_ACCESSION, projectAccession);
  }

  @Test
  public void testGetAssayAccession() {
    String assayAccession = MongoProteinIdentificationIdBuilder.getAssayAccession(ID);
    assertEquals(ASSAY_ACCESSION, assayAccession);
  }

  @Test
  public void testBuildId() {
    List<String> descriptions = new ArrayList<>();
    String testDescription = "testDescription";
    descriptions.add("dummyfirstpart####" + "testDescription");
    descriptions.add(MongoProteinDetailUtils.NAME + "testDescription");
    assertEquals(testDescription, MongoProteinDetailUtils.getNameFromDescription(descriptions));
  }

  @Test
  public void testProteinIdMzTabBuild() throws IOException {
    MZTabFile mzTabFileP1A1 =
        new MZTabFileParser(
                new File(
                    "src/test/resources/submissions/2014/01/PXD000581/generated/PRIDE_Exp_Complete_Ac_32411.mztab"),
                new ErrorLogOutputStream(log))
            .getMZTabFile();
    List<MongoProteinIdentification> proteins =
        MongoProteinIdentificationMzTabBuilder.readProteinIdentificationsFromMzTabFile(
            "PXD000581", "32411", mzTabFileP1A1);
    assertTrue(!CollectionUtils.isEmpty(proteins));
  }
}
