package cpd.utils.transformer;

import static cpd.utils.transformer.CsvHeader.COUPON;
import static cpd.utils.transformer.CsvHeader.CUSTOMER_GROUP;
import static cpd.utils.transformer.CsvHeader.DESC;
import static cpd.utils.transformer.CsvHeader.ELIGIBILITY;
import static cpd.utils.transformer.CsvHeader.FROM;
import static cpd.utils.transformer.CsvHeader.ID;
import static cpd.utils.transformer.CsvHeader.MERCH_GROUP;
import static cpd.utils.transformer.CsvHeader.PLU;
import static cpd.utils.transformer.CsvHeader.REBATE;
import static cpd.utils.transformer.CsvHeader.TO;
import static cpd.utils.transformer.CsvHeader.TO_BE_PAID_QUANTITY;
import static cpd.utils.transformer.CsvHeader.TYPE;

import cpd.utils.helper.CsvCreator;
import cpd.utils.model.v10502.Promotion;
import cpd.utils.model.v10502.TCondition;
import cpd.utils.model.v10502.TEligibility;
import cpd.utils.model.v10502.TItemIDList;
import cpd.utils.model.v10502.TMerchandiseHierarchyGroupIDList;
import cpd.utils.model.v10502.TRule;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author bespalko
 * @since 04.12.2017
 */
@Slf4j
public class CsvTransformer11 implements Transformer {
  private List<Map<CsvHeader, String>> listOfLines;

  @Override
  public String serialize(Promotion promotion) {
    listOfLines = new ArrayList<>();
    Map<CsvHeader, String> promoLine = getEmptyLine();
    getLinePromotion(promotion, promoLine);
    List<TCondition> tConditions = promotion.getCondition();
    if (tConditions == null || tConditions.isEmpty()) {
      listOfLines.add(promoLine);
      return listOfMapsToText(listOfLines);
    }
    for (TCondition condition : tConditions) {
      TRule tRule = condition.getRule();
      Map<CsvHeader, String> currLine = new LinkedHashMap<>(promoLine);
      if(tRule.getInfo() == null) {
        listOfLines.add(currLine);
        continue;
      }
      currLine.put(TYPE, tRule.getInfo().getType());
      try {
        TYPE_RULE typeRule = TYPE_RULE.valueOf(tRule.getInfo().getType());
        switch (typeRule) {
        case GET3PAY2:
          getLineGet3Pay2(tRule, currLine);
          break;
        case SIMPLE:
          getLineSimple(tRule, currLine);
          break;
        case MIX_AND_MATCH:
          getLineMixAndMatch(tRule, currLine);
          break;
        case NO_REBATE:
          getLineNoRebate(tRule, currLine);
          break;
        default:
          log.error("Unhandled TYPE_RULE: " + typeRule + " in promo: " + currLine);
        }
      } catch (IllegalArgumentException e) {
        log.error("Unexpected TYPE_RULE: " + tRule.getInfo().getType() + " in promo: " + currLine);
        listOfLines.add(currLine);
      }
    }
    return listOfMapsToText(listOfLines);
  }

  private Map<CsvHeader, String> getEmptyLine() {
    Map<CsvHeader, String> promoLine = new LinkedHashMap<>(CsvHeader.values().length);
    for (CsvHeader header : CsvHeader.values()) {
      promoLine.put(header, "");
    }
    return promoLine;
  }

  private void getLinePromotion(@NonNull Promotion promotion, Map<CsvHeader, String> currLine) {
    Promotion.GlobalData data = promotion.getGlobalData();
    if (data == null) {
      return;
    }
    currLine.put(ID, data.getID());
    if (data.getEffectiveDate() != null) {
      currLine.put(FROM, String.valueOf(data.getEffectiveDate()));
    }
    if (data.getEffectiveDate() != null) {
      currLine.put(TO, String.valueOf(data.getExpiryDate()));
    }
    currLine.put(DESC, data.getDescription());
  }

  private void getLineNoRebate(@NonNull TRule tRule, Map<CsvHeader, String> currLine) {
    fillEligibilitiesLines(tRule.getEligibility(), currLine);
  }

