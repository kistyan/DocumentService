package org.example.documentservice.utils.impl;

import com.ibm.icu.text.RuleBasedNumberFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class MoneyUtilsImplTest {
  @Mock
  private RuleBasedNumberFormat spellOutNumberFormat;

  @Mock
  private ObjectProvider<RuleBasedNumberFormat> spellOutNumberFormatProvider;

  @Mock
  private DecimalFormat rubleNumberFormat;

  @Mock
  private ObjectProvider<DecimalFormat> rubleNumberFormatProvider;

  private MoneyUtilsImpl moneyUtils;

  @BeforeEach
  void setUp() {
    when(spellOutNumberFormat.format(any(BigDecimal.class)))
        .thenReturn("пропись");
    when(spellOutNumberFormatProvider.getObject())
        .thenReturn(spellOutNumberFormat);

    when(rubleNumberFormat.format(any(BigDecimal.class)))
        .thenAnswer(invocation -> invocation.getArgument(0).toString());
    when(rubleNumberFormatProvider.getObject())
        .thenReturn(rubleNumberFormat);

    moneyUtils = new MoneyUtilsImpl(spellOutNumberFormatProvider, rubleNumberFormatProvider);
  }

  @ParameterizedTest
  @CsvSource({
      "1,1 (пропись) рубль 00 копеек",
      "2,2 (пропись) рубля 00 копеек",
      "5,5 (пропись) рублей 00 копеек",
      "11,11 (пропись) рублей 00 копеек",
      "12,12 (пропись) рублей 00 копеек",
      "15,15 (пропись) рублей 00 копеек",
      "21,21 (пропись) рубль 00 копеек",
      "22,22 (пропись) рубля 00 копеек",
      "25,25 (пропись) рублей 00 копеек",
      "31,31 (пропись) рубль 00 копеек",
      "32,32 (пропись) рубля 00 копеек",
      "35,35 (пропись) рублей 00 копеек",
      "1.00,1 (пропись) рубль 00 копеек",
      "1.10,1 (пропись) рубль 10 копеек",
      "1000,1000 (пропись) рублей 00 копеек",
      "10000000000000000000,10000000000000000000 (пропись) рублей 00 копеек"
  })
  void getCostText(BigDecimal cost, String costText) {
    assertEquals(
        costText,
        moneyUtils.getCostText(cost)
    );
  }
}