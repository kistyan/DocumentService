package org.example.documentservice.strategy.impl;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.xmlbeans.XmlCursor;
import org.example.documentservice.strategy.InsertStrategy;
import org.example.documentservice.utils.impl.DocumentCursor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ListInsertStrategy implements InsertStrategy {
  private static final String LIST_PLACEHOLDER_TYPE = "LIST";

  @Override
  public void insert(DocumentCursor cursor, Map<String, Object> data, String key) {
    XWPFParagraph template = (XWPFParagraph) cursor.getElement();

    keepOnlyFirstRun(template);
    replaceTemplateByList(cursor, (List<?>) data.get(key));
  }

  private void keepOnlyFirstRun(XWPFParagraph paragraph) {
    for (int i = paragraph.getRuns().size() - 1; i > 0; i--) {
      paragraph.removeRun(i);
    }
  }

  private void replaceTemplateByList(DocumentCursor cursor, List<?> values) {
    XWPFDocument document = cursor.getDocument();
    XWPFParagraph template = (XWPFParagraph) cursor.getElement();

    XmlCursor insertPosition = template.getCTP().newCursor();
    insertPosition.toNextSibling();

    for (Object value : values) {
      XWPFParagraph inserted = cloneParagraph(document, template, insertPosition, value.toString());

      insertPosition = inserted.getCTP().newCursor();
      insertPosition.toNextSibling();
    }

    document.removeBodyElement(cursor.getIndex());
    cursor.setIndex(cursor.getIndex() + values.size() - 1);
  }

  private XWPFParagraph cloneParagraph(
      XWPFDocument document,
      XWPFParagraph template,
      XmlCursor insertPosition,
      String value
  ) {
    template.getRuns().get(0).setText(value, 0);

    XWPFParagraph paragraph = document.insertNewParagraph(insertPosition);
    paragraph.getCTP().set(template.getCTP().copy());

    return paragraph;
  }

  @Override
  public String getType() {
    return LIST_PLACEHOLDER_TYPE;
  }
}
