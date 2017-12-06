package cpd.utils.transformer;

import static cpd.utils.transformer.CsvHeader.COUPON;
import static cpd.utils.transformer.CsvHeader.CUSTOMER_GROUP;
import static cpd.utils.transformer.CsvHeader.DESCR;
import static cpd.utils.transformer.CsvHeader.ELIGIBILITY;
import static cpd.utils.transformer.CsvHeader.FROM;
import static cpd.utils.transformer.CsvHeader.ID;
import static cpd.utils.transformer.CsvHeader.MERCH_GROUP;
import static cpd.utils.transformer.CsvHeader.PLU;
import static cpd.utils.transformer.CsvHeader.REBATE;
import static cpd.utils.transformer.CsvHeader.TO;
import static cpd.utils.transformer.CsvHeader.TYPE;

import cpd.utils.helper.CsvCreator;
import cpd.utils.model.v10502.Promotion;
import cpd.utils.model.v10502.TCondition;
import cpd.utils.model.v10502.TEligibility;
import cpd.utils.model.v10502.TItemIDList;
import cpd.utils.model.v10502.TRule;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author bespalko
 * @since 04.12.2017
 */
@Slf4j
@Component
public class CsvTransformer11 implements Transformer {
  private List<Map<CsvHeader, String>> listOfLines;

  @Override
  public String serialize(Promotion promotion) {
    Promotion.GlobalData promoGlobalData = promotion.getGlobalData();
    Map<CsvHeader, String> promoLine = getCsvPromoLine(promoGlobalData);
    List<TCondition> tConditions = promotion.getCondition();
    if (tConditions == null || tConditions.isEmpty()) {
      return CsvCreator.createLine(promoLine.values().toArray()) + '\n';
    }
    listOfLines = new ArrayList<>();
    for (TCondition condition : tConditions) {
      TRule tRule = condition.getRule();
      Map<CsvHeader, String> currLine = new LinkedHashMap<>(promoLine);
      TYPE_RULE typeRule;
      try {
        typeRule = TYPE_RULE.valueOf(tRule.getInfo().getType());
      } catch (IllegalArgumentException e) {
        log.error("Unexpected TYPE_RULE: " + tRule.getInfo().getType() + " in promo: " + currLine);
        listOfLines.add(currLine);
        continue;
      }
      currLine.put(TYPE, typeRule.toString());
      switch (typeRule) {
      case GET3PAY2:
        getLineGet3Pay2(tRule, currLine);
        break;
      case SIMPLE:
        getLineSimple(tRule, currLine);
        break;
      case COUPON:
        getLineCoupon(tRule, currLine);
        break;
      case MIX_AND_MATCH:
        getLineMixAndMatch(tRule, currLine);
        break;
      case MERCHANDISE_HIERARCHY_GROUP:
        getLineMerchandise_Hierarchy_Group(tRule, currLine);
        break;
      case NO_REBATE:
        getLineNoRebate(tRule, currLine);
        break;
      default:
        log.error("Unexpected TYPE_RULE: " + typeRule + " in promo: " + currLine);
      }
    }
    String csv = listOfMapsToText(listOfLines);
    listOfLines = null;
    return csv;
  }

  private void getLineNoRebate(TRule tRule, Map<CsvHeader, String> currLine) {
    listOfLines.add(currLine);
  }

  private Map<CsvHeader, String> getCsvPromoLine(Promotion.GlobalData data) {
    Map<CsvHeader, String> promoLine = new LinkedHashMap<>(CsvHeader.values().length);
    promoLine.put(ID, data.getID());
    promoLine.put(FROM, String.valueOf(data.getEffectiveDate() == null ? "" : data.getEffectiveDate()));
    promoLine.put(TO, String.valueOf(data.getExpiryDate() == null ? "" : data.getExpiryDate()));
    promoLine.put(DESCR, data.getDescription());
    promoLine.put(TYPE, "");
    promoLine.put(ELIGIBILITY, "");
    promoLine.put(PLU, "");
    promoLine.put(REBATE, "");
    promoLine.put(COUPON, "");
    promoLine.put(CUSTOMER_GROUP, "");
    promoLine.put(MERCH_GROUP, "");
    return Collections.unmodifiableMap(promoLine);
  }

