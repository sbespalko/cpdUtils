package cpd.utils;

import cpd.utils.controller.CsvExporter11;
import cpd.utils.controller.XmlExporter11;
import cpd.utils.model.FileSystemModel11;
import cpd.utils.model.Model;
import cpd.utils.transformer.Transformer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CpdUtilsApplication {

  public static void main(String[] args) {
    SpringApplication.run(CpdUtilsApplication.class, args);
  }

  private Model model11;
  private Transformer csvTransformer11;
  private Transformer xmlTransformer11;

  public CpdUtilsApplication(Transformer csvTransformer11, Transformer xmlTransformer11) {
    this.model11 = model11();
    this.csvTransformer11 = csvTransformer11;
    this.xmlTransformer11 = xmlTransformer11;
  }

  @Bean
  public Model model11() {
    return new FileSystemModel11(xmlTransformer11);
  }

  @Bean
  public XmlExporter11 xmlExporter11() {
    return new XmlExporter11(model11, xmlTransformer11);
  }
  @Bean
  public CsvExporter11 csvExporter11() {
    return new CsvExporter11(model11, csvTransformer11);
  }

}
