package org.example.documentservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.example.documentservice.configuration.DocumentProperties;
import org.example.documentservice.exception.DocumentFileNotFoundException;
import org.example.documentservice.exception.DocumentNotFoundException;
import org.example.documentservice.exception.TemplateFileNotFoundException;
import org.example.documentservice.exception.UnknownTemplateException;
import org.example.documentservice.model.Document;
import org.example.documentservice.model.Template;
import org.example.documentservice.repository.DocumentRepository;
import org.example.documentservice.repository.MinioRepository;
import org.example.documentservice.repository.TemplateRepository;
import org.example.documentservice.service.DocumentService;
import org.example.documentservice.utils.impl.DOCXGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
  private static final String PATH_FORMAT = "%s.docx";

  private final DocumentProperties documentProperties;
  private final TemplateRepository templateRepository;
  private final DocumentRepository documentRepository;
  private final MinioRepository minioRepository;
  private final DOCXGenerator docxGenerator;
  private final ZoneId zoneId;

  @Override
  @Transactional(
      propagation = Propagation.REQUIRES_NEW) // при ошибке во внешней транзакции не откатится запись о залитом файле
  public Document generate(String templateName, Map<String, Object> data) {
    try {
      Template template = templateRepository.findById(templateName)
          .orElseThrow(UnknownTemplateException::new);
      try (InputStream templateFile = minioRepository.download(documentProperties.templateBucket(), template.getPath())
          .orElseThrow(TemplateFileNotFoundException::new)) {
        XWPFDocument content = new XWPFDocument(templateFile);
        docxGenerator.fillDocument(content, data);
        UUID id = UUID.randomUUID();
        String path = String.format(PATH_FORMAT, id);
        Document document = new Document(id, template, path, LocalDate.now(zoneId));
        documentRepository.save(document);
        minioRepository.upload(documentProperties.documentBucket(), path, content.getDocument().newInputStream());
        return document;
      }
      catch (IOException exception) {
        throw new RuntimeException(exception);
      }
    }
    catch (RuntimeException exception) {
      log.error("Ошибка при генерации документа", exception);
      throw exception;
    }
  }

  @Override
  @Transactional
  public byte[] download(UUID id) {
    try {
      Document document = documentRepository.findById(id)
          .orElseThrow(DocumentNotFoundException::new);
      String path = document.getPath();
      try (InputStream file = minioRepository.download(documentProperties.documentBucket(), path)
          .orElseThrow(DocumentFileNotFoundException::new)) {
        return file.readAllBytes();
      }
      catch (IOException exception) {
        throw new RuntimeException(exception);
      }
    }
    catch (RuntimeException exception) {
      log.error("Ошибка при загрузке документа", exception);
      throw exception;
    }
  }
}
