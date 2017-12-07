package cpd.utils.model;

import cpd.utils.model.v10502.Promotion;
import java.util.List;
import java.util.Map;

/**
 * @author bespalko
 * @since 04.12.2017
 */

public interface Model {
  List<Promotion> getAll() throws Exception;
  List<Promotion> getAll(String filter) throws Exception;
  Map<Long, String> getRaw() throws Exception;
  Map<Long, String> getRaw(String filter) throws Exception;
}
