package cpd.utils.view;

import java.io.PrintStream;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author bespalko
 * @since 13.11.2017
 */
@Slf4j
@Service
public class ConsoleService {

  private static final String ANSI_YELLOW = "\u001B[33m";
  private static final String ANSI_RESET = "\u001B[0m";
  private static final String ANSI_RED = "\u001B[31m";

  private @Setter @Getter PrintStream out;

  public ConsoleService() {
    this.out = System.out;
  }

  public void answer(String msg, Object... args) {
    out.print("> ");
    out.print(ANSI_YELLOW);
    out.printf(msg, (Object[]) args);
    out.print(ANSI_RESET);
    out.println();
  }

  public void error(Throwable ex, String msg, Object... args) {
    out.print("> ");
    out.print(ANSI_RED);
    out.printf(msg, (Object[]) args);
    out.print(ANSI_RESET);
    out.println();
    log.debug("", ex);
  }
}