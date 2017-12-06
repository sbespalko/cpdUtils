package cpd.utils.model.v10502;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author bespalko
 * @since 06.12.2017
 */
@XmlRootElement(name = "Root")
public class PromotionRoot {
  private Promotion promotion;

  public Promotion getPromotion() {
    return promotion;
  }

  @XmlElement(name = "Promotion")
  public void setPromotion(Promotion promotion) {
    this.promotion = promotion;
  }
}
