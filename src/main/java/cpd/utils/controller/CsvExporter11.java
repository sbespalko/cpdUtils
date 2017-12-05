package cpd.utils.controller;

import cpd.utils.model.Model;
import cpd.utils.model.v10502.Promotion;
import cpd.utils.transformer.CsvTransformer11;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

/**
 * @author bespalko
 * @since 04.12.2017
 */
@Slf4j
@Controller
public class CsvExporter11 extends Exporter11 {

  public CsvExporter11(Model model, CsvTransformer11 transformer) {
    super(transformer, model);
  }

  @Override
  public void exportTo(String fileName, String filter) throws IOException {
    //должно быть в начале, чтобы не делать остальную работу, если путь некорректный
    Path out = Paths.get(fileName);
    Files.createFile(out);
    Files.deleteIfExists(out);

    log.info("Receive promotions");
    List<Promotion> promotions = model.getAll();

    log.info("Transform promotions into CSV string");
    List<List<String>> listCsv = transformer.serialize(promotions);

    log.info("Save CSV string");
    writeToFile(out, listCsv);
  }

}
