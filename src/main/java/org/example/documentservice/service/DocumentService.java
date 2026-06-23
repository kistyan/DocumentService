package org.example.documentservice.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
public interface DocumentService {
  UUID generate(String templateName, HashMap<String, Object> data);

  byte[] download(UUID id);
}
