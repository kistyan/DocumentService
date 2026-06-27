package org.example.documentservice.strategy;

import org.example.documentservice.utils.impl.DocumentCursor;

import java.util.Map;

public interface InsertStrategy {
  void insert(DocumentCursor cursor, Map<String, Object> data, String key);

  String getType();
}
