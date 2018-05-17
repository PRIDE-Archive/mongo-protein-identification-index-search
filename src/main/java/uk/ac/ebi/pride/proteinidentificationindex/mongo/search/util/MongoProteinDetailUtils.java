package uk.ac.ebi.pride.proteinidentificationindex.mongo.search.util;

import org.springframework.util.CollectionUtils;

import java.util.List;

public class MongoProteinDetailUtils {

  public static final String NAME = "NAME####";

  public static String extractInformationByType(List<String> description, String type) {
    String info = "";
    if (!CollectionUtils.isEmpty(description)) {
      for (String text : description) {
        if (text.startsWith(type)) {
          info = text.split(type)[1];
        }
      }
    }
    return info;
  }

  public static String getNameFromDescription(List<String> description) {
    return extractInformationByType(description, NAME);
  }
}
