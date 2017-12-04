package cpd.utils.model;

import cpd.utils.transformer.Transformer;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import cpd.utils.model.v10502.Promotion;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author bespalko
 * @since 04.12.2017
 */
@Repository
public class JdbcModelCpd11 implements Model {
  private static final String PROMO_TABLE = "PD_PROMOTION_OVERVIEW";
  private static final String PROMO_COLUMN = "UI_DATA_LAST_CHANGED";
  private static final String GET_All_SQL = "SELECT " + PROMO_COLUMN + " FROM " + PROMO_TABLE;

  private final JdbcTemplate jdbcTemplate;
  private final Transformer transformerCpd11;

  @Autowired
  public JdbcModelCpd11(JdbcTemplate jdbcTemplate, Transformer xmlTransformerCpd11) {
    this.jdbcTemplate = jdbcTemplate;
    this.transformerCpd11 = xmlTransformerCpd11;
  }

  @Override
  public List<Promotion> getAll() {
    List<String> stringsPromotion = jdbcTemplate.query(GET_All_SQL, (rs, rowNum) -> {
      Blob blob = rs.getBlob(PROMO_COLUMN);
      return new String(blob.getBytes(1, (int) blob.length()));
    });
    return transformerCpd11.transformFrom(stringsPromotion);
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
