package cpd.utils.model;

import cpd.utils.model.v10502.Promotion;
import cpd.utils.transformer.Transformer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author bespalko
 * @since 05.12.2017
 */
@Slf4j
public class FileSystemModel11 implements Model {
  private final Transformer transformer11;
  private @Setter @Getter Path xmlPath;
  private @Setter @Getter String pattern;
  private PathMatcher matcher;

  public FileSystemModel11(Transformer transformer) {
    this.transformer11 = transformer;
    this.xmlPath = Paths.get("examples");
    this.pattern = "**.xml";
    this.matcher = FileSystems.getDefault().getPathMatcher("glob:" + this.pattern);
  }

  @Override
  public List<Promotion> getAll() {
    List<String> xmlTextFiles = getXmlTextFiles(xmlPath);
    return transformer11.deserialize(xmlTextFiles);
  }

  @Override
  public List<String> getRaw() {
    return getXmlTextFiles(xmlPath);
  }

  private List<String> getXmlTextFiles(Path path) {
    List<String> xmlTextFiles = new ArrayList<>();
    if(Files.notExists(path)){
      log.info("Path: {} not exist", path.toAbsolutePath().toString());
      return xmlTextFiles;
    }
    try (Stream<Path> paths = Files.walk(path)) {
      paths.filter(file -> Files.isRegularFile(file) && matcher.matches(file)).forEach(file -> {
        try {
          xmlTextFiles.add(new String(Files.readAllBytes(file)));
        } catch (IOException e) {
          log.error("", e);
        }
      });
    } catch (IOException e) {
      log.error("", e);
    }
    return xmlTextFiles;
  }

  @Override
  public List<Promotion> getAll(String filter) {
    return getAll();
  }

  @Override
  public List<String> getRaw(String filter) {
    return getRaw();
  }
}
