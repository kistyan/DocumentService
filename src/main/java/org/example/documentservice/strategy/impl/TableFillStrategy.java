package org.example.documentservice.strategy.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.example.documentservice.strategy.FillStrategy;
import org.example.documentservice.utils.TableFiller;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class TableFillStrategy implements FillStrategy {
  private final TableFiller tableFiller;

  @Override
  public void fill(IBodyElement element, Map<String, Object> data) {
    tableFiller.fill((XWPFTable) element, data);
  }

  @Override
  public BodyElementType getType() {
    return BodyElementType.TABLE;
  }
}
