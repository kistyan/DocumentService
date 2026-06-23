package org.example.documentservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/document-service/documents/download")
public interface DocumentDownloadController {
  @GetMapping("/{documentId}")
  ResponseEntity<byte[]> download(@PathVariable UUID documentId);
}
