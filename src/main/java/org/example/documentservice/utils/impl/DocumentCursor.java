package org.example.documentservice.utils.impl;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.util.List;

public class DocumentCursor {
  @Getter
  private final XWPFDocument document;
  private final List<IBodyElement> elements;
  @Setter
  @Getter
  private int index;

  public DocumentCursor(XWPFDocument document) {
    this.document = document;
    elements = document.getBodyElements();
    index = 0;
  }

  public void next() {
    index++;
  }

  public void previous() {
    index--;
  }

  public boolean isAvailable() {
    return index < elements.size();
  }

  public IBodyElement getElement() {
    return elements.get(index);
  }
}
