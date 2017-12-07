package cpd.utils.controller;

import cpd.utils.model.Model;
import cpd.utils.transformer.Transformer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bespalko
 * @since 04.12.2017
 */
@Slf4j
public class XmlExporter11 extends Exporter11 {
  public XmlExporter11(Model model, Transformer transformer) {
    super(model, transformer);
  }

  @Override
  public void exportTo(String dirName, String filter) throws Exception {
    //должно быть в начале, чтобы не делать остальную работу, если путь некорректный
    Path out = Paths.get(dirName);
    if (Files.exists(out)) {
      deleteDirectory(out);
    }
    Files.createDirectory(out);

    log.info("Receive promotions");
    Map<Long, String> mapXml = model.getRaw(filter);

    log.info("Save XML string into files");
    for (Map.Entry<Long, String> entry : mapXml.entrySet()) {
      Path fileToSave = out.resolve(entry.getKey() + ".xml");
      Files.write(fileToSave, entry.getValue().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
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
