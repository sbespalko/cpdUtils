package cpd.utils.controller;

import cpd.utils.model.Model;
import cpd.utils.model.v10502.Promotion;
import cpd.utils.transformer.Transformer;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Null;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

/**
 * @author bespalko
 * @since 04.12.2017
 */
@Slf4j
@Controller
public class ExporterCpd11 implements Exporter {
  private final Model jdbcModel;
  private final Transformer csvTransformerCpd11;

  public ExporterCpd11(Model jdbcModel, Transformer csvTransformerCpd11) {
    this.jdbcModel = jdbcModel;
    this.csvTransformerCpd11 = csvTransformerCpd11;
  }

  public void exportTo(String fileName) throws IOException {
    //должно быть в начале, чтобы не делать остальную работу, если путь некорректный
    Path out = Paths.get(fileName);
    Files.deleteIfExists(out);

    log.info("Receive promotions");
    List<Promotion> promotions = jdbcModel.getAll();

    log.info("Transform promotions into CSV string");
    List<List<String>> listCsv = csvTransformerCpd11.transformTo(promotions);

    log.info("Save CSV string");
    writeToFile(out, listCsv);
  }

  /**
   * Convert List<List<String> to List<String> and save it to file;
   *
   * @param out     - file path
   * @param listCsv - not null list
   * @throws IOException - if file is blocked by another program;
   */
  private void writeToFile(@NonNull Path out, @NonNull List<List<String>> listCsv) throws IOException {
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
