package org.example.documentservice.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

@Configuration
@RequiredArgsConstructor
public class DocumentConfiguration {
  private final DocumentProperties documentProperties;

  @Bean
  public Pattern placeholderPattern() {
    return Pattern.compile(documentProperties.placeholderRegex());
  }
}
