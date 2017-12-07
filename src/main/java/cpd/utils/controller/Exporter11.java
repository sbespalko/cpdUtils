package cpd.utils.controller;

import cpd.utils.model.Model;
import cpd.utils.transformer.CsvHeader;
import cpd.utils.transformer.Transformer;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bespalko
 * @since 04.12.2017
 */
public abstract class Exporter11 implements Exporter {
  final Model model;
  final Transformer transformer;

  Exporter11(Model model, Transformer transformer) {
    this.transformer = transformer;
    this.model = model;
  }

  public abstract void exportTo(String fileName, String filter) throws Exception;

  /**
   * Convert List<String> to String and save it to file;
   *
   * @param out     - file path
   * @param listCsv - not null list
   * @throws IOException - if file is blocked by another program;
   */
  void writeToFile(Path out, List<String> listCsv) throws IOException {
    List<String> resultCsv = new ArrayList<>();
    int headerLength = CsvHeader.HEADER.length();
    resultCsv.add(CsvHeader.HEADER.substring(0, headerLength - 1));
    for (String csv : listCsv) {
      if (csv.length() > headerLength) {
        resultCsv.add(csv.substring(headerLength, csv.length() - 1));
      }
    }
    Files.write(out, resultCsv, Charset.defaultCharset(), StandardOpenOption.CREATE);
  }
}
