package cpd.utils.model;

import cpd.utils.model.v10502.Promotion;
import cpd.utils.transformer.Transformer;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

/**
 * @author bespalko
 * @since 04.12.2017
 */
@Slf4j
public class JdbcModel11 implements Model {
  private static final String PROMO_TABLE = "PD_PROMOTION_OVERVIEW";
  private static final String[] PROMO_COLUMNS = { "PROMOTION_ID", "UI_DATA_LAST_CHANGED" };
  private static final String GET_ALL_SQL = "SELECT "
                                            + String.join(",", PROMO_COLUMNS)
                                            + " FROM "
                                            + PROMO_TABLE
                                            + " ";

  private final Transformer transformer;
  @Autowired private JdbcTemplate jdbcTemplate;

  public JdbcModel11(Transformer transformer) {
    this.transformer = transformer;
  }

  @Override
  public List<Promotion> getAll() throws Exception {
    return getAll("");
  }

  @Override
  public List<Promotion> getAll(String filter) {
    return jdbcTemplate.query(GET_ALL_SQL + filter, (rs, rowNum) -> {
      Promotion promotion = new Promotion();
      Blob blob = rs.getBlob(PROMO_COLUMNS[1]);
      String blobText = null;
      if (!rs.wasNull()) {
        blobText = new String(blob.getBytes(1L, (int) blob.length()), StandardCharsets.UTF_8);
      }
      if (!StringUtils.isEmpty(blobText)) {
        try {
          promotion = transformer.deserialize(blobText);
        } catch (Exception e) {
          log.error("Convertation error: ", e);
        }
      }
      long id = rs.getLong(PROMO_COLUMNS[0]);
      if (!rs.wasNull()) {
        Promotion.GlobalData data = promotion.getGlobalData() != null ?
                                    promotion.getGlobalData() :
                                    new Promotion.GlobalData();
        data.setID(String.valueOf(id));
        promotion.setGlobalData(data);
      }
      return promotion;
    });
  }

  @Override
  public Map<Long, String> getRaw() {
    return getRaw("");
  }

  @Override
  public Map<Long, String> getRaw(String filter) {
    Map<Long, String> result = new LinkedHashMap<>();
    jdbcTemplate.query(GET_ALL_SQL + filter, (rs, rowNum) -> {
      long id = rs.getLong(PROMO_COLUMNS[0]);
      if (!rs.wasNull()) {
        Blob blob = rs.getBlob(PROMO_COLUMNS[1]);
        String textBlob = "";
        if (!rs.wasNull()) {
          textBlob = new String(blob.getBytes(1, (int) blob.length()), StandardCharsets.UTF_8);
        }
        result.put(id, textBlob);
      }
      return result;
    });
    return result;
  }
}
