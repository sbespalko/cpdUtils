package cpd.utils.model.v10502;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author bespalko
 * @since 06.12.2017
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "T_Info", propOrder = { "description", "type" })
public class TInfo {
  @XmlElement(name = "Description") protected String description;
  @XmlElement(name = "Type") protected String type;

  public String getDescription() {
    return description;
  }

  public String getType() {
    return type;
  }

}
