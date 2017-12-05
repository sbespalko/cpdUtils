package cpd.utils;

import cpd.utils.model.FileSystemModel11;
import cpd.utils.model.Model;
import cpd.utils.transformer.Transformer;
import cpd.utils.transformer.XmlTransformer11;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CpdUtilsApplication {

  public static void main(String[] args) {
    SpringApplication.run(CpdUtilsApplication.class, args);
  }

  @Bean
  public Model model() {
    Transformer transformer = new XmlTransformer11();
    return new FileSystemModel11(transformer);
  }
}
