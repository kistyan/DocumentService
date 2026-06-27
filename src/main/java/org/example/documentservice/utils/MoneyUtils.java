package org.example.documentservice.utils;

import java.math.BigDecimal;

public interface MoneyUtils {
  String getCostText(BigDecimal cost) throws IllegalArgumentException;
}
