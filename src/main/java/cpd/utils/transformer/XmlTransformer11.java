package cpd.utils.transformer;

import static java.util.stream.Collectors.toList;

import cpd.utils.model.v10502.Promotion;
import cpd.utils.model.v10502.PromotionRoot;
import java.io.StringReader;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author bespalko
 * @since 04.12.2017
 */
@Slf4j
public class XmlTransformer11 implements Transformer {

  @Override
  public Promotion deserialize(String text) throws JAXBException {
    if (StringUtils.isEmpty(text)) {
      return getEmptyPromotion(text, null);
    }
    //JAXB to convert XML into object;
    PromotionRoot root = null;
    JAXBContext jaxbContext = JAXBContext.newInstance(PromotionRoot.class);
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

    InputSource inputSource = new InputSource(new StringReader(text));
    final SAXParserFactory sax = SAXParserFactory.newInstance();
    sax.setNamespaceAware(false);
    final XMLReader reader;
    try {
      reader = sax.newSAXParser().getXMLReader();
    } catch (SAXException | ParserConfigurationException e) {
      throw new RuntimeException(e);
    }
    SAXSource saxSource = new SAXSource(reader, inputSource);
    root = (PromotionRoot) jaxbUnmarshaller.unmarshal(saxSource);
    if (root == null || root.getPromotion() == null) {
      return getEmptyPromotion(text, null);
    }
    return root.getPromotion();
  }

  private Promotion getEmptyPromotion(String text, Throwable exception) {
    log.error("Bad promotion: {}", exception);
    Promotion promo = new Promotion();
    Promotion.GlobalData data = new Promotion.GlobalData();
    data.setDescription("UNKNOWN");
    try {
      GregorianCalendar from = new GregorianCalendar();
      from.setTime(new Date(System.currentTimeMillis()));
      data.setEffectiveDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(from));
      GregorianCalendar to = new GregorianCalendar();
      to.setTime(new Date(System.currentTimeMillis()));
      data.setExpiryDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(to));
    } catch (DatatypeConfigurationException e) {
      log.error("Can't create XMLGregorianCalendar", e);
    }
    promo.setGlobalData(new Promotion.GlobalData());
    return promo;
  }

  @Override
  public List<Promotion> deserialize(List<String> text) {
    return text.stream().map(promoString -> {
      try {
        return deserialize(promoString);
      } catch (JAXBException e) {
        return getEmptyPromotion(promoString, e);
      }
    }).collect(toList());
  }

  @Override
  public String serialize(Promotion promotion) {
    throw new NotImplementedException();
  }

  @Override
  public List<String> serialize(List<Promotion> promotions) {
    throw new NotImplementedException();
  }

}
