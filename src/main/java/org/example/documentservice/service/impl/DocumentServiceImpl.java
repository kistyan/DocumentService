package org.example.documentservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.example.documentservice.configuration.DocumentProperties;
import org.example.documentservice.exception.DocumentFileNotFoundException;
import org.example.documentservice.exception.DocumentNotFoundException;
import org.example.documentservice.exception.DocumentReadException;
import org.example.documentservice.exception.DocumentWriteException;
import org.example.documentservice.exception.TemplateFileNotFoundException;
import org.example.documentservice.exception.UnknownTemplateException;
import org.example.documentservice.model.Document;
import org.example.documentservice.model.Template;
import org.example.documentservice.repository.DocumentRepository;
import org.example.documentservice.service.MinioService;
import org.example.documentservice.repository.TemplateRepository;
import org.example.documentservice.service.DocumentService;
import org.example.documentservice.utils.impl.DOCXGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
  private final MinioService minioService;
  private final DOCXGenerator docxGenerator;
  private final ZoneId zoneId;

  @Override
  @Transactional(
      propagation = Propagation.REQUIRES_NEW) // при ошибке во внешней транзакции не откатится запись о залитом файле
  public Document generate(String templateName, Map<String, Object> data) {
    Template template = templateRepository.findById(templateName)
        .orElseThrow(UnknownTemplateException::new);
    try (InputStream templateFile = minioService.download(documentProperties.templateBucket(), template.getPath())
        .orElseThrow(TemplateFileNotFoundException::new);
         XWPFDocument content = new XWPFDocument(templateFile)) {
      docxGenerator.fillDocument(content, data);
      UUID id = UUID.randomUUID();
      String path = String.format(PATH_FORMAT, id);
      Document document = Document.builder()
          .id(id)
          .template(template)
          .path(path)
          .creationDate(LocalDate.now(zoneId))
          .build();
      documentRepository.save(document);
      minioService.upload(documentProperties.documentBucket(), path, getDocumentStream(content));
      return document;
    }
    catch (IOException exception) {
      log.error("Ошибка записи документа", exception);
      throw new DocumentWriteException();
    }
  }

  private InputStream getDocumentStream(XWPFDocument document) throws IOException {
    try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
      document.write(output);
      return new ByteArrayInputStream(output.toByteArray());
    }
  }

  @Override
  @Transactional
  public byte[] download(UUID id) {
    Document document = documentRepository.findById(id)
        .orElseThrow(DocumentNotFoundException::new);
    String path = document.getPath();
    try (InputStream file = minioService.download(documentProperties.documentBucket(), path)
        .orElseThrow(DocumentFileNotFoundException::new)) {
      return file.readAllBytes();
    }
    catch (IOException exception) {
      log.error("Ошибка чтения документа", exception);
      throw new DocumentReadException();
    }
  }
}
