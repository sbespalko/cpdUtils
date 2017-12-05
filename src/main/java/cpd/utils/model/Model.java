package cpd.utils.model;

import cpd.utils.model.v10502.Promotion;
import java.util.List;

/**
 * @author bespalko
 * @since 04.12.2017
 */

public interface Model {
  List<Promotion> getAll();
  List<Promotion> getAll(String filter);
  List<String> getRaw();
  List<String> getRaw(String filter);
}
