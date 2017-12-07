package cpd.utils.transformer;

import cpd.utils.model.v10502.Promotion;
import java.util.List;

/**
 * Transform text to Object and vice versa
 */
public interface Transformer {

  /**
   * promotion -> text
   * @param promotion
   * @return text of one promotion
   */
  String serialize(Promotion promotion) throws Exception;

  /**
   * promotion -> text
   * @param promotions
   * @return text of all promotions
   */
  List<String> serialize(List<Promotion> promotions) throws Exception;

  /**
   * text -> promotion
   * @param text
   * @return one promotion from text
   */
  Promotion deserialize(String text) throws Exception;

  /**
   * text -> promotion
   * @param text
   * @return all promotions from text
   */
  List<Promotion> deserialize(List<String> text) throws Exception;
}
