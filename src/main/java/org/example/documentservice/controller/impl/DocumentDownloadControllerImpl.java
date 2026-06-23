package org.example.documentservice.controller.impl;

import lombok.RequiredArgsConstructor;
import org.example.documentservice.controller.DocumentDownloadController;
import org.example.documentservice.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/document-service/documents/download")
@RequiredArgsConstructor
public class DocumentDownloadControllerImpl implements DocumentDownloadController {
  private final DocumentService documentService;

  @Override
  @GetMapping("/{documentId}")
  public ResponseEntity<byte[]> download(@PathVariable UUID documentId) {
    return ResponseEntity.ok(documentService.download(documentId));
  }
}
