package org.example.documentservice.strategy.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.example.documentservice.strategy.FillStrategy;
import org.example.documentservice.utils.ParagraphFiller;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ParagraphFillStrategy implements FillStrategy {
  private final ParagraphFiller paragraphFiller;

  @Override
  public void fill(IBodyElement element, Map<String, Object> data) {
    paragraphFiller.fill((XWPFParagraph) element, data);
  }

  @Override
  public BodyElementType getType() {
    return BodyElementType.PARAGRAPH;
  }
}
