package cpd.utils.transformer;

import static java.util.stream.Collectors.toList;

import cpd.utils.model.v10502.Promotion;
import java.util.List;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author bespalko
 * @since 04.12.2017
 */
@Component
public class XmlTransformerCpd11 implements Transformer {

  @Override
  public Promotion transformFrom(String text) {
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
  public List<Promotion> transformFrom(List<String> text) {
    return text.stream().map(this::transformFrom).collect(toList());
  }

  @Override
  public List<String> transformTo(Promotion promotion) {
    throw new NotImplementedException();
  }

  @Override
  public List<List<String>> transformTo(List<Promotion> promotions) {
    throw new NotImplementedException();
  }

}
