package cpd.utils.controller;

import cpd.utils.model.Model;
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

  public Exporter11(Transformer transformer, Model model) {
    this.transformer = transformer;
    this.model = model;
  }

  public abstract void exportTo(String fileName, String filter) throws IOException;

  /**
   * Convert List<List<String> to List<String> and save it to file;
   *
   * @param out     - file path
   * @param listCsv - not null list
   * @throws IOException - if file is blocked by another program;
   */
  void writeToFile(Path out, List<List<String>> listCsv) throws IOException {
    List<String> resultCsv = new ArrayList<>();
    if (!listCsv.isEmpty()) {
      resultCsv.addAll(listCsv.get(0));
    }
    for (int i = 1; i < listCsv.size(); i++) {
      List<String> csv = listCsv.get(i);
      resultCsv.addAll(csv.subList(1, csv.size()));
    }
    Files.write(out, resultCsv, Charset.defaultCharset(), StandardOpenOption.CREATE);
  }
}
