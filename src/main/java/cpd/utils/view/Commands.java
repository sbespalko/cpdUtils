package cpd.utils.view;

import cpd.utils.controller.CsvExporter11;
import cpd.utils.controller.XmlExporter11;
import java.io.IOException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import cpd.utils.controller.Exporter;

/**
 * @author bespalko
 * @since 04.12.2017
 */
@ShellComponent
@Slf4j
public class Commands {
  private final Exporter csvExporter11;
  private final Exporter xmlExporter11;
  private @Setter @Getter ConsoleService console;

  public Commands(ConsoleService console, CsvExporter11 csvExporter11, XmlExporter11 xmlExporter11) {
    this.console = console;
    this.csvExporter11 = csvExporter11;
    this.xmlExporter11 = xmlExporter11;
  }

  @ShellMethod("save promotions from CPD11 to <file.csv> (defaul=export11.csv)")
  public void exportCsv11(@ShellOption(defaultValue = "export11.csv") String fileName, @ShellOption(defaultValue = "") String filter) throws IOException {
    log.info("start csvExporter11.exportTo({})", fileName);
    csvExporter11.exportTo(fileName, filter);
    log.info("finish csvExporter11.exportTo({})", fileName);
  }

  @ShellMethod("save promotions from CPD11 to <folder> (default=raw11/)")
  public void exportXml11(@ShellOption(defaultValue = "raw11") String dirName, @ShellOption(defaultValue = "") String filter) throws IOException {
    log.info("start xmlExporter11.exportTo({})", dirName);
    xmlExporter11.exportTo(dirName, filter);
    log.info("finish xmlExporter11.exportTo({})", dirName);
  }
}
