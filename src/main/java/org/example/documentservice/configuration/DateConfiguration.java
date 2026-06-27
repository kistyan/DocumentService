package org.example.documentservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;

@Configuration
public class DateConfiguration {
  @Bean
  public ZoneId zoneId() {
    return ZoneId.of("Europe/Moscow");
  }
}
