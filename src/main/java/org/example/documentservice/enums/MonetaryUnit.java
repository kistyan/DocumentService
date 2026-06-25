package org.example.documentservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
public enum MonetaryUnit {
  RUBLE("рубль", "рубля", "рублей"),
  KOPECK("копейка", "копейки", "копеек");

  private final String oneForm, fewForm, manyForm;

  public String pluralize(BigDecimal count) {
    count = count.abs();
    int lastTwoDigits = count.remainder(BigDecimal.valueOf(100)).intValue(),
        lastDigit = lastTwoDigits % 10;

    if (11 <= lastTwoDigits && lastTwoDigits <= 19)
      return manyForm;

    return switch (lastDigit) {
      case 1 ->
          oneForm;
      case 2, 3, 4 ->
          fewForm;
      default ->
          manyForm;
    };
  }
}
