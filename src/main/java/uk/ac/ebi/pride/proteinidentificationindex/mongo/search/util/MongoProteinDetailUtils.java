package uk.ac.ebi.pride.proteinidentificationindex.mongo.search.util;

import org.springframework.util.CollectionUtils;

import java.util.List;

/** Extracts protein detail information. */
public class MongoProteinDetailUtils {

  public static final String NAME = "NAME####";

  /**
   * Extracts protein detail information for a specified type.
   *
   * @param description the description to look through
   * @param type the type to find
   * @return the information according to the type found in the description
   */
  @SuppressWarnings("WeakerAccess")
  public static String extractInformationByType(List<String> description, String type) {
    String info = "";
    if (!CollectionUtils.isEmpty(description)) {
      for (String text : description) {
        if (text.startsWith(type)) {
          info = text.split(type)[1];
          break;
        }
      }
    }
    return info;
  }

  /**
   * Helper method to find the name in the description
   *
   * @param description the description to look through
   * @return the name, if one is present.
   */
  public static String getNameFromDescription(List<String> description) {
    return extractInformationByType(description, NAME);
  }
}
