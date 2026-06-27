package org.example.documentservice.utils;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.util.Map;

public interface ParagraphFiller {
  void fill(XWPFParagraph paragraph, Map<String, Object> data);
}
