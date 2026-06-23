package org.example.documentservice.controller;

import org.example.documentservice.dto.request.WorkAcceptanceActRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/document-service/work-acceptance-acts")
public interface WorkAcceptanceActController {
  @PostMapping
  ResponseEntity<UUID> generate(WorkAcceptanceActRequest request);
}
