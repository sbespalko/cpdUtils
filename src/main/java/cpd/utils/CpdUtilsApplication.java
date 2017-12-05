package cpd.utils;

import cpd.utils.controller.CsvExporter11;
import cpd.utils.controller.XmlExporter11;
import cpd.utils.model.FileSystemModel11;
import cpd.utils.model.Model;
import cpd.utils.transformer.CsvTransformer11;
import cpd.utils.transformer.Transformer;
import cpd.utils.transformer.XmlTransformer11;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CpdUtilsApplication {

  public static void main(String[] args) {
    SpringApplication.run(CpdUtilsApplication.class, args);
  }

  @Bean
  public Model model11() {
    return new FileSystemModel11(new XmlTransformer11());
  }

  @Bean
  public XmlExporter11 xmlExporter11() {
    return new XmlExporter11(model11(), new XmlTransformer11());
  }
  @Bean
  public CsvExporter11 csvExporter11() {
    return new CsvExporter11(model11(), new CsvTransformer11());
  }

}
