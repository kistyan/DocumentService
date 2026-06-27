package org.example.documentservice.utils;

import org.apache.poi.xwpf.usermodel.XWPFTable;

import java.util.Map;

public interface TableFiller {
  void fill(XWPFTable table, Map<String, Object> data);
}
