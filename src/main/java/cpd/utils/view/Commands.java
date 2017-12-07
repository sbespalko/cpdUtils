package cpd.utils.view;

import cpd.utils.controller.CsvExporter11;
import cpd.utils.controller.Exporter;
import cpd.utils.controller.XmlExporter11;
import java.io.IOException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * @author bespalko
 * @since 04.12.2017
 */
@Slf4j
@ShellComponent
public class Commands {
  private final Exporter csvExporter11;
  private final Exporter xmlExporter11;
  @Setter @Getter private  ConsoleService console;

  public Commands(ConsoleService console, CsvExporter11 csvExporter11, XmlExporter11 xmlExporter11) {
    this.console = console;
    this.csvExporter11 = csvExporter11;
    this.xmlExporter11 = xmlExporter11;
  }

  @ShellMethod("save promotions from CPD11 to <file.csv> (defaul=export11.csv)")
  public void exportCsv11(@ShellOption(defaultValue = "export11.csv") String fileName, @ShellOption(defaultValue = "") String filter) throws IOException {
    console.answer("start exportCsv11 into \"%s\"", fileName);
    try {
      csvExporter11.exportTo(fileName, filter);
      console.answer("finish exportCsv11 into \"%s\"", fileName);
    } catch (Exception e) {
      log.error("Can't export: {}", e.getMessage());
    }
  }

  @ShellMethod("save promotions from CPD11 to <folder> (default=raw11/)")
  public void exportXml11(@ShellOption(defaultValue = "raw11") String dirName, @ShellOption(defaultValue = "") String filter) throws IOException {
    console.answer("start exportXml11 into \"/%s\"", dirName);
    try {
      xmlExporter11.exportTo(dirName, filter);
      console.answer("finish exportXml11 into \"/%s\"", dirName);
    } catch (Exception e) {
      log.error("Can't export: {}", e.getMessage());
    }
  }
}

