package org.example.documentservice.configuration;

import com.github.petrovich4j.Petrovich;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PetrovichConfiguration {
  @Bean
  public Petrovich petrovich() {
    return new Petrovich();
  }
}
