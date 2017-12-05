package cpd.utils.transformer;

import cpd.utils.model.v10502.Promotion;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author bespalko
 * @since 04.12.2017
 */
@Component
public class XmlTransformer11 implements Transformer {

  @Override
  public Promotion deserialize(String text) {
    //JAXB to convert XML into object;
    //stub
    Promotion promotion = new Promotion();
    Promotion.GlobalData data = new Promotion.GlobalData();
    data.setID("105");
    data.setDescription("desc");
    promotion.setGlobalData(data);
    return promotion;
  }

  @Override
  public List<Promotion> deserialize(List<String> text) {
    return text.stream().map(this::deserialize).collect(toList());
  }

  @Override
  public List<String> serialize(Promotion promotion) {
    throw new NotImplementedException();
  }

  @Override
  public List<List<String>> serialize(List<Promotion> promotions) {
    throw new NotImplementedException();
  }

}
