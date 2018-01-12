package cpd.utils;

import cpd.utils.controller.CsvExporter11;
import cpd.utils.controller.XmlExporter11;
import cpd.utils.model.FileSystemModel11;
import cpd.utils.model.JdbcModel11;
import cpd.utils.model.Model;
import cpd.utils.transformer.CsvTransformer11;
import cpd.utils.transformer.XmlTransformer11;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class CpdUtilsApplication {
  @Value("${connect.type}") private String connectType;

  public static void main(String[] args) {
    SpringApplication.run(CpdUtilsApplication.class, args);
  }

  @Bean
  public XmlExporter11 xmlExporter11() {
    return new XmlExporter11(model11(), new XmlTransformer11());
  }

  @Bean
  public CsvExporter11 csvExporter11() {
    return new CsvExporter11(model11(), new CsvTransformer11());
  }

  @Bean
  public Model model11() {
    try {
      ConnectType TYPE = ConnectType.valueOf(connectType.toUpperCase());
      switch (TYPE) {
      case FILE:
        return new FileSystemModel11(new XmlTransformer11());
      case JDBC:
        return new JdbcModel11(new XmlTransformer11());
      default:
        log.error("Unhandled ConnectType: {}", TYPE);
        System.exit(1);
      }
    } catch (IllegalArgumentException e) {
      log.error("Unexpected ConnectType: {} in application.properties/connect.type", connectType);
      System.exit(1);
    }
    return null;
  }
}
