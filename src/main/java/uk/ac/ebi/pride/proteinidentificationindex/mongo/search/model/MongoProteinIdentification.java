package uk.ac.ebi.pride.proteinidentificationindex.mongo.search.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.ac.ebi.pride.archive.dataprovider.identification.ModificationProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.ProteinDetailProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.ProteinIdentificationProvider;
import uk.ac.ebi.pride.indexutils.helpers.ModificationHelper;
import uk.ac.ebi.pride.proteincatalogindex.search.util.ProteinDetailUtils;

import java.util.*;

@Document(collection = "proteinids")
public class MongoProteinIdentification
    implements ProteinIdentificationProvider, ProteinDetailProvider {

  @Id private String id;

  private String submittedAccession;

  private String accession;

  private String uniprotMapping;

  private String ensemblMapping;

  private Set<String> otherMappings;

  private String submittedSequence;

  private String inferredSequence;

  private List<String> description;

  private List<String> ambiguityGroupSubmittedAccessions;

  private List<String> modificationsAsString;

  private List<String> modificationNames;

  private List<String> modificationAccessions;

  private String projectAccession;

  private String assayAccession;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSubmittedAccession() {
    return submittedAccession;
  }

  public void setSubmittedAccession(String submittedAccession) {
    this.submittedAccession = submittedAccession;
  }

  public String getAccession() {
    return accession;
  }

  public void setAccession(String accession) {
    this.accession = accession;
  }

  public String getName() {
    return ProteinDetailUtils.getNameFromDescription(description);
  }

  public String getProjectAccession() {
    return projectAccession;
  }

  public void setProjectAccession(String projectAccession) {
    this.projectAccession = projectAccession;
  }

  public String getAssayAccession() {
    return assayAccession;
  }

  public void setAssayAccession(String assayAccession) {
    this.assayAccession = assayAccession;
  }

  public String getSubmittedSequence() {
    return submittedSequence;
  }

  public void setSubmittedSequence(String submittedSequence) {
    this.submittedSequence = submittedSequence;
  }

  public List<String> getDescription() {
    return description;
  }

  public void setDescription(List<String> description) {
    this.description = description;
  }

  public List<String> getAmbiguityGroupSubmittedAccessions() {
    return ambiguityGroupSubmittedAccessions;
  }

  public void setAmbiguityGroupSubmittedAccessions(List<String> ambiguityGroupSubmittedAccessions) {
    this.ambiguityGroupSubmittedAccessions = ambiguityGroupSubmittedAccessions;
  }

  public Iterable<ModificationProvider> getModifications() {
    List<ModificationProvider> modifications = new ArrayList<>();
    if (modificationsAsString != null) {
      for (String mod : modificationsAsString) {
        if (!mod.isEmpty()) {
          modifications.add(ModificationHelper.convertFromString(mod));
        }
      }
    }
    return modifications;
  }

  public void setModifications(List<ModificationProvider> modifications) {
    if (modifications == null) return;
    List<String> modificationsAsString = new ArrayList<>();
    List<String> modificationNames = new ArrayList<>();
    List<String> modificationAccessions = new ArrayList<>();
    for (ModificationProvider modification : modifications) {
      modificationsAsString.add(ModificationHelper.convertToString(modification));
      modificationAccessions.add(modification.getAccession());
      modificationNames.add(modification.getName());
    }
    this.modificationsAsString = modificationsAsString;
    this.modificationAccessions = modificationAccessions;
    this.modificationNames = modificationNames;
  }

  public void addModification(ModificationProvider modification) {
    if (modificationsAsString == null) {
      modificationsAsString = new ArrayList<>();
    }
    if (modificationAccessions == null) {
      modificationAccessions = new ArrayList<>();
    }
    if (modificationNames == null) {
      modificationNames = new ArrayList<>();
    }
    modificationsAsString.add(ModificationHelper.convertToString(modification));
    modificationAccessions.add(modification.getAccession());
    modificationNames.add(modification.getName());
  }

  public Set<String> getModificationNames() {
    Set<String> modificationNamesSet = new TreeSet<>();
    if (modificationNames != null) {
      modificationNamesSet.addAll(modificationNames);
    }
    return modificationNamesSet;
  }

  public Set<String> getModificationAccessions() {
    Set<String> modificationAccessionsSet = new TreeSet<>();
    if (modificationAccessions != null) {
      modificationAccessionsSet.addAll(modificationAccessions);
    }
    return modificationAccessionsSet;
  }

  public Map<String, String> getModificationAccessionName() {
    Map<String, String> modificationAccessionsNameMap = new TreeMap<>();
    if (modificationNames != null && modificationAccessions != null) {
      if (modificationNames.size() == modificationAccessions.size()) {
        for (int i = 0; i < modificationNames.size(); i++) {
          modificationAccessionsNameMap.put(
              modificationNames.get(i), modificationAccessions.get(i));
        }
      }
    }
    return modificationAccessionsNameMap;
  }

  public String getUniprotMapping() {
    return uniprotMapping;
  }

  public void setUniprotMapping(String uniprotMapping) {
    this.uniprotMapping = uniprotMapping;
  }

  public String getEnsemblMapping() {
    return ensemblMapping;
  }

  public void setEnsemblMapping(String ensemblMapping) {
    this.ensemblMapping = ensemblMapping;
  }

  public Set<String> getOtherMappings() {
    return otherMappings;
  }

  public void setOtherMappings(Set<String> otherMappings) {
    this.otherMappings = otherMappings;
  }

  public String getInferredSequence() {
    return inferredSequence;
  }

  public void setInferredSequence(String inferredSequence) {
    this.inferredSequence = inferredSequence;
  }
}
