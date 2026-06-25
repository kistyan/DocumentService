package org.example.documentservice.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.documentservice.controller.WorkAcceptanceActController;
import org.example.documentservice.dto.request.WorkAcceptanceActRequest;
import org.example.documentservice.service.WorkAcceptanceActService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/document-service/work-acceptance-acts")
@RequiredArgsConstructor
public class WorkAcceptanceActControllerImpl  implements WorkAcceptanceActController {
  private final WorkAcceptanceActService workAcceptanceActService;

  @Override
  @PostMapping
  public ResponseEntity<UUID> generate(@RequestBody @Valid WorkAcceptanceActRequest request) {
    System.out.println(request);
    return ResponseEntity.ok(workAcceptanceActService.generate(request));
  }
}
