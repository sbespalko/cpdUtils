package cpd.utils.helper;

import java.util.StringJoiner;
import org.springframework.util.StringUtils;

/**
 * @author bespalko
 * @since 04.12.2017
 */
public class CsvCreator {

  private static final String EMPTY_FIELD = "";
  private static final String DELIMITER = "\";\"";
  private static final String PREFIX = "\"";
  private static final String SUFFIX = "\"";

  public static String createLine(Object...args) {
    StringJoiner sj = new StringJoiner(DELIMITER, PREFIX, SUFFIX);
    for (Object arg : args) {
      sj.add(StringUtils.isEmpty(arg) ? EMPTY_FIELD : arg.toString());
    }
    return sj.toString();
  }

  public static String merge(String str1, String str2) {
    return str1 + DELIMITER.substring(1,2) + str2;
  }
}
