package org.example.documentservice.utils.impl;

import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.example.documentservice.exception.UnknownPlaceholderTypeException;
import org.example.documentservice.strategy.FillStrategy;
import org.example.documentservice.strategy.InsertStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class DOCXGenerator {
  private static final String TEXT_PLACEHOLDER_TYPE = "TEXT";

  private final Pattern placeholderPattern;
  private final Map<BodyElementType, FillStrategy> fillStrategies;
  private final Map<String, InsertStrategy> insertStrategies;

  public DOCXGenerator(
      Pattern placeholderPattern,
      List<FillStrategy> fillStrategies,
      List<InsertStrategy> insertStrategies
  ) {
    this.placeholderPattern = placeholderPattern;
    this.fillStrategies = fillStrategies.stream()
        .collect(Collectors.toMap(
            FillStrategy::getType,
            strategy -> strategy
        ));
    this.insertStrategies = insertStrategies.stream()
        .collect(Collectors.toMap(
            InsertStrategy::getType,
            strategy -> strategy
        ));
  }

  public void fillDocument(XWPFDocument document, Map<String, Object> data) {
    DocumentCursor cursor = new DocumentCursor(document);
    while (cursor.isAvailable()) {
      IBodyElement element = cursor.getElement();
      if (!insertInstead(cursor, data)) {
        FillStrategy fillStrategy = fillStrategies.get(element.getElementType());
        if (fillStrategy != null) {
          fillStrategy.fill(element, data);
        }
      }
      cursor.next();
    }
  }

  private boolean insertInstead(DocumentCursor cursor, Map<String, Object> data) {
    IBodyElement element = cursor.getElement();
    if (element.getElementType() != BodyElementType.PARAGRAPH) {
      return false;
    }
    XWPFParagraph paragraph = (XWPFParagraph) element;
    Matcher matcher = placeholderPattern.matcher(paragraph.getText());
    if (!matcher.matches()) {
      return false;
    }
    String type = matcher.group(1);
    if (type.equals(TEXT_PLACEHOLDER_TYPE)) {
      return false;
    }
    InsertStrategy insertStrategy = insertStrategies.get(type);
    if (insertStrategy == null) {
      throw new UnknownPlaceholderTypeException(type);
    }
    insertStrategy.insert(cursor, data, matcher.group(2));
    cursor.next();
    return true;
  }
}
