package org.example.documentservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.documentservice.configuration.DocumentProperties;
import org.example.documentservice.exception.TemplateFileNotFoundException;
import org.example.documentservice.exception.UnknownTemplateException;
import org.example.documentservice.model.Template;
import org.example.documentservice.repository.MinioRepository;
import org.example.documentservice.repository.TemplateRepository;
import org.example.documentservice.service.DocumentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
  private final DocumentProperties documentProperties;
  private final TemplateRepository templateRepository;
  private final MinioRepository minioRepository;

  @Override
  @Transactional
  public UUID generate(String templateName, HashMap<String, Object> data) {
    Template template = templateRepository.findById(templateName)
        .orElseThrow(UnknownTemplateException::new);
    try (InputStream templateFile = minioRepository.download(documentProperties.templateBucket(), template.getPath())
        .orElseThrow(TemplateFileNotFoundException::new)) {
      System.out.println(Arrays.toString(templateFile.readAllBytes()));
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    return UUID.randomUUID();
  }

  @Override
  public byte[] download(UUID id) {
    return new byte[0];
  }
}
