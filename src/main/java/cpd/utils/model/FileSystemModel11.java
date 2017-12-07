package cpd.utils.model;

import cpd.utils.model.v10502.Promotion;
import cpd.utils.transformer.Transformer;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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
  public List<Promotion> getAll() throws Exception {
    Map<Long, String> xmlTextFiles = getXmlTextFiles(xmlPath);
    List<Promotion> promotions = new ArrayList<>(xmlTextFiles.size());
    for (Map.Entry<Long, String> entry : xmlTextFiles.entrySet()) {
      Promotion promotion = new Promotion();
      promotion = transformer11.deserialize(entry.getValue());
      Promotion.GlobalData data =
        promotion.getGlobalData() != null ? promotion.getGlobalData() : new Promotion.GlobalData();
      data.setID(String.valueOf(entry.getKey()));
      promotion.setGlobalData(data);
      promotions.add(promotion);
    }
    return promotions;
  }

  @Override
  public Map<Long, String> getRaw() {
    return getXmlTextFiles(xmlPath);
  }

  private Map<Long, String> getXmlTextFiles(Path path) {
    Map<Long, String> xmlTextFiles = new LinkedHashMap<>();
    if (Files.notExists(path)) {
      log.error("Path: {} not exist", path.toAbsolutePath().toString());
      return xmlTextFiles;
    }
    try (Stream<Path> paths = Files.walk(path)) {
      paths.filter(file -> Files.isRegularFile(file) && matcher.matches(file)).forEach(file -> {
        try {
          String fileNameWithoutExtention = file.getFileName().toString().replaceFirst("[.][^.]+$", "");
          xmlTextFiles.put(Long.valueOf(fileNameWithoutExtention), new String(Files.readAllBytes(file)));
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
  public List<Promotion> getAll(String filter) throws Exception {
    return getAll();
  }

  @Override
  public Map<Long, String> getRaw(String filter) {
    return getRaw();
  }
}
