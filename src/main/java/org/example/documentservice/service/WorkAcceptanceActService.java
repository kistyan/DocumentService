package org.example.documentservice.service;

import org.example.documentservice.dto.request.WorkAcceptanceActRequest;
import org.example.documentservice.dto.response.WorkAcceptanceActFileResponse;
import org.example.documentservice.dto.response.WorkAcceptanceActResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface WorkAcceptanceActService {
  WorkAcceptanceActResponse generate(WorkAcceptanceActRequest request);

  WorkAcceptanceActFileResponse download(UUID id);
}