  private String listOfMapsToText(List<Map<CsvHeader, String>> listOfLines) {
    assert listOfLines.size() != 0;
    StringBuilder sb = new StringBuilder();
    for (Map<CsvHeader, String> line : listOfLines) {
      sb.append(CsvCreator.createLine(line.values().toArray())).append('\n');
    }
    return sb.toString();
  }

  private void getLineMerchandise_Hierarchy_Group(TRule tRule, Map<CsvHeader, String> currLine) {
    listOfLines.add(currLine);
  }

  private void getLineMixAndMatch(TRule tRule, Map<CsvHeader, String> currLine) {
    listOfLines.add(currLine);
  }

  private void getLineCoupon(TRule tRule, Map<CsvHeader, String> currLine) {
    listOfLines.add(currLine);
  }

  private void getLineSimple(TRule tRule, Map<CsvHeader, String> currLine) {
    currLine.put(REBATE, tRule.getSimpleRebate().getValue());
    List<TEligibility> tEligibilities = tRule.getEligibility();
    if (tEligibilities == null || tEligibilities.isEmpty()) {
      listOfLines.add(currLine);
      return;
    }
    for (TEligibility tEligibility : tEligibilities) {
      getLineEligibility(tEligibility, new LinkedHashMap<>(currLine));
    }
  }

  private void getLineGet3Pay2(TRule tRule, Map<CsvHeader, String> currLine) {
    List<TEligibility> tEligibilities = tRule.getEligibility();
    if (tEligibilities == null || tEligibilities.isEmpty()) {
      listOfLines.add(currLine);
      return;
    }
    for (TEligibility tEligibility : tEligibilities) {
      getLineEligibility(tEligibility, new LinkedHashMap<>(currLine));
    }
  }

  private void getLineEligibility(TEligibility tEligibility, Map<CsvHeader, String> currLine) {
    TYPE_ELIGIBILITY typeEligibility;
    try {
      typeEligibility = TYPE_ELIGIBILITY.valueOf(tEligibility.getInfo().getType());
    } catch (IllegalArgumentException e) {
      log.error("Unexpected TYPE_ELIGIBILITY: " + tEligibility.getInfo().getType() + " in promo: " + currLine);
      listOfLines.add(currLine);
      return;
    }
    switch (typeEligibility) {
    case ITEM:
      String id = tEligibility.getItem().getID();
      if (!StringUtils.isEmpty(id)) {
        currLine.put(PLU, id);
        listOfLines.add(currLine);
      } else {
        TItemIDList idList = tEligibility.getItem().getIDList();
        if (idList != null) {
          List<String> IDs = idList.getID();
          for (String plu : IDs) {
            Map<CsvHeader, String> line = new LinkedHashMap<>(currLine);
            line.put(PLU, plu);
            listOfLines.add(line);
          }
        }
      }
      break;
    case CUSTOMER_GROUP:
      currLine.put(CUSTOMER_GROUP, tEligibility.getCustomerGroupID());
      listOfLines.add(currLine);
      break;
    case COUPON:
      currLine.put(COUPON, tEligibility.getCoupon().getID());
      listOfLines.add(currLine);
      break;
    case MERCHANDISE_HIERARCHY_GROUP:
      currLine.put(MERCH_GROUP, tEligibility.getMerchandiseHierarchyGroup().getID());
      listOfLines.add(currLine);
      break;
    case MARKET_BASKET:
      listOfLines.add(currLine);
      break;
    default:
      log.error("Unexpected TYPE_ELIGIBILITY: " + typeEligibility + " in promo: " + currLine);
    }
  }

  @Override
  public List<String> serialize(List<Promotion> promotions) {
    List<String> listCsv = new ArrayList<>();
    for (Promotion promotion : promotions) {
      //добавляем header
      String csv = CsvHeader.HEADER + serialize(promotion);
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

  enum TYPE_RULE {
    GET3PAY2, SIMPLE, COUPON, MERCHANDISE_HIERARCHY_GROUP, MIX_AND_MATCH, NO_REBATE;
  }

  enum TYPE_ELIGIBILITY {
    CUSTOMER_GROUP, ITEM, COUPON, MERCHANDISE_HIERARCHY_GROUP, MARKET_BASKET;
  }
}
