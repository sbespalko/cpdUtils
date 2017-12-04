package cpd.utils.view;

import java.io.IOException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import cpd.utils.controller.Exporter;
import cpd.utils.helper.ConsoleService;

/**
 * @author bespalko
 * @since 04.12.2017
 */
@ShellComponent
@Slf4j
public class Commands {
  private final Exporter exporterCpd11;
  private @Setter @Getter ConsoleService console;

  public Commands(ConsoleService console, Exporter exporterCpd11) {
    this.console = console;
    this.exporterCpd11 = exporterCpd11;
  }

  @ShellMethod("exportTo promotions from CPD11 to <file.csv> (defaul=exportTo.csv)")
  public void exportCpd11(@ShellOption(defaultValue = "exportTo.csv") String fileName) throws IOException {
    log.info("start exporterCpd11.exportTo({})", fileName);
    exporterCpd11.exportTo(fileName);
    log.info("finish exporterCpd11.exportTo({})", fileName);
  }
}
