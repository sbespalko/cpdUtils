package cpd.utils.controller;

import java.io.IOException;

/**
 * @author bespalko
 * @since 04.12.2017
 */
public interface Exporter {
  void exportTo(String fileName) throws IOException;
}
