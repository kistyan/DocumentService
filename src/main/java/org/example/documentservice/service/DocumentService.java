package org.example.documentservice.service;

import org.example.documentservice.model.Document;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public interface DocumentService {
  Document generate(String templateName, Map<String, Object> data);

  byte[] download(UUID id);
}
