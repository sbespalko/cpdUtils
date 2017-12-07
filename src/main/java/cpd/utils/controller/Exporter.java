package cpd.utils.controller;

/**
 * @author bespalko
 * @since 04.12.2017
 */
public interface Exporter {
  void exportTo(String fileName, String filter) throws Exception;
}
