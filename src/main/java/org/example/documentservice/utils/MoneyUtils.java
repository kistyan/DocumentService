package org.example.documentservice.utils;

import com.ibm.icu.text.RuleBasedNumberFormat;
import lombok.RequiredArgsConstructor;
import org.example.documentservice.enums.MonetaryUnit;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

@Component
@RequiredArgsConstructor
public class MoneyUtils {
  // Форматы не потокобезопасны, для них используются Prototype бины
  private final ObjectProvider<RuleBasedNumberFormat> spellOutNumberFormatProvider;
  private final ObjectProvider<DecimalFormat> decimalFormatProvider;

  public String getCostText(BigDecimal cost) throws IllegalArgumentException {
    BigDecimal rubleCount = cost.setScale(0, RoundingMode.DOWN),
        kopeckCount = cost.remainder(BigDecimal.ONE).movePointRight(2);

    return String.format(
        "%s (%s) %s %02.0f %s",
        decimalFormatProvider.getObject().format(rubleCount),
        spellOutNumberFormatProvider.getObject().format(rubleCount),
        MonetaryUnit.RUBLE.pluralize(rubleCount),
        kopeckCount,
        MonetaryUnit.KOPECK.pluralize(kopeckCount)
    );
  }
}
