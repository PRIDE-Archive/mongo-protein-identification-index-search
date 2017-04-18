package uk.ac.ebi.pride.proteinidentificationindex.mongo.search.model;

import org.springframework.data.rest.core.config.Projection;

/**
 * @author florian@ebi.ac.uk.
 */
@Projection(name = "summary", types = MongoProteinIdentification.class)
public interface MongoProteinIdentificationSummary {

    String getAccession();
}
