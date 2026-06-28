package org.example.documentservice.controller;

import jakarta.validation.Valid;
import org.example.documentservice.dto.request.WorkAcceptanceActRequest;
import org.example.documentservice.dto.response.WorkAcceptanceActResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/document-service/work-acceptance-acts")
public interface WorkAcceptanceActController {
  @PostMapping
  ResponseEntity<WorkAcceptanceActResponse> generate(@RequestBody @Valid WorkAcceptanceActRequest request);
}
