package cpd.utils.transformer;

import cpd.utils.helper.CsvCreator;
import cpd.utils.model.v10502.Promotion;
import cpd.utils.model.v10502.TCondition;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author bespalko
 * @since 04.12.2017
 */
@Component
public class CsvTransformer11 implements Transformer {


  @Override
  public List<String> serialize(Promotion promotion) {
    Promotion.GlobalData promoGlobalData = promotion.getGlobalData();
    List<String> csv = new ArrayList<>();
    String promoLine = CsvCreator
      .createLine(promoGlobalData.getID(), promoGlobalData.getEffectiveDate(), promoGlobalData.getExpiryDate(),
                  promoGlobalData.getDescription());
    //TODO: разобраться, как вытащить PLU, MRCHR Group, Rebate, Conditions, desc, Articles
    List<TCondition> tConditions = promotion.getCondition();
    if (!tConditions.isEmpty()) {
      for (TCondition condition : tConditions) {
        TCondition.GlobalData conditionGlobalData = condition.getGlobalData();
        String condLine = CsvCreator
          .createLine(conditionGlobalData.getDescription(), condition.getRule().getSimpleRebate());
        condLine = CsvCreator.merge(promoLine, condLine);
        csv.add(condLine);
      }
    } else {
      csv.add(promoLine);
    }
    return csv;
  }

  @Override
  public List<List<String>> serialize(List<Promotion> promotions) {
    List<List<String>> listCsv = new ArrayList<>();
    for (Promotion promotion : promotions) {
      //добавляем header
      List<String> csv = new ArrayList<>();
      String header = CsvCreator.createLine((Object[]) csvHeader.values());
      csv.add(header);
      csv.addAll(serialize(promotion));
      listCsv.add(csv);
    }
    return listCsv;
  }

  @Override
  public Promotion deserialize(String text) {
    throw new NotImplementedException();
  }

  @Override
  public List<Promotion> deserialize(List<String> text) {
    throw new NotImplementedException();
  }

  private enum csvHeader {
    ID, FROM_DATE, TO_DATE, DESCRIPTION, ARTICLE, REBATE;
  }
}
