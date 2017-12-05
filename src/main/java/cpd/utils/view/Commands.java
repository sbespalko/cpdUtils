package cpd.utils.view;

import cpd.utils.controller.ExporterCsv11;
import cpd.utils.controller.ExporterXml11;
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
  private final Exporter exporterCsv11;
  private final Exporter exporterXml11;
  private @Setter @Getter ConsoleService console;

  public Commands(ConsoleService console, ExporterCsv11 exporterCsv11, ExporterXml11 exporterXml11) {
    this.console = console;
    this.exporterCsv11 = exporterCsv11;
    this.exporterXml11 = exporterXml11;
  }

  @ShellMethod("save promotions from CPD11 to <file.csv> (defaul=export11.csv)")
  public void exportCsv11(@ShellOption(defaultValue = "export11.csv") String fileName) throws IOException {
    log.info("start exporterCsv11.exportTo({})", fileName);
    exporterCsv11.exportTo(fileName);
    log.info("finish exporterCsv11.exportTo({})", fileName);
  }

  @ShellMethod("save promotions from CPD11 to <folder> (default=raw11/)")
  public void exportXml11(@ShellOption(defaultValue = "raw11") String dirName) throws IOException {
    log.info("start exporterCsv11.exportTo({})", dirName);
    exporterXml11.exportTo(dirName);
    log.info("finish exporterCsv11.exportTo({})", dirName);
  }
}
