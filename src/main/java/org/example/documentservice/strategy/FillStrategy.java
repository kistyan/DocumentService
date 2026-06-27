package org.example.documentservice.strategy;

import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;

import java.util.Map;

public interface FillStrategy {
  void fill(IBodyElement element, Map<String, Object> data);

  BodyElementType getType();
}
