package org.example.documentservice.configuration;

import com.ibm.icu.text.RuleBasedNumberFormat;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Configuration
public class MoneyConfiguration {
  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public RuleBasedNumberFormat spellOutNumberFormat() {
    return new RuleBasedNumberFormat(
        Locale.forLanguageTag("ru"),
        RuleBasedNumberFormat.SPELLOUT
    );
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public DecimalFormat rubleNumberFormat() {
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    symbols.setGroupingSeparator(' ');
    return new DecimalFormat("#,###", symbols);
  }
}