  private void getLineMixAndMatch(@NonNull TRule tRule, Map<CsvHeader, String> currLine) {
    TRule.MixAndMatch mixAndMatch = tRule.getMixAndMatch();
    if (mixAndMatch != null) {
      List<TRule.MixAndMatch.MatchingItem> matchingItems = mixAndMatch.getMatchingItem();
      for (TRule.MixAndMatch.MatchingItem item : matchingItems) {
        Map<CsvHeader, String> line = new LinkedHashMap<>(currLine);
        line.put(PLU, item.getItemID());
        line.put(REBATE, String.valueOf(item.getValue()));
        listOfLines.add(line);
      }
    }
    fillEligibilitiesLines(tRule.getEligibility(), currLine);
  }

  private void getLineSimple(@NonNull TRule tRule, Map<CsvHeader, String> currLine) {
    currLine.put(REBATE, tRule.getSimpleRebate().getValue());
    fillEligibilitiesLines(tRule.getEligibility(), currLine);
  }

  private void getLineGet3Pay2(@NonNull TRule tRule, Map<CsvHeader, String> currLine) {
    if (tRule.getToBePaidQuantity() != null) {
      currLine.put(TO_BE_PAID_QUANTITY, String.valueOf(tRule.getToBePaidQuantity()));
    }
    fillEligibilitiesLines(tRule.getEligibility(), currLine);
  }

  private void fillEligibilitiesLines(List<TEligibility> tEligibilities, Map<CsvHeader, String> currLine) {
    if (tEligibilities == null || tEligibilities.isEmpty()) {
      listOfLines.add(currLine);
      return;
    }
    for (TEligibility tEligibility : tEligibilities) {
      getLineEligibility(tEligibility, new LinkedHashMap<>(currLine));
    }
  }

  private void getLineEligibility(@NonNull TEligibility tEligibility, Map<CsvHeader, String> currLine) {
    currLine.put(ELIGIBILITY, tEligibility.getInfo().getType());
    try {
      TYPE_ELIGIBILITY typeEligibility = TYPE_ELIGIBILITY.valueOf(tEligibility.getInfo().getType());
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
            fillLinesByList(currLine, IDs, PLU);
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
        String merchId = tEligibility.getMerchandiseHierarchyGroup().getID();
        if (!StringUtils.isEmpty(merchId)) {
          currLine.put(MERCH_GROUP, merchId);
          listOfLines.add(currLine);
        } else {
          TMerchandiseHierarchyGroupIDList merchIdList = tEligibility.getMerchandiseHierarchyGroup().getIDList();
          if (merchIdList != null) {
            List<String> IDs = merchIdList.getID();
            fillLinesByList(currLine, IDs, MERCH_GROUP);
          }
        }
        break;
      case MARKET_BASKET:
        listOfLines.add(currLine);
        break;
      default:
        log.error("Unhandled TYPE_ELIGIBILITY: " + typeEligibility + " in promo: " + currLine);
      }
    } catch (IllegalArgumentException e) {
      log.error("Unexpected TYPE_ELIGIBILITY: " + tEligibility.getInfo().getType() + " in promo: " + currLine);
      listOfLines.add(currLine);
    }
  }

  private void fillLinesByList(Map<CsvHeader, String> currLine, List<String> values, CsvHeader type) {
    for (String item : values) {
      Map<CsvHeader, String> line = new LinkedHashMap<>(currLine);
      line.put(type, item);
      listOfLines.add(line);
    }
  }

  private String listOfMapsToText(List<Map<CsvHeader, String>> list) {
    assert list.size() != 0;
    StringBuilder sb = new StringBuilder();
    for (Map<CsvHeader, String> line : list) {
      sb.append(CsvCreator.createLine(line.values().toArray())).append('\n');
    }
    listOfLines = null;
    return sb.toString();
  }

  @Override
  public List<String> serialize(List<Promotion> promotions) {
    List<String> listCsv = new ArrayList<>();
    for (Promotion promotion : promotions) {
      //добавляем header
      String head = CsvHeader.HEADER;
      String body = serialize(promotion);
      if (!StringUtils.isEmpty(body)) {
        listCsv.add(head + body);
      }
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
    GET3PAY2, SIMPLE, MIX_AND_MATCH, NO_REBATE;
  }

  enum TYPE_ELIGIBILITY {
    CUSTOMER_GROUP, ITEM, COUPON, MERCHANDISE_HIERARCHY_GROUP, MARKET_BASKET;
  }
}
