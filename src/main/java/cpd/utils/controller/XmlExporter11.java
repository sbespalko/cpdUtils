package cpd.utils.controller;

import cpd.utils.model.Model;
import cpd.utils.transformer.XmlTransformer11;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author bespalko
 * @since 04.12.2017
 */
@Slf4j
@Component
public class XmlExporter11 extends Exporter11 {
  public XmlExporter11(Model model, XmlTransformer11 transformer) {
    super(transformer, model);
  }

  @Override
  public void exportTo(String dirName, String filter) throws IOException {
    //должно быть в начале, чтобы не делать остальную работу, если путь некорректный
    Path out = Paths.get(dirName);
    if (Files.exists(out)) {
      deleteDirectory(out);
    } else {
      Files.createDirectory(out);
    }

    log.info("Receive promotions");
    List<String> listXml = model.getRaw(filter);

    log.info("Save XML string into files");
    for (int i = 0; i < listXml.size(); i++) {
      Path fileToSave = out.resolve(i + ".xml");
      Files.write(fileToSave, listXml.get(i).getBytes(), StandardOpenOption.CREATE);
    }
  }

  private void deleteDirectory(Path out) throws IOException {
    Files.walkFileTree(out, new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Files.delete(file);
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        Files.delete(dir);
        return FileVisitResult.CONTINUE;
      }
    });
  }
}
