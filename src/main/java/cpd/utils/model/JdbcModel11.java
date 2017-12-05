package cpd.utils.model;

import cpd.utils.model.v10502.Promotion;
import cpd.utils.transformer.Transformer;
import cpd.utils.transformer.XmlTransformer11;
import java.sql.Blob;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author bespalko
 * @since 04.12.2017
 */
@Repository
public class JdbcModel11 implements Model {
  private static final String PROMO_TABLE = "PD_PROMOTION_OVERVIEW";
  private static final String PROMO_COLUMN = "UI_DATA_LAST_CHANGED";
  private static final String GET_All_SQL = "SELECT " + PROMO_COLUMN + " FROM " + PROMO_TABLE;

  private Transformer transformer;
  private JdbcTemplate jdbcTemplate;

  public JdbcModel11(Transformer transformer) {
    this.transformer = transformer;
  }

  @Autowired
  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Promotion> getAll() {
    return getAll("");
  }

  @Override
  public List<Promotion> getAll(String filter) {
    List<String> stringsPromotion = jdbcTemplate.query(GET_All_SQL + " " + filter, (rs, rowNum) -> {
      Blob blob = rs.getBlob(PROMO_COLUMN);
      return new String(blob.getBytes(1, (int) blob.length()));
    });
    return transformer.deserialize(stringsPromotion);
  }

  @Override
  public List<String> getRaw(String filter) {
    return jdbcTemplate.query(GET_All_SQL + " " + filter, (rs, rowNum) -> {
      Blob blob = rs.getBlob(PROMO_COLUMN);
      return new String(blob.getBytes(1, (int) blob.length()));
    });
  }

  @Override
  public List<String> getRaw() {
    return getRaw("");
  }

  @Override
  public Promotion getById(Long promotionId) {
    throw new NotImplementedException();
  }

  @Override
  public Promotion getByName(String promotionName) {
    throw new NotImplementedException();
  }
}
