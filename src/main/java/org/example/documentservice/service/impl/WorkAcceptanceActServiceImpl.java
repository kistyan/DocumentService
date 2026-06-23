package org.example.documentservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.documentservice.dto.request.WorkAcceptanceActRequest;
import org.example.documentservice.service.DocumentService;
import org.example.documentservice.service.WorkAcceptanceActService;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkAcceptanceActServiceImpl implements WorkAcceptanceActService {
  private static final String TEMPLATE_NAME = "WORK_ACCEPTANCE_ACT";

  private final DocumentService documentService;

  @Override
  public @Nullable UUID generate(WorkAcceptanceActRequest request) {
    return documentService.generate(TEMPLATE_NAME, new HashMap<>());
  }
}
