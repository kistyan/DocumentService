package org.example.documentservice.service.impl;

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
import org.example.documentservice.repository.TemplateRepository;
import org.example.documentservice.service.MinioService;
import org.example.documentservice.utils.impl.DOCXGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {
  private static final Template TEMPLATE = Template.builder()
      .name("template")
      .path("template.docx")
      .build();
  private static final Map<String, Object> DATA = Map.of();
  private static final DocumentProperties DOCUMENT_PROPERTIES = DocumentProperties.builder()
      .templateBucket("templates")
      .documentBucket("documents")
      .build();
  private static final ZoneId ZONE_ID = ZoneId.of("Europe/Moscow");
  private static final Document DOCUMENT = Document.builder()
      .id(UUID.fromString("588f8e09-6a3d-4454-97b1-4f3f6d2c71be"))
      .template(TEMPLATE)
      .path("file.docx")
      .creationDate(LocalDate.EPOCH)
      .build();

  @Mock
  private TemplateRepository templateRepository;

  @Mock
  private DocumentRepository documentRepository;

  @Mock
  private MinioService minioService;

  @Mock
  private DOCXGenerator docxGenerator;

  private DocumentServiceImpl documentServiceImpl;

  @BeforeEach
  void setUp() {
    documentServiceImpl = new DocumentServiceImpl(
        DOCUMENT_PROPERTIES,
        templateRepository,
        documentRepository,
        minioService,
        docxGenerator,
        ZONE_ID
    );
  }

  @Test
  void generate_shouldSaveDocumentAndUploadFile() throws IOException {
    when(templateRepository.findById(TEMPLATE.getName()))
        .thenReturn(Optional.of(TEMPLATE));

    when(minioService.download(DOCUMENT_PROPERTIES.templateBucket(), TEMPLATE.getPath()))
        .thenReturn(Optional.of(emptyDocx()));

    Document document = documentServiceImpl.generate(TEMPLATE.getName(), DATA);

    assertNotNull(document);
    assertEquals(TEMPLATE, document.getTemplate());

    verify(docxGenerator).fillDocument(any(XWPFDocument.class), eq(DATA));
    verify(minioService).upload(eq(DOCUMENT_PROPERTIES.documentBucket()), anyString(), any(InputStream.class));
    verify(documentRepository).save(any(Document.class));
  }

  private InputStream emptyDocx() throws IOException {
    XWPFDocument document = new XWPFDocument();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    document.write(out);
    document.close();

    return new ByteArrayInputStream(out.toByteArray());
  }

  @Test
  void generate_shouldThrowUnknownTemplateException() {
    when(templateRepository.findById(TEMPLATE.getName()))
        .thenReturn(Optional.empty());

    assertThrows(UnknownTemplateException.class,
        () -> documentServiceImpl.generate(TEMPLATE.getName(), DATA));

    verifyNoInteractions(minioService);
    verifyNoInteractions(documentRepository);
  }

  @Test
  void generate_shouldThrowTemplateFileNotFoundException() {
    when(templateRepository.findById(TEMPLATE.getName()))
        .thenReturn(Optional.of(TEMPLATE));

    when(minioService.download(any(), any()))
        .thenReturn(Optional.empty());

    assertThrows(TemplateFileNotFoundException.class,
        () -> documentServiceImpl.generate(TEMPLATE.getName(), DATA));

    verifyNoInteractions(documentRepository);
  }

  @Test
  void generate_shouldThrowDocumentWriteException_whenIOException() {
    InputStream brokenStream = brokenStream();

    when(templateRepository.findById(TEMPLATE.getName()))
        .thenReturn(Optional.of(TEMPLATE));

    when(minioService.download(any(), any()))
        .thenReturn(Optional.of(brokenStream));

    assertThrows(DocumentWriteException.class,
        () -> documentServiceImpl.generate(TEMPLATE.getName(), DATA));

    verifyNoInteractions(documentRepository);
  }

  InputStream brokenStream() {
    return new InputStream() {
      @Override
      public int read() throws IOException {
        throw new IOException();
      }
    };
  }

  @Test
  void download_shouldReturnBytes() {
    byte[] content = "test".getBytes();

    when(documentRepository.findById(DOCUMENT.getId()))
        .thenReturn(Optional.of(DOCUMENT));

    when(minioService.download(DOCUMENT_PROPERTIES.documentBucket(), DOCUMENT.getPath()))
        .thenReturn(Optional.of(new ByteArrayInputStream(content)));

    assertArrayEquals(content, documentServiceImpl.download(DOCUMENT.getId()));
  }

  @Test
  void download_shouldThrowDocumentNotFoundException() {
    when(documentRepository.findById(DOCUMENT.getId()))
        .thenReturn(Optional.empty());

    assertThrows(DocumentNotFoundException.class,
        () -> documentServiceImpl.download(DOCUMENT.getId())
    );
  }

  @Test
  void download_shouldThrowDocumentFileNotFoundException() {
    when(documentRepository.findById(DOCUMENT.getId()))
        .thenReturn(Optional.of(DOCUMENT));

    when(minioService.download(any(), any()))
        .thenReturn(Optional.empty());

    assertThrows(DocumentFileNotFoundException.class,
        () -> documentServiceImpl.download(DOCUMENT.getId()));
  }

  @Test
  void download_shouldThrowDocumentReadException() {
    InputStream brokenStream = brokenStream();

    when(documentRepository.findById(DOCUMENT.getId()))
        .thenReturn(Optional.of(DOCUMENT));

    when(minioService.download(any(), any()))
        .thenReturn(Optional.of(brokenStream));

    assertThrows(DocumentReadException.class,
        () -> documentServiceImpl.download(DOCUMENT.getId()));
  }
}