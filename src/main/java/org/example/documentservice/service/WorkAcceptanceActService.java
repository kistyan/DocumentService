package org.example.documentservice.service;

import org.example.documentservice.dto.request.WorkAcceptanceActRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface WorkAcceptanceActService {
  @Nullable UUID generate(WorkAcceptanceActRequest request);
}
