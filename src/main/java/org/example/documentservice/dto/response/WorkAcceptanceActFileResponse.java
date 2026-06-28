package org.example.documentservice.dto.response;

import lombok.Builder;

@Builder
public record WorkAcceptanceActFileResponse(
    String name,

    byte[] content
) {
}
