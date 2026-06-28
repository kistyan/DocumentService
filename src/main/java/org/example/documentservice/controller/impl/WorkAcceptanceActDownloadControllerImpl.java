package org.example.documentservice.controller.impl;

import lombok.RequiredArgsConstructor;
import org.example.documentservice.controller.WorkAcceptanceActDownloadController;
import org.example.documentservice.dto.response.WorkAcceptanceActFileResponse;
import org.example.documentservice.service.WorkAcceptanceActService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/document-service/work-acceptance-acts/download")
@RequiredArgsConstructor
public class WorkAcceptanceActDownloadControllerImpl implements WorkAcceptanceActDownloadController {
  private final WorkAcceptanceActService workAcceptanceActService;

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<byte[]> download(@PathVariable UUID id) {
    WorkAcceptanceActFileResponse fileResponse = workAcceptanceActService.download(id);
    ContentDisposition disposition = ContentDisposition.attachment()
        .filename(fileResponse.name(), java.nio.charset.StandardCharsets.UTF_8)
        .build();
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
        .contentType(MediaType
            .parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
        .body(fileResponse.content());
  }
}
