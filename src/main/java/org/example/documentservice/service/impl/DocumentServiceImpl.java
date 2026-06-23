package org.example.documentservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.documentservice.configuration.DocumentProperties;
import org.example.documentservice.service.DocumentService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
  private final DocumentProperties documentProperties;

  @Override
  public UUID generate(String templateName, HashMap<String, Object> data) {
    System.out.println(documentProperties);
    return UUID.randomUUID();
  }

  @Override
  public byte[] download(UUID id) {
    return new byte[0];
  }
}
